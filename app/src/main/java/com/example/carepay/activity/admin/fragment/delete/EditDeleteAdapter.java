package com.example.carepay.activity.admin.fragment.delete;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.carepay.R;
import com.example.carepay.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;


public class EditDeleteAdapter extends BaseAdapter {
    Activity context;
    LayoutInflater inflater;
    ArrayList<User> list;
    public EditDeleteAdapter(Activity context , ArrayList<User> list){
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
        if (context==null)
            return r;
        LayoutInflater inflater=context.getLayoutInflater();
        View view=inflater.inflate(R.layout.item_edit_delete,null,true);
        TextView txt_name,txt_type;
        CircleImageView userimg;
        ImageView icondelete;
        txt_name = view.findViewById(R.id.txt_name);
        txt_type = view.findViewById(R.id.txt_type);
        userimg = view.findViewById(R.id.userimg);
        icondelete = view.findViewById(R.id.deleteicon);
        User user = list.get(position);
        if (user.getName()!=null)
            txt_name.setText(user.getName());
        if (user.getRole()!=null)
            txt_type.setText(user.getRole().toUpperCase());
        if (user.getProfilePicUrl()!=null)
            Glide.with(context).load(user.getProfilePicUrl()).into(userimg);
         icondelete.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 SweetAlertDialog pDialog = new SweetAlertDialog(((AppCompatActivity)context), SweetAlertDialog.WARNING_TYPE);
                 //pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                 pDialog.setTitleText("Are you sure you want to delete?");
                 pDialog.setCancelText("Cancel");
                 pDialog.setConfirmText("Delete");
                 //pDialog.setCancelable(false);
                 pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                     @Override
                     public void onClick(SweetAlertDialog sweetAlertDialog) {
                         FirebaseDatabase.getInstance().getReference().child("users").child(list.get(position).getUid()).removeValue(new DatabaseReference.CompletionListener() {
                             @Override
                             public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                 FirebaseDatabase.getInstance().getReference().child("posts").child(list.get(position).getUid()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                     @Override
                                     public void onComplete(@NonNull Task<Void> task) {
                                         list.remove(position);
                                         notifyDataSetChanged();
                                     }
                                 });
                             }
                         });
                         sweetAlertDialog.dismissWithAnimation();
                     }
                 });
                 pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                     @Override
                     public void onClick(SweetAlertDialog sweetAlertDialog) {
                          sweetAlertDialog.dismiss();
                     }
                 });
                 pDialog.show();
             }
         });
        return view;
    }
}
