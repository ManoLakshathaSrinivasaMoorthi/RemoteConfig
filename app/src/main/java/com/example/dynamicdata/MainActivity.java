package com.example.dynamicdata;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.example.dynamicdata.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.BuildConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


   /* FirebaseRemoteConfig mFirebaseRemoteConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HashMap<String,Object> map=new HashMap<>();
       // map.put("",getString(R.string.button_signup));
        mFirebaseRemoteConfig=FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings= new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(1000)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(map);
        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(
                task -> dialogShow()
        );
    }

    private void dialogShow() {
        if(mFirebaseRemoteConfig.getLong(RemoteUtils.Version)<= BuildConfig.VERSION_CODE)
       return;
        CustomDialog dialog= new CustomDialog(this,mFirebaseRemoteConfig);
        dialog.show();
    }
}

*/




    FirebaseRemoteConfig mRemoteConfig;
    private ActivityMainBinding binding;
    //remote config fields
    private static final String CONFIG_SIGNUP_PROMPT = "signup_prompt";
    private static final String CONFIG_MIN_PASSWORD_LEN = "min_password_length";
    private static final String CONFIG_IS_PROMO_ON = "is_promotion_on";
    private static final String CONFIG_COLOR_PRY = "color_primary";
    private static final String CONFIG_COLOR_PRY_DARK = "color_primary_dark";
    private static final String CONFIG_COLOR_BUTTON = "color_accent";


    private int minPasswordLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);

        setSupportActionBar(binding.toolbar);
         binding.toolbar.setOnClickListener(view -> {
             initRemoteConfig();

             setupView();
         });

    }

    private void initRemoteConfig() {
        mRemoteConfig = FirebaseRemoteConfig.getInstance();

        Resources res = getResources();

        HashMap<String, Object> defaults = new HashMap<>();


        mRemoteConfig.setDefaults(defaults);
        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true).setMinimumFetchIntervalInSeconds(600)
                .build();
        mRemoteConfig.setConfigSettings(remoteConfigSettings);
        fetchRemoteConfigValues();
    }

    private void setupView() {

        setToolbarColor();

        setSignupPrompt();
        setPromoCode();
        minPasswordLength = (int) mRemoteConfig.getLong(CONFIG_MIN_PASSWORD_LEN);

       binding.buttonSignup.setOnClickListener(v -> {
           if (validateInput()) {
               Toast.makeText(getApplicationContext(), R.string.toast_sign_up, Toast.LENGTH_SHORT).show();
           }
       });
    }

    private boolean validateInput() {
        // insert validation for other fields

        //this is the password field we want to configure
        if (binding.editPassword.getText().toString().length() < minPasswordLength) {
            binding.editPassword.setError(String.format(getString(R.string.error_short_password), minPasswordLength));
            return false;
        } else {
            binding.editPassword.setError(null);
            return true;
        }
    }

    private void fetchRemoteConfigValues() {
        long cacheExpiration = 3600;

        //expire the cache immediately for development mode.
        if (mRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        mRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // task successful. Activate the fetched data
                        mRemoteConfig.activateFetched();
                        setupView();
                    }  //task failed

                });
    }

    private void setSignupPrompt() {
        String prompt = mRemoteConfig.getString(CONFIG_SIGNUP_PROMPT);
        binding.editPromoCode.setText(prompt);
    }

    private void setPromoCode() {
        boolean isPromoOn = mRemoteConfig.getBoolean(CONFIG_IS_PROMO_ON);
        binding.editPromoCode.setVisibility(isPromoOn ? View.VISIBLE : View.GONE);
    }

    private void setToolbarColor() {
        boolean isPromoOn = mRemoteConfig.getBoolean(CONFIG_IS_PROMO_ON);
        int color = isPromoOn ? Color.parseColor(mRemoteConfig.getString(CONFIG_COLOR_PRY)) :
                ContextCompat.getColor(this, R.color.colorPrimary);
        //int color1=Color.parseColor(mRemoteConfig.getString(CONFIG_COLOR_PRY_DARK));
      //  int color2=Color.parseColor(mRemoteConfig.getString(CONFIG_COLOR_BUTTON));
        binding.toolbar.setBackgroundColor(color);
      /*  binding.RelativeLayout.setBackgroundColor(color1);
        binding.editUsername.setBackgroundColor(color2);
        binding.editEmail.setBackgroundColor(color2);
        binding.editPassword.setBackgroundColor(color2);
*/

    }




    private void setStatusBarColor() {
        boolean isPromoOn = mRemoteConfig.getBoolean(CONFIG_IS_PROMO_ON);
        int color = isPromoOn ? Color.parseColor(mRemoteConfig.getString(CONFIG_COLOR_PRY_DARK)) :
                ContextCompat.getColor(this, R.color.colorPrimaryDark);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
