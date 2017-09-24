package org.drunkcode.madbike.ui.home.view;

import org.drunkcode.madbike.base.BaseView;
import org.drunkcode.madbike.ui.home.response.PollutionResponse;
import org.drunkcode.madbike.ui.home.response.TotemResponse;
import org.drunkcode.madbike.ui.home.response.WeatherResponse;

public interface MapFragmentView extends BaseView {

    void getTotemsResponse(TotemResponse totemResponse);

    void getWeatherResponse(WeatherResponse weatherResponse);
}
