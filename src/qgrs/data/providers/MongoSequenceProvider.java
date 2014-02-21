package qgrs.data.providers;

import java.net.UnknownHostException;
import java.util.HashMap;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import qgrs.data.Range;
import qgrs.data.mongo.primitives.jongo.Alignment;
import qgrs.data.mongo.primitives.jongo.Homolog;
import qgrs.data.mongo.primitives.jongo.MRNA;

import com.mongodb.DB;
import com.mongodb.MongoClient;


public class MongoSequenceProvider extends SequenceProvider{
	private MongoCollection principals;
	private MongoCollection alignments;
	
	public MongoSequenceProvider () throws UnknownHostException {
		try {
			DB db = new MongoClient().getDB("qgrs");
			Jongo jongo = new Jongo(db);
			principals = jongo.getCollection("principals");
			alignments = jongo.getCollection("alignments");
		}
		catch (Exception e ) {
			System.err.println("Error connecting to MongoDB qgrs database");
			principals = null;
			alignments = null;
		}
	}
	
	public MongoSequenceProvider (Jongo jongo) throws UnknownHostException {
		try {
			principals = jongo.getCollection("principals");
			alignments = jongo.getCollection("alignments");
		}
		catch (Exception e ) {
			System.err.println("Error connecting to MongoDB qgrs database");
			principals = null;
			alignments = null;
		}
	}
	
	
	public String getPrincipalByAccession(String accession) {
		Alignment alignment = this.alignments.findOne("{principal.accessionNumber:#}", accession).as(Alignment.class);
		if ( alignment == null ) return null;
		String sequence = alignment.getPrincipal().getAlignedSequence();
		sequence = sequence.replaceAll("-", "");
		return sequence;
	}
	
	
	private MRNA getAsPrincipalMRNA(String accessionOrGi) {
		try {
			MRNA mrna = this.principals.findOne("{accessionNumber:#}", accessionOrGi).as(MRNA.class);
			if ( mrna == null  ){
				mrna = this.principals.findOne("{giNumber:#}", accessionOrGi).as(MRNA.class);
			}
			return mrna;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	private MRNA getAsComparisonMRNA(String accessionOrGi) {
		try {
			MRNA mrna = this.principals.findOne("{homologs:  {$elemMatch: {mrna.accessionNumber:#}}}", accessionOrGi).as(MRNA.class);
												 
			if ( mrna == null  ){
				mrna = this.principals.findOne("{homologs: {$elemMatch: {mrna.giNumber:#}}}", accessionOrGi).as(MRNA.class);
			}
			if ( mrna == null ) return null;
			
			for ( Homolog comparison : mrna.getHomologs() ) {
				if ( comparison.getMrna().getAccessionNumber().equals(accessionOrGi) || comparison.getMrna().getGiNumber().equals(accessionOrGi)) {
					return comparison.getMrna();
				}
			}
			return null;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	protected HashMap<Key, Object> getCachedSequence(String accessionOrGi) {
		if ( this.principals == null || this.alignments == null ) return null;
		MRNA mrna = getAsPrincipalMRNA(accessionOrGi) ;
		String sequence;
		if ( mrna != null ) {
			// principal
			Alignment alignment = this.alignments.findOne("{principal.accessionNumber:#}", mrna.getAccessionNumber()).as(Alignment.class);
			if ( alignment == null ) return null;
			sequence = alignment.getPrincipal().getAlignedSequence();
		}
		else {
			mrna = getAsComparisonMRNA(accessionOrGi);
			if ( mrna == null ) return null;
			Alignment alignment = this.alignments.findOne("{comparison.accessionNumber:#}", mrna.getAccessionNumber()).as(Alignment.class);
			if ( alignment == null ) return null;
			sequence = alignment.getComparison().getAlignedSequence();
		}
		
		sequence = sequence.replaceAll("-", "");
		HashMap<Key, Object> values = new HashMap<Key, Object>();
		values.put(Key.Sequence, sequence);
		values.put(Key.Accession, mrna.getAccessionNumber().split("\\.")[0]);
		values.put(Key.Version, mrna.getAccessionNumber().split("\\.")[1]);
		values.put(Key.GiNumber, mrna.getGiNumber());
		values.put(Key.Symbol, mrna.getSymbol());
		values.put(Key.Name, mrna.getName());
		values.put(Key.Species, mrna.getSpecies());
		values.put(Key.PolyASignals,  mrna.getPolyASignals());
		values.put(Key.Cds, mrna.getCds());
		values.put(Key.UTR5, new Range(1, mrna.getCds().getStart()-1));
		values.put(Key.UTR3, new Range(mrna.getCds().getEnd()+1, sequence.length()));
		values.put(Key.PolyASites, mrna.getPolyASites());
		values.put(Key.OntologyData, mrna.getOntologyData());
		return values;
		
	}

}
