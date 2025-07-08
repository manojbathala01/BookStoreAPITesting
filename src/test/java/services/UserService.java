package services;

import base.BaseTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.ConfigReader;
import utils.Endpoints;

import static io.restassured.RestAssured.given;

public class UserService extends BaseTest {

    public Response createUser(String username, String password) {
        String requestBody = String.format(
        """
            {
              "userName": "%s",
              "password": "%s"
            }
        """, username, password);

        return given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(Endpoints.CREATE_USER);
    }

    public Response generateToken(String username, String password) {
        String requestBody = String.format(
        """
            {
              "userName": "%s",
              "password": "%s"
            }
        """, username, password);

        return given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(Endpoints.GENERATE_TOKEN);
    }

    public Response getUser(String userId, String token) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .pathParam("userId", userId)
                .when()
                .get(Endpoints.GET_USER + "{userId}");
    }
}
