package framework.web.authentication;

public class Role {
	
	public static final Role Any = new Role("Any");
	
	String display;
	
	public Role() {
		
	}
	public Role (String role) {
		this.display = role;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((display == null) ? 0 : display.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (display == null) {
			if (other.display != null)
				return false;
		} else if (!display.equals(other.display))
			return false;
		return true;
	}
	
	
	
}
