import java.util.ArrayList;

public class Player {

    private String name;
    private boolean isBot;

    //punkte!!

    private ArrayList<Card> cardsInHand;

    public Player(String name, boolean isBot) {
        this.name = name;
        this.isBot = isBot;
        this.cardsInHand = new ArrayList<>();
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

    public void addCard(Card card) {
        cardsInHand.add(card);
    }

    public void setCardsInHand(ArrayList<Card> cardsInHand) {
        this.cardsInHand = cardsInHand;
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

}
