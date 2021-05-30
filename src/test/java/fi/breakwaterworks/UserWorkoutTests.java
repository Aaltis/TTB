package fi.breakwaterworks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//for local testing
//@ActiveProfiles("dev")
public class UserWorkoutTests {

	//@Value("${tests.createworkouttest}")
	//private String createworkouttest;

	@LocalServerPort
	private static int port;

	private String token;

	private static RestTemplate restTemplate = new RestTemplate();

	
	//create user for test.
	@BeforeClass
	public void setup() {
		String url = "http://localhost:" + port + "/api/register";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		JSONObject userCreateObject = new JSONObject();
		userCreateObject.put("name", "Seppo");
		userCreateObject.put("password", "seppo111");
		HttpEntity<String> request = new HttpEntity<String>(userCreateObject.toString(), headers);
		String response = restTemplate.postForObject(url, request, String.class);
		token = new JSONObject(response).getString("token");
	}

	@Test
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
		String response = restTemplate.postForObject(url, request, String.class);
		String g="";
	}

}
