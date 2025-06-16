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
    public Card getTopCardAndRemoveFromList() {
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
        cardsDeck.add(new Card(color + "D", true));  //Reihenfolge wechseln
        cardsDeck.add(new Card(color + "X", true));  //einen Umzug überspringen
    }

    public void createZeroCard(char color) {
        cardsDeck.add(new Card("" + color + 0, false));
    }


    public void drawCard() {

    }

    public void shuffleCardDeck() {
        Collections.shuffle(cardsDeck);
    }

    public void dealCards(ArrayList<Player> players) {

        for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
            for (int j = 0; j < NUMBER_OF_CARDS_IN_HAND; j++) {
                players.get(i).addCard(cardsDeck.get(0));
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

    public void clearDeck() {
        cardsDeck.clear();
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

