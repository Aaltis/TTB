package fi.breakwaterworks.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import fi.breakwaterworks.config.JsonLoader;

@Entity
@Table(name = "EXERCISE")
public class Exercise implements Serializable {

	private static final long serialVersionUID = 1L;
	private Set<Workout> workouts;
	private Long orderNumber;
	private double oneRepMax;
	private Movement movement;
	private String movementName;
	private List<SetRepsWeight> setRepsWeights;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "EXERCISE_ID", columnDefinition = "BigSerial")
	@Access(AccessType.FIELD)
	private Long Id;

	@JsonIgnore
	public Long getId() {
		return Id;

	}

	public void setId(Long Id) {
		this.Id = Id;
	}

	public Exercise() {
	}

	public Exercise(int ordernumber, Movement movement) {
		this.orderNumber = (long) ordernumber;
		this.movement = movement;
	}

/*    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL })
    @JoinColumn(name="mainexercise_id", unique= true, nullable=true, insertable=true, updatable=true)
	private Exercise mainExercise;*/
    
    @JsonSerialize
    @JsonInclude(JsonInclude.Include.NON_EMPTY)//Avoiding empty json arrays.objects
	@OneToMany(cascade = CascadeType.ALL,
	        orphanRemoval = true)
	private Set<Exercise> subExercises = new HashSet<Exercise>();
    
	@JsonCreator
	public Exercise(@JsonProperty("ordernumber") long orderNumber,
			@JsonProperty("movementname") String movementName,
			@JsonProperty("settype") Movement.SetTypeEnum setType,
			@JsonProperty("setrepsweights") List<SetRepsWeight> srw) {
		this.orderNumber = orderNumber;
		this.setMovementName(movementName);
		this.setRepsWeights = srw;
	}
	
	@Column(name = "ORDER_NO")
	@Access(AccessType.PROPERTY)
	public long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(long orderNumber) {
		this.orderNumber = orderNumber;
	}

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "EXERCISE_MOVEMENT", joinColumns = @JoinColumn(name = "EXERCISE_ID", referencedColumnName = "EXERCISE_ID"), inverseJoinColumns = @JoinColumn(name = "MOVEMENT_ID", referencedColumnName = "MOVEMENT_ID"))
	@Access(AccessType.PROPERTY)
	public Movement getMovement() {
		return (Movement) this.movement;
	}

	public void setMovement(Movement movement) {
		this.movement = movement;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "EXERCISE_SRW", joinColumns = @JoinColumn(name = "EXERCISE_ID", referencedColumnName = "EXERCISE_ID"), inverseJoinColumns = @JoinColumn(name = "SRW_ID", referencedColumnName = "SRW_ID"))
	@Access(AccessType.PROPERTY)
	@ElementCollection(targetClass = SetRepsWeight.class)
	public List<SetRepsWeight> getSetRepsWeights() {
		return setRepsWeights;
	}

	public void setsetRepsWeights(List<SetRepsWeight> setrepsweight) {
		this.setRepsWeights = setrepsweight;
	}

	public void addSetRepsWeight(SetRepsWeight setrepsweight) {
		this.setRepsWeights.add(setrepsweight);
	}

	@ManyToMany(mappedBy = "exercises")
	@Access(AccessType.PROPERTY)
	@JsonIgnore
	public Set<Workout> getWorkouts() {
		return workouts;
	}

	public void setWorkouts(Set<Workout> workouts) {
		this.workouts = workouts;
	}

	/*public void setMainSet(Exercise mainExercise) {
		this.mainExercise = mainExercise;
	}*/

	public Set<Exercise> getSubExercises() {
		return subExercises;
	}

	public double calculateOneRepMax() {
		switch (setRepsWeights.iterator().next().reps) {
		case 1:
			oneRepMax = setRepsWeights.iterator().next().weight;
		case 2:
			oneRepMax = (setRepsWeights.iterator().next().weight / 95);
		case 3:
			oneRepMax = (setRepsWeights.iterator().next().weight / 93);
		case 4:
			oneRepMax = (setRepsWeights.iterator().next().weight / 90);
		case 5:
			oneRepMax = (setRepsWeights.iterator().next().weight / 87);
		case 6:
			oneRepMax = (setRepsWeights.iterator().next().weight / 85);
		case 7:
			oneRepMax = (setRepsWeights.iterator().next().weight / 83);
		case 8:
			oneRepMax = (setRepsWeights.iterator().next().weight / 80);
		case 9:
			oneRepMax = (setRepsWeights.iterator().next().weight / 77);
		case 10:
			oneRepMax = (setRepsWeights.iterator().next().weight / 75);
		case 11:
			oneRepMax = (setRepsWeights.iterator().next().weight / 73);
		case 12:
			oneRepMax = (setRepsWeights.iterator().next().weight / 70);
		}
		return 0;
	}

	public String getMovementName() {
		return movementName;
	}

	public void setMovementName(String movementName) {
		this.movementName = movementName;
	}

}
