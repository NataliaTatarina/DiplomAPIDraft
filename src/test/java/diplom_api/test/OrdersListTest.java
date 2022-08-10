package diplom_api.test;

import diplom_api.pojo.Ingredients;
import diplom_api.pojo.OrdersList;
import diplom_api.pojo.UserRegisterResponse;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static diplom_api.proc.CreateOrderProc.createOrderWithAuthRespone;
import static diplom_api.proc.GetIngredientsProc.getIngredients;
import static diplom_api.proc.UserProc.createUserResponse;
import static diplom_api.proc.UserProc.deleteUserCheckStatus;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrdersListTest extends AbstractTest {
    private String token;
    private OrdersList ordersList;
    private String jsonFirstOrder;
    private String jsonSecondOrder;

    @Before
    public void createUserCreateOrders() {
        // Создать пользователя
        // Получить accessToken
        token = createUserResponse(requestSpec, userRegister).getAccessToken().replace("Bearer ", "");
        //  token = loginUserResponse(requestSpec, userLogin).getAccessToken().replace("Bearer ", "");
        System.out.println(token);
        // Получить список ингредиентов
        ingredients = getIngredients(requestSpec);
        // Создать два заказа
        String json1 = "{\"ingredients\":[\"" + ingredients.getData().get(1).get_id() + "\", " +
                "\"" + ingredients.getData().get(2).get_id() + "\"" +
                "]}";
        System.out.println(json1);
        createOrderWithAuthRespone(requestSpec, token, json1, true,
                null);
        String json2 = "{\"ingredients\":[\"" + ingredients.getData().get(3).get_id() + "\", " +
                "\"" + ingredients.getData().get(4).get_id() + "\"" +
                "]}";
        System.out.println(json2);
        createOrderWithAuthRespone(requestSpec, token, json2, true,
                null);
    }

    @After
    public void deleteUser() {
        // Удалить пользователя
        deleteUserCheckStatus(requestSpec, userRegister, token);
    }

    // Получить список с авторизацией
    @Test
    public void getOrdersListUserWithAuthTest() {
        OrdersList ordersList = given()
                .spec(requestSpec)
                .and()
                .auth().oauth2(token)
                .when()
                .get("orders")
                .body()
                .as(OrdersList.class);
        // Убедиться. что вернулся ожидаемый JSON
        MatcherAssert.assertThat(ordersList,
                notNullValue());

    }

    // Получить список без авторизации
    @Test
    public void getOrdersListUserWithoutAuthStatusTest() {
        given()
                .spec(requestSpec)
                .when()
                .get("orders")
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    public void getOrdersListUserWithoutAuthResponseTest() {
        // Получить список без авторизации
        given()
                .spec(requestSpec)
                .when()
                .get("orders")
                .then()
                .body("message",
                        Matchers.equalTo("You should be authorised"))
                .and()
                .body("success",
                        Matchers.equalTo(false));
    }
}
