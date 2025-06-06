import java.util.ArrayList;

public class Player {

    private String name;
    private boolean isBot;

    //punkte!!
    private int points;

    private ArrayList<Card> cardsInHand;

    public Player(String name, boolean isBot) {
        this.name = name;
        this.isBot = isBot;
        this.cardsInHand = new ArrayList<>();
        this.points =0;
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

    public ArrayList<Card> getCardsInHand() {
        return cardsInHand;
    }

    public Card addCard(Card card) {
        cardsInHand.add(card);
        return card;
    }

    public void setCardsInHand(ArrayList<Card> cardsInHand) {
        this.cardsInHand = cardsInHand;
    }

    public void showHand() {
        System.out.print(name + ", Your cards are: ");
        for (Card x: cardsInHand) {
            String colorCardOutput = CardsDeck.getColoredCard(x.getCardName());  // Farbige Darstellung
            System.out.print("[" + colorCardOutput + "] ");
        }
        System.out.println("\u001B[0m\n"); // Reset am Ende, weil letzte Karte in Farbe ist
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", isBot=" + isBot +
                '}';
    }

    public Card getCardByName(String name) {
        for (Card c : cardsInHand) {
            if (c.getCardName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }

    public void removeCard(Card card) {
        cardsInHand.remove(card);
    }


    // Punktesystem:
    public int getPoints() {
        return points;
    }

    public void addPoints(int p) {
        this.points += p;
    }

}
