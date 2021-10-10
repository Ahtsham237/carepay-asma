package com.example.carepay.activity.user.fragment.donate;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.carepay.R;
import com.example.carepay.activity.stripe.PaymentFragment;
import com.example.carepay.model.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class DonateAdapter extends BaseAdapter {
    Activity context;
    LayoutInflater inflater;
    ArrayList<User> list;
    public DonateAdapter(Activity context , ArrayList<User> list){
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
        View view=inflater.inflate(R.layout.item_donate,null,true);
        TextView txt_name,txt_type;
        CircleImageView userimg;
        ImageView icondelete;
        LinearLayout linear;
        linear = view.findViewById(R.id.linear);
        txt_name = view.findViewById(R.id.txt_name);
        userimg = view.findViewById(R.id.userimg);
        final User user = list.get(position);
        if (user.getName()!=null)
            txt_name.setText(user.getName());
        if (user.getProfilePicUrl()!=null)
            Glide.with(context).load(user.getProfilePicUrl()).into(userimg);
        linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentFragment fragment=new PaymentFragment();
                Bundle bundle= new Bundle();
                bundle.putString("uid",user.getUid());
                bundle.putString("name",user.getName());
                bundle.putString("image",user.getProfilePicUrl());
                fragment.setArguments(bundle);
                ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).commit();
            }
        });
        return view;
    }
}
