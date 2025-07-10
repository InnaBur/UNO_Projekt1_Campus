import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;

public class CardsDeck {

    static final int NUMBER_OF_CARDS_IN_HAND = 2;
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

    // methode für Farbanzeige: ANSI-Farbcodes (ANSI Escape Codes) sind spezielle Zeichenfolgen,
    // mit denen du Text in der Konsole/Terminal einfärben oder formatieren kannst.
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

    public ArrayList<Card> getCardsDeck() {
        return cardsDeck;
    }

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

    //top card from the cards deck be added into draw pill or into players hand
    // and be removed from the card deck
    public Card getTopCardAndRemoveFromList(Deque<Card> discardPile) {

        if (cardsDeck.isEmpty()) {
            reshuffleDiscardPileIntoDrawPile(discardPile);
        }
        Card top = cardsDeck.get(0);
        cardsDeck.remove(cardsDeck.get(0));
        return top;
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
        for (int i = 0; i < 40; i++) {
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


    //Diese Methode ist nur für zwischen Testung. Muss gelöscht werden
    public void printCardDeck() {
        int count = 0;
        for (Card card : cardsDeck) {
            System.out.println(card);
            count++;
        }
        System.out.println(count);
    }

    //Help method for tests
    public void printDequeCardDeck(Deque<Card> list) {
        int count = 0;
        for (Card card : list) {
            System.out.println(card);
            count++;
        }
        System.out.println(count);
    }

    //neu shuffle cardDeck, if discard is empty
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
