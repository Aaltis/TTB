package fi.breakwaterworks.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;
import org.springframework.security.crypto.bcrypt.BCrypt;

import fi.breakwaterworks.model.request.UserRequest;

@Entity
@Table(name = "USER_ACCOUNT")
public class User {

	public User() {
	}

	@Id
	@Column(name = "USER_ID", columnDefinition = "Serial")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;

	public long getId() {
		return userId;
	}

	@NotNull(message = "Name cannot be null")
	@Column(unique = true, name = "NAME")
	private String name;

	public String getName() {
		return name;
	}

	@NotNull
	@Column(name = "ENABLED")
	private Boolean enabled;

	@NotNull(message = "Email cannot be null")
	@Column(unique = true, name = "EMAIL")
	private String email;

	public User(String username, Password password, Set<UserRole> userRoleSet) {
		super();
		this.name = username;
		this.enabled = true;
		this.password = password;
		this.worklogs = new HashSet<WorkLog>();
		this.roles = userRoleSet;
	}

	public User(UserRequest userRequest, Password password) {
		this.name = userRequest.getUsername();
		this.email = userRequest.getEmail();
		this.enabled = true;
		this.password = password;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "USER_USERROLE", joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID"))
	private Set<UserRole> roles;

	@OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "PASSWORD_ID")
	private Password password;

	public Set<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(final HashSet<UserRole> roles) {
		this.roles = roles;
	}

	public String getEmail() {
		return this.email;
	}

	public String getPassword() {
		return password.getPassword();
	}

	public String getSalt() {
		return password.getSalt();
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@Cascade(value = { org.hibernate.annotations.CascadeType.ALL })
	@JoinTable(name = "USER_WORKLOG", joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "WORKLOG_ID", referencedColumnName = "WORKLOG_ID"))
	private Set<WorkLog> worklogs;

	public Set<WorkLog> getWorkLogs() {
		return this.worklogs;
	}

	public void addWorkLog(WorkLog worklog) {
		this.worklogs.add(worklog);
	}

	public Date getLastPasswordReset() {
		return lastPasswordReset;
	}

	public void setLastPasswordReset(Date lastPasswordReset) {
		this.lastPasswordReset = lastPasswordReset;
	}

	private Date lastPasswordReset;

	public static Password GeneratePassword(String plainPassword) {
		return new Password(BCrypt.hashpw(plainPassword, BCrypt.gensalt()), BCrypt.gensalt());
	}

}
