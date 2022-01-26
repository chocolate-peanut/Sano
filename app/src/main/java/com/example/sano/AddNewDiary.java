package com.example.sano;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddNewDiary extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, LifecycleObserver {

    FirebaseFirestore firestore;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_diary);

        firestore = FirebaseFirestore.getInstance();

        EditText write_anything = findViewById(R.id.write_anything);
        TextView date_view = findViewById(R.id.date_view);
        Button save_diary_button = findViewById(R.id.save_diary_button);
        Button back_button = findViewById(R.id.back_button);
        Button date_button = findViewById(R.id.date_button);

        date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new com.example.sano.DatePicker();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        save_diary_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = write_anything.getText().toString().trim();
                String createdDate = date_view.getText().toString().trim();

                if (content.isEmpty() || createdDate.isEmpty()){
                    Toast.makeText(AddNewDiary.this, "Please fill in all required field.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //save data
                DocumentReference documentReference = firestore.collection("diaries").document();
                Map<String, Object> diary = new HashMap<>();
                diary.put("Content", content);
                diary.put("CreatedDate", createdDate);
                documentReference.set(diary, SetOptions.merge());

                documentReference.set(diary).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void unused) {
                        Toast.makeText(AddNewDiary.this, "Saved successfully.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddNewDiary.this, "Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });

                finish();

            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        TextView date_view = findViewById(R.id.date_view);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        date_view.setText(currentDate);
    }

    /*@Override
    public void onBackPressed() {
        finish();
    }*/

}