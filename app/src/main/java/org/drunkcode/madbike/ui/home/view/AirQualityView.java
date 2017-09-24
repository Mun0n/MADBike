package org.drunkcode.madbike.ui.home.view;

import org.drunkcode.madbike.base.BaseView;
import org.drunkcode.madbike.ui.home.response.PollutionResponse;

public interface AirQualityView extends BaseView{

    void getPollutionResponse(PollutionResponse pollutionResponse);

}
