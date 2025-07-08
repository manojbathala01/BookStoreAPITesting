package tests;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import services.BookService;
import utils.ConfigReader;

public class BooksTest {

    public static String isbnNumber;
    public static String bookTitle;
    public final BookService bookService = new BookService();
    public Response response;

    @Test(priority = 1, dependsOnMethods = {
            "tests.UserTests.testCreateUser",
            "tests.UserTests.testGenerateToken" })
    public void createBookInCollection() {
        logStep("Creating book in user collection");

        response = bookService.addBookToUserCollection(
                UserTests.userId,
                ConfigReader.get("firstIsbn"),
                UserTests.token
        );

        Assert.assertEquals(response.getStatusCode(), 201);
        logStep("Response: " + response.getBody().asString());

        isbnNumber = response.jsonPath().getString("books[0].isbn");
        Assert.assertNotNull(isbnNumber);
        logStep("Book created with ISBN: " + isbnNumber);
    }

    @Test(priority = 2, dependsOnMethods = "createBookInCollection")
    public void testGetAllBooks() {
        logStep("Getting all books from bookstore");

        response = bookService.getAllBooks();
        Assert.assertEquals(response.getStatusCode(), 200);

        logStep("Response: " + response.getBody().asString());

        bookTitle = response.jsonPath().getString("books[0].title");
        Assert.assertNotNull(bookTitle);
        logStep("Book found with title: " + bookTitle);
    }

    @Test(priority = 3, dependsOnMethods = "createBookInCollection")
    public void updateBookInCollection() {
        logStep("Trying to update book in collection - should fail");

        response = bookService.updateBookInCollection(
                UserTests.userId,
                isbnNumber,
                UserTests.token,
                ConfigReader.get("firstIsbn")
        );

        Assert.assertEquals(response.getStatusCode(), 400);
        logStep("Response: " + response.getBody().asString());
    }

    @Test(priority = 4, dependsOnMethods = "createBookInCollection")
    public void deleteBookFromCollection() {
        logStep("Deleting book from collection");

        Assert.assertNotNull(UserTests.token, "Token is null!");
        Assert.assertNotNull(UserTests.userId, "User ID is null!");

        response = bookService.deleteBookFromUserCollection(
                UserTests.userId,
                isbnNumber,
                UserTests.token
        );

        Assert.assertEquals(response.getStatusCode(), 204);
        logStep("Response: " + response.getBody().asString());

        String respBody = response.getBody().asString();
        Assert.assertTrue(respBody == null || respBody.trim().isEmpty(), "Expected empty response body");
        logStep("Book deleted successfully (empty response body)");
    }

    @Test(priority = 5, dependsOnMethods = "deleteBookFromCollection")
    public void checkIfBookDeleted() {
        logStep("Checking if book is actually deleted");

        Assert.assertNotNull(UserTests.token, "Token is null!");
        Assert.assertNotNull(UserTests.userId, "User ID is null!");
        Assert.assertNotNull(isbnNumber, "ISBN is null!");

        response = bookService.getUserCollection(UserTests.userId, UserTests.token);
        Assert.assertEquals(response.getStatusCode(), 200);

        String responseBody = response.getBody().asString();
        logStep("User collection after deletion: " + responseBody);

        Assert.assertFalse(responseBody.contains(isbnNumber),
                "Deleted book still appears in the user's collection.");
        logStep("Book deletion confirmed – ISBN not found in user collection.");
    }

    @Step("{0}")
    public void logStep(String message) {
        // Step logging for Allure reports
    }
}
