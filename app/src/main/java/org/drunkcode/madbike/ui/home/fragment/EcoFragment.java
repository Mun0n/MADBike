package org.drunkcode.madbike.ui.home.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.tweetui.CollectionTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.utils.tracking.AnalyticsManager;

import java.util.Locale;

/**
 * Created by mun0n on 30/5/16.
 */
public class EcoFragment extends ListFragment {

    String collectionIdEn = "737373381146910721";
    String collectionIdEs = "737373980861059073";
    private String collectionUsed;

    public static EcoFragment newInstance(Bundle arguments) {
        EcoFragment f = new EcoFragment();
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
        return inflater.inflate(R.layout.fragment_eco, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AnalyticsManager.getInstance().trackContentView("Eco");
    }
}
