import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;


//In dieser Klasse werden alle Operationen fuer den Zugriff auf eine SQLite-DB gekapselt
public class SqliteClient {

    //"connection" ist eine aktive Datenbankverbindung
    private Connection connection = null;

    //Der Konstruktor oeffnet eine Verbindung zur SQLite-Datenbank mit dem Dateinamen "dbName"
    //-- Die JDBC-URL für SQLite ist "jdbc:sqlite:<Dateiname>"
    //-- Sollte etwas schief gehe beim Verbinden, wird eine SQLException geworfen
    public SqliteClient(String dbName) throws SQLException{
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
    }

    //Hier wird geprueft ob eine Tabelle existiert
    public boolean tableExists(String tableName) throws SQLException{
        //Abfrage auf die interne SQLite-Metadaten-Tabelle "sqlite_master"
        //-- Gibt ein true zurueck, wenn eine Tabelle mit dem Namen <tableName> existiert
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='"+ tableName+"';";
        return executeQuery(query).size() > 0;
    }

    //Hier werden beliebige SQL-Statements ausgefuehrt, welche nichts zurueckgeben / Kein Return
    //-- z.B. CREATE TABLE, ISNERT, UPDATE, DELETE
    public void executeStatement(String sqlStatement) throws SQLException{
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.
        //der naechste Befehl wird verwendet fuer Aenderungen an der Datenbank
        statement.executeUpdate(sqlStatement);
    }

    //Hier werden nun SELECT-Statements ausgefuehrt und eine ArrayList zurueckgegeben
    public ArrayList<HashMap<String, String>> executeQuery(String sqlQuery) throws SQLException{

        //Nachfolgend wird ein Select ausgefuehrt und die Spaltenanzahl und Metadaten aufgerufen
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.
        ResultSet rs = statement.executeQuery(sqlQuery);
        //Abruf der MetaDaten
        ResultSetMetaData rsmd = rs.getMetaData();
        //Abruf der Spaltenanzahl
        int columns = rsmd.getColumnCount();

        //Nun wird Zeile fuer Zeile durch das ResultSet "rs" durchgegangen
        //-- Fuer jede Zeile wird ein HashMap<String, String> erstellt
        //-- Key = Spaltenname | Value = Datenwert
        //-- Alle Zeilen werden anschließend in einer ArrayList gesammelt und zurueckgegeben!
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
