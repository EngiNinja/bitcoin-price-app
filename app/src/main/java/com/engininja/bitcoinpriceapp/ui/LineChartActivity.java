package com.engininja.bitcoinpriceapp.ui;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;

import com.engininja.bitcoinpriceapp.common.HistoricalDataEntry;
import com.engininja.bitcoinpriceapp.R;
import com.engininja.bitcoinpriceapp.model.LineChartViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents the chart with the historical data.
 * FIXME this class is yet to be refactored.
 * Please don't read it.
 */
public class LineChartActivity extends AppCompatActivity implements OnChartValueSelectedListener {
    private boolean growth;
    private LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linechart);

        LineChartViewModel lineChartViewModel = ViewModelProviders.of(this)
                .get(LineChartViewModel.class);
        lineChartViewModel.getHistoricalDataEntries().observe(this, this::handleUpdate);
    }

    void handleUpdate(ArrayList<HistoricalDataEntry> historicalValues) {
        Collections.reverse(historicalValues);
        drawChart(historicalValues);
    }

    public MinAndMaxEntries getMinAndMaxFromEntryList(ArrayList<HistoricalDataEntry> historicalValues) {
        if (historicalValues.size() == 0) {
            return null;
        }

        HistoricalDataEntry minEntry = historicalValues.get(0);
        HistoricalDataEntry maxEntry = historicalValues.get(0);
        double minValue = minEntry.getAverage();
        double maxValue = maxEntry.getAverage();

        for (int i = 0; i < historicalValues.size(); i++) {
            HistoricalDataEntry currentEntry = historicalValues.get(i);
            double currentValue = currentEntry.getAverage();
            if (currentValue > maxValue) {
                maxEntry = currentEntry;
                maxValue = currentValue;
            } else if (currentValue < minValue) {
                minEntry = currentEntry;
                minValue = currentValue;
            }
        }
        return new MinAndMaxEntries(minEntry, maxEntry);
    }

    /**
     * Draws chart.
     *
     * @param historicalValues
     */
    public void drawChart(ArrayList<HistoricalDataEntry> historicalValues) {
        {   // // Chart Style // //
            chart = findViewById(R.id.chart1);

            // background color
            chart.setBackgroundColor(Color.WHITE);

            // disable description text
            chart.getDescription().setEnabled(false);

            // enable touch gestures
            chart.setTouchEnabled(true);

            // set listeners
            chart.setOnChartValueSelectedListener(this);
            chart.setDrawGridBackground(false);

            // enable scaling and dragging
            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);
            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            chart.setPinchZoom(true);

        }

        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = chart.getXAxis();

            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f);

            String[] xData = extractTimeFromHistoricalData(historicalValues);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            xAxis.setValueFormatter(new MyXAxisValueFormatter(xData));

            // create marker to display box when values are selected
            MyMarkerView mv = new MyMarkerView(LineChartActivity.this, R.layout.custom_marker_view, xData);

            // Set the marker to the chart
            mv.setChartView(chart);
            chart.setMarker(mv);
        }


        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = chart.getAxisLeft();

            // disable dual axis (only use LEFT axis)
            chart.getAxisRight().setEnabled(false);

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f);
        }

        {
            // FIXME separate logic from ui configuration
            //axis range
            MinAndMaxEntries minAndMaxEntries = getMinAndMaxFromEntryList(historicalValues);
            initializeGrowth(historicalValues);

            yAxis.setAxisMaximum((float) minAndMaxEntries.getMaxEntry().getAverage());
            yAxis.setAxisMinimum((float) minAndMaxEntries.getMinEntry().getAverage());

            // add data
            setData(historicalValues);
            // }
        }

        // draw points over time
        chart.animateX(1500);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // draw legend entries as lines
        l.setForm(LegendForm.LINE);
    }

    /**
     * Initializes boolean growth value. If the first value bigger than the last
     * value - the growth will be initialized with false, otherwise - true;
     *
     * @param historicalValues
     */
    private void initializeGrowth(ArrayList<HistoricalDataEntry> historicalValues) {
        growth = historicalValues.get(0).getAverage() > historicalValues.get(historicalValues.size() - 1).getAverage() ? false : true;
    }

    /**
     * @param historicalValues
     * @return
     */
    public ArrayList<Entry> extractPricesFromHistoricalData(ArrayList<HistoricalDataEntry>
                                                                    historicalValues) {
        ArrayList<Entry> output = new ArrayList<>();
        for (int i = 0; i < historicalValues.size(); i++) {
            output.add(new Entry(i, (float) historicalValues.get(i).getAverage(), getResources()
                    .getDrawable(R.drawable.star)));
        }
        return output;
    }

    /**
     * @param historicalValues
     * @return
     */
    public String[] extractTimeFromHistoricalData(ArrayList<HistoricalDataEntry>
                                                          historicalValues) {
        int historicalValuesSize = historicalValues.size();
        String[] output = new String[historicalValuesSize];
        for (int i = 0; i < historicalValuesSize; i++) {
            output[i] = (historicalValues.get(i).getTime());
        }
        return output;
    }

    /**
     * Sets the data to the chart.
     *
     * @param historicalValues
     */
    private void setData(ArrayList<HistoricalDataEntry> historicalValues) {
        List<Entry> historicalPrices = extractPricesFromHistoricalData(historicalValues);

        LineDataSet lineDataSet;
        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            lineDataSet = (LineDataSet) chart.getData().getDataSetByIndex(0);
            lineDataSet.setValues(historicalPrices);
            lineDataSet.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            lineDataSet = new LineDataSet(historicalPrices, "");

            // don't draw icons
            lineDataSet.setDrawIcons(false);

            // don't draw value
            lineDataSet.setDrawValues(false);

            // don't draw circles
            lineDataSet.setDrawCircles(false);

            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            // black lines and points
            lineDataSet.setColor(Color.BLACK);
            lineDataSet.setCircleColor(Color.BLACK);

            // line thickness and point size
            lineDataSet.setLineWidth(1f);
            lineDataSet.setCircleRadius(3f);

            // draw points as solid circles
            lineDataSet.setDrawCircleHole(false);

            // customize legend entry
            lineDataSet.setFormLineWidth(1f);
            lineDataSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            lineDataSet.setFormSize(15.f);

            // text size of values
            lineDataSet.setValueTextSize(9f);

            // draw selection line as dashed
            lineDataSet.enableDashedHighlightLine(10f, 5f, 0f);

            // set the filled area
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillFormatter((dataSet, dataProvider) -> chart.getAxisLeft().getAxisMinimum());

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                // TODO color depending on rate (red - decrease, green - increase)
                Drawable drawable = ContextCompat.getDrawable(this, growth ? R.drawable.fade_green : R.drawable.fade_red);
                lineDataSet.setFillDrawable(drawable);
            } else {
                lineDataSet.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(lineDataSet); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            chart.setData(data);
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());
        Log.i("LOW HIGH", "low: " + chart.getLowestVisibleX()
                + ", high: " + chart.getHighestVisibleX());
        Log.i("MIN MAX", "xMin: " + chart.getXChartMin()
                + ", xMax: " + chart.getXChartMax() + ", yMin: " + chart.getYChartMin()
                + ", yMax: " + chart.getYChartMax());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

    class MinAndMaxEntries {
        HistoricalDataEntry minEntry;
        HistoricalDataEntry maxEntry;

        MinAndMaxEntries(HistoricalDataEntry minEntry, HistoricalDataEntry maxEntry) {
            this.minEntry = minEntry;
            this.maxEntry = maxEntry;
        }

        HistoricalDataEntry getMinEntry() {
            return minEntry;
        }

        HistoricalDataEntry getMaxEntry() {
            return maxEntry;
        }
    }
}
