import java.util.ArrayList;

/**
 * Die Klasse Card repräsentiert eine UNO-Karte.
 * Jede Karte hat einen Namen (z.B. "R5", "B+2", "CC", "+4") und kann speziell sein
 * (z.B. +2, Richtungswechsel, Farbwahl).
 */

public class Card {

    /**
     * CardName: Der Name der Karte, wie "R5" für rote 5, "G+2" für grün +2, "CC" für Farbwahl, "+4" für Zieh-vier.
     * isSpecial: Gibt an, ob es sich um eine Spezialkarte handelt ("+2", "CC", Richtungswechsel).
     */
    private String cardName;
    private boolean isSpecial;

    /**
     * Konstruktor: Erstellt eine neue Karte mit Namen und Spezialstatus.
     * Der Name wird automatisch in Großbuchstaben umgewandelt.
     *
     * @param cardName  Der Name der Karte (z.  B. "R5", "CC", "+4").
     * @param isSpecial true, wenn es sich um eine Spezialkarte handelt.
     */

    public Card(String cardName, boolean isSpecial) {
        this.cardName = cardName.toUpperCase();
        this.isSpecial = isSpecial;
    }

    /**
     * Getter: Gibt den Namen der Karte zurück.
     *
     * @return Der Kartenname in Großbuchstaben.
     */
    public String getCardName() {
        return cardName;
    }

    /**
     * Setter: Setzt den Namen der Karte.
     * Der Name wird automatisch in Großbuchstaben umgewandelt.
     *
     * @param cardName Der neue Kartenname.
     */
    public void setCardName(String cardName) {
        this.cardName = cardName.toUpperCase();
    }

    /**
     * Getter: Gibt zurück, ob die Karte eine Spezialkarte ist.
     *
     *  @return true, wenn Spezialkarte, sonst false.
     */

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean special) {
        isSpecial = special;
    }

    /**
     * die übergeschriebene Methode "toString"
     *  Gibt eine Textdarstellung der Karte für z.B. Debugging  zurück.
     *
     * @return String mit Kartenname und Spezialstatus.
     */
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


    /**
     * die Methode "isPlayableOn"
     * Prüft, ob diese Karte auf eine andere (obere) Karte gespielt werden darf.
     * Farbwahl ("CC") und Zieh-vier ("+4") dürfen immer gelegt werden.
     * Ansonsten gilt: gleiche Farbe oder gleicher Wert/Aktion.
     *
     * @param topCard Die aktuell oberste Karte auf dem Ablagestapel.
     * @return true, wenn spielbar.
     */

    public boolean isPlayableOn(Card topCard) {
        String thisName = this.cardName.toUpperCase();
        String topName = topCard.getCardName().toUpperCase();

        // Falls Karte ein Farbwechsel ist, darf sie immer gespielt werden
        if (thisName.contains("CC") || thisName.contains("+4")) { // +4 darf auch immer gelegt werden (obwohl gegen regel), damit der bluff funktioniert
            return true;
        }

        // Farbe = erstes Zeichen (z.B. 'R' für Rot, 'G' für Grün)

        char thisColor = thisName.charAt(0);
        char topColor = topName.charAt(0);

        // Wert oder Aktion ("5", "+2", "d", "x")
        String thisValue = thisName.substring(1);
        String topValue = topName.substring(1);

        // Spielbar wenn gleiche Farbe oder gleicher Wert/Aktion
        return thisColor == topColor || thisValue.equals(topValue);
    }
}
