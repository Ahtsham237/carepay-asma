package com.example.carepay.activity.user.fragment.followngo;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.carepay.R;
import com.example.carepay.activity.user.fragment.viewprofile.ViewProfileFragment;
import com.example.carepay.helper.StaticClass;
import com.example.carepay.model.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class FollowAdapter extends BaseAdapter {
    Activity context;
    LayoutInflater inflater;
    ArrayList<User> list;
    TextView follow;

    public FollowAdapter(Activity context , ArrayList<User> list){
        this.context=context;
        this.list = list;
        //inflater  =context.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public View getView(final int position, View r, ViewGroup parent){

        LayoutInflater inflater=context.getLayoutInflater();
        final View view=inflater.inflate(R.layout.followngo,null,true);
        TextView txt_name,txt_type;
        CircleImageView userimg;
        txt_name = view.findViewById(R.id.txt_name);
        //txt_type = view.findViewById(R.id.txt_type);
        userimg = view.findViewById(R.id.userimg);
        follow = view.findViewById(R.id.follow);
        final User user = list.get(position);
        if (user.getName()!=null)
            txt_name.setText(user.getName());
        if (user.getRole()!=null)
           // txt_type.setText(user.getRole().toUpperCase());
        if (user.getProfilePicUrl()!=null)
            Glide.with(context).load(user.getProfilePicUrl()).into(userimg);
        follow.setTag(position);
        if (user.getFollower()!=null){
            if (user.getFollower().containsValue(FirebaseAuth.getInstance().getUid())) {
                follow.setText("Unfollow");
            }else {
                follow.setText("Follow");
            }
            //user.set
        }
        //if (follow.getText().toString().toLowerCase().equals("follow"))

        txt_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticClass.user = list.get(position);
                Fragment viewprofile=new ViewProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putString("uid",list.get(position).getUid());
                viewprofile.setArguments(bundle);
                ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction()
                .addToBackStack("dvdvsdsdv").replace(R.id.framelayout,viewprofile).commit();
            }
        });
        return view;
    }
}
