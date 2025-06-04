import org.junit.Assert;
import org.junit.Test;


import java.util.ArrayList;


public class cardDeckTester {


    ArrayList<Card> cards = new ArrayList<>();


    @Test
    public void createDeckTest() {
        CardsDeck cardsDeck = new CardsDeck(cards);


        cardsDeck.createCardDeck();
        String cardName = cardsDeck.getCardsDeck().get(cards.size()-1).getCardName();
        String result = "fw";


        Assert.assertEquals(result, cardName);
    }
}
