package fi.breakwaterworks.response;

import fi.breakwaterworks.model.Exercise;
import fi.breakwaterworks.model.request.SetRepsWeightJson;

import java.util.List;

public class ExerciseJson {

	private Long orderNumber;
	private double oneRepMax;
	private List<SetRepsWeightJson> setRepsWeights;
    private boolean template;
    private long movementIdRemote;
    private long movementIdServer;
	private String movementName;

	private Long remoteId;
	private Long serverId;
    private long ofTrainingMax;
    
	public ExerciseJson() {
	}

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
		return setRepsWeights;
	}

	public void setSetRepsWeight(List<SetRepsWeightJson> setRepsWeight) {
		this.setRepsWeights = setRepsWeight;
	}

	public String getMovementName() {
		return movementName;
	}

	public void setMovementName(String movementName) {
		this.movementName = movementName;
	}

	public double getOneRepMax() {
		return oneRepMax;
	}

	public void setOneRepMax(double oneRepMax) {
		this.oneRepMax = oneRepMax;
	}

	public boolean isTemplate() {
		return template;
	}

	public void setTemplate(boolean template) {
		this.template = template;
	}

	public long getMovementIdRemote() {
		return movementIdRemote;
	}

	public void setMovementIdRemote(long movementId) {
		this.movementIdRemote = movementId;
	}

	public long getMovementIdServer() {
		return movementIdServer;
	}

	public void setMovementIdServer(long movementIdServer) {
		this.movementIdServer = movementIdServer;
	}

	public long getOfTrainingMax() {
		return ofTrainingMax;
	}

	public void setOfTrainingMax(long ofTrainingMax) {
		this.ofTrainingMax = ofTrainingMax;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}
}

