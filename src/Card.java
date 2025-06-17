public class Card {

    private String cardName;
    private boolean isSpecial;

    public Card(String cardName, boolean isSpecial) {
        this.cardName = cardName.toUpperCase();
        this.isSpecial = isSpecial;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName.toUpperCase();
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean special) {
        isSpecial = special;
    }


    //eine Methode, um die Karten zu vergleichen bzw. in Test eine bestimmte Karte im Kartendeck suchen
   // Mit equals wird geprüft, ob zwei Objekte inhaltlich gleich sind (nicht nur, ob es dieselbe Referenz ist).

    @Override
    public boolean equals(Object o) {
        // 1. Wenn die beiden Objekte dieselbe Referenz haben, sind sie gleich
        if (this == o) return true;

        // 2. Wenn das andere Objekt null ist oder nicht vom selben Typ, sind sie ungleich
        if (o == null || getClass() != o.getClass()) return false;

        // 3. Das andere Objekt wird in ein Card-Objekt umgewandelt
        Card card = (Card) o;

        // 4. Vergleiche die beiden wichtigen Felder: isSpecial und cardName
        //Vergleicht das Attribut isSpecial und auch cardName der aktuellen Karte (this) mit dem Attribut isSpecial der anderen Karte (card).
        return isSpecial == card.isSpecial && cardName.equals(card.cardName);
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
        String thisName = this.cardName.toUpperCase();
        String topName = topCard.getCardName().toUpperCase();

        // Falls Karte ein Farbwechsel ist, darf sie immer gespielt werden
        if (thisName.contains("CC") || thisName.contains("+4")){ // +4 darf auch immer gelegt werden (obwohl gegen regel), damit der bluff funktioniert
            return true;
        }

        // Farbe = erstes Zeiche
        char thisColor = thisName.charAt(0);
        char topColor = topName.charAt(0);

        // Wert oder Aktion ( "5", "+2", "d", "x")
        String thisValue = thisName.substring(1);
        String topValue = topName.substring(1);

        // Spielbar wenn gleiche Farbe oder gleicher Wert/Aktion
        return thisColor == topColor || thisValue.equals(topValue);
    }


}
