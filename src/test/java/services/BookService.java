package services;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.Endpoints;

import static io.restassured.RestAssured.given;

public class BookService {

    public Response addBookToUserCollection(String userId, String isbn, String token) {
        String requestBody = String.format("""
            {
              "userId": "%s",
              "collectionOfIsbns": [{ "isbn": "%s" }]
            }
        """, userId, isbn);

        return given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(Endpoints.ADD_BOOKS);
    }

    public Response getAllBooks() {
        return given()
                .when()
                .get(Endpoints.GET_BOOKS);
    }

    public Response updateBookInCollection(String userId, String isbn, String token, String originalIsbn) {
        String body = String.format("""
            {
              "userId": "%s",
              "isbn": "%s"
            }
        """, userId, isbn);

        return given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .pathParam("isbn", originalIsbn)
                .body(body)
                .when()
                .put(Endpoints.UPDATE_BOOKS + "{isbn}");
    }

    public Response deleteBookFromUserCollection(String userId, String isbn, String token) {
        String body = String.format("""
            {
              "userId": "%s",
              "isbn": "%s"
            }
        """, userId, isbn);

        return given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(body)
                .when()
                .delete(Endpoints.DELETE_BOOK);
    }

    public Response getUserCollection(String userId, String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(Endpoints.GET_USER + userId);
    }
}
