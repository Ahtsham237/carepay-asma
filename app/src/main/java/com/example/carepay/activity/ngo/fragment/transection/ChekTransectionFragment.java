package com.example.carepay.activity.ngo.fragment.transection;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.carepay.R;
import com.example.carepay.activity.ngo.fragment.post.PostImageVideoFragment;
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
public class ChekTransectionFragment extends Fragment {


    public ChekTransectionFragment() {
    }
    CheckTransactionAdapter adapter;
    ListView listview;
    ArrayList<HashMap<String,String>> list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chek_transection, container, false);
        listview=view.findViewById(R.id.listview);
        getActivity().setTitle("Donations");
        list=new ArrayList<>();
        adapter=new CheckTransactionAdapter(getActivity(),list);
        listview.setAdapter(adapter);
        fetchnewdata();
        return view;
    }

    private void fetchnewdata() {
        FirebaseDatabase.getInstance().getReference().child("donate").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        HashMap<String,String> mymap = (HashMap<String, String>) snapshot.getValue();
                        if (mymap.get("ruid").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){

                            list.add(mymap);

                        }
                    }
                }
                adapter.notifyDataSetChanged();
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

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new PostImageVideoFragment()).commit();


                    return true;

                }

                return false;
            }
        });
    }
}
