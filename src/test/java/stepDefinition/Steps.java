package stepDefinition;

import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;
import common.Utilities;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import common.Log;

public class Steps {

	private static Response response;
	public static List<String> nowPlayingList = new ArrayList<String>();
	public static JSONObject microService;

	@After
	public void after(Scenario scenario) {
		Log.endTestCase(scenario.getName(), scenario.getStatus());
	}

	@Given("^I am using \"([^\"]*)\"$")
	public void i_am_using_media_service(Object service) throws Throwable {
		microService = (JSONObject) Utilities.getService(service.toString());
		Log.info("The name of the service is " + service.toString());
	}

	@When("^I send \"([^\"]*)\" Request$")
	public void i_send_Request(String requestMethod) throws Throwable {
		String URL = Utilities.getValue("BaseURL");
		RestAssured.baseURI = URL;
		RequestSpecification request = RestAssured.given();
		Log.info("The request method is " + requestMethod);
		String path = "";
		switch (requestMethod) {
		case "GET":
			path = (String) microService.get("getEndPoint");
			break;
		case "POST":
			path = (String) microService.get("postEndPoint");
			break;
		}
		Log.info("The EndPoint is " + URL + path);
		response = request.get(path);
	}

	@Then("^status code is (\\d+)$")
	public void status_code_is(int expectedStatusCode) throws Throwable {
		int actualStatusCode = response.getStatusCode();
		Log.info("The status code is " + actualStatusCode);
		assertTrue("The status code " + expectedStatusCode + " is not matching " + actualStatusCode,
				actualStatusCode == expectedStatusCode);
	}

	@Then("^response time is below (\\d+) milliseconds$")
	public void response_time_is_below_milliseconds(int expectedResponseTime) throws Throwable {
		int actualResponseTime = (int) response.getTime();
		Log.info("The response time is " + actualResponseTime);
		assertTrue("The response time " + actualResponseTime + " is more than expected response time " + expectedResponseTime + " milliseconds ",
				actualResponseTime <= expectedResponseTime);
	}

	@Then("^the \"([^\"]*)\" field is neither empty nor null for all the items$")
	public void the_id_field_is_neither_empty_nor_null_for_all_the_items(String id) throws Throwable {
		JsonPath jsonPath = response.jsonPath();
		jsonPath.getList("data." + id).forEach(dataId -> {
			if (!(dataId.toString().isEmpty()) || dataId != null) {
				assertTrue("The " + id + " value is expected to be alphnumeric instead was " + dataId,
						dataId.toString().matches("^[a-zA-Z0-9]*$"));
			}
		});
	}

	@Then("^the \"([^\"]*)\" field in \"([^\"]*)\" is neither empty nor null for all the items$")
	public void the_field_in_is_neither_empty_nor_null_for_all_the_items(String field1, String field2)
			throws Throwable {
		JsonPath jsonPath = response.jsonPath();
		jsonPath.getList("data." + field2 + "." + field1).forEach(value -> {
			assertTrue("The " + field1 + " value " + value + " is not as expected ",
					!(value.toString().isEmpty()) || (value != null));

		});
	}

	@When("^the \"([^\"]*)\" for all track is \"([^\"]*)\"$")
	public void all_track_is(String key, String expectedValue) throws Throwable {
		JsonPath jsonPath = response.jsonPath();
		jsonPath.getList("data." + key).forEach(actualValue -> {
			assertTrue("The " + actualValue + "for" + key + " is not matching with the " + expectedValue,
					actualValue.toString().equalsIgnoreCase(expectedValue));
		}

		);

	}

	@Then("^only one track has now_playing offset as true$")
	public void only_one_track_has_now_playing_offset_as_true() throws Throwable {
		JsonPath jsonPath = response.jsonPath();
		jsonPath.getList("data.offset.now_playing").forEach(value -> {
			String nowPlayingValue = value.toString();
			if (nowPlayingValue.equalsIgnoreCase("true")) {
				nowPlayingList.add(value.toString());
			}
		});
		assertTrue("The now_playing offset value is true for " + nowPlayingList.size() + " tracks",
				nowPlayingList.size() == 1);
	}

	@When("^the response header has \"([^\"]*)\" value as \"([^\"]*)\"$")
	public void the_response_header_has_value_as(String key, String expectedValue) throws Throwable {
		String actualValue = response.getHeader("Date");
		assertTrue("The " + actualValue + "for" + key + " is not matching with the " + expectedValue,
				actualValue.equalsIgnoreCase(expectedValue));
	}
}
