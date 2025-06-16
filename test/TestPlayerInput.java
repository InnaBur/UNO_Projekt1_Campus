import org.junit.Assert;
import org.junit.Test;

public class TestPlayerInput {


    @Test
    public void testGetCardByNameIgnoreCase() {
        Player player = new Player("Test", true);
        Card card = new Card("R5", false);
        player.addCard(card);

        //Benutzereingabe mit kleingeschriebenem Kartennamen ("r5")
        Card result = player.getCardByName("r5");

        Assert.assertNotNull("Should find the card regardless of case", result);
        // ist gefundene Karte tatsächlich die erwartete? ("R5")
        Assert.assertEquals("R5", result.getCardName());
    }


}
