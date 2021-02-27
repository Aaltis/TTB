package fi.breakwaterworks.config.security.acl.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import fi.breakwaterworks.config.security.acl.model.AclSid;

@Repository
@Transactional
public interface AclSidRepository extends JpaRepository<AclSid, Long> {

	AclSid findBySID(String name);

}
