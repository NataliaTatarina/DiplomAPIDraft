package diplom_api.test;

import diplom_api.pojo.OrdersList;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import static diplom_api.proc.CreateOrderProc.createOrderCheck;
import static diplom_api.proc.GetIngredientsProc.getIngredients;
import static diplom_api.proc.GetOrdersListProc.getOrderList;
import static diplom_api.proc.UserProc.createUserCheckResponse;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.notNullValue;

public class OrdersListTest extends AbstractTest {
    private String token;

    @Before
    public void createUserAndCreateOrdersBeforeOrderListTest() {
        // Создать пользователя
        // Получить accessToken
        token = createUserCheckResponse(requestSpec, userRegister).getAccessToken().replace("Bearer ", "");
        // Получить список ингредиентов
        ingredients = getIngredients(requestSpec);
        // Проверить, что список ингредиентов не нулевой, есть хотя бы 1 ингредиент
        Assert.assertNotNull("list og ingredients is empty", ingredients);
        // Поучить длину списка ингредиентов
        int size = ingredients.getData().size();
        // Создать два заказа
        String json1 = "{\"ingredients\":[\"" + ingredients.getData().get(0).get_id() + "\", " +
                "\"" + ingredients.getData().get(size - 1).get_id() + "\"" +
                "]}";
        createOrderCheck(requestSpec, token, json1, SC_OK, true, null);
        String json2 = "{\"ingredients\":[\"" + ingredients.getData().get(0).get_id() + "\", " +
                "\"" + ingredients.getData().get(size - 1).get_id() + "\"" +
                "]}";
        createOrderCheck(requestSpec, token, json2, SC_OK, true, null);
    }

    @After
    public void deleteUserAfterOrderListTest() {
        // Удалить пользователя
        // deleteUserCheckStatus(requestSpec, userRegister, token);
    }

    // Получить список заказов для авторизированного пользователя
    @Test
    public void getOrdersListForUserWithAuthStatusTest() {
        given()
                .spec(requestSpec)
                .and()
                .auth().oauth2(token)
                .when()
                .get("orders")
                .then()
                .statusCode(SC_OK);
    }

    @Test
    public void getOrdersListForUserWithAuthResponseTest() {
        OrdersList ordersList = getOrderList(requestSpec, token);
        // Убедиться. что вернулся ожидаемый JSON
        MatcherAssert.assertThat(ordersList,
                notNullValue());

    }

    // Получить список без авторизации
    // Проверить статус ответа
    @Test
    public void getOrdersListForUserWithoutAuthTest() {
        given()
                .spec(requestSpec)
                .when()
                .get("orders")
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("message",
                        Matchers.equalTo("You should be authorised"))
                .and()
                .body("success",
                        Matchers.equalTo(false));
    }
}
