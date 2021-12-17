package com.example.dynamicdata.activities;

import android.content.Context;
import android.content.Intent;
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
import com.example.dynamicdata.RemoteUtils;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.List;

public class VehiclesAdapter extends RecyclerView.Adapter<VehiclesAdapter.ViewHolder>implements Filterable {

   // Context context;
    List<Vehicles> vehiclesList;
   // private final FirebaseRemoteConfig configs;
    private VehicleRecyclerListener vehiclesRecyclerListener;

    public VehiclesAdapter(VehicleRecyclerListener RecyclerListener,List<Vehicles> vehiclesList) {
        //this.context = context;
        this.vehiclesList = vehiclesList;
       // this.configs = configs;
        this.vehiclesRecyclerListener=RecyclerListener;
    }

    public VehiclesAdapter(VehicleRecyclerListener recyclerListener) {
    }

    @NonNull
    @Override
    public VehiclesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view,parent,false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull VehiclesAdapter.ViewHolder holder, int position) {
       Vehicles vehicles=vehiclesList.get(position);
        holder.name.setText(vehicles.getNome());


         holder.layout.setOnClickListener(view -> {
           /*  Intent intent= new Intent(context,DetailedActivity.class);
             intent.putExtra(Constants.SharedPreference.Vname,vehicles.getNome());
             intent.putExtra(Constants.SharedPreference.VCardigo,vehicles.getCodigo());
             context.startActivity(intent);*/

         });
    }

    @Override
    public int getItemCount() {
        return vehiclesList.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }
    public static interface VehicleRecyclerListener {
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
