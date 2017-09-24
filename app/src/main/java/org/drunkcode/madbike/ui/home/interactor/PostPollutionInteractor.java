package org.drunkcode.madbike.ui.home.interactor;

import android.content.Context;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.base.BaseInteractor;
import org.drunkcode.madbike.network.Apis;
import org.drunkcode.madbike.ui.home.response.PollutionResponse;

import java.util.Locale;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PostPollutionInteractor implements BaseInteractor {

    private Context context;

    public PostPollutionInteractor(Context context) {
        this.context = context;
    }

    @Override
    public void execute() throws Throwable {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(context.getString(R.string.pollution_url)).build();
        Apis apis = restAdapter.create(Apis.class);
        apis.getPollutionData(new Callback<PollutionResponse>() {
            @Override
            public void success(PollutionResponse pollutionResponse, Response response) {
                pollutionResponse.setSuccess(1);
                EventBus.getDefault().post(pollutionResponse);
            }

            @Override
            public void failure(RetrofitError error) {
                PollutionResponse pollutionResponse = new PollutionResponse();
                pollutionResponse.setSuccess(0);
                EventBus.getDefault().post(pollutionResponse);
            }
        });
    }
}
