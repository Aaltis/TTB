package fi.breakwaterworks.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fi.breakwaterworks.DAO.WorkLogRepository;
import fi.breakwaterworks.model.Exercise;
import fi.breakwaterworks.model.Movement;
import fi.breakwaterworks.model.SetRepsWeight;
import fi.breakwaterworks.model.WorkLog;
import fi.breakwaterworks.model.Workout;

@Configuration
public class JsonLoader {

	private final static Logger logger = (Logger) LogManager.getLogger(JsonLoader.class);
	
	@Autowired
	WorkLogRepository worklogRepo;


	public JsonLoader() {
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	public List<WorkLog> LoadWorkoutTemplates(Environment env) throws JSONException {
		File[] fileList = null;
		List<String> jsonList = new ArrayList<>();
		List<WorkLog> workLogs = new ArrayList<>();
		try {
			ClassPathResource resource = new ClassPathResource(env.getProperty("jsonloader.templatefolder"));
			File folder = resource.getFile();
			fileList = folder.listFiles();

		} catch (Exception ex) {
			logger.error(JsonLoader.class.getName(), "failed to load fileList", ex);
		}
		if (fileList != null) {
			for (int i = 0; i < fileList.length; i++) {
				try {
					jsonList.add(new String(Files.readAllBytes(Paths.get(fileList[i].getAbsolutePath()))));
				} catch (Exception ex) {
					logger.error(JsonLoader.class.getName(), "loadSingleFileFromAssets:", ex);
				}
			}
			for (String workLogTemplateString : jsonList) {
				try {

					Gson g = new Gson();
					WorkLog wLog = g.fromJson(workLogTemplateString, WorkLog.class);
					workLogs.add(wLog);
				} catch (Exception ex) {
					logger.error(JsonLoader.class.getName(), "failed to create json to worklog.:", ex);
				}
			}
		}

		return workLogs;
	}

	private static WorkLog jsonToWorklog(String templateJsonString) throws JSONException {
		JSONObject workLogJsonObject = new JSONObject(templateJsonString);
		WorkLog workLog = new WorkLog(workLogJsonObject.getString("name"),
				jsonWorkoutArrayToObjectList(workLogJsonObject.getJSONArray("workouts")), true);
		return workLog;
	}

	private static Set<Workout> jsonWorkoutArrayToObjectList(JSONArray workoutArray) throws JSONException {
		ArrayList<Workout> workoutArrayList = new ArrayList<>();
		for (int w = 0; w < workoutArray.length(); w++) {
			workoutArrayList.add(new Workout(workoutArray.getJSONObject(w).getString("name"),
					jsonExerciseArrayToObjectList(workoutArray.getJSONObject(w).getJSONArray("exercises")), true));
		}
		return (Set<Workout>) workoutArrayList;
	}

	private static List<Exercise> jsonExerciseArrayToObjectList(JSONArray exercicesArray) throws JSONException {
		List<Exercise> exerciseList = new ArrayList<>();
		for (int e = 0; e < exercicesArray.length(); e++) {
			exerciseList.add(new Exercise(exercicesArray.getJSONObject(e).getInt("orderNumber"),
					exercicesArray.getJSONObject(e).getString("movementName"),
					Movement.SetTypeEnum.valueOf(exercicesArray.getJSONObject(e).getString("setType")),
					jsonSetRepsWeightsArrayToObjectList(
							exercicesArray.getJSONObject(e).getJSONArray("setRepsWeights"))));
		}
		return exerciseList;
	}

	private static List<SetRepsWeight> jsonSetRepsWeightsArrayToObjectList(JSONArray setRepsWeightsArray)
			throws JSONException {
		List<SetRepsWeight> srwList = new ArrayList<>();
		for (int s = 0; s < setRepsWeightsArray.length(); s++) {
			srwList.add(new SetRepsWeight(s, setRepsWeightsArray.getJSONObject(s).getInt("set"),
					setRepsWeightsArray.getJSONObject(s).getInt("reps")));
		}
		return srwList;
	}

	public List<Movement> LoadMovements() {
		try {
			ClassPathResource resource = new ClassPathResource("exercises.txt");
			File file = resource.getFile();
			List<Movement> movements = new ArrayList<Movement>();
			try {
				List<String> allLines = Files.readAllLines(Paths.get(file.getAbsolutePath()));
				for (String line : allLines) {
					try {

						String movementName = "";
						String details = "";
						String type = "";

						String[] movement = line.split(",");

						if (movement[0].contains(" - ")) {

							String[] nameAndDetails = movement[0].split(" - ");
							movementName = nameAndDetails[0];
							details = nameAndDetails[1];
						} else {
							movementName = movement[0];
						}
						if (movement.length > 1) {

							type = movement[1];
						}
						movements.add(new Movement(movementName, details, type));

					} catch (Exception e) {
						logger.error(e);
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			return movements;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}

		return null;
	}

	public List<Role> LoadRoles(Environment env) {
		try {
			ClassPathResource resource = new ClassPathResource(env.getProperty("jsonloader.rolesandpriviledgesfile"));
			File file = resource.getFile();
			List<String> fileString = Files.readAllLines(Paths.get(file.getAbsolutePath()));
			String result = String.join("", fileString);
			List<Role> roles = (List<Role>) new Gson().fromJson(result, new TypeToken<List<Role>>(){}.getType());
			return roles;
		} catch (Exception ex) {
			logger.error(JsonLoader.class.getName(), "failed to load fileList", ex);
		}

		return null;
	}

}
