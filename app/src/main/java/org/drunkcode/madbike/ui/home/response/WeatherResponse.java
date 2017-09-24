package org.drunkcode.madbike.ui.home.response;

import com.google.gson.annotations.SerializedName;

import org.drunkcode.madbike.ui.home.model.TemperatureItem;
import org.drunkcode.madbike.ui.home.model.WeatherItem;
import org.drunkcode.madbike.ui.home.model.WindItem;
import org.drunkcode.madbike.utils.JSONUtils;

public class WeatherResponse {

    @SerializedName(JSONUtils.WEATHER)
    private WeatherItem[] weatherItems;
    @SerializedName(JSONUtils.MAIN)
    private TemperatureItem temperatureItem;
    @SerializedName(JSONUtils.WIND)
    private WindItem windItem;

    public WeatherItem[] getWeatherItems() {
        return weatherItems;
    }

    public void setWeatherItems(WeatherItem[] weatherItems) {
        this.weatherItems = weatherItems;
    }

    public TemperatureItem getTemperatureItem() {
        return temperatureItem;
    }

    public void setTemperatureItem(TemperatureItem temperatureItem) {
        this.temperatureItem = temperatureItem;
    }

    public WindItem getWindItem() {
        return windItem;
    }

    public void setWindItem(WindItem windItem) {
        this.windItem = windItem;
    }
}
