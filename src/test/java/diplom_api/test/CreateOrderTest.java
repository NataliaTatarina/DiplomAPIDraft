package diplom_api.test;

import diplom_api.pojo.OrderResponse;
import diplom_api.pojo.Orders;
import diplom_api.pojo.OrdersList;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static diplom_api.proc.CreateOrderProc.*;
import static diplom_api.proc.GetIngredientsProc.getIngredients;
import static diplom_api.proc.GetOrdersListProc.getOrderList;
import static diplom_api.proc.UserProc.*;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;

public class CreateOrderTest extends AbstractTest {

    private String token;

    @Before
    public void createUserBeforeCreateOrderTest() {
        // Создать пользователя
        // Получить accessToken
        token = createUserCheckResponse(requestSpec, userRegister).getAccessToken().replace("Bearer ", "");
        // Получить список ингредиентов
        ingredients = getIngredients(requestSpec);
        Assert.assertNotNull("list og ingredients is empty",ingredients);
    }

    @After
    public void deleteUserAfterCreateOrderTest() {
        // Удалить пользователя
        deleteUserCheckStatus(requestSpec, userRegister, token);
    }

    // Создать заказ без авторизации и без ингредиентов
    @Test
    public void createOrderWithoutAuthWithoutIngredientsTest() {
        createOrderCheck(requestSpec, "","", SC_BAD_REQUEST, false, "Ingredient ids must be provided");
    }

    // Создать заказ без авторизации и c некорректным хэшем единственного ингредиента
    @Test
    public void createOrderWithoutAuthWithWrongHashForSingleIngredientResponseTest() {
        String json = "{\"ingredients\":[\"123456789012345678901234\"]}";
        createOrderCheck(requestSpec, "", json, SC_BAD_REQUEST,false, "One or more ids provided are incorrect");
    }

    // Создать заказ без авторизации и c некорректным хэшем одного из двух ингредиентов
    @Test
    public void createOrderWithoutAuthWithWrongHashForOneOfTwoIngredientsResponseTest() {
        String json = "{\"ingredients\":[\"" + "123456789012345678901234" + "\", " +
                "\"" + ingredients.getData().get(1).get_id() + "\"" +
                "]}";
        // Создать заказ
        OrderResponse orderResponse = createOrderCheckResponse(requestSpec, "", json);
        // Получить список заказов пользователя
        OrdersList ordersList =        given()
                .spec(requestSpec)
                .when()
                .get("orders/all")
                .body()
                .as(OrdersList.class);
        // Убедиться, что в списке всех заказов есть созданный пользователем заказ
       int i = 1;
        for ( Orders orders : ordersList.getOrders())
        {System.out.println(i); i=i+1;}
     }

    // Создать заказ без авторизации и c корректным хэшем двух ингредиентов
    @Test
    public void createOrderWithoutAuthWithTwoCorrectIngredientsResponseTest() {
        String json = "{\"ingredients\":[\"" + ingredients.getData().get(2).get_id() + "\", " +
                "\"" + ingredients.getData().get(1).get_id() + "\"" +
                "]}";
        // Создать заказ
        OrderResponse orderResponse = createOrderCheckResponse(requestSpec, "", json);
    }

    // Создать заказ с авторизацией и без ингредиентов
    @Test
    public void createOrderWithAuthWithoutIngredientsResponseTest() {
        createOrderCheck(requestSpec, token, "",
                SC_BAD_REQUEST, false, "Ingredient ids must be provided");
    }

    // Создать заказ c авторизацией и c некорректным хэшем единственного ингредиента
    @Test
    public void createOrderWithAuthWithWrongHashForSingleIngredientResponseTest() {
        String json = "{\"ingredients\":[\"" + "123456789012345678901234" + "\"" + "]}";
        createOrderCheck(requestSpec, token, json, SC_BAD_REQUEST, false,
                "One or more ids provided are incorrect");
    }

    // Создать заказ с авторизацией и c некорректным хэшем одного из двух ингредиентов
    @Test
    public void createOrderWithAuthWithWrongHashForOneOfTwoIngredientsResponseTest() {
        String json = "{\"ingredients\":[\"" + "123456789012345678901234" + "\", " +
                "\"" + ingredients.getData().get(1).get_id() + "\"" +
                "]}";
        // Создать заказ
        OrderResponse orderResponse = createOrderCheckResponse(requestSpec, token, json);
        // Получить список заказов пользователя
        OrdersList ordersList = getOrderList(requestSpec, token);
        // Убедиться, что в списке есть созданный пользователем заказ
        Assert.assertEquals(orderResponse.getOrder().getNumber(), ordersList.getOrders().get(0).getNumber());
     }

    // Создать заказ с авторизацией и c корректными хэшами двух ингредиентов
    @Test
    public void createOrderWithAuthWithTwoCorrectIngredientsResponseTest() {
        String json = "{\"ingredients\":[\"" + ingredients.getData().get(2).get_id() + "\", " +
                "\"" + ingredients.getData().get(1).get_id() + "\"" +
                "]}";
        OrderResponse orderResponse = createOrderCheckResponse(requestSpec, token, json);
        // Получить список заказов пользователя
        OrdersList ordersList = getOrderList(requestSpec, token);
        // Убедиться, что в списке есть созданный пользователем заказ
        Assert.assertEquals(orderResponse.getOrder().getNumber(), ordersList.getOrders().get(0).getNumber());
    }
}
