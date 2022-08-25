package diplom_api.proc;

import diplom_api.pojo.OrderResponse;
import diplom_api.pojo.UserRegisterResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderProc {
    // Запрос на создание заказа пользователем - проверяется ответ:
    // значение поля success и текст сообщения
    public static void
    createOrderCheck(RequestSpecification requestSpec, String token, String json,
                                     int status, boolean success, String message) {
        given()
                .spec(requestSpec)
                .and()
                .body(json)
                .auth().oauth2(token)
                .when()
                .post("orders")
                .then()
                .statusCode(status)
                .body("message",
                        equalTo(message))
                .and()
                .body("success",
                        equalTo(success));
    }

    public static OrderResponse
    createOrderCheckResponse(RequestSpecification requestSpec, String token, String json) {
        return given()
                .spec(requestSpec)
                .and()
                .body(json)
                .auth().oauth2(token)
                .when()
                .post("orders")
                .body()
                .as(OrderResponse.class);
    }
}