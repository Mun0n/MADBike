package org.drunkcode.madbike.ui.station.model;

import com.google.gson.annotations.SerializedName;

import org.drunkcode.madbike.ui.home.model.NewStation;
import org.drunkcode.madbike.utils.JSONUtils;

import java.util.List;

public class StationInfo {

    @SerializedName(JSONUtils.STATIONS_EN)
    private List<NewStation> stationList;

    public List<NewStation> getStationList() {
        return stationList;
    }

    public void setStationList(List<NewStation> stationList) {
        this.stationList = stationList;
    }
}
