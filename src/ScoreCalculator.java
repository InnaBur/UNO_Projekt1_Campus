import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScoreCalculator {

    // Punktewerte für alle Karten – als Instanzvariable
    private final HashMap<String, Integer> cardPoints;

    // Konstruktor – initialisiert die Punktetabelle für alle Karten
    public ScoreCalculator() {
        cardPoints = new HashMap<>();
        char[] COLORS = {'R', 'G', 'B', 'Y'};

        // Nummern-Karten 0–9 pro Farbe (Wert = Zahl)
        for (char color : COLORS) {
            for (int i = 0; i <= 9; i++) {
                cardPoints.put("" + color + i, i);
            }

            // Farbige Spezialkarten (jeweils 20 Punkte)
            cardPoints.put(color + "+2", 20);
            cardPoints.put(color + "D", 20);
            cardPoints.put(color + "X", 20);
        }

        // Spezialkarten (jeweils 50 Punkte)
        cardPoints.put("+4", 50);
        cardPoints.put("CC", 50);
    }

    // Berechnet die Gesamtpunktzahl für eine Handkartenliste
    public int calculatePoints(ArrayList<Card> hand) {
        int total = 0;

        for (Card card : hand) {
            String name = card.getCardName();
            total += cardPoints.get(name); // setzt voraus, dass Karte immer vorhanden ist
        }

        return total;
    }

    // Verleiht dem Gewinner einer Runde die Punkte aller gegnerischen Handkarten
    public int awardPointsToWinner(ArrayList<Player> allPlayers, Player winner) {
        int total = 0;

        for (Player p : allPlayers) {
            if (!p.equals(winner)) {
                total += calculatePoints(p.getCardsInHand());
                System.out.println("Player " + p.getName() + "'s hand:"); // TESTING

                for (Card c : p.getCardsInHand()) {
                    System.out.println("  - " + c.getCardName());
                }
            }
        }

        winner.addPoints(total);
        return total;
    }

    // Gibt die aktuelle Rangliste der Spieler aus (sortiert nach Punkten absteigend)
    public void printRanking(ArrayList<Player> allPlayers) {
        // Copy list to sort
        ArrayList<Player> sorted = new ArrayList<>(allPlayers);

        // Simple bubble sort (descending by points)
        for (int i = 0; i < sorted.size() - 1; i++) {
            for (int j = 0; j < sorted.size() - 1 - i; j++) {
                if (sorted.get(j).getPoints() < sorted.get(j + 1).getPoints()) {
                    // Swap
                    Player temp = sorted.get(j);
                    sorted.set(j, sorted.get(j + 1));
                    sorted.set(j + 1, temp);
                }
            }
        }

        System.out.println("\n--- Player Ranking ---");
        int rank = 1;
        for (Player p : sorted) {
            System.out.println(rank + ". " + p.getName() + " - " + p.getPoints() + " points.");
            rank++;
        }
        System.out.println("----------------------\n");
    }

    // Prüft, ob ein Spieler 500 oder mehr Punkte erreicht hat
    public Player checkForGameWinner(ArrayList<Player> allPlayers) {
        for (Player p : allPlayers) {
            if (p.getPoints() >= 500) {
                return p;
            }
        }
        return null;
    }

}
