package com.example.dynamicdata.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.dynamicdata.R;
import com.example.dynamicdata.RemoteUtils;
import com.example.dynamicdata.databinding.ActivityDetailedBinding;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class DetailedActivity extends AppCompatActivity {

    private ActivityDetailedBinding binding;
    private  String name, codigo;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     binding= DataBindingUtil. setContentView(this,R.layout.activity_detailed);
     Intent intent=getIntent();
     name=intent.getStringExtra(Constants.SharedPreference.Vname);
     codigo=intent.getStringExtra(Constants.SharedPreference.VCardigo);
     binding.nameView.setText(name);
     binding.txtBody.setText(codigo);

    }
}