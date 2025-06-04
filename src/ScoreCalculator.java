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
            cardPoints.put(color + "<->", 20);
            cardPoints.put(color + "x", 20);
        }

        // Schwarze Spezialkarten (jeweils 50 Punkte)
        cardPoints.put("+4", 50);
        cardPoints.put("fw", 50);
    }

    // Berechnet die Gesamtpunktzahl für eine Handkartenliste
    public int calculatePoints(List<Card> hand) {
        int total = 0;

        for (Card card : hand) {
            String name = card.getCardName();
            total += cardPoints.get(name); // setzt voraus, dass Karte immer vorhanden ist
        }

        return total;
    }

    // Verleiht dem Gewinner einer Runde die Punkte aller gegnerischen Handkarten
    public int awardPointsToWinner(List<Player> allPlayers, Player winner) {
        int total = 0;

        for (Player p : allPlayers) {
            if (!p.equals(winner)) {
                total += calculatePoints(p.getCardsInHand());
            }
        }

        winner.addPoints(total);
        return total;
    }

    // Gibt die aktuelle Rangliste der Spieler aus (sortiert nach Punkten absteigend)
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

    // Prüft, ob ein Spieler 500 oder mehr Punkte erreicht hat
    public Player checkForGameWinner(List<Player> allPlayers) {
        for (Player p : allPlayers) {
            if (p.getPoints() >= 500) {
                return p;
            }
        }
        return null;
    }
}

