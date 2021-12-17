package com.example.dynamicdata.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_phone);

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
                VehiclesAdapter adapter= new VehiclesAdapter(
                        new VehiclesAdapter.VehicleRecyclerListener() {
                            @Override
                            public void onItemSelected(Vehicles vehicles) {
                           ;
                            }
                        });

               binding.listItem.setAdapter(adapter);



            }

            @Override
            public void onFailure(@NonNull Call<List<Vehicles>> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
    }
}