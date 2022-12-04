package fi.breakwaterworks.model.request;

import fi.breakwaterworks.model.SetRepsWeight;
import fi.breakwaterworks.model.SetType;

public class SetRepsWeightJson {
	protected Long serverId;
	protected int orderNumber;
	protected int set;
	protected int reps;
	protected double weight;
	protected String weightUnit;
	protected double remoteId;
	protected SetType setType;

	public SetRepsWeightJson() {
	}

	public SetRepsWeightJson(SetRepsWeight savedSRW) {
		this.serverId = savedSRW.getId();
		this.orderNumber = savedSRW.getOrderNumber();
		this.set = savedSRW.getSet();
		this.reps = savedSRW.getReps();
		this.weight = savedSRW.getWeight();
		this.weightUnit = savedSRW.getWeightUnit();
		this.setType = savedSRW.getSetType();

	}

	public long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public int getSet() {
		return set;
	}

	public void setSet(int set) {
		this.set = set;
	}

	public int getReps() {
		return reps;
	}

	public void setReps(int reps) {
		this.reps = reps;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}

	public double getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(double remoteId) {
		this.remoteId = remoteId;
	}

	public SetType getSetType() {
		return setType;
	}

	public void setSetType(SetType setType) {
		this.setType = setType;
	}
}
