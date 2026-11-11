
package purrfectfit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Tests the ClothingItem class to verify that clothing
 * items are constructed correctly, their attributes are
 * accessible, and invalid inputs are rejected.
 *
 * @author Lovesh Kataria
 * @version 11.11.2026
 */
public class ClothingItemTest
{
    private ClothingItem item;

    /**
     * Initializes a valid clothing item for testing
     * its name and category.
     */
    public void setUp()
    {
        item = new ClothingItem("Baseball Cap", ClothingCategory.HAT);
    }


    /**
     * Tests that a valid clothing item stores and
     * returns its specified name.
     */
    @Test
    public void testGetName()
    {
        setUp();
        assertEquals("Baseball Cap", item.getName());
    }


    /**
     * Tests that a valid clothing item stores and
     * returns its specified category.
     */
    @Test
    public void testGetCategory()
    {
        setUp();
        assertEquals(ClothingCategory.HAT, item.getCategory());
    }


    /**
     * Tests that constructing a clothing item with a
     * null name throws an IllegalArgumentException.
     */
    @Test
    public void testNullName()
    {
        try
        {
            new ClothingItem(null, ClothingCategory.HAT);
            assertEquals("Expected IllegalArgumentException", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Clothing item name cannot be null or blank.",
                e.getMessage());
        }
    }


    /**
     * Tests that constructing a clothing item with a
     * blank name throws an IllegalArgumentException.
     */
    @Test
    public void testBlankName()
    {
        try
        {
            new ClothingItem("   ", ClothingCategory.HAT);
            assertEquals("Expected IllegalArgumentException", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Clothing item name cannot be null or blank.",
                e.getMessage());
        }
    }


    /**
     * Tests that constructing a clothing item with a
     * null category throws an IllegalArgumentException.
     */
    @Test
    public void testNullCategory()
    {
        try
        {
            new ClothingItem("Baseball Cap", null);
            assertEquals("Expected IllegalArgumentException", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Clothing item category cannot be null.",
                e.getMessage());
        }
    }
}
