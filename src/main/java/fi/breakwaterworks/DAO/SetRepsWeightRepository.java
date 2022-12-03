package fi.breakwaterworks.DAO;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fi.breakwaterworks.model.SetRepsWeight;
import fi.breakwaterworks.model.Workout;

@Repository
@Transactional
public interface SetRepsWeightRepository extends JpaRepository<SetRepsWeight, Long> {
	@Query("SELECT workout FROM Workout workout " + "JOIN workout.worklogs worklogs "
			+ "JOIN worklogs.users user WHERE user.userId = :userId and workout.id = :workoutId")
	public SetRepsWeight FindSetRepsWeightWithRemoteId(long userId, Long workoutId);
}
