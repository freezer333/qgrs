package qgrs.db.tasks;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddKeys extends AbstractDbTask {

	private boolean keyExists(String table) throws SQLException {
		String q = "SELECT * " +
				"FROM INFORMATION_SCHEMA.CONSTRAINTS " +
				"WHERE CONSTRAINT_TYPE = 'PRIMARY KEY' "+
				"AND TABLE_NAME = '" + table + 
				"' AND TABLE_SCHEMA ='PUBLIC'";
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(q);
		boolean retval = rs.next();
		stmt.close();
		return retval;
				
	}
	private boolean foreightKeyExists(String table, String column) throws SQLException {
		String q = "SELECT * " +
				"FROM INFORMATION_SCHEMA.CONSTRAINTS " +
				"WHERE CONSTRAINT_TYPE = 'REFERENTIAL' "+
				"AND TABLE_NAME = '" + table +
				"' AND COLUMN_LIST = '" + column +
				"' AND TABLE_SCHEMA ='PUBLIC'";
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(q);
		boolean retval = rs.next();
		stmt.close();
		return retval;
				
	}
	public void execute() throws Exception{
		
		String q;
		
		if ( !keyExists("QGRS") ) {
			System.out.println("Creating primary key for QGRS");
			q = "ALTER TABLE QGRS ALTER COLUMN ID  varchar(255) NOT NULL";
			this.executeUpdate(q);
			q = "ALTER TABLE QGRS ADD PRIMARY KEY (ID)";
			this.executeUpdate(q);
		}
		
		if ( !keyExists("GENE") ) {
			System.out.println("Creating primary key for GENE");
			q = "ALTER TABLE GENE  ALTER COLUMN ACCESSIONNUMBER  varchar(50) NOT NULL";
			this.executeUpdate(q);
			q = "ALTER TABLE GENE ADD PRIMARY KEY (ACCESSIONNUMBER)";
			this.executeUpdate(q);
		}
		
		if ( !keyExists("GENE_A") ) {
			System.out.println("Creating primary key for GENE_A");
			q = "ALTER TABLE GENE_A  ALTER COLUMN ID  varchar(255) NOT NULL";
			this.executeUpdate(q);
			q = "ALTER TABLE GENE_A ADD PRIMARY KEY (ID)";
			this.executeUpdate(q);
		}
		
		if ( !keyExists("QGRS_H") ) {
			System.out.println("Creating primary key for QGRS_H");
			q = "ALTER TABLE QGRS_H  ALTER COLUMN ID  INTEGER NOT NULL";
			this.executeUpdate(q);
			q = "ALTER TABLE QGRS_H ADD PRIMARY KEY (ID)";
			this.executeUpdate(q);
		}
		
		if ( !keyExists("GENE_A_SEQ") ) {
			System.out.println("Creating primary key for GENE_A_SEQ");
			q = "ALTER TABLE GENE_A_SEQ  ALTER COLUMN ID varchar(255) NOT NULL";
			this.executeUpdate(q);
			q = "ALTER TABLE GENE_A_SEQ  ALTER COLUMN ACCESSIONNUMBER varchar(255) NOT NULL";
			this.executeUpdate(q);
			q = "ALTER TABLE GENE_A_SEQ ADD PRIMARY KEY (ID, ACCESSIONNUMBER)";
			this.executeUpdate(q);
		}
		
		if ( !keyExists("GO") ) {
			System.out.println("Creating primary key for GO");
			q = "ALTER TABLE GO  ALTER COLUMN ACCESSIONNUMBER varchar(255) NOT NULL";
			this.executeUpdate(q);
			q = "ALTER TABLE GO  ALTER COLUMN GOTERM varchar(255) NOT NULL";
			this.executeUpdate(q);
			q = "ALTER TABLE GO  ALTER COLUMN GOTYPE varchar(255) NOT NULL";
			this.executeUpdate(q);
			q = "ALTER TABLE GO ADD PRIMARY KEY (ACCESSIONNUMBER, GOTERM, GOTYPE)";
			this.executeUpdate(q);
		}
		
		System.out.println("Primary keys added");
		
		if ( !this.foreightKeyExists("QGRS_H", "GQ1ID") ){
			System.out.println("Adding Foreign Key QGRS_H.GQ1ID -> QGRS.ID");
			q = "ALTER TABLE QGRS_H ALTER COLUMN GQ1ID varchar(255) NOT NULL";
			this.executeUpdate(q);
			q = "ALTER TABLE QGRS_H ADD FOREIGN KEY (GQ1ID) REFERENCES QGRS(ID)";
			this.executeUpdate(q);
		}
		if ( !this.foreightKeyExists("QGRS_H", "GQ2ID") ){
			System.out.println("Adding Foreign Key QGRS_H.GQ2ID -> QGRS.ID");
			q = "ALTER TABLE QGRS_H ALTER COLUMN GQ2ID varchar(255) NOT NULL";
			this.executeUpdate(q);
			q = "ALTER TABLE QGRS_H ADD FOREIGN KEY (GQ2ID) REFERENCES QGRS(ID)";
			this.executeUpdate(q);
		}
		
		if ( !this.foreightKeyExists("GENE_A", "PRINCIPLE") ){
			System.out.println("Adding Foreign Key GENE_A.PRINCIPLE -> GENE.ACCESSIONNUMBER");
			q = "ALTER TABLE GENE_A ALTER COLUMN PRINCIPLE varchar(255) NOT NULL";
			this.executeUpdate(q);
			q = "ALTER TABLE GENE_A ADD FOREIGN KEY (PRINCIPLE) REFERENCES GENE(ACCESSIONNUMBER)";
			this.executeUpdate(q);
		}
		
		if ( !this.foreightKeyExists("GENE_A_SEQ", "ACCESSIONNUMBER") ){
			System.out.println("Adding Foreign Key GENE_A_SEQ.ACCESSIONNUMBER -> GENE.ACCESSIONNUMBER");
			q = "ALTER TABLE GENE_A_SEQ ALTER COLUMN ACCESSIONNUMBER varchar(255) NOT NULL";
			this.executeUpdate(q);
			q = "ALTER TABLE GENE_A_SEQ ADD FOREIGN KEY (ACCESSIONNUMBER) REFERENCES GENE(ACCESSIONNUMBER)";
			this.executeUpdate(q);
		}
		
		if ( !this.foreightKeyExists("GENE_A_SEQ", "ID") ){
			System.out.println("Adding Foreign Key GENE_A_SEQ.ID -> GENE_A.ID");
			q = "ALTER TABLE GENE_A_SEQ ALTER COLUMN ID varchar(255) NOT NULL";
			this.executeUpdate(q);
			q = "ALTER TABLE GENE_A_SEQ ADD FOREIGN KEY (ID) REFERENCES GENE_A(ID)";
			this.executeUpdate(q);
		}
		
		if ( !this.foreightKeyExists("GO", "ACCESSIONNUMBER") ){
			System.out.println("Adding Foreign Key GO.ACCESSIONNUMBER -> GENE.ACCESSIONNUMBER");
			q = "ALTER TABLE GO ALTER COLUMN ACCESSIONNUMBER varchar(255) NOT NULL";
			this.executeUpdate(q);
			q = "ALTER TABLE GO ADD FOREIGN KEY (ACCESSIONNUMBER) REFERENCES GENE(ACCESSIONNUMBER)";
			this.executeUpdate(q);
		}
		
		if ( !this.foreightKeyExists("QGRS", "GENEID") ){
			System.out.println("Adding Foreign Key QGRS.GENEID -> GENE.ACCESSIONNUMBER");
			q = "ALTER TABLE QGRS ALTER COLUMN GENEID varchar(255) NOT NULL";
			this.executeUpdate(q);
			q = "ALTER TABLE QGRS ADD FOREIGN KEY (GENEID) REFERENCES GENE(ACCESSIONNUMBER)";
			this.executeUpdate(q);
		}
		
		System.out.println("Foreign keys added");
		conn.close();
	}



	@Override
	public void report() {
		// TODO Auto-generated method stub
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		new AddKeys().execute();
	}

}
