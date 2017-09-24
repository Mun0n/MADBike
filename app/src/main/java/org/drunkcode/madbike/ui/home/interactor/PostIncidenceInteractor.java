package org.drunkcode.madbike.ui.home.interactor;

import android.content.Context;

import com.google.gson.JsonObject;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.base.BaseInteractor;
import org.drunkcode.madbike.base.BaseResponse;
import org.drunkcode.madbike.network.Apis;
import org.drunkcode.madbike.preferences.Preferences;
import org.drunkcode.madbike.ui.home.model.IncidenceModel;
import org.drunkcode.madbike.utils.JSONUtils;
import org.json.JSONException;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PostIncidenceInteractor implements BaseInteractor {

    private Context context;
    private IncidenceModel incidenceModel;

    public PostIncidenceInteractor(Context context) {
        this.context = context;
    }

    private JsonObject createJSONToPostRequest() throws JSONException {
        JsonObject requestItem = new JsonObject();
        Preferences preferences = Preferences.getInstance(context);
        requestItem.addProperty(JSONUtils.DNI, preferences.getUserDni());
        requestItem.addProperty(JSONUtils.EMAIL, preferences.getEmail());
        requestItem.addProperty(JSONUtils.NAME, incidenceModel.getName());
        requestItem.addProperty(JSONUtils.ADDRESS_INCIDENCE, incidenceModel.getAddressIncidence());
        requestItem.addProperty(JSONUtils.STATION_INCIDENCE, incidenceModel.getStationIncidence());
        requestItem.addProperty(JSONUtils.NAME_INCIDENCE, incidenceModel.getNameIncidence());
        requestItem.addProperty(JSONUtils.DETAIL_INCIDENCE, incidenceModel.getDetailIncidence());
        return requestItem;
    }

    @Override
    public void execute() throws Throwable {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(context.getString(R.string.url_base)).build();
        Apis apis = restAdapter.create(Apis.class);
        apis.registerIncidence(createJSONToPostRequest(), new Callback<BaseResponse>() {
            @Override
            public void success(BaseResponse baseResponse, Response response) {
                EventBus.getDefault().post(baseResponse);
            }

            @Override
            public void failure(RetrofitError error) {
                BaseResponse baseResponse = new BaseResponse(0, context.getString(R.string.error_generic));
                EventBus.getDefault().post(baseResponse);
            }
        });
    }

    public void setIncidenceModel(IncidenceModel incidenceModel) {
        this.incidenceModel = incidenceModel;
    }
}
