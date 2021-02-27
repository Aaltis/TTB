package fi.breakwaterworks.controller;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.breakwaterworks.model.Movement;
import fi.breakwaterworks.service.MovementService;

@RestController
@RequestMapping(value = "/api/movement")

public class MovementRestController {

	static Logger log = (Logger) LogManager.getLogger(MovementRestController.class);

	@Autowired
	private MovementService mService;

	/*@RequestMapping(consumes = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public String createMovement(@RequestBody Movement movement) {
		String movementID = "";
		try {
			mService.CreateMovement(movement);
		} catch (Exception ex) {
			log.error(ex.toString());
			return "Error creating the movement: " + ex.toString();
		}
		return "Movement succesfully created with id = " + movementID;
	}*/

	@GetMapping()
	@ResponseBody
	public List<Movement> FindByName(@RequestParam Map<String, String> params) {
		List<Movement> movements = null;

		try {
			if (params.containsKey("name") && params.containsKey("type")) {
				return mService.FindMovementsByNameAndType(params.get("name"), params.get("type"));
			} else if (params.containsKey("name")) {
				return mService.FindMovementsByName(params.get("name"));
			} else if (params.containsKey("type")) {
				return mService.FindMovementsByType(params.get("type"));
			}
		} catch (Exception ex) {
			log.error(ex.toString());
			// return "movement not found: " + ex.toString();
		}
		return movements;
	}

	@RequestMapping(value = "/all", consumes = "application/json", method = RequestMethod.GET)
	@ResponseBody
	public List<Movement> getAll() {
		try {
			return mService.getAll();
		} catch (Exception ex) {
			log.error(ex.toString());
			// return "failure to get movements:"+ex.;
		}
		return null;
	}

}
