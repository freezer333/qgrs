package qgrs.controllers;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
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
import qgrs.data.records.AlignmentRecord;
import qgrs.data.records.GQuadruplexRecord;
import qgrs.data.records.QgrsHomologyRecord;
import qgrs.db.AlignedSequenceDb;
import qgrs.db.AlignmentRecordDb;
import qgrs.db.DbMinimumValues;
import qgrs.db.GeneSequenceDb;
import qgrs.db.HomologyRecordDb;
import qgrs.db.QgrsDb;
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
					
			Element principleElement = doc.getRootElement().getChild("principle");
			Element pGene = principleElement.getChild("gene");
			principle = GeneSequence.buildFromXml(pGene);
			principleAlignedSequence = principleElement.getChild("alignment").getText();
			
			principleQgrs = new HashMap<String, GQuadruplexRecord>();
			for ( Object qElement : principleElement.getChild("quadruplexes").getChildren("quadruplex")) {
				GQuadruplexRecord r = new GQuadruplexRecord((Element)qElement);
				if ( this.accept(r)) {
					principleQgrs.put(r.getId(), r);
				}
			}
			
			
			Element comparisonElement = doc.getRootElement().getChild("comparison");
			Element cGene = comparisonElement.getChild("gene");
			comparison = GeneSequence.buildFromXml(cGene);
			comparisonAlignedSequence = comparisonElement.getChild("alignment").getText();
			
			comparisonQgrs = new HashMap<String, GQuadruplexRecord>();
			for ( Object qElement : comparisonElement.getChild("quadruplexes").getChildren("quadruplex")) {
				GQuadruplexRecord r = new GQuadruplexRecord((Element)qElement);
				if ( this.accept(r) ) {
					comparisonQgrs.put(r.getId(), r);
				}
			}
			
			
			homologyRecords = new LinkedList<QgrsHomologyRecord>();
			for ( Object o : doc.getRootElement().getChild("homologyResults").getChildren("homology")) {
				QgrsHomologyRecord r = new QgrsHomologyRecord((Element)o);
				if ( principleQgrs.containsKey(r.getGq1Id()) && 
						comparisonQgrs.containsKey(r.getGq2Id()) &&
						this.accept(r)) {
					homologyRecords.add(r);
				}
			}
			
			AlignmentRecordDb arDb = new AlignmentRecordDb (qContext.getDbConnection());
		    AlignedSequenceDb asDb = new AlignedSequenceDb(qContext.getDbConnection());
		    GeneSequenceDb geneDb = new GeneSequenceDb (qContext.getDbConnection());
		    QgrsDb qgrsDb = new QgrsDb(qContext.getDbConnection());
		    HomologyRecordDb hDb = new HomologyRecordDb(qContext.getDbConnection());
		    
		    HashSet<String> qgrsIds;
		    
			qContext.getDbConnection().getConnection().setAutoCommit(false);
			try {
			    if ( !arDb.has(record.getPrinciple(), record.getComparison())) {
			    	 
				    geneDb.put(principle);
				    geneDb.put(comparison);
				    
				    arDb.put(record);
				    asDb.put(record.getId(), principle.getAccessionNumber(), principleAlignedSequence);
				    asDb.put(record.getId(), comparison.getAccessionNumber(), comparisonAlignedSequence);
				    qgrsIds = qgrsDb.getAllIds(principle);
				    for ( GQuadruplexRecord qr : principleQgrs.values()) {
				    	if ( !qgrsIds.contains(qr.getId())){
				    		qgrsDb.put(qr);
				    	}
					}
				    qgrsIds = qgrsDb.getAllIds(comparison);
				    for ( GQuadruplexRecord qr : comparisonQgrs.values()) {
				    	if ( !qgrsIds.contains(qr.getId())){
				    		qgrsDb.put(qr);
				    	}
					}
				    
				    for ( QgrsHomologyRecord hr : homologyRecords) {
						hDb.put(hr);
					}
			    }
			    qContext.getDbConnection().getConnection().commit();
			}
			catch (SQLException sqlEx) {
				sqlEx.printStackTrace();
				qContext.getDbConnection().getConnection().rollback();
			}
			finally {
				qContext.getDbConnection().getConnection().setAutoCommit(true);
				arDb.close();
				asDb.close();
				geneDb.close();
				qgrsDb.close();
				hDb.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
		Document pageXml = new Document();
	    Element root = new Element("qgrs");
	    pageXml.addContent(root);
	    //System.out.println(context.get("xml"));
	    return new PageResponse(new ModelView(XslViews.Start, pageXml));
	}

}
