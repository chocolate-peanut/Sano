package com.example.sano;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class periodDateFragment extends AppCompatActivity {

    RecyclerView dateList;
    FirebaseFirestore firestore;
    FirestoreRecyclerAdapter<periodDate, dateViewHolder> dateAdapter;

    public periodDateFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perioddate_layout);

        firestore = FirebaseFirestore.getInstance();
        Query query = firestore.collection("periodDate").orderBy("StartDate", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<periodDate> allDates = new FirestoreRecyclerOptions.Builder<periodDate>()
                .setQuery(query, periodDate.class)
                .build();

        dateAdapter = new FirestoreRecyclerAdapter<periodDate, dateViewHolder>(allDates) {
            @Override
            protected void onBindViewHolder(@NonNull dateViewHolder DateViewHolder, int i, @NonNull periodDate periodDateClass) {
                DateViewHolder.StartDate.setText(periodDateClass.getStartDate());
                DateViewHolder.EndDate.setText(periodDateClass.getEndDate());
            }

            @NonNull
            @Override
            public dateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
                View theView = LayoutInflater.from(parent.getContext()).inflate(R.layout.perioddate_review, parent, false);
                return new dateViewHolder(theView);
            }
        };
        dateList = findViewById(R.id.periodDate_recyclerView);
        dateList.setAdapter(dateAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        dateList.setLayoutManager(layoutManager);
    }

    public class dateViewHolder extends RecyclerView.ViewHolder{
        TextView StartDate;
        TextView EndDate;
        View view;

        public dateViewHolder(@NonNull View itemView){
            super(itemView);
            StartDate = itemView.findViewById(R.id.startDate);
            EndDate = itemView.findViewById(R.id.endDate);
            view = itemView;
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        dateAdapter.startListening();
    }

    @Override
    public void onStop(){
        super.onStop();
        if(dateAdapter!=null){
            dateAdapter.stopListening();
        }
    }


}