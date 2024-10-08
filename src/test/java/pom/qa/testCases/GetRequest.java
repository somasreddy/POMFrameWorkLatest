package pom.qa.testCases;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;

public class GetRequest {
    private static final Logger logger = LogManager.getLogger(GetRequest.class);
    @Test // (enabled = false)
    public void testGet() {

	baseURI = "https://reqres.in/api/";

	// Perform GET request and validate response
	given().get("users?page=2").then().statusCode(200).body("data[0].last_name", equalTo("Lawson")).log()
		.ifValidationFails().body("data.first_name", hasItems("Michael", "Lindsay", "Byron", "Tobias")).log()
		.all();
	logger.info("get Request has been verified");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testPost() {
	Map<String, Object> map = new HashMap<>();
	map.put("name", "somasekhar");
	map.put("job", "Sr.TAE");
	JSONObject json = new JSONObject();
	json.put("name", "somasekhar");
	json.put("job", "Sr.TAE");

	// Convert to JSON string
	String mjson = json.toString();

	baseURI = "https://reqres.in/api/";

	given().header("Content-Type", "application/json").contentType(ContentType.JSON).accept(ContentType.JSON)
		.body(mjson).when().post("users").then().statusCode(201).log().all();
	logger.info("post Request has been verified");
    }

}
