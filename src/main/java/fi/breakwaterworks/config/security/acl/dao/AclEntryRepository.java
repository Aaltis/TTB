package fi.breakwaterworks.config.security.acl.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fi.breakwaterworks.config.security.acl.model.AclEntry;

@Repository
@Transactional
public interface AclEntryRepository extends JpaRepository<AclEntry, Long>{
	
	void deleteByuserId(long sid);
}
