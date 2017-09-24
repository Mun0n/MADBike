package org.drunkcode.madbike.ui.home.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.twitter.sdk.android.tweetui.CollectionTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.utils.tracking.AnalyticsManager;

import java.util.Locale;

/**
 * Created by mun0n on 24/2/16.
 */
public class TipsFragment extends ListFragment {

    String collectionIdEn = "702216997413789700";
    String collectionIdEs = "702218309266251777";
    private String collectionUsed;

    public static TipsFragment newInstance(Bundle arguments) {
        TipsFragment f = new TipsFragment();
        if (arguments != null) {
            f.setArguments(arguments);
        }
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Locale.getDefault().getLanguage().equalsIgnoreCase(Locale.ENGLISH.getLanguage())) {
            collectionUsed = collectionIdEn;
        } else {
            collectionUsed = collectionIdEs;
        }
        CollectionTimeline timeline = new CollectionTimeline.Builder()
                .id(Long.parseLong(collectionUsed))
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getActivity())
                .setTimeline(timeline)
                .build();
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tips, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AnalyticsManager.getInstance().trackContentView("Tips");
    }
}
