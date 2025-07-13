import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;

/**
 * Die Klasse CardsDeck verwaltet das UNO-Kartendeck:
 * Es enthält Methoden zum Erstellen, Mischen, Ziehen und Austeilen von Karten.
 */

public class CardsDeck {
    /**
     * Anzahl der Karten, die jeder Spieler am Anfang bekommt.
     */
    static final int NUMBER_OF_CARDS_IN_HAND = 7;

    /**
     * array für Farben-Buchstaben
     * Verfügbare Farben für normale Karten: R = Rot, B = Blau, Y = Gelb, G = Grün.
     */
    static final char[] COLORS = {'R', 'B', 'Y', 'G'};

    /**
     * Anzahl der Spieler im Spiel.
     */
    static final int NUMBER_OF_PLAYERS = 4;

    /**
     * Das aktuelle Kartendeck.
     */
    private ArrayList<Card> cardsDeck;

    /**
     * Konstruktor
     * Erstellt ein neues, vollständiges und gemischtes UNO-Kartendeck.
     */

    public CardsDeck() {
        this.cardsDeck = new ArrayList<>();
        createCardDeck();
        shuffleCardDeck();
    }

    /**
     * Konstruktor
     * Alternativ: Erzeugt ein Kartendeck aus einer bestehenden Liste von Karten (z. B. für Tests).
     *
     * @param cardsDeck Eine vorbereitete Kartenliste.
     */

    public CardsDeck(ArrayList<Card> cardsDeck) {
        this.cardsDeck = cardsDeck;
    }

    /**
     * Gibt den farbig formatierten Namen einer Karte in der Konsolenausgabe zurück.
     * Nutzt ANSI-Escape-Sequenzen, um Karten farblich darzustellen.
     *
     * @param cardName Name der Karte ("R5", "G+2", "+4")
     * @return Farbige Zeichenfolge in der Konsole.
     */
    public static String createColoredOutputForCard(String cardName) {
        String colorCode;
        String upperCardName = cardName.toUpperCase(); // absichern für Testung
        // Kartenname beginnt mit R, G, B, Y (Farben) oder ist schwarz (+4, CC)
        if (upperCardName.startsWith("R")) {
            colorCode = "\u001B[30;41m"; // Schwarzer Text auf rotem Hintergrund
        } else if (upperCardName.startsWith("G")) {
            colorCode = "\u001B[30;42m"; // Schwarzer Text auf grünem Hintergrund
        } else if (upperCardName.startsWith("B")) {
            colorCode = "\u001B[30;44m"; // Schwarzer Text auf blauem Hintergrund
        } else if (upperCardName.startsWith("Y")) {
            colorCode = "\u001B[30;43m"; // Schwarzer Text auf gelbem Hintergrund
        } else {
            colorCode = "\u001B[30;45m"; // Schwarzer Text auf magentafarbenem Hintergrund (für +4, CC)
        }
        return colorCode + upperCardName + "\u001B[0m"; // Reset-Farbe
    }

    /**
     * Getter:
     * Gibt das aktuelle Karten-Deck zurück.
     * @return Liste aller verfügbaren Karten im Deck.
     */
    public ArrayList<Card> getCardsDeck() {
        return cardsDeck;
    }

    /**
     * Gibt die obersten {@code count} Karten vom Deck und entfernt sie.
     * Wenn das Deck leer ist, wird der Ablagestapel neu gemischt.
     * @param count        Anzahl der Karten, die gezogen werden sollen.
     * @param discardPile  Der Ablagestapel (für Re-Shuffle bei leerem Deck).
     * @return Liste der gezogenen Karten.
     */
    public ArrayList<Card> getNTopCardAndRemoveFromCardDeck(int count, Deque<Card> discardPile) {
        ArrayList<Card> drawnCards = new ArrayList<>();
        while (drawnCards.size() < count) {
            if (cardsDeck.isEmpty()) {
                reshuffleDiscardPileIntoDrawPile(discardPile);
            }
            if (cardsDeck.isEmpty()) {
                System.out.println("Not enough Cards! Only " + drawnCards.size() + " cards was drawn.");
                break;
            }
            drawnCards.add(cardsDeck.remove(0));
        }
        return drawnCards;
    }

    /**
     * Gibt die oberste Karte vom Deck zurück und entfernt sie.
     * Mischt bei Bedarf den Ablagestapel neu ein.
     *
     * @param discardPile Der Ablagestapel.
     * @return Die gezogene Karte.
     */

    public Card getTopCardAndRemoveFromList(Deque<Card> discardPile) {

        if (cardsDeck.isEmpty()) {
            reshuffleDiscardPileIntoDrawPile(discardPile);
        }
        Card top = cardsDeck.get(0);
        cardsDeck.remove(cardsDeck.get(0));
        return top;
    }


    /**
     * Erstellt das vollständige UNO-Deck mit farbigen und schwarzen Spezialkarten.
     */
    public void createCardDeck() {
        // Hier werden die Karten von 1 bis 9 und drei Sondere Karten jede Farbe 2 Mal erstellt
            for (char color : COLORS) {
            createZeroCard(color);   // Karte '0' jede Farbe wird nur ein Mal erstellen
            for (int i = 0; i < 2; i++) {
                createColoredCards(color);
            }
        }
        //4+ Karten und 4 karten Farbwechsel werden erstellt
       createSpecialBlackCards("+4");
        createSpecialBlackCards("CC");
    }

    /**
     * Fügt vier schwarze Spezialkarten (z.B. "+4" oder "CC") ins Deck ein.
     *
     * @param cardName Kartenname.
     */
    //4 Karten +4 und 4 karten Farbwechsel werden erstellt
        public void createSpecialBlackCards(String cardName) {
        for (int i = 0; i < 4; i++) {
            cardsDeck.add(new Card(cardName, true));
        }
    }

    /**
     * Erstellt farbige Karten (1–9, +2, D, X) für eine bestimmte Farbe.
     * @param color Farbzeichen ('R', 'G', 'B', 'Y')
     */
    public void createColoredCards(char color) {

        for (int i = 0; i < 9; i++) {
            Card card = new Card("" + color + (i + 1), false); // weil Karte 0 schon erstellt ist
            cardsDeck.add(card);
        }
        cardsDeck.add(new Card(color + "+2", true));  //+2 karten ziehen
        cardsDeck.add(new Card(color + "D", true));  //Reihenfolge wechseln
        cardsDeck.add(new Card(color + "X", true));  //einen Umzug überspringen
    }


    /**
     * Fügt eine farbige Nullkarte dem Deck hinzu.
     * @param color Farbzeichen ('R', 'G', 'B', 'Y')
     */
    public void createZeroCard(char color) {
        cardsDeck.add(new Card("" + color + 0, false));
    }

    /**
     * Mischt das Kartendeck zufällig.
     */
    public void shuffleCardDeck() {
        Collections.shuffle(cardsDeck);
    }


    /**
     * Gibt jedem Spieler eine bestimmte Anzahl an Startkarten.
     * @param players Liste der Spieler.
     */
    public void dealCards(ArrayList<Player> players) {

        for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
            for (int j = 0; j < NUMBER_OF_CARDS_IN_HAND; j++) {
                players.get(i).addCard(cardsDeck.get(0));
                cardsDeck.remove(0);
            }
        }
    }


    /**
     * Testmethode: Gibt alle Karten des aktuellen Decks aus.
     * Sollte im finalen Spiel entfernt werden.
     */
    public void printCardDeck() {
        int count = 0;
        for (Card card : cardsDeck) {
            System.out.println(card);
            count++;
        }
        System.out.println(count);
    }

    /**
     * Testmethode: Gibt alle Karten eines Deque-Stapels aus (z.B. Ablagestapel).
     * @param list Stapel von Karten.
     */
    public void printDequeCardDeck(Deque<Card> list) {
        int count = 0;
        for (Card card : list) {
            System.out.println(card);
            count++;
        }
        System.out.println(count);
    }

    /**
     * Mischt den Ablagestapel zurück ins Kartendeck, wenn das Deck leer ist.
     * Die oberste Karte des Ablagestapels wird dabei nicht gemischt und bleibt unverändert oben liegen.
     * @param discardPile Der Ablagestapel mit bereits gespielten Karten.
     */
    void reshuffleDiscardPileIntoDrawPile(Deque<Card> discardPile) {

        Card temp = discardPile.pop();
        for (Card card : discardPile) {
            if (card.getCardName().endsWith("CC")) {
                card.setCardName("CC");
            } else if (card.getCardName().endsWith("+4")) {
                card.setCardName("+4");
            }
            cardsDeck.add(card);
            discardPile.pop();
        }
        Collections.shuffle(cardsDeck);
        discardPile.add(temp);
    }
}
