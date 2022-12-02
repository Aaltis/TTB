package fi.breakwaterworks.model.request;

import java.security.Timestamp;
import java.util.List;

import fi.breakwaterworks.model.Workout;

public class CreateWorkoutRequest {

    private long workoutId;
    private long worklogId;
    private String name;
    private Timestamp timestamp;
    private String comment;
    private boolean template;
    //used at finding from server;
    private String serverId;
    public List<ExerciseRequest> exercises;

    public CreateWorkoutRequest() {
    }

    public void setWorklogId(long worklogId) {
        this.worklogId = worklogId;
    }

    public long getWorklogId() {
        return worklogId;
    }

    public long getId() {
        return workoutId;
    }

    public List<ExerciseRequest> getExercises() {
        return exercises;
    }

    public void setExercises(List<ExerciseRequest> exercises) {
        this.exercises = exercises;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(Long workoutId) {
        this.workoutId = workoutId;
    }

    public boolean isTemplate() {
        return template;
    }

    public void setTemplate(boolean template) {
        this.template = template;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }
}
