import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Die Klasse DBManager stellt Methoden zur Verfügung,
 * um UNO-Spielstände in einer SQLite-Datenbank zu speichern,
 * auszulesen und zu verwalten.
 */
public class DBManager {

    /** SQL-Statement zum Erstellen der Tabelle "Sessions" */
    private static final String CREATETABLE = "CREATE TABLE Sessions (Player varchar(100) NOT NULL, Session int NOT NULL, Round int NOT NULL, Score int NOT NULL, CONSTRAINT PK_Sessions PRIMARY KEY (Player, Session, Round));";

    /** Vorlage zum Einfügen von Spielerdaten */
    private static final String INSERT_TEMPLATE = "INSERT INTO Sessions (Player, Session, Round, Score) VALUES ('%1s', %2d, %3d, %4d);";

    /** SQL-Query zur Abfrage aller Scores einer Session */
    static String selectAllScores = "SELECT Player, SUM(Score) AS Score FROM Sessions WHERE Session = 1 GROUP BY Player;";

    /**
     * Erstellt die Tabelle "Sessions" in der Datenbank.
     * Existiert die Tabelle bereits, wird sie gelöscht und neu erstellt.
     *
     * @return Ein {@link SqliteClient}-Objekt, mit dem weitergearbeitet werden kann.
     */
    public static SqliteClient createTableInDB() {
        SqliteClient client = null;
        try {
             client = new SqliteClient("uno.sqlite");

            if (client.tableExists("Sessions")) {
                client.executeStatement("DROP TABLE Sessions;");
            }
            //Tabelle wird hier neu erstellt
            client.executeStatement(CREATETABLE);
        } catch (SQLException ex) {
            System.out.println("Ups! Something went wrong:" + ex.getMessage());
            ex.printStackTrace();
        }
        return client;
    }


    /**
     * Fügt Spielerdaten (Name, Session, Runde, Punktzahl) in die Datenbank ein.
     * Vor dem Einfügen werden alle Daten der gegebenen Session gelöscht.
     *
     * @param players Die Liste der Spieler mit ihren Punkteständen.
     * @param session Die Session-Nummer.
     * @param round Die aktuelle Rundennummer.
     * @param client Ein gültiger {@link SqliteClient}.
     * @throws SQLException Falls beim Schreiben in die Datenbank ein Fehler auftritt.
     */
    public static void addDatenIntoDB(ArrayList<Player> players, int session, int round, SqliteClient client) throws SQLException {
        client.executeStatement("DELETE FROM Sessions WHERE Session = " + session + ";");
        for (Player player : players) {
            client.executeStatement(String.format(INSERT_TEMPLATE, player.getName(), session, round, player.getPoints()));
        }
    }


    /**
     * Liest die Scores einer bestimmten Session aus der Datenbank und gibt sie in der Konsole aus.
     *
     * @param session Die Session-Nummer, aus der Daten geholt werden sollen.
     * @param client Ein {@link SqliteClient}, der mit der Datenbank verbunden ist.
     * Dieses SqliteClient-Objekt ist bereits mit deiner Datenbank (z.B. uno.sqlite) verbunden und bereit, SQL-Befehle auszuführen.
     */
    public static void takeDatenFromDB(int session, SqliteClient client) {
        try {
            String selectAllScores = "SELECT Player, SUM(Score) AS Score FROM Sessions WHERE Session = " + session + " GROUP BY Player;";
            ArrayList<HashMap<String, String>> results = client.executeQuery(selectAllScores);
            System.out.println("Data from Database: ");
            for (HashMap<String, String> map : results) {
                System.out.println(map.get("Player") + " hat derzeit: " + map.get("Score") + " Punkte");
            }
            if (results.isEmpty()) {
                System.out.println("There is no data in DB");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Setzt die Datenbank zurück, indem die Tabelle gelöscht und neu erstellt wird.
     *
     * @param client Ein {@link SqliteClient}, der mit der Datenbank verbunden ist.
     */

    public static void cleanDB(SqliteClient client) {
        try {
            if (client.tableExists("Sessions")) {
                client.executeStatement("DROP TABLE Sessions;");
            }
            client.executeStatement(CREATETABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
