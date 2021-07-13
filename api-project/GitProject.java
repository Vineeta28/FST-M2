package project;

import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static io.restassured.RestAssured.given;

import org.testng.Reporter;
import org.testng.annotations.BeforeClass;

public class GitProject {
	RequestSpecification reqSpec;
	ResponseSpecification resSpec;
	String ssh = "";
	public int id = 0;
	
	@BeforeClass
	public void beforeClass() {
		
		reqSpec= new RequestSpecBuilder()
				.setBaseUri("https://api.github.com")
				.setContentType(ContentType.JSON)
				.build();
		
		resSpec= new ResponseSpecBuilder()
				.expectContentType(ContentType.JSON)
				. build();
	}
	
	@Test(priority=1)
	public void postAPI() {
		System.out.println("POST API");
		Response response = given()
				.spec(reqSpec)
				.header("accept", "application/vnd.github.v3+json" )
				.when()
				.auth()
				.preemptive()
				.basic("token","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")
				.body("\n" + 
						"{\n" + 
						"    \"title\": \"TestAPIKey\",\n" + 
						"    \"key\": \"ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDD1j8jYM86m9Qtmp1+WTWYH+3bQssgE39ve1uLIotgBb9ECf8+W/Dv6bGz47hmXGvoR5QVjhTG9iIEn4FA+seWCxbU/IH8ekdv65ssvqCGtKvbDGHQx4eZmRDlsvt+z23ewtw70cO5l23jBhYzlV1GnkSlJhfc/3KOhpiQu5bNAhA7CuUMnHwi31aWqPmAVusLbkmk7115DXH3kC2lRS/iXRyy4ZNv1YMtWSqcs6Cd0fxOla5CoL3X2uFV6zF/aPV99LCDuXQjQ8KGDsOHPaiQijemvHCMMIwJhopPZ8kfXtzmK8DtJA+hld9/7dgR4wLPDhN6WtA2taKACIdV4SFD\"\n" + 
						"}\n" + 
						"\n" + 
						"")
				.log().uri()
				.post("/user/keys");
				
		response.then().spec(resSpec);
		response.then().log().body();
		response.then().statusCode(201);
		id= response.jsonPath().getInt("id");
		System.out.println("Id of the inserted ssh public token is : "+id);
	}
	
	@Test(priority=2)
	public void getAPI() {
		System.out.println("GET API");
		Response response = given()
				.spec(reqSpec)
				.auth()
				.preemptive()
				.basic("token","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")
				.log().uri()
				.get("/user/keys");
		
		response.then().log().body();
		response.then().statusCode(200);
		
				
	}
	
	@Test(priority=3)
	public void deleteAPI() {
		System.out.println("DELETE API");
		Response response = given().spec(reqSpec)
				.auth()
				.preemptive()
				.basic("token","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")
				.pathParam("keyId", id)
				.when()
				.log().uri()
				.delete("/user/keys/{keyId}");
		
		response.then().log().body();
		response.then().statusCode(204);
	}
	

}