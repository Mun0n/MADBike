package org.drunkcode.madbike.ui.login.interactor;

import android.content.Context;

import com.google.gson.JsonObject;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.base.BaseInteractor;
import org.drunkcode.madbike.network.Apis;
import org.drunkcode.madbike.ui.login.model.LoginModel;
import org.drunkcode.madbike.ui.login.response.LoginResponse;
import org.drunkcode.madbike.utils.JSONUtils;
import org.drunkcode.madbike.utils.MD5;
import org.json.JSONException;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PostLoginInteractor implements BaseInteractor {

    private Context context;
    private LoginModel loginModel;

    public PostLoginInteractor(Context context) {
        this.context = context;
    }

    private JsonObject createJSONToPostRequest() throws JSONException {
        JsonObject requestItem = new JsonObject();
        requestItem.addProperty(JSONUtils.PASSWORD, loginModel.getPassword());
        requestItem.addProperty(JSONUtils.PUSH_CODE, "NO");
        requestItem.addProperty(JSONUtils.DNI, loginModel.getDni());
        requestItem.addProperty(JSONUtils.ID_SECURITY, MD5.buildIdSecurity(loginModel.getDni(), loginModel.getPassword()));
        return requestItem;
    }


    @Override
    public void execute() throws Throwable {

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(context.getString(R.string.url_base)).build();
        Apis apis = restAdapter.create(Apis.class);
        apis.doLogin(createJSONToPostRequest(), new Callback<LoginResponse>() {

            @Override
            public void success(LoginResponse loginResponse, Response response) {
                EventBus.getDefault().post(loginResponse);
            }

            @Override
            public void failure(RetrofitError error) {
                LoginResponse loginResponse = new LoginResponse(0, context.getString(R.string.error_generic));
                EventBus.getDefault().post(loginResponse);
            }
        });
    }

    public void setLoginModel(LoginModel loginModel) {
        this.loginModel = loginModel;
    }
}
