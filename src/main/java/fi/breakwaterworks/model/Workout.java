package fi.breakwaterworks.model;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "WORKOUT")
public class Workout {

	private String name;
	private String owner;

	private List<Exercise> exercises;
	private List<WorkLog> worklogs;
	private Date date;
	private String comment;
	private Long workoutId;
	private boolean template;

	public Workout() {
	}

	public Workout(String name, List<Exercise> exercises, boolean template) {
		this.name = name;
		this.exercises = exercises;
		this.template = template;
	}

	@JsonCreator
	public Workout(@JsonProperty("workoutname") String name,
			@JsonProperty("exercises") List<Exercise> exercises) {
		this.name = name;
		this.exercises = exercises;
	}

	@Id
	@Column(name = "WORKOUT_ID", columnDefinition = "Serial")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getWorkoutId() {
		return workoutId;
	}

	public void setWorkoutId(Long workoutId) {
		this.workoutId = workoutId;
	}
	
	@JsonIgnore
	public Long getId() {
		return workoutId;
	}

	public void setId(long id) {
		workoutId = id;
	}

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "WORKOUT_EXERCISE",
	joinColumns = @JoinColumn(name = "WORKOUT_ID",
	referencedColumnName = "WORKOUT_ID"),
	inverseJoinColumns = @JoinColumn(name = "EXERCISE_ID", referencedColumnName = "EXERCISE_ID"))
	public List<Exercise> getExercises() {
		return this.exercises;
	}

	public void setExercises(List<Exercise> exercises) {
		this.exercises = exercises;
	}

	public void addExercises(Exercise ex) {
		this.exercises.add(ex);
	}

	@JsonIgnore
	@ManyToMany(mappedBy = "workouts")
	public List<WorkLog> getWorklogs() {
		return this.worklogs;
	}

	public void setWorklogs(List<WorkLog> worklogs) {
		this.worklogs = worklogs;
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
}
