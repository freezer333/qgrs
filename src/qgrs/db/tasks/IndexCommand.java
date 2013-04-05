package qgrs.db.tasks;

class IndexCommand {
	public final String name;
	public final String table;
	public final String column;
	public IndexCommand(String name, String table, String column) {
		super();
		this.name = name;
		this.table = table;
		this.column = column;
	}
	
	public String createSql() {
		return "CREATE INDEX IF NOT EXISTS " + name + " ON " + table + " (" + column + ")";
	}
	
	public String dropSql() {
		return "DROP INDEX IF EXISTS " + name;
	}
	
	
	
	
}