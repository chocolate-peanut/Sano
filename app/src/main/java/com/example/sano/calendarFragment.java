package com.example.sano;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.lang.String.valueOf;
import static java.time.temporal.ChronoUnit.DAYS;

public class calendarFragment extends Fragment implements CalendarAdapter.OnItemListener{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    public LocalDate selectedDate;
    public static String clickedDate;
    private String periodStartDate,periodEndDate;
    periodMood periodMood;
    public int averageDay;
    List<String> startDate = new ArrayList<>();
    List<String> endDate = new ArrayList<>();
    List<Integer> day = new ArrayList<>();

    public calendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        //initWidget
        calendarRecyclerView=view.findViewById((R.id.calendarRecyclerView));
        calendarRecyclerView.setLayoutManager((new LinearLayoutManager(getContext())));
        monthYearText = view.findViewById((R.id.monthYearTV));
        //
        selectedDate = LocalDate.now();
        TextView showAverage = view.findViewById(R.id.showAverageCycleLength);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //show cycle length
        Task<QuerySnapshot> documentReference = firestore.collection("periodDate").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        startDate.add(document.getString("StartDate"));
                        endDate.add(document.getString("EndDate"));
                    }
                }

                if(startDate!=null){
                    for(int x=0; x<startDate.size(); x++){
                        LocalDate start = LocalDate.parse(startDate.get(x), formatter);
                        LocalDate end = LocalDate.parse(endDate.get(x), formatter);
                        int daysBetween = (int) DAYS.between(start,end);
                        day.add(daysBetween);
                    }
                }
                int calculate = 0;
                for(int x =0; x < day.size() ; x++){

                    calculate = day.get(x) + calculate;
                }
                averageDay = calculate/(day.size());
                showAverage.setText(valueOf(averageDay));
            }
        });

        //setMonthView();
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

        Button endButton = view.findViewById(R.id.endBtn);
        endButton.setOnClickListener(this::endPeriodAction);

        Button startButton = view.findViewById(R.id.startBtn);
        startButton.setOnClickListener(this::startPeriodAction);

        Button nextMonthButton = view.findViewById(R.id.nextMonthBtn);
        nextMonthButton.setOnClickListener(this::nextMonthAction);

        Button previousMonthButton = view.findViewById(R.id.previousMonthBtn);
        previousMonthButton.setOnClickListener(this::previousMonthAction);

        new CalendarAdapter.OnItemListener() {
            @Override
            public void onItemClick(int position, String dayText) {
                if(!dayText.equals(""))
                {
                    //show next page

                    Intent intent = new Intent(getActivity(), PeriodMoodFragment.class);
                    startActivity(intent);
                    String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
                    clickedDate = dayText +"-"+ monthYearFromDate(selectedDate);
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                }
            }
        };

        Button activityLogButton = view.findViewById(R.id.activityLogBtn);
        activityLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), periodDateFragment.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    public ArrayList<String> daysInMonthArray(LocalDate date)
    {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add("");
            }
            else
            {
                daysInMonthArray.add(valueOf(i - dayOfWeek));
            }
        }
        return  daysInMonthArray;
    }

    static String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-yyyy");
        return date.format(formatter);
    }

    public void previousMonthAction(View view)
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view)
    {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    public void startPeriodAction(View view)
    {
        periodMood=new periodMood();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        LocalDate today = java.time.LocalDate.now();
        DocumentReference documentReference = firestore.collection("periodDate").document(monthYearFromDate(today));
        String formattedDate = today.format(DateTimeFormatter.ofPattern("dd-MMM-yy"));
        periodMood.setStartDate(selectedDate);
        periodStartDate = periodMood.getStartDate().toString();
        periodEndDate = today.plusDays(5).toString();
        Map<String,Object> periodDate=new HashMap<>();
        periodDate.put("StartDate",periodStartDate);
        periodDate.put("EndDate",periodEndDate);
        firestore.collection("StartDate").document(monthYearFromDate(selectedDate)).update(periodDate);
        documentReference.set(periodDate, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getContext(), "Start Date :" + periodStartDate, Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void endPeriodAction(View view)
    {
        periodMood=new periodMood();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        LocalDate today = java.time.LocalDate.now();
        DocumentReference documentReference = firestore.collection("periodDate").document(monthYearFromDate(today));
        String formattedDate = today.format(DateTimeFormatter.ofPattern("dd-MMM-yy"));
        periodMood.setEndDate(selectedDate);
        periodEndDate = periodMood.getEndDate().toString();
        Map<String,Object> periodDate=new HashMap<>();
        periodDate.put("EndDate",periodEndDate);
        firestore.collection("EndDate").document(monthYearFromDate(selectedDate)).update(periodDate);
        documentReference.set(periodDate, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getContext(), "End Date :" + periodEndDate, Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(int position, String dayText) {
        Intent intent = new Intent(getActivity(), PeriodMoodFragment.class);
        startActivity(intent);
        String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
        clickedDate = dayText +"-"+ monthYearFromDate(selectedDate);
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}