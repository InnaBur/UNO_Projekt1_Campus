public class Karte {

    private String karteName;
    private boolean isSpezial;

    public Karte(String karteName, boolean isSpeziel) {
        this.karteName = karteName;
        this.isSpezial = isSpeziel;
    }

    public String getKarteName() {
        return karteName;
    }

    public void setKarteName(String karteName) {
        this.karteName = karteName;
    }

    public boolean isSpezial() {
        return isSpezial;
    }

    public void setSpezial(boolean spezial) {
        isSpezial = spezial;
    }

    @Override
    public String toString() {
        return "Karte{" +
                "karteName='" + karteName + '\'' +
                ", isSpezial=" + isSpezial +
                '}';
    }

    public String karteToString() {
        return karteName;
    }

}
