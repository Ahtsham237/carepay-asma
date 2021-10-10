package com.example.carepay.activity.user.fragment.newsfeed;


import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carepay.R;
import com.example.carepay.model.User;
import com.example.carepay.model.ngoData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedFragment extends Fragment {


    public NewsFeedFragment() {
        // Required empty public constructor
    }
    ArrayList<ngoData> ngoDataSet;
    NewsFeedAdapter postAdapter;
    ListView listview;
    Dialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);
        ngoDataSet = new ArrayList<>();
        getActivity().setTitle("Home");
        listview = view.findViewById(R.id.listview);
        //listview.setAdapter(postAdapter);
        progressDialog = new Dialog(getActivity());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        TextView progressTv = progressDialog.findViewById(R.id.progress_tv);
        if(progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        progressDialog.setCancelable(false);
        progressDialog.show();
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                User user = dataSnapshot.getValue(User.class);
                loadnewfeed(user);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return view;
    }
    private void loadnewfeed(final User user) {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
        final DatabaseReference database =  FirebaseDatabase.getInstance().getReference().child("posts");
        Query query= database;
        if (user.getFollowing()!=null){
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            for (DataSnapshot snap : snapshot.getChildren()){
                                ngoData data =snap.getValue(ngoData.class);
                                Log.d("","");
                                if (user.getFollowing().containsValue(data.getUid())){
                                    ngoDataSet.add(data);

                                }
                            }
                        }
                        //Toast.makeText(getActivity().getApplicationContext(), ""+ngoDataSet.size(), Toast.LENGTH_SHORT).show();
                        bubbleSort(ngoDataSet);
                    }
                    //Collections.reverse(ngoDataSet);

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity().getApplicationContext(), "failure", Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            });
        }

    }
    void bubbleSort(ArrayList<ngoData> arr) {
        ArrayList<ngoData> newarralist = new ArrayList<>();
        int n = arr.size() - 2;
        for (int i = 0; i < n - 1; i++){
            for (int j = 0; j < n - i - 1; j++) {
                if (Long.parseLong(arr.get(j).getTimestamp()) > Long.parseLong(arr.get(j + 1).getTimestamp())) {
                    // swap arr[j+1] and arr[i]
             //       arr.remove(j);
             //       arr.remove(j + 1);
                    ngoData temp = arr.get(j);
                    arr.add(j, arr.get(j + 1));
                    arr.remove(j + 1);
                    arr.add(j + 1, temp);
                    arr.remove(j + 2);
                    //arr.get(j+1) = temp;
                }
            }
        }
        postAdapter = new NewsFeedAdapter(getActivity(),arr);
        listview.setAdapter(postAdapter);
        //postAdapter.notifyDataSetChanged();
    }
}
