package com.yosuva.examples;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;


public class APITestsJSON {
    String userName = "";
    String password = "Testuser@123";
    String userId = "";

    @Test(priority = 0)
    public void post_test_create_user() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("ddMMyyyyhhmmss");
        //String date= simpleDateFormat.format(new SimpleDateFormat("dd/MM/yyyy").parse("08/09/2022"));
        String date= simpleDateFormat.format(new Date());
        userName="testuser"+date;
        RestAssured.baseURI="https://demoqa.com/Account/v1/User";
        Response response = given()
                //.queryParam("name", "value")
                //.queryParam("name", "value")
                .filter(new CustomLogFilter())
                //.headers("Authorization", "Basic dGVzdHVzZXIxMjM0NTpUZXN0dXNlckAxMjM=")
                .contentType("application/json")
                .body("{\n" +
                        "  \"userName\": \""+userName+"\",\n" +
                        "  \"password\": \""+password+"\"\n" +
                        "}")
                .when()
                .post()
                .then()
                .extract().response();
        System.out.println(response.body().asString());
        Assert.assertEquals(response.statusCode(), 201);
        Assert.assertEquals(response.body().path("username"), userName);
        userId=response.body().path("userID");
    }

    @Test(priority =1)
    public void put_test() {
        RestAssured.baseURI="https://demoqa.com/BookStore/v1/Books/9781449325862";
        Response response = given()
                .auth().preemptive().basic(userName,password)
                //.queryParam("name", "value")
                //.queryParam("name", "value")
                .filter(new CustomLogFilter())
                .contentType("application/json")
                .body("{\n" +
                        "  \"userId\": \""+userId+"\",\n" +
                        "  \"isbn\": \"9781449325862\"\n" +
                        "}")
                .when()
                .put()
                .then()
                .extract().response();
        System.out.println(response.body().asString());
        Assert.assertEquals(response.statusCode(), 400);

    }

    @Test(priority =2)
    public void delete_test_delete_user() {
        RestAssured.baseURI="https://demoqa.com/Account/v1/User/"+userId;
        Response response = given()
                .auth().preemptive().basic(userName,password)
                //.queryParam("name", "value")
                //.queryParam("name", "value")
                .filter(new CustomLogFilter())
                .contentType("application/json")
                .when()
                .delete()
                .then()
                .extract().response();
        System.out.println(response.body().asString());
        Assert.assertEquals(response.statusCode(), 204);

    }

    @Test(priority =3)
    public void get_test_user_details() {
        RestAssured.baseURI="https://demoqa.com/Account/v1/User/"+userId;
        Response response = given()
                .auth().preemptive().basic(userName,password)
                //.queryParam("name", "value")
                //.queryParam("name", "value")
                .filter(new CustomLogFilter())
                .contentType("application/json")
                .when()
                .get()
                .then()
                .extract().response();
        System.out.println(response.body().asString());
        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.body().path("message"), "User not found!");
        List<String> list=new ArrayList<>();
        list.add("a");list.add("b");list.add("c");list.add("d");
        for (int i = 0; i < list.size(); i++) {
        System.out.println(list.get(i));
        }
    }
}
