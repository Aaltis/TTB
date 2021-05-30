package fi.breakwaterworks.DAO;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fi.breakwaterworks.model.Config;

@Repository
@Transactional
public interface ConfigRepository extends JpaRepository<Config, Long> {

	List<Config> findAll();
}


