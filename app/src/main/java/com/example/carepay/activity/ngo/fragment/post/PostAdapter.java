package com.example.carepay.activity.ngo.fragment.post;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.carepay.R;
import com.example.carepay.model.ngoData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends BaseAdapter {
    Activity context;
    LayoutInflater inflater;
    ArrayList<ngoData> list;
    public PostAdapter(Activity context , ArrayList<ngoData> list){
        this.context=context;
        this.list = list;
//        Collections.sort(list,Collections.reverseOrder());
        //Collections.reverse(this.list);
//        for (int i =list.size()-1;i>-1;i++){
//            this.list.add(list.get(i));
//        }
//        inflater  =context.getLayoutInflater();
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
        View view=inflater.inflate(R.layout.item_post,null,true);
        final TextView txt_name,txt_type,txt_caption,txt_detail;
        CircleImageView userimg;
        final ImageView postimage,editdetails,imgplayvideo;
        final ImageView vidView;
        RelativeLayout videolayout;
        videolayout = view.findViewById(R.id.videolayout);
        imgplayvideo = view.findViewById(R.id.imgplayvideo);
        editdetails = view.findViewById(R.id.edit_details);
        txt_detail = view.findViewById(R.id.txt_detail);
        txt_caption = view.findViewById(R.id.txt_caption);
        txt_type = view.findViewById(R.id.txt_type);
        txt_name = view.findViewById(R.id.txt_name);
         userimg = view.findViewById(R.id.userimg);
        postimage = view.findViewById(R.id.postimage);
        vidView = view.findViewById(R.id.postvideo);
        final ngoData user = list.get(list.size()-1-position);
        if (user.getType()==null)
            return view;
        txt_name.setText(user.getUsername());
        Glide.with(context).load(user.getUserimage()).into(userimg);
        txt_caption.setText(user.getDetail());
        txt_type.setText(user.getDate());

        if (user.getType().equals("video")){
            postimage.setVisibility(View.GONE);
            videolayout.setVisibility(View.VISIBLE);
            txt_detail.setVisibility(View.GONE);
            String vidAddress =user.getLink() ;
            Uri vidUri = Uri.parse(vidAddress);
            Glide.with(context).load(vidUri).into(vidView);
            //vidView.setVideoURI(vidUri);
            //Glide.with(context).load(vidUri).into(imgplayvideo);
            /*String vidAddress =user.getLink() ;
            Uri vidUri = Uri.parse(vidAddress);

            File localFile = null;
            try {
                localFile = File.createTempFile("images", "mp4");
            } catch (IOException e) {
                e.printStackTrace();
            }
            final File finalLocalFile = localFile;
            if (user.getFilename()!=null)
            FirebaseStorage.getInstance().getReference().child(user.getFilename()).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    //vidView.setVideoURI(vidUri);
                    vidView.setVideoPath(finalLocalFile.getPath());
                    MediaController vidControl = new MediaController(context);
                    vidControl.setAnchorView(vidView);
                    vidView.setMediaController(vidControl);
                    //vidView.start();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                }
            });*/
        }else if (user.getType().equals("image")){
            postimage.setVisibility(View.VISIBLE);

            txt_detail.setVisibility(View.GONE);
            videolayout.setVisibility(View.GONE);
            Glide.with(context).load(user.getLink()).into(postimage);
        }else {
            txt_caption.setText("");
            postimage.setVisibility(View.GONE);
            txt_detail.setVisibility(View.VISIBLE);
            videolayout.setVisibility(View.GONE);
            txt_detail.setText(user.getLink());

        }
        imgplayvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVideo(user);
            }
        });
        postimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage(user);
            }
        });
        /*if (user.getName()!=null)
            txt_name.setText(user.getName());
        if (user.getRole()!=null)
            txt_type.setText(user.getRole().toUpperCase());
        if (user.getProfilePicUrl()!=null)
            Glide.with(context).load(user.getProfilePicUrl()).into(userimg);
         icondelete.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 FirebaseDatabase.getInstance().getReference().child("users").child(list.get(position).getUid()).removeValue(new DatabaseReference.CompletionListener() {
                     @Override
                     public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        list.remove(position);
                        notifyDataSetChanged();
                         //Toast.makeText(context, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                     }
                 });
                 //Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
             }
         });*/
        editdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(((AppCompatActivity)context), editdetails);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popupmenu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.popup_delete:

                                SweetAlertDialog pDialog = new SweetAlertDialog(((AppCompatActivity)context), SweetAlertDialog.WARNING_TYPE);
                                //pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                pDialog.setTitleText("Delete item?");
                                pDialog.setCancelText("Cancel");
                                pDialog.setConfirmText("Delete");
                                //pDialog.setCancelable(false);
                                pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                });
                                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        if (list.get(position).getKey()!=null)
                                            FirebaseDatabase.getInstance().getReference().child("posts")
                                                    .child(list.get(list.size()-1-position).getUid())
                                                    .child(list.get(list.size()-1-position).getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    list.remove(list.size()-1-position);
                                                    notifyDataSetChanged();
                                                    //Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        sweetAlertDialog.dismiss();
                                    }
                                });
                                pDialog.show();

                                //Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.popup_edit:
                                final String[] m_Text = {""};
                                AlertDialog.Builder builder = new AlertDialog.Builder(((AppCompatActivity)context));
                                builder.setTitle("Update ?");

// Set up the input
                                final EditText input = new EditText((AppCompatActivity)context);
                                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
                                builder.setView(input);
                                final ngoData ngo = list.get(list.size()-1-position);
                                if (ngo.getType().equals("text")){
                                    input.setText(ngo.getLink());
                                }
// Set up the buttons
                                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        m_Text[0] = input.getText().toString();
                                        final ngoData ngodata = list.get(list.size()-1-position);
                                        if (ngo.getType().equals("text")){
                                            ngodata.setLink( m_Text[0]);
                                        }else
                                            ngodata.setDetail( m_Text[0]);
                                        FirebaseDatabase.getInstance().getReference().child("posts").child(ngodata.getUid())
                                                .child(ngodata.getKey()).setValue(ngodata).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                SweetAlertDialog pDialog = new SweetAlertDialog(((AppCompatActivity)context),
                                                        SweetAlertDialog.SUCCESS_TYPE);
                                                pDialog.setTitleText("Updated successfully");
                                                pDialog.setConfirmText("OK");
                                                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                         list.remove(list.size()-1-position);
                                                         list.add(list.size()-1-position,ngodata);
                                                       ///  notifyDataSetChanged();
                                                        sweetAlertDialog.dismiss();
                                                    }
                                                });
                                                pDialog.show();
                                            }
                                        });
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                                break;
                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu

            }
        });
        return view;
    }

    private void showImage(ngoData user){
        final Dialog dialog = new Dialog(context,android.R.style.Theme_NoTitleBar_Fullscreen);
        //ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.custom_image);

        dialog.setTitle("Title");
        CircularImageView button_cancel = dialog.findViewById(R.id.close_img);
        ImageView imageView = dialog.findViewById(R.id.big_image);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Glide.with(context).load(user.getLink()).into(imageView);
        dialog.show();
    }
    private void showVideo(ngoData user){
        final Dialog dialog = new Dialog(context,android.R.style.Theme_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.custom_video);
        dialog.setTitle("Title");
        final VideoView vidView = dialog.findViewById(R.id.big_videoview);
        CircularImageView button_cancel = dialog.findViewById(R.id.close_img);
        final ProgressBar progress_bar = dialog.findViewById(R.id.progress_bar);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        String vidAddress =user.getLink() ;
        Uri vidUri = Uri.parse(vidAddress);

        File localFile = null;
        try {
            localFile = File.createTempFile("images", "mp4");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final File finalLocalFile = localFile;
        if (user.getFilename()!=null)
            FirebaseStorage.getInstance().getReference().child(user.getFilename()).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    //vidView.setVideoURI(vidUri);
                    progress_bar.setVisibility(View.GONE);
                    vidView.setVideoPath(finalLocalFile.getPath());
                    MediaController vidControl = new MediaController(context);
                    vidControl.setAnchorView(vidView);
                    vidView.setMediaController(vidControl);
                    vidView.start();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                }
            });

        dialog.show();
    }
}
