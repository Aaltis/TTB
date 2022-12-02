package fi.breakwaterworks.model.request;

import java.util.Set;

import fi.breakwaterworks.model.SetRepsWeight;
import fi.breakwaterworks.model.Workout;

public class ExerciseRequest {

	private Long orderNumber;
	private double oneRepMax;
	private Set<SetRepsWeightJson> setRepsWeights;
    private boolean template;
    

	private long movementId;
    private long movementIdServer;
	private String movementName;

	private Long remoteId;
	private Long ServerId;
    private long ofTrainingMax;


	public Long getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}
	public double getOneRepMax() {
		return oneRepMax;
	}
	public void setOneRepMax(double oneRepMax) {
		this.oneRepMax = oneRepMax;
	}
	public String getMovementName() {
		return movementName;
	}
	public void setMovementName(String movementName) {
		this.movementName = movementName;
	}
	public Set<SetRepsWeightJson> getSetRepsWeights() {
		return setRepsWeights;
	}
	public void setSetRepsWeights(Set<SetRepsWeightJson> setRepsWeights) {
		this.setRepsWeights = setRepsWeights;
	}
    public boolean isTemplate() {
		return template;
	}
	public void setTemplate(boolean template) {
		this.template = template;
	}
	public Long getRemoteId() {
		return remoteId;
	}
	public void setRemoteId(Long id) {
		remoteId = id;
	}
	public Long getServerId() {
		return ServerId;
	}
	public void setServerId(Long serverId) {
		ServerId = serverId;
	}
	public long getMovementId() {
		return movementId;
	}
	public void setMovementId(long movementId) {
		this.movementId = movementId;
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
}
