package com.bini.gizecarlockerutility;
/*Binishare (Gize Trading PLC*/
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

public class LoginActivity extends AppCompatActivity {
    ///eziga declare
    EditText simnumber;
    TextView txtTitle, txtInfo;
    Button btnSave;
    private LinearLayout LinearLayout;
    private AnimationDrawable animationDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        // init constraintLayout
        LinearLayout = (LinearLayout) findViewById(R.id.linearlayout);

        // initializing animation drawable by getting background from constraint layout
        animationDrawable = (AnimationDrawable) LinearLayout.getBackground();

        // setting enter fade animation duration to 5 seconds
        animationDrawable.setEnterFadeDuration(4000);

        // setting exit fade animation duration to 2 seconds
        animationDrawable.setExitFadeDuration(2000);
        // wede usable convert
        txtTitle = findViewById(R.id.txtTitle);
        simnumber = findViewById(R.id.sim_number);
        txtInfo = findViewById(R.id.txtInfo);
        btnSave = findViewById(R.id.btnSave);

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();


        if (Prefs.getBoolean("reguser", false)) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to save data wede preferences
                final String LAsimno = simnumber.getText().toString();
                // textu null alemehonun check

                if (LAsimno.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter a sim Number", Toast.LENGTH_LONG).show();
                } else {

                    // Toast.makeText(this,"kutru yihe nw"+Prefs.getString()+"", Toast.LENGTH_LONG).show();
                    Prefs.putString("simnumberkey", LAsimno);
                    Prefs.putBoolean("reguser", true);
                    Toast.makeText(getApplicationContext(), "simno is " + Prefs.getString("simnumberkey", "no number set") + "", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "reguser is " + Prefs.getBoolean("reguser", false) + "", Toast.LENGTH_SHORT).show();


                    //wede main activity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    //back keneku wedehuala endaymeles
                    finish();

                }

            }
        });


    }
// jerbawn animate
    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            // start the animation
            animationDrawable.start();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning()) {
            // stop the animation
            animationDrawable.stop();
        }


    }
}

