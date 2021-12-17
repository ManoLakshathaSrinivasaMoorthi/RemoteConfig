package com.example.dynamicdata.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dynamicdata.R;
import com.example.dynamicdata.RemoteUtils;
import com.example.dynamicdata.databinding.ActivityPhoneBinding;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneActivity extends AppCompatActivity {

    FirebaseRemoteConfig mFirebaseRemoteConfig;
    private ActivityPhoneBinding binding;
    VehiclesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_phone);


       Searchbars();

        mFirebaseRemoteConfig=FirebaseRemoteConfig.getInstance();
        HashMap<String,Object> map=new HashMap<>();
        FirebaseRemoteConfigSettings configSettings= new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(1000)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(map);
        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(task -> getSuperHeroes());
        binding.refreshBtn.setOnClickListener(view -> {
            boolean isPromoOn = mFirebaseRemoteConfig.getBoolean(RemoteUtils.CONFIG_IS_PROMO_ON);
            int color = isPromoOn ? Color.parseColor(mFirebaseRemoteConfig.getString(RemoteUtils.CONFIG_COLOR_PRY)) :
                    ContextCompat.getColor(PhoneActivity.this, R.color.colorPrimaryDark);
            setButtonColor();
            setTextViewColors();
            binding.Constraintslayouts.setBackgroundColor(color);



        });

        //getSuperHeroes();
    }

    private void Searchbars() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        binding.search.setActivated(true);
        binding.search.setQueryHint(getResources().getString(R.string.search));
        binding.search.onActionViewExpanded();
        binding.search.setIconified(false);
        binding.search.clearFocus();
        binding.search.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        binding.search.setMaxWidth(Integer.MAX_VALUE);

        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });


    }

    private void setTextViewColors() {
        boolean isPromoOn = mFirebaseRemoteConfig.getBoolean(RemoteUtils.CONFIG_IS_PROMO_ON);
        int color = isPromoOn ? Color.parseColor(mFirebaseRemoteConfig.getString(RemoteUtils.CONFIG_TEXT_COLOR)) :
                ContextCompat.getColor(PhoneActivity.this, R.color.colorText);
        binding.Headlines.setTextColor(color);
    }

    private void setButtonColor() {
        boolean isPromoOn = mFirebaseRemoteConfig.getBoolean(RemoteUtils.CONFIG_IS_PROMO_ON);
        int color = isPromoOn ? Color.parseColor(mFirebaseRemoteConfig.getString(RemoteUtils.CONFIG_BUTTON_COLOR)) :
                ContextCompat.getColor(PhoneActivity.this, R.color.colorButton);
        binding.refreshBtn.setBackgroundColor(color);
    }

    private void getSuperHeroes() {
        ApiEndUrl endUrl=RetrofitClient.getRetrofitInstance().create(ApiEndUrl.class);
        Call<List<Vehicles>> call =endUrl.getbrands();
        call.enqueue(new Callback<List<Vehicles>>() {
            @Override
            public void onResponse(@NonNull Call<List<Vehicles>> call, @NonNull Response<List<Vehicles>> response) {

                Log.d("Output", " : " + new Gson().toJson(response.body()));
                binding.listItem.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                binding.listItem.setLayoutManager(layoutManager);
               adapter = new VehiclesAdapter(
                       vehicles -> {
                           Intent intent= new Intent(PhoneActivity.this,DetailedActivity.class);
                           intent.putExtra(Constants.SharedPreference.Vname,vehicles.getNome());
                           intent.putExtra(Constants.SharedPreference.VCardigo,vehicles.getCodigo());
                           startActivity(intent);
                       },response.body());

               binding.listItem.setAdapter(adapter);



            }

            @Override
            public void onFailure(@NonNull Call<List<Vehicles>> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
    }
}