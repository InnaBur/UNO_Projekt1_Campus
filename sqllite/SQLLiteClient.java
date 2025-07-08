import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class SQLLiteClient {

    private Connection connection = null; // Verbindung zur SQLite-Datenbank

    // Konstruktor: Stellt eine Verbindung zur SQLite-Datenbank mit dem angegebenen Namen her
    public SQLLiteClient(String dbName) throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
    }

    // Prüft, ob eine bestimmte Tabelle in der Datenbank existiert
    public boolean tableExists(String tableName) throws SQLException {
        // SQL-Abfrage an das sqlite_master-System-Objekt, das Metadaten über Tabellen enthält
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "';";
        // Führt die Abfrage aus und prüft, ob ein Ergebnis zurückkommt
        return this.executeQuery(query).size() > 0;
    }

    // Führt ein SQL-Statement (z.B. CREATE, INSERT, UPDATE, DELETE) aus
    //TODO:zu DatabaseHelper vergleichen: in init auch create Table
    public void executeStatement(String sqlStatement) throws SQLException {
        Statement statement = this.connection.createStatement(); // Erstellt ein Statement-Objekt
        //Sie gibt an, wie lange die Datenbank maximal versuchen soll,
        // die Abfrage oder das Statement auszuführen,
        // bevor es abgebrochen wird – in diesem Fall nach 30 Sekunden.
        statement.setQueryTimeout(30);                            // Setzt die Timeout-Zeit in Sekunden
        statement.executeUpdate(sqlStatement);                    // Führt das Statement aus
    }

    // Führt eine SQL-SELECT-Abfrage aus und gibt die Ergebnisse als Liste von HashMaps zurück
    public ArrayList<HashMap<String, String>> executeQuery(String sqlQuery) throws SQLException {
        Statement statement = this.connection.createStatement();  // Erstellt ein Statement
        statement.setQueryTimeout(30);                            // Setzt das Timeout auf 30 Sekunden
        ResultSet rs = statement.executeQuery(sqlQuery);          // Führt die SELECT-Abfrage aus und speichert das Ergebnis
        ResultSetMetaData rsmd = rs.getMetaData();                // Ruft Metadaten über die Spalten ab
        int columns = rsmd.getColumnCount();                      // Bestimmt die Anzahl der Spalten im Ergebnis
        ArrayList<HashMap<String, String>> result = new ArrayList(); // Liste für alle Zeilen

        // Schleife über alle Zeilen im ResultSet
        while (rs.next()) {
            HashMap<String, String> map = new HashMap(); // Erstellt eine neue Map für die aktuelle Zeile

            // Schleife über alle Spalten einer Zeile
            for (int i = 1; i <= columns; ++i) {
                String value = rs.getString(i);              // Holt den Spaltenwert
                String key = rsmd.getColumnName(i);          // Holt den Spaltennamen
                map.put(key, value);                         // Fügt Spaltenname und -wert in die Map ein
            }

            result.add(map); // Fügt die Map der Ergebnisliste hinzu
        }

        return result; // Gibt die Liste aller Zeilen zurück
    }
}




