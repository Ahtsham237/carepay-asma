package com.example.carepay.activity.ngo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.carepay.R;
import com.example.carepay.activity.admin.fragment.user.NGOProfileFragment;
import com.example.carepay.activity.login.LogIn;
import com.example.carepay.activity.ngo.fragment.transection.ChekTransectionFragment;
import com.example.carepay.activity.ngo.fragment.donar.DonarDetailFragment;
import com.example.carepay.activity.ngo.fragment.post.PostImageVideoFragment;
import com.example.carepay.helper.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class NGOMainScreen extends AppCompatActivity {
    int previousSelect = 0;
    private FragmentManager fragmentManager;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngomain_screen);
        fragmentManager = getSupportFragmentManager();
        floatingActionButton=findViewById(R.id.floating_action);
        final SpaceNavigationView spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.creditcard));
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.small_bell_icon));
        spaceNavigationView.setBackgroundColor(Color.parseColor("#ffffff"));
        PostImageVideoFragment homeFragment = new PostImageVideoFragment();
        loadFrag(homeFragment, "Home", fragmentManager);
     spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
        @Override
        public void onCentreButtonClick() {
            spaceNavigationView.setSelected(false);

            PostImageVideoFragment homeFragment = new PostImageVideoFragment();
            loadFrag(homeFragment, "Home", fragmentManager);
//                Toast.makeText(MainActivity.this,"onCentreButtonClick", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onItemClick(int itemIndex, String itemName) {
            switch (itemIndex){
                case 0:

                    ChekTransectionFragment transectionFragment = new ChekTransectionFragment();
                    loadFrag(transectionFragment, "Transaction", fragmentManager);
                    break;
                case 1:
                    spaceNavigationView.setSelected(true);
                    DonarDetailFragment detailFragment = new DonarDetailFragment();
                    loadFrag(detailFragment, "Detail", fragmentManager);
                    break;
            }
            //Toast.makeText(MainActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onItemReselected(int itemIndex, String itemName) {
            switch (itemIndex){
                case 0:

                    ChekTransectionFragment transectionFragment = new ChekTransectionFragment();
                    loadFrag(transectionFragment, "Transaction", fragmentManager);
                    break;
                case 1:
                    spaceNavigationView.setSelected(true);
                    DonarDetailFragment detailFragment = new DonarDetailFragment();
                    loadFrag(detailFragment, "Detail", fragmentManager);
                    break;
            }
        }
    });
     floatingActionButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             getSupportFragmentManager().beginTransaction().addToBackStack("ProfileFragment").replace(R.id.framelayout,new NGOProfileFragment()).commit();
         }
     });
}
    public void navigationItemSelected(int position) {
        previousSelect = position;
    }
    public void loadFrag(Fragment f1, String name, FragmentManager fm) {
        /*for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }*/
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.framelayout, f1, name);
        ft.commit();
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
                SweetAlertDialog pDialog = new SweetAlertDialog(NGOMainScreen.this, SweetAlertDialog.WARNING_TYPE);
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
                        startActivity(new Intent(NGOMainScreen.this, LogIn.class));
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
        SweetAlertDialog pDialog = new SweetAlertDialog(NGOMainScreen.this, SweetAlertDialog.WARNING_TYPE);
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
