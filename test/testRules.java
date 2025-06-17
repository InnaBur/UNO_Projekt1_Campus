import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.ls.LSOutput;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class testRules {

    private Card topCard;
    private Card playableCardSameColor;
    private Card playableCardSameValue;

    private Card specialCardCC;
    private Card specialCardPlus4;

    private Card unplayableCard;
    Card changeDirectionCard = new Card("Rx", true); // Richtungswechselkarte in Rot

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

//    @Test
//
//
//    public void testSpecialCardCCIsAlwaysPlayable() {
//        System.out.println("Test: Farbwechselkarte 'CC' sollte immer spielbar sein");
//        assertTrue(specialCardCC.isPlayableOn(topCard));
//    }
//
//    @Test
//    public void testSpecialCardPlus4IsAlwaysPlayable() {
//        System.out.println("Test: +4 sollte immer spielbar sein.");
//        assertTrue(specialCardPlus4.isPlayableOn(topCard));
//    }
//
//    @Test
//    public void testPlayableCardSameColor() {
//        System.out.println("Test: Karte mit gleicher Farbe ('R+2') sollte spielbar sein.");
//        assertTrue(playableCardSameColor.isPlayableOn(topCard));
//    }
//
//    @Test
//    public void testPlayableCardSameValue() {
//        System.out.println("Test: Karte mit gleichem Wert ('G5') sollte spielbar sein.");
//        assertTrue(playableCardSameValue.isPlayableOn(topCard));
//    }

//    @Test
//    public void testUnplayableCard() {
//        System.out.println("Test: Karte mit anderer Farbe und anderem Wert ('B9') sollte NICHT spielbar sein.");
//        assertFalse(unplayableCard.isPlayableOn(topCard));
//    }


    @Test
    public void testSpecialCardCCIsAlwaysPlayable() {
        //Output für Menschen
        System.out.println("Test: Farbwechselkarte 'CC' sollte immer spielbar sein");
        boolean result = specialCardCC.isPlayableOn(topCard);
        System.out.println(result ? "Ergebnis: spielbar – Test bestanden" : "Ergebnis: NICHT spielbar – Test fehlgeschlagen");
        System.out.println("********************");
        //Absichern für JUnit
        assertTrue(result);
    }

    @Test
    public void testSpecialCardPlus4IsAlwaysPlayable() {
        //Output für Menschen
        System.out.println("Test: +4 sollte immer spielbar sein.");
        boolean result = specialCardPlus4.isPlayableOn(topCard);
        System.out.println(result ? "Ergebnis: spielbar – Test bestanden" : "Ergebnis: NICHT spielbar – Test fehlgeschlagen");
        System.out.println("********************");
        //Absichern für JUnit
        assertTrue(result);
    }

    @Test
    public void testPlayableCardSameColor() {
        //Output für Menschen
        System.out.println("Test: Karte mit gleicher Farbe ('R+2') sollte spielbar sein.");
        boolean result = playableCardSameColor.isPlayableOn(topCard);
        System.out.println(result ? "✔ Ergebnis: spielbar – Test bestanden" : "Ergebnis: NICHT spielbar – Test fehlgeschlagen");
        System.out.println("********************");
        //Absichern für JUnit
        assertTrue(result);
    }

    @Test
    public void testPlayableCardSameValue() {
        //Output für Menschen
        System.out.println("Test: Karte mit gleichem Wert ('G5') sollte spielbar sein.");
        boolean result = playableCardSameValue.isPlayableOn(topCard);
        System.out.println(result ? "Ergebnis: spielbar – Test bestanden" : "Ergebnis: NICHT spielbar – Test fehlgeschlagen");
        System.out.println("********************");
        //Absichern für JUnit
        assertTrue(result);
    }

    @Test
    public void testUnplayableCard() {
        //Output für Menschen
        System.out.println("Test: Karte mit anderer Farbe und anderem Wert ('B9') sollte NICHT spielbar sein.");
        boolean result = unplayableCard.isPlayableOn(topCard);
        System.out.println(!result ? "Ergebnis: NICHT spielbar – Test bestanden" : "Ergebnis: spielbar – Test fehlgeschlagen");
        System.out.println("********************");
        //Absichern für JUnit
        assertFalse(result);
    }

    @Test
    public void testChangeDirectionCardIsPlayableIfColorMatches() {
        //Output für Menschen
        System.out.println("Test: Richtungswechselkarte 'Rx' auf 'R5'");
        System.out.println("'Rx' hat dieselbe Farbe wie die Karte 'R5'");
        boolean result = changeDirectionCard.isPlayableOn(topCard);

        if (result) {
            System.out.println("Ergebnis: Karte ist spielbar → Test bestanden.");
        } else {
            System.out.println("Ergebnis: Karte ist NICHT spielbar → Test fehlgeschlagen!");
        }


        //Absichern für JUnit
        assertTrue(result);

    }


    @Test
    public void testChangeDirectionCardIsPlayableIfColorDontMatches() {

        unplayableCard = new Card("Yx", true);
        //Output für Menschen
        System.out.println("Test: Richtungswechselkarte 'Yx' auf 'R5'");
        System.out.println("'Yx' hat andere Farbe als die Karte 'R5'");
        boolean result = unplayableCard.isPlayableOn(topCard);

        if (!result) {
            System.out.println("Ergebnis: Karte ist NICHT spielbar → Test bestanden.");
        } else {
            System.out.println("Ergebnis: Karte ist  spielbar → Test fehlgeschlagen!");
        }

        //Absichern für JUnit
        assertFalse(result);

    }
}
