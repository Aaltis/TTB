package fi.breakwaterworks.response;

import java.util.List;

import fi.breakwaterworks.model.Workout;

public class WorkoutResponse {
	private List<Workout> workouts;

	public WorkoutResponse() {
		super();
	}

	public WorkoutResponse(List<Workout> workouts) {
		this.workouts = workouts;
	}

	public List<Workout> getWorkouts() {
		return this.workouts;
	}

	public void setWorkouts(List<Workout> workouts) {
		this.workouts = workouts;
	}

}