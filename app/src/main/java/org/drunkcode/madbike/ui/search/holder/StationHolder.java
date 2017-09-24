package org.drunkcode.madbike.ui.search.holder;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carlosdelachica.easyrecycleradapters.adapter.EasyViewHolder;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.ui.home.model.Station;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StationHolder extends EasyViewHolder<Station> {

    private final Context context;
    @InjectView(R.id.nameTextView)
    public TextView nameTextView;
    @InjectView(R.id.streetTextView)
    public TextView streetTextView;
    @InjectView(R.id.chart)
    PieChart mChart;

    protected String[] mParties;


    public StationHolder(Context context, ViewGroup parent) {
        super(context, parent, R.layout.item_search);
        this.context = context;
        ButterKnife.inject(this, itemView);
    }

    @Override
    public void bindTo(Station value) {
        nameTextView.setText(value.getNumberStation() + " " + value.getNombre());
        streetTextView.setText(value.getAddress());
        mChart.setDescription("");
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setCenterText(context.getString(R.string.base_state));

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

        setData(value);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

    private void setData(Station station) {

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        float inactive = (float) Integer.parseInt(station.getNumberBases()) - Integer.parseInt(station.getBasesFree()) - Integer.parseInt(station.getBikeEngaged());

        yVals1.add(new Entry(Float.parseFloat(station.getBasesFree()), 0));
        yVals1.add(new Entry(Float.parseFloat(station.getBikeEngaged()), 1));
        yVals1.add(new Entry(inactive, 2));

        ArrayList<String> xVals = new ArrayList<String>();
        mParties = context.getResources().getStringArray(R.array.states_values);
        for (int i = 0; i < mParties.length + 1; i++)
            xVals.add(mParties[i % mParties.length]);

        PieDataSet dataSet = new PieDataSet(yVals1, context.getString(R.string.base_state));
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(ContextCompat.getColor(context, R.color.red_chart));
        colors.add(ContextCompat.getColor(context, R.color.green_chart));
        colors.add(ContextCompat.getColor(context, R.color.gray_chart));

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
}
