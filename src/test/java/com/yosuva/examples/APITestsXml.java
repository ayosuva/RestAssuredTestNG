package com.yosuva.examples;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;


public class APITestsXml {
    SOAPXmlResponseParser soapResponseParser;
    public APITestsXml()
    {
         soapResponseParser = new SOAPXmlResponseParser();
    }
    @Test(priority = 0)
    public void post_test_create_user() {

        RestAssured.baseURI="https://www.w3schools.com/xml/tempconvert.asmx";
        Response response = given()
                //.queryParam("name", "value")
                //.queryParam("name", "value")
                .filter(new CustomLogFilter())
                .headers("SOAPAction", "https://www.w3schools.com/xml/FahrenheitToCelsius")
                .contentType("text/xml")
                .body("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                        "  <soap:Body>\n" +
                        "   <FahrenheitToCelsius xmlns=\"https://www.w3schools.com/xml/\">\n" +
                        "     <Fahrenheit>100</Fahrenheit>\n" +
                        "   </FahrenheitToCelsius>\n" +
                        "  </soap:Body>\n" +
                        "</soap:Envelope>")
                .when()
                .post()
                .then()
                .extract().response();
        System.out.println(response.body().asString());
        Assert.assertEquals(response.statusCode(), 200);
        System.out.println(soapResponseParser.getResponseValue(response,"FahrenheitToCelsiusResult"));
        Assert.assertEquals(soapResponseParser.getResponseValue(response,"FahrenheitToCelsiusResult"), "37.7777777777778");
        //response.path("Response.@type"); this is for normal xml response
    }


}
