package com.example.livi;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private LineChart lineChartTemperature, lineChartHumidity;
    private TextView tvTempValue, tvHumidityValue, tvGasLevel, tvFanStatus, tvFlameStatus;
    private ImageView imgFlameStatus, imgFanStatus;
    private BarChart barChartGasLevel;
    private LineData lineDataTemperature, lineDataHumidity;
    private LineDataSet lineDataSetTemperature, lineDataSetHumidity;
    private ArrayList<Entry> lineEntriesTemperature, lineEntriesHumidity;
    private BarEntry barEntryGasLevel;
    private BarData barDataGasLevel;
    private BarDataSet barDataSetGasLevel;
    private DatabaseReference databaseReferenceTemperature, databaseReferenceHumidity, databaseReferenceGasLevel, databaseReferenceFlame, databaseReferenceFans;
    private int timeIndexTemperature = 0;
    private int timeIndexHumidity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lineChartTemperature = findViewById(R.id.lineChartTemperature);
        lineChartHumidity = findViewById(R.id.lineChartHumidity);
        barChartGasLevel = findViewById(R.id.barChartGasLevel);
        tvTempValue = findViewById(R.id.tvTempValue);
        tvHumidityValue = findViewById(R.id.tvHumidityValue);
        tvGasLevel = findViewById(R.id.tvGasLevelValue);
        imgFlameStatus = findViewById(R.id.imgFlameStatus);
        imgFanStatus = findViewById(R.id.imgFanStatus);
        tvFanStatus = findViewById(R.id.tvFanStatus);
        tvFlameStatus = findViewById(R.id.tvFireStatus);

        initializeTemperatureChart();
        initializeHumidityChart();
        initializeGasLevelChart();

        databaseReferenceTemperature = FirebaseDatabase.getInstance().getReference().child("Sensor/temperature");
        databaseReferenceHumidity = FirebaseDatabase.getInstance().getReference().child("Sensor/humidity");
        databaseReferenceGasLevel = FirebaseDatabase.getInstance().getReference().child("Sensor/gasPercentage");
        databaseReferenceFlame = FirebaseDatabase.getInstance().getReference().child("Sensor/flameDetected");
        databaseReferenceFans = FirebaseDatabase.getInstance().getReference().child("Actuator/fanState");

        // Temperature data listener
        databaseReferenceTemperature.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    float temperature = snapshot.getValue(Float.class);
                    addTemperatureEntry(temperature);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });

        // Humidity data listener
        databaseReferenceHumidity.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    float humidity = snapshot.getValue(Float.class);
                    addHumidityEntry(humidity);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });

        // Gas level data listener
        databaseReferenceGasLevel.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    float gasLevel = snapshot.getValue(Float.class);
                    updateGasLevelEntry(gasLevel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });

        // Flame detection listener
        databaseReferenceFlame.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    boolean flameDetected = snapshot.getValue(Boolean.class);
                    if (flameDetected) {
                        imgFlameStatus.setImageResource(R.drawable.flame_yes);
                        tvFlameStatus.setText("Fire detected!");
                    } else {
                        imgFlameStatus.setImageResource(R.drawable.flame_no);
                        tvFlameStatus.setText("No Fire detected!");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });

        //fan status
        databaseReferenceFans.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    boolean fanOn = snapshot.getValue(Boolean.class);
                    if (fanOn) {
                        imgFanStatus.setImageResource(R.drawable.fan_on);
                        tvFanStatus.setText("Exhaust fans are operating");
                    } else {
                        imgFanStatus.setImageResource(R.drawable.fan_off);
                        tvFanStatus.setText("Exhaust fans are not operating");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }

    private void initializeTemperatureChart() {
        lineEntriesTemperature = new ArrayList<>();
        lineDataSetTemperature = new LineDataSet(lineEntriesTemperature, "Temperature");
        lineDataSetTemperature.setDrawCircles(false);
        lineDataSetTemperature.setLineWidth(2f);
        lineDataSetTemperature.setColor(getResources().getColor(R.color.colorPrimary));
        lineDataSetTemperature.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        lineDataTemperature = new LineData(lineDataSetTemperature);
        lineChartTemperature.setData(lineDataTemperature);
        lineChartTemperature.getDescription().setEnabled(false);
        lineChartTemperature.getLegend().setEnabled(false);

        XAxis xAxis = lineChartTemperature.getXAxis();
        xAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(false);

        lineChartTemperature.getAxisLeft().setAxisMinimum(0f);
        lineChartTemperature.getAxisLeft().setAxisMaximum(120f);
        lineChartTemperature.getAxisRight().setEnabled(false);
        lineChartTemperature.invalidate();
    }

    private void initializeHumidityChart() {
        lineEntriesHumidity = new ArrayList<>();
        lineDataSetHumidity = new LineDataSet(lineEntriesHumidity, "Humidity");
        lineDataSetHumidity.setDrawCircles(false);
        lineDataSetHumidity.setLineWidth(2f);
        lineDataSetHumidity.setColor(getResources().getColor(R.color.colorPrimary));
        lineDataSetHumidity.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        lineDataHumidity = new LineData(lineDataSetHumidity);
        lineChartHumidity.setData(lineDataHumidity);
        lineChartHumidity.getDescription().setEnabled(false);
        lineChartHumidity.getLegend().setEnabled(false);

        XAxis xAxis = lineChartHumidity.getXAxis();
        xAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(false);

        lineChartHumidity.getAxisLeft().setAxisMinimum(0f);
        lineChartHumidity.getAxisLeft().setAxisMaximum(100f);
        lineChartHumidity.getAxisRight().setEnabled(false);
        lineChartHumidity.invalidate();
    }

    private void initializeGasLevelChart() {
        // Create a single bar entry for gas level
        barEntryGasLevel = new BarEntry(0f, 0f); // Initially set to 0
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(barEntryGasLevel);

        barDataSetGasLevel = new BarDataSet(barEntries, "Gas Level");
        barDataSetGasLevel.setColor(getResources().getColor(R.color.green));
        barDataSetGasLevel.setDrawValues(false); // Hide values on the bars if needed

        barDataGasLevel = new BarData(barDataSetGasLevel);
        barChartGasLevel.setData(barDataGasLevel);
        barChartGasLevel.getDescription().setEnabled(false);
        barChartGasLevel.getLegend().setEnabled(false);

        // Configure X-axis to not draw values at the top
        XAxis xAxis = barChartGasLevel.getXAxis();
        xAxis.setDrawLabels(false); // Disable labels on the X-axis
        xAxis.setDrawGridLines(false); // Disable grid lines

        barChartGasLevel.getAxisLeft().setAxisMinimum(0f);
        barChartGasLevel.getAxisLeft().setAxisMaximum(100f);
        barChartGasLevel.getAxisRight().setEnabled(false);
        barChartGasLevel.invalidate();
    }


    @SuppressLint("DefaultLocale")
    private void addTemperatureEntry(float temperature) {

        // Add new entry to the temperature data
        lineEntriesTemperature.add(new Entry(timeIndexTemperature++, temperature));


        // Log the size of the entries
        Log.d("Temperature Entries", "Entries size: " + lineEntriesTemperature.size());

        // Notify the dataset that data has changed
        lineDataSetTemperature.notifyDataSetChanged();
        lineDataTemperature.notifyDataChanged();
        lineChartTemperature.notifyDataSetChanged();

        // Update the chart and move the view to the latest entry
        lineChartTemperature.setVisibleXRangeMaximum(5);
        lineChartTemperature.moveViewToX(lineDataTemperature.getEntryCount());
        // Smoothly transition to the new data point
        lineChartTemperature.moveViewToAnimated(timeIndexTemperature - 1, lineChartTemperature.getYChartMax() / 2, YAxis.AxisDependency.LEFT, 1000);


        // Update the temperature text in the card view
        tvTempValue.setText(String.format("%.2f°C", temperature));

        // Change the chart's background color and line properties based on temperature range
        if (temperature >= 0 && temperature <= 50) {
            lineChartTemperature.setBackgroundColor(getResources().getColor(R.color.light_green));
            lineDataSetTemperature.setColor(getResources().getColor(R.color.green));
            lineDataSetTemperature.setLineWidth(2f);
        } else if (temperature >= 51 && temperature <= 70) {
            lineChartTemperature.setBackgroundColor(getResources().getColor(R.color.light_yellow));
            lineDataSetTemperature.setColor(getResources().getColor(R.color.yellow));
            lineDataSetTemperature.setLineWidth(3f);
        } else if (temperature >= 71 && temperature <= 120) {
            lineChartTemperature.setBackgroundColor(getResources().getColor(R.color.light_red));
            lineDataSetTemperature.setColor(getResources().getColor(R.color.red));
            lineDataSetTemperature.setLineWidth(4f);
        }
        lineDataSetTemperature.setDrawValues(false);
        lineChartTemperature.invalidate();
    }

    @SuppressLint("DefaultLocale")
    private void addHumidityEntry(float humidity) {

        // Add new entry to the humidity data
        lineEntriesHumidity.add(new Entry(timeIndexHumidity++, humidity));

        // Log the size of the entries
        Log.d("Humidity Entries", "Entries size: " + lineEntriesHumidity.size());

        lineDataSetHumidity.notifyDataSetChanged();
        lineDataHumidity.notifyDataChanged();
        lineChartHumidity.notifyDataSetChanged();

        // Update the chart and move the view to the latest entry
        lineChartHumidity.setVisibleXRangeMaximum(5);
        lineChartHumidity.moveViewToX(lineDataHumidity.getEntryCount());
        // Smoothly transition to the new data point
        lineChartHumidity.moveViewToAnimated(timeIndexHumidity - 1, lineChartHumidity.getYChartMax() / 2, YAxis.AxisDependency.LEFT, 1000);

        // Update the humidity text in the card view
        tvHumidityValue.setText(String.format("%.2f%%", humidity));

        // Change the chart's background color and line properties based on humidity range
        if (humidity >= 0 && humidity <= 50) {
            lineChartHumidity.setBackgroundColor(getResources().getColor(R.color.light_yellow));
            lineDataSetHumidity.setColor(getResources().getColor(R.color.yellow));
            lineDataSetHumidity.setLineWidth(3f);
        } else if (humidity >= 51 && humidity <= 80) {
            lineChartHumidity.setBackgroundColor(getResources().getColor(R.color.light_green));
            lineDataSetHumidity.setColor(getResources().getColor(R.color.green));
            lineDataSetHumidity.setLineWidth(2f);
        } else if (humidity >= 81 && humidity <= 100) {
            lineChartHumidity.setBackgroundColor(getResources().getColor(R.color.light_red));
            lineDataSetHumidity.setColor(getResources().getColor(R.color.red));
            lineDataSetHumidity.setLineWidth(4f);
        }

        lineDataSetHumidity.setDrawValues(false);
        lineChartHumidity.invalidate();
    }

    @SuppressLint("DefaultLocale")
    private void updateGasLevelEntry(float gasLevel) {
        // Update the current bar value for gas level (no new entries, just update the first one)
        barEntryGasLevel.setY(gasLevel);

        // Set bar color based on gas level
        if (gasLevel <= 40) {
            barDataSetGasLevel.setColor(getResources().getColor(R.color.green));
        } else if (gasLevel >= 41 && gasLevel <= 60) {
            barDataSetGasLevel.setColor(getResources().getColor(R.color.yellow));
        } else if (gasLevel >= 61 && gasLevel <= 100) {
            barDataSetGasLevel.setColor(getResources().getColor(R.color.red));
        }

        // Notify the dataset that data has changed
        barDataSetGasLevel.notifyDataSetChanged();
        barDataGasLevel.notifyDataChanged();
        barChartGasLevel.notifyDataSetChanged();

        // Smoothly move the chart view to the updated bar position using moveViewToAnimated
        barChartGasLevel.moveViewToAnimated(0, gasLevel, YAxis.AxisDependency.LEFT, 1000);

        // Update the gas level text in the card view
        tvGasLevel.setText(String.format("%.2f%%", gasLevel));
        Log.d("Gas Level", "Updated Gas Level: " + gasLevel);
    }
}