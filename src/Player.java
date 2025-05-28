import java.util.ArrayList;

public class Player {

    private String name;
    private boolean isBot;

    private ArrayList<Card> kartenInDerHand;

    public Player(String name, boolean isBot) {
        this.name = name;
        this.isBot = isBot;
        this.kartenInDerHand = new ArrayList<>();
    }

    public Player() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBot() {
        return isBot;
    }

    public void setBot(boolean bot) {
        isBot = bot;
    }

    public ArrayList<Card> getKartenInDerHand() {
        return kartenInDerHand;
    }

    public void addKarte(Card karte) {
        kartenInDerHand.add(karte);
    }

    public void setKartenInDerHand(ArrayList<Card> kartenInDerHand) {
        this.kartenInDerHand = kartenInDerHand;
    }


    @Override
    public String toString() {
        return "Spieler{" +
                "name='" + name + '\'' +
                ", isBot=" + isBot +
                '}';
    }
}
