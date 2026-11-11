
package purrfectfit.model;

/**
 * Generates predefined customer dialogue in Purrfect Fit.
 *
 * Provides fallback dialogue when AI-generated dialogue
 * is unavailable. Each personality has one fixed template
 * that identifies the customer's required clothing items.
 *
 * @author Lovesh Kataria
 * @version 11.11.2026
 */
public class FallBackDialogueGenerator implements DialogueGenerator
{
    /**
     * Generates fallback dialogue based on the customer's
     * personality and correct outfit.
     *
     * @param customer
     *            the customer whose dialogue is generated
     * @return the generated fallback dialogue
     * @throws IllegalArgumentException
     *             if the customer is null
     */
    @Override
    public String generateDialogue(Customer customer)
    {
        if (customer == null)
        {
            throw new IllegalArgumentException(
                "Customer cannot be null.");
        }

        Outfit outfit = customer.getCorrectOutfit();

        String hat = outfit.getHat().getName();
        String top = outfit.getTop().getName();
        String shoes = outfit.getShoes().getName();

        String items = hat + ", " + top + ", and " + shoes;

        switch (customer.getPersonality())
        {
            case AGGRESSIVE:
                return "Get me the " + items + ". Now!";

            case KIND:
                return "Hi! Could you please help me find the "
                    + items + "? Thank you!";

            case IMPATIENT:
                return "I need the " + items + ". Please hurry!";

            case RESPECTFUL:
                return "Good afternoon. I would appreciate the "
                    + items + ".";

            default:
                throw new IllegalStateException(
                    "Unsupported customer personality.");
        }
    }
}
