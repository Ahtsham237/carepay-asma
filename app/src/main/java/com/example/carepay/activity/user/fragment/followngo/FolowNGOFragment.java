package com.example.carepay.activity.user.fragment.followngo;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.carepay.R;
import com.example.carepay.activity.user.fragment.newsfeed.NewsFeedFragment;
import com.example.carepay.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FolowNGOFragment extends Fragment {


    public FolowNGOFragment() {
        // Required empty public constructor
    }
    ArrayList<User>  userArrayList ;
    ListView listview;
    FollowAdapter adapter;
    private SearchView searchView = null;

    private SearchView.OnQueryTextListener queryTextListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_folow_ngo, container, false);
        listview = view.findViewById(R.id.listview);
        userArrayList = new ArrayList<>();
        getActivity().setTitle("Follow NGO");
        adapter = new FollowAdapter(getActivity(),userArrayList);
        fetchdata();
        listview.setAdapter(adapter);
        return view;
    }

    private void fetchdata() {
        FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    User user = dataSnapshot.getValue(User.class);
                    //User user = dataSnapshot.getChildren().getValue(User.class);
                    if (user.getRole().equals("ngo")){
                        userArrayList.add(user);
                        //String string = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("follower").getKey();
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    // fetchdata();
                }
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.more_tab_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
             /*   if(list.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(Splash.this, "No Match found",Toast.LENGTH_LONG).show();
                }*/
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {


        /*        for (userModel value : arrayList) {
                    if (!value.getName().toLowerCase().contains(newText.toLowerCase())) {
                        arrayList.remove(value);
                    }
                }

                adapter = new MailAdapter(getActivity(),arrayList);
                listView.setAdapter(adapter);
*/
        //////////////////////////////////////ahtsham
        ArrayList<User> userModelArrayList = new ArrayList<>();
                if (newText.length()>0){
                    for (User user:userArrayList) {
                        if (user.getName().toLowerCase().contains(newText.toLowerCase())) {
                            userModelArrayList.add(user);
                            //Toast.makeText(getActivity().getApplicationContext(), "" + arrayList.get(i).getName(), Toast.LENGTH_SHORT).show();
                            //adapter.notifyDataSetChanged();
                        }
                    }
                    adapter = new FollowAdapter(getActivity(),userModelArrayList);
                    listview.setAdapter(adapter);
                }else {
                    adapter = new FollowAdapter(getActivity(),userArrayList);
                    listview.setAdapter(adapter);
                }
               /* for (int i = 0;i<arrayList.size();i++){

                }*/

                //listView.setAdapter(adapter);

                /* Toast.makeText(getActivity().getApplicationContext(), "Hi"+newText, Toast.LENGTH_SHORT).show();*/
                return true;
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Not implemented here
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
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
