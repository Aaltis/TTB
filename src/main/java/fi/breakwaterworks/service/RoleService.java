package fi.breakwaterworks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.breakwaterworks.DAO.RoleRepository;
import fi.breakwaterworks.model.UserRole;

@Service
public class RoleService {
	@Autowired
	private RoleRepository rRepo;

	public String createRoles(UserRole pRoles) {
		UserRole created = rRepo.findByName(pRoles.getName());
		String personID = String.valueOf(created.getUserRolesId());
		return personID;
	}

	public UserRole findByName(String name) {
		UserRole r = rRepo.findByName(name);
		return r;
	}

}
