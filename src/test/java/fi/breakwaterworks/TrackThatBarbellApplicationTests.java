package fi.breakwaterworks;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.google.gson.Gson;

import fi.breakwaterworks.model.Movement;
import fi.breakwaterworks.response.LoginResponse;
import fi.breakwaterworks.service.UserService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//for local testing
@ActiveProfiles("dev")
@RunWith(JUnit4.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
class TrackThatBarbellApplicationTests {

	@LocalServerPort
	private int port;

	private RestTemplate restTemplate = new RestTemplate();

	@Test
	void contextLoads() {
		assertThat(restTemplate).isNotNull();
	}

	@Test
	void TestGetMovementsWithNameBench() {

		String url = "http://localhost:" + port + "/api/movement?name=bench";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		Gson gson = new Gson(); // Or use new GsonBuilder().create();
		List<Movement> movements = gson.fromJson(response.getBody(), ArrayList.class);
		Assert.assertEquals(200, response.getStatusCodeValue());
		Assert.assertNotNull("movement mustn't be null", movements);

	}

	@Value("${tests.createworkouttest:testdata/createuserworkout.json")
	private String createworkouttest;

	private String token;
	private String username = "seppo";
	private String password = "seppo";

	// create user for test.
	@Test
    @Order(1) 
	public void CreateUser() {

		String url = "http://localhost:" + port + "/api/register";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		JSONObject userCreateObject = new JSONObject();
		userCreateObject.put("username", username);
		userCreateObject.put("password", password);
		userCreateObject.put("repassword", password);

		HttpEntity<String> request = new HttpEntity<String>(userCreateObject.toString(), headers);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
		assertEquals("Assert that user create return HttpStatus.CREATED", response.getStatusCode(), HttpStatus.CREATED);
	}

	@Test
    @Order(2) 
	public void LoginUser() {

		String url = "http://localhost:" + port + "/api/login";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		JSONObject userCreateObject = new JSONObject();
		userCreateObject.put("username", username);
		userCreateObject.put("password", password);

		HttpEntity<String> request = new HttpEntity<String>(userCreateObject.toString(), headers);

		ResponseEntity<LoginResponse> response = restTemplate.exchange(url, HttpMethod.POST, request,
				LoginResponse.class);
		token = response.getBody().getToken();
		assertEquals("Assert that login returns HttpStatus.OK", response.getStatusCode(), HttpStatus.OK);
	}

	@Test
    @Order(3) 
	void CreateUserWorkout() throws IOException {

		ClassPathResource resource = new ClassPathResource("testdata/createuserworkout.json");
		File file = resource.getFile();
		List<String> fileStrings = Files.readAllLines(Paths.get(file.getAbsolutePath()));
		String joined = String.join("", fileStrings);

		String url = "http://localhost:" + port + "/api/user/workouts";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.set("X-Auth-Token", token);
		JSONObject jsonObj = new JSONObject(joined);

		HttpEntity<String> request = new HttpEntity<String>(jsonObj.toString(), headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

		assertEquals("Assert that workout create return HttpStatus.CREATED", response.getStatusCode(),
				HttpStatus.CREATED);
	}
	
	@Test
    @Order(4) 
	void GetUserWorkouts() throws IOException {
		String url = "http://localhost:" + port + "/api/user/workouts";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.set("X-Auth-Token", token);

		HttpEntity<String> request = new HttpEntity<String>(new JSONObject("{name:fug}").toString(), headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

		assertEquals("Assert that workout create return HttpStatus.OK", response.getStatusCode(),
				HttpStatus.OK);
	}

	@Autowired
	private UserService uservice;

	@AfterAll
	public void clean() throws Exception {
		uservice.DeleteUserDataWithName(username);
	}

}
