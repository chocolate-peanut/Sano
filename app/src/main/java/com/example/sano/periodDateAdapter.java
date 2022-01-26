package com.example.sano;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class periodDateAdapter extends RecyclerView.Adapter<periodDateAdapter.periodDateHolder> {

    List<String> StartDate;
    List<String> EndDate;

    public periodDateAdapter(Object o){}

    public periodDateAdapter(List<String> StartDate, List<String> EndDate){
        this.StartDate = StartDate;
        this.EndDate = EndDate;
    }

    @NonNull
    @Override
    public periodDateAdapter.periodDateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.perioddate_review, parent, false);
        return new periodDateHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull periodDateAdapter.periodDateHolder holder, int position) {
            holder.StartDate.setText(StartDate.get(position));
            holder.EndDate.setText(EndDate.get(position));
            System.out.println(StartDate.get(position));
            System.out.println(EndDate.get(position));
    }

    @Override
    public int getItemCount() {
        return StartDate.size();
    }

    public class periodDateHolder extends RecyclerView.ViewHolder{
        TextView StartDate;
        TextView EndDate;
        View view;

        public periodDateHolder(@NonNull View itemView){
            super(itemView);

            StartDate = itemView.findViewById(R.id.startDate);
            EndDate = itemView.findViewById(R.id.endDate);
            view = itemView;
        }
    }
}
