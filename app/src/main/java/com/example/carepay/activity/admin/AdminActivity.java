package com.example.carepay.activity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.carepay.R;
import com.example.carepay.activity.admin.fragment.HomeScreenFragment;
import com.example.carepay.activity.login.LogIn;
import com.example.carepay.helper.SessionManager;
import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        if (savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new HomeScreenFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_logout:
                SweetAlertDialog pDialog = new SweetAlertDialog(AdminActivity.this, SweetAlertDialog.WARNING_TYPE);
                //pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Log out?");
                pDialog.setCancelText("Cancel");
                pDialog.setConfirmText("Log Out");
                //pDialog.setCancelable(false);
                pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        new SessionManager(getApplicationContext()).setLogin(false);
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(AdminActivity.this, LogIn.class));
                        finish();
                    }
                });
                pDialog.show();


                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        SweetAlertDialog pDialog = new SweetAlertDialog(AdminActivity.this, SweetAlertDialog.WARNING_TYPE);
        //pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Are you sure you want to Exit?");
        pDialog.setCancelText("Cancel");
        pDialog.setConfirmText("Exit");
        //pDialog.setCancelable(false);
        pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
            }
        });
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                //super.onBackPressed();
                finishAffinity();
            }
        });
        pDialog.show();
//        Toast.makeText(this, "gfg", Toast.LENGTH_SHORT).show();

    }
}
