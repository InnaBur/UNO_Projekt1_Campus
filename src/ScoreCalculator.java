import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScoreCalculator {

    // Punktewerte für alle Karten
    private static final HashMap<String, Integer> CARD_POINTS = new HashMap<>();

    static {
        char[] COLORS = {'R', 'G', 'B', 'Y'};

        // Nummern-Karten 0–9
        for (char color : COLORS) {
            for (int i = 0; i <= 9; i++) {
                CARD_POINTS.put("" + color + i, i);
            }

            // Spezialkarten jeder Farbe (20 Punkte)
            CARD_POINTS.put(color + "+2", 20);
            CARD_POINTS.put(color + "<->", 20);
            CARD_POINTS.put(color + "x", 20);
        }

        // Schwarze Spezialkarten (50 Punkte)
        CARD_POINTS.put("+4", 50);
        CARD_POINTS.put("fw", 50);
    }

    /**
     * Berechnet die Punkte für alle Karten in einer Hand.
     */
    public int calculatePoints(List<Card> hand) {
        int total = 0;

        for (Card card : hand) {
            String name = card.getCardName();
            total += CARD_POINTS.getOrDefault(name, 0);
        }

        return total;
    }

    /**
     * Gibt Punkte an den Gewinner der Runde.
     * Alle Punkte der gegnerischen Hände werden zusammengezählt.
     * Gewinner erhält die Summe und wird in seinem Punktestand gespeichert.
     */
    public int awardPointsToWinner(List<Player> allPlayers, Player winner) {
        int total = 0;

        for (Player p : allPlayers) {
            if (!p.equals(winner)) {
                total += calculatePoints(p.getCardsInHand());
            }
        }

        // Punkte zum Gewinner hinzufügen
        winner.addPoints(total);
        return total;
    }

    /**
     * Gibt die Rangliste der Spieler nach Punkten aus.
     */
    public void printRanking(List<Player> allPlayers) {
        List<Player> sorted = new ArrayList<>(allPlayers);
        sorted.sort((a, b) -> Integer.compare(b.getPoints(), a.getPoints()));

        System.out.println("\n--- Player Ranking ---");
        int rank = 1;
        for (Player p : sorted) {
            System.out.println(rank + ". " + p.getName() + " - " + p.getPoints() + " points");
            rank++;
        }
        System.out.println("----------------------\n");
    }

    /**
     * Prüft, ob ein Spieler 500 oder mehr Punkte hat.
     * Gibt diesen Spieler zurück oder null, wenn niemand gewonnen hat.
     */
    public Player checkForGameWinner(List<Player> allPlayers) {
        for (Player p : allPlayers) {
            if (p.getPoints() >= 500) {
                return p;
            }
        }
        return null;
    }
}

