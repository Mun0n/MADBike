package org.drunkcode.madbike.ui.home.interactor;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.base.BaseInteractor;
import org.drunkcode.madbike.network.Apis;
import org.drunkcode.madbike.preferences.Preferences;
import org.drunkcode.madbike.ui.home.model.Station;
import org.drunkcode.madbike.ui.home.response.TotemResponse;
import org.drunkcode.madbike.utils.JSONUtils;
import org.drunkcode.madbike.utils.MD5;
import org.json.JSONException;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PostMapInteractor implements BaseInteractor {

    private Context context;
    private String dni = "INICIO_APP";
    private String idAuth = "INICIO_APP";

    public PostMapInteractor(Context context) {
        this.context = context;
    }

    private JsonObject createJSONToPostRequest() throws JSONException {
        JsonObject requestItem = new JsonObject();
        Preferences preferences = Preferences.getInstance(context);
        if(preferences.isLogged()) {
            dni = preferences.getUserDni();
            idAuth = preferences.getIdAuth();
        }
        requestItem.addProperty(JSONUtils.DNI, dni);
        requestItem.addProperty(JSONUtils.ID_AUTH, idAuth);
        requestItem.addProperty(JSONUtils.ID_SECURITY, MD5.buildIdSecurity(dni, idAuth));
        return requestItem;
    }

    @Override
    public void execute() throws Throwable {

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(context.getString(R.string.url_base)).build();
        Apis apis = restAdapter.create(Apis.class);
        apis.getAllStations(createJSONToPostRequest(), new Callback<TotemResponse>() {
            @Override
            public void success(TotemResponse totemResponse, Response response) {
                Station[] stations = totemResponse.getStations();
                for (int i = 0; i < stations.length; i++) {
                    Station s = stations[i];
                    s.setPosition(new LatLng(Double.parseDouble(s.getLatitude()), Double.parseDouble(s.getLongitude())));
                    stations[i] = s;
                }
                totemResponse.setStations(stations);
                EventBus.getDefault().post(totemResponse);
            }

            @Override
            public void failure(RetrofitError error) {
                TotemResponse totemResponse = new TotemResponse(0, context.getString(R.string.error_generic));
                EventBus.getDefault().post(totemResponse);
            }
        });
    }
}
