package fi.breakwaterworks.DAO;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fi.breakwaterworks.model.Privilege;

@Repository
@Transactional
public interface PrivilegeRepository extends JpaRepository<Privilege, Long>{
	
    Privilege findByName(String name);

    @Override
    void delete(Privilege privilege);

}
