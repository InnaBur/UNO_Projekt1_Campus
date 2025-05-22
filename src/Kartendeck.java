public class Kartendeck {

    Karte[] karten = new Karte[108];

    //es gibt 25 gleich-gefärbt Karten
    // in der Methode kartenErstellen werden 1 Karte "0", 9 Karten "1-9", 3 spez Karten erstellt
    //und dann diese methode wird 2 Mal aufgeführt
    final int COLORED_CARDS = 12;

    //int counter - fuer Platz in Kartendeck (fangt mit 0 an, dann 10 Karten von Farbe erstellen werden,
    // dann counter ist 10, while wir haben schon 10 Platze besetzen und brauchen naechste 10
    public void kartenDeckErstellen() {
        int counter = 0;
        for (int i = 0; i < 2; i++) {
            counter = kartenErstellen('R', counter);
            counter = kartenErstellen('B', counter);
            counter = kartenErstellen('Y', counter);
            counter = kartenErstellen('G', counter);
        }
    }

    public void spezialKartenErstellen(char color, int counter) {

    }

    public int kartenErstellen(char color, int counter) {

        //Karte 0 trifft nur ein mal statt alle andere Karten treffen 2 Mal
        if (counter == 0 || counter == 13 || counter == 26 || counter == 39) {
            karten[counter] = new Karte("" + color + 0, false);
        }
        for (int i = 0; i < COLORED_CARDS; i++) {
            Karte karte = new Karte("" + color + (i + 1), false);
            int currentPlatz = (i + 1) + counter;
            karten[currentPlatz] = karte;
//            currentPlatz += 1;
//            karten[currentPlatz] = karte;
            if (i == 9) {
                karten[(i + 1) + counter] = new Karte(color + "+2", true);
            }
            if (i == 10) {
                karten[(i + 1) + counter] = new Karte(color + "<->", true);
            }
            if (i == 11) {
                karten[(i + 1) + counter] = new Karte(color + "x", true);
            }

        }
        if (counter == 0 || counter == 13 || counter == 26) {
            return counter + COLORED_CARDS + 1; //um schon gemachte 12 Karten zu überspringen (+1 - zum nächste freie Platz im Array)
        }
        return counter + COLORED_CARDS;
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
