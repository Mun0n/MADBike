package org.drunkcode.madbike.ui.home.model;

import com.google.gson.annotations.SerializedName;

import org.drunkcode.madbike.utils.JSONUtils;

public class WeatherItem {

    @SerializedName(JSONUtils.DESCRIPTION)
    private String description;
    @SerializedName(JSONUtils.ICON)
    private String icon;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
