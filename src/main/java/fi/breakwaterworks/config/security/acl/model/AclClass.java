package fi.breakwaterworks.config.security.acl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ACL_CLASS")
public class AclClass {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long ID;

	@NotNull
	@Column(name = "CLASS")
	private String className;
	
	@NotNull
	@Column(name = "class_id_type")
	private String classIdType;

	public Long getID() {
		return ID;
	}

	public AclClass(@NotNull String className) {
		super();
		this.className = className;
		classIdType = "long";

	}

	public void setID(Long iD) {
		ID = iD;
	}

	public String getClassName() {
		return className;
	}

	public void setClass(String className) {
		this.className = className;
	}

	public AclClass() {	
	}
}
