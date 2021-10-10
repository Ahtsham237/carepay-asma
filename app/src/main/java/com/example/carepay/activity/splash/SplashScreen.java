package com.example.carepay.activity.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.example.carepay.R;
import com.example.carepay.activity.admin.AdminActivity;
import com.example.carepay.activity.ngo.NGOMainScreen;
import com.example.carepay.activity.user.MainActivity;
import com.example.carepay.activity.login.LogIn;
import com.example.carepay.helper.SessionManager;
import com.mikhaellopez.circularimageview.CircularImageView;

public class SplashScreen extends AppCompatActivity {
    SessionManager manager;
    private static int SPLASH_TIME_OUT=3000;
    CircularImageView splImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
       splImageView=findViewById(R.id.splash);
        splImageView.animate().rotationBy(360f).setDuration(3000).setInterpolator(new LinearInterpolator()).start();
        manager = new SessionManager(getApplicationContext());
        if (manager.getPermission().toLowerCase().equals("no")){
            ActivityCompat.requestPermissions(SplashScreen.this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.INTERNET},
                    2);
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent ;
                    if (manager.isLoggedIn()){
                        SessionManager sessionManager = new SessionManager(getApplicationContext());
                        if (sessionManager.getRole().equals("admin")){

                            intent = new Intent(SplashScreen.this, AdminActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        if (sessionManager.getRole().equals("user")){

                            intent = new Intent(SplashScreen.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        if (sessionManager.getRole().equals("ngo")){

                            intent = new Intent(SplashScreen.this, NGOMainScreen.class);
                            startActivity(intent);
                            finish();
                        }
                    }else {
                        intent = new Intent(SplashScreen.this, LogIn.class);
                        startActivity(intent);
                    }
                }
            },3000);
        }



    }
        @Override
        public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
            switch (requestCode) {
                case 2: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.
                        new Handler().postDelayed(new Runnable(){
                            @Override
                            public void run(){
                                boolean flag=new SessionManager(getApplicationContext()).getlogin();
                                if (flag)
                                {
                                    SessionManager sessionManager = new SessionManager(getApplicationContext());
                                    if (sessionManager.getRole().equals("admin")){

                                        Intent intent=new Intent(SplashScreen.this, AdminActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    if (sessionManager.getRole().equals("user")){

                                        Intent intent=new Intent(SplashScreen.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    if (sessionManager.getRole().equals("ngo")){

                                        Intent intent=new Intent(SplashScreen.this, NGOMainScreen.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                                else
                                {
                                    Intent i = new Intent(SplashScreen.this,LogIn.class);
                                    startActivity(i);

                                    finish();
                                }

                            }
                        },SPLASH_TIME_OUT);
                    } else {

                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                        Toast.makeText(SplashScreen.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
    }
    }
}
