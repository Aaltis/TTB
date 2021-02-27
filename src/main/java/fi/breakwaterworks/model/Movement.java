package fi.breakwaterworks.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Movement implements Serializable {

	protected Long id;
	protected String name;
	protected String type;
	protected String stance;
	protected String grip;
	protected String details;
	public enum SetTypeEnum {
		STRAIGHT_SET, REST_PAUSE, DROP_SET, WIDOWMAKER,
	}

	private static final long serialVersionUID = -8421547658682023876L;

	public Movement() {
	}

	public Movement(String name, String details, String type) {
		this.name = name;
		this.details = details;
		this.type = type;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "MOVEMENT_ID", unique = true)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotNull
	public String getName() {
		return name;// .replaceAll("_", " ").toLowerCase();
	}

	public void setName(String name) {
		this.name = name;// = name.replaceAll(" ", "_").toLowerCase();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStance() {
		return stance;
	}

	public void setStance(String stance) {
		this.stance = stance;
	}

	public String getGrip() {
		return grip;
	}

	public void setGrip(String grip) {
		this.grip = grip;
	}

	@Override
	public String toString() {
		return "id=" + id + ", name=" + name + ", type=" + type;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}