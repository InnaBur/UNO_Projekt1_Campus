import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class testAwardPointsAndRanking {


    private ScoreCalculator scoreCalculator;
    private Player winner;
    private Player p2;
    private Player p3;
    private ArrayList<Player> players;

    @Before
    public void setUp() {
        // Wird vor jedem Test aufgerufen
        scoreCalculator = new ScoreCalculator();

        winner = new Player("Alice", true);
        p2 = new Player("Bob", true);
        p3 = new Player("Charlie", true);

        p2.addCard(new Card("R5", false));   // 5 Punkte
        p2.addCard(new Card("R5", false));   // 5 Punkte
        p3.addCard(new Card("G+2", true));   // 20 Punkte

        players = new ArrayList<>();
        players.add(winner);
        players.add(p2);
        players.add(p3);

        scoreCalculator.printRanking(players);
    }

    @Test
    public void testAwardPointsToWinner() {
        int awardedPoints = scoreCalculator.awardPointsToWinner(players, winner);

        // Gewinner bekommt 5 + 5 + 20 = 30 Punkte gutgeschrieben
        assertEquals(30, awardedPoints);
        assertEquals(30, winner.getPoints());

        System.out.println(winner.getName() + " is the winner Name. Points: " + winner.getPoints());
        System.out.println("Ranking:");
        System.out.println();
        ScoreCalculator sc = new ScoreCalculator();
        sc.printRanking(players);
    }

    @After
    public void tearDown() {
        // Wird nach jedem Test aufgerufen - z.B. Aufräumen, Objekte zurücksetzen
        players.clear();
    }
}



