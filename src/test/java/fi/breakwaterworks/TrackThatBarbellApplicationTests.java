package fi.breakwaterworks;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import fi.breakwaterworks.model.Movement;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//for local testing
//@ActiveProfiles("dev")
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

}
