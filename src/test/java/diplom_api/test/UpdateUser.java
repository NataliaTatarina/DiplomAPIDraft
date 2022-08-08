package diplom_api.test;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import diplom_api.pojo.UserRegister;
import diplom_api.pojo.UserRegisterResponse;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import static diplom_api.proc.UserProc.*;

public class UpdateUser extends AbstractTest {
    private String token;

    @Before
    public void createUserBeforeUpdateUserTest() {
        // Создать пользователя
        // Получить accessToken
        token = createUserResponse(requestSpec, userRegister).getAccessToken().replace("Bearer ", "");
    }

    @After
    public void deleteUserAfterUpdateUserTest() {
        // Удалить пользователя
        deleteUserCheckStatus(requestSpec, userRegister, token);
    }

    // Изменение данных пользователя без авторизации
    @Test
    public void updateUserNameWithoutAuthorizationTest() {
        String json = "{\"name\": \"" + "Update" + testName + "\"}";
        updateUserWithoutAuthCheckStatus(requestSpec, json);
    }

    @Test
    public void updateUserEmailWithoutAuthorizationTest() {
        String json = "{\"email\": \"" + "Update" + testEmail + "\"}";
        updateUserWithoutAuthCheckStatus(requestSpec, json);
    }

    @Test
    public void updateUserPasswordWithoutAuthorizationTest() {
        String json = "{\"password\": \"" + "Update" + testPassword + "\"}";
        updateUserWithoutAuthCheckStatus(requestSpec, json);
    }

    // Изменить имя - resp
    @Test
    public void updateUserNameWithoutAuthorizationResponseTest() {
        String json = "{\"name\": \"" + "Update" + testName + "\"}";
        updateUserWithoutAuthCheckResponse(requestSpec, json);
    }

    // Изменить пароль - resp
    @Test
    public void updateUserPasswordWithoutAuthorizationResponseTest() {
        String json = "{\"password\": \"" + "Update" + testPassword + "\"}";
        updateUserWithoutAuthCheckResponse(requestSpec, json);
    }

    // Изменить email - resp
    @Test
    public void updateUserEmailWithoutAuthorizationResponseTest() {
        String json = "{\"email\": \"" + "Update" + testEmail + "\"}";
        updateUserWithoutAuthCheckResponse(requestSpec, json);
    }

    // Изменение данных пользователя с авторизацией
    // Корректно изменить имя - проверить статус
    @Test
    public void updateUserNameCorrectTest() {
        String json = "{\"name\": \"" + "Update" + testName + "\"}";
        updateUserWithAuthCheckStatus(requestSpec, json, token);
    }

    @Test
    public void updateUserEmailCorrectTest() {
        String json = "{\"email\": \"" + "Update" + testEmail + "\"}";
        updateUserWithAuthCheckStatus(requestSpec, json, token);
    }

    @Test
    public void updateUserPasswordCorrectTest() {
        String json = "{\"password\": \"" + "Update" + testPassword + "\"}";
        updateUserWithAuthCheckStatus(requestSpec, json, token);
    }

    // Корректно изменить имя - проверить response
    @Test
    public void updateUserNameCorrectResponseTest() {
        String json = "{\"name\": \"" + "Update" + testName + "\"}";
        UserRegisterResponse userUpdateName =
                updateUserdWithAuthResponse(requestSpec, json, token);
        MatcherAssert.assertThat(userUpdateName,
                notNullValue());
    }

    // Корректно изменить пароль - проверить response
    @Test
    public void updateUserPasswordCorrectResponseTest() {
        String json = "{\"password\": \"" + "Update" + testPassword + "\"}";
        UserRegisterResponse userUpdatePassword =
                updateUserdWithAuthResponse(requestSpec, json, token);
        MatcherAssert.assertThat(userUpdatePassword,
                notNullValue());
    }

    // Корректно изменить email - проверить response
    @Test
    public void updateUserEmailCorrectResponseTest() {
        String json = "{\"email\": \"" + "Update" + testEmail + "\"}";
        UserRegisterResponse userUpdateEmail =
                updateUserdWithAuthResponse(requestSpec, json, token);
        MatcherAssert.assertThat(userUpdateEmail,
                notNullValue());
    }

    // Некорректно изменить email - проверить статус
    @Test
    public void updateUserEmailFallsStatusTest() {
        String json = "{\"email\": \"" + testEmail + "\"}";
        // Создать второго пользователя
        UserRegister secondUser = new UserRegister("Update" + testEmail, testPassword, testName);
        String secondToken = createUserResponse(requestSpec, secondUser).
                getAccessToken().replace("Bearer ", "");
        // Попытаться обновить email второму пользователю
        given()
                .spec(requestSpec)
                .and()
                .body(json)
                .auth().oauth2(secondToken)
                .when()
                .patch("auth/user")
                .then()
                .statusCode(SC_FORBIDDEN);
        // Удалить второго пользователя
        deleteUserCheckStatus(requestSpec, userRegister, secondToken);
    }

    // Некорректно изменить email - проверить response
    @Test
    public void updateUserEmailFallsResponseTest() {
        String json = "{\"email\": \"" + testEmail + "\"}";
        // Создать второго пользователя
        UserRegister secondUser = new UserRegister("Update" + testEmail, testPassword, testName);
        String secondToken = createUserResponse(requestSpec, secondUser).
                getAccessToken().replace("Bearer ", "");
        // Попытаться обновить email второму пользователю
        given()
                .spec(requestSpec)
                .and()
                .body(json)
                .auth().oauth2(secondToken)
                .when()
                .patch("auth/user")
                .then()
                .body("message",
                        equalTo("User with such email already exists"))
                .and()
                .body("success",
                        equalTo(false));
        // Удалить второго пользователя
        deleteUserCheckStatus(requestSpec, userRegister, secondToken);
    }
}
