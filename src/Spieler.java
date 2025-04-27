public class Spieler {

    private String name;
    private boolean isBot;

    public Spieler(String name, boolean isBot) {
        this.name = name;
        this.isBot = isBot;
    }

    public Spieler() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBot() {
        return isBot;
    }

    public void setBot(boolean bot) {
        isBot = bot;
    }

    @Override
    public String toString() {
        return "Spieler{" +
                "name='" + name + '\'' +
                ", isBot=" + isBot +
                '}';
    }
}
