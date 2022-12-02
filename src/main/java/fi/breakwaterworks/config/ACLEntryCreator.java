package fi.breakwaterworks.config;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;

import fi.breakwaterworks.config.security.acl.dao.AclClassRepository;
import fi.breakwaterworks.config.security.acl.dao.AclEntryRepository;
import fi.breakwaterworks.config.security.acl.dao.AclObjectIdentityRepository;
import fi.breakwaterworks.config.security.acl.dao.AclSidRepository;

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
		
	
	}
}
