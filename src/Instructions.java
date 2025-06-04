public class Instructions {

    public static void printGameInstructions() {

        System.out.println("=== UNO GAME ===\n");

        System.out.println("Goal of the Game:");
        System.out.println("Be the first player to reach 500 points by discarding all your cards in each round.");
        System.out.println("You earn points based on the cards left in your opponents' hands.\n");

        System.out.println(" Game Setup:");
        System.out.println("- Each player receives 7 cards.");
        System.out.println("- The rest form the draw pile.");
        System.out.println("- One card is placed face-up as the discard pile.");
        System.out.println("- The first player is chosen randomly.\n");

        System.out.println(" Gameplay:");
        System.out.println("- You can play a card if it matches the color or number of the top discard.");
        System.out.println("- Special wild card choose color can be played anytime, but wild draw four has rules.");
        System.out.println("- If you can't play, draw one card. If it's playable, you may use it.");
        System.out.println("- Say 'UNO!' when you have one card left. If you forget and get caught, draw 1 penalty card.\n");

        System.out.println(" Action Cards:");
        System.out.println("1. Draw Two: Next player draws 2 cards and skips their turn.");
        System.out.println("2. Reverse: Changes the direction of play.");
        System.out.println("3. Skip: Next player loses their turn.");
        System.out.println("4. Wild Choose the next color to play.");
        System.out.println("5. Wild Draw Four: Choose color + next player draws 4 cards. Only play if you have no matching color.\n");

        System.out.println(" Penalties:");
        System.out.println("- Forgot to say UNO: Draw 1 cards if caught.");
        System.out.println("- Suggesting moves to others: Draw 2 cards.");
        System.out.println("- Playing invalid card: Take the card back + draw 1 penalty card.");
        System.out.println("- Wrong +4 usage: If challenged and guilty, draw 4 cards. If innocent, challenger draws 6 cards.\n");

        System.out.println(" Scoring:");
        System.out.println("- Number cards: Number value.");
        System.out.println("- Draw Two, Reverse and Skip: 20 points each.");
        System.out.println("- Wild Choose Color and Wild Draw Four: 50 points each.");
        System.out.println("- First to 500 points wins.\n");

        System.out.println("Here’s how to enter cards and actions:\n");

        System.out.println("Playing cards:");
        System.out.println("- R5     → Red 5");
        System.out.println("- Rx     → Red Skip card");
        System.out.println("- R<->   → Red Reverse card");
        System.out.println("- +2     → Draw Two card");
        System.out.println("- +4     → Draw Four ");
        System.out.println("- fw     → Choose color\n");

        System.out.println("Player actions:");
        System.out.println("- 1 → Draw a card");
        System.out.println("- 2 → Play a card");
        System.out.println("- 3 → Bluff");
        System.out.println("- 4 → UNO - Last card in hand");
        System.out.println("- 5 → Give advice (e.g., suggest a color)");
        System.out.println("- 6 → Game Instructions");
        System.out.println("- 0 → Exit the game\n");

        System.out.println(" Color codes:");
        System.out.println("- R = Red");
        System.out.println("- G = Green");
        System.out.println("- B = Blue");
        System.out.println("- Y = Yellow");
        System.out.println("- +4 and fw are wild cards\n");

        System.out.println("Tip: Play smart, bluff wisely, and don't forget to say UNO!\n");

        System.out.println("Have fun and good luck!\n");
    }

}
