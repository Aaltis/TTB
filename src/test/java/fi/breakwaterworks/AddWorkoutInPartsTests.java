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
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
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

import fi.breakwaterworks.response.ExerciseJson;

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
	private long setRepWeightId;
	private String token;

	@Test
	void contextLoads() {
		assertThat(restTemplate).isNotNull();
	}
	
	
	@Test
	@Order(5)
	void AddExerciseToWorkout() throws IOException {
		String url = "http://localhost:" + port + "/api/user/workouts/" + workoutId;

		JSONObject jsonObj = new JSONObject(ReadFile("testdata/addexercisetoworkout.json"));

		HttpEntity<String> request = new HttpEntity<String>(jsonObj.toString(), HeadersWithToken());
		ResponseEntity<ExerciseJson> response = restTemplate.exchange(url, HttpMethod.POST, request,
				ExerciseJson.class);

		assertEquals("Assert that workout create return HttpStatus.OK", response.getStatusCode(), HttpStatus.CREATED);

		assertNotEquals("Assert that workout has non-zero serverId", response.getBody().getServerId(), 0);
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
