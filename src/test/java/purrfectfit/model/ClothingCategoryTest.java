
package purrfectfit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Tests the ClothingCategory enum to verify that
 * all three clothing categories are defined correctly.
 *
 * @author Lovesh Kataria
 * @version 11.11.2026
 */
public class ClothingCategoryTest
{
    /**
     * Tests that the ClothingCategory enum contains
     * exactly HAT, TOP, and SHOES in the expected order.
     */
    @Test
    public void testClothingCategories()
    {
        ClothingCategory[] categories = ClothingCategory.values();

        assertEquals(3, categories.length);
        assertEquals(ClothingCategory.HAT, categories[0]);
        assertEquals(ClothingCategory.TOP, categories[1]);
        assertEquals(ClothingCategory.SHOES, categories[2]);
    }
}
