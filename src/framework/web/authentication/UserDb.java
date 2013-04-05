package framework.web.authentication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserDb {
	private final Connection c;
	
	public UserDb(Connection con) {
		c = con;
	}
	
	public boolean addUser(User user, String plainTextPassword) {
		String sql = "insert into user (username, password) values (?,?)";
		try {
			initializeTables(c);
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, user.getUsername());
			ps.setString(2, this.encryptPassword(plainTextPassword));
			ps.execute();
			ps.close();
			
			for ( Role r : user.getRoles() ) {
				this.insert_safely(r);
				this.link(user.getUsername(), r);
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	private void link(String username, Role role) throws Exception{
		String insert = "insert into userrole (username, role) values(?, ?)";
		PreparedStatement ps = c.prepareStatement(insert);
		ps.setString(1, username);
		ps.setString(2, role.getDisplay()) ;
		ps.execute();
		ps.close();
	}
	private void insert_safely(Role role) {
		try {
			String select = "select * from role where display=?";
			String insert = "insert into role (display) values (?)";
			PreparedStatement selectPs = c.prepareStatement(select);
			selectPs.setString(1, role.getDisplay());
			if ( selectPs.executeQuery().next()) {
				selectPs.close();
				return;
			}
			PreparedStatement insertPs = c.prepareStatement(insert);
			insertPs.setString(1, role.getDisplay());
			insertPs.execute();
			insertPs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public User authenticate (String username, String plainTextPassword) {
		PreparedStatement ps;
		try {
			String sql = "select * from user where user.username=? and user.password=?";
			ps = c.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, encryptPassword(plainTextPassword));
			ResultSet rs = ps.executeQuery();
			User u = null;
			if ( rs.next() ) {
				u = new User();
				u.setUsername(rs.getString("username"));
			}
			ps.close();
			
			if ( u != null  ){
				String rsql = "select role.display from userrole join role on userrole.role = role.display where userrole.username=?";
				ps = c.prepareStatement(rsql);
				ps.setString(1, username);
				rs = ps.executeQuery();
				while ( rs.next() ) {
					Role r = new Role();
					r.setDisplay(rs.getString("role.display"));
					u.getRoles().add(r);
				}
				ps.close();
			}
			
			return u;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	private String encryptPassword(String plainText) {
		return org.apache.commons.codec.digest.DigestUtils.sha256Hex(plainText +"qgrs");
	}
	
	public void resetPassword(User user, String newPlainTextPassword) {
		return ;
	}
	
	public static void initializeTables(Connection connection) throws Exception {
		String user = "create table if not exists user (username varchar(250) primary key, password varchar(1024))";
		String role = "create table if not exists role (display varchar(1024))";
		String userrole = "create table if not exists userrole (username varchar(250), role varchar(1024), primary key ( username, role ))";
		Statement s = connection.createStatement();
		try {
			s.execute(user);
			s.execute(role);
			s.execute(userrole);
		}
		finally {
			s.close();
		}
	}
}
