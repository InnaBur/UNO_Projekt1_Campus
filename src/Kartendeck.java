import java.util.ArrayList;
import java.util.Collections;

public class Kartendeck {

    static final int ANZAHL_DER_KARTEN_IN_DER_HAND = 7;
    //array für Farben-Buchstaben
    static final char[] COLORS = {'R', 'B', 'Y', 'G'};
    final int ANZAHL_DER_SPIELER = 4;
    //Kartendeck list
    private ArrayList<Karte> karten;

    public Kartendeck() {
        this.karten = new ArrayList<>();
        kartenDeckErstellen();
        kartenDeckMischen();
    }

    public ArrayList<Karte> getKarten() {
        return karten;
    }

    //верхня карта з колоди
    public Karte getObereKarte() {
        return karten.get(0);
    }

    //верхня карта з колоди як її бачить гравець
    public String zeigenObereKarte() {
        return karten.get(0).getKarteName();
    }
    public void setKarten(ArrayList<Karte> karten) {
        this.karten = karten;
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


    public void karteZiehen() {

    }

    public void kartenDeckMischen() {
        Collections.shuffle(karten);
    }

    public void kartenAusteilen(Spieler[] spielers) {

        for (int i = 0; i < ANZAHL_DER_SPIELER; i++) {
            for (int j = 0; j < ANZAHL_DER_KARTEN_IN_DER_HAND; j++) {
                spielers[i].addKarte(karten.get(0));
                karten.remove(0);
            }
        }
    }

    public void spielersKartenZeigen(Spieler spieler) {
        System.out.print(spieler.getName() + ", Sie haben diese Karten: ");
        for (Karte karte: spieler.getKartenInDerHand()) {
            System.out.print(karte.getKarteName() + " ");
        }
        System.out.println("\n");
    }


    //Diese Methode ist nur für zwischen Testung. Muss gelöscht werden
    public void printKartendeck() {
        int count = 0;
        for (Karte karte : karten) {
            System.out.println(karte);
            count++;
        }
        System.out.println(count);
    }

}
