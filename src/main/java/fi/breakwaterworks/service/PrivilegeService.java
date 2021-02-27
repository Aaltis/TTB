package fi.breakwaterworks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.breakwaterworks.DAO.PrivilegeRepository;
import fi.breakwaterworks.model.Privilege;

@Service
public class PrivilegeService {

	@Autowired
	private PrivilegeRepository pRepo;

	public void CreatePrivilege(Privilege privilege) {

		pRepo.save(privilege);
	}

	public Privilege findByName(String name) {
		Privilege p = pRepo.findByName(name);
		return p;
	}

}