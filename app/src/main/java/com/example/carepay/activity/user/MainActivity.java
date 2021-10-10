package com.example.carepay.activity.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.carepay.R;
import com.example.carepay.activity.admin.fragment.user.UserProfileFragment;
import com.example.carepay.activity.login.LogIn;
import com.example.carepay.activity.user.fragment.donate.DonateFragment;
import com.example.carepay.activity.user.fragment.followngo.FolowNGOFragment;
import com.example.carepay.activity.user.fragment.newsfeed.NewsFeedFragment;
import com.example.carepay.activity.user.fragment.viewtransection.ViewTransectionFragment;
import com.example.carepay.helper.SessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    int previousSelect = 0;
    private FragmentManager fragmentManager;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = findViewById(R.id.toolbar);

        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState==null){
            NewsFeedFragment homeFragment = new NewsFeedFragment();
            navigationItemSelected(0);
            loadFrag(homeFragment, "Home", fragmentManager);
        }
//        setSupportActionBar(toolbar);
        SpaceNavigationView spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem(" ", R.drawable.donate));
        spaceNavigationView.addSpaceItem(new SpaceItem(" ", R.drawable.ic_add_circle_black_24dp));
        spaceNavigationView.addSpaceItem(new SpaceItem(" ", R.drawable.ic_list));
        spaceNavigationView.addSpaceItem(new SpaceItem(" ", R.drawable.ic_user));
        spaceNavigationView.setBackgroundColor(Color.parseColor("#ffffff"));
        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {

                NewsFeedFragment homeFragment = new NewsFeedFragment();
                navigationItemSelected(0);
                loadFrag(homeFragment, "Home", fragmentManager);
//                Toast.makeText(MainActivity.this,"onCentreButtonClick", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                switch (itemIndex){
                    case 0:
                        DonateFragment favouriteFragment = new DonateFragment();
                        navigationItemSelected(0);
                        loadFrag(favouriteFragment, "Donate", fragmentManager);
                        break;
                    case 1:
                        FolowNGOFragment matchFragment = new FolowNGOFragment();
                        navigationItemSelected(1);
                        loadFrag(matchFragment, "NGO", fragmentManager);
                        break;
                    case 2:
                        ViewTransectionFragment messageFragment = new ViewTransectionFragment();
                        navigationItemSelected(3);
                        loadFrag(messageFragment, "Transection", fragmentManager);
                        break;
                    case 3:
                        navigationItemSelected(4);
                        getSupportFragmentManager().beginTransaction().addToBackStack("ProfileFragment").replace(R.id.framelayout,new UserProfileFragment()).commit();
                        break;
                }
                //Toast.makeText(MainActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                switch (itemIndex){
                    case 0:
                        DonateFragment favouriteFragment = new DonateFragment();
                        navigationItemSelected(0);
                        loadFrag(favouriteFragment, "Donate", fragmentManager);
                        break;
                    case 1:
                        FolowNGOFragment matchFragment = new FolowNGOFragment();
                        navigationItemSelected(1);
                        loadFrag(matchFragment, "NGO", fragmentManager);
                        break;
                    case 2:
                        ViewTransectionFragment messageFragment = new ViewTransectionFragment();
                        navigationItemSelected(3);
                        loadFrag(messageFragment, "Transection", fragmentManager);
                        break;
                    case 3:
                        navigationItemSelected(4);
                        getSupportFragmentManager().beginTransaction().addToBackStack("ProfileFragment").replace(R.id.framelayout,new UserProfileFragment()).commit();
                        break;
                }
                // Toast.makeText(MainActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
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
                SweetAlertDialog pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
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
                        startActivity(new Intent(MainActivity.this, LogIn.class));
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
        SweetAlertDialog pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
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
