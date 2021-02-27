package fi.breakwaterworks.config;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;

import fi.breakwaterworks.config.security.acl.dao.AclClassRepository;
import fi.breakwaterworks.config.security.acl.dao.AclEntryRepository;
import fi.breakwaterworks.config.security.acl.dao.AclObjectIdentityRepository;
import fi.breakwaterworks.config.security.acl.dao.AclSidRepository;
import fi.breakwaterworks.config.security.acl.model.AclClass;
import fi.breakwaterworks.config.security.acl.model.AclEntry;
import fi.breakwaterworks.config.security.acl.model.AclObjectIdentity;
import fi.breakwaterworks.config.security.acl.model.AclSid;

public class ACLEntryCreator {

	@Autowired
	private AclClassRepository aclClassRepo;

	@Autowired
	private AclSidRepository aclSidRepo;

	@Autowired
	private AclObjectIdentityRepository aclObjectIdentityRepository;

	@Autowired
	private AclEntryRepository aclEntryRepository;

	public ACLEntryCreator() {
		
	}
	
	public void create(@NotNull String aclClassName,
			String aclSidName, 
			@NotNull long objectIdIdentity,
			String parentObject,
			@NotNull Integer entriesInheritting,
			int aclOrder,
			int permission) {
		
		AclClass aclclass = aclClassRepo.findByClassName(aclClassName);
		AclSid sid = aclSidRepo.findBySID(aclSidName);
		AclObjectIdentity identity = aclObjectIdentityRepository
				.save(new AclObjectIdentity(aclclass, objectIdIdentity, parentObject, sid, entriesInheritting));

		aclEntryRepository.save(new AclEntry(identity, aclOrder, sid, permission, 1, true, true));

	}
}
