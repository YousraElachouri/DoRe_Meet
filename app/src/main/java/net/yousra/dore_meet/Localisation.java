package net.yousra.dore_meet;

public class Localisation {

    private Double latitude;
    private Double longitude;
    private String adr;
    private String locali;

    public Localisation(Double latitude, Double longitude, String adr, String locali) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.adr = adr;
        this.locali = locali;
    }

    public Localisation() {
    }

    public Localisation(String local) {
        this.locali = local;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAdr() {
        return adr;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }

    public String getLocali() {
        return locali;
    }

    public void setLocal(String local) {
        this.locali = local;
    }
}
