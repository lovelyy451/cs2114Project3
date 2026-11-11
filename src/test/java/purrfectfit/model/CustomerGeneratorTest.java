
package purrfectfit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.Random;

/**
 * Tests the CustomerGenerator class to verify customer
 * generation, constructor validation, dialogue assignment,
 * and automatic fallback behavior.
 *
 * @author Lovesh Kataria
 * @version 11.11.2026
 */
public class CustomerGeneratorTest
{
    private ClothingCatalog catalog;
    private Random random;
    private CustomerGenerator generator;

    /**
     * Initializes a seeded clothing catalog, random
     * generator, and customer generator for testing.
     */
    @BeforeEach
    public void setUp()
    {
        catalog = new ClothingCatalog(new Random(42));
        random = new Random(42);

        generator = new CustomerGenerator(
            catalog,
            new FallBackDialogueGenerator(),
            random);
    }


    /**
     * Tests that the default constructor creates a
     * generator capable of producing a valid customer.
     */
    @Test
    public void testDefaultConstructor()
    {
        CustomerGenerator defaultGenerator = new CustomerGenerator();
        Customer customer = defaultGenerator.generateCustomer();

        assertEquals(null, customer.getDialogue());
        assertEquals(
            3,
            customer.getAvailableOptions().length);
    }


    /**
     * Tests that the constructor rejects a null catalog.
     */
    @Test
    public void testNullCatalog()
    {
        

        try
        {
            new CustomerGenerator(
                null,
                new FallBackDialogueGenerator(),
                random);
            assertEquals("Exception expected", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Customer generator arguments cannot be null.",
                e.getMessage());
        }
    }


    /**
     * Tests that the constructor rejects a null
     * primary dialogue generator.
     */
    @Test
    public void testNullDialogueGenerator()
    {
        

        try
        {
            new CustomerGenerator(catalog, null, random);
            assertEquals("Exception expected", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Customer generator arguments cannot be null.",
                e.getMessage());
        }
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
            new CustomerGenerator(
                catalog,
                new FallBackDialogueGenerator(),
                null);
            assertEquals("Exception expected", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Customer generator arguments cannot be null.",
                e.getMessage());
        }
    }


    /**
     * Tests that generateCustomer creates a customer
     * with valid clothing options and no prepared dialogue.
     */
    @Test
    public void testGenerateCustomer()
    {
        

        Customer customer = generator.generateCustomer();

        assertEquals(null, customer.getDialogue());
        assertEquals(3, customer.getAvailableOptions().length);

        ClothingItem[][] options = customer.getAvailableOptions();

        for (int i = 0; i < 3; i++)
        {
            assertEquals(3, options[i].length);
        }
    }


    /**
     * Tests that valid dialogue from the primary
     * generator is assigned without using fallback.
     */
    @Test
    public void testPrepareDialogueValid()
    {
        

        DialogueGenerator primary = new DialogueGenerator()
        {
            /**
             * Returns valid dialogue for testing.
             *
             * @param customer
             *            the customer receiving dialogue
             * @return the predefined dialogue
             */
            @Override
            public String generateDialogue(Customer customer)
            {
                return "Welcome to Purrfect Fit!";
            }
        };

        CustomerGenerator customGenerator =
            new CustomerGenerator(catalog, primary, random);

        Customer customer = customGenerator.generateCustomer();

        customGenerator.prepareDialogue(customer);

        assertEquals(
            "Welcome to Purrfect Fit!",
            customer.getDialogue());
    }


    /**
     * Tests that null dialogue from the primary
     * generator causes fallback dialogue to be used.
     */
    @Test
    public void testPrepareDialogueNullResponse()
    {
        

        DialogueGenerator primary = new DialogueGenerator()
        {
            /**
             * Returns null to trigger fallback dialogue.
             *
             * @param customer
             *            the customer receiving dialogue
             * @return null to simulate an invalid response
             */
            @Override
            public String generateDialogue(Customer customer)
            {
                return null;
            }
        };

        CustomerGenerator customGenerator =
            new CustomerGenerator(catalog, primary, random);

        Customer customer = customGenerator.generateCustomer();

        customGenerator.prepareDialogue(customer);

        FallBackDialogueGenerator fallback =
            new FallBackDialogueGenerator();

        assertEquals(
            fallback.generateDialogue(customer),
            customer.getDialogue());
    }


    /**
     * Tests that blank dialogue from the primary
     * generator causes fallback dialogue to be used.
     */
    @Test
    public void testPrepareDialogueBlankResponse()
    {
        

        DialogueGenerator primary = new DialogueGenerator()
        {
            /**
             * Returns blank dialogue to trigger fallback.
             *
             * @param customer
             *            the customer receiving dialogue
             * @return a blank dialogue string
             */
            @Override
            public String generateDialogue(Customer customer)
            {
                return "   ";
            }
        };

        CustomerGenerator customGenerator =
            new CustomerGenerator(catalog, primary, random);

        Customer customer = customGenerator.generateCustomer();

        customGenerator.prepareDialogue(customer);

        FallBackDialogueGenerator fallback =
            new FallBackDialogueGenerator();

        assertEquals(
            fallback.generateDialogue(customer),
            customer.getDialogue());
    }


    /**
     * Tests that an exception from the primary
     * generator causes fallback dialogue to be used.
     */
    @Test
    public void testPrepareDialogueException()
    {
        

        DialogueGenerator primary = new DialogueGenerator()
        {
            /**
             * Simulates a primary dialogue generator failure.
             *
             * @param customer
             *            the customer receiving dialogue
             * @return no value because an exception is thrown
             * @throws RuntimeException
             *             always, to simulate generator failure
             */
            @Override
            public String generateDialogue(Customer customer)
            {
                throw new RuntimeException("Generator failed.");
            }
        };

        CustomerGenerator customGenerator =
            new CustomerGenerator(catalog, primary, random);

        Customer customer = customGenerator.generateCustomer();

        customGenerator.prepareDialogue(customer);

        FallBackDialogueGenerator fallback =
            new FallBackDialogueGenerator();

        assertEquals(
            fallback.generateDialogue(customer),
            customer.getDialogue());
    }


    /**
     * Tests that prepareDialogue rejects a null customer.
     */
    @Test
    public void testPrepareDialogueNullCustomer()
    {
        

        try
        {
            generator.prepareDialogue(null);
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
