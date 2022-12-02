package fi.breakwaterworks.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.breakwaterworks.model.request.ExerciseRequest;
import fi.breakwaterworks.response.ExerciseJson;
import fi.breakwaterworks.service.WorkoutsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(value = "/api/user/workouts")
@Api(value = "ExerciseController")
public class ExerciseController {
	static Logger log = (Logger) LogManager.getLogger(ExerciseController.class);

	@Autowired
	private WorkoutsService workoutService;
	
	@Operation(summary = "Save exercise to workout for user which have this X-Auth-Token")
	@PostMapping("{workoutID}/exercises" )
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-Auth-Token", value = "Authorization token", required = true, dataType = "string", paramType = "header") })
	public ResponseEntity<List<ExerciseJson>> SaveExerciseToWorkoutForUser(@PathVariable long workoutId, @RequestBody List<ExerciseRequest> request) {
		try {
			List<ExerciseJson> response = workoutService.SaveExerciseToWorkoutForUser(workoutId,request);
			return new ResponseEntity<>(response, HttpStatus.CREATED);

		} catch (Exception ex) {
			log.error(ex);
			return ResponseEntity.badRequest().body(null);
		}
	}
}
