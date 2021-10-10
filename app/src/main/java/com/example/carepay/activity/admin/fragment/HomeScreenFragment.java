package com.example.carepay.activity.admin.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.carepay.R;
import com.example.carepay.activity.admin.fragment.addngo.AddFragment;
import com.example.carepay.activity.admin.fragment.delete.EditDeleteFragment;
import com.example.carepay.activity.admin.fragment.transection.AdminChekTransectionFragment;
import com.example.carepay.activity.admin.fragment.user.ProfileFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeScreenFragment extends Fragment implements View.OnClickListener {


    public HomeScreenFragment() {
        // Required empty public constructor
    }
    TextView addngo,deleteuser,editprofile,viewtransections;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);
        addngo = view.findViewById(R.id.text_addngo);
        getActivity().setTitle("Home");
        deleteuser = view.findViewById(R.id.text_deleteuser);
        editprofile = view.findViewById(R.id.text_editprofile);
        viewtransections = view.findViewById(R.id.text_transection);
        addngo.setOnClickListener(this);
        viewtransections.setOnClickListener(this);
        deleteuser.setOnClickListener(this);
        editprofile.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.text_editprofile:
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("ProfileFragment").replace(R.id.framelayout,new ProfileFragment()).commit();
                break;
            case R.id.text_transection:
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("text_transection").replace(R.id.framelayout,new AdminChekTransectionFragment()).commit();
                break;
            case R.id.text_deleteuser:
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("text_deleteuser").replace(R.id.framelayout,new EditDeleteFragment()).commit();
                break;
            case R.id.text_addngo:
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("text_addngo").replace(R.id.framelayout,new AddFragment()).commit();
                break;
            default:
                break;
        }
    }
}
