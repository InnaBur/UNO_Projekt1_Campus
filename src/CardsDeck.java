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

    public ArrayList<Card> getCardsDeck() {
        return cardsDeck;
    }

    //верхня карта з колоди
    public Card getTopCard() {
        return cardsDeck.get(0);
    }

    public void deleteCard(Card karte) {
        cardsDeck.remove(karte);
    }

    //верхня карта з колоди як її бачить гравець
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
        createSpecialBlackCards("fw");
    }

    //4 Karten +4 und 4 karten Farbwechsel werden erstellt
    public void createSpecialBlackCards(String cardName) {
        for (int i = 0; i < 4; i++) {
            cardsDeck.add(new Card(cardName, true));
        }
    }

    public void createColoredCards(char color) {

        for (int i = 0; i < 9; i++) {
            Card card = new Card("" + color + (i + 1), false);
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
