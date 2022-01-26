package com.example.sano;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    //this class has no usage, just for reserved.

    List<String> content;
    List<String> createdDate;

    public RecyclerViewAdapter(Object o){}

    public RecyclerViewAdapter(List<String> content, List<String> createdDate){
        this.content = content;
        this.createdDate = createdDate;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_rview, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {

        holder.diary_content.setText(content.get(position));
        holder.diary_date.setText(createdDate.get(position));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "You clicked this.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return content.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView diary_content;
        TextView diary_date;
        View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            diary_content = itemView.findViewById(R.id.diary_content);
            diary_date = itemView.findViewById(R.id.diary_date);
            view = itemView;
        }
    }
}