package qgrs.compute;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import qgrs.data.GQuadruplex;
import qgrs.data.GeneSequence;
import qgrs.data.QgrsHomology;
import qgrs.db.records.QgrsHomologyRecord;
import qgrs.job.CancelFlag;

public class CachedHomologyScorer implements QgrsHomologyScorer {

	private List<QgrsHomology> similarityResults;
	
	public CachedHomologyScorer (GeneSequencePair pair, List<QgrsHomologyRecord> records ) {
		this.similarityResults = new LinkedList<QgrsHomology>();
		HashMap<String, GQuadruplex> pMap = buildQuadruplexMap(pair.getPrinciple());
		HashMap<String, GQuadruplex> cMap = buildQuadruplexMap(pair.getComparison());
		HashMap<String, Integer> pIMap = buildQuadruplexIndexMap(pair.getPrinciple());
		HashMap<String, Integer> cIMap = buildQuadruplexIndexMap(pair.getComparison());
		for ( QgrsHomologyRecord r : records ) {
			GQuadruplex pQuad = pMap.get(r.getGq1Id());
			GQuadruplex cQuad = cMap.get(r.getGq2Id());
			if (pIMap.containsKey(r.getGq1Id()) && cIMap.containsKey(r.getGq2Id())) {
				int pNum = pIMap.get(r.getGq1Id());
				int cNum = cIMap.get(r.getGq2Id());
				similarityResults.add(new QgrsHomology(pNum, pQuad, cNum, cQuad, r));
			}
			else {
				System.out.println("Missing " + r.getGq1Id() + " or " +r.getGq2Id() );
			}
		}
	}
	
	private HashMap<String, Integer> buildQuadruplexIndexMap(GeneSequence seq) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		int i = 0;
		for (GQuadruplex gq : seq.getgQuads() ) {
			map.put(gq.getId(), i);
			i++;
		}
		return map;
		
	}
	private HashMap<String, GQuadruplex> buildQuadruplexMap(GeneSequence seq) {
		HashMap<String, GQuadruplex> map = new HashMap<String, GQuadruplex>();
		for (GQuadruplex gq : seq.getgQuads() ) {
			map.put(gq.getId(), gq);
		}
		return map;
	}
	
	
	@Override
	public void setCancelFlag(CancelFlag flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void alignGQuadruplexes(GeneSequencePair pair) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public List<QgrsHomology> getSimilarityResults() {
		
		return similarityResults;
	}

}
