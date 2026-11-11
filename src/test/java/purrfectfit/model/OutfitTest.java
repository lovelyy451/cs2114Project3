
package purrfectfit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

/**
 * Tests the Outfit class to verify valid construction,
 * clothing category validation, getters, and outfit matching.
 *
 * @author Lovesh Kataria
 * @version 11.11.2026
 */
public class OutfitTest
{
    private ClothingItem hat;
    private ClothingItem top;
    private ClothingItem shoes;
    private Outfit outfit;

    /**
     * Initializes valid clothing items and an outfit
     * for testing.
     */
    @BeforeEach
    public void setUp()
    {
        hat = new ClothingItem("Cap", ClothingCategory.HAT);
        top = new ClothingItem("Shirt", ClothingCategory.TOP);
        shoes = new ClothingItem("Sneakers", ClothingCategory.SHOES);
        outfit = new Outfit(hat, top, shoes);
    }


    /**
     * Tests that a valid outfit returns the correct
     * hat, top, and shoes.
     */
    @Test
    public void testGetters()
    {
        

        assertEquals(hat, outfit.getHat());
        assertEquals(top, outfit.getTop());
        assertEquals(shoes, outfit.getShoes());
    }


    /**
     * Tests that a null hat is rejected by the constructor.
     */
    @Test
    public void testNullHat()
    {
        

        try
        {
            new Outfit(null, top, shoes);
            assertEquals("Exception expected", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Outfit clothing items cannot be null.",
                e.getMessage());
        }
    }


    /**
     * Tests that a null top is rejected by the constructor.
     */
    @Test
    public void testNullTop()
    {
        

        try
        {
            new Outfit(hat, null, shoes);
            assertEquals("Exception expected", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Outfit clothing items cannot be null.",
                e.getMessage());
        }
    }


    /**
     * Tests that null shoes are rejected by the constructor.
     */
    @Test
    public void testNullShoes()
    {
        

        try
        {
            new Outfit(hat, top, null);
            assertEquals("Exception expected", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Outfit clothing items cannot be null.",
                e.getMessage());
        }
    }


    /**
     * Tests that an item from the wrong category
     * cannot occupy the hat position.
     */
    @Test
    public void testInvalidHatCategory()
    {
        

        try
        {
            new Outfit(top, top, shoes);
            assertEquals("Exception expected", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Outfit clothing items must match their categories.",
                e.getMessage());
        }
    }


    /**
     * Tests that an item from the wrong category
     * cannot occupy the top position.
     */
    @Test
    public void testInvalidTopCategory()
    {
        

        try
        {
            new Outfit(hat, hat, shoes);
            assertEquals("Exception expected", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Outfit clothing items must match their categories.",
                e.getMessage());
        }
    }


    /**
     * Tests that an item from the wrong category
     * cannot occupy the shoes position.
     */
    @Test
    public void testInvalidShoesCategory()
    {
        

        try
        {
            new Outfit(hat, top, hat);
            assertEquals("Exception expected", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Outfit clothing items must match their categories.",
                e.getMessage());
        }
    }


    /**
     * Tests that an outfit matches itself when all
     * clothing item references are identical.
     */
    @Test
    public void testMatchesSameOutfit()
    {
        

        assertEquals(true, outfit.matches(outfit));
    }


    /**
     * Tests that two separate outfits match when
     * they contain the same clothing item references.
     */
    @Test
    public void testMatchesSameItems()
    {
        

        Outfit other = new Outfit(hat, top, shoes);

        assertEquals(true, outfit.matches(other));
    }


    /**
     * Tests that matching against a null outfit
     * returns false.
     */
    @Test
    public void testMatchesNull()
    {
        

        assertEquals(false, outfit.matches(null));
    }


    /**
     * Tests that outfits do not match when their
     * hat references differ.
     */
    @Test
    public void testMatchesDifferentHat()
    {
        

        ClothingItem otherHat =
            new ClothingItem("Beanie", ClothingCategory.HAT);
        Outfit other = new Outfit(otherHat, top, shoes);

        assertEquals(false, outfit.matches(other));
    }


    /**
     * Tests that outfits do not match when their
     * top references differ.
     */
    @Test
    public void testMatchesDifferentTop()
    {
        

        ClothingItem otherTop =
            new ClothingItem("Jacket", ClothingCategory.TOP);
        Outfit other = new Outfit(hat, otherTop, shoes);

        assertEquals(false, outfit.matches(other));
    }


    /**
     * Tests that outfits do not match when their
     * shoes references differ.
     */
    @Test
    public void testMatchesDifferentShoes()
    {
        

        ClothingItem otherShoes =
            new ClothingItem("Boots", ClothingCategory.SHOES);
        Outfit other = new Outfit(hat, top, otherShoes);

        assertEquals(false, outfit.matches(other));
    }
}
