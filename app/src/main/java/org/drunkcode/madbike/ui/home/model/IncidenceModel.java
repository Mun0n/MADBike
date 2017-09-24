package org.drunkcode.madbike.ui.home.model;

public class IncidenceModel {

    private String dni;
    private String email;
    private String name;
    private String addressIncidence;
    private String stationIncidence;
    private String nameIncidence;
    private String detailIncidence;

    public IncidenceModel(String dni, String email, String name, String addressIncidence, String stationIncidence, String nameIncidence, String detailIncidence) {
        this.dni = dni;
        this.email = email;
        this.name = name;
        this.addressIncidence = addressIncidence;
        this.stationIncidence = stationIncidence;
        this.nameIncidence = nameIncidence;
        this.detailIncidence = detailIncidence;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressIncidence() {
        return addressIncidence;
    }

    public void setAddressIncidence(String addressIncidence) {
        this.addressIncidence = addressIncidence;
    }

    public String getStationIncidence() {
        return stationIncidence;
    }

    public void setStationIncidence(String stationIncidence) {
        this.stationIncidence = stationIncidence;
    }

    public String getDetailIncidence() {
        return detailIncidence;
    }

    public void setDetailIncidence(String detailIncidence) {
        this.detailIncidence = detailIncidence;
    }

    public String getNameIncidence() {
        return nameIncidence;
    }

    public void setNameIncidence(String nameIncidence) {
        this.nameIncidence = nameIncidence;
    }
}
