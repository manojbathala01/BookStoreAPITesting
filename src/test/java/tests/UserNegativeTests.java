package tests;

import base.BaseTest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static utils.Endpoints.*;

public class UserNegativeTests extends BaseTest {

    private Response response;

    @Test
    public void testCreateUserWithWeakPassword() {
        logStep("Start test: Create User with Weak Password");

        String requestBody = """
        {
          "userName": "weakUser123",
          "password": "12345"
        }
        """;

        response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(CREATE_USER)
                .then()
                .statusCode(400)
                .extract().response();

        String message = response.jsonPath().getString("message");
        Assert.assertTrue(message.contains("Passwords must have"), "Expected password policy error");

        logStep("Weak password was correctly rejected. Response: " + response.getBody().asString());
    }

    @Test
    public void testGenerateTokenInvalidCredentials() {
        logStep("Start test: Generate Token with Invalid Credentials");

        String requestBody = """
         {
           "userName": "nonexistentUser",
           "password": "wrongPass1!"
         }
        """;

        response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(GENERATE_TOKEN)
                .then()
                .statusCode(200)
                .extract().response();

        String status = response.jsonPath().getString("status");
        Assert.assertEquals(status, "Failed");

        logStep("Invalid login attempt was correctly rejected. Response: " + response.getBody().asString());
    }

    @Test
    public void testGetUserWithInvalidId() {
        logStep("Start test: Get user with wrong userId");

        response = given()
                .contentType("application/json")
                .pathParam("userId", "invalidUserId")
                .when()
                .get(GET_USER + "{userId}")
                .then()
                .statusCode(401)
                .extract().response();

        String message = response.jsonPath().getString("message");
        Assert.assertTrue(message.contains("User not authorized!"));

        logStep("Invalid userId access correctly rejected. Response: " + response.getBody().asString());
    }

    @Step("{0}")
    public void logStep(String message) {
        // Allure step logging
    }
}
