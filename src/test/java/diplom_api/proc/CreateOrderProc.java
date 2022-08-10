package diplom_api.proc;

import diplom_api.pojo.UserRegister;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderProc {
    public static void createOrderWithoutAuthStatus(RequestSpecification requestSpec, String json, int status) {

        given()
                .spec(requestSpec)
                .and()
                .body(json)
                .when()
                .post("orders")
                .then()
                .statusCode(status);
    }

    public static void createOrderWithAuthStatus(RequestSpecification requestSpec, String token, String json, int status) {

        given()
                .spec(requestSpec)
                .and()
                .body(json)
                .auth().oauth2(token)
                .when()
                .post("orders")
                .then()
                .statusCode(status);
    }


    public static void createOrderWithoutAuthRespone(RequestSpecification requestSpec, String json,
                                                     boolean success, String message) {
        given()
                .spec(requestSpec)
                .and()
                .body(json)
                .when()
                .post("orders")
                .then()
                .body("message",
                        equalTo(message))
                .and()
                .body("success",
                        equalTo(success));
    }
    public static void createOrderWithAuthRespone(RequestSpecification requestSpec, String token, String json,
                                                     boolean success, String message) {
        given()
                .spec(requestSpec)
                .and()
                .body(json)
                .auth().oauth2(token)
                .when()
                .post("orders")
                .then()
                .body("message",
                        equalTo(message))
                .and()
                .body("success",
                        equalTo(success));
    }
}