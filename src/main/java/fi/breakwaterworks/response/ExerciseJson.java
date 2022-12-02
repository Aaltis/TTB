package fi.breakwaterworks.response;

import fi.breakwaterworks.model.Exercise;
import fi.breakwaterworks.model.request.SetRepsWeightJson;

import java.util.List;

public class ExerciseJson {

	private long remoteId;
	private long serverId;
	private Long orderNumber;
	private String movementName;
	private List<SetRepsWeightJson> setRepsWeight;
	
	public ExerciseJson(Long serverId, long remoteId) {
		this.serverId = serverId;
		this.remoteId = remoteId;
	}

	public ExerciseJson(Exercise exercise) {
		this.serverId = exercise.getId();
		this.remoteId = exercise.getRemoteId();
		this.orderNumber = exercise.getOrderNumber();
		this.movementName = exercise.getMovementName();

	}

	public long getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(long id) {
		this.remoteId = id;
	}

	public long getServerId() {
		return serverId;
	}

	public void setServerId(long serverId) {
		this.serverId = serverId;
	}

	public long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}

	public List<SetRepsWeightJson> getSetRepsWeight() {
		return setRepsWeight;
	}

	public void setSetRepsWeight(List<SetRepsWeightJson> setRepsWeight) {
		this.setRepsWeight = setRepsWeight;
	}

	public String getMovementName() {
		return movementName;
	}

	public void setMovementName(String movementName) {
		this.movementName = movementName;
	}
}

