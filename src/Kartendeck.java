import java.util.ArrayList;

public class Kartendeck {

    //es gibt 25 gleich-gefärbt Karten
    // in der Methode kartenErstellen werden 1 Karte "0", 9 Karten "1-9", 3 spez Karten erstellt
    //und dann diese methode wird 2 Mal aufgeführt
    static final int COLORED_CARDS = 12;
    static final int ANZAHL_DER_KARTEN_IN_DER_HAND = 7;
    //array für Farben-Buchstaben
    static final char[] COLORS = {'R', 'B', 'Y', 'G'};
    final int ANZAHL_DER_SPIELER = 4;
    //Kartendeck list
    ArrayList<Karte> karten;

    public Kartendeck() {
        this.karten = new ArrayList<>();
        kartenDeckErstellen();
    }


    public void kartenDeckErstellen() {

        // Karten von 1 bis 9 und drei Sondere Karten jede Farbe zwei Mal erstellen wurden,
        // Karte '0' jede Farbe wird nur ein Mal erstellen
        for (char color : COLORS) {
            nullKartenErstellen(color);
            for (int i = 0; i < 2; i++) {
                bunteKartenErstellen(color);
            }
        }

        //4+ Karten und 4 karten Farbwechsel werden erstellt
        spezialeSchwarzeKartenErstellen("+4");
        spezialeSchwarzeKartenErstellen("fw");
    }

    //4 Karten +4 und 4 karten Farbwechsel werden erstellt
    public void spezialeSchwarzeKartenErstellen(String kartenName) {
        for (int i = 0; i < 4; i++) {
            karten.add(new Karte(kartenName, true));
        }
    }

    public void bunteKartenErstellen(char color) {

        for (int i = 0; i < 9; i++) {
            Karte karte = new Karte("" + color + (i + 1), false);
            karten.add(karte);
        }
        karten.add(new Karte(color + "+2", true));  //+2 karten ziehen
        karten.add(new Karte(color + "<->", true));  //Reihenfolge wechseln
        karten.add(new Karte(color + "x", true));  //einen Umzug überspringen
    }

    public void nullKartenErstellen(char color) {
        karten.add(new Karte("" + color + 0, false));
    }

    //Diese Methode ist nur für zwischen Testung. Muss gelöscht werden
    public void printKartendeck() {
        for (Karte karte : karten) {
            System.out.println(karte);
        }
    }

}
