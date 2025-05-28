import java.util.ArrayList;

public class Spieler {

    private String name;
    private boolean isBot;

    private ArrayList<Karte> kartenInDerHand;

    public Spieler(String name, boolean isBot) {
        this.name = name;
        this.isBot = isBot;
        this.kartenInDerHand = new ArrayList<>();
    }

    public Spieler() {
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

    public ArrayList<Karte> getKartenInDerHand() {
        return kartenInDerHand;
    }

    public void addKarte(Karte karte) {
        kartenInDerHand.add(karte);
    }

    public void setKartenInDerHand(ArrayList<Karte> kartenInDerHand) {
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
