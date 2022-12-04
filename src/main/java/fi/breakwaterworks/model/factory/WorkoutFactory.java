package fi.breakwaterworks.model.factory;

import java.util.HashSet;
import java.util.Set;

import fi.breakwaterworks.model.Exercise;
import fi.breakwaterworks.model.SetRepsWeight;
import fi.breakwaterworks.model.Workout;
import fi.breakwaterworks.model.request.SetRepsWeightJson;
import fi.breakwaterworks.response.ExerciseJson;
import fi.breakwaterworks.response.WorkoutJson;

public class WorkoutFactory {
	public static Workout createInstance(WorkoutJson saveWorkoutRequest) throws Exception {

		Workout workout = new Workout(saveWorkoutRequest);
		if (saveWorkoutRequest.getExercises() != null) {
			Set<Exercise> exercises = new HashSet<Exercise>();
			for (ExerciseJson exerciseJson : saveWorkoutRequest.getExercises()) {
				Exercise exercise = ExerciseFactory.createInstanceWithoutSetRepsWeight(exerciseJson);
				exercise.setWorkout(workout);
				if (exerciseJson.getSetRepsWeight() != null) {
					Set<SetRepsWeight> srwList = new HashSet();
					for (SetRepsWeightJson srw : exerciseJson.getSetRepsWeight()) {
						SetRepsWeight setRepsWeight = SetRepsWeightFactory.createInstance(srw);
						setRepsWeight.setExercise(exercise);
						srwList.add(setRepsWeight);
					}
					exercise.setSetRepsWeights(srwList);
				}
				exercises.add(exercise);
			}
			workout.setExercises(exercises);

		}
		return workout;
	}
}
