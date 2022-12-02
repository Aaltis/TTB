package fi.breakwaterworks.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Config")
public class Config implements Serializable {
	private static final long serialVersionUID = 1L;
	protected Long id;

	boolean isInitialized;
	public Config() {
	}

	public Config(boolean isInitialized, Timestamp timestamp) {
		this.isInitialized = isInitialized;
		this.movementsUpdated = timestamp;
	}

	public boolean isInitialized() {
		return isInitialized;
	}

	@Column(name = "movements_updated")
	private java.sql.Timestamp  movementsUpdated;
	
	

	public void setInitialized(boolean isInitialized) {
		this.isInitialized = isInitialized;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CONFIG_ID", unique = true)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}	
	
	public java.sql.Timestamp getMovementsUpdated() {
		return movementsUpdated;
	}

	public void setMovementsUpdated(java.sql.Timestamp movementsUpdated) {
		this.movementsUpdated = movementsUpdated;
	}
}
