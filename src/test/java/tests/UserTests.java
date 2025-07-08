package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import services.UserService;
import utils.ConfigReader;

@Epic("BookStore API Tests")
@Feature("User Management")
public class UserTests extends UserService{

    public final UserService userService = new UserService();
    public static String username = "user" + (int) (Math.random() * 10000);
    public final String password = ConfigReader.get("password");

    public static String userId;
    public static String token;

    @Test(priority = 1)
    public void testCreateUser() {
        logStep("Start test: Create User");

        Response response = userService.createUser(username, password);
        Assert.assertEquals(response.getStatusCode(), 201);
        userId = response.jsonPath().getString("userID");
        Assert.assertNotNull(userId);

        logStep("User created with ID: " + userId);
        logStep("Response: " + response.getBody().asString());
    }

    @Test(priority = 2, dependsOnMethods = "testCreateUser")
    public void testGenerateToken() {
        logStep("Start test: Generate Token");

        Response response = userService.generateToken(username, password);
        Assert.assertEquals(response.getStatusCode(), 200);
        token = response.jsonPath().getString("token");
        Assert.assertNotNull(token);

        logStep("Token generated: " + token);
        logStep("Response: " + response.getBody().asString());
    }

    @Test(priority = 3, dependsOnMethods = "testGenerateToken")
    public void testUserIsCreated() {
        logStep("Start test: Validate User Exists");

        Response response = userService.getUser(userId, token);
        Assert.assertEquals(response.getStatusCode(), 200);

        logStep("Validated user presence with ID: " + userId);
        logStep("Response: " + response.getBody().asString());
    }

    @Step("{0}")
    public void logStep(String message) {
        // Step logging for Allure reports
    }
}
