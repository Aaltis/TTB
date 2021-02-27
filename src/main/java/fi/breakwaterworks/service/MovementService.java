package fi.breakwaterworks.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import fi.breakwaterworks.DAO.MovementRepository;
import fi.breakwaterworks.model.Movement;

@Service
public class MovementService {

	@Autowired
	private MovementRepository mRepo;

	public String CreateMovement(Movement movement) {
		Movement created = mRepo.save(new Movement(movement.getName(),"", movement.getType()));
		return String.valueOf(created.getId());
	}

	public String findById(@PathVariable Long id) {
		Optional<Movement> m = mRepo.findById(id);
		return m.toString();
	}

	public List<Movement> FindMovementsByName(String name) {
		return mRepo.FindMovementsWithNameLike("%" + name + "%");
	}

	public List<Movement> FindMovementsByType(String type) {
		return mRepo.findByType(type);
	}

	public List<Movement> getAll() {
		List<Movement> movements = new ArrayList<Movement>();
		movements = mRepo.findAll();
		return movements;
	}

	public List<Movement> FindMovementsByNameAndType(String name, String type) {
		return mRepo.findByNameAndType(name, type);
	}
}
