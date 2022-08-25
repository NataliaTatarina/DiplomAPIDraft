package diplom_api.test;

import org.hamcrest.MatcherAssert;
import org.junit.Test;
import diplom_api.pojo.UserRegister;
import diplom_api.pojo.UserRegisterResponse;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static diplom_api.proc.UserProc.*;

public class CreateUserTest extends AbstractTest {

    // Создание уникального пользователя
    // Проверить возвращаемый статус
    @Test
    public void createCorrectUserStatusTest() {
        // Создать пользователя и проверить возвращаемый статус
        given()
                .spec(requestSpec)
                .and()
                .body(userRegister)
                .when()
                .post("auth/register")
                .then()
                .statusCode(SC_OK);
        // Убедиться, что пользователь может авторизироваться и получить accessToken
        UserRegisterResponse userLoginResponse =
                loginUserCheckResponse(requestSpec, userLogin);
        // Удалить пользователя
        deleteUserCheckStatus(requestSpec, userRegister,
                userLoginResponse.getAccessToken().replace("Bearer ", ""));
    }

    // Проверить возвращаемый response
    @Test
    public void createCorrectUserResponseTest() {
        // Создать пользователя
        UserRegisterResponse UserRegisterResponse =
                createUserCheckResponse(requestSpec, userRegister);
        // Убедиться. что вернулся ожидаемый JSON
        MatcherAssert.assertThat(UserRegisterResponse,
                notNullValue());
        // Убедиться, что пользователь может авторизироваться
        UserRegisterResponse userLoginResponse =
                loginUserCheckResponse(requestSpec, userLogin);
        // Удалить пользователя
        deleteUserCheckStatus(requestSpec, userRegister,
                userLoginResponse.getAccessToken().replace("Bearer ", ""));
    }

    // Нельзя создать 2 одинаковых пользователей
    // Проверить возвращаемый статус
    @Test
    public void createTwoEqualUsersFallsTest() {
        // Создать первого  пользователя
        UserRegisterResponse userRegisterResponse =
                createUserCheckResponse(requestSpec, userRegister);
        // Попытаться создать второго пользователя с теми же параметрами
        // и убедиться, что вернулся верный статус
        given()
                .spec(requestSpec)
                .and()
                .body(userRegister)
                .when()
                .post("auth/register")
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("message",
                        equalTo("User already exists"))
                .and()
                .body("success",
                        equalTo(false));
        // Удалить первого пользователя
        deleteUserCheckStatus(requestSpec, userRegister,
                userRegisterResponse.getAccessToken().replace("Bearer ", ""));
    }

    // Нельзя создать пользователя, если не указано одно из обязательных полей
    // Попытка создать пользователя без указания email
    @Test
    public void createUserWithoutEmailFallsStatusTest() {
        UserRegister userWithoutEmail = new UserRegister(null, testPassword, testName);
        createUserWithoutNecessaryFieldCheck(requestSpec, userWithoutEmail);
    }

    // Попытка создать пользователя без указания пароля
    @Test
    public void createUserWithoutPasswordFallsStatusTest() {
        UserRegister userWithoutPassword = new UserRegister(testEmail, null, testName);
        createUserWithoutNecessaryFieldCheck(requestSpec, userWithoutPassword);
    }

    // Попытка создать пользователя без указания имени
    @Test
    public void createUserWithoutNameFallsStatusTest() {
        UserRegister userWithoutName = new UserRegister(testEmail, testPassword, null);
        createUserWithoutNecessaryFieldCheck(requestSpec, userWithoutName);
    }


}
