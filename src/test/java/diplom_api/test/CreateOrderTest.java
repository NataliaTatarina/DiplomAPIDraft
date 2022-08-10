package diplom_api.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static diplom_api.proc.CreateOrderProc.*;
import static diplom_api.proc.GetIngredientsProc.getIngredients;
import static diplom_api.proc.UserProc.*;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;

public class CreateOrderTest extends AbstractTest{

    private String token;

    @Before
    public void createUserBeforeCreateOrderTest() {
        // Создать пользователя
        // Получить accessToken
     token =  createUserResponse(requestSpec, userRegister).getAccessToken().replace("Bearer ", "");
     //  token = loginUserResponse(requestSpec, userLogin).getAccessToken().replace("Bearer ", "");
       System.out.println(token);
        // Получить список ингредиентов
        ingredients = getIngredients(requestSpec);
    }

    @After
    public void deleteUserAfterCreateOrderTest() {
        // Удалить пользователя
        deleteUserCheckStatus(requestSpec, userRegister, token);
    }

    // Создать заказ без авторизации и без ингредиентов
    @Test
    public void createOrderWithoutAuthWithoutIngredientsStatusTest ()
    {
        createOrderWithoutAuthStatus(requestSpec, "", SC_BAD_REQUEST);
    }

    @Test
    public void createOrderWithoutAuthWithoutIngredientsResponseTest ()
    {
        createOrderWithoutAuthRespone(requestSpec, "", false, "Ingredient ids must be provided");
    }

    // Создать заказ без авторизации и c некорректным хэшем одного ингредиента
    @Test
    public void createOrderWithoutAuthWithWrongHashForSingleIngredientStatusTest ()
    {
        String json = "{\"ingredients\":[\"" + "123456789012345678901234"+ "\""+"]}";
        System.out.println(json);
        createOrderWithoutAuthStatus(requestSpec, json, SC_BAD_REQUEST);
    }
    @Test
    public void createOrderWithoutAuthWithWrongHashForSingleIngredientResponseTest ()
    {
        String json = "{\"ingredients\":[\"123456789012345678901234\"]}";
        System.out.println(json);
        createOrderWithoutAuthRespone(requestSpec, json, false, "One or more ids provided are incorrect");
    }

    // Создать заказ без авторизации и c некорректным хэшем одного из двух ингредиентов
    @Test
    public void createOrderWithoutAuthWithWrongHashForOneOfTwoIngredientsStatusTest ()
    {
               String json = "{\"ingredients\":[\"" + "123456789012345678901234"+ "\", " +
                "\"" + ingredients.getData().get(1).get_id()+ "\"" +
                "]}";
        System.out.println(json);
        createOrderWithoutAuthStatus(requestSpec, json, SC_OK);
    }

    @Test
    public void createOrderWithoutAuthWithWrongHashForOneOfTwoIngredientsResponseTest ()
    {
        String json = "{\"ingredients\":[\"" + "123456789012345678901234"+ "\", " +
                "\"" + ingredients.getData().get(1).get_id()+ "\"" +
                "]}";
        System.out.println(json);
        createOrderWithoutAuthRespone(requestSpec, json, true, null);
    }

    // Создать заказ без авторизации и c корректным хэшем двух ингредиентов
    @Test
    public void createOrderWithoutAuthWithTwoCorrectIngredientsStatusTest ()
    {
                String json = "{\"ingredients\":[\"" + ingredients.getData().get(2).get_id()+ "\", " +
                "\"" + ingredients.getData().get(1).get_id()+ "\"" +
                "]}";
        System.out.println(json);
        createOrderWithoutAuthStatus(requestSpec, json, SC_OK);
    }

    @Test
    public void createOrderWithoutAuthWithTwoCorrectIngredientsResponseTest ()
    {
        String json = "{\"ingredients\":[\"" + ingredients.getData().get(2).get_id()+ "\", " +
                "\"" + ingredients.getData().get(1).get_id()+ "\"" +
                "]}";
        System.out.println(json);
        createOrderWithoutAuthRespone(requestSpec, json, true, null);
    }

    // Создать заказ с авторизацией и без ингредиентов
    @Test
    public void createOrderWithAuthWithoutIngredientsStatusTest ()
    {
        createOrderWithAuthStatus(requestSpec, token, "", SC_BAD_REQUEST);
    }

    @Test
    public void createOrderWithAuthWithoutIngredientsResponseTest ()
    {
        createOrderWithAuthRespone( requestSpec, token, "",
                false, "Ingredient ids must be provided");
    }

    // Создать заказ c авторизацией и c некорректным хэшем одного ингредиента
    @Test
    public void createOrderWithAuthWithWrongHashForSingleIngredientStatusTest ()
    {
        String json = "{\"ingredients\":[\"" + "123456789012345678901234"+ "\""+"]}";
        System.out.println(json);
        createOrderWithAuthStatus(requestSpec, token, json, SC_BAD_REQUEST);
    }
    @Test
    public void createOrderWithAuthWithWrongHashForSingleIngredientResponseTest ()
    {
        String json = "{\"ingredients\":[\"" + "123456789012345678901234"+ "\""+"]}";
        System.out.println(json);
        createOrderWithAuthRespone(requestSpec, token, json, false,
                "One or more ids provided are incorrect");
    }

    // Создать заказ с авторизацией и c некорректным хэшем одного из двух ингредиентов
    @Test
    public void createOrderWithAuthWithWrongHashForOneOfTwoIngredientsStatusTest ()
    {
        String json = "{\"ingredients\":[\"" + "123456789012345678901234"+ "\", " +
                "\"" + ingredients.getData().get(1).get_id()+ "\"" +
                "]}";
        System.out.println(json);
        createOrderWithAuthStatus(requestSpec, token, json, SC_OK);
    }

    @Test
    public void createOrderWithAuthWithWrongHashForOneOfTwoIngredientsResponseTest ()
    {
        String json = "{\"ingredients\":[\"" + "123456789012345678901234"+ "\", " +
                "\"" + ingredients.getData().get(1).get_id()+ "\"" +
                "]}";
        System.out.println(json);
        createOrderWithAuthRespone(requestSpec, token, json, true,
                null);
    }

    // Создать заказ с авторизацией и c корректными хэшами двух ингредиентов
    @Test
    public void createOrderWithAuthWithTwoCorrectIngredientsStatusTest ()
    {
        String json = "{\"ingredients\":[\"" + ingredients.getData().get(2).get_id()+ "\", " +
                "\"" + ingredients.getData().get(1).get_id()+ "\"" +
                "]}";
        System.out.println(json);
        createOrderWithAuthStatus(requestSpec, token, json, SC_OK);
    }

    @Test
    public void createOrderWithAuthWithTwoCorrectIngredientsResponseTest ()
    {
        String json = "{\"ingredients\":[\"" + ingredients.getData().get(2).get_id()+ "\", " +
                "\"" + ingredients.getData().get(1).get_id()+ "\"" +
                "]}";
        System.out.println(json);
        createOrderWithAuthRespone(requestSpec, token, json, true,
                null);
    }
}
