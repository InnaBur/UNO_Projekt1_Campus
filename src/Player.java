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

    public Card addCard(Card card) {
        cardsInHand.add(card);
        return card;
    }

    public void setCardsInHand(ArrayList<Card> cardsInHand) {
        this.cardsInHand = cardsInHand;
    }

    public void showHand() {
        System.out.print(name + ", You have these cards: ");
        for (Card karte: cardsInHand) {
            System.out.print(karte.getCardName() + " ");
        }
        System.out.println("\n");
    }
    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", isBot=" + isBot +
                '}';
    }
}
