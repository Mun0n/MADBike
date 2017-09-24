package org.drunkcode.madbike.ui.station.view;

import org.drunkcode.madbike.base.BaseView;
import org.drunkcode.madbike.ui.station.response.StationResponse;

public interface StationView extends BaseView{

    void getStationResponse(StationResponse stationResponse);
}
