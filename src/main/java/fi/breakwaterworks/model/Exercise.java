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

import fi.breakwaterworks.response.ExerciseJson;

@Entity
@Table(name = "EXERCISE")
public class Exercise implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "exercise_id", columnDefinition = "BigSerial")
	@Access(AccessType.FIELD)
	private long Id;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "workout_id")
	private Workout workout;
	
	@Column(name = "order_number")
	private long orderNumber;
	
	@Column(name = "movement_name")
	private String movementName;
	
	@Column(name = "remote_id")
	private long remoteId;
	
    @OneToMany(mappedBy="exercise", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<SetRepsWeight> setRepsWeights;

	@JsonIgnore
	public long getId() {
		return Id;
	}

	public void setId(long Id) {
		this.Id = Id;
	}

	public Exercise() {
	}

	public Exercise(int ordernumber, Movement movement) {
		this.orderNumber = (long) ordernumber;
		this.movement = movement;
	}

	@JsonCreator
	public Exercise(@JsonProperty("ordernumber") long orderNumber, @JsonProperty("movementname") String movementName,
			@JsonProperty("settype") Movement.SetTypeEnum setType,
			@JsonProperty("setrepsweights") Set<SetRepsWeight> srw) {
		this.orderNumber = orderNumber;
		this.setMovementName(movementName);
		this.setRepsWeights = srw;
	}

	public Exercise(ExerciseJson request) {
		this.remoteId = request.getRemoteId();
		this.movementName = request.getMovementNameRemote();
		this.orderNumber = request.getOrderNumber();
		
	}
	public Exercise(ExerciseJson request, Movement movement) {
		this.movementName = movement.getName();
		this.orderNumber = request.getOrderNumber();
		this.remoteId = request.getRemoteId();
		
	}

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
	
	public void setSetRepsWeights(Set<SetRepsWeight> setrepsweight) {
		this.setRepsWeights = setrepsweight;
	}

	public void addSetRepsWeight(SetRepsWeight setrepsweight) {
		this.setRepsWeights.add(setrepsweight);
	}

	public Workout getWorkout() {
		return workout;
	}

	public void setWorkout(Workout workout) {
		this.workout = workout;
	}
	
	/*public double calculateOneRepMax() {
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
	}*/

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
