package org.drunkcode.madbike.ui.home.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.utils.tracking.AnalyticsManager;

/**
 * Created by mun0n on 24/2/16.
 */
public class NewsFragment extends ListFragment {

    public static NewsFragment newInstance(Bundle arguments) {
        NewsFragment f = new NewsFragment();
        if (arguments != null) {
            f.setArguments(arguments);
        }
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName("madbikeapp")
                .includeReplies(false)
                .includeRetweets(true)
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getActivity())
                .setTimeline(userTimeline)
                .build();
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AnalyticsManager.getInstance().trackContentView("News");
    }
}
