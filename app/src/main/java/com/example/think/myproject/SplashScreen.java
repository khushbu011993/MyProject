package com.example.think.myproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class SplashScreen extends AppCompatActivity {
   // public static String str_login_test;

    SharedPreferences preferences ;
    public static  String OTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        preferences = getSharedPreferences("TEXT", 0);

        OTP = preferences.getString("otpKey", "false");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if(OTP.equals("true"))
                {
                    Intent send = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(send);
                }
                else {

                    Intent send = new Intent(getApplicationContext(),MobileNoActivity.class);
                    startActivity(send);
                }
            }

        }, 3000);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
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
