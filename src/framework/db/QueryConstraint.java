package framework.db;



public class QueryConstraint {

	private String name;
	private Object value;
	private String operand = "=";
	
	
	public QueryConstraint(String name, Object value) {
		this.name = name;
		this.value = value;
	}
	public QueryConstraint(String name, Object value, String operand) {
		this.name = name;
		this.value = value;
		this.operand = operand;
	}

	public String getOperand() {
		return operand;
	}
	public String getName() {
		return name;
	}
	public Object getValue() {
		return value;
	}
}
