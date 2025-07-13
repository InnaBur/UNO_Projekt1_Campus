import java.util.ArrayList;

/**
 * Die Klasse Player repräsentiert einen Spieler im UNO-Spiel.
 * Ein Spieler kann ein Mensch oder ein Bot sein und hat Karten auf der Hand sowie Punktestand.
 */

public class Player {

    private String name;
    /** Gibt an, ob der Spieler ein Bot ist. */
    private boolean isBot;
    /** Aktueller Punktestand des Spielers. */
    private int points;

    /** Aktueller Punktestand des Spielers. */
    private ArrayList<Card> cardsInHand;

    /**
     * Konstruktor: Erstellt einen neuen Spieler mit Namen und Bot-Status.
     * Name wird automatisch in Großbuchstaben gespeichert.
     *
     * @param name  Spielername.
     * @param isBot true, wenn Spieler ein Bot ist.
     */
    public Player(String name, boolean isBot) {
        this.name = name.toUpperCase(); // Spielername wird beim Erstellen großgeschrieben
        this.isBot = isBot;
        this.cardsInHand = new ArrayList<>();
        this.points = 0;
    }

    /**
     * Leerer Konstruktor (z.B. für Tests oder spezielle Initialisierungen).
     */
    public Player() {
    }

    /**
     * Gibt den Namen des Spielers zurück.
     *
     * @return Spielername.
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen des Spielers (wird in Großbuchstaben gespeichert).
     *
     * @param name Neuer Spielername.
     */
    public void setName(String name) {
        this.name = name.toUpperCase(); // Spielername wird beim Erstellen großgeschrieben
    }

    /**
     * Gibt zurück, ob es sich um einen Bot handelt.
     *
     * @return true, wenn Bot.
     */
    public boolean isBot() {
        return isBot;
    }


    /**
     * Gibt alle Karten auf der Hand des Spielers zurück.
     *
     * @return Liste der Handkarten.
     */
    public ArrayList<Card> getCardsInHand() {
        return cardsInHand;
    }

    /**
     * Fügt dem Spieler eine Karte hinzu.
     *
     * @param card Die Karte, die hinzugefügt wird.
     * @return Die hinzugefügte Karte.
     */
    public Card addCard(Card card) {
        cardsInHand.add(card);
        return card;
    }

    /**
     * Fügt mehrere Karten gleichzeitig zur Hand hinzu.
     *
     * @param cards Liste der hinzuzufügenden Karten.
     */
    public void addAllCards(ArrayList<Card> cards) {
        cardsInHand.addAll(cards);
    }

    /**
     * Zeigt die aktuelle Hand des Spielers farbig in der Konsole an.
     */
    public void showHand() {
        // System.out.print(name + ", Your cards are: ");
        // Schwarzer Text (30), Cyan Hintergrund (46)
        System.out.print("\n\u001B[30;46m[" + name + "]\u001B[0m, your cards are: ");
        for (Card x : cardsInHand) {
            String colorCardOutput = CardsDeck.createColoredOutputForCard(x.getCardName());  // Farbige Darstellung
            System.out.print("[" + colorCardOutput + "] ");
        }
        System.out.println("\u001B[0m\n"); // Reset am Ende, weil letzte Karte in Farbe ist
    }

    /**
     * Gibt eine textuelle Darstellung des Spielers zurück (für Debug oder Logs).
     *
     * @return Spielername und Bot-Status.
     */
    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", isBot=" + isBot +
                '}';
    }

    /**
     * Sucht eine Karte in der Hand anhand des Kartennamens.
     *
     * @param name Kartenname (z.B. "R5").
     * @return Gefundene Karte oder null.
     */
    public Card getCardByName(String name) {
        for (Card c : cardsInHand) {
            if (c.getCardName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Entfernt eine bestimmte Karte aus der Hand.
     *
     * @param card Die zu entfernende Karte.
     */
    public void removeCard(Card card) {
        cardsInHand.remove(card);
    }

    /**
     * Gibt den aktuellen Punktestand des Spielers zurück.
     * @return Punkte.
     */
    // Punktesystem:
    public int getPoints() {
        return points;
    }

    /**
     * Setzt den Punktestand neu (z.B. für Test oder Neustart).
     * @param points Neue Punkte.
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Addiert Punkte zum bisherigen Punktestand (z.B. nach Spielrunde).
     * Wenn deine Spielschleife die Player-Objekte nicht in jeder Runde neu erstellt,
     * sondern dieselben Player-Instanzen weiterverwendet,
     * dann bleiben die hinzugefügten Punkte beim Spieler erhalten.
     *
     * @param p Punkte, die addiert werden.
     */

    public void addPoints(int p) {
        this.points += p;
    }

}
