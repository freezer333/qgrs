package qgrs.db.tasks;

import qgrs.db.AppProperties;
import framework.db.DatabaseConnectionParameters;
import framework.web.ResourceResolver;
import framework.web.authentication.Role;
import framework.web.authentication.User;
import framework.web.authentication.UserDb;

public class AddUsers extends AbstractDbTask {

	public AddUsers() {
		super(new DatabaseConnectionParameters(AppProperties.getUserDbConnectionStringFromPropsxml(), "sa", "sa"));
	}
	public AddUsers (ResourceResolver r) {
		super(new DatabaseConnectionParameters(AppProperties.getUserDbConnectionString(r), "sa", "sa"));
		
	}
	
	public static void main ( String [] args) {
		AddUsers task = new AddUsers();
		UserDb db = new UserDb(task.getConnection());
		Role r = new Role("user");
		adduser(db, "sfrees", r);
		adduser(db, "pbagga", r);
		adduser(db, "cmenende", r);
		adduser(db, "mcrum1", r);
		adduser(db, "emurray1", r);
	}
	
	private static void adduser(UserDb db, String username, Role r) {
		User user = new User();
		user.setUsername(username);
		user.getRoles().add(r);
		db.addUser(user, username);
	}

	@Override
	public void report() {
		// TODO Auto-generated method stub
		
	}
}
