package org.drunkcode.madbike.ui.home.fragment;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.base.BaseFragment;
import org.drunkcode.madbike.utils.tracking.AnalyticsManager;

import butterknife.InjectView;

public class ProposalsFragment extends BaseFragment {

    @InjectView(R.id.webView)
    WebView webView;

    public static ProposalsFragment newInstance(Bundle arguments) {
        ProposalsFragment f = new ProposalsFragment();
        if (arguments != null) {
            f.setArguments(arguments);
        }
        return f;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_proposals;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView.loadUrl("https://www.nextinit.com/landings/madbike/mobile-activation.html?p=RANDOM");
        webView.getSettings().setJavaScriptEnabled(true);
        AnalyticsManager.getInstance().trackContentView("Proposals");
    }
}
