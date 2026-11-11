package purrfectfit.model;

import java.util.Random;

/**
 * Stores the available clothing items in Purrfect Fit.
 *
 * The catalog contains five items per clothing category.
 * It generates random outfits and clothing options using
 * the same predefined clothing item objects.
 *
 * @author Lovesh Kataria
 * @version 11.11.2026
 */
public class ClothingCatalog
{
    private final ClothingItem[] hats;
    private final ClothingItem[] tops;
    private final ClothingItem[] shoes;
    private final Random random;

    /**
     * Creates a clothing catalog using a new random generator.
     */
    public ClothingCatalog()
    {
        this(new Random());
    }


    /**
     * Creates a clothing catalog using the specified
     * random generator.
     *
     * @param random
     *            the random generator used to select items
     * @throws IllegalArgumentException
     *             if random is null
     */
    public ClothingCatalog(Random random)
    {
        if (random == null)
        {
            throw new IllegalArgumentException(
                "Random generator cannot be null.");
        }

        this.random = random;

        hats = new ClothingItem[] {
            new ClothingItem("Crown", ClothingCategory.HAT),
            new ClothingItem("Top Hat", ClothingCategory.HAT),
            new ClothingItem("Bow", ClothingCategory.HAT),
            new ClothingItem("Sun Hat", ClothingCategory.HAT),
            new ClothingItem("Cap", ClothingCategory.HAT)
        };

        tops = new ClothingItem[] {
            new ClothingItem("Large Dress", ClothingCategory.TOP),
            new ClothingItem("Suit", ClothingCategory.TOP),
            new ClothingItem("Cute Outfit", ClothingCategory.TOP),
            new ClothingItem("Beach Outfit", ClothingCategory.TOP),
            new ClothingItem("Casual Outfit", ClothingCategory.TOP)
        };

        shoes = new ClothingItem[] {
            new ClothingItem("Heels", ClothingCategory.SHOES),
            new ClothingItem("Fancy Shoes", ClothingCategory.SHOES),
            new ClothingItem("Mary Janes", ClothingCategory.SHOES),
            new ClothingItem("Sandals", ClothingCategory.SHOES),
            new ClothingItem("Sneakers", ClothingCategory.SHOES)
        };
    }


    /**
     * Generates a random outfit using existing catalog items.
     *
     * @return an outfit containing one randomly selected
     *         hat, top, and pair of shoes
     */
    public Outfit generateOutfit()
    {
        ClothingItem hat = hats[random.nextInt(hats.length)];
        ClothingItem top = tops[random.nextInt(tops.length)];
        ClothingItem shoe = shoes[random.nextInt(shoes.length)];

        return new Outfit(hat, top, shoe);
    }


    /**
     * Generates three clothing options for each category.
     *
     * Each category contains the correct item and two
     * distinct incorrect items in randomized positions.
     *
     * @param correctOutfit
     *            the outfit containing the correct items
     * @return a two-dimensional array containing three
     *         hats, three tops, and three shoe options
     * @throws IllegalArgumentException
     *             if the outfit is null or contains items
     *             that do not belong to this catalog
     */
    public ClothingItem[][] generateOptions(Outfit correctOutfit)
    {
        if (correctOutfit == null)
        {
            throw new IllegalArgumentException(
                "Correct outfit cannot be null.");
        }

        ClothingItem[] correctItems = {
            correctOutfit.getHat(),
            correctOutfit.getTop(),
            correctOutfit.getShoes()
        };

        ClothingItem[][] categories = {
            hats,
            tops,
            shoes
        };

        ClothingItem[][] options = new ClothingItem[3][3];

        for (int category = 0; category < categories.length; category++)
        {
            ClothingItem[] available = categories[category];
            ClothingItem correct = correctItems[category];

            boolean found = false;

            for (int i = 0; i < available.length; i++)
            {
                if (available[i] == correct)
                {
                    found = true;
                    break;
                }
            }

            if (!found)
            {
                throw new IllegalArgumentException(
                    "Correct outfit contains an item outside the catalog.");
            }

            options[category][0] = correct;

            int count = 1;

            while (count < 3)
            {
                ClothingItem candidate =
                    available[random.nextInt(available.length)];

                boolean duplicate = false;

                for (int i = 0; i < count; i++)
                {
                    if (options[category][i] == candidate)
                    {
                        duplicate = true;
                        break;
                    }
                }

                if (!duplicate)
                {
                    options[category][count] = candidate;
                    count++;
                }
            }

            for (int i = options[category].length - 1; i > 0; i--)
            {
                int j = random.nextInt(i + 1);

                ClothingItem temp = options[category][i];
                options[category][i] = options[category][j];
                options[category][j] = temp;
            }
        }

        return options;
    }
}
