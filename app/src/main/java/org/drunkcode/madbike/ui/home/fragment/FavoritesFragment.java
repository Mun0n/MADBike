package org.drunkcode.madbike.ui.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.carlosdelachica.easyrecycleradapters.adapter.EasyRecyclerAdapter;
import com.carlosdelachica.easyrecycleradapters.adapter.EasyViewHolder;
import com.carlosdelachica.easyrecycleradapters.decorations.DividerItemDecoration;
import com.google.gson.Gson;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.base.BaseFragment;
import org.drunkcode.madbike.preferences.Preferences;
import org.drunkcode.madbike.ui.favorite.activity.FavoriteActivity;
import org.drunkcode.madbike.ui.home.model.FavoriteItem;
import org.drunkcode.madbike.ui.home.model.Station;
import org.drunkcode.madbike.ui.search.holder.StationHolder;
import org.drunkcode.madbike.utils.tracking.AnalyticsManager;

import java.util.ArrayList;

import butterknife.InjectView;

public class FavoritesFragment extends BaseFragment implements EasyViewHolder.OnItemClickListener {

    public static final int SEE_DETAIL = 5001;
    public static final String DATA = "data";
    @InjectView(android.R.id.empty)
    TextView emptyTextView;
    @InjectView(R.id.resultsView)
    RecyclerView resultsRecyclerView;
    private EasyRecyclerAdapter adapter;
    private Gson gson;
    private ArrayList<Station> stationsArrayList;
    private Station[] stations;

    public static FavoritesFragment newInstance(Bundle arguments) {
        FavoritesFragment f = new FavoritesFragment();
        if (arguments != null) {
            f.setArguments(arguments);
        }
        return f;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_favorites;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AnalyticsManager.getInstance().trackContentView("Favorites");
        initAdapter();
        initRecyclerView();
        initData();
    }

    private void initData() {
        gson = new Gson();
        stations = gson.fromJson(Preferences.getInstance(getActivity()).getAllStations(), Station[].class);
        FavoriteItem[] favorites = gson.fromJson(Preferences.getInstance(getActivity()).getIdFav(), FavoriteItem[].class);
        if (favorites != null && favorites.length > 0) {
            stationsArrayList = new ArrayList<Station>();
            for (int i = 0; i < favorites.length; i++) {
                for (int j = 0; j < stations.length; j++) {
                    if (favorites[i].getId().equalsIgnoreCase(stations[j].getIdStation())) {
                        stationsArrayList.add(stations[j]);
                    }
                }
            }
            if (stationsArrayList.size() > 0) {
                emptyTextView.setVisibility(View.GONE);
                resultsRecyclerView.setVisibility(View.VISIBLE);
                adapter.addAll(stationsArrayList);
            }
        }
    }

    private void initAdapter() {
        adapter = new EasyRecyclerAdapter(getActivity(), Station.class, StationHolder.class);
        adapter.setOnClickListener(this);
    }

    private void initRecyclerView() {
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        resultsRecyclerView.setAdapter(adapter);
        resultsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), ContextCompat.getDrawable(getActivity(), R.drawable.custom_divider_black)));
    }

    @Override
    public void onItemClick(int position, View view) {
        if (stationsArrayList != null && stationsArrayList.size() > 0) {
            Intent favoriteIntent = new Intent(getActivity(), FavoriteActivity.class);
            favoriteIntent.putExtra(DATA, gson.toJson(stationsArrayList.get(position)));
            startActivityForResult(favoriteIntent, SEE_DETAIL);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FavoriteItem[] favorites = gson.fromJson(Preferences.getInstance(getActivity()).getIdFav(), FavoriteItem[].class);
        if (favorites != null && favorites.length > 0) {
            stationsArrayList = new ArrayList<Station>();
            for (int i = 0; i < favorites.length; i++) {
                for (int j = 0; j < stations.length; j++) {
                    if (favorites[i].getId().equalsIgnoreCase(stations[j].getIdStation())) {
                        stationsArrayList.add(stations[j]);
                    }
                }
            }
            if (stationsArrayList.size() > 0) {
                emptyTextView.setVisibility(View.GONE);
                resultsRecyclerView.setVisibility(View.VISIBLE);
                adapter.addAll(stationsArrayList);
                adapter.notifyDataSetChanged();
            } else {
                emptyTextView.setVisibility(View.VISIBLE);
                resultsRecyclerView.setVisibility(View.GONE);
            }
        } else {
            emptyTextView.setVisibility(View.VISIBLE);
            resultsRecyclerView.setVisibility(View.GONE);
        }
    }
}
