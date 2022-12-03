package fi.breakwaterworks.config;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import fi.breakwaterworks.DAO.ConfigRepository;
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
import fi.breakwaterworks.model.Config;
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
	static Logger logger = LogManager.getLogger(InitialDataLoader.class.getName());

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
	private ConfigRepository configRepository;
	
	@Autowired
	private AclObjectIdentityRepository aclObjectIdentityRepository;

	@Autowired
	private AclEntryRepository aclEntryRepository;

	private JsonLoader jsonLoader;

	@Value("${spring.profiles.active:dev}")
	private String activeProfile;

	@Value("${database.hbm2ddl.auto}")
	private String hbm2ddlAuto;

	public InitialDataLoader() {
		jsonLoader = new JsonLoader();
	}

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {

		//we only initialize once.
		List<Config> config = configRepository.findAll();
		if(config!=null && config.size()>0 ) {
			Config x=config.get(0);
			if(x.isInitialized())
				return;
		}
		
		System.out.println("Load profiles.");
		List<Role> jsonRoles = jsonLoader.LoadRoles(activeProfile, env);
		logger.info("Found " + jsonRoles.size() + " roles.");
		SaveSids(jsonRoles);
		SaveRoles(jsonRoles);
		logger.info("Reading movements.");
		List<Movement> movements = jsonLoader.LoadMovements(activeProfile, env);
		logger.info("Found " + movements.size() + " movements.");
		mRepo.saveAll(movements);
		aclClassRepo.save(new AclClass(Workout.class.getName()));
		aclClassRepo.save(new AclClass(WorkLog.class.getName()));

		configRepository.save(new Config(true, new Timestamp(System.currentTimeMillis())));

		
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
			uService.CreateUser("kuolevainen", "kuolevainen");
			uService.CreateUser("jumala", "jumala");
		} catch (Exception ex) {
			logger.error(ex);
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
	
	private void createWorkoutTemplates() {
		AclSid adminSID = aclSidRepo.findBySID("ROLE_ADMIN");
		AclSid userSID = aclSidRepo.findBySID("ROLE_USER");
		try {
			// Find movements from database for movements in templates.
			List<WorkLog> templateWorkLog = jsonLoader.LoadWorkoutTemplates(activeProfile, env);
			for (WorkLog worklog : templateWorkLog) {
				for (Workout workout : worklog.getWorkouts()) {
					for (Exercise exercise : workout.getExercises()) {
						Optional<Movement> m = mRepo.findByName(exercise.getMovementName());
						if (m.isPresent()) {
							exercise.setMovement(m.get());
							exercise.setWorkout(workout);
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
						Optional<Movement> m = mRepo.findByName(exercise.getMovementName());
						if (m.isPresent()) {
							exercise.setMovement(m.get());
							exercise.setWorkout(workout);
						}
					}
				}

			}
			if (activeProfile == "dev") {
				TestData();
			}
			
			configRepository.save(new Config(true, new Timestamp(System.currentTimeMillis())));
		} catch (Exception ex) {
			logger.error(ex);
		}

	}

}
