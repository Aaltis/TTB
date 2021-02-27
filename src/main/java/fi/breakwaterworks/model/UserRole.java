package fi.breakwaterworks.model;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="USERROLE")
public class UserRole implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ROLE_ID")
	@NotNull
	@GeneratedValue(strategy=GenerationType.AUTO)    
	private Long UserRoleID;
	
	public Long getUserRolesId() {
		return UserRoleID;
	}

	public void setUSERRolesId(Long USERRolesId) {
		this.UserRoleID = USERRolesId;
	}

	@NotNull
	private String name;
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

	
	public String getName() {
		return name;
	}
	
    @ManyToMany
    @JoinTable(name = "ROLES_PRIVILEGES", 
    	joinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID"), 
        inverseJoinColumns = @JoinColumn(name = "PRIVILEGE_ID", referencedColumnName = "PRIVILEGE_ID"))
    private Collection<Privilege> privileges;
	public void setPrivileges(Collection<Privilege> privileges) {
		this.privileges=privileges;
	}   
	public UserRole(){
	}

	public UserRole(String name){
		this.name=name;
	}

	public Collection<? extends Privilege> getPrivileges() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
