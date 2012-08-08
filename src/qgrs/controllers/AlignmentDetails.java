package qgrs.controllers;

import org.jdom.Document;
import org.jdom.Element;

import qgrs.data.AlignmentRecord;
import qgrs.db.AlignedSequenceDb;
import qgrs.db.AlignmentRecordDb;
import qgrs.input.QParam;
import qgrs.model.QgrsWebContext;
import qgrs.view.XslViews;
import framework.web.AbstractController;
import framework.web.AbstractWebContext;
import framework.web.ModelView;
import framework.web.response.PageResponse;
import framework.web.response.Response;

public class AlignmentDetails extends AbstractController {

	public AlignmentDetails() {
		super();
		this.supportedUrls.add("/app/align-detail");
	}
	
	@Override
	public Response processRequest(AbstractWebContext context) {
		Document pageXml = new Document();
	    Element root = new Element("qgrs");
	    QgrsWebContext qContext = (QgrsWebContext)context;
	    AlignmentRecordDb db = new AlignmentRecordDb (qContext.getDbConnection());
	    AlignmentRecord ar = db.get(context.getString(QParam.AlignmentId));
	    AlignedSequenceDb seqDb = new AlignedSequenceDb(qContext.getDbConnection());
	    String seq1 = seqDb.get(ar.getId(), ar.getPrinciple());
	    String seq2 = seqDb.get(ar.getId(), ar.getComparison());
	    
	    root.addContent(new Element("alignmentId").setText(String.valueOf(ar.getId())));
	    root.addContent(new Element("principleSequence").setText(seq1));
	    root.addContent(new Element("comparisonSequence").setText(seq2));
	    
	    root.addContent(new Element("principleSequenceId").setText(ar.getPrinciple()));
	    root.addContent(new Element("comparisonSequenceId").setText(ar.getComparison()));
	    
	    db.close();
	    seqDb.close();
	    pageXml.addContent(root);
	    return new PageResponse(new ModelView(XslViews.AlignmentDetails, pageXml));	
	}

}
