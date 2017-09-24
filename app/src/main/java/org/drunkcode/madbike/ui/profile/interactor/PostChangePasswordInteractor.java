package org.drunkcode.madbike.ui.profile.interactor;

import android.content.Context;

import com.google.gson.JsonObject;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.base.BaseInteractor;
import org.drunkcode.madbike.network.Apis;
import org.drunkcode.madbike.preferences.Preferences;
import org.drunkcode.madbike.ui.profile.model.ChangePasswordModel;
import org.drunkcode.madbike.ui.profile.response.ChangePasswordResponse;
import org.drunkcode.madbike.utils.JSONUtils;
import org.drunkcode.madbike.utils.MD5;
import org.json.JSONException;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PostChangePasswordInteractor implements BaseInteractor {

    private Context context;
    private ChangePasswordModel changePasswordModel;

    public PostChangePasswordInteractor(Context context) {
        this.context = context;
    }

    private JsonObject createJSONToPostRequest() throws JSONException {
        JsonObject requestItem = new JsonObject();
        Preferences preferences = Preferences.getInstance(context);
        requestItem.addProperty(JSONUtils.DNI, preferences.getUserDni());
        requestItem.addProperty(JSONUtils.PASSWORD, changePasswordModel.getOldPassword());
        requestItem.addProperty(JSONUtils.PASSWORD_NEW, changePasswordModel.getNewPassword());
        requestItem.addProperty(JSONUtils.ID_SECURITY, MD5.buildIdSecurity(preferences.getUserDni(), preferences.getIdAuth()));
        return requestItem;
    }

    @Override
    public void execute() throws Throwable {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(context.getString(R.string.url_base)).build();
        Apis apis = restAdapter.create(Apis.class);
        apis.changePassword(createJSONToPostRequest(), new Callback<ChangePasswordResponse>() {
            @Override
            public void success(ChangePasswordResponse changePasswordResponse, Response response) {
                EventBus.getDefault().post(changePasswordResponse);
            }

            @Override
            public void failure(RetrofitError error) {
                ChangePasswordResponse changePasswordResponse = new ChangePasswordResponse(0, context.getString(R.string.error_generic));
                EventBus.getDefault().post(changePasswordResponse);
            }
        });
    }

    public void setChangePasswordModel(ChangePasswordModel changePasswordModel) {
        this.changePasswordModel = changePasswordModel;
    }
}
