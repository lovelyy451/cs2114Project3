
package purrfectfit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

/**
 * Tests the FallBackDialogueGenerator class to verify
 * dialogue generation for every customer personality
 * and rejection of null customers.
 *
 * @author Lovesh Kataria
 * @version 11.11.2026
 */
public class FallBackDialogueGeneratorTest
{
    private FallBackDialogueGenerator generator;
    private Outfit outfit;
    private ClothingItem[][] options;

    /**
     * Initializes the fallback dialogue generator,
     * a valid outfit, and clothing options for testing.
     */
    @BeforeEach
    public void setUp()
    {
        generator = new FallBackDialogueGenerator();

        ClothingItem hat =
            new ClothingItem("Cap", ClothingCategory.HAT);
        ClothingItem top =
            new ClothingItem("Suit", ClothingCategory.TOP);
        ClothingItem shoes =
            new ClothingItem("Sneakers", ClothingCategory.SHOES);

        outfit = new Outfit(hat, top, shoes);

        options = new ClothingItem[][] {
            {
                hat,
                new ClothingItem("Crown", ClothingCategory.HAT),
                new ClothingItem("Bow", ClothingCategory.HAT)
            },
            {
                top,
                new ClothingItem("Shirt", ClothingCategory.TOP),
                new ClothingItem("Dress", ClothingCategory.TOP)
            },
            {
                shoes,
                new ClothingItem("Boots", ClothingCategory.SHOES),
                new ClothingItem("Sandals", ClothingCategory.SHOES)
            }
        };
    }


    /**
     * Tests that an aggressive customer receives
     * the aggressive dialogue template containing
     * all three required clothing items.
     */
    @Test
    public void testAggressiveDialogue()
    {
        

        Customer customer =
            new Customer(Personality.AGGRESSIVE, outfit, options);

        assertEquals(
            "Get me the Cap, Suit, and Sneakers. Now!",
            generator.generateDialogue(customer));
    }


    /**
     * Tests that a kind customer receives the polite
     * dialogue template containing all three required
     * clothing items.
     */
    @Test
    public void testKindDialogue()
    {
        

        Customer customer =
            new Customer(Personality.KIND, outfit, options);

        assertEquals(
            "Hi! Could you please help me find the "
                + "Cap, Suit, and Sneakers? Thank you!",
            generator.generateDialogue(customer));
    }


    /**
     * Tests that an impatient customer receives
     * the impatient dialogue template containing
     * all three required clothing items.
     */
    @Test
    public void testImpatientDialogue()
    {
        

        Customer customer =
            new Customer(Personality.IMPATIENT, outfit, options);

        assertEquals(
            "I need the Cap, Suit, and Sneakers. Please hurry!",
            generator.generateDialogue(customer));
    }


    /**
     * Tests that a respectful customer receives
     * the respectful dialogue template containing
     * all three required clothing items.
     */
    @Test
    public void testRespectfulDialogue()
    {
        

        Customer customer =
            new Customer(Personality.RESPECTFUL, outfit, options);

        assertEquals(
            "Good afternoon. I would appreciate the "
                + "Cap, Suit, and Sneakers.",
            generator.generateDialogue(customer));
    }


    /**
     * Tests that generating dialogue for a null
     * customer throws an IllegalArgumentException.
     */
    @Test
    public void testNullCustomer()
    {
        

        try
        {
            generator.generateDialogue(null);
            assertEquals("Exception expected", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Customer cannot be null.",
                e.getMessage());
        }
    }
}
