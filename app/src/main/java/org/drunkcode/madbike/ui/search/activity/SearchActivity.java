package org.drunkcode.madbike.ui.search.activity;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.carlosdelachica.easyrecycleradapters.adapter.EasyRecyclerAdapter;
import com.carlosdelachica.easyrecycleradapters.adapter.EasyViewHolder;
import com.carlosdelachica.easyrecycleradapters.decorations.DividerItemDecoration;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.base.BaseActivity;
import org.drunkcode.madbike.common.dialogs.loading.LoadingManager;
import org.drunkcode.madbike.common.notification.NotificationManager;
import org.drunkcode.madbike.preferences.Preferences;
import org.drunkcode.madbike.ui.home.adapter.PlaceAutocompleteAdapter;
import org.drunkcode.madbike.ui.home.fragment.MapFragment;
import org.drunkcode.madbike.ui.home.model.Station;
import org.drunkcode.madbike.ui.search.holder.StationHolder;
import org.drunkcode.madbike.ui.search.presenter.SearchPresenter;
import org.drunkcode.madbike.ui.search.response.SearchResponse;
import org.drunkcode.madbike.ui.search.view.SearchActivityView;
import org.drunkcode.madbike.utils.MADBikePermissionListener;
import org.drunkcode.madbike.utils.MADBikeSearchPermissionListener;
import org.drunkcode.madbike.utils.tracking.AnalyticsManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import butterknife.InjectView;
import butterknife.OnClick;

import static org.drunkcode.madbike.R.id.rootView;

public class SearchActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemClickListener, TextWatcher, SearchActivityView, EasyViewHolder.OnItemClickListener, TextView.OnEditorActionListener {
    public static final String RETURN_STATION = "return_station";
    LatLngBounds madridArea = new LatLngBounds(new LatLng(40.357903, -3.7858293), new LatLng(40.5327086, -3.6503509));

    @InjectView(R.id.autoCompleteTextView)
    AutoCompleteTextView autoCompleteTextView;
    @InjectView(R.id.autoButton)
    ImageButton autoButton;
    @InjectView(R.id.resultsView)
    RecyclerView resultsRecyclerView;
    @InjectView(android.R.id.empty)
    TextView emptyTextView;
    @InjectView(R.id.rootView)
    CoordinatorLayout rootView;

    private GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    private boolean clear = false;
    private NotificationManager notificationManager;
    protected static final int RESULT_SPEECH = 1;
    private EasyRecyclerAdapter adapter;

    private SearchPresenter searchPresenter;
    private LoadingManager loadingManager;

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

    private Station[] stations;

    private CompositePermissionListener locationPermissionListener;

    private LocationManager locationManager;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_search;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }

    @Override
    protected boolean getActivityHomeAsUpEnabled() {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(CommonStatusCodes.CANCELED);
                finish();
                return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnalyticsManager.getInstance().trackContentView("Search");
        setResult(CommonStatusCodes.CANCELED);
        initThings();
        initAdapter();
        initRecyclerView();
        emptyTextView.setText(getString(R.string.no_results_init));
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, madridArea,
                null);
        autoCompleteTextView.setAdapter(mAdapter);
    }

    private void initThings() {
        loadingManager = new LoadingManager(this);
        searchPresenter = new SearchPresenter(this);
        notificationManager = new NotificationManager(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

        autoCompleteTextView.setOnItemClickListener(this);
        autoCompleteTextView.addTextChangedListener(this);
        autoCompleteTextView.setOnEditorActionListener(this);

        createPermissionListener();
        Dexter.checkPermission(locationPermissionListener, Manifest.permission.ACCESS_COARSE_LOCATION);

    }

    private void createPermissionListener() {
        PermissionListener feedbackViewPermissionListener = new MADBikeSearchPermissionListener(this);

        locationPermissionListener = new CompositePermissionListener(feedbackViewPermissionListener,
                SnackbarOnDeniedPermissionListener.Builder.with(rootView,
                        R.string.permission_location_denied_feedback)
                        .withOpenSettingsButton(R.string.permission_rationale_settings_button_text)
                        .build());
    }

    private void initAdapter() {
        adapter = new EasyRecyclerAdapter(this, Station.class, StationHolder.class);
        adapter.setOnClickListener(this);
    }

    private void initRecyclerView() {
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        resultsRecyclerView.setAdapter(adapter);
        resultsRecyclerView.addItemDecoration(new DividerItemDecoration(this, ContextCompat.getDrawable(this, R.drawable.custom_divider_black)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        searchPresenter.onPause();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AnalyticsManager.getInstance().trackSearch();
        final AutocompletePrediction item = mAdapter.getItem(position);
        final String placeId = item.getPlaceId();
        final CharSequence primaryText = item.getPrimaryText(null);
        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                .getPlaceById(mGoogleApiClient, placeId);
        placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
    }

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                places.release();
                return;
            }
            try {
                final Place place = places.get(0);
                if (Preferences.getInstance(SearchActivity.this).isLogged()) {
                    searchPresenter.getNearStations(place.getLatLng());
                    places.release();
                } else {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(MapFragment.LATITUDE_SEARCH, place.getLatLng().latitude);
                    returnIntent.putExtra(MapFragment.LONGITUDE_SEARCH, place.getLatLng().longitude);
                    places.release();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            } catch (Throwable throwable) {
                resultsRecyclerView.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.VISIBLE);
                emptyTextView.setText(getString(R.string.error_generic));
            }
        }
    };


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() == 0) {
            clear = false;
            autoButton.setImageResource(R.drawable.ic_mic);
        } else {
            clear = true;
            autoButton.setImageResource(R.drawable.ic_clear);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @OnClick(R.id.autoButton)
    public void onAutoPressed() {
        if (clear) {
            autoCompleteTextView.setText("");
        } else {
            Intent intent = new Intent(
                    RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault().getDisplayLanguage());

            try {
                startActivityForResult(intent, RESULT_SPEECH);
                autoCompleteTextView.setText("");
            } catch (ActivityNotFoundException a) {
                notificationManager.showMessage(getString(R.string.error_no_mic));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    autoCompleteTextView.setText(text.get(0));
                }
                break;
            }

        }
    }

    @Override
    public void getSearchResponse(SearchResponse searchResponse) {
        switch (searchResponse.getSuccess()) {
            case 1:
                if (searchResponse.getStations().length > 0) {
                    resultsRecyclerView.setVisibility(View.VISIBLE);
                    emptyTextView.setVisibility(View.GONE);
                    stations = searchResponse.getStations();
                    adapter.addAll(Arrays.asList(searchResponse.getStations()));

                } else {
                    resultsRecyclerView.setVisibility(View.GONE);
                    emptyTextView.setVisibility(View.VISIBLE);
                    emptyTextView.setText(getString(R.string.no_results_found));
                }
                break;
            default:
                resultsRecyclerView.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.VISIBLE);
                emptyTextView.setText(getString(R.string.error_generic));
                break;
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
    public void onItemClick(int position, View view) {
        Intent stationIntent = new Intent();
        Gson gson = new Gson();
        stationIntent.putExtra(RETURN_STATION, gson.toJson(stations[position]));
        setResult(CommonStatusCodes.SUCCESS, stationIntent);
        finish();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            try {
                if (userLocation != null) {
                    searchPresenter.getNearStations(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()));
                } else {
                    resultsRecyclerView.setVisibility(View.GONE);
                    emptyTextView.setVisibility(View.VISIBLE);
                    emptyTextView.setText(getString(R.string.error_generic));
                    return false;
                }
            } catch (Throwable throwable) {
                resultsRecyclerView.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.VISIBLE);
                emptyTextView.setText(getString(R.string.error_generic));
                return false;
            }
            return true;
        }
        return false;
    }

    public void showPermissionGranted(String permissionName) {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                notificationManager.showMessage(getString(R.string.no_search));
            }
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    public void showPermissionDenied(String permissionName, boolean permanentlyDenied) {

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void showPermissionRationale(final PermissionToken token) {

        new AlertDialog.Builder(this).setTitle(R.string.permission_location_title)
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
}
