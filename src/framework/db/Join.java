package framework.db;

public class Join {

	private final String leftTableName;
	private final String rightTableName;
	private final String leftKey;
	private final String rightKey;
	
	public Join(String leftTableName, String leftKey, String rightTableName, 
			String rightKey) {
		super();
		this.leftTableName = leftTableName;
		this.rightTableName = rightTableName;
		this.leftKey = leftKey;
		this.rightKey = rightKey;
	}
	
	public String getLeftTableName() {
		return leftTableName;
	}
	public String getRightTableName() {
		return rightTableName;
	}
	public String getLeftKey() {
		return leftKey;
	}
	public String getRightKey() {
		return rightKey;
	}
	
	public String getJoinExpression() {
		return "LEFT JOIN " + rightTableName+ " ON " + leftTableName + "." + leftKey + 
				" = " + rightTableName + "." + rightKey; 
	}
	
	
	
	
}
