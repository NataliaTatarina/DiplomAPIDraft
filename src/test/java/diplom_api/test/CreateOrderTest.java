package diplom_api.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static diplom_api.proc.GetIngredients.getIngredients;
import static diplom_api.proc.UserProc.createUserResponse;
import static diplom_api.proc.UserProc.deleteUserCheckStatus;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTest extends AbstractTest{

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

    // Создать заказ без авторизации и без ингредиентов
    @Test
    public void createOrderWithoutAuthWithoutIngredientsStatusTest ()
    {
        given()
                .spec(requestSpec)
                .and()
                .body("")
                .when()
                .post("orders")
                .then()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    public void createOrderWithoutAuthWithoutIngredientsResponseTest ()
    {
        given()
                .spec(requestSpec)
                .and()
                .body("")
                .when()
                .post("orders")
                .then()
                .body("message",
                        equalTo("Ingredient ids must be provided"))
                .and()
                .body("success",
                        equalTo(false));
    }

    // Создать заказ без авторизации и c некорректным хэшем одного из двух ингредиентов
    @Test
    public void createOrderWithoutAuthWithWrongHashForOneIngredientResponseTest ()
    {
        ingredients = getIngredients(requestSpec);
        String json = "{\"ingredients\":[\"" + ingredients.getData().get(1).get_id()+"1"+ "\"}]";
        System.out.println(json);
       given()
                    .spec(requestSpec)
                    .and()
                    .body(json)
                    .when()
                    .post("orders")
                    .then()
               .body("message",
                       equalTo("Ingredient ids must be provided"))
               .and()
               .body("success",
                       equalTo(false));
    }

    @Test
    public void createOrderWithoutAuthWithWrongHashForOneIngredientStatusTest ()
    {
        ingredients = getIngredients(requestSpec);
        String json = "{\"ingredients\":[\"" + ingredients.getData().get(1).get_id()+"1"+ "\"}]";
        System.out.println(json);
        given()
                .spec(requestSpec)
                .and()
                .body(json)
                .when()
                .post("orders")
                .then()
                .statusCode(SC_BAD_REQUEST);
    }

    // Создать заказ с авторизацией и без ингредиентов
    @Test
    public void createOrderWithAuthWithoutIngredientsStatusTest ()
    {
        given()
                .spec(requestSpec)
                .and()
                .body("")
                .auth().oauth2(token)
                .when()
                .post("orders")
                .then()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    public void createOrderWithAuthWithoutIngredientsResponseTest ()
    {
        given()
                .spec(requestSpec)
                .and()
                .body("")
                .auth().oauth2(token)
                .when()
                .post("orders")
                .then()
                .body("message",
                        equalTo("Ingredient ids must be provided"))
                .and()
                .body("success",
                        equalTo(false));
    }

    // Создать заказ c авторизацией и c некорректным хэшем одного из двух ингредиентов
    @Test
    public void createOrderWithAuthWithWrongHashForOneIngredientResponseTest ()
    {
       ingredients = getIngredients(requestSpec);
       String json = "{\"ingredients\":[\"" + "123456789012345678901234" + "\"]}";
       System.out.println(json);
        given()
                .spec(requestSpec)
                .and()
                .body(json)
                .auth().oauth2(token)
                .when()
                .post("orders")
                .then()
                .statusCode(SC_INTERNAL_SERVER_ERROR);

            }
}
