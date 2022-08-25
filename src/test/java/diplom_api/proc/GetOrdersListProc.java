package diplom_api.proc;

import diplom_api.pojo.OrdersList;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class GetOrdersListProc {
    public static OrdersList getOrderList (RequestSpecification requestSpec, String token)
    {
       return given()
                .spec(requestSpec)
                .and()
                .auth().oauth2(token)
                .when()
                .get("orders")
                .body()
                .as(OrdersList.class);
    }
}
