import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Scanner;

public class GameController {

    //gemeinsame Anzahl der Spieler im Spiel
    final int NUMBER_OF_PLAYERS = 4;
    Scanner scanner = new Scanner(System.in);

    //Array für Reihenfolge und für gespeicherte Spieler
    Player[] players = new Player[NUMBER_OF_PLAYERS];
    private PlayerManager playerManager = new PlayerManager(players);
    ;
    // aktueller Spieler
    Player currentPlayer;
    //die Bedingung für das Beenden des Spiels.
    boolean isExit = false;
    // menschliche Spieler
    int humanPlayersCount;
    private Deque<Card> discardPile = new ArrayDeque<>();
    private ScoreCalculator scoreCalculator = new ScoreCalculator();

    public static void printGameInstructions() {

        System.out.println("=== UNO GAME ===\n");

        System.out.println("Goal of the Game:");
        System.out.println("Be the first player to reach 500 points by discarding all your cards in each round.");
        System.out.println("You earn points based on the cards left in your opponents' hands.\n");

        System.out.println(" Game Setup:");
        System.out.println("- Each player receives 7 cards.");
        System.out.println("- The rest form the draw pile.");
        System.out.println("- One card is placed face-up as the discard pile.");
        System.out.println("- The first player is chosen randomly.\n");

        System.out.println(" Gameplay:");
        System.out.println("- You can play a card if it matches the color or number of the top discard.");
        System.out.println("- Special wild card choose color can be played anytime, but wild draw four has rules.");
        System.out.println("- If you can't play, draw one card. If it's playable, you may use it.");
        System.out.println("- Say 'UNO!' when you have one card left. If you forget and get caught, draw 1 penalty card.\n");

        System.out.println(" Action Cards:");
        System.out.println("1. Draw Two: Next player draws 2 cards and skips their turn.");
        System.out.println("2. Reverse: Changes the direction of play.");
        System.out.println("3. Skip: Next player loses their turn.");
        System.out.println("4. Wild Choose the next color to play.");
        System.out.println("5. Wild Draw Four: Choose color + next player draws 4 cards. Only play if you have no matching color.\n");

        System.out.println(" Penalties:");
        System.out.println("- Forgot to say UNO: Draw 1 cards if caught.");
        System.out.println("- Suggesting moves to others: Draw 2 cards.");
        System.out.println("- Playing invalid card: Take the card back + draw 1 penalty card.");
        System.out.println("- Wrong +4 usage: If challenged and guilty, draw 4 cards. If innocent, challenger draws 6 cards.\n");

        System.out.println(" Scoring:");
        System.out.println("- Number cards: Number value.");
        System.out.println("- Draw Two, Reverse and Skip: 20 points each.");
        System.out.println("- Wild Choose Color and Wild Draw Four: 50 points each.");
        System.out.println("- First to 500 points wins.\n");

        System.out.println("Here’s how to enter cards and actions:\n");

        System.out.println("Playing cards:");
        System.out.println("- R5     → Red 5");
        System.out.println("- Rx     → Red Skip card");
        System.out.println("- R<->   → Red Reverse card");
        System.out.println("- +2     → Draw Two card");
        System.out.println("- +4     → Draw Four ");
        System.out.println("- fw     → Choose color\n");

        System.out.println("Player actions:");
        System.out.println("- 1 → Draw a card");
        System.out.println("- 2 → Play a card");
        System.out.println("- 3 → Bluff");
        System.out.println("- 4 → UNO - Last card in hand");
        System.out.println("- 5 → Give advice (e.g., suggest a color)");
        System.out.println("- 6 → Game Instructions");
        System.out.println("- 0 → Exit the game\n");

        System.out.println(" Color codes:");
        System.out.println("- R = Red");
        System.out.println("- G = Green");
        System.out.println("- B = Blue");
        System.out.println("- Y = Yellow");
        System.out.println("- +4 and fw are wild cards\n");

        System.out.println("Tip: Play smart, bluff wisely, and don't forget to say UNO!\n");

        System.out.println("Have fun and good luck!\n");
    }

    public void run() {
        CardsDeck cardsDeck = new CardsDeck();
        prepareGame(cardsDeck);

        //first card from the cards deck is a first card in drawPile
        discardPile.addFirst(cardsDeck.getTopCard());

        currentPlayer = playerManager.getCurrentPlayer();

        // Reihenfolge ausgeben
        // System.out.println("The players take their turns in the following order: ");
        // playerManager.printPlayerOrder();

        // Spielschleife
        do {

            //Auswahl Menu
            System.out.println("------------------------------");

            assert discardPile.peek() != null;
            System.out.println("The top card is " + discardPile.peek().getCardName());
            System.out.println(currentPlayer.getName() + ", it's your turn! ");
            currentPlayer.showHand();

            int auswahl = -1; //initialize
            do {
                System.out.println("""
                                                
                            Make your choice:
                            [1] Draw a card
                            [2] Play a card
                            [3] Accuse previous player of bluffing
                            [4] Say UNO
                            [5] Suggest a move
                            [0] Exit the game
                        """);

                try {
                    auswahl = scanner.nextInt();
                } catch (Exception e) {
                    System.out.println("Invalid input. Try again.");
                    scanner.next(); // Eingabe verwerfen
                }
            } while (auswahl < 0 || auswahl > 6);

            switch (auswahl) {

                case 1:
                    String userInput = "";

                    //Kann ein Spieler keine
                    //passende Karte legen, so muss er eine Strafkarte vom verdeckten Stapel ziehen.
                    Card currentUsersCard = currentPlayer.addCard(cardsDeck.getTopCard());
                    System.out.println("You drew: " + currentUsersCard);
                    cardsDeck.getCardsDeck().remove(cardsDeck.getTopCard());
                    currentPlayer.showHand();

                    //Diese Karte kann Spieler
                    //sofort wieder ausspielen, sofern diese passt.
                    if (currentUsersCard.isPlayableOn(discardPile.peek())) {
                        do {
                            System.out.println("Do you want to PLAY this card? Press 'y' or 'n'");
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
                    System.out.println("Specify the card to play (e.g., r5, g+2, <->, x):");
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

                                int awardedPoints = scoreCalculator.awardPointsToWinner(Arrays.asList(players), currentPlayer);
                                System.out.println(currentPlayer.getName() + " receives " + awardedPoints + " points!");

                                // Rangliste ausgeben
                                scoreCalculator.printRanking(Arrays.asList(players));

                                // Spielgewinner prüfen
                                Player gameWinner = scoreCalculator.checkForGameWinner(Arrays.asList(players));
                                if (gameWinner != null) {
                                    System.out.println("WoW! " + gameWinner.getName() + " has won the game with " + gameWinner.getPoints() + " points!");
                                    isExit = true;
                                } else {
                                    // Nur Runde ist vorbei, Spiel kann weitergehen
                                    System.out.println("Next round will start...");
                                    // isExit = true;  // Optional: oder neue Runde vorbereiten
                                }
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
                    printGameInstructions();
                    break;
                case 0:
                    System.out.println("Game is over!");
                    isExit = true;
            }

        } while (!isExit);
    }

    private void prepareGame(CardsDeck cardsDeck) {

        askPlayersCount();
        askPlayersNames();

        cardsDeck.dealCards(players);

        playerManager = new PlayerManager(players);
        playerManager.printPlayerOrder();

    }

    // Diese Methode fragt Names der Spieler und fühlt das Array für Reihenfolge und für gespeicherte Spieler
    private void askPlayersNames() {
        for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {  // gemeinsame Anzahl der Spieler im Spiel
            for (int j = i; j < humanPlayersCount; j++) { // menschliche Spieler
                System.out.println("Enter the name of player " + (j + 1)); //j+1 - um Spieler 1 statt Spieler 0 zu sein

                players[j] = new Player(scanner.next(), false); //  neuen menschlichen Spieler wird erstellt
                i++; //
            }

            //  Bots (if exist) werden erstellt
            if (i < NUMBER_OF_PLAYERS) {
                players[i] = new Player("Player " + (i + 1), true);
            }
        }
        System.out.println(Arrays.toString(players));
    }

    // Diese Methode fragt, wie viel menschliche Spieler gibt es und andere Spieler sind Bots
    private void askPlayersCount() {

        int bots = 0;

        do {
            try {
                System.out.println("How many human players play. Choose between 1 and 4.");
                humanPlayersCount = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Try once more " + e.getMessage());  // wir brauchen unsere Exception
                scanner.next();
            }
        } while (humanPlayersCount < 1 || humanPlayersCount > 4);

        if (humanPlayersCount < 4) {
            bots = 4 - humanPlayersCount;
        }

        System.out.println(humanPlayersCount + " Human player and " + bots + " bots are playing");
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


        } else if (cardName.contains("x")) {
            System.out.println(playerManager.getNextPlayer().getName() + " skipped!");
            currentPlayer = playerManager.getNextPlayer();

        } else if (cardName.contains("+2")) {
            Player next = playerManager.getNextPlayer();
            next.addCard(cardsDeck.getTopCard());
            next.addCard(cardsDeck.getTopCard());
            System.out.println(next.getName() + " draws 2 cards!");
            currentPlayer = playerManager.getNextPlayer();

        } else if (cardName.contains("+4")) {
            Player next = playerManager.getNextPlayer();
            for (int i = 0; i < 4; i++) {
                next.addCard(cardsDeck.getTopCard());
            }
            System.out.println(next.getName() + " draws 4 cards!");
            currentPlayer = playerManager.getNextPlayer();

        } else {
            currentPlayer = playerManager.getNextPlayer();  // normale Karte
        }
    }

}

