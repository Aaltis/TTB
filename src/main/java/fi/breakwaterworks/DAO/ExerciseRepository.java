package fi.breakwaterworks.DAO;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fi.breakwaterworks.model.Exercise;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

	@Query("SELECT exercise FROM Exercise exercise left join fetch exercise.setRepsWeights "
			+ "JOIN exercise.workout workout " + "JOIN workout.worklogs worklogs " + "JOIN worklogs.users user "
			+ "WHERE user.userId = :uid And workout.id=:wid")
	public Set<Exercise> FindAllExercisesFromWorkoutWithIDAndFromUserId(@Param("uid") long id, @Param("wid") long wid);

	@Query("SELECT exercise FROM Exercise exercise left join fetch exercise.setRepsWeights "
			+ "JOIN exercise.workout workout " + "JOIN workout.worklogs worklogs " + "JOIN worklogs.users user "
			+ "WHERE user.userId = :uid And exercise.id=:eid")
	public Exercise FindExercisesWithIDAndFromUserId(@Param("uid") long id, @Param("eid") long eid);

}
