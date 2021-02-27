package fi.breakwaterworks.DAO;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fi.breakwaterworks.model.WorkLog;

@Repository
@Transactional
public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {

	/* @Query("SELECT * FROM Person p WHERE LOWER(p.lastName) = LOWER(:lastName)")
	 public List<Person> find(@Param("lastName") String lastName);*/
	 
	/* @Query("SELECT * FROM WORKLOG W INNER JOIN USER_WORKLOG UW WHERE UW.USER_ID = :id)")
	 public List<WorkLog> FindAllFromUserWithID(@Param("id") String id);*/
	
	 
}

