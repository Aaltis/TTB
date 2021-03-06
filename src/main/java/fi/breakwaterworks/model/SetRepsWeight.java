package fi.breakwaterworks.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SET_REPS_WEIGHT")
public class SetRepsWeight {
	protected Long id;
	protected int orderNumber;
	protected int set;
	protected int reps;
	protected double weight;
	protected String weightUnit;

	public SetRepsWeight() {
	}

	public SetRepsWeight(int orderNumber, int set, int reps, double weight, String weightUnit) {
		super();
		this.orderNumber = orderNumber;
		this.set = set;
		this.reps = reps;
		this.weight = weight;
		this.weightUnit = weightUnit;
	}

	public SetRepsWeight(int ordernumber, int set, int rep) {
		this.orderNumber = ordernumber;
		this.set = set;
		this.reps = rep;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SRW_ID", unique = true)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "ORDER_NO", nullable = false)
	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Column(name = "SET")
	public int getSet() {
		return set;
	}

	public void setSet(int set) {
		this.set = set;
	}

	@Column(name = "REPS")
	public int getReps() {
		return reps;
	}

	public void setReps(int reps) {
		this.reps = reps;
	}

	@Column(name = "WEIGHT")
	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Column(length = 2, name = "WEIGHT_UNIT")
	public String getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}

}
