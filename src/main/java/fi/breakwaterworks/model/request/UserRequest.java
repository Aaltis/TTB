package fi.breakwaterworks.model.request;

public class UserRequest {
	private String username;
	private String password;
	private String repassword;
	private String email;

	public UserRequest() {
	}
	
	public UserRequest(String name, String password, String repassword) {
		super();
		this.username = name;
		this.password = password;
		this.repassword = repassword;


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
	public String getRepassword() {
		return repassword;
	}

	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}
	
}
