import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Diese Klasse kapselt alle Operationen für den Zugriff auf eine SQLite-Datenbank.
 * Sie bietet Methoden zum Erstellen von Verbindungen, Ausführen von SQL-Statements,
 * Abfragen von Daten und Prüfen, ob Tabellen existieren.
 */

public class SqliteClient {

    /** Aktive Verbindung zur SQLite-Datenbank */
    //Die Variable connection wird deklariert,
    //aber zunächst mit null initialisiert,
    // weil die tatsächliche Verbindung zur Datenbank erst später im Konstruktor hergestellt wird:
    private Connection connection = null;

    /**
     * Erstellt eine Verbindung zur SQLite-Datenbank mit dem angegebenen Dateinamen.
     *
     * @param dbName Der Name der SQLite-Datenbankdatei.
     * @throws SQLException Wenn die Verbindung nicht hergestellt werden kann.
     */
    public SqliteClient(String dbName) throws SQLException{
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
    }

    /**
     * Prüft, ob eine Tabelle mit dem angegebenen Namen in der Datenbank existiert.
     *
     * @param tableName Der Name der zu prüfenden Tabelle.
     * @return {@code true}, wenn die Tabelle existiert, sonst {@code false}.
     * @throws SQLException Wenn ein Fehler bei der SQL-Abfrage auftritt.
     */
    public boolean tableExists(String tableName) throws SQLException{
        //Abfrage auf die interne SQLite-Metadaten-Tabelle "sqlite_master"
        //-- Gibt ein true zurueck, wenn eine Tabelle mit dem Namen <tableName> existiert
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='"+ tableName+"';";
        //Prüft, ob die Liste mindestens ein Ergebnis enthält – also ob die Tabelle existiert.
        //Gibt den Wert (true oder false) zurück an den Aufrufer der Methode
        return executeQuery(query).size() > 0;
    }

    /**
     * Führt ein SQL-Statement aus, das keine Rückgabewerte liefert (z.B. CREATE, INSERT, UPDATE, DELETE).
     *
     * @param sqlStatement Das auszuführende SQL-Statement.
     * @throws SQLException Wenn ein Fehler beim Ausführen des Statements auftritt.
     */
    public void executeStatement(String sqlStatement) throws SQLException{
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.
        //der naechste Befehl wird verwendet fuer Aenderungen an der Datenbank
        statement.executeUpdate(sqlStatement);
    }

    /**
     * Führt ein SELECT-Statement aus und gibt das Ergebnis als Liste von HashMaps zurück.
     * Jede Zeile wird als {@code HashMap<String, String>} gespeichert,
     * der Schlüssel ist der Spaltenname und der Wert  ist der Zellinhalt.
     *
     * @param sqlQuery Das SELECT-Statement.
     * @return Eine {@code ArrayList<HashMap<String, String>>} mit allen Ergebniszeilen.
     * @throws SQLException Wenn ein Fehler beim Ausführen der Abfrage erscheint.
     */
    public ArrayList<HashMap<String, String>> executeQuery(String sqlQuery) throws SQLException{

        //wird ein Select ausgefuehrt und die Spaltenanzahl und Metadaten aufgerufen
        Statement statement = connection.createStatement();
        // Setzt das Timeout für die SQL-Abfrage auf 30 Sekunden.
        // Wenn das Statement länger als 30 Sek. braucht, wird es automatisch abgebrochen.
        // Beispiel: Eine langsame Abfrage auf eine große Tabelle wird nach 30 Sekunden gestoppt.
        //  SELECT * FROM Sessions WHERE Player LIKE '%Anna%' -- bei sehr vielen Einträgen

        statement.setQueryTimeout(30);  // Timeout auf 30 sec.
        ResultSet rs = statement.executeQuery(sqlQuery);
        //Abruf der MetaDaten
        ResultSetMetaData rsmd = rs.getMetaData();
        //Abruf der Spaltenanzahl
        int columns = rsmd.getColumnCount();

        //Es wird Zeile fuer Zeile durch das ResultSet "rs" durchgegangen
        //Fuer jede Zeile wird ein HashMap<String, String> erstellt
        //Key = Spaltenname | Value = Datenwert
        //Alle Zeilen werden anschließend in einer ArrayList gesammelt und zurueckgegeben
        ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
        while(rs.next())
        {
            HashMap<String, String> map = new HashMap<String, String>();
            for (int i = 1; i <= columns; i++) {
                String value = rs.getString(i);
                String key = rsmd.getColumnName(i);
                map.put(key, value);
            }
            result.add(map);
        }
        //Rueckgabe der ArrayList
        return result;
    }
}
