import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Die Klasse PlayerManager verwaltet die Spieler, deren Reihenfolge,
 * die Spielrichtung sowie die aktuelle Spielerin oder den aktuellen Spieler.
 */
public class PlayerManager {

    /** Maximale Spieleranzahl im Spiel */
    final int NUMBER_OF_PLAYERS = 4;

    Scanner scanner = new Scanner(System.in);
    private ArrayList<Player> playerList;
    private boolean isClockwise;
    private int humanPlayersCount;
    private Player currentPlayer;

    /**
     * Konstruktor: Initialisiert die Spielerliste
     * und setzt die Spielrichtung auf gegen den Uhrzeigersinn.
     */
    public PlayerManager() {
        playerList = new ArrayList<>();
        isClockwise = false; // Startet gegen den Uhrzeigersinn
    }

    /**
     * Setzt den ersten Spieler in der Liste als aktuellen Spieler.
     */
    public void setCurrentPlayer() {
        if (!playerList.isEmpty()) {
            this.currentPlayer = playerList.get(0);
        } else {
            this.currentPlayer = null;
        }
    }

    /**
     * Startet den Prozess zur Vorbereitung der Spieler:
     * Fragt Anzahl, Namen und ergänzt ggf. Bots.
     */
    public void preparePlayers() {
        askPlayersCount();
        askPlayersNamesAndCreateBots();
        showPlayersAtBeginn();
    }


    /**
     * Mischt die Reihenfolge der Spieler und wählt zufällig den Startspieler.
     */
        public void setSequenceAndFirstPlayer() {
        Collections.shuffle(playerList);
        setCurrentPlayer();
    }


    /**
     * Fragt Namen der menschlichen Spieler ab
     * und erstellt ggf. Bots zur Vervollständigung.
     */
    public void askPlayersNamesAndCreateBots() {
        for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {  // gemeinsame Anzahl der Spieler im Spiel
            for (int j = i; j < humanPlayersCount; j++) { // menschliche Spieler
                System.out.println("Enter the name of Player " + (j + 1) + " : "); //j+1 - um Spieler 1 statt Spieler 0 zu sein
                playerList.add(new Player(scanner.next(), false));         //  neuen menschlichen Spieler wird erstellt
                i++;
            }
            createBotsIfExist(i);
        }
    }

    /**
     * Erstellt Bots, falls die Gesamtanzahl von 4 Spielern noch nicht erreicht ist.
     *
     * @param i Aktueller Index nach den menschlichen Spielern
     */
    private void createBotsIfExist(int i) {
        //  Bots (if exist) werden erstellt
        if (i < NUMBER_OF_PLAYERS) {
            playerList.add(new Player("Bot " + (i + 1), true));
        }
    }

    /**
     * Gibt alle Spielernamen in einer Begrüßung aus.
     */
    private void showPlayersAtBeginn() {
        for (Player player : playerList) {
            System.out.print(player.getName() + ", ");
        }
        System.out.print("Welcome in game!");
        System.out.println();
        System.out.println("-----------------------------");
    }


    /**
     * Fragt die Anzahl der menschlichen Spieler ab und gibt die Aufteilung aus.
     */
    public void askPlayersCount() {
        humanPlayersCount = humanPlayers();
        int bots = botsPlayers();
        System.out.println(humanPlayersCount + " human player(s) and " + bots + " bot(s) are playing");
    }

    /**
     * Gibt Anzahl der Bots zurück.
     *
     * @return Anzahl der Bot-Spieler
     */
    private int botsPlayers() {
        int bots = 0;
        if (humanPlayersCount < 4) {
            bots = 4 - humanPlayersCount;
        }
        return bots;
    }

    /**
     * Liest eine gültige Spieleranzahl zwischen 1 und 4 ein.
     *
     * @return Anzahl der menschlichen Spieler
     */
    private int humanPlayers() {
        do {
            try {
                System.out.println("How many live players will participate? (1 to 4)");
                System.out.print("Enter the number of live players: ");
                humanPlayersCount = scanner.nextInt();

            } catch (Exception e) {
                System.out.println("Try once more " + e.getMessage());  // wir brauchen unsere Exception
                scanner.next();
            }
        } while (isPlayerCountCorrect());
        return humanPlayersCount;
    }

    /**
     * Prüft, ob die eingegebene Spieleranzahl ungültig ist.
     *
     * @return true, wenn ungültig
     */
    private boolean isPlayerCountCorrect() {
        return humanPlayersCount < 0 || humanPlayersCount > NUMBER_OF_PLAYERS;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Wechselt die Spielrichtung (Uhrzeigersinn ↔ Gegen-Uhrzeigersinn).
     */
    // Wechselt die Spielrichtung
    // Negation des aktuellen Werts: true → false, false → true
    public void switchDirection() {
        isClockwise = !isClockwise;
    }


    /**
     * Gibt den nächsten Spieler entsprechend der aktuellen Spielrichtung zurück
     * und setzt ihn als aktuellen Spieler.
     *
     * @return Der nächste Spieler
     */
    public Player getNextPlayer() {

        int index = getCurrentPlayersIndex();
        if (isClockwise) {
             // Im Code: Index + 1 → rechts im Array → in der Realität gibt der Spieler an seinen linken Nachbarn weiter.
            // Im Uhrzeigersinn: Index + 1 (mit Modulo, damit nach dem letzten Spieler wieder bei 0 begonnen wird – Rundenlogik).
            // Beispiel: (1 + 1) % 4 = 2
            // Im Uhrzeigersinn: gehe zum nächsten Spieler in der Liste


            currentPlayer = playerList.get((index + 1) % playerList.size());
            System.out.println("Current Player: " + currentPlayer.getName());

        } else {
            // Im Code: Index - 1 → links im Array → in der Realität gibt der Spieler nach rechts weiter.
            //counter-clockwise Gegen den Uhrzeigersinn: Index - 1 (Modulo verhindert negative Zahlen). (0 - 1 + 4) % 4 = 3
            //Counterclockwise: Gegen den Uhrzeigersinn: gehe zum vorherigen Spieler in der Liste



            currentPlayer = playerList.get((index - 1 + playerList.size()) % playerList.size());
            System.out.println("Current Player: " + getCurrentPlayer().getName());
        }
        return currentPlayer;
    }

    /**
     * Gibt die Spielreihenfolge und Spielrichtung farblich formatiert in der Konsole aus.
     */
    // Wenn isClockwise == true, dann steht im Text "Clock-wise", sonst == false "Counter-clock-wise
    // System.out.println("\u001B[30;41mGame Direction :\u001B[0m " + (isClockwise ? "Clockwise" : "Counter-clockwise"));
    public void printPlayerOrderInColour() {
        System.out.println("\nPlayer order:");
        String style = "\u001B[30;47m"; // Schwarzer Text auf hellgrauem Hintergrund
        String reset = "\u001B[0m";

        for (int i = 0; i < playerList.size(); i++) {
            int index;
            if (isClockwise) {
                // Spielerreihenfolge im Uhrzeigersinn, ausgehend vom aktuellen Spieler
                index = (getCurrentPlayersIndex() + i) % playerList.size();
            } else {
                // Spielerreihenfolge gegen den Uhrzeigersinn, ausgehend vom aktuellen Spieler
                index = (getCurrentPlayersIndex() - i + playerList.size()) % playerList.size();
            }
            System.out.println(style + " [" + playerList.get(index).getName() + "] " + reset);
        }
        System.out.println("\n\u001B[30;41mGame Direction :\u001B[0m " + (isClockwise ? "Clockwise" : "Counter-clockwise"));

    }

    public boolean isClockwise() {
        return isClockwise;
    }

    public void setClockwise(boolean clockwise) {
        isClockwise = clockwise;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(ArrayList<Player> playerList) {
        this.playerList = playerList;
    }

    public int getHumanPlayersCount() {
        return humanPlayersCount;
    }

    public int getCurrentPlayersIndex() {
        return playerList.indexOf(currentPlayer);
    }


    /**
     * Gibt den Spieler zurück, der vor dem aktuellen Spieler kommt.
     *
     * @return Der vorherige Spieler
     */
    public Player getPreviousPlayer() {
        int index = getCurrentPlayersIndex();
        int previousPlayerIndex = -1;
        if (isClockwise) {
            previousPlayerIndex = (index - 1 + playerList.size()) % playerList.size();
        } else {
            previousPlayerIndex = (index + 1 ) % playerList.size();
        }
        return playerList.get(previousPlayerIndex);
    }

    /**
     * Leert die Handkarten aller Spieler – z. B. beim Start einer neuen Runde.
     */
    public void clearPlayersHand() {
        for (Player player : getPlayerList()) {
            player.getCardsInHand().clear();
        }
    }
}
