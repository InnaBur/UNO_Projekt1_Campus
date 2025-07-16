import java.util.ArrayList;
import java.util.HashMap;


/**
 * Die Klasse ScoreCalculator berechnet die Punktestände in einem UNO-Spiel.
 * Sie weist jeder Karte einen Punktewert zu und verwaltet die Rundensiegerwertung
 * sowie die Ermittlung des Gesamtsiegers.
 */
public class ScoreCalculator {
    /**
     * HashMap zur Speicherung der Punktewerte für jede Karte.
     */
    // Punktewerte für alle Karten – als Instanzvariable
    private final HashMap<String, Integer> cardPoints;


    /**
     * Konstruktor – Initialisiert die Punktewerte aller Karten im Spiel.
     * Farbige Zahlenkarten erhalten ihren Zahlenwert,
     * Aktionskarten 20 Punkte, und schwarze Spezialkarten 50 Punkte.
     */
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


    /**
     * Berechnet die Gesamtpunktzahl der übergebenen Kartenhand.
     *
     * @param hand Liste der Karten, z.B. aus der Hand eines Spielers.
     * @return Gesamtpunktzahl gemäß der definierten Punktewerte.
     */
    // Berechnet die Gesamtpunktzahl für eine Handkartenliste
    public int calculatePoints(ArrayList<Card> hand) {
        int total = 0;
        for (Card card : hand) {
            String name = card.getCardName();
            total += cardPoints.get(name); // setzt voraus, dass Karte immer vorhanden ist
        }
        return total;
    }

    /**
     * Verleiht dem Gewinner einer Runde die Punkte aller gegnerischen Handkarten.
     *
     * @param allPlayers Liste aller Mitspieler.
     * @param winner Der Spieler, der die Runde gewonnen hat.
     * @return Gesamtpunktzahl, die dem Gewinner gutgeschrieben wurde.
     */
    // Verleiht dem Gewinner einer Runde die Punkte aller gegnerischen Handkarten
    public int awardPointsToWinner(ArrayList<Player> allPlayers, Player winner) {
        int total = 0;
        for (Player p : allPlayers) {
            if (!p.equals(winner)) {
                total += calculatePoints(p.getCardsInHand());
            }
        }
        winner.addPoints(total);
        return total;
    }

    /**
     * Gibt die aktuelle Rangliste der Spieler auf der Konsole aus,
     * sortiert nach Punkten absteigend.
     *
     * @param allPlayers Liste aller Spieler.
     */
    // Gibt die aktuelle Rangliste der Spieler aus (sortiert nach Punkten absteigend)
    public void printRanking(ArrayList<Player> allPlayers) {
        // Liste kopieren, um sie zu sortieren
        ArrayList<Player> sorted = new ArrayList<>(allPlayers);
        // Einfache Bubble-Sortierung (absteigend nach Punkten)
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

    /**
     * Prüft, ob ein Spieler das Spiel mit mindestens 500 Punkten gewonnen hat.
     *
     * @param allPlayers Liste aller Spieler.
     * @return Der Gewinner mit >= 500 Punkten oder {@code null}, falls noch niemand gewonnen hat.
     */
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
