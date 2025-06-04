import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class PlayerManager {
    final int NUMBER_OF_PLAYERS = 4;
    Scanner scanner = new Scanner(System.in);
    private ArrayList<Player> playerList;
    private boolean isClockwise;
//    private int currentPlayerIndex;
    private int humanPlayersCount;
    private Player currentPlayer;

    public PlayerManager() {
        playerList = new ArrayList<>();
//        Collections.addAll(playerList, players); // Array in List umwandeln
//        Collections.shuffle(playerList); // Spielerreihenfolge mischen. Das geht nur mit einer List, nicht direkt mit einem Array!
        isClockwise = false; // Startet gegen den Uhrzeigersinn
    }

    public void setCurrentPlayer() {
        if (!playerList.isEmpty()) {
            this.currentPlayer = playerList.get(0);
        } else {
            this.currentPlayer = null;
        }
    }

    public void preparePlayers() {

        askPlayersCount();
        askPlayersNames();


        setSequenceAndFirstPlayer();
    }

    private void setSequenceAndFirstPlayer() {
        Collections.shuffle(playerList);
        setCurrentPlayer();
    }


    public void askPlayersNames() {
        for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {  // gemeinsame Anzahl der Spieler im Spiel
            for (int j = i; j < humanPlayersCount; j++) { // menschliche Spieler
                System.out.println("Enter the name of player " + (j + 1)); //j+1 - um Spieler 1 statt Spieler 0 zu sein
                playerList.add(new Player(scanner.next(), false));
                //  neuen menschlichen Spieler wird erstellt
                i++; //
            }

            //  Bots (if exist) werden erstellt
            if (i < NUMBER_OF_PLAYERS) {
                playerList.add(new Player("Bot " + (i + 1), true));
            }
        }
        for (Player player : playerList) {
            System.out.println(player);
        }
    }

    public void askPlayersCount() {

        int bots = 0;

        do {
            try {
                System.out.println("How many human players play?");
                humanPlayersCount = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Try once more " + e.getMessage());  // wir brauchen unsere Exception
                scanner.next();
            }
        } while (humanPlayersCount < 0 || humanPlayersCount > 4);

        if (humanPlayersCount < 4) {
            bots = 4 - humanPlayersCount;
        }

        System.out.println(humanPlayersCount + " Human player and " + bots + " bots are playing");
    }


    // Gibt den aktuellen Spieler zurück, der am Zug ist.
    // Die Liste playerList enthält alle Spieler. Der currentPlayerIndex zeigt auf den Spieler, der aktuell an der Reihe ist


//    public Player getCurrentPlayer() {
//        return playerList.get(currentPlayerIndex);
//    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    // Wechselt die Spielrichtung
    // Negation des aktuellen Werts: true → false, false → true
    public void switchDirection() {
        isClockwise = !isClockwise;
    }


//    public Player getNextPlayer() {
//        if (isClockwise) {
//            // Uhrzeigersinn: Index + 1 (mit Modulo, damit es nach dem letzten Spieler wieder bei 0 beginnt - Rundenlogik. (1 + 1) % 4 = 2
//            currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
//            System.out.println("currentPlayerIndex " + currentPlayerIndex + currentPlayer);
//        } else {
//            // Gegen den Uhrzeigersinn: Index - 1 (Modulo verhindert negative Zahlen). (0 - 1 + 4) % 4 = 3
//            currentPlayerIndex = (currentPlayerIndex - 1 + playerList.size()) % playerList.size();
//            System.out.println("currentPlayerIndex " + currentPlayerIndex + getCurrentPlayer());
//        }
//        return getCurrentPlayer();
//    }

    public Player getNextPlayer() {
        int index = playerList.indexOf(currentPlayer);
        if (!isClockwise) {
            // Uhrzeigersinn: Index + 1 (mit Modulo, damit es nach dem letzten Spieler wieder bei 0 beginnt - Rundenlogik. (1 + 1) % 4 = 2
            currentPlayer = playerList.get((index + 1) % playerList.size());
            System.out.println("currentPlayer "  + currentPlayer);
        } else {
            // Gegen den Uhrzeigersinn: Index - 1 (Modulo verhindert negative Zahlen). (0 - 1 + 4) % 4 = 3
            currentPlayer = playerList.get((index - 1 + playerList.size()) % playerList.size());
            System.out.println("currentPlayerIndex "  + getCurrentPlayer());
        }
        return currentPlayer;
    }

    // Spieler überspringen, bei x-Karte (Aussetzen)
    public Player skipNextPlayer() {
        // Schritt 1: einmal weiter gehen (übersprungener Spieler)
        getNextPlayer();
        // Schritt 2: noch einmal weiter gehen (tatsächlicher nächster Spieler)
        return getNextPlayer();
    }

    public void printPlayerOrder() {
        // Wenn isClockwise == true, dann steht im Text "Clock-wise", sonst == false "Counter-clock-wise
        System.out.println("Game direction: " + (isClockwise ? "Clock-wise" : "Counter-clock-wise"));
        System.out.println("Player order:");
        if (isClockwise) {
            for (int i = playerList.size() - 1; i >= 0; i--) {
                System.out.println("- " + playerList.get(i).getName() + " ");
            }
        } else {
            for (Player p : playerList) {
                System.out.println("- " + p.getName());
            }
        }
    }

    public boolean isClockwise() {
        return isClockwise;
    }

    public void setClockwise(boolean clockwise) {
        isClockwise = clockwise;
    }

    public int getNUMBER_OF_PLAYERS() {
        return NUMBER_OF_PLAYERS;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(ArrayList<Player> playerList) {
        this.playerList = playerList;
    }

//    public int getCurrentPlayerIndex() {
//        currentPlayerIndex = playerList.indexOf(getCurrentPlayer());
//        return currentPlayerIndex;
//    }

//    public void setCurrentPlayerIndex(int currentPlayerIndex) {
//        this.currentPlayerIndex = currentPlayerIndex;
//    }

    public int getHumanPlayersCount() {
        return humanPlayersCount;
    }

    public void setHumanPlayersCount(int humanPlayersCount) {
        this.humanPlayersCount = humanPlayersCount;
    }

/*System.out.println("Current Player: "+manager.getCurrentPlayer().

    getName());

    // "x"-Karte wird gespielt → nächster Spieler wird übersprungen
    Player skippedPlayer = manager.skipNextPlayer();
System.out.println("Player after skip: "+skippedPLayer.getName());
*/

}

