package com.example.sano;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import static java.time.temporal.ChronoUnit.DAYS;

public class AnalysisFragment extends Fragment {

    double BMI, BMR, RMR;
    double height, weight, birthYear, heightCM;
    TextView bmi, bmr, rmr, category, dataAvail;
    Button startdateButton;
    DatePickerDialog.OnDateSetListener onDateSetListenerStart;
    public String fromDate;
    LineChart graphView1;
    LineData lineData;
    PieChart pieChart;
    PieData pieData;
    List<Entry> entryList = new ArrayList<>();
    List<PieEntry> pieEntryList = new ArrayList<>();
    List<String> startDateStr = new ArrayList<>();
    List<String> endDateStr = new ArrayList<>();
    List<Integer> periodMonth = new ArrayList<>();
    List<Integer> periodLength = new ArrayList<>();

    public AnalysisFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_analysis, container, false);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        startdateButton = (Button) view.findViewById(R.id.startDateButton);
        bmi = (TextView) view.findViewById(R.id.bmi);
        bmr = (TextView) view.findViewById(R.id.bmr);
        rmr = (TextView) view.findViewById(R.id.rmr);
        dataAvail = (TextView) view.findViewById(R.id.dataAvailable);
        category = (TextView) view.findViewById(R.id.category);
        graphView1 = (LineChart) view.findViewById(R.id.graphview1);
        pieChart = (PieChart) view.findViewById(R.id.pieChart);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DocumentReference infoRef = firestore.collection("userInfo").document("info");
        DocumentReference ageRef = firestore.collection("birthYear").document("by");
        infoRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    if (value.getLong("Height") != null) {
                        height = value.getDouble("Height").doubleValue();
                        heightCM = value.getDouble("Height").doubleValue();
                    }
                    if (value.getLong("Weight") != null) {
                        weight = value.getDouble("Weight").doubleValue();
                    }
                    height = height / 100;
                    height = height * height;
                    BMI = weight / height;
                    bmi.setText(String.format("%.2f", BMI));
                    if (BMI < 20)
                        category.setText("Under Weight");
                    else if (20 <= BMI && BMI < 25)
                        category.setText("Normal Weight");
                    else if (25 <= BMI && BMI < 30)
                        category.setText("Over Weight");
                    else
                        category.setText("Obese");

                    ageRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (value.exists()) {
                                if (value.getLong("birthYear") != null) {
                                    birthYear = value.getDouble("birthYear").doubleValue();
                                    double year = Calendar.getInstance().get(Calendar.YEAR);
                                    double age = year-birthYear;
                                    BMR = 447.593 + (9.247 * weight) + (3.098 * heightCM) - (4.330 * age);
                                    RMR = 9.99 * weight + 6.25 * heightCM - 4.92 * age - 161;
                                    bmr.setText(String.format("%.2f", BMR));
                                    rmr.setText(String.format("%.2f", RMR));
                                }
                            }
                        }});
                }
            }
        });

        Task<QuerySnapshot> documentReference = firestore.collection("periodDate")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                startDateStr.add(document.getString("StartDate"));
                                endDateStr.add(document.getString("EndDate"));
                            }
                        }
                        ArrayList<Date> startDateList = new ArrayList<Date>();
                        ArrayList<Date> endDateList = new ArrayList<Date>();
                        ArrayList<String> startDateListStr = new ArrayList<String>();
                        ArrayList<String> endDateListStr = new ArrayList<String>();
                        if(startDateStr != null){
                            for (String dateString : startDateStr) {
                                try {
                                    startDateList.add(df.parse(dateString));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            for (String dateString : endDateStr) {
                                try {
                                    endDateList.add(df.parse(dateString));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            Collections.sort(startDateList);
                            Collections.sort(endDateList);
                            for(int i=0; i < startDateStr.size(); i++){
                                startDateListStr.add(df.format(startDateList.get(i)));
                                endDateListStr.add(df.format(endDateList.get(i)));
                            }
                            for(int x=0; x < startDateStr.size(); x++){

                                LocalDate start = LocalDate.parse(startDateListStr.get(x), formatter);
                                LocalDate end = LocalDate.parse(endDateListStr.get(x), formatter);
                                int daysBetween = (int) DAYS.between(start,end);
                                periodLength.add(daysBetween);
                                String sub = startDateListStr.get(x).substring(5,7);
                                int number = Integer.parseInt(sub);
                                periodMonth.add(number);
                            }
                        }
                        int count = 0, index = 0;
                        for (int i = 0 ; i <periodMonth.size(); i++){
                            index = duplicate(periodMonth, periodMonth.get(i));
                            System.out.println(index);
                            if(periodMonth.get(0) > periodMonth.get(i) || count <= 12 && index > 1 ){
                                entryList.add(new Entry(periodMonth.get(i) +12 , periodLength.get(i)));
                                count ++;
                            }
                            else if (count > 12 && count <= 24){
                                entryList.add(new Entry(periodMonth.get(i) +24 , periodLength.get(i)));
                                count ++;
                            }
                            else{
                                entryList.add(new Entry(periodMonth.get(i), periodLength.get(i)));
                            }
                        }
                        removeEntry(entryList, periodLength);
                        LineDataSet lineDataSet = new LineDataSet(entryList, "Dataset");
                        lineDataSet.setLineWidth(3f);
                        lineDataSet.setColors(Color.BLUE);
                        lineDataSet.setValueTextSize(15f);
                        lineDataSet.setValueTextColor(Color.RED);
                        lineDataSet.setCircleColor(Color.CYAN);
                        lineDataSet.setDrawCircles(true);
                        lineDataSet.setDrawCircleHole(true);
                        lineDataSet.setCircleRadius(10);

                        lineData = new LineData(lineDataSet);
                        lineData.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {
                                return String.valueOf((int) Math.floor(value));
                            }
                        });
                        graphView1.setData(lineData);
                        XAxis xAxis = graphView1.getXAxis();
                        YAxis yAxis = graphView1.getAxisLeft();
                        YAxis yAxis2 = graphView1.getAxisRight();
                        final String[] labels = new String[]{"Dummy","Jan", "Feb", "March", "April", "May",
                                "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec","Jan", "Feb", "March", "April", "May",
                                "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec","Jan", "Feb", "March", "April", "May",
                                "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"}; //Dummy

                        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                        xAxis.setGranularity(1f);
                        xAxis.setTextSize(15f);
                        yAxis.setTextSize(15f);
                        yAxis2.setTextSize(15f);
                        xAxis.setGranularityEnabled(true);
                        graphView1.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
                        graphView1.getXAxis().setDrawLabels(true);
                        graphView1.getLegend().setEnabled(false);
                        graphView1.getDescription().setEnabled(false);
                        graphView1.setAutoScaleMinMaxEnabled(true);
                    }
                });

        final Calendar calendar = Calendar.getInstance();
        Date currentTime = Calendar.getInstance().getTime();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String formattedDate = df.format(currentTime.getTime());
        startdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(), android.R.style.Theme_Holo_Dialog_MinWidth,
                        onDateSetListenerStart, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        onDateSetListenerStart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                if(month < 10 && dayOfMonth >=10){
                    fromDate = year + "-0" + month + "-" + dayOfMonth;
                }
                else if(dayOfMonth < 10 && month >=10){
                    fromDate = year + "-" + month + "-0" + dayOfMonth;
                }
                else if (month < 10 && dayOfMonth < 10){
                    fromDate = year + "-0" + month + "-0" + dayOfMonth;
                }
                else{
                    fromDate = year + "-" + month + "-" + dayOfMonth;
                }
                startdateButton.setText(fromDate);
                firestore
                        .collection("periodMood")
                        .whereGreaterThanOrEqualTo("date", fromDate)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int sad=0, happy=0, neutral=0, angry=0;
                                    sad=0; happy=0; neutral=0; angry=0;
                                    for (DocumentSnapshot document : task.getResult()) {

                                        if(document.getString("mood").equals("sad")){
                                            sad++;
                                        }
                                        else if(document.getString("mood").equals("happy")){
                                            happy++;
                                        }
                                        else if(document.getString("mood").equals("neutral")){
                                            neutral++;
                                        }
                                        else if(document.getString("mood").equals("angry")){
                                            angry++;
                                        }
                                    }

                                    pieEntryList.clear();
                                    if(sad > 0 || happy > 0 || neutral > 0 || angry > 0 ) {
                                        if (sad > 0)
                                            pieEntryList.add(new PieEntry(sad, "Sad"));
                                        if (happy > 0)
                                            pieEntryList.add(new PieEntry(happy, "Happy"));
                                        if (neutral > 0)
                                            pieEntryList.add(new PieEntry(neutral, "Neutral"));
                                        if (angry > 0)
                                            pieEntryList.add(new PieEntry(angry, "Angry"));

                                        dataAvail.setVisibility(TextView.INVISIBLE);
                                        pieChart.setVisibility(View.VISIBLE);
                                    }
                                    else
                                    {
                                        dataAvail.setVisibility(TextView.VISIBLE);
                                        pieChart.setVisibility(View.INVISIBLE);
                                    }
                                    sad=0; happy=0; neutral=0; angry=0;
                                    PieDataSet pieDataSet = new PieDataSet(pieEntryList, "Mood");
                                    pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                                    pieData = new PieData(pieDataSet);
                                    pieData.setValueFormatter(new ValueFormatter() {
                                        @Override
                                        public String getFormattedValue(float value) {
                                            return String.valueOf((int) Math.floor(value));
                                        }
                                    });
                                    pieData.setValueFormatter(new PercentFormatter());
                                    pieData.setValueTextSize(20f);
                                    pieChart.setData(pieData);
                                    pieChart.setEntryLabelTextSize(20f);
                                    pieChart.getLegend().setEnabled(false);
                                    pieChart.setEntryLabelColor(Color.BLACK);
                                    pieChart.getDescription().setEnabled(false);
                                    pieChart.setUsePercentValues(true);
                                    pieChart.getLegend().setFormSize(20f);
                                    pieChart.getLegend().setTextSize(20f);
                                    pieChart.invalidate();
                                }
                            }
                        });
            }
        };
        return view;
    }

    void removeEntry(List<Entry> entryList, List<Integer> array)
    {
        int counter = array.size()-12;
        for (int i=0; i< array.size(); i++){
            if(array.size() > 12 && counter != 0){
                entryList.remove(0);
                counter--;
            }
        }
    }

    int duplicate(List<Integer>array, Integer what) {
        int count = 0;
        for (int i = 0; i < array.size(); i++) {
            if (array.equals(what)) {
                count++;
            }
        }
        return count;
    }
}