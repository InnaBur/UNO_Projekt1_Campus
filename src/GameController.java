import java.util.*;

public class GameController {

    private final PlayerManager playerManager = new PlayerManager();
    // menschliche Spieler
    private final Deque<Card> discardPile = new ArrayDeque<>();
    private final ScoreCalculator scoreCalculator = new ScoreCalculator();
    Scanner scanner = new Scanner(System.in);
    // aktueller Spieler
    private Player currentPlayer;
    //die Bedingung für das Beenden des Spiels.
    private boolean isExit = false;

    public void run() {
        CardsDeck cardsDeck = new CardsDeck();

        prepareGame();
        startNewRound(cardsDeck);

        do {
            //  PrintManager.printLine();

            showTopCard();
            currentPlayer = playerManager.getCurrentPlayer();
            currentPlayer.showHand();

            gamePlay(cardsDeck);

        } while (!isExit);
    }

    private void gamePlay(CardsDeck cardsDeck) {

        switch (userChoice()) {

            case 1:
                drawCard(cardsDeck);
                break;
            case 2:
                playCard(cardsDeck);
                break;
            case 3:
                System.out.println("Bluff check logic not implemented yet.");
                break;
            case 4:
                System.out.println(currentPlayer.getName() + " said UNO!");
                break;
            case 5:
                System.out.println("Suggestions isn't allowed. Draw two cards!");
                drawTwoCardsPenalty(cardsDeck);
                break;
            case 6:
                Instructions.printGameInstructions();
                break;
            case 0:
                System.out.println("Game is over!");
                //saveYoDatenbank;
                isExit = true;
        }
    }

    /*In this method, the number of human players and bots is determined,
      names and current Player are set, players list shuffel
     */
    private void prepareGame() {
        playerManager.preparePlayers();
        playerManager.setSequenceAndFirstPlayer();
    }

    private void playCard(CardsDeck cardsDeck) {

        System.out.println("Specify the card to play (e.g., r5, g+2, Bd, Gx):");
        String inputCardName = scanner.next().toUpperCase();
        Card selectedCard = currentPlayer.getCardByName(inputCardName);

        // Check if selected card exists in player's hand and is playable on top of discard pile
        if (isCardExistAndPlayable(selectedCard)) {
            currentPlayer.removeCard(selectedCard);
            discardPile.push(selectedCard);

            // Check if player has emptied their hand → they win the round
            if (isPlayersHandEmpty()) {
                System.out.println(currentPlayer.getName() + " has won the round!");
                isGameWinOrNewRound();
            } else {
                handlePlayedCard(selectedCard, cardsDeck);
            }
        } else {
            // Invalid or unplayable card is penalty
            invalidCardFromUserAndPenalty(cardsDeck);

        }

    }

    // Handle scoring and check if game ends
    private void isGameWinOrNewRound() {
        boolean isGameWin = handleRoundEnd(playerManager.getPlayerList(), currentPlayer, scoreCalculator);
        if (isGameWin) {
            //!!!!! Daten von DB
            isExit = true;
        } else {
            CardsDeck cardsDeck = new CardsDeck();
            startNewRound(cardsDeck);
        }
    }

    private void invalidCardFromUserAndPenalty(CardsDeck cardsDeck) {
        PrintManager.printInvalidInput(" or card cannot be played. You receive 2 penalty cards.");
        drawTwoCardsPenalty(cardsDeck);
        // next player
        currentPlayer = playerManager.getNextPlayer();
    }

    private void drawTwoCardsPenalty(CardsDeck cardsDeck) {
        currentPlayer.addCard(cardsDeck.getTopCardAndRemoveFromList(discardPile));
        currentPlayer.addCard(cardsDeck.getTopCardAndRemoveFromList(discardPile));
    }

    private boolean isPlayersHandEmpty() {
        return currentPlayer.getCardsInHand().isEmpty();
    }

    //Kann ein Spieler keine
    //passende Karte legen, so muss er eine Strafkarte vom verdeckten Stapel ziehen.
    private void drawCard(CardsDeck cardsDeck) {

        Card drawnCard = currentPlayer.addCard(cardsDeck.getTopCardAndRemoveFromList(discardPile));
        System.out.println("You drew: " + drawnCard);
        cardsDeck.getCardsDeck().remove(cardsDeck.getTopCardAndRemoveFromList(discardPile));

        System.out.println("Your new Card from the draw pile: " + drawnCard);
        currentPlayer.showHand();

        optionDirectPlayableOnTopCard(drawnCard, cardsDeck);
    }


    //Diese Karte kann Spieler
    //sofort wieder ausspielen, sofern diese passt.
    private void optionDirectPlayableOnTopCard(Card drawnCard, CardsDeck cardsDeck) {

        assert discardPile.peek() != null;
        if (drawnCard.isPlayableOn(discardPile.peek())) {
            String userInput = optionUserInputYesOrNo();

            //If the player wants to play a card, they place it from their hand onto the table;
            // if not, the next player in turn becomes the currentPlayer.
            if (userInput.equalsIgnoreCase("y")) {
                discardPile.addFirst(drawnCard);
                currentPlayer.removeCard(drawnCard);
                handlePlayedCard(drawnCard, cardsDeck);
            } else {
                currentPlayer = playerManager.getNextPlayer();
            }
        } else {
            System.out.println("This card cannot be played now.");
            currentPlayer = playerManager.getNextPlayer();
        }
    }

    private String optionUserInputYesOrNo() {
        String userInput;
        do {
            System.out.println("Do you want to PLAY this card? Press 'Y' for yes or 'N' for no");
            userInput = scanner.next();
        }
        while (!userInput.equalsIgnoreCase("n") && !userInput.equalsIgnoreCase("y"));
        return userInput;
    }

    private int userChoice() {
        int choice = -1; //initialize

        do {
            PrintManager.showMenu();
            try {
                choice = scanner.nextInt();
            } catch (Exception e) {
                PrintManager.printInvalidInput(". Try again.");
                scanner.next(); // Eingabe verwerfen
            }
        } while (isChoiceInMenuCorrect(choice));
        return choice;
    }

    // Show top card in color
    private void showTopCard() {
        assert discardPile.peek() != null;
        String coloredTopCard = CardsDeck.createColoredOutputForCard(discardPile.peek().getCardName());
        System.out.println("The top card is [" + coloredTopCard + "]");
    }

    private boolean isCardExistAndPlayable(Card selectedCard) {
        return selectedCard != null && selectedCard.isPlayableOn(discardPile.peek());
    }

    private boolean isChoiceInMenuCorrect(int choice) {
        return choice < 0 || choice > 8;
    }

    public void handlePlayedCard(Card playedCard, CardsDeck cardsDeck) {
        String cardName = playedCard.getCardName();

        // Spieler A spielt Richtungswechselkarte. Richtung ändern
        if (cardName.contains("D")) {
           directionChange();

        } else if (cardName.contains("X")) {
            skippPlayer();

        } else if (cardName.contains("+2")) {
            twoCardsToNextPlayer(cardsDeck);

        } else if (cardName.contains("+4")) {

            plusFourCardSpecial(); //!!!!!Sollte geschrieben werden
            String newColor = askForColor(); // Returns "R", "G", "B", or "Y"

            // Update the card name to include the chosen color,
            playedCard.setCardName(newColor + "+4");


            //IF CHECK BLUFF implementieren
            fourCardsToNextPlayer(cardsDeck);
            currentPlayer = playerManager.getNextPlayer();

        }
        // When the player plays "CC", ask for the color and update the card name
        else if (cardName.equals("CC")) {

            changeColour(playedCard);
            // Let the player choose a color


        } else {
            currentPlayer = playerManager.getNextPlayer();  // normale Karte
        }

    }

    private void changeColour(Card playedCard) {
        String newColor = askForColor(); // Returns "R", "G", "B", or "Y"

        // Update the card name to include the chosen color
        playedCard.setCardName(newColor + "CC");
        String newCardName = playedCard.getCardName();
        CardsDeck.createColoredOutputForCard(newCardName);

        PrintManager.printChangeColorMessage(currentPlayer.getName(), newCardName);
        currentPlayer = playerManager.getNextPlayer();
    }

    private void plusFourCardSpecial() {
    }

    private void fourCardsToNextPlayer(CardsDeck cardsDeck) {
        Player next = playerManager.getNextPlayer();
        for (int i = 0; i < 4; i++) {
            next.addCard(cardsDeck.getTopCardAndRemoveFromList(discardPile));
        }
        System.out.println(next.getName() + " \u001B[30;41mDraws 4 cards!\u001B[0m");
    }

    private void twoCardsToNextPlayer(CardsDeck cardsDeck) {
        Player next = playerManager.getNextPlayer();
        next.addCard(cardsDeck.getTopCardAndRemoveFromList(discardPile));
        next.addCard(cardsDeck.getTopCardAndRemoveFromList(discardPile));
        System.out.println(next.getName() + " \u001B[30;41mDraws 2 cards!\u001B[0m");
        currentPlayer = playerManager.getNextPlayer();
    }

    private void skippPlayer() {
        System.out.println("\u001B[30;46m[" + playerManager.getNextPlayer().getName() + "]\u001B[0m lost her/his turn: \u001B[30;45mSkipped!\u001B[0m");
        currentPlayer = playerManager.getNextPlayer();
    }

    private void directionChange() {
        playerManager.switchDirection();
        System.out.println(playerManager.getCurrentPlayer().getName() + " made a \u001B[30;41mDirection change\u001B[0m to "
                + (playerManager.isClockwise() ? "Clockwise" : "Counter-clockwise"));
        // Spieler neuer Nachbar in der neuen Richtung ist dran
        currentPlayer = playerManager.getNextPlayer();
        playerManager.printPlayerOrderInColour();
    }

    // Helper-Method for CC-card
    public String askForColor() {
        PrintManager.colorChoice(currentPlayer.getName());

//        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine().toUpperCase();
            if (userColourChoice(input)) {
                return input;
            }
            PrintManager.printInvalidInput(". Enter R, G, B, or Y.");
        }
    }

    private boolean userColourChoice(String input) {
        return input.equals("R") || input.equals("G") || input.equals("B") || input.equals("Y");
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


    public void startNewRound(CardsDeck cardsDeck) {

        // playerManager.setClockwise(false);     //Spielrichtung zu Beginn der neuen Runde auf counter-clockwise)

        clearPlayersHand();
        discardPile.clear();


        playerManager.getCurrentPlayer();


        cardsDeck.dealCards(playerManager.getPlayerList());
        Collections.shuffle(playerManager.getPlayerList());
//        playerManager.setSequenceAndFirstPlayer();
        playerManager.printPlayerOrderInColour();
        discardPile.addFirst(cardsDeck.getTopCardAndRemoveFromList(discardPile));       //first card from the cards deck is a first card in drawPile

        // playerManager.setSequenceAndFirstPlayer();
        //currentPlayer = playerManager.getCurrentPlayer();

        handleFirstCardEffect(cardsDeck);
//playerManager.getNextPlayer();

    }

    private void clearPlayersHand() {
        for (Player player : playerManager.getPlayerList()) {
            player.getCardsInHand().clear();
        }
    }

    public void handleFirstCardEffect(CardsDeck cardsDeck) {

        // Check: if at start the top card is a direction change card (contains 'D')
        if (discardPile.peek().getCardName().toUpperCase().contains("D")) {
            playerManager.switchDirection();
            System.out.println("\u001B[30;41mDirection changed!\u001B[0m Now: " + (playerManager.isClockwise() ? "Clockwise" : "Counter-clockwise"));
            currentPlayer = playerManager.getNextPlayer();
            playerManager.printPlayerOrderInColour();


        } else if (discardPile.peek().getCardName().toUpperCase().contains("+2")) {
            Player current = playerManager.getCurrentPlayer();
            current.addCard(cardsDeck.getTopCardAndRemoveFromList(discardPile));
            current.addCard(cardsDeck.getTopCardAndRemoveFromList(discardPile));
            System.out.println(currentPlayer.getName() + " \u001B[30;41mDraws 2 cards!\u001B[0m");
            currentPlayer = playerManager.getNextPlayer();

        } else if (discardPile.peek().getCardName().toUpperCase().contains("X")) {
            System.out.println("\u001B[30;46m[" + playerManager.getCurrentPlayer().getName() + "]\u001B[0m lost her/his turn:  \u001B[30;45mSkipped!\u001B[0m");
            currentPlayer = playerManager.getNextPlayer();
            System.out.println("\u001B[30;46m[" + currentPlayer.getName() + "]\u001B[0m, it's your turn!");

        } else if (discardPile.peek().getCardName().toUpperCase().contains("CC")) {
            // Let the player choose a color
            String newColor = askForColor(); // Returns "R", "G", "B", or "Y"

            // Update the card name to include the chosen color
            discardPile.peek().setCardName(newColor + "CC");
            String newCardName = discardPile.peek().getCardName();
            CardsDeck.createColoredOutputForCard(newCardName);

            System.out.println(currentPlayer.getName() + " \u001B[30;45m! ! ! Color change ! ! !\u001B[0m to: " + CardsDeck.createColoredOutputForCard(newCardName));
            currentPlayer = playerManager.getNextPlayer();
        }

    }


}


