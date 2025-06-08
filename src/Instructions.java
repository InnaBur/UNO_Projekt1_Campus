public class Instructions {

    public static void printGameInstructions() {

        final String RED = "\u001B[31m";
        final String GREEN = "\u001B[32m";
        final String YELLOW = "\u001B[33m";
        final String BLUE = "\u001B[34m";
        final String PURPLE = "\u001B[35m";
        final String CYAN = "\u001B[36m";
        final String BOLD = "\u001B[1m";
        final String RESET = "\u001B[0m";

        System.out.println("\n/***********************************\\");
        System.out.println("*                                   *");
        System.out.println("*          === UNO GAME ===         *");
        System.out.println("*                                   *");
        System.out.println("\\***********************************/\n");

        System.out.println(BOLD + "Goal of the Game:" + RESET);
        System.out.println("Be the first player to reach " + BOLD + "500 points" + RESET + " by discarding all your cards in each round.");
        System.out.println("You earn points based on the cards left in your opponents' hands.\n");

        System.out.println(BOLD + " Game Setup:" + RESET);
        System.out.println("You can play with up to 4 players – a mix of live players and bots is allowed.\n");
        System.out.println("Examples of valid combinations:");
        System.out.println("- 1 Live Player + 3 Bots");
        System.out.println("- 2 Live Players + 2 Bots");
        System.out.println("- 3 Live Players + 1 Bot");
        System.out.println("- 4 Live Players (no bots)\n");

        System.out.println("- Each player receives 7 cards.");
        System.out.println("- The rest form the draw pile.");
        System.out.println("- One card is placed face-up as the discard pile.");
        System.out.println("- The first player is chosen randomly.");
        System.out.println("- Players take turns in " + CYAN + "clockwise or counter-clockwise" + RESET + " order, depending on the game direction.\n");

        System.out.println(BOLD + " Gameplay:" + RESET);
        System.out.println("- You can play a card if it matches the color or number of the top discard.");
        System.out.println("- " + YELLOW + "Special wild card choose color" + RESET + " can be played anytime, but " + RED + "wild draw four" + RESET + " has rules.");
        System.out.println("- If you can't play, then draw one card. If it's playable, you may use it.");
        System.out.println("- " + RED + BOLD + "Say 'UNO!' before you have only one card left." + RESET + " If you forget, you receive " + RED + "1 penalty card." + RESET + "\n");

        System.out.println(BOLD + " Action Cards:" + RESET);
        System.out.println("1. " + BLUE + "Draw Two" + RESET + ": Next player draws 2 cards and skips their turn.");
        System.out.println("2. " + GREEN + "Reverse" + RESET + ": Changes the direction of play.");
        System.out.println("3. " + RED + "Skip" + RESET + ": Next player loses their turn.");
        System.out.println("4. " + PURPLE + "Wild Color" + RESET + ": Choose the next color to play on discard pile.");
        System.out.println("5. " + RED + BOLD + "Wild Draw Four" + RESET + ": Choose color and next player draws 4 cards. " + RED + "Only play if you have no matching color." + RESET + "\n");

        System.out.println(BOLD + " Penalties:" + RESET);
        System.out.println("- Forgot to say UNO: " + RED + "1 card" + RESET + ".");
        System.out.println("- Suggesting moves to others: " + RED + "2 cards" + RESET + ".");
        System.out.println("- Playing invalid card or typo: " + RED + "1 penalty card" + RESET + ".");
        System.out.println("- Wrong usage of wild draw four card:");
        System.out.println("    If " + RED + "challenged and guilty" + RESET + ": draw 4 cards.");
        System.out.println("    If " + GREEN + "innocent" + RESET + ": challenger draws 6 cards.\n");

        System.out.println(BOLD + " Scoring:" + RESET);
        System.out.println("- Number cards: Number value.");
        System.out.println("- Draw Two, Reverse and Skip: " + YELLOW + "20 points each" + RESET + ".");
        System.out.println("- Wild Color and Wild Draw Four: " + PURPLE + "50 points each" + RESET + ".");
        System.out.println("- First player with " + BOLD + "500 points" + RESET + " wins.\n");

        System.out.println(BOLD + " Card Input Format:" + RESET);
        System.out.println("- R5     → Red 5");
        System.out.println("- Rx     → Red Skip card");
        System.out.println("- Rd     → Red Direction-Reverse card");
        System.out.println("- R+2    → Red Draw Two card");
        System.out.println("- +4     → Draw Four");
        System.out.println("- CC     → Choose color\n");

        System.out.println(BOLD + " Player Actions:" + RESET);
        System.out.println("- 1 → Draw a card");
        System.out.println("- 2 → Play a card");
        System.out.println("- 3 → Check Bluff");
        System.out.println("- 4 → Play a card AND SAY UNO");
        System.out.println("- 5 → Give advice (e.g., suggest a color)");
        System.out.println("- 6 → Game Instructions");
        System.out.println("- 7 → Pause");
        System.out.println("- 0 → Exit the game\n");

        System.out.println(BOLD + " Color Codes:" + RESET);
        System.out.println("- " + RED + "R = Red" + RESET);
        System.out.println("- " + GREEN + "G = Green" + RESET);
        System.out.println("- " + BLUE + "B = Blue" + RESET);
        System.out.println("- " + YELLOW + "Y = Yellow" + RESET);
        System.out.println("- " + PURPLE + "+4 and CC" + RESET + " are wild cards\n");

        System.out.println(BOLD + "Tip:" + RESET + " Play smart, bluff wisely, and " + RED + BOLD + "don't forget to say UNO!" + RESET + "\n");

        System.out.println(BOLD + "Have fun and good luck!" + RESET + "\n");
    }
}
