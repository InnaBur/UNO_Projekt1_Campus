public class Kartendeck {

    //es gibt 25 gleich-gefärbt Karten
    // in der Methode kartenErstellen werden 1 Karte "0", 9 Karten "1-9", 3 spez Karten erstellt
    //und dann diese methode wird 2 Mal aufgeführt
    final int COLORED_CARDS = 12;
    Karte[] karten = new Karte[108];

    //int counter - fuer Platz in Kartendeck (fangt mit 0 an, dann 10 Karten von Farbe erstellen werden,
    // dann counter ist 10, while wir haben schon 10 Platze besetzen und brauchen naechste 10
    public void kartenDeckErstellen() {

        // Karten von 1 bis 9 und drei Sondere Karten jede Farbe zwei Mal erstellen wurden,
        // 0 jede Farbe - nur ein Mal

        int counter = 0; // erstes Counter ist 0 - ab erste Platz des Arrays, dann es sich immer veraendert
        for (int i = 0; i < 2; i++) {
            counter = kartenErstellen('R', counter);
            counter = kartenErstellen('B', counter);
            counter = kartenErstellen('Y', counter);
            counter = kartenErstellen('G', counter);
        }

        //4+ Karten und 4 karten Farbwechsel werden erstellt
        counter = spezialKartenErstellen("+4", counter);
        spezialKartenErstellen("fw", counter);


    }

    //4 Karten +4 und 4 karten Farbwechsel werden erstellt
    public int spezialKartenErstellen(String kartenName, int counter) {
        for (int i = 0; i < 4; i++) {
            int currentPlatz = counter + i+1;
            karten[currentPlatz] = new Karte(kartenName, true);
        }
        return counter + 4; //weil 4 Karten erstellt wurden
    }

    public int kartenErstellen(char color, int counter) {

        //Karte 0 trifft nur ein mal statt alle andere Karten treffen 2 Mal
        if (counter == 0 || counter == 13 || counter == 26 || counter == 39) {
            karten[counter] = new Karte("" + color + 0, false);
        }

        for (int i = 0; i < COLORED_CARDS; i++) {
            Karte karte = new Karte("" + color + (i + 1), false);

            //Platz für die nächste Karte (i+1 - weil wir schon haben Karte auf 0-Stelle)
            int currentPlatz = (i + 1) + counter;
            karten[currentPlatz] = karte;

            // Platz für spezielle Karten dieser Farbe
            if (i == 9) {
                karten[(i + 1) + counter] = new Karte(color + "+2", true); //+2 karten ziehen
            }
            if (i == 10) {
                karten[(i + 1) + counter] = new Karte(color + "<->", true); //Reihenfolge wechseln
            }
            if (i == 11) {
                karten[(i + 1) + counter] = new Karte(color + "x", true); //einen Umzug überspringen
            }

        }
        if (counter == 0 || counter == 13 || counter == 26 || counter == 99) {
            return counter + COLORED_CARDS + 1; //um schon gemachte 12 Karten zu überspringen (+1 - zum nächste freie Platz im Array)
        }
        return counter + COLORED_CARDS;
    }


    public void karteZiehen() {

    }

    public void kartenDeckMischen() {

    }

    public void printKartendeck() {
        int count = 0;
        for (Karte karte : karten) {
            System.out.println(karte);
            count++;
        }
    }

}
