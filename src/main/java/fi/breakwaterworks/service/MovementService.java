package fi.breakwaterworks.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import fi.breakwaterworks.DAO.ConfigRepository;
import fi.breakwaterworks.DAO.MovementRepository;
import fi.breakwaterworks.model.Config;
import fi.breakwaterworks.model.Exercise;
import fi.breakwaterworks.model.Movement;
import fi.breakwaterworks.model.Workout;
import fi.breakwaterworks.model.request.MovementRequest;

@Service
public class MovementService {

	@Autowired
	private MovementRepository movementRepo;
	@Autowired
	private ConfigRepository configRepository;

	public String CreateMovement(Movement movement) {
		Movement created = movementRepo.save(new Movement(movement.getName(), "", movement.getType()));
		return String.valueOf(created.getId());
	}

	public List<Movement> FindMovements(MovementRequest request) {
		Movement movement = new Movement(request.getName(), request.getType());
		ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", match -> match.contains())
				.withMatcher("type", match -> match.contains());

		Example<Movement> example = Example.of(movement, matcher);

		return movementRepo.findAll(example);

	}

	public Timestamp GetMovementsStatus() {
		List<Config> config = configRepository.findAll();
		if (config != null && config.size() > 0) {
			return config.get(0).getMovementsUpdated();
		}
		return null;
	}

	// TODO add error returns if too many or none found
	public Set<Exercise> GetMovementsToExercises(Set<Exercise> exercises) {

		if (exercises != null) {
			for (Exercise exercise : exercises) {
				Optional<Movement> movement = movementRepo.findByName(exercise.getMovementName());
				if (movement.isPresent()) {
					exercise.setMovement(movement.get());
				}
			}
		}
		return exercises;

	}

}
