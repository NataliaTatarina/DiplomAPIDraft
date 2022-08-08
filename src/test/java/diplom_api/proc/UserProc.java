package diplom_api.proc;

import io.restassured.specification.RequestSpecification;
import diplom_api.pojo.UserLogin;
import diplom_api.pojo.UserRegister;
import diplom_api.pojo.UserRegisterResponse;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class UserProc {
    public static void createUserWithountNessaryFieldCheckStatus(RequestSpecification requestSpec, UserRegister userRegister) {
        given()
                .spec(requestSpec)
                .and()
                .body(userRegister)
                .when()
                .post("auth/register")
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    public static void createUserWithountNessaryFieldCheckResponse(RequestSpecification requestSpec, UserRegister userRegister) {
        given()
                .spec(requestSpec)
                .and()
                .body(userRegister)
                .when()
                .post("auth/register")
                .then()
                .body("message",
                        equalTo("Email, password and name are required fields"))
                .and()
                .body("success",
                        equalTo(false));
    }

    public static UserRegisterResponse createUserResponse(RequestSpecification requestSpec, UserRegister userRegister) {
        return
                given()
                        .spec(requestSpec)
                        .and()
                        .body(userRegister)
                        .when()
                        .post("auth/register")
                        .body()
                        .as(UserRegisterResponse.class);
    }

    public static void deleteUserCheckStatus(RequestSpecification requestSpec, UserRegister userRegister, String token) {
        given()
                .spec(requestSpec)
                .and()
                .body(userRegister)
                .auth().oauth2(token)
                .when()
                .delete("auth/user")
                .then()
                .statusCode(SC_ACCEPTED);
    }

    public static UserRegisterResponse loginUserResponse(RequestSpecification requestSpec, UserLogin userLogin) {
        return
                given()
                        .spec(requestSpec)
                        .and()
                        .body(userLogin)
                        .when()
                        .post("auth/login")
                        .body()
                        .as(UserRegisterResponse.class);
    }

    public static void loginUserWithOneWrongFieldCheckStatus(RequestSpecification requestSpec, UserLogin userLogin) {
        given()
                .spec(requestSpec)
                .and()
                .body(userLogin)
                .when()
                .post("auth/login")
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    public static void loginUserWithOneWrongFieldCheckResponse(RequestSpecification requestSpec, UserLogin userLogin) {
        given()
                .spec(requestSpec)
                .and()
                .body(userLogin)
                .when()
                .post("auth/login")
                .then()
                .body("message",
                        equalTo("email or password are incorrect"))
                .and()
                .body("success",
                        equalTo(false));
    }
    public static void updateUserSingleFieldWithAutorisation (RequestSpecification requestSpec,String json, String token)
    {
        given()
                .spec(requestSpec)
                .and()
                .body(json)
                .auth().oauth2(token)
                .when()
                .patch("auth/user")
                .then()
                .statusCode(SC_OK);
    }

    public static void updateUserWithAuthCheckStatus(RequestSpecification requestSpec, String json, String token) {
        given()
                .spec(requestSpec)
                .and()
                .body(json)
                .auth().oauth2(token)
                .when()
                .patch("auth/user")
                .then()
                .statusCode(SC_OK);
    }

    public static void updateUserWithoutAuthCheckStatus(RequestSpecification requestSpec, String json) {
        given()
                .spec(requestSpec)
                .and()
                .body(json)
                .when()
                .patch("auth/user")
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    public static void updateUserWithoutAuthCheckResponse(RequestSpecification requestSpec, String json) {
        given()
                .spec(requestSpec)
                .and()
                .body(json)
                .when()
                .patch("auth/user")
                .then()
                .body("message",
                        equalTo("You should be authorised"))
                .and()
                .body("success",
                        equalTo(false));
    }

    public static UserRegisterResponse updateUserdWithAuthResponse(RequestSpecification requestSpec, String json, String token) {
        return
                given()
                        .spec(requestSpec)
                        .and()
                        .body(json)
                        .auth().oauth2(token)
                        .when()
                        .patch("auth/user")
                        .body()
                        .as(UserRegisterResponse.class);
    }


}