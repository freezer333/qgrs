package qgrs.controllers;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import qgrs.compute.BuildKey;
import qgrs.data.GeneSequence;
import qgrs.db.AlignedSequenceDb;
import qgrs.db.AlignmentRecordDb;
import qgrs.db.DbMinimumValues;
import qgrs.db.GeneSequenceDb;
import qgrs.db.HomologyRecordDb;
import qgrs.db.QgrsDb;
import qgrs.db.records.AlignmentRecord;
import qgrs.db.records.GQuadruplexRecord;
import qgrs.db.records.QgrsHomologyRecord;
import qgrs.model.QgrsWebContext;
import qgrs.view.XslViews;
import framework.io.GZipper;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.response.PageResponse;
import framework.web.response.Response;

public class Populate extends AbstractController {

	public Populate() {
		super();
		this.supportedUrls.add("/app/populate");
	}

	final DbMinimumValues dbMinValue = new DbMinimumValues();
	
	private boolean accept(GQuadruplexRecord record) {
		return dbMinValue.acceptableGScore(record.getScore());
	}
	private boolean accept(QgrsHomologyRecord record){
		return dbMinValue.acceptableQgrsHomology(record.getOverallScore());	
	}
	
	
	@Override
	public Response processRequest(AbstractWebContext context) {
		QgrsWebContext qContext = (QgrsWebContext)context;
		try {
			AlignmentRecord record = null;
			
			GeneSequence principle = null;
			GeneSequence comparison = null;
			
			String principleAlignedSequence = null;
			String comparisonAlignedSequence = null;
			
			HashMap<String, GQuadruplexRecord> principleQgrs = null;
			HashMap<String, GQuadruplexRecord> comparisonQgrs = null;
			
			List<QgrsHomologyRecord> homologyRecords = null;
			
			boolean isMultipart = ServletFileUpload.isMultipartContent(context.getRequest());
			if ( !isMultipart ) throw new RuntimeException("invalid request"); 
			
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List items = upload.parseRequest(context.getRequest());
			Iterator iter = items.iterator();
			String xmlContent=null;
			while (iter.hasNext()) {
			    FileItem item = (FileItem) iter.next();
			    if (!item.isFormField()) {
			    	InputStream uploadedStream = item.getInputStream();
			    	GZipper zip = new GZipper();
					xmlContent = zip.inflate(uploadedStream); 
			        uploadedStream.close(); 
			    }
			}
			
			
			Reader in = new StringReader(xmlContent);

			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(in);
			
			Element ar = doc.getRootElement().getChild("alignment-record");
			record = new AlignmentRecord(ar);
			//System.out.println("Populating record:  " + record.getId());
			
			
					
			Element principleElement = doc.getRootElement().getChild("principle");
			Element pGene = principleElement.getChild("gene");
			principle = GeneSequence.buildFromXml(pGene);
			//System.out.println("  Loaded " + principle.getAccessionNumber());
			principleAlignedSequence = principleElement.getChild("alignment").getText();
			
			principleQgrs = new HashMap<String, GQuadruplexRecord>();
			for ( Object qElement : principleElement.getChild("quadruplexes").getChildren("quadruplex")) {
				GQuadruplexRecord r = new GQuadruplexRecord((Element)qElement);
				if ( this.accept(r)) {
					principleQgrs.put(r.getId(), r);
				}
			}
			//System.out.println("  Loaded " + principleQgrs.size() + " quadruplexes");
			
			
			Element comparisonElement = doc.getRootElement().getChild("comparison");
			Element cGene = comparisonElement.getChild("gene");
			comparison = GeneSequence.buildFromXml(cGene);
			//System.out.println("  Loaded " + comparison.getAccessionNumber());
			comparisonAlignedSequence = comparisonElement.getChild("alignment").getText();
			
			comparisonQgrs = new HashMap<String, GQuadruplexRecord>();
			for ( Object qElement : comparisonElement.getChild("quadruplexes").getChildren("quadruplex")) {
				GQuadruplexRecord r = new GQuadruplexRecord((Element)qElement);
				if ( this.accept(r) ) {
					comparisonQgrs.put(r.getId(), r);
				}
			}
			//System.out.println("  Loaded " + comparisonQgrs.size() + " quadruplexes");
			
			
			homologyRecords = new LinkedList<QgrsHomologyRecord>();
			for ( Object o : doc.getRootElement().getChild("homologyResults").getChildren("homology")) {
				QgrsHomologyRecord r = new QgrsHomologyRecord((Element)o);
				if ( principleQgrs.containsKey(r.getGq1Id()) && 
						comparisonQgrs.containsKey(r.getGq2Id()) &&
						this.accept(r)) {
					homologyRecords.add(r);
				}
			}
			//System.out.println("--Loaded " + homologyRecords.size() + " homology records");
			
			
			
			AlignmentRecordDb arDb = new AlignmentRecordDb (qContext.getDbConnection());
		    AlignedSequenceDb asDb = new AlignedSequenceDb(qContext.getDbConnection());
		    GeneSequenceDb geneDb = new GeneSequenceDb (qContext.getDbConnection());
		    QgrsDb qgrsDb = new QgrsDb(qContext.getDbConnection());
		    HomologyRecordDb hDb = new HomologyRecordDb(qContext.getDbConnection());
		    
		    if ( !arDb.has(record.getPrinciple(), record.getComparison())) {
		    	 
			    geneDb.put(principle);
			    geneDb.put(comparison);
			    
			    arDb.put(record);
			    asDb.put(record.getId(), principle.getAccessionNumber(), principleAlignedSequence);
			    asDb.put(record.getId(), comparison.getAccessionNumber(), comparisonAlignedSequence);
			    if (!qgrsDb.has(principle, BuildKey.QgrsIdentify) ) {
				    for ( GQuadruplexRecord qr : principleQgrs.values()) {
				    	qgrsDb.put(qr);
				    }
			    }
			    //else {
			    //	System.out.println("QGRS for " + principle.getAccessionNumber() + " already in database");
			    //}
			    if (!qgrsDb.has(comparison, BuildKey.QgrsIdentify) ) {
				    for ( GQuadruplexRecord qr : comparisonQgrs.values()) {
				    	qgrsDb.put(qr);
				    }
			    }
			    //else {
			    //	System.out.println("QGRS for " + comparison.getAccessionNumber() + " already in database");
			    //}
				for ( QgrsHomologyRecord hr : homologyRecords) {
					hDb.put(hr);
				}
			    //System.out.println("Persisted " + record.getId() + " to database");
		    }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		Document pageXml = new Document();
	    Element root = new Element("qgrs");
	    pageXml.addContent(root);
	    //System.out.println(context.get("xml"));
	    return new PageResponse(new ModelView(XslViews.Start, pageXml));
	}

}
