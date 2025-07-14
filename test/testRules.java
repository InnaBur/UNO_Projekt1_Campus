import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class testRules {

    private Card topCard;
    private Card playableCardSameColor;
    private Card playableCardSameValue;

    private Card specialCardCC;
    private Card specialCardPlus4;

    private Card unplayableCard;

    @Before
    public void setUp() {
        topCard = new Card("R5", false);  // Beispiel: rote 5
        // gleiche Farbe, andere Wert/Aktion
        playableCardSameColor = new Card("R+2", true);
// gleiche Zahl, andere Farbe
        playableCardSameValue = new Card("G5", false);
        // Spezialkarten, die immer gespielt werden
        specialCardCC = new Card("CC", true);
        specialCardPlus4 = new Card("+4", true);
        // nicht spielbar -  andere Farbe, andere Zahl
        unplayableCard = new Card("B9", false);
    }

    @Test
    public void testSpecialCardCCIsAlwaysPlayable() {
        assertTrue(specialCardCC.isPlayableOn(topCard));
    }

    @Test
    public void testSpecialCardPlus4IsAlwaysPlayable() {
        assertTrue(specialCardPlus4.isPlayableOn(topCard));
    }

    @Test
    public void testPlayableCardSameColor() {
        assertTrue(playableCardSameColor.isPlayableOn(topCard));
    }

    @Test
    public void testPlayableCardSameValue() {
        assertTrue(playableCardSameValue.isPlayableOn(topCard));
    }

    @Test
    public void testUnplayableCard() {
        assertFalse(unplayableCard.isPlayableOn(topCard));
    }
}
