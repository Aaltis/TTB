package fi.breakwaterworks.model;

import java.util.ArrayList;
import java.util.List;

import fi.breakwaterworks.model.request.SetRepsWeightJson;
import fi.breakwaterworks.response.ExerciseJson;
import fi.breakwaterworks.response.WorkoutJson;

public class WorkoutJsonFactory {
	
	public static WorkoutJson createInstance(Workout workout) {

		WorkoutJson workoutRepsonse = new WorkoutJson(workout);
		List<ExerciseJson> exerciseResponses = new ArrayList<>();
		if (workout.getExercises() != null) {
			for (Exercise exercise : workout.getExercises()) {
				ExerciseJson exerciseResponse = new ExerciseJson(exercise);
				List<SetRepsWeightJson> srwResponses = new ArrayList<>();
				if (exercise.getSetRepsWeights() != null) {

					for (SetRepsWeight srw : exercise.getSetRepsWeights()) {
						srwResponses.add(new SetRepsWeightJson(srw));
					}
				}
				exerciseResponse.setSetRepsWeight(srwResponses);
				exerciseResponses.add(exerciseResponse);
			}
		}
		workoutRepsonse.setExercises(exerciseResponses);
		return workoutRepsonse;

	}
}
