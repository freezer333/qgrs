package qgrs.data.providers;

import java.util.HashMap;

import framework.web.util.StringUtils;
import qgrs.data.GeneSequence;
import qgrs.data.Range;
import qgrs.data.SequenceFeatureAdapter;
import qgrs.data.providers.SequenceProvider.Key;
import qgrs.db.AlignedSequenceDb;
import qgrs.db.DatabaseConnection;
import qgrs.db.GeneSequenceDb;


/** 
 * This is an adapter to pull sequence data from the older relational database
 * This adapter will become obsolete once we move away from the relational database
 * It relies on code in the older GeneSequenceDb class, which will also be obsolete
 * when the transition is completed.
 * @author sfrees
 *
 */
public class RDBSequenceProvider extends SequenceProvider{

	
	private final DatabaseConnection connection;
	
	public RDBSequenceProvider (DatabaseConnection connection) {
		this.connection = connection;
	}
	
	@Override
	protected HashMap<Key, Object> getCachedSequence(String accessionOrGi) {
		GeneSequenceDb db = new GeneSequenceDb(connection);
		
		GeneSequence gene = db.get(accessionOrGi);
		if ( gene == null ) return null;
		
		// Note that its somewhat redundant to create a GeneSequence, only to split it into a map, which
		// most likely will be recombined in to a GeneSequence.  This is an ADAPTER - it is transitional.
		// This code should be removed once GeneSequenceDb and all the relational DB code goes away - so 
		// its being completely reused, ignoring performance, here.
		
		// Need to pull in the actual sequence data, which would be in the GENE-A table
		AlignedSequenceDb seqDb = new AlignedSequenceDb(connection);
		String sequence = seqDb.getPureSequence(accessionOrGi);
		if ( !StringUtils.isDefined(sequence)) {
			return null;// can't use the gene, we don't have the sequence
		}
		gene.buildBases(sequence);
		
		db.close();
		seqDb.close();
		
		HashMap<Key, Object> values = new HashMap<Key, Object>();
		values.put(Key.Sequence, sequence);
		values.put(Key.Accession, gene.getAccessionNumber().split("\\.")[0]);
		values.put(Key.Version, gene.getAccessionNumber().split("\\.")[1]);
		values.put(Key.GiNumber, gene.getGiNumber());
		values.put(Key.Symbol, gene.getGeneSymbol());
		values.put(Key.Name, gene.getGeneName());
		values.put(Key.Species, gene.getSpecies());
		values.put(Key.PolyASignals,  gene.getPolyASignals());
		values.put(Key.Cds, gene.getCds());
		values.put(Key.UTR5, new Range(1, gene.getCds().getStart()-1));
		values.put(Key.UTR3, new Range(gene.getCds().getEnd()+1, sequence.length()));
		values.put(Key.PolyASites, gene.getPolyASites());
		values.put(Key.OntologyData, gene.getOntologyData());
		return values;
		
	}

}
