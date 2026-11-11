
package purrfectfit.model;

/**
 * Represents a single clothing item in Purrfect Fit.
 *
 * Each clothing item has a name and belongs to one
 * clothing category. Clothing items are immutable.
 *
 * @author Lovesh Kataria
 * @version 11.11.2026
 */
public class ClothingItem
{
    private final String name;
    private final ClothingCategory category;

    /**
     * Creates a clothing item with the specified name
     * and category.
     *
     * @param name
     *            the name of the clothing item
     * @param category
     *            the category of the clothing item
     * @throws IllegalArgumentException
     *             if the name is null or blank, or the
     *             category is null
     */
    public ClothingItem(String name, ClothingCategory category)
    {
        if (name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "Clothing item name cannot be null or blank.");
        }

        if (category == null)
        {
            throw new IllegalArgumentException(
                "Clothing item category cannot be null.");
        }

        this.name = name;
        this.category = category;
    }


    /**
     * Returns the name of this clothing item.
     *
     * @return the clothing item's name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Returns the category of this clothing item.
     *
     * @return the clothing item's category
     */
    public ClothingCategory getCategory()
    {
        return category;
    }
}
