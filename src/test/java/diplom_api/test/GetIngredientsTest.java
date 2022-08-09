package diplom_api.test;

import diplom_api.pojo.Ingredients;
import diplom_api.pojo.UserRegisterResponse;
import org.junit.Test;

import static diplom_api.proc.GetIngredients.getIngredients;
import static io.restassured.RestAssured.given;

public class GetIngredientsTest extends AbstractTest{

    @Test
    public void getIngredientsListTest()
    {
        ingredients = getIngredients(requestSpec);
    }
}
