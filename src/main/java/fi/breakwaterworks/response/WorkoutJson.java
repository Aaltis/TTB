package fi.breakwaterworks.response;

import java.sql.Date;

import fi.breakwaterworks.model.Workout;
import java.util.List;

public class WorkoutJson {

	private String name;
	private String remoteId;
	private long serverId;

	private List<ExerciseJson> exercises;
	private Date date;
	private String comment;

	public WorkoutJson() {
	}

	public WorkoutJson(Workout savedWorkout) {
		this.name = savedWorkout.getName();
		this.serverId = savedWorkout.getId();
		this.remoteId = savedWorkout.getRemoteId();

		this.date = savedWorkout.getDate();
		this.comment = savedWorkout.getComment();
	}

	public List<ExerciseJson> getExercises() {
		return this.exercises;
	}

	public void setExercises(List<ExerciseJson> exercises) {
		this.exercises = exercises;
	}

	public void addExercises(ExerciseJson exercise) {
		this.exercises.add(exercise);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return this.comment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(String remoteId) {
		this.remoteId = remoteId;
	}

	public long getServerId() {
		return serverId;
	}

	public void setServerId(long serverId) {
		this.serverId = serverId;
	}

}
