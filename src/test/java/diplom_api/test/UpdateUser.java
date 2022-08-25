package diplom_api.test;

import diplom_api.pojo.UserLogin;
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
        token = createUserCheckResponse(requestSpec, userRegister).getAccessToken().replace("Bearer ", "");
    }

    @After
    public void deleteUserAfterUpdateUserTest() {
        // Удалить пользователя
        deleteUserCheckStatus(requestSpec, userRegister, token);
    }

    // Изменение данных пользователя без авторизации
    @Test
    public void updateUserNameWithoutAuthorizationFallsTest() {
        String json = "{\"name\": \"" + "Update" + testName + "\"}";
        updateUserCheckStatusAndResponse(requestSpec, json, "", SC_UNAUTHORIZED);
    }

    @Test
    public void updateUserEmailWithoutAuthorizationFallsTest() {
        String json = "{\"email\": \"" + "Update" + testEmail + "\"}";
        updateUserCheckStatusAndResponse(requestSpec, json,"",SC_UNAUTHORIZED);
    }

    @Test
    public void updateUserPasswordWithoutAuthorizationFallsTest() {
        String json = "{\"password\": \"" + "Update" + testPassword + "\"}";
        updateUserCheckStatusAndResponse(requestSpec, json, "", SC_UNAUTHORIZED);
    }

    // Изменение данных пользователя с авторизацией - проверка возвращаемого ответа
    // Корректно изменить имя
    @Test
    public void updateCorrectUserNameResponseTest() {
        String json = "{\"name\": \"" + "Update" + testName + "\"}";
        UserRegisterResponse userUpdateName =
                updateUserWithAuthCheckResponse(requestSpec, json, token);
        MatcherAssert.assertThat(userUpdateName,
                notNullValue());
        // Авторизироваться, из ответа получть name, сравнить с обновленным занчением
        UserRegisterResponse loginUserRespone = loginUserCheckResponse(requestSpec, userLogin);
        MatcherAssert.assertThat(loginUserRespone.getUser().getName(), equalTo("Update" + testName));
    }

    // Корректно изменить пароль
    @Test
    public void updateCorrectUserPasswordResponseTest() {
        String json = "{\"password\": \"" + "Update" + testPassword + "\"}";
        UserRegisterResponse userUpdatePassword =
                updateUserWithAuthCheckResponse(requestSpec, json, token);
        MatcherAssert.assertThat(userUpdatePassword,
                notNullValue());
        // Авторизироваться под новым паролем
        UserLogin newUserLogin = new UserLogin(testEmail, "Update" + testPassword );
        loginUserCheckStatus(requestSpec, newUserLogin);
    }

    // Корректно изменить email
    @Test
    public void updateCorrectUserEmailResponseTest() {
        String json = "{\"email\": \"" + "Update" + testEmail + "\"}";
        UserRegisterResponse userUpdateEmail =
                updateUserWithAuthCheckResponse(requestSpec, json, token);
        MatcherAssert.assertThat(userUpdateEmail,
                notNullValue());
        // Авторизироваться под новым email
        UserLogin newUserLogin = new UserLogin("Update" +testEmail,  testPassword );
        loginUserCheckStatus(requestSpec, newUserLogin);
    }

    // Некорректно изменить email авторизированного пользователя
    // Попытаться установить ему email, который уже иcпользуется -
    // первым пользователем, созданным в Before

    // Проверить статус
    @Test
    public void updateUserDuplicateEmailFallsStatusTest() {
        String json = "{\"email\": \"" + testEmail + "\"}";
        // Создать второго пользователя
        UserRegister secondUser = new UserRegister("Update" + testEmail, testPassword, testName);
        String secondToken = createUserCheckResponse(requestSpec, secondUser).
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
                .statusCode(SC_FORBIDDEN)
                .body("message",
                        equalTo("User with such email already exists"))
                .and()
                .body("success",
                        equalTo(false));
        // Удалить второго пользователя
        deleteUserCheckStatus(requestSpec, userRegister, secondToken);
    }
}
