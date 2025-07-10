import java.util.Deque;

public class StrafManager {

    public static void fourCardsToCurrentPlayer(CardsDeck cardsDeck, Player currentPlayer, Deque<Card> discardPile) {
        currentPlayer.addAllCards(cardsDeck.getNTopCardAndRemoveFromCardDeck(4, discardPile));
        PrintManager.fourCardsMessage(currentPlayer.getName());
    }

    public static void sixCardsToCurrentPlayer(CardsDeck cardsDeck, Player currentPlayer, Deque<Card> discardPile) {
        System.out.println("Bluff not confirmed");
        currentPlayer.addAllCards(cardsDeck.getNTopCardAndRemoveFromCardDeck(6, discardPile));
        PrintManager.sixCardsMessage(currentPlayer.getName());
    }

    public static void fourCardsToPrevPlayer(CardsDeck cardsDeck, Player prevPlayer, Deque<Card> discardPile) {
        System.out.println("Bluff confirmed! Player " + prevPlayer.getName() + " bluffed!");
        prevPlayer.addAllCards(cardsDeck.getNTopCardAndRemoveFromCardDeck(4, discardPile));
        PrintManager.fourCardsMessage(prevPlayer.getName());
    }

    public static void drawOneCardPenalty(CardsDeck cardsDeck, Player currentPlayer, Deque<Card> discardPile) {
        currentPlayer.addCard(cardsDeck.getTopCardAndRemoveFromList(discardPile));


    }

}
