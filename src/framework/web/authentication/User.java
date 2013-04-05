package framework.web.authentication;

import java.util.Collection;
import java.util.LinkedList;

public class User {

	private String username;
	
	private Collection<Role> roles = new LinkedList<Role>();

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

}
