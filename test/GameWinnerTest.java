import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameWinnerTest {

    private ScoreCalculator scoreCalculator;
    private ArrayList<Player> players;

    @Before
    public void setUp() {
        scoreCalculator = new ScoreCalculator();
        players = new ArrayList<>();

        // Create 3 players
        Player p1 = new Player("Alice", true);
        Player p2 = new Player("Bob", true);
        Player p3 = new Player("Charlie", true);

        // Assign points
        p1.addPoints(320);
        p2.addPoints(510); // Winner
        p3.addPoints(470);

        players.add(p1);
        players.add(p2);
        players.add(p3);
    }

    @Test
    public void testCheckForGameWinnerReturnsCorrectPlayer() {
        Player winner = scoreCalculator.checkForGameWinner(players);

        assertNotNull("There should be a winner", winner);
        assertEquals("Bob", winner.getName());
        assertTrue("Winner should have 500+ points", winner.getPoints() >= 500);
    }

    @Test
    public void testNoGameWinnerIfNoOneHas500() {
        for (Player p : players) {
            p.setPoints(0); // Directly reset points to 0
        }

        Player winner = scoreCalculator.checkForGameWinner(players);
        assertNull("There should be no winner", winner);
    }
}

