package fi.breakwaterworks.model;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "WORKLOG")
public class WorkLog {

	private List<User> users;
	private Set<Workout> workouts;
	private int Id;
	private String name;
	private String description;
	private String comment;
	private Date Date;
	private boolean template;

	public WorkLog() {
	}

	public WorkLog(@JsonProperty("name") String n, @JsonProperty("description") String d) {
		this.name = n;
		this.description = d;
		this.workouts = new HashSet<Workout>();
	}

	public WorkLog(String name, Set<Workout> workouts, boolean template) {
		this.name = name;
		this.workouts = workouts;
		this.setTemplate(template);
	}

	@Id
	@Column(name = "WORKLOG_ID", columnDefinition = "Serial")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	public @NotNull int getId() {
		return Id;
	}

	public void setId(int worklogid) {
		this.Id = worklogid;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;// .replaceAll("_", " ").toLowerCase();
	}

	public void setName(String name) {
		this.name = name;// = name.replaceAll(" ", "_").toLowerCase();
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "WORKLOG_WORKOUT", joinColumns = @JoinColumn(name = "WORKLOG_ID", referencedColumnName = "WORKLOG_ID"), inverseJoinColumns = @JoinColumn(name = "WORKOUT_ID", referencedColumnName = "WORKOUT_ID"))
	public Set<Workout> getWorkouts() {
		return workouts;
	}

	public void setWorkouts(Set<Workout> workouts) {
		this.workouts = workouts;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "USER_WORKLOG", joinColumns = @JoinColumn(name = "WORKLOG_ID", referencedColumnName = "WORKLOG_ID"), inverseJoinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID"))
	public void setUsers(List<User> users) {
		this.users = users;
	}

	@ManyToMany(mappedBy = "worklogs")
	public List<User> getUsers() {
		return this.users;
	}

	@Column(name = "COMMENT")
	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return this.comment;
	}

	@Column(name = "DATE")
	public void setDate(Date date) {
		this.Date = date;
	}

	public void addWorkout(Workout workout) {
		this.workouts.add(workout);
	}

	public boolean isTemplate() {
		return template;
	}

	public void setTemplate(boolean template) {
		this.template = template;
	}

}
