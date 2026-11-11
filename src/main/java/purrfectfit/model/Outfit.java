
package purrfectfit.model;

/**
 * Represents a complete outfit in Purrfect Fit.
 *
 * Each outfit contains exactly one hat, one top,
 * and one pair of shoes. Outfits are immutable.
 *
 * @author Lovesh Kataria
 * @version 11.11.2026
 */
public class Outfit
{
    private final ClothingItem hat;
    private final ClothingItem top;
    private final ClothingItem shoes;

    /**
     * Creates an outfit with the specified clothing items.
     *
     * @param hat
     *            the hat included in the outfit
     * @param top
     *            the top included in the outfit
     * @param shoes
     *            the shoes included in the outfit
     * @throws IllegalArgumentException
     *             if any item is null or belongs to
     *             the wrong clothing category
     */
    public Outfit(ClothingItem hat, ClothingItem top, ClothingItem shoes)
    {
        if (hat == null || top == null || shoes == null)
        {
            throw new IllegalArgumentException(
                "Outfit clothing items cannot be null.");
        }

        if (hat.getCategory() != ClothingCategory.HAT
            || top.getCategory() != ClothingCategory.TOP
            || shoes.getCategory() != ClothingCategory.SHOES)
        {
            throw new IllegalArgumentException(
                "Outfit clothing items must match their categories.");
        }

        this.hat = hat;
        this.top = top;
        this.shoes = shoes;
    }


    /**
     * Returns the hat included in this outfit.
     *
     * @return the outfit's hat
     */
    public ClothingItem getHat()
    {
        return hat;
    }


    /**
     * Returns the top included in this outfit.
     *
     * @return the outfit's top
     */
    public ClothingItem getTop()
    {
        return top;
    }


    /**
     * Returns the shoes included in this outfit.
     *
     * @return the outfit's shoes
     */
    public ClothingItem getShoes()
    {
        return shoes;
    }


    /**
     * Determines whether this outfit matches another outfit.
     *
     * @param other
     *            the outfit to compare against
     * @return true if all three clothing items match,
     *         false otherwise
     */
    public boolean matches(Outfit other)
    {
        if (other == null)
        {
            return false;
        }

        return hat == other.hat
            && top == other.top
            && shoes == other.shoes;
    }
}
