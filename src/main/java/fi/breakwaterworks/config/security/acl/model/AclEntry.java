package fi.breakwaterworks.config.security.acl.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="ACL_ENTRY")
public class AclEntry {

	@Id
	@NotNull
	@GeneratedValue(strategy=GenerationType.AUTO) 
	@Column(name="ID")
	private Long Id;
	
	@NotNull   
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="ACL_OBJECT_IDENTITY")
	private AclObjectIdentity aclObjectIdentity;		

	// the order of current entry in the ACL entries list of corresponding Object Identity
	@NotNull   
	@Column(name="ACE_ORDER")
	private Integer aclOrder;

	//the target SID which the permission is granted to or denied from, links to ACL_SID table
	@NotNull   
	@ManyToOne
	@JoinColumn(name = "SID")
	private AclSid aclSid;

	//the integer bit mask that represents the actual permission being granted or denied
	@NotNull   
	@Column(name = "MASK")
	private Integer mask;

	//value 1 means granting, value 0 means denying
	@NotNull   
	@Column(name="GRANTING")
	private Integer granting;

	@NotNull   
	@Column(name="AUDIT_SUCCESS")
	private boolean auditSuccess;

	@NotNull   
	@Column(name="AUDIT_FAILURE")
	private boolean auditFailure;

	public AclEntry(@NotNull AclObjectIdentity aclObjectIdentity,
			@NotNull Integer aclOrder, 
			@NotNull AclSid aclSid,
			@NotNull Integer mask,
			@NotNull Integer granting, 
			@NotNull boolean auditSuccess,
			@NotNull boolean auditFailure) {
		super();
		this.aclObjectIdentity = aclObjectIdentity;
		this.aclOrder = aclOrder;
		this.aclSid = aclSid;
		this.mask = mask;
		this.granting = granting;
		this.auditSuccess = auditSuccess;
		this.auditFailure = auditFailure;
	}

	

	public Long getID() {
		return Id;
	}

	public void setID(Long Id) {
		this.Id = Id;
	}
	

	public AclObjectIdentity getAclObjectIdentity() {
		return aclObjectIdentity;
	}


	public void setAclObjectIdentity(AclObjectIdentity aclObjectIdentity) {
		this.aclObjectIdentity = aclObjectIdentity;
	}

	public @NotNull Integer getAclOrder() {
		return aclOrder;
	}

	public void setAclOrder(Integer aclOrder) {
		this.aclOrder = aclOrder;
	}

	public AclSid getAclSid() {
		return aclSid;
	}

	public void setAclSid(AclSid aclSid) {
		this.aclSid = aclSid;
	}

	public Integer getMask() {
		return mask;
	}

	public void setMask(Integer mask) {
		this.mask = mask;
	}

	public Integer getGranting() {
		return granting;
	}

	public void setGranting(Integer granting) {
		this.granting = granting;
	}

	public boolean isAuditSuccess() {
		return auditSuccess;
	}

	public void setAuditSuccess(boolean auditSuccess) {
		this.auditSuccess = auditSuccess;
	}

	public boolean isAuditFailure() {
		return auditFailure;
	}

	public void setAuditFailure(boolean auditFailure) {
		this.auditFailure = auditFailure;
	}


}
