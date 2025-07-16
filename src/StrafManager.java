import java.util.Deque;

/**
 * Die Klasse StrafManager verwaltet Strafaktionen im Spiel,
 * z.B. Kartenstrafen bei Regelverstößen oder Bluffversuchen.
 */

public class StrafManager {

    /**
     * Gibt dem aktuellen Spieler 4 Strafkarten.
     *
     * @param cardsDeck    Das Kartenstapel-Objekt.
     * @param currentPlayer Der Spieler, der bestraft wird.
     * @param discardPile  Der Ablagestapel, falls neu gemischt werden muss.
     */
    public static void fourCardsToCurrentPlayer(CardsDeck cardsDeck, Player currentPlayer, Deque<Card> discardPile) {
        currentPlayer.addAllCards(cardsDeck.getNTopCardAndRemoveFromCardDeck(4, discardPile));
        PrintManager.fourCardsMessage(currentPlayer.getName());
    }


    /**
     * Gibt dem aktuellen Spieler 6 Strafkarten (z.B. nach falschem Bluff-Vorwurf).
     *
     * @param cardsDeck     Das Kartenstapel-Objekt.
     * @param currentPlayer Der Spieler, der bestraft wird.
     * @param discardPile   Der Ablagestapel, falls neu gemischt werden muss.
     */
    public static void sixCardsToCurrentPlayer(CardsDeck cardsDeck, Player currentPlayer, Deque<Card> discardPile) {
        System.out.println("Bluff not confirmed");
        currentPlayer.addAllCards(cardsDeck.getNTopCardAndRemoveFromCardDeck(6, discardPile));
        PrintManager.sixCardsMessage(currentPlayer.getName());
    }

    /**
     * Gibt dem vorherigen Spieler 4 Strafkarten (z.B. bei aufgedecktem Bluff).
     *
     * @param cardsDeck    Das Kartenstapel-Objekt.
     * @param prevPlayer   Der vorherige Spieler, der bestraft wird.
     * @param discardPile  Der Ablagestapel, falls neu gemischt werden muss.
     */
    public static void fourCardsToPrevPlayer(CardsDeck cardsDeck, Player prevPlayer, Deque<Card> discardPile) {
        System.out.println("Bluff confirmed! Player " + prevPlayer.getName() + " bluffed!");
        prevPlayer.addAllCards(cardsDeck.getNTopCardAndRemoveFromCardDeck(4, discardPile));
        PrintManager.fourCardsMessage(prevPlayer.getName());
    }

    /**
     * Gibt dem aktuellen Spieler 1 Strafkarte (z.B. bei vergessenem UNO).
     *
     * @param cardsDeck     Das Kartenstapel-Objekt.
     * @param currentPlayer Der Spieler, der bestraft wird.
     * @param discardPile   Der Ablagestapel.
     */
    public static void drawOneCardPenalty(CardsDeck cardsDeck, Player currentPlayer, Deque<Card> discardPile) {
        currentPlayer.addCard(cardsDeck.getTopCardAndRemoveFromList(discardPile));
    }

    /**
     * Gibt dem aktuellen Spieler 2 Strafkarten (z.B. bei Regelverstoß).
     *
     * @param cardsDeck     Das Kartenstapel-Objekt.
     * @param currentPlayer Der Spieler, der bestraft wird.
     * @param discardPile   Der Ablagestapel.
     */
    public static void drawTwoCardsPenalty(CardsDeck cardsDeck, Player currentPlayer, Deque<Card> discardPile) {
        currentPlayer.addCard(cardsDeck.getTopCardAndRemoveFromList(discardPile));
        currentPlayer.addCard(cardsDeck.getTopCardAndRemoveFromList(discardPile));
    }

    /**
     * Meldet ungültige Eingabe und gibt dem Spieler 2 Strafkarten.
     *
     * @param cardsDeck     Das Kartenstapel-Objekt.
     * @param currentPlayer Der Spieler, der bestraft wird.
     * @param discardPile   Der Ablagestapel.
     */
    public static void invalidCardFromUserAndPenalty(CardsDeck cardsDeck, Player currentPlayer, Deque<Card> discardPile) {
        PrintManager.printInvalidInput(" or card cannot be played. You receive 2 penalty cards.");
       drawTwoCardsPenalty(cardsDeck, currentPlayer, discardPile);
    }

    /**
     * Gibt dem nächsten Spieler 2 Strafkarten (z.B. durch +2-Karte).
     *
     * @param cardsDeck   Das Kartenstapel-Objekt.
     * @param next        Der nächste Spieler, der bestraft wird.
     * @param discardPile Der Ablagestapel.
     */
    public static void twoCardsToNextPlayer(CardsDeck cardsDeck, Player next, Deque<Card> discardPile) {
        next.addAllCards(cardsDeck.getNTopCardAndRemoveFromCardDeck(2, discardPile));
        PrintManager.twoCardsMessage(next.getName());
    }

}
