import java.sql.SQLException;
import java.util.*;


/**
 * Die Klasse {@code GameController} steuert den gesamten Ablauf des UNO-Spiels.
 * Sie koordiniert den Spielstart, die Spielrunden, Benutzerinteraktionen,
 * Kartenaktionen, Punktevergabe sowie die Kommunikation mit der Datenbank.
 *
 * Funktionen:
 * - Initialisierung des Spiels
 * - Durchführung von Spieler- und Bot-Zügen
 * - Auswertung von Spezialkarten
 * - Verwaltung von Spielrunden und Spielende
 * - Kommunikation mit {@code PlayerManager}, {@code CardsDeck}, {@code ScoreCalculator}, {@code DBManager}
 */
public class GameController {

    private final PlayerManager playerManager = new PlayerManager();
    private final Deque<Card> discardPile = new ArrayDeque<>();
    private final ScoreCalculator scoreCalculator = new ScoreCalculator();
    Scanner scanner = new Scanner(System.in);
    String[] colours = new String[]{"R", "Y", "B", "G"};
    // Rounds counter
    int counter = 0;
    // aktueller Spieler
    private Player currentPlayer;
    //die Bedingung für das Beenden des Spiels.
    private boolean isExit = false;
    SqliteClient client = DBManager.createTableInDB();

    /**
     * Startet das Spiel:
     * - Initialisiert Kartendeck
     * - Bereitet Spieler vor
     * - Startet die erste Runde und das Hauptspiel.
     */

    public void run() {
        CardsDeck cardsDeck = new CardsDeck();
        prepareGame();
        currentPlayer = playerManager.getCurrentPlayer();
        startRound(cardsDeck);
        startGame(cardsDeck);
    }

    /**
     * Hauptspielschleife.
     * Wiederholt die Spielerzüge bis das Spiel beendet wird.
     *
     * @param cardsDeck Das aktuelle Kartendeck.
     */
    //In diese Methode ist der ganze Spielablauf
    private void startGame(CardsDeck cardsDeck) {
        do {
            showTopCard();
            currentPlayer = playerManager.getCurrentPlayer();
            if (!currentPlayer.isBot()) {
                playerPlays(cardsDeck);
            } else {
                botPlays(cardsDeck);
            }
        } while (!isExit);
    }

    /**
     * Zeigt das Auswahlmenü für den aktuellen Spieler an und führt je nach Eingabe eine Aktion aus.
     * <p>Folgende Optionen stehen zur Verfügung:</p>
     * <ul>
     *   <li><b>1</b> – Eine Karte ziehen.</li>
     *   <li><b>2</b> – Eine Karte ausspielen (wenn Spieler 2 Karten hat, aber kein UNO sagt → Strafkarte).</li>
     *   <li><b>3</b> – "UNO" ansagen und Karte ausspielen.</li>
     *   <li><b>4</b> – Ungültige Option → Spieler erhält zwei Strafkarten.</li>
     *   <li><b>5</b> – Spielanleitung anzeigen.</li>
     *   <li><b>0</b> – Spiel beenden und aktuelle Punkte aus der Datenbank anzeigen.</li>
     * </ul>
     *
     * @param cardsDeck Das aktuelle Kartendeck, das im Spiel verwendet wird.
     */
    private void gamePlay(CardsDeck cardsDeck) {

        switch (userChoice()) {
            case 1:
                drawCard(cardsDeck);
                break;
            case 2:
                // Spielt Karte OHNE "UNO", wenn letzte 2 Karten auf Hand sind
                if (currentPlayer.getCardsInHand().size() == 2) {
                    System.out.println("You forgot to say UNO! You get 1 penalty card");
                    StrafManager.drawOneCardPenalty(cardsDeck, currentPlayer, discardPile);
                    playCard(cardsDeck);
                } else {
                    playCard(cardsDeck);
                }
                break;
            case 3:
                playerSaysUno(cardsDeck);
                break;
            case 4:
                System.out.println("Suggestions isn't allowed. Draw two cards!");
                StrafManager.drawTwoCardsPenalty(cardsDeck, currentPlayer, discardPile);
                break;
            case 5:
                PrintManager.printGameInstructions();
                break;
            case 0:
                System.out.println("Game is over!");
                //Daten aus DB
                DBManager.takeDatenFromDB(1, client);
                //saveYoDatenbank;
                isExit = true;
        }
    }


    /**
     * Führt den Spielzug eines menschlichen Spielers aus.
     * <p>Zeigt die aktuellen Handkarten des Spielers an und ruft  das Auswahlmenü auf,
     * in dem der Spieler eine Aktion (z.B. Karte spielen, ziehen, UNO sagen) wählen kann.</p>
     *
     * @param cardsDeck Das aktuelle Kartendeck des Spiels.
     */

    private void playerPlays(CardsDeck cardsDeck) {
        currentPlayer.showHand();
        gamePlay(cardsDeck);
    }

    /**
     * Bereitet das Spiel vor, indem:
     * <ul>
     *   <li>die Anzahl der menschlichen Spieler und Bots festgelegt wird,</li>
     *   <li>die Namen abgefragt und die Spieler erstellt werden,</li>
     *   <li>ein Startspieler bestimmt und die Spielerreihenfolge gemischt wird,</li>
     *   <li>die Datenbank bereinigt wird.</li>
     * </ul>
     * Diese Methode wird zu Beginn des Spiels einmal aufgerufen.
     */
    private void prepareGame() {
        playerManager.preparePlayers();
        playerManager.setSequenceAndFirstPlayer();
        DBManager.cleanDB(client);
    }

    /**
     * Startet eine neue Runde im Spiel, indem ein neues Kartendeck erstellt
     * und die Runde initialisiert wird (z. B. Kartenverteilung, Spielfeld zurücksetzen).
     *
     * @return Ein neues {@link CardsDeck}-Objekt für die nächste Runde.
     */
    public CardsDeck startNewRound() {
        CardsDeck cardsDeck = new CardsDeck();
        startRound(cardsDeck);
        return cardsDeck;
    }

    /**
     * Startet eine neue Spielrunde:
     * - Setzt die Spielrichtung zurück
     * - Mischt Karten neu und teilt sie aus
     * - Bestimmt erste Karte und löst deren Effekt aus
     *
     * @param cardsDeck Das neu erstellte Kartendeck.
     */

    //Spielrichtung zu Beginn der neuen Runde auf counter-clockwise
    //Die erste Karte vom Kartenstapel ist die erste Karte im Nachziehstapel(drawPile)
    public void startRound(CardsDeck cardsDeck) {
        playerManager.setClockwise(false);
        playerManager.clearPlayersHand();
        discardPile.clear();

        cardsDeck.dealCards(playerManager.getPlayerList());
        Collections.shuffle(playerManager.getPlayerList());
        playerManager.printPlayerOrderInColour();

        discardPile.push(cardsDeck.getTopCardAndRemoveFromList(discardPile));
        handleFirstCardEffect(cardsDeck);
    }

    /**
     * Führt das Ausspielen einer Karte durch.
     * Prüft Gültigkeit und Spielbarkeit, entfernt Karte aus der Hand und verarbeitet Spezialeffekte.
     *
     * @param cardsDeck Das aktuelle Kartendeck.
     */
    private void playCard(CardsDeck cardsDeck) {
        System.out.println("Specify the card to play (e.g., r5, g+2, Bd, Gx):");
        String inputCardName = scanner.next().toUpperCase();
        Card selectedCard = currentPlayer.getCardByName(inputCardName);

        // Überprüft, ob die ausgewählte Karte in der Hand des Spielers vorhanden ist und
        // oben auf dem Ablagestapel gespielt werden kann
        if (isCardExistAndPlayable(selectedCard)) {
            currentPlayer.removeCard(selectedCard);
            discardPile.push(selectedCard);

            // Überprüfen Sie, ob der Spieler seine Hand geleert hat → er gewinnt die Runde
            if (isPlayersHandEmpty()) {
                System.out.println(currentPlayer.getName() + " has won the round!");
                isGameWinOrNewRound();
            } else {
                handlePlayedCard(selectedCard, cardsDeck);
            }
        } else {
            // Ungültige oder nicht spielbare Karte ist eine Strafe
            StrafManager.invalidCardFromUserAndPenalty(cardsDeck, currentPlayer, discardPile);
            currentPlayer = playerManager.getNextPlayer();
        }

    }

    /**
     * Führt den Spielzug eines Bots aus:
     * - Spielt eine gültige Karte, wenn vorhanden
     * - Entscheidet zufällig, ob bei zwei verbleibenden Karten „UNO“ gesagt wird
     * - Prüft, ob der Bot die Runde gewinnt oder der Effekt der gespielten Karte ausgeführt wird
     * - Falls keine Karte spielbar ist, zieht der Bot eine Karte
     *
     * @param cardsDeck Das aktuelle Kartendeck.
     */
    private void botPlays(CardsDeck cardsDeck) {
        currentPlayer.showHand();
        Card botsCard = botsCardToPlay(cardsDeck);
        if (botsCard != null) {
            botSaysUnoOrNot(cardsDeck);
            currentPlayer.removeCard(botsCard);
            discardPile.push(botsCard);
            System.out.println(currentPlayer.getName() + " plays "
                    + CardsDeck.createColoredOutputForCard(discardPile.peek().getCardName()));

            if (isPlayersHandEmpty()) {
                System.out.println(currentPlayer.getName() + " has won the round!");
                counter++;
                isGameWinOrNewRound();
            } else {
                handlePlayedCard(botsCard, cardsDeck);
            }
        } else {
            drawCard(cardsDeck);
        }
    }

    /**
     * Entscheidet zufällig, ob der Bot bei zwei verbleibenden Karten „UNO“ sagt.
     * Falls er es vergisst, erhält er eine Strafkarte.
     *
     * @param cardsDeck Das aktuelle Kartendeck, um ggf. Strafkarten zu ziehen.
     */

    private void botSaysUnoOrNot(CardsDeck cardsDeck) {
        if (currentPlayer.getCardsInHand().size() == 2) {
            Random random = new Random();
            int uno = random.nextInt() % 2;
            if (uno == 0) {
                System.out.println(currentPlayer.getName() + " forgot to say UNO and get 1 penalty card");
                StrafManager.drawOneCardPenalty(cardsDeck, currentPlayer, discardPile);
            } else {
                System.out.println("\u001B[30;46m[" + currentPlayer.getName() + "] said UNO!\u001B[0m");
            }
        }
    }

    /**
     * Ermittelt die Karte, die der Bot in diesem Zug spielen möchte.
     * <p>
     * Die Methode prüft, welche Karten aus der Hand des Bots auf die oberste Karte des Ablagestapels spielbar sind.
     * Wenn eine „+4“-Karte vorhanden ist, wird mit 20% Wahrscheinlichkeit geblufft (d.h. „+4“ gespielt,
     * auch wenn andere Karten spielbar wären).
     * Ansonsten wird zufällig eine andere spielbare Karte gewählt.
     * Falls keine andere Karte außer „+4“ verfügbar ist, wird diese gespielt.
     *
     * @param cardsDeck Das aktuelle Kartendeck (nicht direkt verwendet, aber Teil der Spielstruktur).
     * @return Die vom Bot gewählte Karte oder {@code null}, wenn keine Karte spielbar ist.
     */

    //in diese Methode ist verrechnen, welche Karte der Bot spielen kann, um dann die Möglichkeit des Bluffs
    //an 20% zu setzen
    private Card botsCardToPlay(CardsDeck cardsDeck) {
        ArrayList<Card> botsCardsToPlay = new ArrayList<>();  // Liste der regulären spielbaren Karten
        Card plusFour = null;  // Speichert ggf. eine +4-Karte separat


        // Durchläuft alle Handkarten des Bots
        for (Card card : currentPlayer.getCardsInHand()) {
            if (card.isPlayableOn(discardPile.peek())) { // Karte ist spielbar auf oberste Ablagekarte
                if (card.getCardName().contains("+4")) {
                    // Speichert die +4-Karte (für Bluff-Möglichkeit)
                    plusFour = card;
                } else {
                    // Fügt reguläre spielbare Karte zur Liste hinzu
                    botsCardsToPlay.add(card);
                }
            }
        }

        // Wenn es spielbare Karten (außer +4) gibt
        if (!botsCardsToPlay.isEmpty()) {
            Random random = new Random();
            // Mit 20% Wahrscheinlichkeit wird absichtlich „+4“ gespielt (Bluff),
            // auch wenn andere Karten möglich wären
            //bluff with 20% random
            if (plusFour != null && random.nextDouble() < 0.2) {
                return plusFour;
            } else {
                // Sonst wird eine zufällige spielbare Karte aus der Liste gewählt
                int index = random.nextInt(botsCardsToPlay.size());
                return botsCardsToPlay.get(index);
            }
        }
        // Wenn keine andere Karte außer +4 spielbar ist, wird +4 gespielt
        return plusFour;
    }

    /**
     * Prüft, ob das Spiel nach dem aktuellen Rundenende gewonnen ist oder eine neue Runde gestartet werden soll.
     *
     * <p>Wenn ein Spieler mindestens 500 Punkte erreicht hat, wird das Spiel beendet.
     * Ansonsten wird eine neue Runde mit einem neu gemischten Kartendeck gestartet.</p>
     *
     * @param players Die aktuelle Liste aller Spieler.
     * @param round   Die aktuelle Rundenzahl.
     */
    private void isGameWinOrNewRound(ArrayList<Player> players, int round) {
        // Ermittelt, ob ein Spieler nach dieser Runde das Spiel gewonnen hat
        boolean isGameWin = handleRoundEnd(playerManager.getPlayerList());
        if (isGameWin) {
            // Wenn Spiel gewonnen → Datenbank abfragen & Spiel beenden
            DBManager.takeDatenFromDB(1, client);
            System.out.println("Ther are " + counter + " rounds!");
            isExit = true;
        } else {
            // Spiel ist nicht vorbei → Neue Runde vorbereiten und starten
            CardsDeck newDeck = startNewRound();
            startGame(newDeck);
        }
    }


    /**
     * Prüft, ob der aktuelle Spieler keine Karten mehr auf der Hand hat.
     *
     * @return {@code true}, wenn der Spieler keine Karten mehr hat, andernfalls {@code false}.
     */
    private boolean isPlayersHandEmpty() {
        return currentPlayer.getCardsInHand().isEmpty();
    }

    /**
     * Zieht eine Karte vom Kartenstapel für den aktuellen Spieler (menschlich oder Bot).
     * <p>
     * Falls der Spieler eine Karte ziehen muss, wird sie der Hand hinzugefügt und ggf. sofort ausgespielt,
     * wenn sie gültig ist. Bots spielen die Karte automatisch aus, falls sie passt.
     *
     * @param cardsDeck Das aktuelle Kartendeck.
     */
    //Kann ein Spieler keine passende Karte legen, so muss er eine Strafkarte vom verdeckten Stapel ziehen.
    private void drawCard(CardsDeck cardsDeck) {

        if (!currentPlayer.isBot()) {
            // Spieler zieht eine Karte vom Stapel
            Card drawnCard = currentPlayer.addCard(cardsDeck.getTopCardAndRemoveFromList(discardPile));
            System.out.println("Your new Card from the draw pile: " + drawnCard.getCardName());
            // Zeigt die neue Hand nach dem Ziehen
            currentPlayer.showHand();
            // Prüft, ob die gezogene Karte sofort ausgespielt werden kann
            optionDirectPlayableOnTopCard(drawnCard, cardsDeck);
        } else {
            // Bot zieht eine Karte
            Card drawnCard = currentPlayer.addCard(cardsDeck.getTopCardAndRemoveFromList(discardPile));
            System.out.println(currentPlayer.getName() + " drew: " + drawnCard.getCardName());
            currentPlayer.showHand();
            // Falls Karte spielbar ist, wird sie automatisch ausgespielt
            if (drawnCard.isPlayableOn(discardPile.peek())) {
                currentPlayer.removeCard(drawnCard);
                discardPile.push(drawnCard);
                handlePlayedCard(drawnCard, cardsDeck);
                System.out.println(currentPlayer.getName() + " plays " + CardsDeck.createColoredOutputForCard(drawnCard.getCardName()));
            } else {
                // Andernfalls ist der nächste Spieler dran
                currentPlayer = playerManager.getNextPlayer();
            }
        }
    }

    /**
     * Prüft, ob die gezogene Karte direkt auf die oberste Karte des Ablagestapels gespielt werden kann.
     * <p>
     * Wenn ja, kann der Spieler entscheiden, ob er sie sofort ausspielen möchte.
     * Wenn nicht oder der Spieler lehnt ab, wird der nächste Spieler aktiv.
     *
     * @param drawnCard  Die gerade gezogene Karte.
     * @param cardsDeck  Das aktuelle Kartendeck.
     */
    //Diese Karte kann Spieler sofort wieder ausspielen, sofern diese passt.
    private void optionDirectPlayableOnTopCard(Card drawnCard, CardsDeck cardsDeck) {
        assert discardPile.peek() != null;

        // Wenn die gezogene Karte auf die oberste Karte passt
        if (drawnCard.isPlayableOn(discardPile.peek())) {
            String userInput = optionUserInputYesOrNo();// Spielerentscheidung: Karte spielen?
        //Wenn der Spieler eine Karte spielen möchte, legt er sie aus seiner Hand auf den Tisch;

            if (userInput.equalsIgnoreCase("y")) {
                discardPile.push(drawnCard);
                currentPlayer.removeCard(drawnCard);
                handlePlayedCard(drawnCard, cardsDeck);
            } else {
                //Wenn nicht, wird der nächste Spieler zum aktuellen Spieler.
                // Spieler lehnt ab → nächster Spieler ist dran
                currentPlayer = playerManager.getNextPlayer();
            }
        } else {
            // Wenn Karte nicht passt, Info und weiter zum nächsten Spieler
            System.out.println("This card cannot be played now.");
            currentPlayer = playerManager.getNextPlayer();
        }
    }

    /**
     * Fragt den Benutzer, ob er die gezogene Karte sofort ausspielen möchte.
     * <p>
     * Es wird eine Eingabeaufforderung angezeigt, die nur 'Y' (ja) oder 'N' (nein) akzeptiert.
     * Ungültige Eingaben führen zur Wiederholung der Abfrage.
     *
     * @return Die Benutzereingabe ("y" oder "n").
     */
    private String optionUserInputYesOrNo() {
        //Speichert die Auswahl des Users
        String userInput;
        do {
            // Benutzerabfrage: Karte sofort spielen?
            System.out.println("Do you want to PLAY this card? Press 'Y' for yes or 'N' for no");
            userInput = scanner.next();
        }
        // Wiederholen, bis Eingabe gültig ist (nur 'y' oder 'n')
        while (!userInput.equalsIgnoreCase("n") && !userInput.equalsIgnoreCase("y"));
        return userInput;
    }


    /**
     * Fordert den Benutzer zur Auswahl einer Aktion im Spielmenü auf.
     * <p>
     * Zeigt ein Menü an und liest die Eingabe als Ganzzahl ein.
     * Bei ungültiger Eingabe wird eine Fehlermeldung angezeigt und die Eingabe erneut abgefragt.
     *
     * @return Die vom Benutzer gewählte gültige Menüoption als Ganzzahl.
     */
    private int userChoice() {
        int choice = -1; //  int choice = -1; // Initialwert setzen (ungültig)
        do {
            PrintManager.showMenu(); // Menü anzeigen
            try {
                choice = scanner.nextInt(); // Benutzereingabe lesen
            } catch (Exception e) {
                // Bei ungültiger Eingabe (z.B. Buchstaben) Fehlermeldung anzeigen
                PrintManager.printInvalidInput(". Try again.");
                scanner.next(); // Eingabe verwerfen
            }
        } while (isChoiceInMenuCorrect(choice)); // Wiederholen, solange Eingabe ungültig ist

        return choice; //Ausgewählter Menüpunkt (Ganzzahl)
    }

    /**
     * Zeigt die oberste Karte des Ablagestapels farbig im Terminal an.
     * <p>
     * Die Methode greift auf die oberste Karte des Stapels zu,
     * wandelt sie in eine farbige Zeichenkette um und gibt sie auf der Konsole aus.
     * <p>
     * Nutzt ANSI-Farbcodes zur besseren Lesbarkeit.
     */
    private void showTopCard() {
        // Sicherstellen, dass der Ablagestapel nicht leer ist
        assert discardPile.peek() != null;
        // Farbig formatierte Darstellung der obersten Karte
        String coloredTopCard = CardsDeck.createColoredOutputForCard(discardPile.peek().getCardName());
        System.out.println("\nThe top card is [" + coloredTopCard + "]");
    }


    /**
     * Prüft, ob die ausgewählte Karte existiert und auf die oberste Karte des Ablagestapels gespielt werden kann.
     *
     * @param selectedCard Die vom Spieler gewählte Karte.
     * @return {@code true}, wenn die Karte vorhanden und spielbar ist, sonst {@code false}.
     */
    private boolean isCardExistAndPlayable(Card selectedCard) {
        // Rückgabe true, wenn Karte nicht null ist und laut Spielregel gespielt werden darf
        return selectedCard != null && selectedCard.isPlayableOn(discardPile.peek());
    }

    /**
     * Prüft, ob die eingegebene Menü-Auswahl außerhalb des gültigen Bereichs liegt.
     *
     * @param choice Die vom Benutzer eingegebene Auswahl.
     * @return {@code true}, wenn die Auswahl ungültig ist (kleiner 0 oder größer 8), sonst {@code false}.
     */
    private boolean isChoiceInMenuCorrect(int choice) {
        // Nur Werte zwischen 0 und 8 sind gültige Menüoptionen
        return choice < 0 || choice > 8;
    }


    /**
     * Behandelt die Auswirkungen der gespielten Karte und steuert den Spielverlauf entsprechend.
     *
     * @param playedCard Die vom Spieler gespielte Karte.
     * @param cardsDeck  Das aktuelle Kartendeck.
     */
    public void handlePlayedCard(Card playedCard, CardsDeck cardsDeck) {
        String cardName = playedCard.getCardName();

        // Spieler A spielt Richtungswechselkarte. Richtung ändern
        if (cardName.contains("D")) {
            directionChange();
        // Aussetzen-Karte: Nächster Spieler wird übersprungen
        } else if (cardName.contains("X")) {
            skippPlayer();

        // +2-Karte: Nächster Spieler muss 2 Karten ziehen
        } else if (cardName.contains("+2")) {
            Player next = playerManager.getNextPlayer();
            StrafManager.twoCardsToNextPlayer(cardsDeck, next, discardPile);
            currentPlayer = playerManager.getNextPlayer(); // Zug geht weiter zum übernächsten Spieler

        // +4-Karte: Spieler wählt Farbe und es kann ein Bluff überprüft werden
        } else if (cardName.contains("+4")) {
            plusFourCardSpecial(playedCard, cardsDeck);
        }
        // Farbwahlkarte "CC": Spieler muss eine Farbe wählen

        else if (cardName.equals("CC")) {
            changeColour(playedCard);
        // Normale Karte: einfach zum nächsten Spieler weitergeben
        } else {
            currentPlayer = playerManager.getNextPlayer();  // normale Karte
        }
    }

    /**
     * Entscheidet zufällig, ob der Bot den Bluff des vorherigen Spielers überprüfen möchte.
     *
     * @param currentPlayer Der aktuelle Spieler (Bot), der entscheiden soll.
     * @return {@code true}, wenn der Bot den Bluff überprüfen möchte, sonst {@code false}.
     */
    private boolean botDecidesToCheckBluff(Player currentPlayer) {
        Random random = new Random();
        // Zufällige Entscheidung (true oder false)
        boolean decidesToCheck = random.nextBoolean();
        // Ausgabe der Entscheidung im Spiel
        System.out.println(currentPlayer.getName() + (decidesToCheck ? " decides to check bluff." :
                " decides not to check bluff."));
        return decidesToCheck;
    }

    /**
     * Fragt den menschlichen Spieler, ob er den Bluff eines anderen Spielers überprüfen möchte.
     *
     * Der Spieler gibt "y" für Ja oder "n" für Nein ein.
     * Die Eingabe wird so lange wiederholt, bis eine gültige Antwort erfolgt.
     *
     * @return {@code true}, wenn der Spieler den Bluff überprüfen möchte, sonst {@code false}.
     */
    private boolean askPlayerIfCheckBluff() {
        String antwort;
        boolean chose = false; // Entscheidung des Spielers (true = prüfen)
        boolean validInput = false; // Kontrolliert, ob Eingabe gültig war

        do {
            System.out.println("Do you want to check bluff? y- if yes, n - no");
            antwort = scanner.nextLine(); // Benutzereingabe ein

            switch (antwort) {
                case "y":
                    chose = true; // Spieler will den Bluff prüfen
                    validInput = true; // gültige Eingabe
                    break;
                case "n":
                    validInput = true;  // gültige Eingabe, aber Spieler will nicht prüfen
                    break;
                default:
                    System.out.println("Invalid input. Please enter y or n.");// ungültige Eingabe
            }
        } while (!validInput);
        return chose;
    }

    /**
     * Lässt den Spieler (oder Bot) eine neue Farbe wählen, wenn eine Farbwechselkarte ("CC") gespielt wurde.
     * Die gewählte Farbe wird an den Kartennamen angehängt (z.B. "RCC").
     * Anschließend wird die Farbe farbig angezeigt und der nächste Spieler ist an der Reihe.
     *
     * @param playedCard Die gespielte Farbwechselkarte, deren Farbe aktualisiert wird.
     */
    private void changeColour(Card playedCard) {
        String newColor = "";
        if (!currentPlayer.isBot()) {     // Menschlicher Spieler wählt Farbe über Konsoleneingabe
            newColor = askForColor(); // Returns "R", "G", "B", or "Y"
        } else {
            newColor = botChoosesColor();    // Bot wählt eine zufällige Farbe
        }
        // Kartennamen entsprechend aktualisieren, z.B. "RCC"
        playedCard.setCardName(newColor + "CC");
        String newCardName = playedCard.getCardName();
        CardsDeck.createColoredOutputForCard(newCardName);
        // Hinweis an Spieler, welche Farbe gewählt wurde
        PrintManager.printChangeColorMessage(currentPlayer.getName(), newCardName);

        // Nächster Spieler ist an der Reihe
        currentPlayer = playerManager.getNextPlayer();
    }

    /**
     * Wählt zufällig eine Farbe für den Bot, wenn dieser eine Farbwechselkarte (z.B. +4 oder CC) spielt.
     * Die Farbe wird aus dem Array {@code colours} ausgewählt und in der Konsole ausgegeben.
     *
     * @return Ein String mit der gewählten Farbe: "R", "G", "B" oder "Y"
     */

    private String botChoosesColor() {
        Random random = new Random();
        int index = random.nextInt(colours.length);   // Zufälliger Index im Bereich des Farben-Arrays
        System.out.println(currentPlayer.getName() + " chooses " + colours[index]);
        // Rückgabe der gewählten Farbe
        return colours[index];
    }

    /**
     * Führt die Spezialbehandlung der +4-Karte durch:
     * - Der Spieler wählt (oder der Bot bestimmt zufällig) eine neue Farbe.
     * - Die Karte wird mit der gewählten Farbe aktualisiert.
     * - Der nächste Spieler hat die Möglichkeit, einen Bluff zu überprüfen.
     *
     * @param playedCard Die gespielte +4-Karte.
     * @param cardsDeck  Das aktuelle Kartendeck, von dem ggf. Strafkarten gezogen werden.
     */

    private void plusFourCardSpecial(Card playedCard, CardsDeck cardsDeck) {
        String newColor = "";
        System.out.println(currentPlayer.getName() + " plays Card +4");
        if (!currentPlayer.isBot()) {  // Farbwahl: Menschlicher Spieler wird gefragt, Bot wählt zufällig
            newColor = askForColor(); // Returns "R", "G", "B", or "Y"
        } else {
            newColor = botChoosesColor();
        }

        // Kartenname wird mit der gewählten Farbe aktualisiert (z.B. "G+4")
        playedCard.setCardName(newColor + "+4");
        // Spielerwechsel nach gespielter +4-Karte
        currentPlayer = playerManager.getNextPlayer();
        // Zeige die neue oberste Karte (aktualisierte +4)
        showTopCard();
        // Ermöglicht dem nächsten Spieler, einen Bluff zu überprüfen
        checkBluffOrNot(cardsDeck);
    }
    /**
     * Prüft, ob der nächste Spieler den Bluff nach einer +4-Karte aufdecken möchte.
     * - Ein menschlicher Spieler wird gefragt.
     * - Ein Bot entscheidet zufällig.
     *
     * Wenn der Spieler den Bluff prüfen will, wird {@code checkBluffMethode(...)} aufgerufen.
     * Ansonsten muss der aktuelle Spieler 4 Strafkarten ziehen.
     *
     * @param cardsDeck Das aktuelle Kartendeck.
     */
    private void checkBluffOrNot(CardsDeck cardsDeck) {
        boolean checkBluff = false;

        // Menschlicher Spieler wird gefragt, Bot entscheidet zufällig
        if (!currentPlayer.isBot()) {
            checkBluff = askPlayerIfCheckBluff();
        } else {
            checkBluff = botDecidesToCheckBluff(currentPlayer);
        }
        // Wenn Bluff geprüft wird → entsprechende Logik
        if (checkBluff) {
            checkBluffMethode(cardsDeck);
        } else {
            // Kein Bluff-Check → Spieler muss 4 Strafkarten ziehen
            StrafManager.fourCardsToCurrentPlayer(cardsDeck, currentPlayer, discardPile);
            // Weitergabe an nächsten Spieler
            currentPlayer = playerManager.getNextPlayer();
        }
    }

    /**
     * Prüft, ob der vorherige Spieler bei einer +4-Karte geblufft hat.
     *
     * - Wenn der vorherige Spieler eine andere passende Karte (außer +4) hätte spielen können,
     *   gilt dies als Bluff → der vorherige Spieler erhält 4 Strafkarten.
     * - Andernfalls hat er nicht geblufft → der aktuelle Spieler erhält 6 Strafkarten.
     *
     * @param cardsDeck Das aktuelle Kartendeck.
     */
    private void checkBluffMethode(CardsDeck cardsDeck) {
        // Oberste Karte vom Ablagestapel (die +4-Karte)
        Card topCard = discardPile.peek();
        // Spieler, der die +4-Karte gelegt hat
        Player prevPlayer = playerManager.getPreviousPlayer();
        // Sicherheitsprüfung (dürfte nie null sein)
        assert topCard != null;

        // Überprüfen, ob der Spieler eine andere Karte als +4 hätte spielen können
        if (hasPlayerPlayableCardNotPlus4(topCard, prevPlayer)) {
            // Bluff erkannt → vorheriger Spieler bekommt 4 Strafkarten
            StrafManager.fourCardsToPrevPlayer(cardsDeck, prevPlayer, discardPile);
        } else {
            // Kein Bluff → aktueller Spieler bekommt 6 Strafkarten
            StrafManager.sixCardsToCurrentPlayer(cardsDeck, currentPlayer, discardPile);
            // Nächster Spieler ist an der Reihe
            currentPlayer = playerManager.getNextPlayer();
        }
    }

    /**
     * Überspringt den nächsten Spieler:
     * Gibt eine Nachricht aus und setzt den aktuellen Spieler auf den übernächsten Spieler.
     */

    private void skippPlayer() {
        PrintManager.skippMessage(playerManager.getNextPlayer().getName());
        currentPlayer = playerManager.getNextPlayer();
    }

    /**
     * Wechselt die Spielrichtung (Uhrzeigersinn <-> Gegenuhrzeigersinn),
     * informiert darüber und setzt den nächsten Spieler gemäß der neuen Richtung.
     */
    private void directionChange() {
        playerManager.switchDirection();
        PrintManager.directionChangeMessage(playerManager.getCurrentPlayer().getName(), playerManager.isClockwise());

        // Spieler neuer Nachbar in der neuen Richtung ist dran
        currentPlayer = playerManager.getNextPlayer();

        // Gibt die neue Spielreihenfolge farblich aus
        playerManager.printPlayerOrderInColour();
    }

    /**
     * Fragt den aktuellen Spieler nach der gewünschten Farbe,
     * die nach dem Ausspielen einer Farbwahlkarte (z.B. "CC" oder "+4") gesetzt werden soll.
     *
     * @return Die gewählte Farbe als String: "R", "G", "B" oder "Y".
     */
    // Helper-Method for CC-card
    public String askForColor() {
        // Druckt Aufforderung zur Farbauswahl für den aktuellen Spieler
        PrintManager.colorChoice(currentPlayer.getName());
        while (true) {
            String input = scanner.nextLine();
            if (userColourChoice(input)) {
                return input;
            } else {
                // Wiederhole, wenn Eingabe ungültig ist
                System.out.println("Enter R, G, B, or Y.");
            }
        }
    }


    /**
     * Prüft, ob die Eingabe eine gültige Farbe für das UNO-Spiel darstellt.
     * Gültige Eingaben sind: R (Rot), G (Grün), B (Blau), Y (Gelb).
     *
     * @param input Die Eingabe des Spielers.
     * @return true, wenn die Eingabe gültig ist, sonst false.
     */

    private boolean userColourChoice(String input) {
        // Vergleicht Eingabe mit den erlaubten Farbcodes (unabhängig von Groß-/Kleinschreibung)
        return input.equalsIgnoreCase("R") || input.equalsIgnoreCase("G") ||
                input.equalsIgnoreCase("B") || input.equalsIgnoreCase("Y");
    }

    /**
     * Wird aufgerufen, wenn ein Spieler keine Karten mehr hat.
     * - Berechnet Punkte
     * - Gibt das Ranking aus
     * - Speichert die Runde in der Datenbank
     * - Prüft, ob das Spiel beendet ist
     *
     * @param players Liste aller Spieler
     * @return {@code true}, wenn das Spiel gewonnen wurde, sonst {@code false}
     */
    public boolean handleRoundEnd(ArrayList<Player> players) {
        // 1. Vergabe von Punkten an den aktuellen Spieler (Gewinner der Runde)
        int awardedPoints = scoreCalculator.awardPointsToWinner(players, currentPlayer);
        System.out.println(currentPlayer.getName() + " gets " + awardedPoints + " points!");

        // 2. Druck Rangliste
        scoreCalculator.printRanking(players);
        try {
            // Speichern in die DB
            DBManager.addDatenIntoDB(players, 1, counter, client);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Player gameWinner = scoreCalculator.checkForGameWinner(players);
        if (gameWinner != null) {
            System.out.println("WoW! " + gameWinner.getName() + " has won the game with "
                    + gameWinner.getPoints() + " points!");
            return true;  // Signal „Spiel vorbei“
        } else {
            //Daten aus DB
            DBManager.takeDatenFromDB(1, client);
            System.out.println("Next round will start...");
            return false; // Spiel geht weiter (game continues)
        }
    }

    /**
     * Prüft die erste Karte im Ablagestapel und führt gegebenenfalls ihren Effekt aus.
     * Spezielle Karten wie Richtungswechsel, Aussetzen, +2, Farbwahl und +4 werden dabei berücksichtigt.
     *
     * @param cardsDeck Das aktuelle Kartendeck, das im Spiel verwendet wird.
     */
    public void handleFirstCardEffect(CardsDeck cardsDeck) {

        // Prüfen, ob es sich um eine Richtungswechsel-Karte handelt
        if (isTopCardSpecial("D")) {
            showTopCard();
            directionChange(); // Spielrichtung umkehren

        // Prüfen, ob es sich um eine +2-Karte handelt
        } else if (isTopCardSpecial("+2")) {
            showTopCard();
            firstCardDrawTwo(cardsDeck); // Aktueller Spieler muss zwei Karten ziehen

        // Prüfen, ob es sich um eine Aussetzen-Karte handelt
        } else if (isTopCardSpecial("X")) {
            showTopCard();
            firstCardSkipp(); // Aktueller Spieler wird übersprungen

        // Prüfen, ob es sich um eine Farbwahlkarte (CC) handelt
        } else if (isTopCardSpecial("CC")) {
            showTopCard();
            assert discardPile.peek() != null;
            changeColour(discardPile.peek());  // Spieler wählt eine Farbe

        // Prüfen, ob es sich um eine +4-Karte handelt (darf am Anfang nicht gespielt werden)
        } else if (isTopCardSpecial("+4")) {
            showTopCard();
            // +4 darf nicht als Startkarte gelten → zurück ins Deck und neue Karte ziehen
            cardsDeck.getCardsDeck().add(discardPile.pop());
            discardPile.push(cardsDeck.getTopCardAndRemoveFromList(discardPile));
            showTopCard(); // Neue Karte anzeigen
        }

    }

    /**
     * Effekt der Startkarte: Aussetzen (Skip) –
     * Der aktuelle Spieler wird übersprungen und der nächste Spieler ist an der Reihe.
     */
    private void firstCardSkipp() {
        // Nachricht: Spieler wird übersprungen
        PrintManager.skippMessage(playerManager.getCurrentPlayer().getName());
        currentPlayer = playerManager.getNextPlayer();
        // Hinweis in Farbe, dass der nächste Spieler dran ist
        System.out.println("\u001B[30;46m[" + currentPlayer.getName() + "]\u001B[0m, it's your turn!");
    }

    /**
     * Effekt der Startkarte: +2 –
     * Der aktuelle Spieler muss zwei Strafkarten ziehen und wird danach übersprungen.
     *
     * @param cardsDeck Das aktuelle Kartendeck, aus dem gezogen wird.
     */
    private void firstCardDrawTwo(CardsDeck cardsDeck) {
        // Aktueller Spieler bestimmen
        currentPlayer = playerManager.getCurrentPlayer();
        // Zwei Karten vom Stapel ziehen
        currentPlayer.addCard(cardsDeck.getTopCardAndRemoveFromList(discardPile));
        currentPlayer.addCard(cardsDeck.getTopCardAndRemoveFromList(discardPile));
        // Hinweis ausgeben
        PrintManager.twoCardsMessage(currentPlayer.getName());
        // Nächster Spieler ist dran
        currentPlayer = playerManager.getNextPlayer();
    }


    /**
     * Prüft, ob die oberste Karte des Ablagestapels ein bestimmtes Symbol enthält
     * (z.B. "D" für Richtungswechsel, "+2" für Zieh-zwei etc.).
     *
     * @param s Das gesuchte Symbol (z.B. "D", "+2", "X", "+4", "CC").
     * @return true, wenn das Symbol in der Kartensignatur enthalten ist.
     */
    private boolean isTopCardSpecial(String s) {
        return discardPile.peek().getCardName().toUpperCase().contains(s);
    }


    /**
     * Prüft, ob ein Spieler mindestens eine passende Karte auf der Hand hat,
     * die **nicht** die +4-Karte ist.
     * Diese Methode wird verwendet, um einen möglichen Bluff beim Ausspielen einer +4-Karte zu überprüfen.
     *
     * @param topCard Die aktuell oberste Karte auf dem Ablagestapel (die +4-Karte).
     * @param player  Der Spieler, der überprüft werden soll (ob er blufft).
     * @return true, wenn der Spieler eine Karte hat, die auf die vorherige Karte passt (Farbe),
     *         andernfalls false.
     */

    public boolean hasPlayerPlayableCardNotPlus4(Card topCard, Player player) {
        // Alle Karten des Spielers holen
        ArrayList<Card> playersCards = player.getCardsInHand();
        // Temporär die +4-Karte vom Ablagestapel entfernen, um die darunterliegende Karte zu prüfen
        discardPile.pop();

        // Die Karte unter der +4-Karte (vorherige Karte auf dem Ablagestapel)
        String topNameBefor = discardPile.peek().getCardName().toUpperCase();

        // Durch alle Karten des Spielers iterieren
        for (Card card : playersCards) {

            // Wenn eine Karte mit derselben Farbe wie die vorherige Karte vorhanden ist
            String cardName = card.getCardName().toUpperCase();
            if ((cardName.charAt(0) == topNameBefor.charAt(0))) {
                discardPile.push(topCard); // +4-Karte wieder auf Ablagestapel legen
                return true;  // Spieler hätte andere Karte legen können → Bluff!
            }
        }
        // Kein Bluff: Spieler hatte keine passende Karte
        discardPile.push(topCard); // +4-Karte wieder auf Ablagestapel legen
        return false;
    }

    /**
     * Diese Methode wird aufgerufen, wenn ein Spieler "UNO!" sagen möchte.
     * Wenn der Spieler genau zwei Karten auf der Hand hat, darf er "UNO!" rufen und eine Karte spielen.
     * Ansonsten wird eine Warnung ausgegeben, dass er zu viele Karten hat.
     *
     * @param cardsDeck Das aktuelle Kartendeck des Spiels.
     */
    private void playerSaysUno(CardsDeck cardsDeck) {
        // Prüfen, ob der Spieler genau 2 Karten auf der Hand hat
        if (currentPlayer.getCardsInHand().size() == 2) {
            // UNO korrekt gesagt → Spielzug fortsetzen
            System.out.println("\u001B[30;46m[" + currentPlayer.getName() + "] said UNO!\u001B[0m");
            playCard(cardsDeck); // Spieler darf eine Karte ausspielen
        } else {
            // Falsche Anzahl an Karten → "UNO" nicht erlaubt
            System.out.println("Too many cards for UNO!");
        }
    }
}


