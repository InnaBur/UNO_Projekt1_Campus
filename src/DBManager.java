import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DBManager {

    private static final String CREATETABLE = "CREATE TABLE Sessions (Player varchar(100) NOT NULL, Session int NOT NULL, Round int NOT NULL, Score int NOT NULL, CONSTRAINT PK_Sessions PRIMARY KEY (Player, Session, Round));";

    //Vorlage zum Einfuegen von Daten
    private static final String INSERT_TEMPLATE = "INSERT INTO Sessions (Player, Session, Round, Score) VALUES ('%1s', %2d, %3d, %4d);";

    //Sucht "Score" eines Spielers in einer Session
    private static final String SELECT_BYPLAYERANDSESSION = "SELECT Player, SUM(Score) AS Score FROM Sessions WHERE Player = '%1s' AND Session = %2d;";
    private static final String SELECT_BY_SESSION = "SELECT Player, SUM(Score) AS Score FROM Sessions WHERE Session = 1;";
    static String selectAllScores = "SELECT Player, SUM(Score) AS Score FROM Sessions WHERE Session = 1 GROUP BY Player;";

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

    public static void addDatenIntoDB(ArrayList<Player> players, int session, int round, SqliteClient client) throws SQLException {
        client.executeStatement("DELETE FROM Sessions WHERE Session = " + session + ";");

        for (Player player : players) {
            client.executeStatement(String.format(INSERT_TEMPLATE, player.getName(), session, round, player.getPoints()));
        }
    }

    public static void takeDatenFromDB(int session, SqliteClient client) {
        try {
            String selectAllScores = "SELECT Player, SUM(Score) AS Score FROM Sessions WHERE Session = " + session + " GROUP BY Player;";
            ArrayList<HashMap<String, String>> results = client.executeQuery(selectAllScores);

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
