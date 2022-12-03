package fi.breakwaterworks.service;

import java.util.ArrayList;
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
import fi.breakwaterworks.model.request.ExerciseRequest;
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
			appliedWorkout.setOwner(user.getName());

			AclSid userSid = aclSidRepository.findBySID(String.valueOf(user.getId()));

			AclObjectIdentity workoutIdentity = aclObjectIdentityRepository
					.save(new AclObjectIdentity(workoutClass, savedWorkout.getId(), null, userSid, 0));

			aclEntryRepository.save(
					new AclEntry(workoutIdentity, 1, userSid, Permission.Admin.value, 1, true, true, user.getId()));

			return savedWorkout;
		} catch (Exception ex) {
			log.error(ex);
			return workout;
		}
	}

	private void connectExercisesToWorkout(Workout appliedWorkout) {
		for (Exercise exercise : appliedWorkout.getExercises()) {
			for (SetRepsWeight srw : exercise.getSetRepsWeights()) {
				srw.setExercise(exercise);
			}
			exercise.setWorkout(appliedWorkout);
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
			Optional<Movement> movement = movementRepo.findByName(exercise.getMovementName());
			if (movement.isPresent()) {
				exercise.setMovement(movement.get());
			}
		}
		return workout;
	}

	// user has "administration privileges to their own workouts.
	// @PostFilter("hasPermission(filterObject, 'ADMINISTRATION')")
	@PostFilter("hasRole('ADMINISTRATION') or filterObject.owner == authentication.name")
	public List<Workout> GetUserWorkouts() {
		return workoutRepo.findAll();
	}

	// user has "administration privileges to their own workouts.
	// @PostFilter("hasPermission(filterObject, 'ADMINISTRATION')")
	@PostFilter("hasRole('ADMINISTRATION') or filterObject.owner == authentication.name")
	public List<Workout> GetWorkoutWithId(WorkoutRequest workoutRequest) {

		Workout workout = new Workout(workoutRequest);
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

	public List<ExerciseJson> SaveExerciseToWorkoutForUser(long workoutID, List<ExerciseRequest> exercisesToSave) {

		List<ExerciseJson> responseList = new ArrayList<ExerciseJson>();
		CustomUserDetails user = cuserDetailService.LoadUserInfoIfUserIsLoggedIn();

		Workout workout = workoutRepo.FindWorkoutFromUserWithIDAndWorkoutId(user.getUser().getId(), workoutID);

		for (ExerciseRequest exerciseRequest : exercisesToSave) {

			Optional<Movement> movement = movementRepo.findById(exerciseRequest.getMovementIdServer());
			if (!movement.isPresent()) {
				movement = movementRepo.findByName(exerciseRequest.getMovementName());
			}
			if (!movement.isPresent()) {
				Exercise ex = new Exercise(exerciseRequest, movement.get());
			}
			Exercise ex = new Exercise(exerciseRequest);

			workout.addExercises(ex);
			Workout w = workoutRepo.save(workout);
			Exercise savedExercise = w.getExercises().stream()
					.filter(exercise -> exerciseRequest.getRemoteId().equals(exercise.getRemoteId())).findAny()
					.orElse(null);
			responseList.add(new ExerciseJson(savedExercise.getId(), savedExercise.getRemoteId()));
		}

		return responseList;

	}

	public List<SetRepsWeight> SaveSetRepsWeightToExerciseForUser(long workoutID, long exerciseId,
			List<SetRepsWeight> setRepsWeightRequest) {
		List<SetRepsWeight> saveSRWList = new ArrayList<SetRepsWeight>();

		CustomUserDetails user = cuserDetailService.LoadUserInfoIfUserIsLoggedIn();
		Workout workout = workoutRepo.FindWorkoutFromUserWithIDAndWorkoutId(user.getUser().getId(), workoutID);

		Exercise foundExercise = workout.getExercises().stream()
				.filter(exercise -> exerciseId == exercise.getRemoteId()).findAny().orElse(null);

		if (foundExercise != null) {
			foundExercise.addSetRepsWeights(setRepsWeightRequest);
			Exercise savedExercise = exerciseRepo.save(foundExercise);

			for (SetRepsWeight srwFromRequest : setRepsWeightRequest) {

				SetRepsWeight foundSrw = savedExercise.getSetRepsWeights().stream()
						.filter(srw -> srwFromRequest.getRemoteId() == srw.getRemoteId()).findAny().orElse(null);

				if (foundSrw != null) {
					saveSRWList.add(foundSrw);
				}
			}

		}
		{
			// TODO what error?
		}
		return saveSRWList;
	}

}
