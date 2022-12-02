package fi.breakwaterworks.response;

import java.util.List;

public class WorkoutResponse {
	private List<WorkoutJson> workouts;

	public WorkoutResponse() {
		super();
	}

	public WorkoutResponse(List<WorkoutJson> workouts) {
		this.workouts = workouts;
	}

	public List<WorkoutJson> getWorkouts() {
		return this.workouts;
	}

	public void setWorkouts(List<WorkoutJson> workouts) {
		this.workouts = workouts;
	}

}