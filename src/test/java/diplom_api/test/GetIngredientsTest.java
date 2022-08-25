package diplom_api.test;

import org.junit.Test;

import static diplom_api.proc.GetIngredientsProc.getIngredients;

public class GetIngredientsTest extends AbstractTest {

    @Test
    public void getIngredientsListTest() {
        ingredients = getIngredients(requestSpec);
    }
}
