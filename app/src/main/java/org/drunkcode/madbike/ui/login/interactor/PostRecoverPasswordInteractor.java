package org.drunkcode.madbike.ui.login.interactor;

import android.content.Context;

import com.google.gson.JsonObject;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.base.BaseInteractor;
import org.drunkcode.madbike.base.BaseResponse;
import org.drunkcode.madbike.network.Apis;
import org.drunkcode.madbike.ui.login.model.RecoverModel;
import org.drunkcode.madbike.utils.JSONUtils;
import org.drunkcode.madbike.utils.MD5;
import org.json.JSONException;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PostRecoverPasswordInteractor implements BaseInteractor {

    private Context context;
    private RecoverModel recoverModel;

    public PostRecoverPasswordInteractor(Context context) {
        this.context = context;
    }

    private JsonObject createJSONToPostRequest() throws JSONException {
        JsonObject requestItem = new JsonObject();
        requestItem.addProperty(JSONUtils.DNI, recoverModel.getDocument());
        requestItem.addProperty(JSONUtils.EMAIL, recoverModel.getEmail());
        requestItem.addProperty(JSONUtils.ID_SECURITY, MD5.buildIdSecurity(recoverModel.getDocument(), recoverModel.getEmail()));
        return requestItem;
    }

    @Override
    public void execute() throws Throwable {

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(context.getString(R.string.url_base)).build();
        Apis apis = restAdapter.create(Apis.class);
        apis.generateNewPassword(createJSONToPostRequest(), new Callback<BaseResponse>() {
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

    public RecoverModel getRecoverModel() {
        return recoverModel;
    }

    public void setRecoverModel(RecoverModel recoverModel) {
        this.recoverModel = recoverModel;
    }
}
