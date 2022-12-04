package fi.breakwaterworks.model.factory;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import fi.breakwaterworks.model.Exercise;
import fi.breakwaterworks.model.Movement;
import fi.breakwaterworks.model.SetRepsWeight;
import fi.breakwaterworks.model.request.SetRepsWeightJson;
import fi.breakwaterworks.response.ExerciseJson;

public class ExerciseFactory {

	public static Exercise createInstance(ExerciseJson exerciseRequest, Optional<Movement> movement) throws Exception {

		Exercise exercise = new Exercise();
		if (movement != null && movement.isPresent()) {
			exercise = new Exercise(exerciseRequest, movement.get());
		} else {
			exercise = new Exercise(exerciseRequest);
		}
		if (exerciseRequest.getSetRepsWeight() != null) {
			Set<SetRepsWeight> srwList = new HashSet();
			for (SetRepsWeightJson srw : exerciseRequest.getSetRepsWeight()) {
				srwList.add(SetRepsWeightFactory.createInstance(srw));
			}
			exercise.setSetRepsWeights(srwList);
		}
		return exercise;

	}

	public static Exercise createInstanceWithoutSetRepsWeight(ExerciseJson exerciseRequest) throws Exception {
		return new Exercise(exerciseRequest);
	}

}
