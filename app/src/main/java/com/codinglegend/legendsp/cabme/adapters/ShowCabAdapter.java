package com.codinglegend.legendsp.cabme.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codinglegend.legendsp.cabme.R;
import com.codinglegend.legendsp.cabme.activity_book_now;
import com.codinglegend.legendsp.cabme.common;
import com.codinglegend.legendsp.cabme.models.ShowCabModel;

import java.util.List;

public class ShowCabAdapter extends RecyclerView.Adapter<ShowCabAdapter.CabHolder> {

    List<ShowCabModel> list;
    Context context;
    String activityName;

    public ShowCabAdapter(List<ShowCabModel> list, Context context, String activityName) {
        this.list = list;
        this.context = context;
        this.activityName = activityName;
    }

    @NonNull
    @Override
    public ShowCabAdapter.CabHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_sharecab_layout, parent, false);

        return new CabHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowCabAdapter.CabHolder holder, int position) {

        holder.fromAddress.setText(list.get(position).getCabFromAddress());
        holder.toAddress.setText(list.get(position).getCabToAdress());
        holder.time.setText(list.get(position).getCabTime());

        holder.bookNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context.startActivity(new Intent(context, activity_book_now.class)
                        .putExtra("cabId", list.get(position).getCabId()));

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CabHolder extends RecyclerView.ViewHolder {

        TextView fromAddress, toAddress, time;
        Button bookNowBtn;

        public CabHolder(@NonNull View itemView) {
            super(itemView);

            fromAddress = itemView.findViewById(R.id.tvFromCab);
            toAddress = itemView.findViewById(R.id.tvToCab);
            time = itemView.findViewById(R.id.tvTime);
            bookNowBtn = itemView.findViewById(R.id.bookNowBtn);


        }
    }
}
