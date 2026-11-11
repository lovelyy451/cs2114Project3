
package purrfectfit.model;

import java.util.Random;

/**
 * Generates customers for Purrfect Fit.
 *
 * Creates customers with random personalities, outfits,
 * and clothing options. Prepares customer dialogue using
 * a dialogue generator with automatic fallback support.
 *
 * @author Lovesh Kataria
 * @version 11.11.2026
 */
public class CustomerGenerator
{
    private final ClothingCatalog catalog;
    private final DialogueGenerator dialogueGenerator;
    private final FallBackDialogueGenerator fallbackGenerator;
    private final Random random;

    /**
     * Creates a customer generator using a new clothing
     * catalog, random generator, and fallback dialogue.
     */
    public CustomerGenerator()
    {
        this(
            new ClothingCatalog(),
            new FallBackDialogueGenerator(),
            new Random());
    }


    /**
     * Creates a customer generator using the specified
     * catalog, dialogue generator, and random generator.
     *
     * @param catalog
     *            the catalog used to generate clothing
     * @param dialogueGenerator
     *            the primary dialogue generator
     * @param random
     *            the random generator for personalities
     * @throws IllegalArgumentException
     *             if any argument is null
     */
    public CustomerGenerator(
        ClothingCatalog catalog,
        DialogueGenerator dialogueGenerator,
        Random random)
    {
        if (catalog == null || dialogueGenerator == null
            || random == null)
        {
            throw new IllegalArgumentException(
                "Customer generator arguments cannot be null.");
        }

        this.catalog = catalog;
        this.dialogueGenerator = dialogueGenerator;
        this.fallbackGenerator = new FallBackDialogueGenerator();
        this.random = random;
    }


    /**
     * Generates a customer with a random personality,
     * correct outfit, and available clothing options.
     *
     * Dialogue is prepared separately after creation.
     *
     * @return the newly generated customer
     */
    public Customer generateCustomer()
    {
        Personality[] personalities = Personality.values();

        Personality personality =
            personalities[random.nextInt(personalities.length)];

        Outfit correctOutfit = catalog.generateOutfit();

        ClothingItem[][] options =
            catalog.generateOptions(correctOutfit);

        return new Customer(
            personality,
            correctOutfit,
            options);
    }


    /**
     * Generates and assigns dialogue to a customer.
     *
     * Uses the primary dialogue generator first.
     * If it fails or returns invalid dialogue, the
     * fallback generator provides the dialogue instead.
     *
     * @param customer
     *            the customer whose dialogue is prepared
     * @throws IllegalArgumentException
     *             if the customer is null
     */
    public void prepareDialogue(Customer customer)
    {
        if (customer == null)
        {
            throw new IllegalArgumentException(
                "Customer cannot be null.");
        }

        String dialogue = null;

        try
        {
            dialogue = dialogueGenerator.generateDialogue(customer);
        }
        catch (RuntimeException exception)
        {
            dialogue = null;
        }

        if (dialogue == null || dialogue.trim().isEmpty())
        {
            dialogue = fallbackGenerator.generateDialogue(customer);
        }

        customer.setDialogue(dialogue);
    }
}
