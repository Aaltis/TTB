package fi.breakwaterworks.model;

import java.sql.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import fi.breakwaterworks.controller.WorkoutRequest;
import fi.breakwaterworks.response.WorkoutJson;

@Entity
@Table(name = "WORKOUT")
public class Workout {

	private String name;
	private String owner;
	private Set<Exercise> exercises;
	private Set<WorkLog> worklogs;
	private Set<User> users;
	private Date date;
	private String comment;
	private String remoteId;

	private Long id;
	private boolean template;

	public Workout() {
	}

	public Workout(String name, Set<Exercise> exercises, boolean template) {
		this.name = name;
		this.exercises = exercises;
		this.template = template;
	}

	@JsonCreator
	public Workout(@JsonProperty("workoutname") String name, @JsonProperty("exercises") Set<Exercise> exercises) {
		this.name = name;
		this.exercises = exercises;
	}

	public Workout(WorkoutJson saveWorkoutRequest) {
		this.id = saveWorkoutRequest.getServerId();
		this.remoteId = saveWorkoutRequest.getRemoteId();
		this.name = saveWorkoutRequest.getName();
		this.date = saveWorkoutRequest.getDate();
		this.comment = saveWorkoutRequest.getComment();

	}

	@Id
	@Column(name = "WORKOUT_ID", columnDefinition = "Serial")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(mappedBy = "workout", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public Set<Exercise> getExercises() {
		return this.exercises;
	}

	public void setExercises(Set<Exercise> exercises) {
		this.exercises = exercises;
	}

	public void addExercises(Exercise ex) {
		this.exercises.add(ex);
	}

	@JsonIgnore
	@ManyToMany(mappedBy = "workouts")
	public Set<WorkLog> getWorklogs() {
		return this.worklogs;
	}

	public void setWorklogs(Set<WorkLog> worklogs) {
		this.worklogs = worklogs;
	}

	@JsonIgnore
	@ManyToMany(mappedBy = "workouts")
	public Set<User> getUsers() {
		return this.users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Column(name = "DATE", columnDefinition = "DATE")
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(name = "COMMENT")
	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getTemplate() {
		return template;
	}

	public void setTemplate(boolean template) {
		this.template = template;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getComment() {
		return comment;
	}

	public String getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(String remoteId) {
		this.remoteId = remoteId;
	}
}
