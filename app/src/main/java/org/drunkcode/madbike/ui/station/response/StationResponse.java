package org.drunkcode.madbike.ui.station.response;

import com.google.gson.annotations.SerializedName;

import org.drunkcode.madbike.base.BaseResponse;
import org.drunkcode.madbike.utils.JSONUtils;

public class StationResponse extends BaseResponse{

    @SerializedName(JSONUtils.DATA)
    private String stationData;

    public String getStationData() {
        return stationData;
    }

    public void setStationData(String stationData) {
        this.stationData = stationData;
    }
}
