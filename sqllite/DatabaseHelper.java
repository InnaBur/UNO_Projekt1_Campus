import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Die Klasse DatabaseHelper übernimmt die gesamte Kommunikation mit der SQLite-Datenbank –
 * sie kapselt also alle SQL-relevanten Aufgaben,
 * damit dein restlicher Spielcode einfach, übersichtlich und von der Datenbank unabhängig bleibt.
 */
public class DatabaseHelper {

    private SQLLiteClient client;

    public DatabaseHelper(String dbName) throws SQLException {
        this.client = new SQLLiteClient(dbName);
    }

    // Initialisiert die Tabelle, wenn sie noch nicht existiert
    public void initDatabaseIfNeeded() throws SQLException {
        if (!client.tableExists("Sessions")) {
            client.executeStatement(
                    "CREATE TABLE Sessions (" +
                            "name varchar(100) NOT NULL, " +
                            "session int NOT NULL, " +
                            "round int NOT NULL, " +
                            "point int NOT NULL, " +
                            "isBot boolean NOT NULL, " +
                            "CONSTRAINT PK_Sessions PRIMARY KEY (name, session, round)" +
                            ");"
            );
        }
    }

    // Spielstand speichern
    //TODO: eventuell name ändern: nicht Round result wird gespeichert, sondern das gesamte Spiel
    public void saveRoundResult(String name, int sessionId, int roundNumber, int point, boolean isBot) throws SQLException {
        int isBotValue = isBot ? 1 : 0; // SQLite kennt kein echtes Boolean
        String insertSql = String.format(
                "INSERT INTO Sessions (name, session, round, point, isBot) VALUES ('%s', %d, %d, %d, %d);",
                name, sessionId, roundNumber, point, isBotValue
        );
        client.executeStatement(insertSql);
    }


//Game State wird gesspeichert: beim Exit(0)-also, das Spiel noch nicht beendet
//momentan mehr als Test für DB Connection und Speichern gedacht

    public void saveGameState(ArrayList<Player> players, int sessionId, int roundNumber) {
        try {
            initDatabaseIfNeeded();

            for (Player p : players) {
                saveRoundResult(
                        p.getName(),
                        sessionId,
                        roundNumber,
                        p.getPoints(),
                        p.isBot()
                );
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim Speichern in die Datenbank: " + e.getMessage());
        }
    }

    //Um DB vor das neue Spiel leeren

    public void resetSessions() throws SQLException {
        if (client.tableExists("Sessions")) {
            client.executeStatement("DELETE FROM Sessions;");
        }
    }


    // Weitere Methoden wie loadScores() usw. wären hier möglich
    //TODO: beim Ende des Spiels die Daten aus der Datenbank anzeigen

    // Gibt gespeicherte Daten am Ende des Spiels aus (zur Demonstration)
    public void showSessionData() {
        try {
            if (!client.tableExists("Sessions")) {
                System.out.println("Keine Daten vorhanden – Tabelle 'Sessions' existiert nicht.");
                return;
            }

            String query = "SELECT * FROM Sessions ORDER BY session, round, name;";
            ArrayList<HashMap<String, String>> rows = client.executeQuery(query);

            System.out.println("Gespeicherte Spieldaten in der Datenbank:");
            for (HashMap<String, String> row : rows) {
                String name = row.get("name");
                int session = Integer.parseInt(row.get("session"));
                int round = Integer.parseInt(row.get("round"));
                int point = Integer.parseInt(row.get("point"));
                boolean isBot = row.get("isBot").equals("1");

                System.out.printf("Spieler: %-10s | Session: %d | Runde: %d | Punkte: %d | Bot: %s%n",
                        name, session, round, point, isBot ? "Ja" : "Nein");
            }

        } catch (SQLException e) {
            System.out.println("Fehler beim Laden der Session-Daten: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Fehler beim Parsen von Zahlen aus der DB: " + e.getMessage());
        }
    }




}
