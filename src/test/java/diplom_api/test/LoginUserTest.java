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
        token = createUserResponse(requestSpec, userRegister).getAccessToken().replace("Bearer ", "");
    }

    @After
    public void deleteUserAfterLoginUserTest() {
        // Удалить пользователя
        deleteUserCheckStatus(requestSpec, userRegister, token);
    }

    // Логин под существующим паролем
    @Test
    public void loginUserSuccessTest() {
        given()
                .spec(requestSpec)
                .and()
                .body(userLogin)
                .when()
                .post("auth/login")
                .then()
                .statusCode(SC_OK);
    }

    @Test
    public void loginUserSuccessResponseTest() {
        UserRegisterResponse userLoginResponse =
                loginUserResponse(requestSpec, userLogin);
        // Убедиться. что вернулся ожидаемый JSON
        MatcherAssert.assertThat(userLoginResponse,
                notNullValue());
    }

    // Логин с неверным логином и паролем
    // Пользователь существует, email некорректный
    @Test
    public void loginUserWithWrongEmailFailsTest()
    {
        UserLogin userLoginWithWrongEmail =
                new UserLogin(testEmail+testEmail, testPassword);
        loginUserWithOneWrongFieldCheckStatus (requestSpec, userLoginWithWrongEmail);
    }

    // Пользователь существует, пароль некорректный
    @Test
    public void loginUserWithWrongPasswordFailsTest()
    {
        UserLogin userLoginWithWrongPassword =
                new UserLogin(testEmail, testPassword+testPassword);
        loginUserWithOneWrongFieldCheckStatus (requestSpec, userLoginWithWrongPassword);
    }

    @Test
    public void loginUserWithWrongPasswordFailsResponseTest()
    {
        UserLogin userLoginWithWrongPassword =  new UserLogin(testEmail, testPassword+testPassword);
        loginUserWithOneWrongFieldCheckResponse(requestSpec, userLoginWithWrongPassword);
    }

    @Test
    public void loginUserWithWrongEmailFailsResponseTest()
    {
        UserLogin userLoginWithWrongEmail =  new UserLogin(testEmail+testEmail, testPassword);
        loginUserWithOneWrongFieldCheckResponse(requestSpec, userLoginWithWrongEmail);
    }

}