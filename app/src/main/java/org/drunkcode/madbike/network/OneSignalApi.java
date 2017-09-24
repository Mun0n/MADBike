package org.drunkcode.madbike.network;

import org.drunkcode.madbike.notifications.network.OneSignalBody;
import org.drunkcode.madbike.notifications.network.OneSignalResponse;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface OneSignalApi {

    @Headers("Content-Type: application/json")
    @PUT("/notifications/{id}/") void openedOneSignalPush(@Path("id") String id, @Body OneSignalBody data, Callback<OneSignalResponse> response);

}
