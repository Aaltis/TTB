package fi.breakwaterworks.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

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
	
	@Value("${spring.profiles.active}")
	private String activeProfile;

	public JsonLoader() {
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	public List<WorkLog> LoadWorkoutTemplates(String profile, Environment env) throws JSONException {
		List<String> workLogTemplateJsonList = new ArrayList<>();
		List<WorkLog> workLogs = new ArrayList<>();
		String foldername=env.getProperty("jsonloader.templatefolder");
		try {
			


			if (profile.contains("docker") || profile.contains("jar")) {
				ClassLoader cl = this.getClass().getClassLoader();
				ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
				String path="classpath:"+foldername+"*.json";
				logger.info(path);
				Resource[] resources = resolver.getResources(path);
				logger.info(resources.toString());

				logger.info("Found "+resources.length+" templates");
				for (Resource file: resources){
					if (file.isReadable()) {
						logger.info("File is readable.");

						InputStream in = file.getInputStream();
						BufferedReader br = new BufferedReader(new InputStreamReader(in));
						String line;
						StringBuilder sb = new StringBuilder();
						while ((line = br.readLine()) != null) {
							sb.append(line);
						}
						workLogTemplateJsonList.add(sb.toString());
						br.close();
						in.close();
					}
				}
			}
			else{
				ClassPathResource resource = new ClassPathResource(foldername);
				File folder = resource.getFile();
				File[] fileList = folder.listFiles();
				logger.info("found "+fileList.length+" template files.");

				for (int i = 0; i < fileList.length; i++) {
					try {
						workLogTemplateJsonList
								.add(new String(Files.readAllBytes(Paths.get(fileList[i].getAbsolutePath()))));
					} catch (Exception ex) {
						logger.error(JsonLoader.class.getName(), "loadSingleFileFromAssets:", ex);
					}
				}
			}
		} catch (Exception ex) {
			logger.error(JsonLoader.class.getName(), "failed to load fileList", ex);
		}
		logger.info("Amount of worklog templates found:" + workLogTemplateJsonList.size());

		for (String workLogTemplateString : workLogTemplateJsonList) {
			try {
				Gson g = new Gson();
				WorkLog wLog = g.fromJson(workLogTemplateString, WorkLog.class);
				workLogs.add(wLog);
			} catch (Exception ex) {
				logger.error(JsonLoader.class.getName(), "failed to create json to worklog:", ex);
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

	public List<Movement> LoadMovements(String profile, Environment env) {
		try {
			List<String> movementLines = new ArrayList<String>();
			logger.debug(profile);
			
			if (profile.contains("docker") || profile.contains("jar")) {
				movementLines = ReadFromJarResources(env.getProperty("jsonloader.exercisesfile"));

			}else {
				ClassPathResource resource = new ClassPathResource(env.getProperty("jsonloader.exercisesfile"));
				File file = resource.getFile();
				movementLines= Files.readAllLines(Paths.get(file.getAbsolutePath()));

			}
			List<Movement> movements = new ArrayList<Movement>();
			try {
				for (String line : movementLines) {
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
					}
				}
			} catch (Exception e) {
				logger.error(e);
			}

			return movements;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}

		return null;
	}

	public List<Role> LoadRoles(String profile, Environment environment) {
		try {			
			// https://medium.com/@jonathan.henrique.smtp/reading-files-in-resource-path-from-jar-artifact-459ce00d2130
			if (profile.contains("docker") || profile.contains("jar")) {
				List<String> resourceStringList = ReadFromJarResources(
						environment.getProperty("jsonloader.rolesandpriviledgesfile"));
				List<Role> roles = (List<Role>) new Gson().fromJson(String.join("", resourceStringList),
						new TypeToken<List<Role>>() {
						}.getType());
				return roles;
			}else {
				File file = new ClassPathResource(
						environment.getProperty("jsonloader.rolesandpriviledgesfile")).getFile();
				List<String> fileString = Files.readAllLines(Paths.get(file.getAbsolutePath()));
				String result = String.join("", fileString);
				List<Role> roles = (List<Role>) new Gson().fromJson(result, new TypeToken<List<Role>>() {
				}.getType());
				return roles;
			}
		} catch (Exception ex) {
			logger.error(JsonLoader.class.getName(), "failed to load fileList", ex);
		}

		return null;
	}

	// https://medium.com/@jonathan.henrique.smtp/reading-files-in-resource-path-from-jar-artifact-459ce00d2130
	public List<String> ReadFromJarResources(String fileName) throws Exception {
		List<String> textList = new ArrayList<String>();
		try {
			ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext();
			logger.debug("Loading file "+fileName+" from resources.");

			Resource file = appContext.getResource("classpath:" + fileName);
			if (file.isReadable()) {
				logger.debug("File is readable.");

				InputStream in = file.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String line;
				while ((line = br.readLine()) != null) {
					textList.add(line);
				}
				br.close();
				in.close();
				appContext.close();
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		return textList;

	}
}
