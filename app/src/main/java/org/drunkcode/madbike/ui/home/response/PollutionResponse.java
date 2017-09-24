package org.drunkcode.madbike.ui.home.response;

import com.google.gson.annotations.SerializedName;

import org.drunkcode.madbike.base.BaseResponse;
import org.drunkcode.madbike.ui.home.model.PollutionStation;
import org.drunkcode.madbike.utils.JSONUtils;

public class PollutionResponse extends BaseResponse {

    @SerializedName(JSONUtils.POLLUTION_STATIONS)
    private PollutionStation[] stations;

    public PollutionStation[] getStations() {
        return stations;
    }

    public void setStations(PollutionStation[] stations) {
        this.stations = stations;
    }
}
