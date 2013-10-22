package qgrs.data.providers;

import java.util.HashMap;

import org.biojavax.bio.db.ncbi.GenbankRichSequenceDB;
import org.biojavax.bio.seq.RichSequence;

import qgrs.data.Range;
import qgrs.data.SequenceFeatureAdapter;
import qgrs.input.GenbankRichSequenceTextDB;
import qgrs.input.OntologyLoader;



public abstract class SequenceProvider {

	public enum Key { 
		Sequence, 
		Accession, 
		Version, 
		GiNumber, 
		Symbol, 
		Name, 
		Species, 
		PolyASignals, 
		PolyASites, 
		Cds, 
		UTR5, 
		UTR3, 
		OntologyData, 
		Live
	};
	
		
	// network sources - ncbi has gene data, go database has ontology data
	private GenbankRichSequenceDB ncbi = new GenbankRichSequenceTextDB();
	private OntologyLoader olo = new OntologyLoader();
	
	protected abstract HashMap<Key, Object> getCachedSequence(String accessionOrGi) ;
	
	public HashMap<Key, Object> getSequence(String accessionOrGi) {
		/*System.out.println("Sequence data requested -> " + accessionOrGi);*/
		HashMap<Key, Object> values = this.getCachedSequence(accessionOrGi);
		
		if ( values == null ) {
			if( this.allowLiveDownload() ) {
				values = this.getLiveSequence(accessionOrGi);
				values.put(Key.Live, true);
			}
			else {
				System.out.println("\t Live download not available with this provider");
			}
		}
		else {
			/*System.out.println("\t [Cached Version]");*/
			values.put(Key.Live, false);
		}
		return values;
	}

	public void close() {
		olo.shutdown();
	}
	
	private HashMap<Key, Object> getLiveSequence(String accessionOrGi) {
		/*System.out.println("\t [Live Version required (no cache)]");*/
		RichSequence rs;
		try {
			rs = ncbi.getRichSequence(accessionOrGi);
		} catch (Exception e) {
			System.out.println("\t\tProvider failure:  Connection to NCBI failed");
			e.printStackTrace();
			
			return null;
		}
		if ( rs == null ) {
			System.out.println("\t\tProvider failure:  Sequence was not found");
			return null;
		}
		
		return fillSequenceData(rs);
	}
	
	private HashMap<Key, Object> fillSequenceData(RichSequence rs) {
		HashMap<Key, Object> values = new HashMap<Key, Object>();
		SequenceFeatureAdapter featuresAdapter = new SequenceFeatureAdapter(rs);
		values.put(Key.Sequence, rs.seqString());
		values.put(Key.Accession, rs.getAccession());
		values.put(Key.Version, rs.getVersion());
		values.put(Key.GiNumber, rs.getIdentifier());
		values.put(Key.Symbol, featuresAdapter.getGeneSymbol().toUpperCase());
		values.put(Key.Name, rs.getDescription());
		values.put(Key.Species, rs.getTaxon() == null ? "" : rs.getTaxon().getDisplayName());
		values.put(Key.PolyASignals,  featuresAdapter.getPolyASignals());
		Range cds =  new Range(featuresAdapter.getCDSStart(), featuresAdapter.getCDSEnd());
		values.put(Key.Cds, cds);
		values.put(Key.UTR5, new Range(1, cds.getStart()-1));
		values.put(Key.UTR3, new Range(cds.getEnd()+1, rs.seqString().length()));
		values.put(Key.PolyASites, featuresAdapter.getPolyASites());
		fillOntologyDataFromNetwork(values);
		/*System.out.println("\t\tLive version downloaded successfully");*/
		return values;
	}
	
	private void fillOntologyDataFromNetwork(HashMap<Key, Object> values) {
		String accessionNumber = values.get(Key.Accession) + "." + values.get(Key.Version);
		values.put(Key.OntologyData, olo.getOntologyData(accessionNumber));
	}
	
	protected boolean allowLiveDownload() {
		return true;
	}
	
	
}
