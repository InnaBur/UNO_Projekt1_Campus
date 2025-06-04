import java.util.ArrayList;
import java.util.Collections;

public class PlayerManager {


    ArrayList<Player> playerList;
    boolean isClockwise;
    int currentPlayerIndex;

    public PlayerManager(Player[] players) {
        playerList = new ArrayList<>();
        Collections.addAll(playerList, players); // Array in List umwandeln
        Collections.shuffle(playerList); // Spielerreihenfolge mischen. Das geht nur mit einer List, nicht direkt mit einem Array!
        isClockwise = false; // Startet gegen den Uhrzeigersinn
        currentPlayerIndex = 0;
    }
    // Gibt den aktuellen Spieler zurück, der am Zug ist.
    // Die Liste playerList enthält alle Spieler. Der currentPlayerIndex zeigt auf den Spieler, der aktuell an der Reihe ist
    public Player getCurrentPlayer() {
        return playerList.get(currentPlayerIndex);
    }

    // Wechselt die Spielrichtung
    // Negation des aktuellen Werts: true → false, false → true
    public void switchDirection() {
        isClockwise = !isClockwise;
    }

    public Player getNextPlayer() {
        if (isClockwise) {
            // Uhrzeigersinn: Index + 1 (mit Modulo, damit es nach dem letzten Spieler wieder bei 0 beginnt - Rundenlogik. (1 + 1) % 4 = 2
            currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
        } else {
            // Gegen den Uhrzeigersinn: Index - 1 (Modulo verhindert negative Zahlen). (0 - 1 + 4) % 4 = 3
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
        // Wenn isClockwise == true, dann steht im Text "Clock-wise", sonst == false "Counter-clock-wise
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

