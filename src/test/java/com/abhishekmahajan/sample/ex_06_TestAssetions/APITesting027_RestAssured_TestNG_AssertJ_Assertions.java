package com.abhishekmahajan.sample.ex_06_TestAssetions;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class APITesting027_RestAssured_TestNG_AssertJ_Assertions {

    RequestSpecification requestSpecification;
    ValidatableResponse validatableResponse;
    Response response;
    Integer bookingId;

    @Test
    public void test_POST() {

        String payload_POST = "{\n" +
                "    \"firstname\" : \"Abhi\",\n" +
                "    \"lastname\" : \"Mahajan\",\n" +
                "    \"totalprice\" : 123,\n" +
                "    \"depositpaid\" : false,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2024-01-01\",\n" +
                "        \"checkout\" : \"2024-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Lunch\"\n" +
                "}";

        requestSpecification = RestAssured.given()
                .baseUri("https://restful-booker.herokuapp.com/")
                .basePath("/booking")
                .contentType(ContentType.JSON)
                .body(payload_POST)
                .log().all();

        // ✅ No shadowing
        response = requestSpecification.when().post();

        validatableResponse = response.then().log().all();
        validatableResponse.statusCode(200);
        validatableResponse.body("bookingid", Matchers.notNullValue());
        validatableResponse.body("booking.firstname", Matchers.equalTo("Abhi"));
        validatableResponse.body("booking.lastname", Matchers.equalTo("Mahajan"));
        validatableResponse.body("booking.depositpaid", Matchers.equalTo(false));

        bookingId = response.then().extract().path("bookingid");
        String firstname = response.then().extract().path("booking.firstname");
        String lastname = response.then().extract().path("booking.lastname");

        JsonPath jp = new JsonPath(response.asString());

        assertThat(jp.getInt("bookingid")).isNotNull();
        assertThat(jp.getString("booking.firstname")).isEqualTo("Abhi");
        assertThat(jp.getString("booking.lastname")).isEqualTo("Mahajan");
        assertThat(jp.getInt("booking.totalprice")).isEqualTo(123);
        assertThat(jp.getBoolean("booking.depositpaid")).isFalse();

        Assert.assertEquals(firstname, "Abhi");
        Assert.assertEquals(lastname, "Mahajan");
        Assert.assertNotNull(bookingId);

        if (!firstname.contains("Abhi")) {
            Assert.fail("Failed kar diya Test");
        }

        assertThat(bookingId).isNotZero().isPositive();
        assertThat(firstname).isNotBlank().isEqualTo("Abhi");
    }
}
