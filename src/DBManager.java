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


    public static void addDatenIntoDB(ArrayList<Player> players, int session, int round) {
        try {
            SqliteClient client = new SqliteClient("uno.sqlite");

            if (client.tableExists("Sessions")) {
                client.executeStatement("DROP TABLE Sessions;");
            }
            //Tabelle wird hier neu erstellt
            client.executeStatement(CREATETABLE);

            for (Player player : players) {
                client.executeStatement(String.format(INSERT_TEMPLATE, player.getName(), session, round, player.getPoints()));
            }
//            client.executeStatement(String.format(INSERT_TEMPLATE, "Hans", 1, 1, 0));
//            client.executeStatement(String.format(INSERT_TEMPLATE, "Anita", 1, 2, 20));
//            client.executeStatement(String.format(INSERT_TEMPLATE, "Hans", 1, 2, 100));
            ArrayList<HashMap<String, String>> results = new ArrayList<>();

            for (Player player: players) {
                results = client.executeQuery(String.format(SELECT_BYPLAYERANDSESSION, player.getName(), 1));
                HashMap<String, String> map = results.get(0);
                System.out.println(map.get("Player") + " hat derzeit:  " + map.get("Score") + " Punkte");
                }

//            for (HashMap<String, String> map : results) {
//                System.out.println(map.get("Player") + " hat derzeit:  " + map.get("Score") + " Punkte");
//            }

        } catch (SQLException ex) {
            System.out.println("Ups! Something went wrong:" + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
