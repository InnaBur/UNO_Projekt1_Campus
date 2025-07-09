import java.util.Deque;

public class StrafManager {

    public static void fourCardsToCurrentPlayer(CardsDeck cardsDeck, Player currentPlayer, Deque<Card> discardPile) {
        currentPlayer.addAllCards(cardsDeck.getNTopCardAndRemoveFromCardDeck(4, discardPile));
        PrintManager.fourCardsMessage(currentPlayer.getName());
    }


}
