package org.drunkcode.madbike.ui.home.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.base.BaseFragment;
import org.drunkcode.madbike.ui.home.adapter.TabPagerAdapter;
import org.drunkcode.madbike.utils.tracking.AnalyticsManager;

import butterknife.InjectView;

public class InfoFragment extends BaseFragment{

    @InjectView(R.id.viewPager)
    ViewPager viewPager;
    @InjectView(R.id.tabLayout)
    TabLayout tabLayout;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_info;
    }

    public static InfoFragment newInstance(Bundle arguments) {
        InfoFragment f = new InfoFragment();
        if (arguments != null) {
            f.setArguments(arguments);
        }
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflateView = inflater.inflate(R.layout.fragment_info, container, false);
        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getChildFragmentManager());
        tabPagerAdapter.addFragment(NewsFragment.newInstance(new Bundle()),getString(R.string.info_title));
        tabPagerAdapter.addFragment(TipsFragment.newInstance(new Bundle()),getString(R.string.tips_title));
        tabPagerAdapter.addFragment(EcoFragment.newInstance(new Bundle()),getString(R.string.eco_title));
        ViewPager viewPager = (ViewPager) inflateView.findViewById(R.id.viewPager);
        viewPager.setAdapter(tabPagerAdapter);
        TabLayout tabLayout = (TabLayout) inflateView.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        return inflateView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AnalyticsManager.getInstance().trackContentView("Info");
    }
}
