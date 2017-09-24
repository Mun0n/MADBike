package org.drunkcode.madbike.ui.home.fragment;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.utils.OneSignalUtils;
import org.drunkcode.madbike.utils.tracking.AnalyticsManager;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static SettingsFragment newInstance(Bundle arguments) {
        SettingsFragment f = new SettingsFragment();
        if (arguments != null) {
            f.setArguments(arguments);
        }
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnalyticsManager.getInstance().trackContentView("Settings");
        addPreferencesFromResource(R.xml.preferences);

    }

    @Override
    public void onPause() {
        super.onPause();
        OneSignalUtils.getInstance().sendOneSignalTags();
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

    }
}
