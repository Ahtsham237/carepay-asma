package com.example.carepay.activity.admin.fragment.delete;


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
import com.example.carepay.activity.admin.fragment.HomeScreenFragment;
import com.example.carepay.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditDeleteFragment extends Fragment {

    ListView listview;
    ArrayList<User> modelArrayList;
    public EditDeleteFragment() {
        // Required empty public constructor
    }
    EditDeleteAdapter customListAdapter;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_edit_delete, container, false);
        modelArrayList = new ArrayList<>();
        getActivity().setTitle("Delete User");
        listview=view.findViewById(R.id.listview);
        customListAdapter=new EditDeleteAdapter(getActivity(),modelArrayList);
        loaddata();
        listview.setAdapter(customListAdapter);
        return view;
    }

    private void loaddata() {

        FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    User user = dataSnapshot.getValue(User.class);
                    if (!user.getRole().equals("admin"))
                        modelArrayList.add(user);
                }
                customListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                customListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    User user = dataSnapshot.getValue(User.class);
                    modelArrayList.remove(user);
                }
                customListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                customListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                customListAdapter.notifyDataSetChanged();
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
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new HomeScreenFragment()).commit();
                    return true;
                }
                return false;
            }
        });
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
                    for (User user:modelArrayList) {
                        if (user.getName().toLowerCase().contains(newText.toLowerCase())) {
                            userModelArrayList.add(user);
                            //Toast.makeText(getActivity().getApplicationContext(), "" + arrayList.get(i).getName(), Toast.LENGTH_SHORT).show();
                            //adapter.notifyDataSetChanged();
                        }
                    }
                    customListAdapter = new EditDeleteAdapter(getActivity(),userModelArrayList);
                    listview.setAdapter(customListAdapter);
                }else {
                    customListAdapter = new EditDeleteAdapter(getActivity(),modelArrayList);
                    listview.setAdapter(customListAdapter);
                }
               /* for (int i = 0;i<arrayList.size();i++){

                }*/

                //listView.setAdapter(customListAdapter);

                /* Toast.makeText(getActivity().getApplicationContext(), "Hi"+newText, Toast.LENGTH_SHORT).show();*/
                return true;
            }
        });

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
}
