import java.util.Arrays;
import java.util.Scanner;

public class SpielVerwaltung {

    //gemeinsame Anzahl der Spieler im Spiel
    final int PLAYERS_COUNT = 4;
    Scanner scanner = new Scanner(System.in);

    //Array für Reihenfolge und für gespeicherte Spieler
    Spieler [] spielers = new Spieler[4];

    // menschliche Spieler
    int humanPlayersCount;
    public void run() {

        askPlayersCount();

        // Diese Methode fragt Names der Spieler und fühlt das Array für Reihenfolge und für gespeicherte Spieler

        for (int i = 0; i < PLAYERS_COUNT; i++) {  // gemeinsame Anzahl der Spieler im Spiel
            for (int j = i; j < humanPlayersCount; j++) { // menschliche Spieler
                System.out.println("Enter the name of player " + (j+1)); //j+1 - um Spieler 1 statt Spieler 0 zu sein

                spielers [j] = new Spieler(scanner.next(), false); //  neuen menschlichen Spieler wird erstellt
                i++; //
            }

            //  Bots (if exist) werden erstellt
            if (i < PLAYERS_COUNT) {
                spielers [i] = new Spieler("Player " + (i+1), true);
            }
        }
        System.out.println(Arrays.toString(spielers));

        // Diese Methode erstellt Reihenfolge (randomly)


    }

    // Diese Methode fragt, wie viel menschliche Spieler gibt es und andere Spieler sind Bots
    private void askPlayersCount() {

        int bots = 0;

        do {
            try {
                System.out.println("How many human players play?");
                humanPlayersCount = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Try once more " + e.getMessage());  // wir brauchen unsere Exception
                scanner.next();
            }
        } while (humanPlayersCount < 1 || humanPlayersCount > 4);

        if (humanPlayersCount < 4) {
            bots = 4 - humanPlayersCount;
        }

        System.out.println(humanPlayersCount + " menschliche Spieler und " + bots + " bots sind dran");
    }
}
