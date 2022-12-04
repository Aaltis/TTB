package fi.breakwaterworks.controller;

import java.util.ArrayList;
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

import fi.breakwaterworks.exception.FactoryValidationException;
import fi.breakwaterworks.model.SetRepsWeight;
import fi.breakwaterworks.model.factory.SetRepsWeightFactory;
import fi.breakwaterworks.model.factory.SetRepsWeightJsonFactory;
import fi.breakwaterworks.model.request.SetRepsWeightJson;
import fi.breakwaterworks.service.WorkoutsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(value = "/api/user/workouts")
@Api(value = "SetRepsWeightController")
public class SetRepsWeightController {

	static Logger log = (Logger) LogManager.getLogger(SetRepsWeightController.class);

	@Autowired
	private WorkoutsService workoutService;

	@Operation(summary = "Save setRepsWeight to exercise for user which have this X-Auth-Token")
	@PostMapping("/{workoutId}/exercises/{exerciseId}/setrepsweight")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-Auth-Token", value = "Authorization token", required = true, dataType = "string", paramType = "header") })
	public ResponseEntity<?> SaveSetRepsWeightToExerciseForUser(@PathVariable long workoutId,
			@PathVariable long exerciseId,
			@RequestBody SetRepsWeightJson setRepsWeightJson) {
		try {
			SetRepsWeight savedSRW = workoutService.SaveSetRepsWeightToExerciseForUser(workoutId, exerciseId,
					SetRepsWeightFactory.createInstance(setRepsWeightJson));

			SetRepsWeightJson srwJson = SetRepsWeightJsonFactory.createInstance(savedSRW);
			return new ResponseEntity<SetRepsWeightJson>(srwJson, HttpStatus.CREATED);

		}catch (FactoryValidationException ex) {
			log.error(ex);
			return ResponseEntity.badRequest().body(ex.getMessage());
		} catch (Exception ex) {
			log.error(ex);
			return ResponseEntity.badRequest().body(null);
		}
	}

}
