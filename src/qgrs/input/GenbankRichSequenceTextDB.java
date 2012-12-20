package qgrs.input;
import java.net.MalformedURLException;
import java.net.URL;

import org.biojava.bio.seq.db.FetchURL;
import org.biojavax.bio.db.RichSequenceDBLite;
import org.biojavax.bio.db.ncbi.GenbankRichSequenceDB;



public class GenbankRichSequenceTextDB extends GenbankRichSequenceDB implements RichSequenceDBLite {
    
	@Override
    protected URL getAddress(String id) throws MalformedURLException {
        FetchURL seqURL = new FetchURL("Genbank", "text");
        String baseurl = seqURL.getbaseURL();
        String db = seqURL.getDB();
        String url = baseurl+db+"&id="+id+"&rettype=gb&retmode=text";
        return new URL(url);
    }
    
    
}
