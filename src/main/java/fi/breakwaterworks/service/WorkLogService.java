package fi.breakwaterworks.service;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.breakwaterworks.DAO.UserRepository;
import fi.breakwaterworks.DAO.WorkLogRepository;
import fi.breakwaterworks.model.WorkLog;
import fi.breakwaterworks.service.CustomUserDetailService.CustomUserDetails;

@Service
public class WorkLogService {

	@Autowired
	CustomUserDetailService cuserDetailService;
	@Autowired
	UserRepository urepo;

	@Autowired
	WorkLogRepository worklogRepo;

	public Set<WorkLog> GetUserWorkLogs() {
		CustomUserDetails user = cuserDetailService.LoadUserInfoIfUserIsLoggedIn();
		Set<WorkLog> worklogs = user.getWorkLogs();
		return worklogs;
	}

	public boolean CreateWorkLogForLoggedInUser(WorkLog worklog) {
		try {
			CustomUserDetails customUserDetails = cuserDetailService.LoadUserInfoIfUserIsLoggedIn();
			Date date = new Date();
			worklog.setDate(new java.sql.Date(date.getTime()));
			customUserDetails.addWorkLog(worklog);
			urepo.save(customUserDetails.getUser());
			return true;
		} catch (Exception ex) {
			return false;
		}

	}
}