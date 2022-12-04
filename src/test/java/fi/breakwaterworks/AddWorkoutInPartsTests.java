package fi.breakwaterworks;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

import org.json.JSONObject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import fi.breakwaterworks.model.request.SetRepsWeightJson;
import fi.breakwaterworks.response.ExerciseJson;
import fi.breakwaterworks.response.LoginResponse;
import fi.breakwaterworks.response.WorkoutJson;
import fi.breakwaterworks.response.WorkoutResponse;
import fi.breakwaterworks.service.UserService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//for local testing
@ActiveProfiles("dev")
@RunWith(JUnit4.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class AddWorkoutInPartsTests {

	@LocalServerPort
	private int port;
	private RestTemplate restTemplate = new RestTemplate();
	private long workoutId;
	private long exerciseId;
	private String token;
	private String username;
	
	@Autowired
	private UserService userService;
	
	@Test
	void contextLoads() {
		assertThat(restTemplate).isNotNull();
	}
	
	@BeforeAll
	public void createUser(){		
		username = "seppo";
		String password = "seppo";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		JSONObject userCreateObject = new JSONObject();
		userCreateObject.put("username", username);
		userCreateObject.put("password", password);
		userCreateObject.put("repassword", password);

		HttpEntity<String> request = new HttpEntity<String>(userCreateObject.toString(), headers);

		restTemplate.exchange("http://localhost:" + port + "/api/register", HttpMethod.POST, request, String.class);		

		ResponseEntity<LoginResponse> response = restTemplate.exchange("http://localhost:" + port + "/api/login", HttpMethod.POST, request,
				LoginResponse.class);
		token = response.getBody().getToken();
	}
	
	
	@Test
	@Order(1)
	void SaveOnlyWorkout() throws IOException {
		String url = "http://localhost:" + port + "/api/user/workouts";
		JSONObject jsonObj = new JSONObject(ReadFile("testdata/onlyworkout.json"));
		ResponseEntity<WorkoutJson> response = restTemplate.exchange(url, HttpMethod.POST,
				new HttpEntity<String>(jsonObj.toString(), HeadersWithToken()), WorkoutJson.class);
		
		assertEquals("Assert that workout create return HttpStatus.CREATED", response.getStatusCode(), HttpStatus.CREATED);

		assertNotEquals("Assert that workout has non-zero serverId", response.getBody().getServerId(), 0);
		
		workoutId = response.getBody().getServerId();
	}
	
	@Test
	@Order(2)
	void AddExerciseToWorkout() throws IOException {
		String url = "http://localhost:" + port + "/api/user/workouts/" + workoutId+"/exercises";

		JSONObject jsonObj = new JSONObject(ReadFile("testdata/onlyexercise.json"));

		HttpEntity<String> addExerciseToWorkoutRequest = new HttpEntity<String>(jsonObj.toString(), HeadersWithToken());
		ResponseEntity<ExerciseJson> addExerciseToWorkoutResponse = restTemplate.exchange(url, HttpMethod.POST, addExerciseToWorkoutRequest,
				ExerciseJson.class);
		assertEquals("Assert that workout create return HttpStatus.CREATED", addExerciseToWorkoutResponse.getStatusCode(), HttpStatus.CREATED);

		assertNotEquals("Assert that has non-zero serverId", addExerciseToWorkoutResponse.getBody().getServerId(), 0);
		assertNotEquals("Assert that non-zero remoteId", addExerciseToWorkoutResponse.getBody().getRemoteId(), 0);

		HttpEntity<String> getFullWorkoutRequest = new HttpEntity<String>(new JSONObject("{name:fug}").toString(),
				HeadersWithToken());
		ResponseEntity<WorkoutResponse> getFullWorkoutResponse = restTemplate.exchange("http://localhost:" + port + "/api/user/workouts", HttpMethod.GET, getFullWorkoutRequest,
				WorkoutResponse.class);

		assertEquals("Assert that there is one exercise in workout", getFullWorkoutResponse.getBody().getWorkouts().get(0).getExercises().size(), 1);
		assertNotEquals("Assert that there is serverId", getFullWorkoutResponse.getBody().getWorkouts().get(0).getExercises().get(0).getServerId(), 0);
		assertNotEquals("Assert that there is remoteId", getFullWorkoutResponse.getBody().getWorkouts().get(0).getExercises().get(0).getRemoteId(), 0);

		exerciseId = addExerciseToWorkoutResponse.getBody().getServerId();

	}
	
	@Test
	@Order(3)
	void AddSetRepsWeightToExercise() throws IOException {
		String url = "http://localhost:" + port + "/api/user/workouts/"+workoutId+"/exercises/"+exerciseId+"/setrepsweight";

		JSONObject jsonObj = new JSONObject(ReadFile("testdata/onlysetrepsweight.json"));

		HttpEntity<String> addSRWrequest = new HttpEntity<String>(jsonObj.toString(), HeadersWithToken());
		ResponseEntity<SetRepsWeightJson> addSRWresponse = restTemplate.exchange(url, HttpMethod.POST, addSRWrequest,
				SetRepsWeightJson.class);

		assertEquals("Assert that workout create return HttpStatus.CREATED", addSRWresponse.getStatusCode(), HttpStatus.CREATED);
		assertNotEquals("Assert that workout has non-zero serverId", addSRWresponse.getBody().getServerId(), 0);
		assertNotEquals("Assert that there is remoteId", addSRWresponse.getBody().getRemoteId(), 0);
		
		HttpEntity<String> getFullWorkoutRequest = new HttpEntity<String>(new JSONObject("{name:fug}").toString(),
				HeadersWithToken());
		ResponseEntity<WorkoutResponse> getFullWorkoutResponse = restTemplate.exchange("http://localhost:" + port + "/api/user/workouts", HttpMethod.GET, getFullWorkoutRequest,
				WorkoutResponse.class);

		assertEquals("Assert that there is one exercise in workout", getFullWorkoutResponse.getBody().getWorkouts().get(0).getExercises().get(0).getSetRepsWeight().size(), 1);
		assertNotEquals("Assert that there is serverId", getFullWorkoutResponse.getBody().getWorkouts().get(0).getExercises().get(0).getSetRepsWeight().get(0).getServerId(), 0);
		assertNotEquals("Assert that there is remoteId", getFullWorkoutResponse.getBody().getWorkouts().get(0).getExercises().get(0).getSetRepsWeight().get(0).getRemoteId(), 0);

	}
	
	@AfterAll
	public void clean() throws Exception {
		userService.DeleteUserDataWithName(username);
	}

	String ReadFile(String fileLocation) throws IOException {
		return String.join("",
				Files.readAllLines(Paths.get(new ClassPathResource(fileLocation).getFile().getAbsolutePath())));
	}
	
	
	
	HttpHeaders HeadersWithToken() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.set("X-Auth-Token", token);

		return headers;
	}

}
