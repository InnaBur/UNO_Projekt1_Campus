import java.util.ArrayList;

public class Player {

    private String name;
    private boolean isBot;
    private int points;

    private ArrayList<Card> cardsInHand;

    public Player(String name, boolean isBot) {
        this.name = name.toUpperCase(); // Spielername wird beim Erstellen großgeschrieben
        this.isBot = isBot;
        this.cardsInHand = new ArrayList<>();
        this.points = 0;
    }

    public Player() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toUpperCase(); // Spielername wird beim Erstellen großgeschrieben
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

    public void setCardsInHand(ArrayList<Card> cardsInHand) {
        this.cardsInHand = cardsInHand;
    }

    public Card addCard(Card card) {
        cardsInHand.add(card);
        return card;
    }

    public void addAllCards(ArrayList<Card> cards) {
        cardsInHand.addAll(cards);
    }

    public void showHand() {
        // System.out.print(name + ", Your cards are: ");
        // Schwarzer Text (30), Cyan Hintergrund (46)
        System.out.print("\u001B[30;46m[" + name + "]\u001B[0m, Your cards are: ");
        for (Card x : cardsInHand) {
            String colorCardOutput = CardsDeck.createColoredOutputForCard(x.getCardName());  // Farbige Darstellung
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

    public void setPoints(int points) {
        this.points = points;
    }

    //If your game loop does not recreate the Player objects each round, and instead uses the same Player instances, then the added points will stay with the player.
    public void addPoints(int p) {
        this.points += p;
    }

}
