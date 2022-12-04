package fi.breakwaterworks.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.breakwaterworks.config.security.acl.model.AclSid;
import fi.breakwaterworks.model.Exercise;
import fi.breakwaterworks.model.SetRepsWeight;
import fi.breakwaterworks.model.Workout;
import fi.breakwaterworks.model.factory.WorkoutFactory;
import fi.breakwaterworks.model.factory.WorkoutJsonFactory;
import fi.breakwaterworks.model.request.SetRepsWeightJson;
import fi.breakwaterworks.response.ExerciseJson;
import fi.breakwaterworks.response.WorkoutJson;
import fi.breakwaterworks.response.WorkoutResponse;
import fi.breakwaterworks.service.MovementService;
import fi.breakwaterworks.service.WorkoutsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(value = "/api/user/workouts")
@Api(value = "UserWorkoutController")
public class WorkoutController {

	static Logger log = (Logger) LogManager.getLogger(WorkoutController.class);

	@Autowired
	private WorkoutsService workoutService;
	

	
	@Operation(summary = "Save workout for user which have this X-Auth-Token")
	@PostMapping()
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-Auth-Token", value = "Authorization token", required = true, dataType = "string", paramType = "header") })
	public ResponseEntity<?> SaveWorkoutForUser(@RequestBody Workout saveWorkoutRequest) {
		try {

			if (saveWorkoutRequest.getId() != null && saveWorkoutRequest.getId() == 0) {
				saveWorkoutRequest.setId(null);
			}
			Workout savedWorkout = workoutService.SaveWorkoutForUser(saveWorkoutRequest);
			return new ResponseEntity<>(WorkoutJsonFactory.createInstance(savedWorkout), HttpStatus.CREATED);

		} catch (Exception ex) {
			log.error(ex);
			return ResponseEntity.badRequest().body(null);
		}
	}


	@Operation(summary = "Get workout with id for user which have this X-Auth-Token or all workouts of no id.")
	@GetMapping()
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-Auth-Token", value = "Authorization token", required = true, dataType = "string", paramType = "header") })
	public ResponseEntity<?> GetWorkoutWithIdForUser(@ModelAttribute WorkoutRequest workoutRequest) {
		try {
			if (workoutRequest.getId() != 0) {
				List<Workout> workouts = workoutService.GetWorkoutWithId(workoutRequest.getId());
				if (workouts.size() == 0) {
					// TODO return error?
				}
				return new ResponseEntity<>(WorkoutJsonFactory.createInstance(workouts.get(0)), HttpStatus.CREATED);
			} else {
				List<Workout> downloadedWorkouts = workoutService.GetUserWorkouts();

				List<WorkoutJson> workoutJsons = new ArrayList<>();

				for (Workout workout : downloadedWorkouts) {
					workoutJsons.add(WorkoutJsonFactory.createInstance(workout));
				}

				return ResponseEntity.ok(new WorkoutResponse(workoutJsons));
			}
		} catch (Exception ex) {
			
			
			log.error(ex);
			return ResponseEntity.badRequest().body(null);
		}
	}
}
