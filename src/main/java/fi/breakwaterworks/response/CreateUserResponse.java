package fi.breakwaterworks.response;

public class CreateUserResponse {
	String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String status) {
		this.message = status;
	}

	public CreateUserResponse(String status) {
		this.message = status;

	}
}