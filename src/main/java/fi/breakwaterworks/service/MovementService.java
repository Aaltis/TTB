package fi.breakwaterworks.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import fi.breakwaterworks.DAO.ConfigRepository;
import fi.breakwaterworks.DAO.MovementRepository;
import fi.breakwaterworks.model.Config;
import fi.breakwaterworks.model.Movement;
import fi.breakwaterworks.model.request.MovementRequest;

@Service
public class MovementService {

	@Autowired
	private MovementRepository mRepo;
	@Autowired
	private ConfigRepository configRepository;
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
	
	public Timestamp GetMovementsStatus() {
		List<Config> config = configRepository.findAll();
		if(config!=null && config.size()>0 ) {
			return config.get(0).getMovementsUpdated();
		}
		return null;
	}

}
