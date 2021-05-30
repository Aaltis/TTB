package fi.breakwaterworks.model.request;

public class UserRequest {
	private String username;
	private String email;
	private String password;
	
	public UserRequest() {
	}
	
	public UserRequest(String name, String password, String email) {
		super();
		this.username = name;
		this.email = email;
		this.password = password;
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String name) {
		this.username = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

	

}
