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
        askPlayersNamesAndCreateBots();
        showPlayersAtBeginn();
    }

    public void setSequenceAndFirstPlayer() {
        Collections.shuffle(playerList);
        setCurrentPlayer();
    }


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

    private void createBotsIfExist(int i) {
        //  Bots (if exist) werden erstellt
        if (i < NUMBER_OF_PLAYERS) {
            playerList.add(new Player("Bot " + (i + 1), true));
        }
    }

    private void showPlayersAtBeginn() {
        for (Player player : playerList) {
            System.out.print(player.getName() + ", ");
        }
        System.out.print("Welcome in game!");
        System.out.println();
        System.out.println("-----------------------------");
    }

    public void askPlayersCount() {
        humanPlayersCount = humanPlayers();
        int bots = botsPlayers();
        System.out.println(humanPlayersCount + " human player(s) and " + bots + " bot(s) are playing");
    }

    private int botsPlayers() {
        int bots = 0;
        if (humanPlayersCount < 4) {
            bots = 4 - humanPlayersCount;
        }
        return bots;
    }

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

    private boolean isPlayerCountCorrect() {
       return humanPlayersCount < 0 || humanPlayersCount > NUMBER_OF_PLAYERS;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    // Wechselt die Spielrichtung
    // Negation des aktuellen Werts: true → false, false → true
    public void switchDirection() {
        isClockwise = !isClockwise;
    }

    public Player getNextPlayer() {
        /*In a clockwise game:Player passes to their left neighbor.
        In a counter-clockwise game:Player passes to their right neighbor.
        In a circle, going clockwise means going to the left in real seating at a round table.
        */
        int index = playerList.indexOf(currentPlayer);
        if (isClockwise) {
            //In code: Index + 1--> right in array --> real-life Player gives to his left neighbour: clockwise.: Index + 1 (mit Modulo, damit es nach dem letzten Spieler wieder bei 0 beginnt - Rundenlogik. (1 + 1) % 4 = 2
            // Clockwise: go to the next player in the list
            currentPlayer = playerList.get((index + 1) % playerList.size());
            System.out.println("Current Player: " + currentPlayer);

        } else {
            // In code: Index - 1 --> left in array --> real-life Player gives to his right neighbour: counter-clockwise.: Index - 1 (Modulo verhindert negative Zahlen). (0 - 1 + 4) % 4 = 3
            // Counterclockwise: go to the previous player in the list
            currentPlayer = playerList.get((index - 1 + playerList.size()) % playerList.size());
            System.out.println("Current Player: " + getCurrentPlayer());
        }
        return currentPlayer;
    }

    // Wenn isClockwise == true, dann steht im Text "Clock-wise", sonst == false "Counter-clock-wise
    // System.out.println("\u001B[30;41mGame Direction :\u001B[0m " + (isClockwise ? "Clockwise" : "Counter-clockwise"));
    public void printPlayerOrderInColour() {
        System.out.println("\nPlayer order:");
        String style = "\u001B[30;47m"; // Black text on light gray background
        String reset = "\u001B[0m";


        for (int i = 0; i < playerList.size(); i++) {
            int index;
            if (isClockwise) {
                // forward order from currentPlayer
                index = (playerList.indexOf(currentPlayer) + i) % playerList.size();
            } else {
                // backward order from currentPlayer
                index = (playerList.indexOf(currentPlayer) - i + playerList.size()) % playerList.size();
            }

            System.out.println(style + " [" + playerList.get(index).getName() + "] " + reset);
        }
        System.out.println("\u001B[30;41mGame Direction :\u001B[0m " + (isClockwise ? "Clockwise" : "Counter-clockwise"));

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

    public void setHumanPlayersCount(int humanPlayersCount) {
        this.humanPlayersCount = humanPlayersCount;
    }

}

/*
Rundenlogik ohne Modulo:

public Player getNextPlayer() {
    int index = playerList.indexOf(currentPlayer);

    if (isClockwise) {
        index = index + 1;
        // Wenn über das Ende hinaus, auf 0 zurücksetzen
        if (index >= playerList.size()) {
            index = 0;
        }
    } else {
        index = index - 1;
        // Wenn vor Anfang, auf letztes Element springen
        if (index < 0) {
            index = playerList.size() - 1;
        }
    }

    currentPlayer = playerList.get(index);
    System.out.println("Current Player: " + currentPlayer);
    return currentPlayer;
}


public void printPlayerOrder() {
    System.out.println("\nPlayer order:");
    String style = "\u001B[30;47m"; // Black text on light gray background
    String reset = "\u001B[0m";

    int index = playerList.indexOf(currentPlayer);

    for (int i = 0; i < playerList.size(); i++) {
        System.out.println(style + " [" + playerList.get(index).getName() + "] " + reset);

        if (isClockwise) {
            index = index + 1;
            if (index >= playerList.size()) {
                index = 0;
            }
        } else {
            index = index - 1;
            if (index < 0) {
                index = playerList.size() - 1;
            }
        }
    }

    System.out.println("\u001B[30;41mGame Direction :\u001B[0m " + (isClockwise ? "Clockwise" : "Counter-clockwise"));
}

*/



