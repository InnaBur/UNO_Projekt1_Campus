import java.util.ArrayList;
import java.util.Collections;

public class CardsDeck {

    static final int NUMBER_OF_CARDS_IN_HAND = 7;
    //array für Farben-Buchstaben
    static final char[] COLORS = {'R', 'B', 'Y', 'G'};
    final int NUMBER_OF_PLAYERS = 4;
    //Kartendeck list
    private ArrayList<Card> cardsDeck;

    public CardsDeck() {
        this.cardsDeck = new ArrayList<>();
        createCardDeck();
        kartenDeckMischen();
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
        spezialeSchwarzeKartenErstellen("+4");
        spezialeSchwarzeKartenErstellen("fw");
    }

    //4 Karten +4 und 4 karten Farbwechsel werden erstellt
    public void spezialeSchwarzeKartenErstellen(String kartenName) {
        for (int i = 0; i < 4; i++) {
            cardsDeck.add(new Card(kartenName, true));
        }
    }

    public void createColoredCards(char color) {

        for (int i = 0; i < 9; i++) {
            Card karte = new Card("" + color + (i + 1), false);
            cardsDeck.add(karte);
        }
        cardsDeck.add(new Card(color + "+2", true));  //+2 karten ziehen
        cardsDeck.add(new Card(color + "<->", true));  //Reihenfolge wechseln
        cardsDeck.add(new Card(color + "x", true));  //einen Umzug überspringen
    }

    public void createZeroCard(char color) {
        cardsDeck.add(new Card("" + color + 0, false));
    }


    public void karteZiehen() {

    }

    public void kartenDeckMischen() {
        Collections.shuffle(cardsDeck);
    }

    public void kartenAusteilen(Player[] spielers) {

        for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
            for (int j = 0; j < NUMBER_OF_CARDS_IN_HAND; j++) {
                spielers[i].addKarte(cardsDeck.get(0));
                cardsDeck.remove(0);
            }
        }
    }

    public void spielersKartenZeigen(Player spieler) {
        System.out.print(spieler.getName() + ", Sie haben diese Karten: ");
        for (Card karte: spieler.getKartenInDerHand()) {
            System.out.print(karte.getCardName() + " ");
        }
        System.out.println("\n");
    }


    //Diese Methode ist nur für zwischen Testung. Muss gelöscht werden
    public void printKartendeck() {
        int count = 0;
        for (Card karte : cardsDeck) {
            System.out.println(karte);
            count++;
        }
        System.out.println(count);
    }

}
