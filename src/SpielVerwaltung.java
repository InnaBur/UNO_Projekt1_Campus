import java.util.Arrays;
import java.util.Scanner;

public class SpielVerwaltung {

    //gemeinsame Anzahl der Spieler im Spiel
    final int PLAYERS_COUNT = 4;
    Scanner scanner = new Scanner(System.in);

    //Array für Reihenfolge und für gespeicherte Spieler
    Spieler[] spielers = new Spieler[4];

    // aktueller Spieler
    Spieler currentPlayer;

    //die Bedingung für das Beenden des Spiels.
    boolean isExit = false;

    // menschliche Spieler
    int humanPlayersCount;

    public void run() {

        askPlayersCount();
        askPlayersNames();

        // Diese Methode erstellt Reihenfolge (randomly) - IN PROGRESS
        System.out.println("The queue of players is being created..");

        System.out.println("The players take their turns in the following order: ");

        // Spielverlauf
        do {
            // aktueller Spieler (TEMPORARY for Tests ist initializer als Spieler 1 von dem Array !!!!
            Spieler currentPlayer = spielers[0];
            //Auswahl Menu
            System.out.println(currentPlayer.getName() + ", it's your move! Make your choice: ");
            int auswahl = 0;
            do {
                System.out.println("------------------------------------------");
                System.out.println("If you want to DRAW the card  - press 1\n" +
                        "If you want to PLAY the card  - press 2\n" +
                        "If you want to catch the previous player BLUFFing - press 3\n" +
                        "If you have only one card left, to say 'UNO' - press 4\n" +
                        "If you want to suggest a move or action - press 5\n" +
                        "If you want to exit - press 0\n");

                try {
                    auswahl = scanner.nextInt();
                } catch (Exception e) {
                    System.out.println("Try once more " + e.getMessage());  // wir brauchen unsere Exception
                    scanner.next();
                }
            } while (auswahl < 0 || auswahl > 5);

            switch (auswahl) {
                case 1:
                case 2:
                    System.out.println("Specify the card: first letter of color + card number (no spaces). " +
                            "Special cards: +2 or +4; reverse: <->");
                case 3:
                case 4:
                case 5:
                case 0:
                    System.out.println("Game is over!");
                    isExit = true;


            }


        } while (!isExit);


    }

    // Diese Methode fragt Names der Spieler und fühlt das Array für Reihenfolge und für gespeicherte Spieler
    private void askPlayersNames() {
        for (int i = 0; i < PLAYERS_COUNT; i++) {  // gemeinsame Anzahl der Spieler im Spiel
            for (int j = i; j < humanPlayersCount; j++) { // menschliche Spieler
                System.out.println("Enter the name of player " + (j + 1)); //j+1 - um Spieler 1 statt Spieler 0 zu sein

                spielers[j] = new Spieler(scanner.next(), false); //  neuen menschlichen Spieler wird erstellt
                i++; //
            }

            //  Bots (if exist) werden erstellt
            if (i < PLAYERS_COUNT) {
                spielers[i] = new Spieler("Player " + (i + 1), true);
            }
        }
        System.out.println(Arrays.toString(spielers));
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
