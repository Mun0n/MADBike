package org.drunkcode.madbike.ui.home.interactor;

import android.content.Context;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.base.BaseInteractor;
import org.drunkcode.madbike.network.Apis;
import org.drunkcode.madbike.ui.home.response.WeatherResponse;

import java.util.Locale;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PostWeatherInteractor implements BaseInteractor {

    private Context context;

    public PostWeatherInteractor(Context context) {
        this.context = context;
    }

    @Override
    public void execute() throws Throwable {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(context.getString(R.string.weather_url)).build();
        Apis apis = restAdapter.create(Apis.class);
        apis.getWeatherData(6359304, Locale.getDefault().getLanguage(), "metric", "43cd5e05d25e37bdffbad27e42619e3f", new Callback<WeatherResponse>() {
            @Override
            public void success(WeatherResponse weatherResponse, Response response) {
                EventBus.getDefault().post(weatherResponse);
            }

            @Override
            public void failure(RetrofitError error) {
                EventBus.getDefault().post(new WeatherResponse());
            }
        });

    }
}
