package com.example.sano;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class AddNewGoal extends AppCompatActivity {

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_goal);

        firestore = FirebaseFirestore.getInstance();

        EditText new_goal_text = findViewById(R.id.new_goal_text);
        Button back_to_goal_button = findViewById(R.id.back_to_goal_button);
        Button save_goal_button = findViewById(R.id.save_goal_button);

        save_goal_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String goal_content = new_goal_text.getText().toString().trim();

                if (goal_content.isEmpty()){
                    Toast.makeText(AddNewGoal.this, "Please fill in all required field.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    DocumentReference documentReference = firestore.collection("goals").document(goal_content);
                    Map<String, Object> goal = new HashMap<>();
                    goal.put("Goal", goal_content);
                    goal.put("GoalCheckbox", "false"); //default as false
                    documentReference.set(goal, SetOptions.merge());

                    documentReference.set(goal).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(@NonNull Void unused) {
                            Toast.makeText(AddNewGoal.this, "Saved successfully.", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddNewGoal.this, "Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                finish();

            }
        });

        back_to_goal_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}