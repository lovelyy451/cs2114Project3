
package purrfectfit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

/**
 * Tests the Customer class to verify construction,
 * clothing option validation, defensive copying,
 * customer attributes, and dialogue assignment.
 *
 * @author Lovesh Kataria
 * @version 11.11.2026
 */
public class CustomerTest
{
    private ClothingItem[] hats;
    private ClothingItem[] tops;
    private ClothingItem[] shoes;
    private ClothingItem[][] options;
    private Outfit outfit;
    private Customer customer;

    /**
     * Initializes valid clothing items, available options,
     * an outfit, and a customer for testing.
     */
    @BeforeEach
    public void setUp()
    {
        hats = new ClothingItem[] {
            new ClothingItem("Cap", ClothingCategory.HAT),
            new ClothingItem("Crown", ClothingCategory.HAT),
            new ClothingItem("Bow", ClothingCategory.HAT)
        };

        tops = new ClothingItem[] {
            new ClothingItem("Shirt", ClothingCategory.TOP),
            new ClothingItem("Suit", ClothingCategory.TOP),
            new ClothingItem("Dress", ClothingCategory.TOP)
        };

        shoes = new ClothingItem[] {
            new ClothingItem("Sneakers", ClothingCategory.SHOES),
            new ClothingItem("Boots", ClothingCategory.SHOES),
            new ClothingItem("Sandals", ClothingCategory.SHOES)
        };

        options = new ClothingItem[][] {
            hats.clone(),
            tops.clone(),
            shoes.clone()
        };

        outfit = new Outfit(hats[0], tops[0], shoes[0]);

        customer = new Customer(Personality.KIND, outfit, options);
    }


    /**
     * Tests that a valid customer returns its assigned
     * personality and correct outfit.
     */
    @Test
    public void testGetters()
    {
        

        assertEquals(Personality.KIND, customer.getPersonality());
        assertEquals(outfit, customer.getCorrectOutfit());
        assertEquals(null, customer.getDialogue());
    }


    /**
     * Tests that a null personality is rejected.
     */
    @Test
    public void testNullPersonality()
    {
        

        try
        {
            new Customer(null, outfit, options);
            assertEquals("Exception expected", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Customer arguments cannot be null.",
                e.getMessage());
        }
    }


    /**
     * Tests that a null correct outfit is rejected.
     */
    @Test
    public void testNullOutfit()
    {
        

        try
        {
            new Customer(Personality.KIND, null, options);
            assertEquals("Exception expected", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Customer arguments cannot be null.",
                e.getMessage());
        }
    }


    /**
     * Tests that null clothing options are rejected.
     */
    @Test
    public void testNullOptions()
    {
        

        try
        {
            new Customer(Personality.KIND, outfit, null);
            assertEquals("Exception expected", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Customer arguments cannot be null.",
                e.getMessage());
        }
    }


    /**
     * Tests that an incorrect number of clothing
     * categories is rejected.
     */
    @Test
    public void testInvalidCategoryCount()
    {
        

        ClothingItem[][] invalid = {
            hats,
            tops
        };

        try
        {
            new Customer(Personality.KIND, outfit, invalid);
            assertEquals("Exception expected", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Customer must have exactly three clothing categories.",
                e.getMessage());
        }
    }


    /**
     * Tests that a null clothing category array
     * is rejected.
     */
    @Test
    public void testNullCategoryArray()
    {
        
        options[0] = null;

        try
        {
            new Customer(Personality.KIND, outfit, options);
            assertEquals("Exception expected", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Each category must contain exactly three options.",
                e.getMessage());
        }
    }


    /**
     * Tests that a clothing category containing
     * fewer than three options is rejected.
     */
    @Test
    public void testInvalidOptionCount()
    {
        

        options[1] = new ClothingItem[] {
            tops[0],
            tops[1]
        };

        try
        {
            new Customer(Personality.KIND, outfit, options);
            assertEquals("Exception expected", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Each category must contain exactly three options.",
                e.getMessage());
        }
    }


    /**
     * Tests that a null clothing item within
     * the available options is rejected.
     */
    @Test
    public void testNullClothingItem()
    {
        
        options[2][1] = null;

        try
        {
            new Customer(Personality.KIND, outfit, options);
            assertEquals("Exception expected", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Clothing option is null or has an invalid category.",
                e.getMessage());
        }
    }


    /**
     * Tests that a clothing item belonging to
     * the wrong category is rejected.
     */
    @Test
    public void testWrongClothingCategory()
    {
        
        options[0][1] = tops[1];

        try
        {
            new Customer(Personality.KIND, outfit, options);
            assertEquals("Exception expected", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Clothing option is null or has an invalid category.",
                e.getMessage());
        }
    }


    /**
     * Tests that duplicate clothing item references
     * within the same category are rejected.
     */
    @Test
    public void testDuplicateOptions()
    {
        
        options[0][1] = options[0][0];

        try
        {
            new Customer(Personality.KIND, outfit, options);
            assertEquals("Exception expected", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Clothing options must be distinct.",
                e.getMessage());
        }
    }


    /**
     * Tests that a category without its required
     * correct clothing item is rejected.
     */
    @Test
    public void testMissingCorrectItem()
    {
        

        options[1][0] =
            new ClothingItem("Jacket", ClothingCategory.TOP);

        try
        {
            new Customer(Personality.KIND, outfit, options);
            assertEquals("Exception expected", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Each category must contain exactly one correct item.",
                e.getMessage());
        }
    }


    /**
     * Tests that getAvailableOptions returns the
     * correct clothing item references.
     */
    @Test
    public void testGetAvailableOptions()
    {
        

        ClothingItem[][] returned = customer.getAvailableOptions();

        assertEquals(3, returned.length);

        for (int i = 0; i < 3; i++)
        {
            assertEquals(3, returned[i].length);

            for (int j = 0; j < 3; j++)
            {
                assertEquals(options[i][j], returned[i][j]);
            }
        }
    }


    /**
     * Tests that modifying the original options
     * after construction does not affect the customer.
     */
    @Test
    public void testConstructorDefensiveCopy()
    {
        

        options[0][0] = hats[1];

        ClothingItem[][] returned = customer.getAvailableOptions();

        assertEquals(hats[0], returned[0][0]);
    }


    /**
     * Tests that modifying returned clothing options
     * does not affect the customer's stored options.
     */
    @Test
    public void testGetterDefensiveCopy()
    {
        

        ClothingItem[][] returned = customer.getAvailableOptions();
        returned[0][0] = hats[1];

        assertEquals(
            hats[0],
            customer.getAvailableOptions()[0][0]);
    }


    /**
     * Tests that valid dialogue is stored and returned.
     */
    @Test
    public void testSetDialogue()
    {
        

        customer.setDialogue("Hello!");

        assertEquals("Hello!", customer.getDialogue());
    }


    /**
     * Tests that null dialogue is rejected.
     */
    @Test
    public void testNullDialogue()
    {
        

        try
        {
            customer.setDialogue(null);
            assertEquals("Exception expected", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Customer dialogue cannot be null or blank.",
                e.getMessage());
        }
    }


    /**
     * Tests that blank dialogue is rejected.
     */
    @Test
    public void testBlankDialogue()
    {
        

        try
        {
            customer.setDialogue("   ");
            assertEquals("Exception expected", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Customer dialogue cannot be null or blank.",
                e.getMessage());
        }
    }
}
