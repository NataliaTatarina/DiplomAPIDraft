package diplom_api.proc;

import io.restassured.specification.RequestSpecification;
import diplom_api.pojo.UserLogin;
import diplom_api.pojo.UserRegister;
import diplom_api.pojo.UserRegisterResponse;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class UserProc {

    // Запрос на создание пользователя без необходимых полей
    public static void createUserWithoutNecessaryFieldCheck(RequestSpecification requestSpec, UserRegister userRegister) {
        given()
                .spec(requestSpec)
                .and()
                .body(userRegister)
                .when()
                .post("auth/register")
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("message",
                        equalTo("Email, password and name are required fields"))
                .and()
                .body("success",
                        equalTo(false));
    }


    // Запрос на создание пользователя
    public static UserRegisterResponse createUserCheckResponse(RequestSpecification requestSpec, UserRegister userRegister) {
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

    // Запрос на удаление пользователя
    // Проверяется, что возвращаемый статус - SC_ACCEPTED
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

    // Заррос на авторизацию пользователя
    public static UserRegisterResponse loginUserCheckResponse(RequestSpecification requestSpec, UserLogin userLogin) {
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

    // Заррос на авторизацию пользователя - проверка статуса
    public static void loginUserCheckStatus(RequestSpecification requestSpec, UserLogin userLogin) {
                    given()
                        .spec(requestSpec)
                        .and()
                        .body(userLogin)
                        .when()
                        .post("auth/login")
                        .then()
                        .statusCode(SC_OK);
    }
    // Запрос на авторизацию пользователя без одного из необходимых полей
    // Проверяется, что возвращаемый статус - SC_UNAUTHORIZED
    public static void loginUserWithOneWrongFieldCheck(RequestSpecification requestSpec, UserLogin userLogin) {
        given()
                .spec(requestSpec)
                .and()
                .body(userLogin)
                .when()
                .post("auth/login")
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("message",
                        equalTo("email or password are incorrect"))
                .and()
                .body("success",
                        equalTo(false));
    }

    // Запрос на измение полей учетной записи пользователя
    // Проверяется, что возвращаемый статус соответствует ожидаемому
    public static void updateUserCheckStatusAndResponse(RequestSpecification requestSpec, String json, String token, int status) {
        given()
                .spec(requestSpec)
                .and()
                .body(json)
                .auth().oauth2(token)
                .when()
                .patch("auth/user")
                .then()
                .statusCode(status)
                .body("message",
                        equalTo("You should be authorised"))
                .and()
                .body("success",
                        equalTo(false));
    }


    // Запрос на измение полей учетной записи авторизированного пользователя
    public static UserRegisterResponse updateUserWithAuthCheckResponse(RequestSpecification requestSpec, String json, String token) {
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