package fi.breakwaterworks.model.request;

import io.swagger.annotations.ApiParam;

public class MovementRequest {
    @ApiParam(value = "Movement name", required = false)
	private String name;
    @ApiParam(value = "Movement type", required = false)

	private String type;
	public MovementRequest() {
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
