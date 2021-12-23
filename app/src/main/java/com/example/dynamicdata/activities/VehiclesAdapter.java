package com.example.dynamicdata.activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dynamicdata.R;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.ArrayList;
import java.util.List;

public class VehiclesAdapter extends RecyclerView.Adapter<VehiclesAdapter.ViewHolder>implements Filterable {


    List<Vehicles> vehiclesList;
    List<Vehicles> vehiclesFilterList;
    private final VehicleRecyclerListener vehiclesRecyclerListener;
    int index = -1;

    private FirebaseRemoteConfig config;
    public VehiclesAdapter(VehicleRecyclerListener RecyclerListener, List<Vehicles> vehiclesList,  FirebaseRemoteConfig config) {

        this.vehiclesList = vehiclesList;
        this.vehiclesFilterList = vehiclesList;
        this.vehiclesRecyclerListener=RecyclerListener;

        this.config = config;
    }




    @NonNull
    @Override
    public VehiclesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view,parent,false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull VehiclesAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final  int position) {
       Vehicles vehicles=vehiclesFilterList.get(position);

        holder.name.setText(vehicles.getNome());
        holder.itemView.setOnClickListener(view -> vehiclesRecyclerListener.onItemSelected(vehicles));

        holder.name.setOnClickListener(v -> {
            index = position;
            notifyDataSetChanged();

        });
        if(index==position){
            holder.layout.setBackgroundColor(Color.parseColor("#080555"));
            holder.name.setTextColor(Color.parseColor("#FFFFFF"));

        }else{
            holder.layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.name.setTextColor(Color.parseColor("#080555"));
        }

    }

    @Override
    public int getItemCount() {
        return vehiclesFilterList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    vehiclesFilterList = vehiclesList;
                } else {
                    List<Vehicles> filteredList = new ArrayList<>();
                    for (Vehicles vehicles : vehiclesList) {
                        if (vehicles.getNome().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(vehicles);
                        }
                    }
                    vehiclesFilterList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = vehiclesFilterList;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                vehiclesFilterList = (ArrayList<Vehicles>) results.values;
                notifyDataSetChanged();
            }

        };
    }
    public interface VehicleRecyclerListener {
        void onItemSelected(Vehicles vehicles);
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
          TextView name;
          LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.nameView);
            layout=itemView.findViewById(R.id.LinaerLayouts);

        }
    }

}
