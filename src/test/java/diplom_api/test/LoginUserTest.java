package diplom_api.test;

import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import diplom_api.pojo.UserLogin;
import diplom_api.pojo.UserRegisterResponse;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.notNullValue;
import static diplom_api.proc.UserProc.*;

public class LoginUserTest extends AbstractTest {
    private String token;

    @Before
    public void createUserBeforeLoginUserTest() {
        // Создать пользователя
        // Получить accessToken
        token = createUserCheckResponse(requestSpec, userRegister).getAccessToken().replace("Bearer ", "");
    }

    @After
    public void deleteUserAfterLoginUserTest() {
        // Удалить пользователя
        deleteUserCheckStatus(requestSpec, userRegister, token);
    }

    // Успешная авторизация под существующим паролем
    // Проверить статус ответа
    @Test
    public void loginCorrectUserStatusTest() {
        given()
                .spec(requestSpec)
                .and()
                .body(userLogin)
                .when()
                .post("auth/login")
                .then()
                .statusCode(SC_OK);
    }

    // Проверить возвращаемый response
    @Test
    public void loginCorrectUserResponseTest() {
        UserRegisterResponse userLoginResponse =
                loginUserCheckResponse(requestSpec, userLogin);
        // Убедиться. что вернулся ожидаемый JSON
        MatcherAssert.assertThat(userLoginResponse,
                notNullValue());
        // Убедиться, что можно разлогиниться - получть refreshToken из ответа
        // Сделать logout
        String json = "{\"token\": \"" + userLoginResponse.getRefreshToken() + "\"}";
        given()
                .spec(requestSpec)
                .and()
                .body(json)
                .when()
                .post("auth/logout")
                .then()
                .statusCode(SC_OK);
    }

    // Авторизация с неверным логином и паролем
    // Пользователь существует, email некорректный
    @Test
    public void loginUserWithWrongEmailFallsStatusTest() {
        UserLogin userLoginWithWrongEmail =
                new UserLogin(testEmail + testEmail, testPassword);
        loginUserWithOneWrongFieldCheck(requestSpec, userLoginWithWrongEmail);
    }

    // Пользователь существует, пароль некорректный
    @Test
    public void loginUserWithWrongPasswordFallsResponseTest() {
        UserLogin userLoginWithWrongPassword = new UserLogin(testEmail, testPassword + testPassword);
        loginUserWithOneWrongFieldCheck(requestSpec, userLoginWithWrongPassword);
    }

}