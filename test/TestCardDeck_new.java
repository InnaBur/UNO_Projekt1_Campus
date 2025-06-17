import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;


 /** In dieser Klasse wird das Kartendeck überprüft */

public class TestCardDeck_new {


   //Test des Default-Konstruktors
    @Test
    public void testDefaultConstructor() {
        CardsDeck deck = new CardsDeck(); //Neue Instanze der Klasse CardsDeck
        //Hier wird ein Kartendeck mit 108 Karten erstellt

        //1. Prüfe, ob das Deck nicht leer ist //bzw. nicht inizialisiert
        assertNotNull(deck.getCardsDeck());
        System.out.println("Kartendeck ist initialisiert");
        assertFalse(deck.getCardsDeck().isEmpty());
        System.out.println("Kartendeck ist nicht leer");

        //2. Prüfe, ob die erwartete Anzahl an Karten vorhanden ist

        int expectedCardCount = 108; // UNO Standard
        assertEquals(expectedCardCount, deck.getCardsDeck().size());
        System.out.println("Excpected Cards Count: "  + expectedCardCount);
        System.out.println("Fact Cards Count: "  + deck.getCardsDeck().size());


        //3. Sinnvolle UNO-Karten zum Überprüfen, ob sie im Kartendeck sind:
        checkCard(deck, "+4", true, "Wild Draw Four (+4)");
        checkCard(deck, "CC", true, "Wild (Farbwahl)");
        checkCard(deck, "R+2", true, "Rot +2");
        checkCard(deck, "GD", true, "Grün Richtungswechsel (Reverse)");
        checkCard(deck, "Bx", true, "Blau Aussetzen (Skip)");
        checkCard(deck, "Y0", false, "Gelb 0");
        checkCard(deck, "R8", false, "Rot 8");

    }

    //TODO:
    //Eventuell nicht nötig, weil Konstruktor noch nicht benutzt ist
     // public CardsDeck(ArrayList<Card> cardsDeck) {
     //        this.cardsDeck = cardsDeck;
     //    }

     //Test Konstruktor mit Parameters

     @Test
     public void testConstructorWithParameter() {
         // Schritt 1: Testdaten vorbereiten
         // Erstelle eine neue Liste von Karten als Testdaten
         ArrayList<Card> testList = new ArrayList<>();
         testList.add(new Card("1", false));
         testList.add(new Card("2", false));

         System.out.println("Testdaten vorbereitet: " + testList);

         // Schritt 2: Konstruktor aufrufen
         // Erzeuge ein neues CardsDeck-Objekt mit der vorbereiteten Liste
         CardsDeck deck = new CardsDeck(testList);
         System.out.println("CardsDeck mit Testdaten erzeugt.");

         // Schritt 3: Überprüfung, ob die Kartenliste korrekt übernommen wurde
         // Erwartet wird, dass das interne cardsDeck exakt der übergebenen testList entspricht

         assertEquals("Die Kartenliste im Deck sollte gleich der übergebenen Liste sein.", testList, deck.getCardsDeck());
         System.out.println("Übergebene Liste wurde übernommen.");

         // Schritt 4: Test auf Referenzübernahme
         // Ändere die ursprüngliche Liste und prüfe, ob sich das auf das Deck auswirkt
         //Das funktioniert nur, wenn equals() korrekt überschrieben ist – denn contains() verwendet intern equals() zum Vergleich.
         //deswegen in der Klasse Card.java wurde die Methode equals überschrieben
         testList.add(new Card("3", false));
         System.out.println("Karte '3' zur ursprünglichen Liste hinzugefügt.");
         System.out.println("Deck enthält jetzt: " + deck.getCardsDeck());

         // Erwartet wird, dass die neue Karte auch im Deck vorhanden ist,
         // da der Konstruktor keine Kopie der Liste angelegt hat, sondern die Referenz übernommen wurde
         assertTrue("Deck sollte auch die nachträglich hinzugefügte Karte enthalten.", deck.getCardsDeck().contains(new Card("3", false)));
         System.out.println("Deck enthält die neue Karte '3'.");
     }


     //Prüfen, ob die benötige Karten im Kartendeck sind:
    private void checkCard(CardsDeck deck, String cardName, boolean isSpecial, String description) {
        boolean contains = deck.getCardsDeck().contains(new Card(cardName, isSpecial));
        System.out.println("Enthält Deck eine '" + cardName + "' Karte (" + description + ")?:" + contains);
        assertTrue("Deck sollte eine '" + cardName + "' Karte (" + description + ") enthalten", contains);
    }
}
