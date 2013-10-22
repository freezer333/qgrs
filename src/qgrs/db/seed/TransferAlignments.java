package qgrs.db.seed;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;
import java.util.List;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import qgrs.data.GeneSequence;
import qgrs.data.mongo.primitives.jongo.AlignedSequence;
import qgrs.data.mongo.primitives.jongo.Alignment;
import qgrs.data.providers.RDBAlignmentProvider;
import qgrs.data.providers.RDBSequenceProvider;
import qgrs.data.providers.SequenceProvider;
import qgrs.data.records.AlignmentRecord;
import qgrs.db.AlignedSequenceDb;
import qgrs.db.AlignmentRecordDb;
import qgrs.db.AppProperties;
import qgrs.db.DatabaseConnection;
import qgrs.db.GeneSequenceDb;
import framework.db.DatabaseConnectionParameters;


/***
 * This script transfers to the data in the RDBMS GENE_A_SEQ table.  
 * 		In order to provide better query ability going forward, additional data has been 
 * 		added to the collection - the principal and comparison's gene name, species, and GI number
 *      are also included..
 *      
 *      The structure of each record are as follows:
 *      
 *      alignment {
 *           principal {
 *               accessionNumber:
 *               giNumber:
 *               name:
 *               species:
 *               alignedSequence: long string of bases, with gaps
 *           }
 *           comparison {
 *               accessionNumber
 *               giNumber
 *               name
 *               species:
 *               alignedSequence: long string of bases, with gaps
 *           }
 *           alignmentPercentage: 90 (integer for indexing purposes)
 *        	 
 * @author sfrees
 *
 * Note, once this is run, the new mongo alignment collection supercedes the RDBMS and there 
 * is no longer an need to re-run this, unless the DB is being rebuilt from an old data source.
 */
public class TransferAlignments {
	static DatabaseConnection connection;
	static GeneSequenceDb geneDb ;
	static AlignmentRecordDb alignmentdb ;
	static AlignedSequenceDb seqDb ;
	
	static {
		DatabaseConnectionParameters params  = new DatabaseConnectionParameters(AppProperties.getSeedCacheConnectionStringFromPropsxml(), "sa", "sa");
		connection = new DatabaseConnection(params);
		geneDb = new GeneSequenceDb(connection);
		alignmentdb = new AlignmentRecordDb (connection);
		seqDb = new AlignedSequenceDb (connection);
	}
	
	
	private static AlignedSequence makeAlignedSequence(GeneSequence seq, String data) {
		AlignedSequence retval = new AlignedSequence();
		retval.setAccessionNumber(seq.getAccessionNumber());;
		retval.setGeneName(seq.getGeneName());
		retval.setSymbol(seq.getGeneSymbol());
		retval.setGiNumber(seq.getGiNumber());
		retval.setSpecies(seq.getSpecies());;
		retval.setAlignedSequence(data);;
		return retval;
	}
	
	private static Alignment makeAlignment(AlignmentRecord ar ) {
		GeneSequence principal = geneDb.get(ar.getPrinciple());
		GeneSequence comparison = geneDb.get(ar.getComparison());
		String pSeq = seqDb.get(ar.getPrinciple()+"x"+ar.getComparison(), ar.getPrinciple());
		String cSeq = seqDb.get(ar.getPrinciple()+"x"+ar.getComparison(), ar.getComparison());
		
		Alignment alignment = new Alignment();
		alignment.setPrincipal(makeAlignedSequence(principal, pSeq));
		alignment.setComparison(makeAlignedSequence(comparison, cSeq));
		alignment.setAlignmentPercentage(Math.round(ar.getSimilarityPercentage()*100));
		return alignment;
	}
	
	public static void main(String[] args) throws UnknownHostException {
		DB db = new MongoClient().getDB("qgrs");
		Jongo jongo = new Jongo(db);
		MongoCollection alignments = jongo.getCollection("alignments");
		LinkedList<Alignment> toInsert = new LinkedList<Alignment>();
		List<AlignmentRecord> records = alignmentdb.getAll();
		int total = records.size();
		int i = 0;
		for ( AlignmentRecord ar : records ) {
			System.out.println(ar.getPrinciple() + " -> " + ar.getComparison() + " (" + i + " / " + total);
			i++;
			toInsert.add(makeAlignment(ar));
			
			if ( toInsert.size() >= 300 ) {
				System.out.println("Batch insert....");
				alignments.insert(toInsert.toArray());
				toInsert.clear();
			}
			
		}
		if ( toInsert.size() > 0 ) {
			System.out.println("Final batch insert....");
			alignments.insert(toInsert.toArray());
		}
		seqDb.close();
		alignmentdb.close();
		geneDb.close();
	}

}
