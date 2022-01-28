package com.example.sano;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.*;


public class DiaryFragment extends Fragment {

    RecyclerView diaryList;
    FirebaseFirestore firestore;
    FirestoreRecyclerAdapter<Model, DiaryViewHolder> diaryAdapter;

    public DiaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_diary, container, false);

        //initialize & assign variable[add new diary button in diary fragment]
        Button add_new_button = (Button) rootView.findViewById(R.id.add_new_button);
        add_new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddNewDiary.class);
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
        Query query = firestore.collection("diaries").orderBy("CreatedDate", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Model> allDiaries = new FirestoreRecyclerOptions.Builder<Model>()
                .setQuery(query, Model.class)
                .build();

        diaryAdapter = new FirestoreRecyclerAdapter<Model, DiaryViewHolder>(allDiaries) {
            @Override
            protected void onBindViewHolder(@NonNull DiaryViewHolder diaryViewHolder, int i, @NonNull Model model) {
                diaryViewHolder.diary_content.setText(model.getContent());
                diaryViewHolder.diary_date.setText(model.getCreatedDate());
                System.out.println(model.getCreatedDate());

                diaryViewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }

            @NonNull
            @Override
            public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View theview = LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_rview, parent, false);
                return new DiaryViewHolder(theview);
            }

        };

        diaryList = getActivity().findViewById(R.id.diary_recyclerView);
        diaryList.setAdapter(diaryAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        diaryList.setHasFixedSize(true);
        diaryList.setLayoutManager(layoutManager);
    }

    public class DiaryViewHolder extends RecyclerView.ViewHolder{
        TextView diary_content;
        TextView diary_date;
        View view;

        public DiaryViewHolder (@NonNull View itemView) {
            super(itemView);

            diary_content = itemView.findViewById(R.id.diary_content);
            diary_date = itemView.findViewById(R.id.diary_date);
            view = itemView;
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        diaryAdapter.startListening();
    }

    @Override
    public void onStop(){
        super.onStop();
        if (diaryAdapter != null){
            diaryAdapter.stopListening();
        }
    }
}