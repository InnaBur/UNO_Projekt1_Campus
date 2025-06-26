import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;


public class cardDeckTester {


    ArrayList<Card> cards = new ArrayList<>();
    CardsDeck cardsDeck;

    @Before
    public void setCardsDeck() {
        cardsDeck = new CardsDeck(cards);
    }


    @Test
    public void createDeckTest() {
        cardsDeck.createCardDeck();
        String cardName = cardsDeck.getCardsDeck().get(cards.size() - 1).getCardName();
        String result = "cc";

        // Case-insensitive Vergleich
        Assert.assertEquals(result.toLowerCase(), cardName.toLowerCase());
    }

    @Test
    public void cardsTest() {


        cardsDeck.createCardDeck();
        ArrayList<Card> cards = cardsDeck.getCardsDeck();
        int count = 0;

        for (Card card : cards) {
            if (card.getCardName().equals("R3")) {
                count++;
            }
        }
        int result = 2; //2 Karten jede Fahrbe und Anzahl

        Assert.assertEquals(result, count);
    }
}
