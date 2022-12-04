package fi.breakwaterworks.model.factory;

import fi.breakwaterworks.model.Exercise;
import fi.breakwaterworks.response.ExerciseJson;

public class ExerciseJsonFactory {
    public static ExerciseJson createInstance(Exercise exercise) throws Exception {

        ExerciseJson json = new ExerciseJson(exercise);
        if(exercise.getMovement()!=null){
            json.setMovementIdRemote(exercise.getMovement().getId());
            json.setMovementNameServer(exercise.getMovement().getName());
        }
        return json;
	}

}
