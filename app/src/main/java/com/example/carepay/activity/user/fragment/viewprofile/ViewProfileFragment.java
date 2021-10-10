package com.example.carepay.activity.user.fragment.viewprofile;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carepay.R;
import com.example.carepay.activity.stripe.PaymentFragment;
import com.example.carepay.activity.user.fragment.newsfeed.NewsFeedAdapter;
import com.example.carepay.helper.StaticClass;
import com.example.carepay.model.User;
import com.example.carepay.model.ngoData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewProfileFragment extends Fragment {


    public ViewProfileFragment() {
        // Required empty public constructor
    }
    ArrayList<ngoData> ngoDataSet;
    NewsFeedAdapter postAdapter;
    ListView listview;
    Dialog progressDialog;
    FloatingActionButton floatingdonate;
    FloatingActionButton floatingfollow;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_profile, container, false);
        ngoDataSet = new ArrayList<>();
        getActivity().setTitle("NGO's Profile");
        postAdapter = new NewsFeedAdapter(getActivity(),ngoDataSet);
        listview = view.findViewById(R.id.listview);
        floatingfollow = view.findViewById(R.id.floatingfollow);
        floatingdonate = view.findViewById(R.id.floatingdonate);
        listview.setAdapter(postAdapter);
        progressDialog = new Dialog(getActivity());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        TextView progressTv = progressDialog.findViewById(R.id.progress_tv);
        if(progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        },2000);
        final Bundle bundle = getArguments();
        final String uid = bundle.get("uid").toString();
        FirebaseDatabase.getInstance().getReference().child("posts").child(uid)
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()){
                    ngoData ngoData = dataSnapshot.getValue(com.example.carepay.model.ngoData.class);
                    ngoDataSet.add(ngoData);
                    //Toast.makeText(getActivity().getApplicationContext(), ""+ngoDataSet.size(), Toast.LENGTH_SHORT).show();
                }
                listview.setAdapter(postAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        floatingdonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PaymentFragment fragment=new PaymentFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).commit();
            }
        });
        floatingfollow.setBackgroundColor(Color.WHITE);
        if (StaticClass.user.getFollower()==null||!StaticClass.user.getFollower().containsValue(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                floatingfollow.setImageResource(R.drawable.ic_add_black_24dp);
        }else {
            floatingfollow.setImageResource(R.drawable.ic_check_black_24dp);
        }
        floatingfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=FirebaseAuth.getInstance().getCurrentUser().getUid();
                if (StaticClass.user.getFollower()==null||!StaticClass.user.getFollower().containsValue(s)){
                    final String userid =FirebaseAuth.getInstance().getCurrentUser().getUid();//
                    ///String key  = FirebaseDatabase.getInstance().getReference().child("users").child(StaticClass.user.getUid()).child("follower").push().getKey();
                    FirebaseDatabase.getInstance().getReference().child("users")
                            .child(StaticClass.user.getUid()).child("follower").child(userid).setValue(userid)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        //String key2  = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("follower").push().getKey();
                                        FirebaseDatabase.getInstance().getReference().child("users")
                                                .child(FirebaseAuth.getInstance().getUid()).child("following")
                                                .child(StaticClass.user.getUid()).setValue(StaticClass.user.getUid())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
//                                         follow.setText("Unfollow");
                                                final String userid =FirebaseAuth.getInstance().getCurrentUser().getUid();//
                                                if (StaticClass.user.getFollower()==null) {
                                                    HashMap<String, String> hashMap=new HashMap<>();
                                                    hashMap.put(userid,userid);
                                                    StaticClass.user.setFollower(hashMap);

                                                }else
                                                    StaticClass.user.getFollower().put(userid,userid);
                                                floatingfollow.setImageResource(R.drawable.ic_check_black_24dp);
                                                Toast.makeText(getActivity().getApplicationContext(), "Following", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            });
                }else {
                    String userid =FirebaseAuth.getInstance().getCurrentUser().getUid();//
                    FirebaseDatabase.getInstance().getReference().child("users").child(StaticClass.user.getUid())
                    .child("follower").child(userid).setValue("")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                //String key2  = FirebaseDatabase.getInstance().getReference().child("users").child(StaticClass.user.getUid()).child("follower").push().getKey();
                                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("following")
                                        .child(StaticClass.user.getUid()).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        final String userid =FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        User user =StaticClass.user;
                                        HashMap<String,String> hashMap = user.getFollower();
                                        hashMap.remove(userid);
                                        user.setFollower(hashMap);
                                        StaticClass.user=user;
                                        floatingfollow.setImageResource(R.drawable.ic_add_black_24dp);
                                        Toast.makeText( getActivity().getApplicationContext(), "Status Updated", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
        return view;
    }
}