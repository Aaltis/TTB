package fi.breakwaterworks.service;

import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import fi.breakwaterworks.DAO.ExerciseRepository;
import fi.breakwaterworks.DAO.MovementRepository;
import fi.breakwaterworks.DAO.UserRepository;
import fi.breakwaterworks.DAO.WorkoutRepository;
import fi.breakwaterworks.config.Permission;
import fi.breakwaterworks.config.security.acl.dao.AclClassRepository;
import fi.breakwaterworks.config.security.acl.dao.AclEntryRepository;
import fi.breakwaterworks.config.security.acl.dao.AclObjectIdentityRepository;
import fi.breakwaterworks.config.security.acl.dao.AclSidRepository;
import fi.breakwaterworks.config.security.acl.model.AclClass;
import fi.breakwaterworks.config.security.acl.model.AclEntry;
import fi.breakwaterworks.config.security.acl.model.AclObjectIdentity;
import fi.breakwaterworks.config.security.acl.model.AclSid;
import fi.breakwaterworks.controller.UserWorkoutController;
import fi.breakwaterworks.model.Exercise;
import fi.breakwaterworks.model.Movement;
import fi.breakwaterworks.model.SetRepsWeight;
import fi.breakwaterworks.model.User;
import fi.breakwaterworks.model.Workout;
import fi.breakwaterworks.service.CustomUserDetailService.CustomUserDetails;
import java.util.UUID;

@Service
public class WorkoutsService {

	@Autowired
	WorkoutRepository workoutRepo;

	@Autowired
	ExerciseRepository exerciseRepo;

	@Autowired
	MovementRepository MovementRepo;

	@Autowired
	CustomUserDetailService cuserDetailService;

	@Autowired
	AclClassRepository aclClassRepository;

	@Autowired
	AclObjectIdentityRepository aclObjectIdentityRepository;

	@Autowired
	AclSidRepository aclSidRepository;

	@Autowired
	AclEntryRepository aclEntryRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private MovementRepository movementRepository;

	@Autowired
	UserRepository urepo;

	static Logger log = (Logger) LogManager.getLogger(UserWorkoutController.class);

	public boolean SaveWorkoutForUser(Workout workout) throws Exception {

		try {
			Workout appliedWorkouts = GetMovementsToWorkout(workout);

			User user = userService.GetUserByName(SecurityContextHolder.getContext().getAuthentication().getName());

			//AclClass workoutClass = aclClassRepository.findByClassName(Workout.class.getName());
			appliedWorkouts.setOwner(user.getName());
			workout.setUnigueId(UUID.randomUUID().toString());
			Workout savedWorkout = workoutRepo.save(appliedWorkouts);
			
			//TODO this is not needed when workouts are only for user.
			/*AclSid userSid = aclSidRepository.findBySID(user.getName());
			AclObjectIdentity workoutIdentity = aclObjectIdentityRepository
					.save(new AclObjectIdentity(workoutClass, savedWorkout.getId(), null, userSid, 0));
			aclEntryRepository.save(new AclEntry(workoutIdentity, 1, userSid, Permission.Admin.value, 1, true, true));*/
			return true;
		} catch (Exception ex) {
			log.error(ex);
			return false;
		}
	}

	// TODO
	/*
	 * public void ShareWorkoutWithUser(String workoutUUID, String userid) throws
	 * Exception {
	 * 
	 * Workout appliedWorkouts = workoutRepo.getWorkoutWithUUID(workoutUUID);
	 * 
	 * User user = userService.GetUserById(userid);
	 * 
	 * AclClass workoutClass =
	 * aclClassRepository.findByClassName(Workout.class.getName());
	 * appliedWorkouts.setOwner(user.getName());
	 * workout.setUnigueId(UUID.randomUUID().toString()); Workout savedWorkout =
	 * workoutRepo.save(appliedWorkouts); AclSid userSid =
	 * aclSidRepository.findBySID(user.getName()); AclObjectIdentity workoutIdentity
	 * = aclObjectIdentityRepository .save(new AclObjectIdentity(workoutClass,
	 * savedWorkout.getId(), null, userSid, 0)); aclEntryRepository.save(new
	 * AclEntry(workoutIdentity, 1, userSid, Permission.Admin.value, 1, true,
	 * true));
	 * 
	 * }
	 */

	// TODO add error returns if too many or none found
	private Workout GetMovementsToWorkout(Workout workout) {
		for (Exercise exercise : workout.getExercises()) {
			Movement movement = movementRepository.findByName(exercise.getMovementName());
			exercise.setMovement(movement);
		}
		return workout;
	}

	// user has "administration privileges to their own workouts.
	// @PostFilter("hasPermission(filterObject, 'ADMINISTRATION')")
	@PostFilter("hasRole('ADMINISTRATION') or filterObject.owner == authentication.name")
	public List<Workout> GetUserWorkouts() {
		return workoutRepo.findAll();
	}

	public List<Workout> GetUserWorkoutsTest() {
		CustomUserDetails user = cuserDetailService.LoadUserInfoIfUserIsLoggedIn();
		return workoutRepo.FindAll();
	}

	public Set<Exercise> GetUserExercisesFromWorkoutWithId(long id) {
		CustomUserDetails user = cuserDetailService.LoadUserInfoIfUserIsLoggedIn();
		return exerciseRepo.FindAllExercisesFromWorkoutWithIDAndFromUserId(user.getUser().getId(), id);
	}

	public Set<Exercise> AddExerciseToWorkoutWithId(int ordernumber, String movementname, Long workoutId) {
		CustomUserDetails user = cuserDetailService.LoadUserInfoIfUserIsLoggedIn();
		Workout workout = workoutRepo.FindWorkoutFromUserWithIDAndWorkoutId(user.getUser().getId(), workoutId);
		Movement movement = MovementRepo.findByName(movementname);
		if (movement != null) {
			Exercise ex = new Exercise(ordernumber, movement);
			workout.addExercises(ex);
			workoutRepo.save(workout);
		}

		return exerciseRepo.FindAllExercisesFromWorkoutWithIDAndFromUserId(user.getUser().getId(), workoutId);
	}

	public Set<Exercise> addSetRepWeightToExercise(Long exId, SetRepsWeight srw, long workoutId) {
		CustomUserDetails user = cuserDetailService.LoadUserInfoIfUserIsLoggedIn();
		Exercise ex = exerciseRepo.FindExercisesWithIDAndFromUserId(user.getUser().getId(), exId);
		srw.setWeightUnit("kg");
		ex.addSetRepsWeight(srw);
		exerciseRepo.save(ex);

		return exerciseRepo.FindAllExercisesFromWorkoutWithIDAndFromUserId(user.getUser().getId(), workoutId);
	}

	// }
}
