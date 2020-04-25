package com.example.dronetracker2.ui.details;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dronetracker2.MainActivity;
import com.example.dronetracker2.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {


    private List<DetailItem> detailItems;
    private Context context;

    public Adapter(List<DetailItem> detailItems, Context context) {
        this.detailItems = detailItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_item, parent, false);

        final ViewHolder newViewHolder = new ViewHolder(v);

        newViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = newViewHolder.getAdapterPosition();
                ((MainActivity)context).OnDroneDetailSelected(detailItems.get(i));
            }
        });

        return newViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DetailItem detailItem = detailItems.get(position);

        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#d4d4d4"));
        }

        holder.textviewCallsign.setText(detailItem.getCallsign());
        holder.textviewCallsignPlaceHolder.setText(detailItem.getCallsignPlaceholder());
        holder.textViewGufi.setText(detailItem.getGufi());
        holder.textViewGufiPlaceHolder.setText(detailItem.getGufiPlaceHolder());
        holder.textViewLat.setText(detailItem.getLat());
        holder.textViewLatPlaceholder.setText(detailItem.getLatPlaceholder());
        holder.textViewLng.setText(detailItem.getLng());
        holder.textViewLngPlaceholder.setText(detailItem.getLngPlaceholder());
    }

    @Override
    public int getItemCount() {
        return detailItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewCallsign;
        public TextView textviewCallsignPlaceHolder;
        public TextView textViewGufi;
        public TextView textViewGufiPlaceHolder;
        public TextView textViewLat;
        public TextView textViewLatPlaceholder;
        public TextView textViewLng;
        public TextView textViewLngPlaceholder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //add something for image view

            textviewCallsign = itemView.findViewById(R.id.callsign_textView);
            textviewCallsignPlaceHolder = itemView.findViewById(R.id.callsign_placeholder);
            textViewGufi = itemView.findViewById(R.id.gufi_textView);
            textViewGufiPlaceHolder =  itemView.findViewById(R.id.gufi_placeholder);
            textViewLat = itemView.findViewById(R.id.lat_textView);
            textViewLatPlaceholder = itemView.findViewById(R.id.lat_placeholder);
            textViewLng = itemView.findViewById(R.id.lng_textView);
            textViewLngPlaceholder = itemView.findViewById(R.id.lng_placeholder);
        }
    }
}
