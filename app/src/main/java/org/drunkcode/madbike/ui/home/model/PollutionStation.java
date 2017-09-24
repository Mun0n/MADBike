package org.drunkcode.madbike.ui.home.model;

import com.google.gson.annotations.SerializedName;

import org.drunkcode.madbike.utils.JSONUtils;

/**
 * Created by mun0n on 6/6/16.
 */
public class PollutionStation {

    @SerializedName(JSONUtils.ID)
    private String id;
    @SerializedName(JSONUtils.NAME_PARAM)
    private String name;
    @SerializedName(JSONUtils.LATITUDE_ENGLISH)
    private double latitude;
    @SerializedName(JSONUtils.LONGITUDE_ENGLISH)
    private double longitude;
    @SerializedName(JSONUtils.ALTITUDE)
    private double altitude;
    @SerializedName(JSONUtils.DATE)
    private String date;
    @SerializedName(JSONUtils.METRICS)
    private Metric[] metrics;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Metric[] getMetrics() {
        return metrics;
    }

    public void setMetrics(Metric[] metrics) {
        this.metrics = metrics;
    }
}
