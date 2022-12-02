package fi.breakwaterworks.controller;

import io.swagger.annotations.ApiParam;

public class WorkoutRequest {
    @ApiParam(value = "WorkoutId", required = false)
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
 
}

