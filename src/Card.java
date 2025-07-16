/**
 * Die Klasse Card repräsentiert eine UNO-Karte.
 * Eine Karte hat einen Namen (z.B. "R5", "+2", "CC")
 * und kann eine Spezialkarte sein.
 */

public class Card {
    /** Der Name der Karte (z.B. "R5", "G+2", "CC"). */
    private String cardName;
    /** true, wenn die Karte eine Spezialfunktion hat  (+2, Richtungswechsel, CC, etc.). */
    private boolean isSpecial;

    /**
     * Konstruktor: Erstellt eine neue Karte mit Name und Spezialstatus.
     *
     * @param cardName  Kartenname (z.B. "R5", "CC").
     * @param isSpecial true, wenn Spezialkarte.
     */
    public Card(String cardName, boolean isSpecial) {
        this.cardName = cardName.toUpperCase();
        this.isSpecial = isSpecial;
    }

    /**
     * Getters und Setters
     */

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName.toUpperCase();
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    /**
     * Gibt eine textuelle Darstellung der Karte zurück.
     *
     * @return Kartenname und Spezialstatus.
     */

    @Override
    public String toString() {
        return "Card{" +
                "cardName='" + cardName + '\'' +
                ", isSpecial=" + isSpecial +
                '}';
    }

    /**
     * Prüft, ob diese Karte auf die übergebene Karte gespielt werden darf.
     * Farbwechsel ("CC") und Zieh-vier ("+4") dürfen immer gespielt werden.
     * Ansonsten gilt: gleiche Farbe oder gleicher Wert/Aktion.
     *
     * @param topCard Die aktuelle Karte oben auf dem Ablagestapel.
     * @return true, wenn spielbar.
     */
    public boolean isPlayableOn(Card topCard) {
        String thisName = this.cardName.toUpperCase();
        String topName = topCard.getCardName().toUpperCase();

        // Falls Karte ein Farbwechsel ist, darf sie immer gespielt werden
        // +4 darf auch immer gelegt werden (obwohl gegen regel), damit der bluff funktioniert
        if (thisName.contains("CC") || thisName.contains("+4")){
            return true;
        }

        // Farbe = erstes Zeiche
        char thisColor = thisName.charAt(0);
        char topColor = topName.charAt(0);

        // Wert oder Aktion ("5", "+2", "d", "x")
        String thisValue = thisName.substring(1);
        String topValue = topName.substring(1);

        // Spielbar wenn gleiche Farbe oder gleicher Wert/Aktion
        return thisColor == topColor || thisValue.equals(topValue);
    }
}
