package stepDefinition;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;
import common.Utilities;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.it.Data;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import common.Log;

public class Steps {

	private static Response response;
	public static List<String> nowPlayingList = new ArrayList<String>();
	public static List<String> domain = new ArrayList<String>();
	public static List<String> label = new ArrayList<String>();
	public static JSONObject microService;

	//This method is used to log the scenario name and its result after the scenario is executed
	@After
	public void after(Scenario scenario) {
		Log.endTestCase(scenario.getName(), scenario.getStatus());
	}
	
	
	//This method is used to call different microservice defined under config.json
	@Given("^I am using \"([^\"]*)\"$")
	public void i_am_using_media_service(Object service) throws Throwable {
	//microservice below is JSONObject declared above
	//Under Utilities i have declared getService method that take name of service as  a parameter.
	// The "Utilities.getService(service.toString());" returns  {"getEndPoint": "/api/ottplatform/media"} which is then typecasted to JSONObject.
	microService = (JSONObject) Utilities.getService(service.toString());
	Log.info("The name of the service is " + service.toString());
	}

	//This method is used to send any kind of Request which has no request parameters.
	@When("^I send \"([^\"]*)\" Request$")
	public void i_send_Request(String requestMethod) throws Throwable {
		//Below I am declaring URL as a String variable.Under utilities I have declared getValue that takes string as a parameter.Here I want the value of baseURL.
		String URL = Utilities.getValue("BaseURL");
		//The below line specifies baseURL to the restfulwebservice.
		RestAssured.baseURI = URL;
		//THe below line is to get request specification of request being made
		RequestSpecification request = RestAssured.given();
		//THe below line is to log the requestMethod to logfile
		Log.info("The request method is " + requestMethod);
		//The main aim of this entire function is to make a request for ex:GET request in this case.To make a request we need an endpoint that has baseURL and path.
		//baseURL is already specified above and thus path is obtained from config .json as mentioned below
		//At begining I conside the path is empty
		String path = "";
		//Here I am using Switch statement based on request method.I want same method to be used to make GET,POST,UPDATE and DELETE too.
		switch (requestMethod) {
		case "GET":
			//The microservice JSONObject value is obtained in the step 1.For ex it would have value {"getEndPoint": "/api/ottplatform/media"} ,From this microservice value we need to get the value for getEndPoint.
			//This value is then typecaste to string
			path = (String) microService.get("getEndPoint");
//			response = request.get(path);
			break;
		case "POST":
			path = (String) microService.get("postEndPoint");
//			response = request.post(path);
			break;
		}
		Log.info("The EndPoint is " + URL + path);
		//In the below line I am making GET request by specifying the path.This statement below should be moved under case "GET" block.This response object will have all the properties associated with reponse like status code,response body
		response = request.get(path);
	}

	@Then("^status code is (\\d+)$")
	public void status_code_is(int expectedStatusCode) throws Throwable {
		//The getStatusCode() method mentioned below is the inbuilt method to get the status code.It is stored under actualStatus code.
		int actualStatusCode = response.getStatusCode();
		Log.info("The status code is " + actualStatusCode);
		//Note that the assertTrue accepts two parameters one message and second the condition.
		//assertTrue is expecting the condition to be true and incase the condition fails then the failure message is displayed.
		//Under assertion condition I am checking if actual statuscode is matching the expected status code mentioned in the feature file step
		assertTrue("The status code " + expectedStatusCode + " is not matching " + actualStatusCode,
				actualStatusCode == expectedStatusCode);
	}

	@Then("^response time is below (\\d+) milliseconds$")
	public void response_time_is_below_milliseconds(int expectedResponseTime) throws Throwable {
		//The getTime() method is the inbuilt method that returns the time it took to return the response.
		int actualResponseTime = (int) response.getTime();
		Log.info("The response time is " + actualResponseTime);
		//Under assertion condition I am checking if the actual response time is less than or equal to expected response time
		assertTrue("The response time " + actualResponseTime + " is more than expected response time " + expectedResponseTime + " milliseconds ",
				actualResponseTime <= expectedResponseTime);
	}

	//This method is used to check if the field by key id is neither empty nor null.
	//Here in we use JsonPath to parse through response and get access to the json objects.We can also use serialisation and desrialisation but I am using Jsonpath
	@Then("^the \"([^\"]*)\" field is neither empty nor null for all the items$")
	public void the_id_field_is_neither_empty_nor_null_for_all_the_items(String id) throws Throwable {
		JsonPath jsonPath = response.jsonPath();
		//jsonpath has entire response
		//jsonPath.getList("data." + id) gives the list of id's(response has data array and and by saying "data.id" we can get access to all the id's in the response)
		//forEach is used to iterate over this collection that is list 
		//ex list below looks like this ["p002d79p","p002d7bc","p002d7jv" and all remanining id's ]
		jsonPath.getList("data." + id).forEach(dataId -> {
			//forEach of this dataId i convert it string as below and check if dataID is not empty and is not null
			if (!(dataId.toString().isEmpty()) || dataId != null) {
				//Under assertion condition below I then check if dataId when converted to string has alphnumeric values
				assertTrue("The " + id + " value is expected to be alphnumeric instead was " + dataId,
						dataId.toString().matches("^[a-zA-Z0-9]*$"));
			}
		});
	}

	//This method is to check that primary field in title_list is neither empty nor null
	@Then("^the \"([^\"]*)\" field in \"([^\"]*)\" is neither empty nor null for all the items$")
	public void the_field_in_is_neither_empty_nor_null_for_all_the_items(String field1, String field2)
			throws Throwable {
		JsonPath jsonPath = response.jsonPath();
		//jsonpath has entire response
		//jsonPath.getList("data." + field2 + "." + field1) gives the list of values for primary field under title_list (response has data array and and by saying "data."data.title_list.primary" we can get access to all the values of primary in the response)
		//forEach is used to iterate over this collection that is list 
		//ex list below looks like this ["John Mellencamp","CeCe Peniston", and all remanining primary values ]
		jsonPath.getList("data." + field2 + "." + field1).forEach(value -> {
			//forEach of the value in the list under assert condition I am checking if the value is not empty and is not null
			assertTrue("The " + field1 + " value " + value + " is not as expected ",
					!(value.toString().isEmpty()) || (value != null));

		});
	}

	//This method is to check if segment type for all track is music
	@When("^the \"([^\"]*)\" for all track is \"([^\"]*)\"$")
	public void all_track_is(String key, String expectedValue) throws Throwable {
		JsonPath jsonPath = response.jsonPath();
		//jsonpath has entire response
		//jsonPath.getList("data." + key) gives the list of segment_type (response has data array and and by saying "data.segment_type" we can get access to all the segemnet_types's in the response)
		//forEach is used to iterate over this collection that is list 
		//ex list below looks like this ["music","music","music" and all remanining segment_types ]
		jsonPath.getList("data." + key).forEach(actualValue -> {
			//When we iterate through list using forEach then Under assertion condition I am checking if the expected value that is "music" mentioned in feature file
			//is same as the actual value available in the list
			assertTrue("The " + actualValue + "for" + key + " is not matching with the " + expectedValue,
					actualValue.toString().equalsIgnoreCase(expectedValue));
		}

		);

	}

	@Then("^only one track has now_playing offset as true$")
	public void only_one_track_has_now_playing_offset_as_true() throws Throwable {
		JsonPath jsonPath = response.jsonPath();
		//jsonpath has entire response
		//jsonPath.getList("offset.now_playing") gives the list of now_playing (response has data array and and by saying "data.offset.now_playing" we can get access to all the now_playing values in the response)
		//forEach is used to iterate over this collection that is list 
		//ex list below looks like this [false,false,true,false and all remanining now_playing values ]
		jsonPath.getList("data.offset.now_playing").forEach(value -> {
			//in below line I am iterating through the above list and each value I am converting to String.
			String nowPlayingValue = value.toString();
			//in the below line I am checking if nowPplaying value is true and if it is true then add the value true to the list by name "nowPlayingList" declared at the top
			if (nowPlayingValue.equalsIgnoreCase("true")) {
				nowPlayingList.add(value.toString());
			}
		});
		//User assertion condition I am checking that size of the "nowPlayingList" is only one.Because the scenario is to check that no_playing offest value is set as true only for one music track
		assertTrue("The now_playing offset value is true for " + nowPlayingList.size() + " tracks",
				nowPlayingList.size() == 1);
	}

	//This method is to check if the response header has "Date" value as "Fri, 21 May"
	@When("^the response header has \"([^\"]*)\" value as \"([^\"]*)\"$")
	public void the_response_header_has_value_as(String key, String expectedValue) throws Throwable {
		//in the below line we have inbuilt method getHeader.There are several headers but I am interested in getting the value of header that is "Date" 
		String actualValue = response.getHeader("Date");
		System.out.println(actualValue);
		assertTrue("The " + actualValue + "for" + key + " is not matching with the " + expectedValue,
				actualValue.contains(expectedValue));
	}
	
	@Then("^the response is received with all tracks having \"([^\"]*)\" as \"([^\"]*)\"$")
	public void the_response_is_received_with_all_tracks_having_as(String Key, String Value) throws Throwable {
		
	JsonPath jsonPath = response.jsonPath();
		//trackType = jsonPath.getList("data." + arg1);
		
		
		jsonPath.getList("data." + Key).forEach(value ->{
			if (Key.equalsIgnoreCase("type")|| (Key.equalsIgnoreCase("segment_type"))) {
				//System.out.println();
				assertTrue("The value "+ Value + " is not matching the for tracktype " + Key ,((String) value).equalsIgnoreCase(Value));
			}
			
		});
	}
		@Then("^the response is received with schema \"([^\"]*)\"$")
		public void the_response_is_received_with_schema(String arg1) throws Throwable {
		    // Write code here that turns the phrase above into concrete actions
			response.then().assertThat()
		      .body(JsonSchemaValidator.
		      matchesJsonSchema(new File("./src/test/resources/TestData/MediaServiceSchema.json")));
			
	}
		@Then("^the domain name is \"([^\"]*)\" for track with label \"([^\"]*)\"$")
		public void the_domain_name_is_spotify_for_track_with_label_Spotify(String domainValue , String labelValue) throws Throwable {
		    // Write code here that turns the phrase above into concrete actions
		   JsonPath jsonPath =response.jsonPath();
		   label = jsonPath.getList("data.uris.label");
		   domain= jsonPath.getList("data.uris.uri");
		   System.out.println("label"+label);
		   System.out.println("domain"+domain);
		   for(int i=0;i<label.size();i++) {
			   for (int j=0;j<domain.size();j++)
			   {
				   System.out.println(label.get(i));
				   System.out.println(domain.get(j));
			   }
		   }
//		   jsonPath.getList("data.uris.label").forEach(value -> {
//			   if(((String) value).equalsIgnoreCase("spotify")) {
//				   System.out.println(jsonPath.get("data.uris[0].uri").toString());
////				   assertTrue("the domain value " +domain+ "for label "+label+ "is not matching", jsonPath.get("data.uris.uri").toString());
//			   }
//		   });
		   
		}

		


	
}
