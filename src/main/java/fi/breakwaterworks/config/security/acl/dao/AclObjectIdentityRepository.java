package fi.breakwaterworks.config.security.acl.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fi.breakwaterworks.config.security.acl.model.AclObjectIdentity;

@Repository
@Transactional
public interface AclObjectIdentityRepository extends JpaRepository<AclObjectIdentity, Long> {
	void deleteByObjectIdIdentity(String id);
}