public class Card {

    private String cardName;
    private boolean isSpecial;

    public Card(String cardName, boolean isSpecial) {
        this.cardName = cardName;
        this.isSpecial = isSpecial;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean special) {
        isSpecial = special;
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardName='" + cardName + '\'' +
                ", isSpecial=" + isSpecial +
                '}';
    }

    public String karteToString() {
        return cardName;
    }

}
