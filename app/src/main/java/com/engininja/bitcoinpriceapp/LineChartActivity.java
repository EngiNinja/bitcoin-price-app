package com.engininja.bitcoinpriceapp;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.util.Log;

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
import java.util.List;

/**
 * Example of a heavily customized {@link LineChart} with limit lines, custom line shapes, etc.
 *
 * @version 3.1.0
 * @since 1.7.4
 */
public class LineChartActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    private LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linechart);

        Bundle bundle = getIntent().getExtras();
        ArrayList<HistoricalDataEntry> historicalValues = bundle
                .getParcelableArrayList("historicalData");
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

    class MinAndMaxEntries {
        HistoricalDataEntry minEntry;
        HistoricalDataEntry maxEntry;

        public MinAndMaxEntries(HistoricalDataEntry minEntry, HistoricalDataEntry maxEntry) {
            this.minEntry = minEntry;
            this.maxEntry = maxEntry;
        }

        public HistoricalDataEntry getMinEntry() {
            return minEntry;
        }

        public HistoricalDataEntry getMaxEntry() {
            return maxEntry;
        }
    }

    /**
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

            // create marker to display box when values are selected
            MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

            // Set the marker to the chart
            mv.setChartView(chart);
            chart.setMarker(mv);

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
        }

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = chart.getAxisLeft();

            // disable dual axis (only use LEFT axis)
            chart.getAxisRight().setEnabled(false);

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f);

            // TODO set x values

        }

        {
            //TODO Refactor Logic {
            //axis range
            MinAndMaxEntries minAndMaxEntries = getMinAndMaxFromEntryList(historicalValues);

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

    public ArrayList<Entry> extractPricesFromHistoricalData(ArrayList<HistoricalDataEntry>
                                                                    historicalValues) {
        ArrayList<Entry> output = new ArrayList<>();
        for (int i = 0; i < historicalValues.size(); i++) {
            // TODO unsafe conversion?
            output.add(new Entry(i, (float) historicalValues.get(i).getAverage(), getResources()
                    .getDrawable(R.drawable.star)));
        }
        return output;
    }

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
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
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
}
