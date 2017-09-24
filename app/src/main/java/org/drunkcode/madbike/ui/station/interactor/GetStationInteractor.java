package org.drunkcode.madbike.ui.station.interactor;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.base.BaseInteractor;
import org.drunkcode.madbike.network.Apis;
import org.drunkcode.madbike.ui.station.response.StationResponse;
import org.drunkcode.madbike.utils.SelfSigningClientBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

public class GetStationInteractor implements BaseInteractor {

    private Context context;
    private String stationId;

    public GetStationInteractor(Context context) {
        this.context = context;
    }

    @Override
    public void execute() throws Throwable {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(context.getString(R.string.emt_url_single_station)).setClient(new OkClient(SelfSigningClientBuilder.createClient())).build();
        Apis apis = restAdapter.create(Apis.class);
        apis.getStationInfo(stationId, new Callback<StationResponse>() {
            @Override
            public void success(StationResponse stationResponse, Response response) {
                stationResponse.setSuccess(1);
                EventBus.getDefault().post(stationResponse);
            }

            @Override
            public void failure(RetrofitError error) {
                StationResponse stationResponse = new StationResponse();
                stationResponse.setSuccess(0);
                EventBus.getDefault().post(stationResponse);
            }
        });
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

}
