package fi.breakwaterworks.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="PRIVILEGE")
public class Privilege {
  
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="PRIVILEGE_ID")
    private Long id;
 
	@Column(name="NAME")
    private String name;
    
    public Privilege(){

    }
    public Privilege(String name){
    	this.name=name;
    }

	public String getName() {
		return name;
	}
}