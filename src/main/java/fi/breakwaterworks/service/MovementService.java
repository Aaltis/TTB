package fi.breakwaterworks.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import fi.breakwaterworks.DAO.MovementRepository;
import fi.breakwaterworks.model.Movement;
import fi.breakwaterworks.model.request.MovementRequest;

@Service
public class MovementService {

	@Autowired
	private MovementRepository mRepo;

	public String CreateMovement(Movement movement) {
		Movement created = mRepo.save(new Movement(movement.getName(), "", movement.getType()));
		return String.valueOf(created.getId());
	}

	public List<Movement> FindMovements(MovementRequest request) {
		Movement movement = new Movement(request.getName(), request.getType());
		ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", match -> match.contains())
				.withMatcher("type", match -> match.contains());

		Example<Movement> example = Example.of(movement, matcher);

		return mRepo.findAll(example);

	}
}
