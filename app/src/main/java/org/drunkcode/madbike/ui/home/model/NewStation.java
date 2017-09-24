package org.drunkcode.madbike.ui.home.model;

import com.google.gson.annotations.SerializedName;

import org.drunkcode.madbike.utils.JSONUtils;

public class NewStation {

    @SerializedName(JSONUtils.ID)
    private int id;
    @SerializedName(JSONUtils.LATITUDE_ENGLISH)
    private String latitude;
    @SerializedName(JSONUtils.LONGITUDE_ENGLISH)
    private String longitude;
    @SerializedName(JSONUtils.NAME_EN)
    private String name;
    @SerializedName(JSONUtils.LIGTH_EN)
    private int light;
    @SerializedName(JSONUtils.NUMBER_EN)
    private String number;
    @SerializedName(JSONUtils.ADDRESS_EN)
    private String address;
    @SerializedName(JSONUtils.ACTIVATE_EN)
    private int activate;
    @SerializedName(JSONUtils.NO_AVAILABLE_EN)
    private int noAvailable;
    @SerializedName(JSONUtils.TOTAL_BASES)
    private int totalBases;
    @SerializedName(JSONUtils.DOCK_BIKES)
    private int dockBases;
    @SerializedName(JSONUtils.FREE_BASES)
    private int freeBases;
    @SerializedName(JSONUtils.RESERVATION_COUNT)
    private int reservationCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getActivate() {
        return activate;
    }

    public void setActivate(int activate) {
        this.activate = activate;
    }

    public int getNoAvailable() {
        return noAvailable;
    }

    public void setNoAvailable(int noAvailable) {
        this.noAvailable = noAvailable;
    }

    public int getTotalBases() {
        return totalBases;
    }

    public void setTotalBases(int totalBases) {
        this.totalBases = totalBases;
    }

    public int getDockBases() {
        return dockBases;
    }

    public void setDockBases(int dockBases) {
        this.dockBases = dockBases;
    }

    public int getFreeBases() {
        return freeBases;
    }

    public void setFreeBases(int freeBases) {
        this.freeBases = freeBases;
    }

    public int getReservationCount() {
        return reservationCount;
    }

    public void setReservationCount(int reservationCount) {
        this.reservationCount = reservationCount;
    }
}
