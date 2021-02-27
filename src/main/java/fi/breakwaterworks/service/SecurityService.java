package fi.breakwaterworks.service;

import org.springframework.stereotype.Service;

@Service
public interface SecurityService {

	public Boolean hasProtectedAccess();

}
