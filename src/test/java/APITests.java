import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;


public class APITests {
    //testuser12345/Testuser@123

    @Test
    public void test() {
        RestAssured.baseURI="https://demoqa.com/Account/v1/User";
        Response response = null;
        response = given()
                //.queryParam("name", "value")
                //.queryParam("name", "value")
                .headers("Authorization", "Basic dGVzdHVzZXIxMjM0NTpUZXN0dXNlckAxMjM=")
                .contentType("application/json")
                .body("{\n" +
                        "  \"userName\": \"testuser12345\",\n" +
                        "  \"password\": \"Testuser@123\"\n" +
                        "}")
                .when()
                .post()
                .then()
                .extract().response();
        System.out.println(response.body().asString());
        Assert.assertEquals(response.statusCode(), 406);
        Assert.assertEquals(response.body().path("message"), "User exists!");


    }
}
