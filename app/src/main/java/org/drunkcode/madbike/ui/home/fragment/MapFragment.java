package org.drunkcode.madbike.ui.home.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.google.maps.android.clustering.ClusterManager;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener;
import com.onesignal.OneSignal;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Media;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetView;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.base.BaseFragment;
import org.drunkcode.madbike.common.dialogs.loading.LoadingManager;
import org.drunkcode.madbike.common.notification.NotificationManager;
import org.drunkcode.madbike.preferences.Preferences;
import org.drunkcode.madbike.ui.home.activity.HomeActivity;
import org.drunkcode.madbike.ui.home.adapter.BubbleAdapter;
import org.drunkcode.madbike.ui.home.adapter.IconRendered;
import org.drunkcode.madbike.ui.home.model.FavoriteItem;
import org.drunkcode.madbike.ui.home.model.Station;
import org.drunkcode.madbike.ui.home.presenter.MapPresenter;
import org.drunkcode.madbike.ui.home.response.TotemResponse;
import org.drunkcode.madbike.ui.home.response.WeatherResponse;
import org.drunkcode.madbike.ui.home.view.MapFragmentView;
import org.drunkcode.madbike.ui.search.activity.SearchActivity;
import org.drunkcode.madbike.utils.MADBikeMultiplePermissionListener;
import org.drunkcode.madbike.utils.MADBikePermissionListener;
import org.drunkcode.madbike.utils.OneSignalUtils;
import org.drunkcode.madbike.utils.tracking.AnalyticsManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.mime.TypedFile;

public class MapFragment extends BaseFragment implements OnMapReadyCallback, MapFragmentView, ClusterManager.OnClusterItemClickListener<Station>, GoogleMap.OnInfoWindowClickListener {

    public static final int SEARCH_CODE = 9001;
    private static final int CAMERA_REQUEST = 1888;
    public static final String LATITUDE_SEARCH = "latitude_search";
    public static final String LONGITUDE_SEARCH = "longitude_search";
    private static String idStation;
    private int TWITTER_CODE = 140;
    @InjectView(R.id.rootView)
    RelativeLayout rootView;
    @InjectView(R.id.mapView)
    MapView mapView;

    @InjectView(R.id.weatherImageView)
    ImageView weatherImageView;
    @InjectView(R.id.weatherTextView)
    TextView weatherTextView;
    @InjectView(R.id.tempTextView)
    TextView tempTextView;
    @InjectView(R.id.tempMaxTextView)
    TextView tempMaxTextView;
    @InjectView(R.id.tempMinTextView)
    TextView tempMinTextView;
    @InjectView(R.id.login_button)
    TwitterLoginButton loginButton;

    @InjectView(R.id.weatherMainLayout)
    RelativeLayout weatherRelativeLayout;

    private GoogleMap map;
    private LoadingManager loadingManager;
    private NotificationManager notificationManager;
    private MapPresenter mapPresenter;
    private Station[] stations;
    private ClusterManager<Station> clusterManager;
    private boolean reload = false;
    LatLng madrid;
    private Gson gson;
    private Dialog d;

    protected String[] mParties;
    private ArrayList<FavoriteItem> favList;
    private CompositePermissionListener locationPermissionListener;
    private MultiplePermissionsListener cameraPermissionListener;

    private Location userLocation;
    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            userLocation = location;
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };
    private File pictureFile;

    public static MapFragment newInstance(Bundle arguments) {
        MapFragment f = new MapFragment();
        if (arguments != null) {
            f.setArguments(arguments);
        }
        return f;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_map;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                startActivityForResult(searchIntent, SEARCH_CODE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        mapPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        mapPresenter.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MapsInitializer.initialize(getActivity());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AnalyticsManager.getInstance().trackContentView("Map");
        mParties = getResources().getStringArray(R.array.states_values);
        setHasOptionsMenu(true);
        createPermissionListeners();
        initManagers();
        initMap(savedInstanceState);
        if (Dexter.isRequestOngoing()) {
            return;
        }
        Dexter.checkPermission(locationPermissionListener, Manifest.permission.ACCESS_COARSE_LOCATION);
        idStation = getArguments().getString(HomeActivity.STATION_ID);
    }

    private void createPermissionListeners() {
        PermissionListener feedbackViewPermissionListener = new MADBikePermissionListener(this);

        locationPermissionListener = new CompositePermissionListener(feedbackViewPermissionListener,
                SnackbarOnDeniedPermissionListener.Builder.with(rootView,
                        R.string.permission_location_denied_feedback)
                        .withOpenSettingsButton(R.string.permission_rationale_settings_button_text)
                        .build());

        PermissionListener dialogOnDeniedPermissionListener =
                DialogOnDeniedPermissionListener.Builder.withContext(getActivity())
                        .withTitle(R.string.permission_camera_title)
                        .withMessage(R.string.permission_camera_title)
                        .withButtonText(android.R.string.ok)
                        .withIcon(R.drawable.ic_camera_alt_white)
                        .build();

        MultiplePermissionsListener feedbackViewMultiplePermissionListener =
                new MADBikeMultiplePermissionListener(this);

        cameraPermissionListener = new CompositeMultiplePermissionsListener(feedbackViewMultiplePermissionListener,
                SnackbarOnAnyDeniedMultiplePermissionsListener.Builder.with(rootView,
                        R.string.permission_camera_title)
                        .withOpenSettingsButton(R.string.permission_rationale_settings_button_text)
                        .build());
    }

    private void initMap(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    private void initManagers() {
        madrid = new LatLng(40.4324421, -3.6967743);
        notificationManager = new NotificationManager(getActivity());
        loadingManager = new LoadingManager(getActivity());
        mapPresenter = new MapPresenter(this);
        gson = new Gson();
    }

    private void setUpClusterer() {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(madrid, 10));
        clusterManager = new ClusterManager<Station>(getActivity(), map);
        map.setOnCameraIdleListener(clusterManager);
        map.setOnMarkerClickListener(clusterManager);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        map.setInfoWindowAdapter(new BubbleAdapter(getActivity()));
        map.setOnInfoWindowClickListener(this);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(madrid, 12.5f));
        setUpClusterer();
        if (!reload) {
            LatLng madrid = new LatLng(40.4324421, -3.6967743);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(madrid, 12.5f));
        }
    }

    @Override
    public void getTotemsResponse(TotemResponse totemResponse) {
        stations = totemResponse.getStations();
        if (stations.length > 0) {
            Preferences.getInstance(getActivity()).setAllStations(gson.toJson(stations));
            clusterManager.clearItems();
            for (int i = 0; i < stations.length; i++) {
                View marker = createMarker(stations[i]);
                stations[i].setBitmapMarker(loadingBitmapFromView(marker));
                clusterManager.addItem(stations[i]);
            }

            clusterManager.setRenderer(new IconRendered(getActivity().getApplicationContext(), map, clusterManager));
            clusterManager.setOnClusterItemClickListener(this);

            if (idStation != null && !idStation.isEmpty()) {
                for (final Station station : stations) {
                    if (idStation.equalsIgnoreCase(station.getNumberStation())) {
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(station.getLatitude()), Double.parseDouble(station.getLongitude())), 15), new GoogleMap.CancelableCallback() {
                            @Override
                            public void onFinish() {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        onClusterItemClick(station);
                                    }
                                }, 2000);
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                    }
                }
            }
        } else {
            notificationManager.showMessage(getString(R.string.error_no_stations));
        }
    }

    private void showInfoStation(Station station) {

    }

    @Override
    public void getWeatherResponse(WeatherResponse weatherResponse) {
        if (weatherResponse.getWeatherItems() != null) {
            setWeatherWidgetImage(weatherResponse.getWeatherItems()[0].getIcon());
            weatherTextView.setText(weatherResponse.getWeatherItems()[0].getDescription());
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            double currentTemp = weatherResponse.getTemperatureItem().getTemp();
            double maxTemp = weatherResponse.getTemperatureItem().getTempMax();
            double minTemp = weatherResponse.getTemperatureItem().getTempMin();
            if (sharedPreferences.getString("degrees_list", "ºC").equalsIgnoreCase("ºF")) {
                currentTemp = (currentTemp * 1.8) + 32;
                maxTemp = (maxTemp * 1.8) + 32;
                minTemp = (minTemp * 1.8) + 32;
            }
            DecimalFormat df = new DecimalFormat("#.#");
            df.setRoundingMode(RoundingMode.CEILING);
            tempTextView.setText(df.format(currentTemp) + sharedPreferences.getString("degrees_list", "ºC"));
            tempMaxTextView.setText(df.format(maxTemp) + sharedPreferences.getString("degrees_list", "ºC"));
            tempMinTextView.setText(df.format(minTemp) + sharedPreferences.getString("degrees_list", "ºC"));
        } else {
            weatherRelativeLayout.setVisibility(View.GONE);
        }
    }

    private void setWeatherWidgetImage(String icon) {
        if (icon.equalsIgnoreCase("01d")) {
            weatherImageView.setImageResource(R.drawable.ic_sunny);
        } else if (icon.equalsIgnoreCase("01n")) {
            weatherImageView.setImageResource(R.drawable.ic_moon);
        } else if (icon.equalsIgnoreCase("02d")) {
            weatherImageView.setImageResource(R.drawable.ic_sun_cloud);
        } else if (icon.equalsIgnoreCase("02n")) {
            weatherImageView.setImageResource(R.drawable.ic_moon_cloud);
        } else if (icon.equalsIgnoreCase("03d") || icon.equalsIgnoreCase("03n")) {
            weatherImageView.setImageResource(R.drawable.ic_cloudy);
        } else if (icon.equalsIgnoreCase("04d") || icon.equalsIgnoreCase("04n")) {
            weatherImageView.setImageResource(R.drawable.ic_more_cloudy);
        } else if (icon.equalsIgnoreCase("09d") || icon.equalsIgnoreCase("09n")) {
            weatherImageView.setImageResource(R.drawable.ic_rain);
        } else if (icon.equalsIgnoreCase("10d")) {
            weatherImageView.setImageResource(R.drawable.ic_sun_rain);
        } else if (icon.equalsIgnoreCase("10n")) {
            weatherImageView.setImageResource(R.drawable.ic_moon_rain);
        } else if (icon.equalsIgnoreCase("11d") || icon.equalsIgnoreCase("11n")) {
            weatherImageView.setImageResource(R.drawable.ic_thunder);
        } else if (icon.equalsIgnoreCase("13d") || icon.equalsIgnoreCase("13n")) {
            weatherImageView.setImageResource(R.drawable.ic_snow);
        } else if (icon.equalsIgnoreCase("50d")) {
            weatherImageView.setImageResource(R.drawable.ic_sun_fog);
        } else if (icon.equalsIgnoreCase("50n")) {
            weatherImageView.setImageResource(R.drawable.ic_moon_fog);
        } else {
            weatherImageView.setImageResource(R.drawable.ic_unknown);
        }
    }

    @NonNull
    private View createMarker(Station station) {
        View marker = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_marker, null);
        ImageView markerImageView = (ImageView) marker.findViewById(R.id.markerImageView);
        TextView numTxt = (TextView) marker.findViewById(R.id.numberTextView);
        numTxt.setText(station.getNumberStation());
        switch (Integer.parseInt(station.getLight())) {
            case 0:
                markerImageView.setImageResource(R.drawable.ic_marker_green);
                break;
            case 1:
                markerImageView.setImageResource(R.drawable.ic_marker_red);
                break;
            case 2:
                markerImageView.setImageResource(R.drawable.ic_marker_yellow);
                break;
            case 3:
                markerImageView.setImageResource(R.drawable.ic_marker_gray);
                break;
        }
        return marker;
    }

    private Bitmap loadingBitmapFromView(View marker) {
        if (marker.getMeasuredHeight() <= 0) {
            marker.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            marker.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            Bitmap b = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            marker.layout(0, 0, marker.getMeasuredWidth(), marker.getMeasuredHeight());
            marker.draw(c);
            return b;
        } else {
            return BitmapFactory.decodeResource(getResources(), R.drawable.ic_marker_gray);
        }
    }

    @OnClick(R.id.reloadButton)
    public void onReloadPressed() {
        try {
            reload = true;
            mapPresenter.getStations();
        } catch (Throwable throwable) {
            notificationManager.showMessage(getString(R.string.error_no_reload));
        }
    }

    @Override
    public void showLoading(int idTitle, int idMessage) {
        loadingManager.showLoadingDialog(idTitle, idMessage);
    }

    @Override
    public void hideLoading() {
        loadingManager.hideLoading();
    }

    @Override
    public void showError() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEARCH_CODE && resultCode == CommonStatusCodes.SUCCESS) {
            Gson gson = new Gson();
            Station station = gson.fromJson(data.getExtras().getString(SearchActivity.RETURN_STATION), Station.class);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(station.getLatitude()), Double.parseDouble(station.getLongitude())), 15));
        } else if (requestCode == SEARCH_CODE && resultCode == Activity.RESULT_OK) {
            double latitude = data.getDoubleExtra(LATITUDE_SEARCH, 0.0);
            double longitude = data.getDoubleExtra(LONGITUDE_SEARCH, 0.0);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
        } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                storeImage(bitmap);
                loginButton.setCallback(new Callback<TwitterSession>() {
                    @Override
                    public void success(Result<TwitterSession> result) {
                        TypedFile typedFile = new TypedFile("image/jpeg", pictureFile);
                        Twitter.getApiClient().getMediaService().upload(typedFile, null, null, new Callback<Media>() {
                            @Override
                            public void success(Result<Media> result) {
                                try {
                                    Geocoder geocoder;
                                    List<Address> addresses;
                                    geocoder = new Geocoder(getActivity(), Locale.getDefault());
                                    if (userLocation != null) {
                                        addresses = geocoder.getFromLocation(userLocation.getLatitude(), userLocation.getLongitude(), 1);

                                        String streetName = addresses.get(0).getAddressLine(0);
                                        Twitter.getApiClient().getStatusesService().update("@BiciMAD @Lineamadrid @madbikeapp #MADBikeLost " + streetName, null, false, userLocation.getLatitude(), userLocation.getLongitude(), null, true, false, result.data.mediaIdString, new Callback<Tweet>() {
                                            @Override
                                            public void success(Result<Tweet> result) {
                                                View v = new TweetView(getActivity(), result.data);
                                                Toast.makeText(getActivity(), R.string.review_OK, Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void failure(TwitterException e) {
                                                Toast.makeText(getActivity(), R.string.review_fail, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getActivity(), R.string.review_fail_location, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (IOException e) {
                                    Toast.makeText(getActivity(), R.string.review_fail, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void failure(TwitterException e) {
                                Toast.makeText(getActivity(), R.string.review_fail, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void failure(TwitterException e) {
                        Toast.makeText(getActivity(), R.string.review_fail, Toast.LENGTH_SHORT).show();
                    }
                });
                loginButton.performClick();
            }
        } else if (requestCode == TWITTER_CODE) {
            loginButton.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void storeImage(Bitmap image) {
        pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getActivity().getPackageName()
                + "/Files");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "MI_" + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    @Override
    public boolean onClusterItemClick(Station station) {
        Location stationLocation = new Location("");
        stationLocation.setLatitude(Double.parseDouble(station.getLatitude()));
        stationLocation.setLongitude(Double.parseDouble(station.getLongitude()));
        Object[] markers = clusterManager.getMarkerCollection().getMarkers().toArray();
        for (Object m : markers) {
            if (m instanceof Marker) {
                Marker mark = (Marker) m;
                Location location = new Location("");
                location.setLatitude(mark.getPosition().latitude);
                location.setLongitude(mark.getPosition().longitude);
                if (location.distanceTo(stationLocation) == 0) {
                    map.animateCamera(CameraUpdateFactory.newLatLng(station.getPosition()));
                    mark.setTitle(gson.toJson(station));
                    mark.showInfoWindow();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onInfoWindowClick(final Marker marker) {

        d = new Dialog(getActivity());
        d.setContentView(R.layout.dialog_detail_station);
        final Station station = gson.fromJson(marker.getTitle(), Station.class);
        TextView nameTextView = (TextView) d.findViewById(R.id.nameTextView);
        TextView streetTextView = (TextView) d.findViewById(R.id.streetTextView);
        PieChart mChart = (PieChart) d.findViewById(R.id.chart);
        configureFavButton(station);
        ImageButton walkButton = (ImageButton) d.findViewById(R.id.walkButton);
        walkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Toast.makeText(getActivity(), getString(R.string.activate_gps), Toast.LENGTH_SHORT).show();
                }
                Intent cityMapperIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.citymapper.app.release");
                if (cityMapperIntent != null) {
                    try {
                        String locationUrl = "citymapper://directions?endcoord=" + marker.getPosition().latitude + "," + marker.getPosition().longitude + "&endname=" + URLEncoder.encode(station.getNombre(), "UTF-8") + "&endaddress=" + URLEncoder.encode(station.getAddress(), "UTF-8");
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(locationUrl));
                        getActivity().startActivity(i);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    Uri uri = Uri.parse("google.navigation:q=" + marker.getPosition().latitude + "," + marker.getPosition().longitude + "&mode=w");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            }
        });
        configChart(station, mChart);

        nameTextView.setText(station.getNumberStation() + " " + station.getNombre());
        streetTextView.setText(station.getAddress());


        d.show();
    }

    private void configureFavButton(final Station station) {
        final ImageButton favButton = (ImageButton) d.findViewById(R.id.favButton);
        FavoriteItem[] favoriteItems = gson.fromJson(Preferences.getInstance(getActivity()).getIdFav(), FavoriteItem[].class);
        if (favoriteItems != null) {
            favList = new ArrayList<FavoriteItem>(Arrays.asList(favoriteItems));
            for (int i = 0; i < favList.size(); i++) {
                FavoriteItem f = favList.get(i);
                if (f != null && f.getId().equalsIgnoreCase(station.getIdStation())) {
                    favButton.setImageResource(R.drawable.ic_favorite);
                    break;
                }
            }
        }
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsManager.getInstance().trackFavorites(true);
                FavoriteItem[] favoriteItems = gson.fromJson(Preferences.getInstance(getActivity()).getIdFav(), FavoriteItem[].class);
                boolean isFavorite = false;
                if (favoriteItems != null) {
                    favList = new ArrayList<FavoriteItem>(Arrays.asList(favoriteItems));
                    for (int i = 0; i < favList.size(); i++) {
                        FavoriteItem f = favList.get(i);
                        if (f != null && f.getId().equalsIgnoreCase(station.getIdStation())) {
                            isFavorite = true;
                            break;
                        }
                    }
                } else {
                    favList = null;
                }
                if (isFavorite) {
                    for (int i = 0; i < favList.size(); i++) {
                        FavoriteItem f = favList.get(i);
                        if (f.getId().equalsIgnoreCase(station.getIdStation())) {
                            if (favList.size() == 1) {
                                Preferences.getInstance(getActivity()).setIdFav("");
                            } else {
                                favList.remove(i);
                                FavoriteItem[] favorites = new FavoriteItem[favList.size()];
                                for (int j = 0; j < favList.size(); j++) {
                                    favorites[j] = favList.get(j);
                                }
                                OneSignal.deleteTag(String.format("Station%s", station.getIdStation()));
                                Preferences.getInstance(getActivity()).setIdFav(gson.toJson(favorites));
                            }
                            break;
                        }
                    }
                    favButton.setImageResource(R.drawable.ic_favorite_empty);
                } else {
                    FavoriteItem[] favorites;
                    if (favList != null) {
                        favList.add(new FavoriteItem(station.getIdStation()));
                        favorites = new FavoriteItem[favList.size()];
                        for (int i = 0; i < favList.size(); i++) {
                            favorites[i] = favList.get(i);
                        }
                    } else {
                        favorites = new FavoriteItem[1];
                        favorites[0] = new FavoriteItem(station.getIdStation());
                    }
                    Preferences.getInstance(getActivity()).setIdFav(gson.toJson(favorites));
                    favButton.setImageResource(R.drawable.ic_favorite);
                }
                OneSignalUtils.getInstance().sendOneSignalTags();
            }
        });
    }

    private void configChart(Station station, PieChart mChart) {
        mChart.setDescription("");
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setCenterText(getString(R.string.base_state));

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColorTransparent(true);

        mChart.setUsePercentValues(false);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setTouchEnabled(false);
        mChart.setRotationEnabled(false);
        mChart.setRotationAngle(0);

        setData(mChart, station);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setEnabled(false);
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

    private void setData(PieChart mChart, Station station) {

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        float inactive = (float) Integer.parseInt(station.getNumberBases()) - Integer.parseInt(station.getBasesFree()) - Integer.parseInt(station.getBikeEngaged());

        yVals1.add(new Entry(Float.parseFloat(station.getBasesFree()), 0));
        yVals1.add(new Entry(Float.parseFloat(station.getBikeEngaged()), 1));
        yVals1.add(new Entry(inactive, 2));

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < mParties.length + 1; i++)
            xVals.add(mParties[i % mParties.length]);

        PieDataSet dataSet = new PieDataSet(yVals1, getString(R.string.base_state));
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(ContextCompat.getColor(getActivity(), R.color.red_chart));
        colors.add(ContextCompat.getColor(getActivity(), R.color.green_chart));
        colors.add(ContextCompat.getColor(getActivity(), R.color.gray_chart));

        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "" + (int) value;
            }
        });
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        mChart.setData(data);

        mChart.highlightValues(null);

        mChart.invalidate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    public void makeInitialRequest() {
        try {
            mapPresenter.getStations();
        } catch (Throwable throwable) {
            notificationManager.showMessage(getString(R.string.error_get_station));
        }
        try {
            mapPresenter.getWeather();
        } catch (Throwable throwable) {
            weatherRelativeLayout.setVisibility(View.GONE);
        }
    }


    public void showPermissionGranted(String permission) {
        makeInitialRequest();
        if (getActivity() != null) {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    notificationManager.showMessage(getString(R.string.no_location));
                    map.setMyLocationEnabled(false);
                } else {
                    map.setMyLocationEnabled(true);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                    map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                        @Override
                        public boolean onMyLocationButtonClick() {
                            if (userLocation != null) {
                                LatLng latLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                            }
                            return true;
                        }
                    });
                }
            }
        }
    }

    public void showPermissionDenied(String permissionName, boolean permanentlyDenied) {
        makeInitialRequest();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void showPermissionRationale(final PermissionToken token) {

        new AlertDialog.Builder(getActivity()).setTitle(R.string.permission_location_title)
                .setMessage(R.string.permission_location_text)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        token.cancelPermissionRequest();
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        token.continuePermissionRequest();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        token.cancelPermissionRequest();
                    }
                })
                .setIcon(R.drawable.ic_location)
                .show();
    }

    @OnClick(R.id.fab)
    public void onFabButtonClicked() {
        AnalyticsManager.getInstance().trackSendIncidence();
        if (Dexter.isRequestOngoing()) {
            return;
        }
        Dexter.checkPermissions(cameraPermissionListener, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void showPermissionRationaleAdviseBike(final PermissionToken token) {

        new AlertDialog.Builder(getActivity()).setTitle(R.string.permission_advise_title)
                .setMessage(R.string.permission_advise_text)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        token.cancelPermissionRequest();
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        token.continuePermissionRequest();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        token.cancelPermissionRequest();
                    }
                })
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }

    public void showPermissionGranteAdviseBike(String permissionName) {
        if (permissionName.equalsIgnoreCase(Manifest.permission.CAMERA)) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    public void showPermissionDeniedAdviseBike(String permissionName, boolean permanentlyDenied) {
    }

}
