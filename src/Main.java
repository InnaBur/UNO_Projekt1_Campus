import java.sql.SQLException;

/**
 * Einstiegspunkt für das UNO-Spiel.
 * <p>
 * Diese Klasse enthält die main-Methode, die das Spiel initialisiert,
 * die Spielanleitung ausgibt und das Spiel über den GameController startet.
 */

public class Main {

    /**
     * Hauptmethode – Startpunkt der Anwendung.
     *
     * @throws SQLException falls bei der Datenbankverbindung ein Fehler auftritt.
     */
    public static void main(String[] args) throws SQLException {
        // Spielanleitung in der Konsole anzeigen
        PrintManager.printGameInstructions();
        // Startnachricht
        System.out.println("Game starts");
        // Haupt-Spielsteuerung starten
        new GameController().run();

    }


}