package tests;

import base.BaseTest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ConfigReader;

import static io.restassured.RestAssured.given;
import static utils.Endpoints.*;

public class BookNegativeTests extends BaseTest {

    private Response response;

    @Test
    public void createBookInCollectionWithoutAuth() {
        logStep("Start test: Create Book without authorization");

        String requestBody = String.format("""
         {
           "userId": "%s",
           "collectionOfIsbns": [{ "isbn": "%s" }]
         }
        """, UserTests.userId, ConfigReader.get("firstIsbn"));

        response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(ADD_BOOKS)
                .then()
                .statusCode(401)
                .extract().response();

        String message = response.jsonPath().getString("message");
        Assert.assertTrue(message.contains("User not authorized!"));

        logStep("Unauthorized book creation was correctly rejected. Response: " + response.getBody().asString());
    }

    @Test(dependsOnMethods = { "tests.UserTests.testCreateUser" })
    public void updateBookWithoutIsbn() {
        logStep("Start test: Update book without providing ISBN");

        String body = String.format("""
         {
           "userId": "%s",
           "isbn": "%s"
         }
        """, UserTests.userId, "");

        response = given()
                .header("Authorization", "Bearer " + UserTests.token)
                .contentType("application/json")
                .pathParam("isbn", ConfigReader.get("firstIsbn"))
                .body(body)
                .when()
                .put(UPDATE_BOOKS + "{isbn}")
                .then()
                .statusCode(400)
                .extract().response();

        String message = response.jsonPath().getString("message");
        Assert.assertTrue(message.contains("Request Body is Invalid!"));

        logStep("Book update without ISBN correctly rejected. Response: " + response.getBody().asString());
    }

    @Test
    public void deleteBookWithMissingUserId() {
        logStep("Start test: Delete book without userId");

        String body = String.format("""
         {
           "userId": "%s",
           "isbn": "%s"
         }
        """, "", ConfigReader.get("firstIsbn"));

        response = given()
                .header("Authorization", "Bearer " + UserTests.token)
                .contentType("application/json")
                .body(body)
                .when()
                .delete(DELETE_BOOK)
                .then()
                .statusCode(401)
                .extract().response();

        String message = response.jsonPath().getString("message");
        Assert.assertTrue(message.contains("User Id not correct!"));

        logStep("Book deletion with missing userId correctly rejected. Response: " + response.getBody().asString());
    }

    @Step("{0}")
    public void logStep(String message) {
        // Allure step logging
    }
}
