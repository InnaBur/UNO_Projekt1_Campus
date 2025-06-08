import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Scanner;

public class GameController {

    private final PlayerManager playerManager = new PlayerManager();
    // menschliche Spieler
    private final Deque<Card> discardPile = new ArrayDeque<>();
    Scanner scanner = new Scanner(System.in);
    // aktueller Spieler
    private Player currentPlayer;
    //die Bedingung für das Beenden des Spiels.
    private boolean isExit = false;
    private ScoreCalculator scoreCalculator = new ScoreCalculator();


    public void run() {

        CardsDeck cardsDeck = new CardsDeck();

        prepareGame(cardsDeck);

        currentPlayer = playerManager.getCurrentPlayer();


        do {

            //Auswahl Menu
            System.out.println("------------------------------");

            assert discardPile.peek() != null;
            //System.out.println("The top card is [" + discardPile.peek().getCardName()+ "]");
            // Show top card in color
            String coloredTopCard = CardsDeck.createColoredOutputForCard(discardPile.peek().getCardName());
            System.out.println("The top card is [" + coloredTopCard + "]");
            //handleFirstCardEffect(playerManager,cardsDeck,discardPile); // TESTING
            currentPlayer.showHand();

            int choice = -1; //initialize
            do {
                System.out.println("\u001B[30;47m"); // Black text on light gray background
                System.out.println("""
                                    Make your choice:                 
                          ┌────────────┬────────────┬────────────┐             
                          │ [1] Draw   │ [2] Play   │ [3] Bluff  │            
                          │     a card │     a card │     check  │             
                          ├────────────┼────────────┼────────────┤            
                          │ [4] Play   │ [5] Suggest│ [6] Help   │             
                          │     & UNO  │     a move │     rules  │            
                          ├────────────┼────────────┼────────────┤             
                          │ [7] Pause  │ [8]        │ [0] Exit   │            
                          │            │            │     game   │             
                          └────────────┴────────────┴────────────┘             
                        """);
                System.out.print("\u001B[0m"); // Reset colors

                try {
                    choice = scanner.nextInt();
                } catch (Exception e) {
                    System.out.println("Invalid input. Try again.");
                    scanner.next(); // Eingabe verwerfen
                }
            } while (isChoiceInMenuCorrect(choice));

            switch (choice) {

                case 1:
                    String userInput;

                    //Kann ein Spieler keine
                    //passende Karte legen, so muss er eine Strafkarte vom verdeckten Stapel ziehen.
                    Card currentUsersCard = currentPlayer.addCard(cardsDeck.getTopCardAndRemoveFromList());
                    //  System.out.println("You drew: " + currentUsersCard);
                    // cardsDeck.getCardsDeck().remove(cardsDeck.getTopCardAndRemoveFromList()); // Muss hier nochmal rmeove sein?, wenn schon oben in methode karte entfernt wird????

                    System.out.println("Your new Card from the draw pile: " + currentUsersCard);

                    currentPlayer.showHand();

                    //Diese Karte kann Spieler
                    //sofort wieder ausspielen, sofern diese passt.
                    if (currentUsersCard.isPlayableOn(discardPile.peek())) {
                        do {
                            System.out.println("Do you want to PLAY this card? Press 'Y' for yes or 'N' for no");
                            userInput = scanner.next();
                        }
                        while (!userInput.equalsIgnoreCase("n") && !userInput.equalsIgnoreCase("y"));

                        //If the player wants to play a card, they place it from their hand onto the table;
                        // if not, the next player in turn becomes the currentPlayer.
                        if (userInput.equalsIgnoreCase("y")) {
                            discardPile.addFirst(currentUsersCard);
                            currentPlayer.removeCard(currentUsersCard);
                            handlePlayedCard(currentUsersCard, cardsDeck);
                        } else {
                            currentPlayer = playerManager.getNextPlayer();
                        }
                    } else {
                        System.out.println("This card cannot be played now.");
                        currentPlayer = playerManager.getNextPlayer();
                    }
                    break;

                case 2: {
                    System.out.println("Specify the card to play (e.g., r5, g+2, Bd, Gx):");
                    String inputCardName = scanner.next().toUpperCase();
                    Card selectedCard = currentPlayer.getCardByName(inputCardName);

                    // Check if selected card exists in player's hand and is playable on top of discard pile
                    if (selectedCard != null && selectedCard.isPlayableOn(discardPile.peek())) {
                        // Play the card
                        currentPlayer.removeCard(selectedCard);
                        discardPile.push(selectedCard);

                        // Check if player has emptied their hand → they win the round
                        if (currentPlayer.getCardsInHand().isEmpty()) {
                            System.out.println(currentPlayer.getName() + " has won the round!");

                            // Handle scoring and check if game ends
                            boolean isGameOver = handleRoundEnd(playerManager.getPlayerList(), currentPlayer, scoreCalculator);
                            if (isGameOver) {
                                // ask for new game????
                                //isExit = true;
                            } else {
                                System.out.println("Starting next round...");
                                startNewRound(playerManager, cardsDeck, discardPile);
                            }
                        } else {

                            handlePlayedCard(selectedCard, cardsDeck);

                        }
                    } else {
                        // Invalid or unplayable card is penalty
                        System.out.println("Invalid card or card cannot be played. You receive 2 penalty cards.");
                        currentPlayer.addCard(cardsDeck.getTopCardAndRemoveFromList());
                        currentPlayer.addCard(cardsDeck.getTopCardAndRemoveFromList());

                        // next player
                        currentPlayer = playerManager.getNextPlayer();
                    }
                }
                break;


                case 3:
                    System.out.println("Bluff check logic not implemented yet.");
                case 4:
                    System.out.println(currentPlayer.getName() + " said UNO!");
                case 5:
                    System.out.println("Suggestion logic not implemented.");
                case 6:
                    Instructions.printGameInstructions();
                    break;
                case 7:

                    break;
                case 8:
                    //pause
                    //saveYoDatenbank;
                case 0:
                    System.out.println("Game is over!");
                    //saveYoDatenbank;
                    isExit = true;
            }

        } while (!isExit);
    }

    private boolean isChoiceInMenuCorrect(int choice) {
        return choice < 0 || choice > 8;
    }

    /*In this method, the number of human players and bots is determined,
    names are set, cards are dealt and the order of players,
    the top card of the discard deck is set
    */
    private void prepareGame(CardsDeck cardsDeck) {
        playerManager.preparePlayers();
        playerManager.printPlayerOrder();

        cardsDeck.dealCards(playerManager.getPlayerList());
        discardPile.addFirst(cardsDeck.getTopCardAndRemoveFromList());         //first card from the cards deck is a first card in drawPile

        // Apply its effect ONCE in game loop
        handleFirstCardEffect(playerManager, cardsDeck, discardPile);

        // Set the currentPlayer correctly after handling effect
        currentPlayer = playerManager.getCurrentPlayer();
    }

    public void handlePlayedCard(Card playedCard, CardsDeck cardsDeck) {
        String cardName = playedCard.getCardName();

        if (cardName.contains("D")) {
            // Spieler A spielt Richtungswechselkarte
            // Richtung ändern
            playerManager.switchDirection();



            System.out.println(playerManager.getCurrentPlayer().getName()+" made a \u001B[30;41mDirection change\u001B[0m to " + (playerManager.isClockwise() ? "Clockwise" : "Counter-clockwise"));
            // Spieler neuer Nachbar in der neuen Richtung ist dran
            currentPlayer = playerManager.getNextPlayer();
            playerManager.printPlayerOrder();

        } else if (cardName.contains("X")) {
            System.out.println("\u001B[30;46m[" + playerManager.getNextPlayer().getName() + "]\u001B[0m lost her/his turn: \u001B[30;45mSkipped!\u001B[0m");
            currentPlayer = playerManager.getNextPlayer();

        } else if (cardName.contains("+2")) {
            Player next = playerManager.getNextPlayer();
            next.addCard(cardsDeck.getTopCardAndRemoveFromList());
            next.addCard(cardsDeck.getTopCardAndRemoveFromList());
            System.out.println(next.getName() + " \u001B[30;41mDraws 2 cards!\u001B[0m");
            currentPlayer = playerManager.getNextPlayer();

        } else if (cardName.contains("+4")) {
            String newColor = askForColor(); // Returns "R", "G", "B", or "Y"

            // Update the card name to include the chosen color,
            playedCard.setCardName(newColor + "+4");


            //IF CHECK BLUFF implementieren
            Player next = playerManager.getNextPlayer();
            for (int i = 0; i < 4; i++) {
                next.addCard(cardsDeck.getTopCardAndRemoveFromList());
            }
            System.out.println(next.getName() + " \u001B[30;41mDraws 4 cards!\u001B[0m");
            currentPlayer = playerManager.getNextPlayer();

        }
        // When the player plays "CC", ask for the color and update the card name
        else if (cardName.equals("CC")) {
            // Let the player choose a color
            String newColor = askForColor(); // Returns "R", "G", "B", or "Y"

            // Update the card name to include the chosen color
            playedCard.setCardName(newColor + "CC");
            String newCardName = playedCard.getCardName();
            CardsDeck.createColoredOutputForCard(newCardName);

            System.out.println(currentPlayer.getName() + " \u001B[30;45m! ! ! Color change ! ! !\u001B[0m to: " + CardsDeck.createColoredOutputForCard(newCardName));
            currentPlayer = playerManager.getNextPlayer();

        } else {
            currentPlayer = playerManager.getNextPlayer();  // normale Karte
        }

    }

    // Helper-Method for CC-card
    public String askForColor() {
        System.out.println(currentPlayer.getName() + ", choose the next color: " +
                "\u001B[30;41m[R]\u001B[0m Red, " +
                "\u001B[30;42m[G]\u001B[0m Green, " +
                "\u001B[30;44m[B]\u001B[0m Blue, " +
                "\u001B[30;43m[Y]\u001B[0m Yellow");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine().toUpperCase();
            if (input.equals("R") || input.equals("G") || input.equals("B") || input.equals("Y")) {
                return input;
            }
            System.out.println("Invalid input. Enter R, G, B, or Y.");
        }
    }

    // Called when a round ends (player has no cards)
    public boolean handleRoundEnd(ArrayList<Player> players, Player currentPlayer, ScoreCalculator scoreCalculator) {
        // 1. Award points to the current player (winner of the round)
        int awardedPoints = scoreCalculator.awardPointsToWinner(players, currentPlayer);
        System.out.println(currentPlayer.getName() + " gets " + awardedPoints + " points!");

        // 2. Print ranking
        scoreCalculator.printRanking(players);

        // 3. Check if someone won the entire game (500+ points)
        Player gameWinner = scoreCalculator.checkForGameWinner(players);
        if (gameWinner != null) {
            System.out.println("WoW! " + gameWinner.getName() + " has won the game with " + gameWinner.getPoints() + " points!");
            return true;  // signal game over
        } else {
            System.out.println("Next round will start...");

            return false; // game continues
        }
    }


    public void startNewRound(PlayerManager playerManager, CardsDeck cardsDeck, Deque<Card> discardPile) {
        // Leere das bisherige Karten-Deck
        // Methode in CardsDeck um das ArrayList komplett zu leeren
        cardsDeck.clearDeck();
        // Erstelle ein neues vollständiges Karten-Deck mit allen Karten
        cardsDeck.createCardDeck();
        // Mische das neu erstellte Karten-Deck
        cardsDeck.shuffleCardDeck();
        // Für jeden Spieler leere die Kartenhand des Spielers (alte Runde)
        for (Player player : playerManager.getPlayerList()) {
            player.getCardsInHand().clear();
        }
        // Verteile Karten an alle Spieler
        cardsDeck.dealCards(playerManager.getPlayerList());
        // Leere Ablagestapel
        discardPile.clear();
        // Karte auf den Ablagestapel legen
        discardPile.push(cardsDeck.getTopCardAndRemoveFromList());
        //Spielrichtung zu Beginn der neuen Runde auf counter-clockwise)
        playerManager.setClockwise(false);

        // Startspieler setzen
        playerManager.setSequenceAndFirstPlayer(); // Methode spieler setzen
    }

    public void handleFirstCardEffect(PlayerManager playerManager, CardsDeck cardsDeck, Deque<Card> discardPile){

        // Check: if at start the top card is a direction change card (contains 'D')
        if (discardPile.peek().getCardName().toUpperCase().contains("D")) {
            playerManager.switchDirection();
            System.out.println("\u001B[30;41mDirection changed!\u001B[0m Now: " + (playerManager.isClockwise() ? "Clockwise" : "Counter-clockwise"));
            currentPlayer = playerManager.getNextPlayer();
            playerManager.printPlayerOrder();


        } else if (discardPile.peek().getCardName().toUpperCase().contains("+2")) {
            Player current = playerManager.getCurrentPlayer();
            current.addCard(cardsDeck.getTopCardAndRemoveFromList());
            current.addCard(cardsDeck.getTopCardAndRemoveFromList());
            System.out.println(currentPlayer.getName() + " \u001B[30;41mDraws 2 cards!\u001B[0m");
           currentPlayer = playerManager.getNextPlayer();

        } else if (discardPile.peek().getCardName().toUpperCase().contains("X")) {
            System.out.println("\u001B[30;46m[" + playerManager.getCurrentPlayer().getName() + "]\u001B[0m lost her/his turn:  \u001B[30;45mSkipped!\u001B[0m");
            currentPlayer = playerManager.getNextPlayer();
            System.out.println("\u001B[30;46m[" + currentPlayer.getName() + "]\u001B[0m, it's your turn!");
        }
    }

}


