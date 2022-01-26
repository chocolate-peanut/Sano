package com.example.sano;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import org.joda.time.PeriodType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.joda.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class CountDown extends AppCompatActivity {
    Button bt_pregnancy, bt_today, bt_calculate, bt_back;
    TextView tv_result;
    DatePickerDialog.OnDateSetListener dateSetListener1, dateSetListener2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);

        FirebaseFirestore fStore;
        fStore = FirebaseFirestore.getInstance();

        DocumentReference documentRef = fStore.collection("giveBirthDate").document("date");

        bt_pregnancy = findViewById(R.id.bt_pregnancy);
        bt_today = findViewById(R.id.bt_today);
        bt_calculate = findViewById(R.id.bt_calculate);
        tv_result = findViewById(R.id.tv_result);
        bt_back = findViewById(R.id.bt_back);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = simpleDateFormat.format(Calendar.getInstance().getTime());

        //Set the button to today's date
        bt_today.setText(date);

        //Retrieve the date of give birth from firebase
        documentRef.addSnapshotListener((value, error) -> {
            assert value != null;
            if (value.exists()) {
                if(value.getString("giveBirthDate")!=null){
                    bt_pregnancy.setText(value.getString("giveBirthDate"));
                }
            }
        });

        //Button to pick the date of give birth
        bt_pregnancy.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    CountDown.this
                    , android.R.style.Theme_Holo_Dialog_MinWidth
                    ,dateSetListener1,year,month,day
            );
            datePickerDialog.getWindow().setBackgroundDrawable(new
                    ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });

        //Get the date in dd/mm/yyyy format
        dateSetListener1 = (datePicker, year_p, month_p, day_p) -> {
            month_p = month_p + 1;
            String date_p = day_p + "/" + month_p + "/" + year_p;
            //Set the text of the button to the date selected
            bt_pregnancy.setText(date_p);
        };

        //Button to pick the starting date
        bt_today.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    CountDown.this
                    , android.R.style.Theme_Holo_Dialog_MinWidth
                    ,dateSetListener2,year,month,day
            );
            datePickerDialog.getWindow().setBackgroundDrawable(new
                    ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });

        //Get the date in dd/mm/yyyy format
        dateSetListener2 = (datePicker, year_t, month_t, day_t) -> {
            month_t = month_t + 1;
            String date_t = day_t + "/" + month_t + "/" + year_t;
            bt_today.setText(date_t);
        };

        //Button to calculate the duration between 2 dates
        bt_calculate.setOnClickListener(view -> {
            String eDate = bt_pregnancy.getText().toString();
            String sDate = bt_today.getText().toString();
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");

            try {
                Date date1 = simpleDateFormat1.parse(sDate);
                Date date2 = simpleDateFormat1.parse(eDate);
                assert date1 != null;
                long startDate = date1.getTime();
                assert date2 != null;
                long endDate = date2.getTime();

                //Validation start date should be less than end date
                if (startDate <= endDate) {
                    Period period;
                    period = new Period(startDate, endDate, PeriodType.yearMonthDay());
                    int years = period.getYears();
                    int months = period.getMonths();
                    int days = period.getDays();
                    //Value validation, the pregnancy period should be less than 11 months
                    if(years>=1 || months >=11){
                        Toast.makeText(getApplicationContext(),
                                "Pregnancy should be within 11 months.",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //Save the date of give birth to the firebase
                        Map<String, Object> giveBirthDate = new HashMap<>();
                        giveBirthDate.put("giveBirthDate", eDate);
                        documentRef.set(giveBirthDate).addOnSuccessListener(unused ->
                                makeText(CountDown.this, "Calculated",
                                        LENGTH_SHORT).show());
                        //Show the duration in the Result text view
                        tv_result.setText(months + "Months | " + days + "Days");
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "Start date should not be larger than today's date.",
                            Toast.LENGTH_SHORT).show();
                }
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}