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

    // Можно создать уникального пользователя
    @Test
    public void createUserSuccessTest() {
        // Создать пользователя и проверить возвращаемый статус
        given()
                .spec(requestSpec)
                .and()
                .body(userRegister)
                .when()
                .post("auth/register")
                .then()
                .statusCode(SC_OK);
        // Получить accessToken
        UserRegisterResponse userRegisterResponse =
                loginUserResponse(requestSpec, userLogin);
        // Удалить пользователя
        deleteUserCheckStatus(requestSpec, userRegister,
                userRegisterResponse.getAccessToken().replace("Bearer ", ""));
    }

    @Test
    public void createUserSuccessResponseTest() {
        // Создать пользователя
        UserRegisterResponse userRegisterResponse =
                createUserResponse(requestSpec, userRegister);
        // Убедиться. что вернулся ожидаемый JSON
        MatcherAssert.assertThat(userRegisterResponse,
                notNullValue());
        // Получить accessToken
        // Удалить пользователя
        deleteUserCheckStatus(requestSpec, userRegister, userRegisterResponse.getAccessToken().replace("Bearer ", ""));
    }

    // Нельзя создать 2 одинаковых пользователей
    @Test
    public void createTwoEqualUsersFailsTest() {
        // Создать первого  пользователя
        UserRegisterResponse userRegisterResponse =
                createUserResponse(requestSpec, userRegister);
        // Попытаться создать второго пользователя с теми же параметрами
        // и убедиться, что вернулся верный статус
        given()
                .spec(requestSpec)
                .and()
                .body(userRegister)
                .when()
                .post("auth/register")
                .then()
                .statusCode(SC_FORBIDDEN);
        // Удалить первого пользователя
        deleteUserCheckStatus(requestSpec, userRegister,
                userRegisterResponse.getAccessToken().replace("Bearer ", ""));
    }
    @Test
    public void createTwoEqualUsersFailsResponseTest() {
        // Создать первого  пользователя
        UserRegisterResponse userRegisterResponse =
                createUserResponse(requestSpec, userRegister);
        // Попытаться создать второго пользователя с теми же параметрами
        // и убедиться, что вернулся false и ожидаемое сообщение
        given()
                .spec(requestSpec)
                .and()
                .body(userRegister)
                .when()
                .post("auth/register")
                .then()
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
    @Test
    public void createUserWithoutEmailFailsTest() {
        UserRegister userWithoutEmail = new UserRegister(null, testPassword, testName);
        createUserWithountNessaryFieldCheckStatus(requestSpec, userWithoutEmail);
    }

    @Test
    public void createUserWithoutPasswordFailsTest() {
        UserRegister userWithoutPassword = new UserRegister(testEmail, null, testName);
        createUserWithountNessaryFieldCheckStatus(requestSpec, userWithoutPassword);
    }

    @Test
    public void createUserWithoutNameFailsTest() {
        UserRegister userWithoutName = new UserRegister(testEmail, testPassword, null);
        createUserWithountNessaryFieldCheckStatus(requestSpec, userWithoutName);
    }

    @Test
    public void createUserWithoutEmailFailsResponseTest() {
        UserRegister userWithoutEmail = new UserRegister(null, testPassword, testName);
        createUserWithountNessaryFieldCheckResponse(requestSpec, userWithoutEmail);
    }

    @Test
    public void createUserWithoutPasswordFailsResponseTest() {
        UserRegister userWithoutPassword = new UserRegister(testEmail, null, testName);
        createUserWithountNessaryFieldCheckResponse(requestSpec, userWithoutPassword);
    }

    @Test
    public void createUserWithoutNameFailsResponseTest() {
        UserRegister userWithoutName = new UserRegister(testEmail, testPassword, null);
        createUserWithountNessaryFieldCheckResponse(requestSpec, userWithoutName);
    }

}
