package framework.db;

public class IdQueryConstraint extends QueryConstraints {

	public IdQueryConstraint(String idColumnName, int id) {
		super();
		this.add(new QueryConstraint(idColumnName, id));
	}
	public IdQueryConstraint(int id) {
		super();
		this.add(new QueryConstraint("ID", id));
	}
}
