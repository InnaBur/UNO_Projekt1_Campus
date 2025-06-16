import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testPoints {

    private ScoreCalculator scoreCalculator;
    private ArrayList<Card> hand;

    @Before
    public void setUp() {
        // Vor jedem Test
        scoreCalculator = new ScoreCalculator();
        hand = new ArrayList<>();
        hand.add(new Card("r5", false));    // Rot 5 -> 5 Punkte
        hand.add(new Card("G+2", true));    // Grün +2 -> 20 Punkte
        hand.add(new Card("+4", true));     // Wild Draw Four -> 50 Punkte
    }

    @Test
    public void testCalculatePoints() {
        // Berechne Punkte für die Hand
        int points = scoreCalculator.calculatePoints(hand);

        // Erwartete Punkte: 5 + 20 + 50 = 75
        assertEquals(75, points, "Die Punktzahl sollte 75 sein");
    }

    @After
    public void tearDown() {
        // Nach jedem Test - hier z.B. Aufräumen, falls nötig
        hand.clear();
    }
}
