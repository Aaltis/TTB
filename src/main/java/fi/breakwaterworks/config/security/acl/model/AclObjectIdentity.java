package fi.breakwaterworks.config.security.acl.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

//Stores information for each unique domain object:
@Entity
@Table(name = "ACL_OBJECT_IDENTITY")
public class AclObjectIdentity {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long Id;

	// define the domain object class, links to ACL_CLASS table
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "OBJECT_ID_CLASS")
	private AclClass aclClass;

	// domain objects can be stored in many tables depending on the class. Hence,
	// this field store the target object primary key
	// https://github.com/spring-projects/spring-security/issues/5455
	@NotNull
	@Column(name = "OBJECT_ID_IDENTITY")
	private String objectIdIdentity;

	// specify parent of this Object Identity within this table
	@Column(name = "PARENT_OBJECT")
	private String parentObject;

	// ID of the object owner, links to ACL_SID table
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "OWNER_SID")
	private AclSid ownerSid;

	@OneToMany(mappedBy = "aclObjectIdentity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<AclEntry> aclEntry;

	// whether ACL Entries of this object inherits from the parent object (ACL
	// Entries are defined in ACL_ENTRY table)
	@NotNull
	@Column(name = "ENTRIES_INHERITING")
	private Integer entriesInheritting;

	@NotNull
	@Column(name = "class_id_type")
	private String classIdType;
	
	public AclObjectIdentity(@NotNull AclClass aclClass,
			@NotNull long objectIdIdentity, 
			String parentObject,
			@NotNull AclSid ownerSid,
			@NotNull Integer entriesInheritting) {
		super();
		this.aclClass = aclClass;
		this.objectIdIdentity = Long.toString(objectIdIdentity);
		this.parentObject = parentObject;
		this.ownerSid = ownerSid;
		this.entriesInheritting = entriesInheritting;
	}

	public long getId() {
		return Id;
	}

	public void setId(long Id) {
		this.Id = Id;
	}

	public List<AclEntry> getAclEntry() {
		return aclEntry;
	}

	public void setAclEntry(List<AclEntry> aclEntry) {
		this.aclEntry = aclEntry;
	}

	public AclClass getAclClass() {
		return aclClass;
	}

	public void setAclClass(AclClass aclClass) {
		this.aclClass = aclClass;
	}

	public String getObjectIdIdetity() {
		return objectIdIdentity;
	}

	public void setObjectIdIdetity(String objectIdIdentity) {
		this.objectIdIdentity = objectIdIdentity;
	}

	public String getParentObject() {
		return parentObject;
	}

	public void setParentObject(String parentObject) {
		this.parentObject = parentObject;
	}

	public AclSid getOwnerSid() {
		return ownerSid;
	}

	public void setOwnerSid(AclSid ownerSid) {
		this.ownerSid = ownerSid;
	}

	public Integer getEntriesInheritting() {
		return entriesInheritting;
	}

	public void setEntriesInheritting(Integer entriesInheritting) {
		this.entriesInheritting = entriesInheritting;
	}

}
