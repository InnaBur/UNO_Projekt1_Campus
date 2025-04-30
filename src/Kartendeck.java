public class Kartendeck {

    Karte[] karten = new Karte[108];

    //int counter - fuer Platz in Kartendeck (fangt mit 0 an, dann 10 Karten von Farbe erstellen werden,
    // dann counter ist 10, while wir haben schon 10 Platze besetzen und brauchen naechste 10
    public void kartenDeckErstellen() {
        int counter1 = kartenErstellen('R', 0);
        int counter2 = kartenErstellen('B', counter1);
        int counter3 = kartenErstellen('Y', counter2);
        int counter4 = kartenErstellen('G', counter3);
    }

    public void spezialKartenErstellen(char color, int counter) {

    }

    public int kartenErstellen(char color, int counter) {
        for (int i = 0; i < 13; i++) {
            karten[i+counter] = new Karte("" + color + i, false);
            if (i == 10) {
                karten[i+counter] = new Karte(color+"+2", true);
            }
        }
        return counter+10;
    }

    public void karteZiehen() {

    }

    public void kartenDeckMischen() {

    }

    public void printKartendeck() {
        for (Karte karte : karten) {
            System.out.println(karte);
        }
    }

}
