package org.drunkcode.madbike.network;

import com.google.gson.JsonObject;

import org.drunkcode.madbike.base.BaseResponse;
import org.drunkcode.madbike.ui.home.response.PollutionResponse;
import org.drunkcode.madbike.ui.home.response.RouteResponse;
import org.drunkcode.madbike.ui.home.response.TotemResponse;
import org.drunkcode.madbike.ui.home.response.WeatherResponse;
import org.drunkcode.madbike.ui.login.response.LoginResponse;
import org.drunkcode.madbike.ui.profile.response.ChangePasswordResponse;
import org.drunkcode.madbike.ui.profile.response.ProfileResponse;
import org.drunkcode.madbike.ui.search.response.SearchResponse;
import org.drunkcode.madbike.ui.station.response.StationResponse;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface Apis {

    @POST("/get_usuario.php")
    void doLogin(@Body JsonObject data, Callback<LoginResponse> response);

    @POST("/generate_new_password.php")
    void generateNewPassword(@Body JsonObject data, Callback<BaseResponse> response);

    @POST("/get_all_estaciones_new.php")
    void getAllStations(@Body JsonObject data, Callback<TotemResponse> response);

    @POST("/get_historial_rutas_usuario.php")
    void getAllUserRoutes(@Body JsonObject data, Callback<RouteResponse> response);

    @POST("/registrar_incidencia.php")
    void registerIncidence(@Body JsonObject data, Callback<BaseResponse> response);

    @POST("/get_datos_usuario.php")
    void getUserData(@Body JsonObject data, Callback<ProfileResponse> response);

    @POST("/change_password.php")
    void changePassword(@Body JsonObject data, Callback<ChangePasswordResponse> response);

    @POST("/get_estaciones_cercanas.php")
    void getNearestStations(@Body JsonObject data, Callback<SearchResponse> response);

    @GET("/weather")
    void getWeatherData(@Query("id") long id, @Query("lang") String locale, @Query("units") String units, @Query("APPID") String appID, Callback<WeatherResponse> response);

    @Headers({
            "X-User-Email: javi@drunkcode.org",
            "X-User-Token: 4AeUTqRoixc9wMM_wCkM"
    })
    @GET("/air_qualities.json/?current_values=1&discard_average=1")
    void getPollutionData(Callback<PollutionResponse> callback);

    @GET("/{id_station}")
    void getStationInfo(@Path("id_station") String idStation, Callback<StationResponse> callback);
}
