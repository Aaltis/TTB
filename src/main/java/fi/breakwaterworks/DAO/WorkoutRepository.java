package fi.breakwaterworks.DAO;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fi.breakwaterworks.model.Workout;

@Repository
@Transactional
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

	@Query("SELECT workout FROM Workout workout where workout.template = false")
	public List<Workout> FindAllThatAreNotTemplates();
	
	@Query("SELECT workout FROM Workout workout ")
	public List<Workout> FindAll();
	@Query("SELECT workout FROM Workout workout where workout.template = false")
	public List<Workout> FindAllTemplates();

	@Query("SELECT workout FROM Workout workout " + "JOIN workout.worklogs worklogs "
			+ "JOIN worklogs.users user WHERE user.userId = :userId")
	public List<Workout> FindAllWorkoutsFromUserWithID(@Param("userId") long userId);

	@Query("SELECT workout FROM Workout workout " + "JOIN workout.worklogs worklogs "
			+ "JOIN worklogs.users user WHERE user.userId = :uid AND workout.workoutId=:wid")
	public Workout FindWorkoutFromUserWithIDAndWorkoutId(@Param("uid") long userId, @Param("wid") long workoutId);

	/*
	 * String query_findByProductDepartmentHospital =
	 * "select location from ProductInstallLocation location " +
	 * " join location.product prod " + " join location.department dep " +
	 * " join location.department.hospital hos " + " where  prod.name = :product " +
	 * " and dep.name.name = :department " + " and hos.name = :hospital ";
	 * 
	 * @Query(query_findByProductDepartmentHospital) ProductInstallLocation
	 * findByProductDepartmentHospital(@Param("product") String productName,
	 * 
	 * @Param("department") String departName, @Param("hospital") String
	 * hospitalName);
	 */
}
