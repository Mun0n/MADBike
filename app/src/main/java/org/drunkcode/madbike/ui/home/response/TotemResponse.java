package org.drunkcode.madbike.ui.home.response;

import com.google.gson.annotations.SerializedName;

import org.drunkcode.madbike.base.BaseResponse;
import org.drunkcode.madbike.ui.home.model.Station;
import org.drunkcode.madbike.utils.JSONUtils;

public class TotemResponse extends BaseResponse{

    public TotemResponse(int success, String message) {
        super(success, message);
    }

    @SerializedName(JSONUtils.STATIONS)
    private Station[] stations;

    public Station[] getStations() {
        return stations;
    }

    public void setStations(Station[] stations) {
        this.stations = stations;
    }
}
