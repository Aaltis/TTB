package fi.breakwaterworks.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PASSWORD")
public class Password {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long ID;

	@NotNull
	@Column(name = "PASSWORD")
	private String password;

	@NotNull
	@Column(name = "SALT")
	private String salt;

	public Password() {
	}

	public Password(String p, String s) {
		this.setPassword(p);
		this.setSalt(s);

	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

}
