import java.util.ArrayList;
import java.util.Collections;

public class CardsDeck {

    static final int NUMBER_OF_CARDS_IN_HAND = 7;
    //array für Farben-Buchstaben
    static final char[] COLORS = {'R', 'B', 'Y', 'G'};
    static final int NUMBER_OF_PLAYERS = 4;
    //Kartendeck list
    private ArrayList<Card> cardsDeck;


    public CardsDeck() {
        this.cardsDeck = new ArrayList<>();
        createCardDeck();
        shuffleCardDeck();
    }

    public CardsDeck(ArrayList<Card> cardsDeck) {
        this.cardsDeck = cardsDeck;
    }

    public ArrayList<Card> getCardsDeck() {
        return cardsDeck;
    }

    //top card from the cards deck be added into draw pill or into players hand
    // and be removed from the card deck
    public Card getTopCard() {
        if (cardsDeck.isEmpty()) {
            throw new IllegalStateException("Deck is empty, no cards to draw!");
        }

        Card top = cardsDeck.get(0);
        cardsDeck.remove(cardsDeck.get(0));
        return top;
    }



    public void deleteCard(Card karte) {
        cardsDeck.remove(karte);
    }

    //die oberste Karte vom Stapel, wie sie der Spieler sieht
    public String showTopCard() {
        return cardsDeck.get(0).getCardName();
    }
    public void setCardsDeck(ArrayList<Card> cardsDeck) {
        this.cardsDeck = cardsDeck;
    }


    public void createCardDeck() {

        // Karten von 1 bis 9 und drei Sondere Karten jede Farbe zwei Mal erstellen wurden,
        // Karte '0' jede Farbe wird nur ein Mal erstellen
        for (char color : COLORS) {
            createZeroCard(color);
            for (int i = 0; i < 2; i++) {
                createColoredCards(color);
            }
        }

        //4+ Karten und 4 karten Farbwechsel werden erstellt
        createSpecialBlackCards("+4");
        createSpecialBlackCards("CC");
    }

    //4 Karten +4 und 4 karten Farbwechsel werden erstellt
    public void createSpecialBlackCards(String cardName) {
        for (int i = 0; i < 4; i++) {
            cardsDeck.add(new Card(cardName, true));
        }
    }

    public void createColoredCards(char color) {

        for (int i = 0; i < 9; i++) {
            Card card = new Card("" + color + (i + 1), false); // weil Karte 0 schon erstellt ist
            cardsDeck.add(card);
        }
        cardsDeck.add(new Card(color + "+2", true));  //+2 karten ziehen
        cardsDeck.add(new Card(color + "<->", true));  //Reihenfolge wechseln
        cardsDeck.add(new Card(color + "x", true));  //einen Umzug überspringen
    }

    public void createZeroCard(char color) {
        cardsDeck.add(new Card("" + color + 0, false));
    }


    public void drawCard() {

    }

    public void shuffleCardDeck() {
        Collections.shuffle(cardsDeck);
    }

    public void dealCards(Player[] spielers) {

        for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
            for (int j = 0; j < NUMBER_OF_CARDS_IN_HAND; j++) {
                spielers[i].addCard(cardsDeck.get(0));
                cardsDeck.remove(0);
            }
        }
    }

    public void showPlayerCards(Player player) {
        System.out.print(player.getName() + ", You have these cards: ");
        for (Card karte: player.getCardsInHand()) {
            System.out.print(karte.getCardName() + " ");
        }
        System.out.println("\n");
    }
// methode für Farbanzeige: ANSI-Farbcodes (ANSI Escape Codes) sind spezielle Zeichenfolgen, mit denen du Text in der Konsole/Terminal einfärben oder formatieren kannst.
    public static String getColoredCard(String cardName) {
        String colorCode;

        // Kartenname beginnt mit R, G, B, Y (Farben) oder ist schwarz (+4, fw)
        if (cardName.startsWith("R")) {
            colorCode = "\u001B[31m"; // Rot
        } else if (cardName.startsWith("G")) {
            colorCode = "\u001B[32m"; // Grün
        } else if (cardName.startsWith("B")) {
            colorCode = "\u001B[34m"; // Blau
        } else if (cardName.startsWith("Y")) {
            colorCode = "\u001B[33m"; // Gelb
        } else {
            colorCode = "\u001B[37m"; // Weiss für +4, fw
        }

        return colorCode + cardName  + "\u001B[0m"; // Reset am Ende der Farbe
    }




    //Diese Methode ist nur für zwischen Testung. Muss gelöscht werden
    public void printCardDeck() {
        int count = 0;
        for (Card card : cardsDeck) {
            System.out.println(card);
            count++;
        }
        System.out.println(count);
    }

}

/* HashMap einbauen, um den Punktewert jeder Karte zu definieren, sodass du später einfach die Punkte der in der Hand verbleibenden Karten berechnen kannst. Dafür zeige ich dir:

 HashMap<String, Integer> mit Kartenwerten erstellt.

 HashMap für Kartenwerte
Füge diese HashMap als private static final Variable zur Klasse CardsDeck hinzu:

private static final java.util.HashMap<String, Integer> CARD_POINTS = new java.util.HashMap<>();

static {
    // Nummernkarten:
    for (char color : COLORS) {
        for (int i = 0; i <= 9; i++) {
            CARD_POINTS.put("" + color + i, i);
        }
        for (int i = 1; i <= 9; i++) {
            CARD_POINTS.put("" + color + i, i); // Jede Nummer doppelt, aber HashMap überschreibt
        }

        // Spezialkarten mit 20 Punkten
        CARD_POINTS.put(color + "+2", 20);   // Zieh Zwei
        CARD_POINTS.put(color + "<->", 20);  // Retour
        CARD_POINTS.put(color + "x", 20);    // Aussetzen
    }

    // Schwarze Spezialkarten mit 50 Punkten
    CARD_POINTS.put("+4", 50);  // Zieh Vier Farbenwahl
    CARD_POINTS.put("fw", 50);  // Farbenwahl
}

 Methode zur Berechnung der verbleibenden Punkte

// Berechnet die Punkte aller Karten, die ein Spieler noch auf der Hand hat
public int calculatePoints(ArrayList<Card> hand) {
    int totalPoints = 0;

    for (Card card : hand) {
        String name = card.getCardName();
        totalPoints += CARD_POINTS.getOrDefault(name, 0);  // Falls Karte unbekannt ist, zähle 0
    }

    return totalPoints;
}


Beispielverwendung:
ein Spieler hat seine Kartenhand player.getCardsInHand():

int punkte = cardsDeck.calculatePoints(player.getCardsInHand());
System.out.println("Verbleibende Punkte von " + player.getName() + ": " + punkte);*/