package org.drunkcode.madbike.ui.home.model;

import com.google.gson.annotations.SerializedName;

import org.drunkcode.madbike.utils.JSONUtils;

public class TemperatureItem {

    @SerializedName(JSONUtils.TEMP)
    private float temp;
    @SerializedName(JSONUtils.HUMIDITY)
    private float humidity;
    @SerializedName(JSONUtils.TEMP_MAX)
    private float tempMax;
    @SerializedName(JSONUtils.TEMP_MIN)
    private float tempMin;

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getTempMax() {
        return tempMax;
    }

    public void setTempMax(float tempMax) {
        this.tempMax = tempMax;
    }

    public float getTempMin() {
        return tempMin;
    }

    public void setTempMin(float tempMin) {
        this.tempMin = tempMin;
    }
}
