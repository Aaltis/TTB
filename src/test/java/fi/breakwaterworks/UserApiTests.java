package fi.breakwaterworks;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//for local testing
//@ActiveProfiles("dev")
public class UserApiTests {
	private final Logger logger = (Logger) LogManager.getLogger(UserApiTests.class);

	@LocalServerPort
	private int port;

	private RestTemplate restTemplate = new RestTemplate();

	@Test
	void contextLoads() {
		assertThat(restTemplate).isNotNull();
	}

	@Test
	void CreateUser() {
		String url = "http://localhost:" + port + "/api/register";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		JSONObject userCreateObject = new JSONObject();
		userCreateObject.put("name", "Seppo");
		userCreateObject.put("password", "seppo");
		HttpEntity<String> request = new HttpEntity<String>(userCreateObject.toString(), headers);
		String response = restTemplate.postForObject(url, request, String.class);
		String token = new JSONObject(response).getString("token");
		Assert.assertNotNull("token must not be null", token);

	}

	@Test
	void TestLoginUser() {
		try {
			String url = "http://localhost:" + port + "/api/login";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			JSONObject userLoginObject = new JSONObject();
			userLoginObject.put("username", "Seppo");
			userLoginObject.put("password", "seppo111");
			HttpEntity<String> request = new HttpEntity<String>(userLoginObject.toString(), headers);
			String response = restTemplate.postForObject(url, request, String.class);
			String token = new JSONObject(response).getString("token");
			Assert.assertNotNull("token must not be null", token);
		} catch (Exception ex) {
			logger.error(ex);
		}
	}

}
