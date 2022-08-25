package diplom_api.test;

import diplom_api.pojo.OrderResponse;
import diplom_api.pojo.OrdersList;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import static diplom_api.proc.CreateOrderProc.createOrderCheck;
import static diplom_api.proc.CreateOrderProc.createOrderCheckResponse;
import static diplom_api.proc.GetIngredientsProc.getIngredients;
import static diplom_api.proc.GetOrdersListProc.getOrderList;
import static diplom_api.proc.GetOrdersListProc.getOrderListStatus;
import static diplom_api.proc.UserProc.createUserCheckResponse;
import static diplom_api.proc.UserProc.deleteUserCheckStatus;
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
        OrderResponse firstOrder = createOrderCheckResponse(requestSpec, token, json1);
        String json2 = "{\"ingredients\":[\"" + ingredients.getData().get(0).get_id() + "\", " +
                "\"" + ingredients.getData().get(size - 1).get_id() + "\"" +
                "]}";
        OrderResponse secondOrder = createOrderCheckResponse(requestSpec, token, json2);
    }

    @After
    public void deleteUserAfterOrderListTest() {
        // Удалить пользователя
        deleteUserCheckStatus(requestSpec, userRegister, token);
    }

    // Получить список заказов для авторизированного пользователя - проверить статус
    @Test
    public void getOrdersListForUserWithAuthStatusTest() {
        getOrderListStatus (requestSpec, token, SC_OK, null, true);
      }

    @Test
    public void getOrdersListForUserWithAuthResponseTest() {
        OrdersList ordersList = getOrderList(requestSpec, token);
        // Убедиться. что вернулся ожидаемый JSON
        MatcherAssert.assertThat(ordersList,
                notNullValue());

    }

    // Получить список без авторизации
    @Test
    public void getOrdersListForUserWithoutAuthTest() {
        getOrderListStatus (requestSpec, "", SC_UNAUTHORIZED, "You should be authorised", false);
    }
}
