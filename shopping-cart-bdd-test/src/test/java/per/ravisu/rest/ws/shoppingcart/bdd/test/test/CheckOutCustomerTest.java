package per.ravisu.rest.ws.shoppingcart.bdd.test.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CheckOutCustomerTest {

	private static final Logger log = LoggerFactory.getLogger(CheckOutCustomerTest.class);

	int responseValue;
	String resultName;

	@Value("${localhost.uri.ravisu}")
	private static String localhostvalue;

	@Autowired
	private Environment environment;

	ResponseEntity<String> responseEntity = null;

	@When("^client requests POST on (.*) with json data:$")
	public String client_requests_POST_on(String resourceUri, String jsonData) throws Throwable {
				

		log.info("Config URL from test case :" + resourceUri);

		log.info("Localhost value read as :" + localhostvalue);
		
		if((localhostvalue==null) && (environment!=null)){
			log.info("Value read from env : " + environment.getProperty("localhost.uri.ravisu"));
			localhostvalue=environment.getProperty("localhost.uri.ravisu");
		}
		
		if (localhostvalue != null) {
			resourceUri = resourceUri.replace("localhost.uri.ravisu.test", localhostvalue);
		}else
		{
			resourceUri= resourceUri.replace("localhost.uri.ravisu.test", "localhost");
		}
		
		log.info("Connecting to URL after getting value from properties file :" + resourceUri);

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("User-Agent", "Mozilla/5.0"); // MyClientLibrary/2.0

		String payload = jsonData;
		HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);

		responseEntity = restTemplate.exchange(resourceUri, HttpMethod.POST, requestEntity, String.class);

		responseValue = Integer.parseInt(responseEntity.getStatusCode().toString());

		log.info("client_requests_POST_on: the response value is :" + responseEntity.getBody());

		return responseEntity.getBody();
	}

	@Then("^the response status should be (\\d*)$")
	public void the_resonse_status_code_should_be(int statusCode) throws Throwable {
		log.info("the_resonse_status_code_should_be: responseValue :" + responseValue);
		assertEquals("status code respone: " + responseValue + " match with test data : " + statusCode, responseValue,
				statusCode);

	}

	@And("^the response should not contain empty array")
	public void the_resonse_status_not_contain_empty_array() throws Throwable {

		JSONObject jsonObject = new JSONObject(responseEntity.getBody().toString());
		Long id = jsonObject.getLong("orderId");

		log.info("the_resonse_status_not_contain_empty_array: orderId is :" + id);

		assertTrue("order created with order id : " + id + " ", id > 0);
	}

}
