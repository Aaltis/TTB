package fi.breakwaterworks.DAO;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import fi.breakwaterworks.model.Movement;

@Repository
@Transactional
public interface MovementRepository extends JpaRepository<Movement,Long>, QueryByExampleExecutor<Movement> {
	Optional<Movement> findByName(String name);
	
}

