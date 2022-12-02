package fi.breakwaterworks.model.request;

import fi.breakwaterworks.model.Exercise;
import io.swagger.annotations.ApiParam;

public class AddExerciseRequest {
    @ApiParam(value = "Workout Id At Server", required = false)
	private long serverWorkoutId;    
    private Exercise exercise;
    
	public long getServerWorkoutId() {
		return serverWorkoutId;
	}
	public void setServerWorkoutId(long serverWorkoutId) {
		this.serverWorkoutId = serverWorkoutId;
	}	
	public Exercise getExercise() {
		return exercise;
	}
	public void setExercise(Exercise exercise) {
		this.exercise = exercise;
	}
}
