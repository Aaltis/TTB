package fi.breakwaterworks.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.breakwaterworks.model.Movement;
import fi.breakwaterworks.model.request.MovementRequest;
import fi.breakwaterworks.service.MovementService;

@RestController
@RequestMapping(value = "/api/movement")

public class MovementRestController {

	static Logger log = (Logger) LogManager.getLogger(MovementRestController.class);

	@Autowired
	private MovementService mService;

	// TODO should this be possible for admins?
	/*
	 * @RequestMapping(consumes = "application/json", method = RequestMethod.POST)
	 * 
	 * @ResponseBody public String createMovement(@RequestBody Movement movement) {
	 * String movementID = ""; try { mService.CreateMovement(movement); } catch
	 * (Exception ex) { log.error(ex.toString()); return
	 * "Error creating the movement: " + ex.toString(); } return
	 * "Movement succesfully created with id = " + movementID; }
	 */

	@GetMapping()
	@ResponseBody
	public List<Movement> FindMovement(@ModelAttribute MovementRequest request) {
		List<Movement> movements = null;

		try {
			return mService.FindMovements(request);

		} catch (Exception ex) {
			log.error(ex.toString());
			// return "movement not found: " + ex.toString();
		}
		return movements;
	}

}
