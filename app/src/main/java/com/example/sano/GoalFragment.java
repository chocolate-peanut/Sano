package com.example.sano;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.common.api.internal.LifecycleCallback.getFragment;

public class GoalFragment extends Fragment {

    RecyclerView goalList;
    FirebaseFirestore firestore;
    FirestoreRecyclerAdapter<GoalModel, GoalViewHolder> goalAdapter;

    public GoalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_goal, container, false);

        Button new_goal_button = (Button) rootView.findViewById(R.id.new_goal_button);
        new_goal_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddNewGoal.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //--- retrieve data
        firestore = FirebaseFirestore.getInstance();

        Query query = firestore.collection("goals").orderBy("Goal", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<GoalModel> allGoals = new FirestoreRecyclerOptions.Builder<GoalModel>()
                .setQuery(query, GoalModel.class)
                .build();

        goalAdapter = new FirestoreRecyclerAdapter<GoalModel, GoalFragment.GoalViewHolder>(allGoals) {
            @Override
            protected void onBindViewHolder(@NonNull GoalViewHolder goalViewHolder, int i, @NonNull GoalModel goalModel) {
                //retrieve goal content
                goalViewHolder.goal_check.setText(goalModel.getGoal());
                //retrieve goal checkbox value
                if(goalModel.getGoalCheckbox() != null){
                    if (goalModel.getGoalCheckbox().equals("true")){
                        goalViewHolder.goal_check.performClick();
                    }
                }

                goalViewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(view.getContext(), "You clicked this.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public GoalFragment.GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_rview, parent, false);

                CheckBox goal_check_box = (CheckBox) view.findViewById(R.id.goal_checkBox);

                Map<String, Object> goalCheck = new HashMap<>();

                goal_check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(goal_check_box.isChecked()){
                            //get from program
                            goal_check_box.getText().toString();
                            System.out.println(goal_check_box.getText().toString());
                            DocumentReference documentReference = firestore.collection("goals").document(goal_check_box.getText().toString());
                            goalCheck.put("Goal", goal_check_box.getText().toString());
                            goalCheck.put("GoalCheckbox", "true");
                            //update to firestore
                            firestore.collection("Goal").document("goal_check_box.getText().toString()").update(goalCheck);
                            documentReference.set(goalCheck, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //Toast.makeText(getContext(), "Goal done", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else{
                            //untick - update to firestore
                            DocumentReference documentReference = firestore.collection("goals").document(goal_check_box.getText().toString());
                            goalCheck.put("Goal", goal_check_box.getText().toString());
                            goalCheck.put("GoalCheckbox", "false");
                            firestore.collection("Goal").document("goal_check_box.getText().toString()").update(goalCheck);
                            documentReference.set(goalCheck, SetOptions.merge());
                        }

                    }
                });

                return new GoalFragment.GoalViewHolder(view);

            }
        };
        //---

        goalList = getActivity().findViewById(R.id.goal_recyclerView);
        goalList.setAdapter(goalAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        goalList.setHasFixedSize(true);
        goalList.setLayoutManager(layoutManager);
    }

    public class GoalViewHolder extends RecyclerView.ViewHolder{
        TextView goal_content;
        CheckBox goal_check;
        View view;

        public GoalViewHolder (@NonNull View itemView){
            super(itemView);

            //goal_content = itemView.findViewById(R.id.goal_content);
            goal_check = itemView.findViewById(R.id.goal_checkBox);
            view = itemView;
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        goalAdapter.startListening();
    }

    @Override
    public void onStop(){
        super.onStop();
        if (goalAdapter != null){
            goalAdapter.stopListening();
        }
    }

}