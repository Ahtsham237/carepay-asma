package com.example.carepay.activity.user.fragment.donate;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.carepay.R;
import com.example.carepay.activity.user.fragment.newsfeed.NewsFeedFragment;
import com.example.carepay.helper.StaticClass;
import com.example.carepay.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DonateFragment extends Fragment {


    public DonateFragment() {
        // Required empty public constructor
    }
    ListView listView;
    ArrayList<User> userArrayList;
    DonateAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donate, container, false);
        userArrayList= new ArrayList<>();
        getActivity().setTitle("Donate");
        listView = view.findViewById(R.id.listview);
        adapter = new DonateAdapter(getActivity(),userArrayList);
        listView.setAdapter(adapter);
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    User user = dataSnapshot.getValue(User.class);
                    fetchalluser(user);
                    StaticClass.name=user.getName();
                    StaticClass.url=user.getProfilePicUrl();
                    StaticClass.name=user.getName();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        fetchalluser();
        return view;
    }

    private void fetchalluser(final User user) {
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     if (dataSnapshot.exists()){
                         for (DataSnapshot snapshot  : dataSnapshot.getChildren()){
                                User user1 = snapshot.getValue(User.class);
                                if (user.getFollowing()!=null)
                                if (user.getFollowing().containsValue(user1.getUid())){
                                    userArrayList.add(user1);
                                }
                             //Toast.makeText(getActivity().getApplicationContext(), ""+user1.getRole(), Toast.LENGTH_SHORT).show();
                         }
                     }
                 adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onResume() {

        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new NewsFeedFragment()).commit();


                    return true;

                }

                return false;
            }
        });
    }
}
