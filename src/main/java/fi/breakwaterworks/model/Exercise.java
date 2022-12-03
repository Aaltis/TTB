package fi.breakwaterworks.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import fi.breakwaterworks.model.request.ExerciseRequest;
import fi.breakwaterworks.model.request.SetRepsWeightJson;

@Entity
@Table(name = "EXERCISE")
public class Exercise implements Serializable {

	private static final long serialVersionUID = 1L;
	private Workout workout;
	private Long orderNumber;
	private double oneRepMax;
	private String movementName;
	private long remoteId;
	
    @OneToMany(mappedBy="exercise", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private Set<SetRepsWeight> setRepsWeights;

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

	/*
	 * @NotFound(action = NotFoundAction.IGNORE)
	 * 
	 * @JsonIgnore
	 * 
	 * @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL })
	 * 
	 * @JoinColumn(name="mainexercise_id", unique= true, nullable=true,
	 * insertable=true, updatable=true) private Exercise mainExercise;
	 */	


	@JsonCreator
	public Exercise(@JsonProperty("ordernumber") long orderNumber, @JsonProperty("movementname") String movementName,
			@JsonProperty("settype") Movement.SetTypeEnum setType,
			@JsonProperty("setrepsweights") Set<SetRepsWeight> srw) {
		this.orderNumber = orderNumber;
		this.setMovementName(movementName);
		this.setRepsWeights = srw;
	}

	public Exercise(ExerciseRequest request, Movement movement) {
		this.movement = movement;
		this.movementName = movement.getName();
		this.oneRepMax = request.getOneRepMax();
		for(SetRepsWeightJson srw: request.getSetRepsWeights()) {
			this.setRepsWeights.add(new SetRepsWeight(srw));
		}
	}

	public Exercise(ExerciseRequest request) {
		this.movementName = request.getMovementName();
		this.oneRepMax = request.getOneRepMax();
		for(SetRepsWeightJson srw: request.getSetRepsWeights()) {
			this.setRepsWeights.add(new SetRepsWeight(srw));
		}
	}

	@Column(name = "ORDER_NO")
	@Access(AccessType.PROPERTY)
	public long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(long orderNumber) {
		this.orderNumber = orderNumber;
	}

	@ManyToOne
	@JoinColumn(name = "movement_id")
	private Movement movement;

	public Set<SetRepsWeight> getSetRepsWeights() {
		return setRepsWeights;
	}
	
	public void setsetRepsWeights(Set<SetRepsWeight> setrepsweight) {
		this.setRepsWeights = setrepsweight;
	}

	public void addSetRepsWeight(SetRepsWeight setrepsweight) {
		this.setRepsWeights.add(setrepsweight);
	}

	@ManyToOne
	@Access(AccessType.PROPERTY)
    @OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	@JoinColumn(name = "workout_id", updatable = false)
	public Workout getWorkout() {
		return workout;
	}

	public void setWorkout(Workout workout) {
		this.workout = workout;
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

	public Movement getMovement() {
		return movement;
	}

	public void setMovement(Movement movement) {
		this.movement = movement;
	}

	public long getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(long remoteId) {
		this.remoteId = remoteId;
	}

	public void addSetRepsWeights(List<SetRepsWeight> srw) {
		this.setRepsWeights.addAll(srw);
	}

}
