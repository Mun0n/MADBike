package org.drunkcode.madbike.ui.home.model;

import android.graphics.Bitmap;
import android.view.View;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

import org.drunkcode.madbike.utils.JSONUtils;

public class Station implements ClusterItem{

    @SerializedName(JSONUtils.ID_STATION)
    private String idStation;
    @SerializedName(JSONUtils.NAME)
    private String nombre;
    @SerializedName(JSONUtils.NUMBER_STATION)
    private String numberStation;
    @SerializedName(JSONUtils.ADDRESS)
    private String address;
    @SerializedName(JSONUtils.LATITUDE)
    private String latitude;
    @SerializedName(JSONUtils.LONGITUDE)
    private String longitude;
    @SerializedName(JSONUtils.ACTIVE)
    private String active;
    @SerializedName(JSONUtils.LIGTH)
    private String light;
    @SerializedName(JSONUtils.NO_AVAILABLE)
    private String noAvailable;
    @SerializedName(JSONUtils.NUMBER_BASES)
    private String numberBases;
    @SerializedName(JSONUtils.BIKE_ENGAGED)
    private String bikeEngaged;
    @SerializedName(JSONUtils.BASES_FREE)
    private String basesFree;
    @SerializedName(JSONUtils.PERCENT)
    private float percent;

    private transient BitmapDescriptor icon;

    private LatLng position;
    private transient Bitmap bitmapMarker;

    public String getIdStation() {
        return idStation;
    }

    public void setIdStation(String idStation) {
        this.idStation = idStation;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumberStation() {
        return numberStation;
    }

    public void setNumberStation(String numberStation) {
        this.numberStation = numberStation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public String getNoAvailable() {
        return noAvailable;
    }

    public void setNoAvailable(String noAvailable) {
        this.noAvailable = noAvailable;
    }

    public String getNumberBases() {
        return numberBases;
    }

    public void setNumberBases(String numberBases) {
        this.numberBases = numberBases;
    }

    public String getBikeEngaged() {
        return bikeEngaged;
    }

    public void setBikeEngaged(String bikeEngaged) {
        this.bikeEngaged = bikeEngaged;
    }

    public String getBasesFree() {
        return basesFree;
    }

    public void setBasesFree(String basesFree) {
        this.basesFree = basesFree;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public BitmapDescriptor getIcon() {
        return icon;
    }

    public void setIcon(BitmapDescriptor icon) {
        this.icon = icon;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    public Bitmap getBitmapMarker() {
        return bitmapMarker;
    }

    public void setBitmapMarker(Bitmap bitmapMarker) {
        this.bitmapMarker = bitmapMarker;
    }
}
