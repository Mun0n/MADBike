package org.drunkcode.madbike.ui.search.response;

import com.google.gson.annotations.SerializedName;

import org.drunkcode.madbike.base.BaseResponse;
import org.drunkcode.madbike.ui.home.model.Station;
import org.drunkcode.madbike.utils.JSONUtils;

public class SearchResponse extends BaseResponse{

    @SerializedName(JSONUtils.STATIONS)
    private Station[] stations;

    public SearchResponse(int success, String message) {
        super(success, message);
    }

    public Station[] getStations() {
        return stations;
    }

    public void setStations(Station[] stations) {
        this.stations = stations;
    }
}
