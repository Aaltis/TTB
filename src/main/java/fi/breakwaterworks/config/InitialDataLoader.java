package fi.breakwaterworks.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import fi.breakwaterworks.DAO.MovementRepository;
import fi.breakwaterworks.DAO.PrivilegeRepository;
import fi.breakwaterworks.DAO.RoleRepository;
import fi.breakwaterworks.DAO.WorkLogRepository;
import fi.breakwaterworks.config.security.acl.dao.AclClassRepository;
import fi.breakwaterworks.config.security.acl.dao.AclEntryRepository;
import fi.breakwaterworks.config.security.acl.dao.AclObjectIdentityRepository;
import fi.breakwaterworks.config.security.acl.dao.AclSidRepository;
import fi.breakwaterworks.config.security.acl.model.AclClass;
import fi.breakwaterworks.config.security.acl.model.AclEntry;
import fi.breakwaterworks.config.security.acl.model.AclObjectIdentity;
import fi.breakwaterworks.config.security.acl.model.AclSid;
import fi.breakwaterworks.model.Exercise;
import fi.breakwaterworks.model.Movement;
import fi.breakwaterworks.model.Privilege;
import fi.breakwaterworks.model.UserRole;
import fi.breakwaterworks.model.WorkLog;
import fi.breakwaterworks.model.Workout;
import fi.breakwaterworks.model.request.UserRequest;
import fi.breakwaterworks.service.UserService;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {
	boolean alreadySetup = false;
	static Logger log = LogManager.getLogger(InitialDataLoader.class.getName());

	@Autowired
	private Environment env;

	@Autowired
	private UserService uService;

	@Autowired
	private RoleRepository rRepo;

	@Autowired
	private MovementRepository mRepo;

	@Autowired
	private WorkLogRepository wRepo;

	@Autowired
	private PrivilegeRepository pRepo;

	@Autowired
	private AclClassRepository aclClassRepo;

	@Autowired
	private AclSidRepository aclSidRepo;

	@Autowired
	private AclObjectIdentityRepository aclObjectIdentityRepository;

	@Autowired
	private AclEntryRepository aclEntryRepository;

	private JsonLoader jsonLoader;

	@Value("${spring.profiles.active}")
	private String activeProfile;

	@Value("${database.hbm2ddl.auto}")
	private String hbm2ddlAuto;

	public InitialDataLoader() {
		jsonLoader = new JsonLoader();
	}

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {

		List<Role> jsonRoles = jsonLoader.LoadRoles(env);
		SaveSids(jsonRoles);
		SaveRoles(jsonRoles);
		List<Movement> movements = jsonLoader.LoadMovements();
		AclSid adminSID = aclSidRepo.findBySID("ROLE_ADMIN");
        AclSid userSID = aclSidRepo.findBySID("ROLE_USER");
		mRepo.saveAll(movements);
		try {
			// Find movements from database for movements in templates.
			List<WorkLog> templateWorkLog = jsonLoader.LoadWorkoutTemplates(env);
			for (WorkLog worklog : templateWorkLog) {
				for (Workout workout : worklog.getWorkouts()) {
					for (Exercise exercise : workout.getExercises()) {
						Movement m = mRepo.findByName(exercise.getMovementName());
						if (m != null) {
							exercise.setMovement(m);
						}
					}
				}
			}
			List<WorkLog> saveWorkLogs = wRepo.saveAll(templateWorkLog);

			AclClass workoutAclClass = aclClassRepo.save(new AclClass(Workout.class.getName()));
			AclClass workLogAclClass = aclClassRepo.save(new AclClass(WorkLog.class.getName()));

			/*
			 * Template workouts. admin can do anything, user can only see.
			 */
			for (WorkLog worklog : saveWorkLogs) {

				AclObjectIdentity worlogidentity1 = aclObjectIdentityRepository
						.save(new AclObjectIdentity(workLogAclClass, worklog.getId(), null, adminSID, 0));
				AclObjectIdentity worlogidentity2 = aclObjectIdentityRepository
						.save(new AclObjectIdentity(workLogAclClass, worklog.getId(), null, userSID, 0));
				aclEntryRepository.save(new AclEntry(worlogidentity1, 1, adminSID, 16, 1, true, true));
				aclEntryRepository.save(new AclEntry(worlogidentity1, 1, userSID, 1, 1, true, true));

				aclEntryRepository.save(new AclEntry(worlogidentity2, 1, adminSID, 16, 1, true, true));
				aclEntryRepository.save(new AclEntry(worlogidentity2, 1, userSID, 1, 1, true, true));

				for (Workout workout : worklog.getWorkouts()) {
					AclObjectIdentity workoutIdentity1 = aclObjectIdentityRepository
							.save(new AclObjectIdentity(workoutAclClass, workout.getId(), null, userSID, 0));
					AclObjectIdentity workoutIdentity2 = aclObjectIdentityRepository
							.save(new AclObjectIdentity(workoutAclClass, workout.getId(), null, adminSID, 0));

					aclEntryRepository.save(new AclEntry(workoutIdentity1, 1, adminSID, 16, 1, true, true));
					aclEntryRepository.save(new AclEntry(workoutIdentity1, 1, userSID, 1, 1, true, true));

					aclEntryRepository.save(new AclEntry(workoutIdentity2, 1, adminSID, 16, 1, true, true));
					aclEntryRepository.save(new AclEntry(workoutIdentity2, 1, userSID, 1, 1, true, true));
					for (Exercise exercise : workout.getExercises()) {
						Movement m = mRepo.findByName(exercise.getMovementName());
						if (m != null) {
							exercise.setMovement(m);
						}
					}
				}

			}
			if (activeProfile == "dev") {
				TestData();
			}
		} catch (Exception ex) {
			log.error(ex);
		}

		alreadySetup = true;
	}

	private void SaveRoles(List<Role> jsonRoles) {
		for (Role role : jsonRoles) {
			createRoleIfNotFound(role.name);
		}
	}

	private List<AclSid> SaveSids(List<Role> roles) {
		List<AclSid> sids = new ArrayList<AclSid>();
		for (Role role : roles) {
			aclSidRepo.save(new AclSid(0, role.name));
		}
		return sids;
	}

	public void TestData() {

		try {
			uService.CreateUser(new UserRequest("kuolevainen", "kuolevainen", "kuolevainen@kuolevainen"));
			uService.CreateUser(new UserRequest("jumala", "jumala", "jumala@jumala"));
		} catch (Exception ex) {
			log.error(ex);
		}
	}

	@Transactional
	private Privilege createPrivilegeIfNotFound(String name) {
		// privilegeService=new PrivilegeService();
		Privilege privilege = pRepo.findByName(name);
		if (privilege == null) {
			privilege = new Privilege(name);
			pRepo.save(privilege);
		}
		return privilege;
	}

	@Transactional
	private UserRole createRoleIfNotFound(String name) {

		UserRole role = rRepo.findByName(name);
		if (role == null) {
			rRepo.save(new UserRole(name));
		}
		return role;

	}

}
