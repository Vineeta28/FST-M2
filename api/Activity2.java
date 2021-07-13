package activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;


public class Activity2 {
	   final static String ROOT_URI = "https://petstore.swagger.io/v2/user";

	    @Test(priority=1)
	    public void addNewUserFromFile() throws Exception {
	        // Import JSON file
	     //   FileInputStream inputJSON = new FileInputStream("src/activities/userinfo.json");
	        String file = "src/activities/userinfo.json";
	        String json = new String(Files.readAllBytes(Paths.get(file)));
	        System.out.println(json);
	        // Read JSON file as String
	     //   String reqBody = inputJSON.toString();
	        String reqBody=json;
	        		//readAllBytes());

	        Response response =given().contentType(ContentType.JSON) 
	            .body(reqBody) // Pass request body from file
	            .when().post(ROOT_URI); // Send POST request

	     //   inputJSON.close();
	        

	        // Assertion
	        response.then().body("code", equalTo(200));
	        response.then().body("message", equalTo("9905"));
	       }
	   
	    
	    @Test(priority=2)
	    public void getUserInfo() {
	        // Import JSON file to write to
	        File outputJSON = new File("src/activities/userGETResponse.json");

	        Response response = 
	            given().contentType(ContentType.JSON) // Set headers
	            .pathParam("username", "justint") // Pass request body from file
	            .when().get(ROOT_URI + "/{username}"); // Send POST request
	        
	        // Get response body
	        String resBody = response.getBody().asPrettyString();

	        try {
	            // Create JSON file
	            outputJSON.createNewFile();
	            // Write response body to external file
	            FileWriter writer = new FileWriter(outputJSON.getPath());
	            writer.write(resBody);
	            writer.close();
	        } catch (IOException excp) {
	            excp.printStackTrace();
	        }
	        
	        // Assertion
	    //    response.then().body("id", equalTo(9904));
	        response.then().body("username", equalTo("justint"));
	        response.then().body("firstName", equalTo("Just"));
	        response.then().body("lastName", equalTo("Case1"));
	        response.then().body("email", equalTo("justincase1@mail.com"));
	        response.then().body("password", equalTo("password123456"));
	        response.then().body("phone", equalTo("9812763567"));
	    }
	    
	    @Test(priority=3)
	    public void deleteUser() throws IOException {
	        Response response = 
	            given().contentType(ContentType.JSON) // Set headers
	            .pathParam("username", "justint") // Add path parameter
	            .when().delete(ROOT_URI + "/{username}"); // Send POST request

	        // Assertion
	        response.then().body("code", equalTo(200));
	        System.out.println("Done");
	    }
}
