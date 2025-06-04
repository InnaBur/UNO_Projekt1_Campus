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


    public boolean isPlayableOn(Card topCard) {
        String thisName = this.cardName;
        String topName = topCard.getCardName();

        // Falls Karte ein Farbwechsel ist, darf sie immer gespielt werden
        if (thisName.contains("CC")){
            return true;
        }

        // Farbe = erstes Zeiche
        char thisColor = thisName.charAt(0);
        char topColor = topName.charAt(0);

        // Wert oder Aktion ( "5", "+2", "<->", "x")
        String thisValue = thisName.substring(1);
        String topValue = topName.substring(1);

        // Spielbar wenn gleiche Farbe oder gleicher Wert/Aktion
        return thisColor == topColor || thisValue.equals(topValue);
    }


}
