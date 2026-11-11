
package purrfectfit.model;

/**
 * Represents a customer in Purrfect Fit.
 *
 * Each customer has a personality, a correct outfit,
 * available clothing options, and dialogue.
 *
 * @author Lovesh Kataria
 * @version 11.11.2026
 */
public class Customer
{
    private final Personality personality;
    private final Outfit correctOutfit;
    private final ClothingItem[][] availableOptions;
    private String dialogue;

    /**
     * Creates a customer with the specified personality,
     * correct outfit, and available clothing options.
     *
     * @param personality
     *            the customer's personality
     * @param correctOutfit
     *            the outfit the customer wants
     * @param availableOptions
     *            the clothing choices for each category
     * @throws IllegalArgumentException
     *             if any argument is null, the options
     *             have an invalid structure, or the correct
     *             items are missing from their categories
     */
    public Customer(
        Personality personality,
        Outfit correctOutfit,
        ClothingItem[][] availableOptions)
    {
        if (personality == null || correctOutfit == null
            || availableOptions == null)
        {
            throw new IllegalArgumentException(
                "Customer arguments cannot be null.");
        }

        if (availableOptions.length != 3)
        {
            throw new IllegalArgumentException(
                "Customer must have exactly three clothing categories.");
        }

        ClothingItem[] correctItems = {
            correctOutfit.getHat(),
            correctOutfit.getTop(),
            correctOutfit.getShoes()
        };

        ClothingCategory[] categories = {
            ClothingCategory.HAT,
            ClothingCategory.TOP,
            ClothingCategory.SHOES
        };

        ClothingItem[][] copiedOptions = new ClothingItem[3][3];

        for (int category = 0; category < 3; category++)
        {
            if (availableOptions[category] == null
                || availableOptions[category].length != 3)
            {
                throw new IllegalArgumentException(
                    "Each category must contain exactly three options.");
            }

            int correctCount = 0;

            for (int i = 0; i < 3; i++)
            {
                ClothingItem item = availableOptions[category][i];

                if (item == null
                    || item.getCategory() != categories[category])
                {
                    throw new IllegalArgumentException(
                        "Clothing option is null or has an invalid category.");
                }

                for (int j = 0; j < i; j++)
                {
                    if (item == availableOptions[category][j])
                    {
                        throw new IllegalArgumentException(
                            "Clothing options must be distinct.");
                    }
                }

                if (item == correctItems[category])
                {
                    correctCount++;
                }

                copiedOptions[category][i] = item;
            }

            if (correctCount != 1)
            {
                throw new IllegalArgumentException(
                    "Each category must contain exactly one correct item.");
            }
        }

        this.personality = personality;
        this.correctOutfit = correctOutfit;
        this.availableOptions = copiedOptions;
        this.dialogue = null;
    }


    /**
     * Returns the customer's personality.
     *
     * @return the customer's personality
     */
    public Personality getPersonality()
    {
        return personality;
    }


    /**
     * Returns the outfit requested by the customer.
     *
     * @return the customer's correct outfit
     */
    public Outfit getCorrectOutfit()
    {
        return correctOutfit;
    }


    /**
     * Returns a defensive copy of the customer's
     * available clothing options.
     *
     * @return a two-dimensional array containing
     *         the customer's clothing choices
     */
    public ClothingItem[][] getAvailableOptions()
    {
        ClothingItem[][] copy = new ClothingItem[3][3];

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                copy[i][j] = availableOptions[i][j];
            }
        }

        return copy;
    }


    /**
     * Returns the customer's dialogue.
     *
     * @return the customer's dialogue, or null
     *         if dialogue has not been assigned
     */
    public String getDialogue()
    {
        return dialogue;
    }


    /**
     * Assigns dialogue to the customer.
     *
     * @param dialogue
     *            the dialogue to assign
     * @throws IllegalArgumentException
     *             if the dialogue is null or blank
     */
    public void setDialogue(String dialogue)
    {
        if (dialogue == null || dialogue.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "Customer dialogue cannot be null or blank.");
        }

        this.dialogue = dialogue;
    }
}
