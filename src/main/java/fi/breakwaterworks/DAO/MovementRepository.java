package fi.breakwaterworks.DAO;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fi.breakwaterworks.model.Movement;

@Repository
@Transactional
public interface  MovementRepository extends JpaRepository<Movement,Long> {
	
	Movement findByName(String name);

	Optional<Movement> findById(Long id);

    @Override
    void delete(Movement movement);
    
    
    
	@Query("SELECT movement FROM Movement movement WHERE movement.name LIKE %:name% AND movement.details=''")
	List<Movement> FindMovementsWithNameLike(@Param("name") String name);
	
	List<Movement> findByType(String Type);
	
	@Query("SELECT movement FROM Movement movement WHERE movement.name = :name and movement.type = :type")
	List<Movement> findByNameAndType(@Param("name")String name, @Param("type") String type);
}

/*
	  public void create(Movement movement) {
	    entityManager.persist(movement);
	    return;
	  }
	  

	  public void delete(Movement movement) {
	    if (entityManager.contains(movement))
	      entityManager.remove(movement);
	    else
	      entityManager.remove(entityManager.merge(movement));
	    return;
	  }
	  

	  @SuppressWarnings("unchecked")
	  public List<Movement> getAll() {
	    return (List<Movement>) entityManager.createQuery("from Movement").getResultList();
	  }
	  

	  public Movement getByName(String name) {
	    return (Movement) entityManager.createQuery(
	        "from Movement where name = :name")
	        .setParameter("name", name)
	        .getSingleResult();
	  }


	  public Movement getById(long id) {
		  return (Movement) entityManager.createQuery(
			        "from Movement where id = :id")
			        .setParameter("id", id)
			        .getSingleResult();
	  }


	  public void update(Movement movement) {
	    entityManager.merge(movement);
	    return;
	  }

	  // ------------------------
	  // PRIVATE FIELDS
	  // ------------------------
	  
	  // An EntityManager will be automatically injected from entityManagerFactory
	  // setup on DatabaseConfig class.
	  @PersistenceContext
	  private EntityManager entityManager;
	  
	} */
