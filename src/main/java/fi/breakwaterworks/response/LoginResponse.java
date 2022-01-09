package fi.breakwaterworks.response;

public class LoginResponse {
	private String token;

	public LoginResponse() {
		super();
	}

	public LoginResponse(String token) {
		this.setToken(token);
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}