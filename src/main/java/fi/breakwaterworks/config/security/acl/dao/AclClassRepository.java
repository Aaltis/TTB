package fi.breakwaterworks.config.security.acl.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fi.breakwaterworks.config.security.acl.model.AclClass;

@Repository
@Transactional
public interface AclClassRepository extends JpaRepository<AclClass, Long> {
	
	AclClass findByClassName(String ClassName);
	List<AclClass> findAll();
}

