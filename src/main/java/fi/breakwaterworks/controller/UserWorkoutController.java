package fi.breakwaterworks.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.breakwaterworks.model.AuthenticationResponse;
import fi.breakwaterworks.model.Workout;
import fi.breakwaterworks.response.WorkoutResponse;
import fi.breakwaterworks.service.WorkoutsService;

@RestController
@RequestMapping(value = "/api/user/workouts")

public class UserWorkoutController {

	static Logger log = (Logger) LogManager.getLogger(UserWorkoutController.class);

	
	@Autowired
	private WorkoutsService workoutService;

	@GetMapping()
	@ResponseBody
	public ResponseEntity<?> GetWorkouts(HttpServletRequest request) {
		try {
			List<Workout> result = workoutService.GetUserWorkouts();
			return ResponseEntity.ok(new WorkoutResponse(result));
		} catch (Exception ex) {
			log.error(ex);
			return ResponseEntity.badRequest().body(null);
		}
	}

	@PostMapping()
	@ResponseBody
	public ResponseEntity<?> SaveWorkoutForUser(@RequestBody Workout request) {
		try {
			workoutService.SaveWorkoutForUser(request);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception ex) {
			log.error(ex);
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	
}

