public class Karte {

    String karteName;
    boolean isSpezial;

    public Karte(String karteName, boolean isSpeziel) {
        this.karteName = karteName;
        this.isSpezial = isSpeziel;
    }

    @Override
    public String toString() {
        return "Karte{" +
                "karteName='" + karteName + '\'' +
                ", isSpezial=" + isSpezial +
                '}';
    }
}
