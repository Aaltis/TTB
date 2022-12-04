package fi.breakwaterworks.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import fi.breakwaterworks.DAO.ExerciseRepository;
import fi.breakwaterworks.DAO.MovementRepository;
import fi.breakwaterworks.DAO.SetRepsWeightRepository;
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
import fi.breakwaterworks.controller.WorkoutRequest;
import fi.breakwaterworks.model.Exercise;
import fi.breakwaterworks.model.Movement;
import fi.breakwaterworks.model.SetRepsWeight;
import fi.breakwaterworks.model.User;
import fi.breakwaterworks.model.Workout;
import fi.breakwaterworks.model.request.MovementRequest;
import fi.breakwaterworks.model.request.SetRepsWeightJson;
import fi.breakwaterworks.response.ExerciseJson;
import fi.breakwaterworks.service.CustomUserDetailService.CustomUserDetails;

@Service
public class WorkoutsService {

	@Autowired
	WorkoutRepository workoutRepo;

	@Autowired
	ExerciseRepository exerciseRepo;

	@Autowired
	MovementRepository movementRepo;

	@Autowired
	SetRepsWeightRepository srwRepo;

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
	UserRepository urepo;

	static Logger log = (Logger) LogManager.getLogger(WorkoutsService.class);

	public Workout SaveWorkoutForUser(Workout workout) throws Exception {

		try {
			Workout appliedWorkout = GetMovementsToWorkout(workout);
			connectExercisesToWorkout(appliedWorkout);

			User user = userService.GetUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
			appliedWorkout.setOwner(user.getName());

			Workout savedWorkout = workoutRepo.save(appliedWorkout);
			workoutRepo.SaveUserWorkoutRelation(user.getId(), workout.getId());
			AclClass workoutClass = aclClassRepository.findByClassName(Workout.class.getName());

			AclSid userSid = aclSidRepository.findBySID(String.valueOf(user.getId()));

			AclObjectIdentity workoutIdentity = aclObjectIdentityRepository
					.save(new AclObjectIdentity(workoutClass, savedWorkout.getId(), null, userSid, 0));

			aclEntryRepository.save(
					new AclEntry(workoutIdentity, 1, userSid, Permission.Admin.value, 1, true, true, user.getId()));

			return savedWorkout;
		} catch (Exception ex) {
			log.error(ex);
			throw new Exception("problem saving workout");
		}
	}

	private Workout GetMovementsToWorkout(Workout workout) {
		if (workout.getExercises() != null) {
			for (Exercise exercise : workout.getExercises()) {
				movementRepo.findByName(exercise.getMovementName());
			}
		}
		return workout;

	}

	private void connectExercisesToWorkout(Workout appliedWorkout) {
		if (appliedWorkout.getExercises() != null) {
			for (Exercise exercise : appliedWorkout.getExercises()) {
				if (exercise.getSetRepsWeights() != null) {
					for (SetRepsWeight srw : exercise.getSetRepsWeights()) {
						srw.setExercise(exercise);
					}
				}
				exercise.setWorkout(appliedWorkout);
			}
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

	// user has "administration privileges to their own workouts.
	// @PostFilter("hasPermission(filterObject, 'ADMINISTRATION')")
	@PostFilter("hasRole('ADMINISTRATION') or filterObject.owner == authentication.name")
	public List<Workout> GetUserWorkouts() {
		return workoutRepo.findAll();
	}

	// user has "administration privileges to their own workouts.
	// @PostFilter("hasPermission(filterObject, 'ADMINISTRATION')")
	@PostFilter("hasRole('ADMINISTRATION') or filterObject.owner == authentication.name")
	public List<Workout> GetWorkoutWithId(long id) {

		Workout workout = new Workout();
		workout.setId(id);
		ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("workoutId", match -> match.contains());

		Example<Workout> example = Example.of(workout, matcher);
		return workoutRepo.findAll(example);
	}

	public Set<Exercise> GetUserExercisesFromWorkoutWithId(long id) {
		CustomUserDetails user = cuserDetailService.LoadUserInfoIfUserIsLoggedIn();
		return exerciseRepo.FindAllExercisesFromWorkoutWithIDAndFromUserId(user.getUser().getId(), id);
	}

	public Set<Exercise> AddExerciseToWorkoutWithId(int ordernumber, String movementname, Long workoutId) {
		CustomUserDetails user = cuserDetailService.LoadUserInfoIfUserIsLoggedIn();
		Workout workout = workoutRepo.FindWorkoutFromUserWithIDAndWorkoutId(user.getUser().getId(), workoutId);
		Optional<Movement> movement = movementRepo.findByName(movementname);
		if (movement.isPresent()) {
			Exercise ex = new Exercise(ordernumber, movement.get());
			workout.addExercises(ex);
			workoutRepo.save(workout);
		}

		return exerciseRepo.FindAllExercisesFromWorkoutWithIDAndFromUserId(user.getUser().getId(), workoutId);
	}

	public Set<Exercise> AddSetRepWeightToExercise(Long exId, SetRepsWeight srw, long workoutId) {
		CustomUserDetails user = cuserDetailService.LoadUserInfoIfUserIsLoggedIn();
		Exercise ex = exerciseRepo.FindExercisesWithIDAndFromUserId(user.getUser().getId(), exId);
		srw.setWeightUnit("kg");
		ex.addSetRepsWeight(srw);
		exerciseRepo.save(ex);

		return exerciseRepo.FindAllExercisesFromWorkoutWithIDAndFromUserId(user.getUser().getId(), workoutId);
	}

	public Exercise SaveExerciseToWorkoutForUser(long workoutID, Exercise exercise) {
		try {
			CustomUserDetails user = cuserDetailService.LoadUserInfoIfUserIsLoggedIn();

			List<Workout> workouts = GetWorkoutWithId(workoutID);
			Workout workout = workouts.get(0);

			exercise.setWorkout(workout);
			return exerciseRepo.save(exercise);
		} catch (Exception ex) {
			log.error(ex);
			return null;
		}

	}

	public Optional<Movement> getMovementForExerciseRequest(ExerciseJson exerciseRequest) {
		Optional<Movement> movement = movementRepo.findById(exerciseRequest.getMovementIdServer());
		if (!movement.isPresent()) {
			movement = movementRepo.findByName(exerciseRequest.getMovementNameRemote());
		}
		return movement;
	}

	public SetRepsWeight SaveSetRepsWeightToExerciseForUser(long workoutId, long exerciseId,
			SetRepsWeight setRepsWeightToSave) throws Exception {

		List<Workout> workouts = GetWorkoutWithId(workoutId);

		if (workouts == null || workouts.size() == 0) {
			throw new Exception("Workout not found with given id" + workoutId);
		}
		if (workouts.size() > 1) {
			log.error("too many workouts found with id" + workoutId);
			throw new Exception("Problem finding workout with id:" + workoutId);

		}
		Workout workout = workouts.get(0);

		Exercise foundExercise = workout.getExercises().stream().filter(exercise -> exerciseId == exercise.getId())
				.findAny().orElse(null);

		if (foundExercise == null) {
			throw new Exception("Exercise not found with given id" + exerciseId);
		}
		setRepsWeightToSave.setExercise(foundExercise);
		return srwRepo.save(setRepsWeightToSave);
	}

}
