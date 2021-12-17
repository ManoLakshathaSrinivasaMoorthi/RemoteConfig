package com.example.dynamicdata;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.dynamicdata.databinding.ActivityCustomDialogBinding;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class CustomDialog extends Dialog {
    private Context contexts;
    private final FirebaseRemoteConfig configs;
    private ActivityCustomDialogBinding binding;

    public CustomDialog(@NonNull Context context, FirebaseRemoteConfig config) {
        super(context);
        this.contexts=context;
        this.configs=config;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = DataBindingUtil.setContentView((Activity) contexts, R.layout.activity_custom_dialog);
         setOnClickListeners();
    }

    private void setOnClickListeners() {
        binding.btnUpdate.setOnClickListener(view -> {
            binding.txtTitle.setText(configs.getString(RemoteUtils.Title));
            binding.txtBody.setText(configs.getString(RemoteUtils.BodyContent));
            binding.txtVersion.setText(configs.getString(RemoteUtils.Version));

        });


    }
}