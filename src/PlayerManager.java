import java.util.ArrayList;
import java.util.Collections;

public class PlayerManager {


    ArrayList<Player> playerList;
    boolean isClockwise;
    int currentPlayerIndex;

    public PlayerManager(Player[] players) {
        playerList = new ArrayList<>();
        Collections.addAll(playerList, players);
        Collections.shuffle(playerList);
        isClockwise = false; // Startet gegen den Uhrzeigersinn
        currentPlayerIndex = 0;
    }

    public Player getCurrentPlayer() {
        return playerList.get(currentPlayerIndex);
    }

    public void switchDirection() {
        isClockwise = !isClockwise;
    }

    public Player getNextPlayer() {
        if (isClockwise) {
            currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
        } else {
            currentPlayerIndex = (currentPlayerIndex - 1 + playerList.size()) % playerList.size();
        }
        return getCurrentPlayer();
    }

    // Spieler überspringen, bei x-Karte (Aussetzen)
    public Player skipNextPlayer() {
        // Schritt 1: einmal weiter gehen (übersprungener Spieler)
        getNextPlayer();
        // Schritt 2: noch einmal weiter gehen (tatsächlicher nächster Spieler)
        return getNextPlayer();
    }

    public void printPlayerOrder() {
        System.out.println("Game direction: " + (isClockwise ? "Clock-wise" : "Counter-clock-wise"));
        System.out.println("Player order:");
        for (Player p : playerList) {
            System.out.println("- " + p.getName());
        }
    }

    public boolean isClockwise() {
        return isClockwise;
    }



/*System.out.println("Current Player: "+manager.getCurrentPlayer().

    getName());

    // "x"-Karte wird gespielt → nächster Spieler wird übersprungen
    Player skippedPlayer = manager.skipNextPlayer();
System.out.println("Player after skip: "+skippedPLayer.getName());
*/

}

