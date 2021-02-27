package fi.breakwaterworks.config.security.acl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ACL_SID")
public class AclSid {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long ID;

	// SID: which is the username or role name. SID stands for Security Identity
	@NotNull
	@Column(name = "SID")
	private String SID;

	/**
	 * 0=role, 1=user
	 */
	@NotNull
	@Column(name = "PRINCIPAL")
	private int principal;
	
	public AclSid() {
	}
	
	/**
	 * 
	 * @param principal principal, 0=role, 1 user.
	 * @param sID user or role name.
	 */
	public AclSid(@NotNull int principal, @NotNull String sID) {
		super();
		this.principal = principal;
		SID = sID;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}

	public String getSID() {
		return SID;
	}

	public void setSID(String sID) {
		SID = sID;
	}

	public int getPrincipal() {
		return principal;
	}

	public void setPrincipal(int principal) {
		this.principal = principal;
	}
}
