package org.drunkcode.madbike.ui.home.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.gson.Gson;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import io.fabric.sdk.android.Fabric;

import org.drunkcode.madbike.BuildConfig;
import org.drunkcode.madbike.R;
import org.drunkcode.madbike.base.BaseActivity;
import org.drunkcode.madbike.preferences.Preferences;
import org.drunkcode.madbike.ui.home.fragment.AboutUsFragment;
import org.drunkcode.madbike.ui.home.fragment.AirQualityFragment;
import org.drunkcode.madbike.ui.home.fragment.ContactFragment;
import org.drunkcode.madbike.ui.home.fragment.FavoritesFragment;
import org.drunkcode.madbike.ui.home.fragment.IncidenceFragment;
import org.drunkcode.madbike.ui.home.fragment.InfoFragment;
import org.drunkcode.madbike.ui.home.fragment.MapFragment;
import org.drunkcode.madbike.ui.home.fragment.ProposalsFragment;
import org.drunkcode.madbike.ui.home.fragment.SettingsFragment;
import org.drunkcode.madbike.ui.login.activity.LoginActivity;
import org.drunkcode.madbike.ui.login.model.UserModel;
import org.drunkcode.madbike.ui.profile.activity.ProfileActivity;
import org.drunkcode.madbike.utils.BranchManager;
import org.drunkcode.madbike.utils.OneSignalUtils;

import java.util.Hashtable;

import butterknife.InjectView;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final int PROFILE_REQUEST = 9001;
    public static final String EXTRA_DATA = "extra_data";
    public static final String SECTION_AIR_QUALITY = "quality";
    public static final String SECTION_NEWS = "news";
    public static final String SECTION_MAP_STATION = "station";
    public static final String STATION_ID = "station_id";
    public static final String SECTION_PROPOSALS = "proposals";
    public static final String REVIEW_LINK = "review";

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawer;
    @InjectView(R.id.nav_view)
    NavigationView navigationView;
    TextView nameUserTextView;
    TextView subTitleTextView;

    private Preferences preferences;
    private int itemSelected = 0;

    private Hashtable<Integer, Integer> titleIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig("FILL_YOUR_OWN_KEY_CONSUMER_KEY", "FILL_YOUR_OWN_CONSUMER_KEY");
        Fabric.with(this, new Crashlytics.Builder().core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG_MODE).build()).build(), new Twitter(authConfig));
        checkPlayServices();
        createTitleIdsHash();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        preferences = Preferences.getInstance(this);
        initHeaderFunctionallity(navigationView.getHeaderView(0));
        if (preferences.getIdAuth() != null && !preferences.getIdAuth().isEmpty()) {
            setDataToHeader(preferences);
            preferences.setIsLogged(true);
        } else {
            preferences.setIsLogged(false);
        }
        if (getIntent().getExtras() != null && getIntent().getExtras().getString(EXTRA_DATA) != null) {
            if (getIntent().getExtras().getString(EXTRA_DATA).equalsIgnoreCase(SECTION_AIR_QUALITY)) {
                showFragment(R.id.nav_air);
            } else if (getIntent().getExtras().getString(EXTRA_DATA).equalsIgnoreCase(SECTION_NEWS)) {
                showFragment(R.id.nav_news);
            } else if (getIntent().getExtras().getString(EXTRA_DATA).equalsIgnoreCase(SECTION_PROPOSALS)) {
                showFragment(R.id.nav_proposals);
            } else if (getIntent().getExtras().getString(EXTRA_DATA).equalsIgnoreCase(REVIEW_LINK)) {
                Preferences.getInstance(this).setRateDone(true);
                OneSignalUtils.getInstance().sendOneSignalTags();
                final String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }else if (getIntent().getExtras().getString(EXTRA_DATA).equalsIgnoreCase(SECTION_MAP_STATION)) {
                Bundle bundle = new Bundle();
                if (getIntent().getExtras().getString(STATION_ID) != null) {
                    bundle.putString(STATION_ID, getIntent().getExtras().getString(STATION_ID));
                }
                showMapFragment(bundle);
            }
        } else {
            showMapFragment(new Bundle());
        }
        Branch branch = Branch.getInstance();

        branch.initSession(new Branch.BranchUniversalReferralInitListener() {
            @Override
            public void onInitFinished(BranchUniversalObject branchUniversalObject, LinkProperties linkProperties, BranchError error) {
                if (error == null && branchUniversalObject != null) {
                    BranchManager.getInstance().manageBranchLink(HomeActivity.this, branchUniversalObject);
                }
            }
        }, this.getIntent().getData(), this);

        OneSignalUtils.getInstance().sendOneSignalTags();

    }

    @Override
    protected void onDestroy() {
        OneSignalUtils.getInstance().sendOneSignalTags();
        super.onDestroy();
    }

    private void createTitleIdsHash() {
        titleIds = new Hashtable<>();
        titleIds.put(R.id.nav_air, R.string.air_title);
        titleIds.put(R.id.nav_news, R.string.info_title);
        titleIds.put(R.id.nav_fav, R.string.favorite_title);
        titleIds.put(R.id.nav_about_us, R.string.about_us_title);
        titleIds.put(R.id.nav_contact, R.string.contact_title);
        titleIds.put(R.id.nav_settings, R.string.settings);
        titleIds.put(R.id.nav_send_incidence, R.string.incidence_title);
        titleIds.put(R.id.nav_proposals, R.string.proposals);
    }

    private void initHeaderFunctionallity(View headerView) {
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (preferences.isLogged()) {
                    Intent profileIntent = new Intent(HomeActivity.this, ProfileActivity.class);
                    startActivityForResult(profileIntent, PROFILE_REQUEST);
                } else {
                    showLoginDialog();
                }
            }
        });
        nameUserTextView = (TextView) headerView.findViewById(R.id.nameUserTextView);
        subTitleTextView = (TextView) headerView.findViewById(R.id.subTitleTextView);
        nameUserTextView.setText("MADBike!");
        subTitleTextView.setText(R.string.press_here_to_login);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }

    private void initLoginActivity() {
        Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivityForResult(loginIntent, LoginActivity.LOGIN_RESULT);
    }

    private void setDataToHeader(Preferences preferences) {
        Gson gson = new Gson();
        try {
            UserModel userModel = gson.fromJson(preferences.getUserData(), UserModel.class);
            String name = userModel.getName().substring(0, 1) + userModel.getName().substring(1).toLowerCase();
            String firstSurname = "";
            String secondSurname = "";
            if ((userModel.getFirstSurname() != null) && (userModel.getFirstSurname().length() > 1)) {
                firstSurname = userModel.getFirstSurname().substring(0, 1) + userModel.getFirstSurname().substring(1).toLowerCase();
            }
            if ((userModel.getSecondSurname() != null) && (userModel.getSecondSurname().length() > 1)) {
                secondSurname = userModel.getSecondSurname().substring(0, 1) + userModel.getSecondSurname().substring(1).toLowerCase();
            }
            nameUserTextView.setText(name + " " + firstSurname + " " + secondSurname);
            subTitleTextView.setText(userModel.getEmail());
        } catch (Exception e) {
            nameUserTextView.setText("MADBike!");
            subTitleTextView.setText(R.string.press_here_to_login);
        }
    }

    private void changeFragment(Fragment fragment, boolean addBackstack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (addBackstack) {
            fragmentTransaction.replace(R.id.containerHome, fragment).addToBackStack("");
        } else {
            fragmentTransaction.replace(R.id.containerHome, fragment);
        }
        fragmentTransaction.commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_home;
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.map_toolbar);
    }

    @Override
    protected boolean getActivityHomeAsUpEnabled() {
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.containerHome);
            if (f instanceof MapFragment) {
                super.onBackPressed();
            } else {
                if (getIntent().getExtras() != null && getIntent().getExtras().getString(EXTRA_DATA) != null) {
                    finish();
                } else {
                    navigationView.getMenu().getItem(0).setChecked(true);
                    setTitleToolbar(getString(R.string.map_stations));
                    getSupportFragmentManager().popBackStack("", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("active_switch", false)) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        if (id == R.id.nav_map) {
            itemSelected = 0;
            setTitleToolbar(getString(R.string.map_stations));
            getSupportFragmentManager().popBackStack("", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            if (checkIfItsLogged(id)) {
                showFragment(id);
            } else {
                navigationView.getMenu().getItem(itemSelected).setChecked(true);
                showLoginDialog();
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showLoginDialog() {
        new AlertDialog.Builder(this).setTitle(R.string.need_login_title).setMessage(R.string.need_login_message).setPositiveButton(R.string.need_login_title, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                initLoginActivity();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setCancelable(false)
                .show();
    }

    private void showFragment(int idNavigation) {
        setTitleToolbar(getString(titleIds.get(idNavigation)));
        Fragment fragment;
        switch (idNavigation) {
            case R.id.nav_send_incidence:
                itemSelected = 3;
                fragment = IncidenceFragment.newInstance(null);
                break;
            case R.id.nav_proposals:
                itemSelected = 6;
                fragment = ProposalsFragment.newInstance(null);
                break;
            case R.id.nav_settings:
                itemSelected = 7;
                fragment = SettingsFragment.newInstance(null);
                break;
            case R.id.nav_contact:
                itemSelected = 5;
                fragment = ContactFragment.newInstance(null);
                break;
            case R.id.nav_about_us:
                itemSelected = 8;
                fragment = AboutUsFragment.newInstance(null);
                break;
            case R.id.nav_fav:
                itemSelected = 2;
                fragment = FavoritesFragment.newInstance(null);
                break;
            case R.id.nav_news:
                itemSelected = 4;
                fragment = InfoFragment.newInstance(null);
                break;
            case R.id.nav_air:
                itemSelected = 1;
                fragment = AirQualityFragment.newInstance(null);
                break;
            default:
                itemSelected = 0;
                fragment = MapFragment.newInstance(null);
                break;
        }
        changeFragment(fragment, true);
    }

    private boolean checkIfItsLogged(int id) {
        if (!preferences.isLogged() && (id == R.id.nav_send_incidence)) {
            return false;
        }
        return true;
    }

    private void showMapFragment(Bundle bundle) {
        setTitleToolbar(getString(R.string.map_stations));
        navigationView.getMenu().getItem(0).setChecked(true);
        MapFragment mapFragment = MapFragment.newInstance(bundle);
        changeFragment(mapFragment, false);
    }

    private void setTitleToolbar(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PROFILE_REQUEST && resultCode == CommonStatusCodes.SUCCESS) {
            preferences.setIdAuth("");
            preferences.setUserData("");
            preferences.setUserDni("");
            preferences.setEmail("");
            preferences.setIsLogged(false);
            setDataToHeader(preferences);
            if (!(getSupportFragmentManager().findFragmentById(R.id.containerHome) instanceof MapFragment)) {
                navigationView.getMenu().getItem(0).setChecked(true);
                setTitleToolbar(getString(R.string.map_stations));
                getSupportFragmentManager().popBackStack("", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        } else if (requestCode == 140) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.containerHome);
            if (fragment != null && fragment instanceof MapFragment) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == LoginActivity.LOGIN_RESULT) {
            navigationView.getMenu().getItem(itemSelected).setChecked(true);
            preferences.setIsLogged(true);
            setDataToHeader(preferences);
        }
    }

}
