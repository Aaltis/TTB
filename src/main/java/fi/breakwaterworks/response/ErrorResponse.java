package fi.breakwaterworks.response;

public class ErrorResponse {
	private String errorMessage;

	public ErrorResponse() {
		super();
	}
	public ErrorResponse(String errormessage) {
		this.setErrorMessage(errormessage);
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	public void setErrorMessage(String errormessage) {
		this.errorMessage = errormessage;
	}
}
