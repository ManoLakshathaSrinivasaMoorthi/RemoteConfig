package com.example.dynamicdata.activities;

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
    private FirebaseRemoteConfig config;
    public VehiclesAdapter(VehicleRecyclerListener RecyclerListener,List<Vehicles> vehiclesList) {

        this.vehiclesList = vehiclesList;
        this.vehiclesFilterList = vehiclesList;
        this.vehiclesRecyclerListener=RecyclerListener;
    }

    public VehiclesAdapter() {

    }


    @NonNull
    @Override
    public VehiclesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view,parent,false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull VehiclesAdapter.ViewHolder holder, int position) {
       Vehicles vehicles=vehiclesFilterList.get(position);
        holder.name.setText(vehicles.getNome());

        holder.itemView.setOnClickListener(view -> vehiclesRecyclerListener.onItemSelected(vehicles));
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
