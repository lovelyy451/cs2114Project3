
package purrfectfit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Random;

/**
 * Tests the ClothingCatalog class to verify catalog
 * construction, random outfit generation, clothing
 * options, and invalid input handling.
 *
 * @author Lovesh Kataria
 * @version 11.11.2026
 */
public class ClothingCatalogTest
{
    private ClothingCatalog catalog;

    /**
     * Initializes a clothing catalog with a seeded
     * random generator for reproducible testing.
     */
    @BeforeEach
    public void setUp()
    {
        catalog = new ClothingCatalog(new Random(42));
    }


    /**
     * Tests that the default constructor creates
     * a catalog capable of generating an outfit.
     */
    @Test
    public void testDefaultConstructor()
    {

        ClothingCatalog defaultCatalog = new ClothingCatalog();
        Outfit outfit = defaultCatalog.generateOutfit();

        assertEquals(ClothingCategory.HAT, outfit.getHat().getCategory());
        assertEquals(ClothingCategory.TOP, outfit.getTop().getCategory());
        assertEquals(
            ClothingCategory.SHOES,
            outfit.getShoes().getCategory());
    }


    /**
     * Tests that the constructor rejects a null
     * random generator.
     */
    @Test
    public void testNullRandom()
    {
        try
        {
            new ClothingCatalog(null);
            assertEquals("Exception expected", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Random generator cannot be null.",
                e.getMessage());
        }
    }


    /**
     * Tests that generateOutfit creates an outfit
     * containing items from the correct categories.
     */
    @Test
    public void testGenerateOutfit()
    {
        

        Outfit outfit = catalog.generateOutfit();

        assertEquals(ClothingCategory.HAT, outfit.getHat().getCategory());
        assertEquals(ClothingCategory.TOP, outfit.getTop().getCategory());
        assertEquals(
            ClothingCategory.SHOES,
            outfit.getShoes().getCategory());
    }


    /**
     * Tests that generateOptions rejects a null outfit.
     */
    @Test
    public void testGenerateOptionsNull()
    {
        

        try
        {
            catalog.generateOptions(null);
            assertEquals("Exception expected", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Correct outfit cannot be null.",
                e.getMessage());
        }
    }


    /**
     * Tests that generateOptions rejects an outfit
     * containing clothing items outside the catalog.
     */
    @Test
    public void testGenerateOptionsForeignItem()
    {
        

        Outfit valid = catalog.generateOutfit();

        ClothingItem foreignHat =
            new ClothingItem("Foreign Hat", ClothingCategory.HAT);

        Outfit invalid = new Outfit(
            foreignHat,
            valid.getTop(),
            valid.getShoes());

        try
        {
            catalog.generateOptions(invalid);
            assertEquals("Exception expected", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Correct outfit contains an item outside the catalog.",
                e.getMessage());
        }
    }


    /**
     * Tests that generateOptions creates three distinct
     * options per category, including the correct item.
     */
    @Test
    public void testGenerateOptions()
    {
        

        Outfit correct = catalog.generateOutfit();
        ClothingItem[][] options = catalog.generateOptions(correct);

        ClothingItem[] correctItems = {
            correct.getHat(),
            correct.getTop(),
            correct.getShoes()
        };

        ClothingCategory[] categories = {
            ClothingCategory.HAT,
            ClothingCategory.TOP,
            ClothingCategory.SHOES
        };

        assertEquals(3, options.length);

        for (int category = 0; category < 3; category++)
        {
            assertEquals(3, options[category].length);

            int correctCount = 0;

            for (int i = 0; i < 3; i++)
            {
                assertEquals(
                    categories[category],
                    options[category][i].getCategory());

                if (options[category][i] == correctItems[category])
                {
                    correctCount++;
                }

                for (int j = i + 1; j < 3; j++)
                {
                    assertEquals(
                        false,
                        options[category][i] == options[category][j]);
                }
            }

            assertEquals(1, correctCount);
        }
    }
}
