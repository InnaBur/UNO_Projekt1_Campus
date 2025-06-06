import java.util.ArrayDeque;
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
            String coloredTopCard = CardsDeck.getColoredCard(discardPile.peek().getCardName());
            System.out.println("The top card is [" + coloredTopCard + "]");
            System.out.println(currentPlayer.getName() + ", it's your turn! ");
            currentPlayer.showHand();

            int choice = -1; //initialize
            do {
                System.out.println("""
                                                
                            Make your choice:
                            [1] Draw a card
                            [2] Play a card
                            [3] Accuse previous player of bluffing
                            [4] Say UNO
                            [5] Suggest a move
                            [6] Check the bluff
                            [7] Instructions
                            [8] Pause
                            [0] Exit the game
                        """);

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
                    System.out.println("You drew: " + currentUsersCard);
                    cardsDeck.getCardsDeck().remove(cardsDeck.getTopCardAndRemoveFromList());

                    System.out.println("Your new Card from the draw pile: " + currentUsersCard);

                    currentPlayer.showHand();

                    //Diese Karte kann Spieler
                    //sofort wieder ausspielen, sofern diese passt.
                    if (currentUsersCard.isPlayableOn(discardPile.peek())) {
                        do {
                            System.out.println("Do you want to PLAY this card? Press 'Y' for yes or 'N' for no");
                            userInput = scanner.next().toLowerCase();
                        }
                        while (!userInput.equals("n") && !userInput.equals("y"));

                        //If the player wants to play a card, they place it from their hand onto the table;
                        // if not, the next player in turn becomes the currentPlayer.
                        if (userInput.equals("y")) {
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
                    // Karte manuell aus Hand spielen
                    System.out.println("Specify the card to play (e.g., r5, g+2, B<->, Gx):");
                    String inputCardName = scanner.next();
                    Card selectedCard = currentPlayer.getCardByName(inputCardName);

                    if (selectedCard != null) {
                        if (selectedCard.isPlayableOn(discardPile.peek())) {
                            currentPlayer.removeCard(selectedCard);
                            discardPile.push(selectedCard);
                            handlePlayedCard(selectedCard, cardsDeck);
                            // Prüfen, ob Spieler alle Karten losgeworden ist
                            if (currentPlayer.getCardsInHand().isEmpty()) {
                                System.out.println(currentPlayer.getName() + " has won the round!");

                                // Punktevergabe

//                                int awardedPoints = scoreCalculator.awardPointsToWinner(Arrays.asList(players), currentPlayer);
//                                System.out.println(currentPlayer.getName() + " receives " + awardedPoints + " points!");
//
//                                // Rangliste ausgeben
//                                scoreCalculator.printRanking(Arrays.asList(players));
//
//                                // Spielgewinner prüfen
//                                Player gameWinner = scoreCalculator.checkForGameWinner(Arrays.asList(players));
//                                if (gameWinner != null) {
//                                    System.out.println("WoW! " + gameWinner.getName() + " has won the game with " + gameWinner.getPoints() + " points!");
//                                    isExit = true;
//                                } else {
//                                    // Nur Runde ist vorbei, Spiel kann weitergehen
//                                    System.out.println("Next round will start...");
//                                    // isExit = true;  // Optional: oder neue Runde vorbereiten
//                                }
                            }

                        } else {
                            System.out.println("This card cannot be played on the current top card.");
                        }
                    } else {
                        System.out.println("You do not have this card.");
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
                    break;
                case 7:
                    Instructions.printGameInstructions();
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

    }

    public void handlePlayedCard(Card playedCard, CardsDeck cardsDeck) {
        String cardName = playedCard.getCardName();

        if (cardName.contains("<->")) {
            // Spieler A spielt Richtungswechselkarte
            // Richtung ändern
            playerManager.switchDirection();

            // Spieler D (neuer Nachbar in der neuen Richtung) ist dran
            currentPlayer = playerManager.getNextPlayer();

            System.out.println("Direction changed! Now: " + (playerManager.isClockwise() ? "clockwise" : "counterclockwise"));
            playerManager.printPlayerOrder();

        } else if (cardName.contains("x")) {
            System.out.println(playerManager.getNextPlayer().getName() + " skipped!");
            currentPlayer = playerManager.getNextPlayer();

        } else if (cardName.contains("+2")) {
            Player next = playerManager.getNextPlayer();
            next.addCard(cardsDeck.getTopCardAndRemoveFromList());
            next.addCard(cardsDeck.getTopCardAndRemoveFromList());
            System.out.println(next.getName() + " draws 2 cards!");
            currentPlayer = playerManager.getNextPlayer();

        } else if (cardName.contains("+4")) {
            Player next = playerManager.getNextPlayer();
            for (int i = 0; i < 4; i++) {
                next.addCard(cardsDeck.getTopCardAndRemoveFromList());
            }
            System.out.println(next.getName() + " draws 4 cards!");
            currentPlayer = playerManager.getNextPlayer();

        }
        // When the player plays "CC", ask for the color and update the card name
        else if (cardName.equals("CC")) {
            // Let the player choose a color
            String newColor = askForColor(); // Returns "R", "G", "B", or "Y"

            // Update the card name to include the chosen color
            playedCard.setCardName(newColor + "CC");

            System.out.println(currentPlayer.getName() + " changed color to: " + newColor);
            currentPlayer = playerManager.getNextPlayer();

        } else {
            currentPlayer = playerManager.getNextPlayer();  // normale Karte
        }

    }
// Helper-Method for CC-card
    public String askForColor() {
        System.out.println(currentPlayer.getName() + ", choose the next color: [R]ed, [G]reen, [B]lue, [Y]ellow");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine().toUpperCase();
            if (input.equals("R") || input.equals("G") || input.equals("B") || input.equals("Y")) {
                return input;
            }
            System.out.println("Invalid input. Enter R, G, B, or Y.");
        }
    }

}

