package com.example.carepay.activity.admin.fragment.transection;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.carepay.R;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class AdminTransactionAdapter extends BaseAdapter {
    Activity context;
    LayoutInflater inflater;
    ArrayList<HashMap<String,String>>   list;
    public AdminTransactionAdapter(Activity context , ArrayList<HashMap<String,String>>   list){
        this.context=context;
        this.list = list;
       // inflater  =context.getLayoutInflater();
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
        View view=inflater.inflate(R.layout.item_transaction,null,true);
        TextView txt_name,txt_type;
        CircleImageView userimg;
        ImageView icondelete;
        LinearLayout linear;
        linear = view.findViewById(R.id.linear);
        txt_name = view.findViewById(R.id.txt_name);
        userimg = view.findViewById(R.id.userimg);
        HashMap<String,String> map = list.get(position);
        String date = map.get("date");
        String amount = map.get("amount");
        String sendername = map.get("sender_name");
        String receivername = map.get("receiver_name");
        String image_url = map.get("sender_image_url");
        if (sendername!=null)
            txt_name.setText(sendername+" have deposited Rs."+amount+" in "+receivername+" account on "+date);
        if (image_url!=null)
            Glide.with(context).load(image_url).into(userimg);
        return view;
    }

}
