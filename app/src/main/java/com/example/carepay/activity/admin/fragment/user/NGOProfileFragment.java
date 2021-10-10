package com.example.carepay.activity.admin.fragment.user;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.carepay.R;
import com.example.carepay.activity.ngo.fragment.post.PostImageVideoFragment;
import com.example.carepay.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class NGOProfileFragment extends Fragment implements View.OnClickListener {


    public NGOProfileFragment() {
        // Required empty public constructor
    }
    TextView name,email,text_update_password;
    ImageView profile,nameimg;
    CircleImageView profilepic;
    User user;
    Bitmap bitmap;
    Uri tempUri;
    String filename;
    Dialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.ngoprofile, container, false);
        init(view);
        user = new User();
        profile.setOnClickListener(this);
        nameimg.setOnClickListener(this);
        getActivity().setTitle("Edit Profile");
        progressDialog = new Dialog(getActivity());
        text_update_password = view.findViewById(R.id.text_update_password);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        TextView progressTv = progressDialog.findViewById(R.id.progress_tv);
        progressTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        progressTv.setText("Creating account");
        progressTv.setTextSize(19F);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        //progressDialog.show();
        loadData();
        text_update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] m_Text = {""};
                AlertDialog.Builder builder = new AlertDialog.Builder(((AppCompatActivity)getActivity()));
                builder.setTitle("Update password?");

// Set up the input
                final EditText input = new EditText((AppCompatActivity)getActivity());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetpassword(input.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        return view;
    }

    private void loadData() {
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    user=dataSnapshot.getValue(User.class);
                    Glide.with(getActivity()).load(user.getProfilePicUrl()).into(profilepic);
                    name.setText(user.getName());
                    email.setText(user.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init(View view) {
        name=view.findViewById(R.id.edt_profilename);
        email=view.findViewById(R.id.edt_email);
        profile=view.findViewById(R.id.editphotoprofile);
        nameimg=view.findViewById(R.id.edit_nameimg);
        profilepic=view.findViewById(R.id.profileimage);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.edit_nameimg:
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.custom_dialogtwo);
                dialog.setTitle("Title");

                Button button_update = (Button) dialog.findViewById(R.id.btn_update);
                Button button_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
                button_update.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        EditText edit=(EditText)dialog.findViewById(R.id.edt_profilename);
                        String text=edit.getText().toString();
                        user.setName(text);
                        updateData();
                        dialog.dismiss();
                        //name=text;

                    }
                });
                button_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });

                dialog.show();
                break;
            case R.id.editphotoprofile:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
                break;
            default:
                break;
        }
    }
    public void updateData() {
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            filename = data.getData().getPath();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            tempUri = getImageUri(getActivity().getApplicationContext(), bitmap);
            uploadfile();
            progressDialog.show();
            profilepic.setImageBitmap(bitmap);
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public void uploadfile(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String datename = String.valueOf(Calendar.getInstance().getTimeInMillis());
        String abc = "abcdefghijklmnopqrstvuwxyz123456789";
        for (int i=0;i<5;i++){
            final int random = new Random().nextInt(35);
            datename+=abc.charAt(random);
        }
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpg")
                .build();
        final StorageReference ref = storageRef.child("images/"+datename+".jpg");
        UploadTask uploadTask = ref.putFile(tempUri,metadata);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Toast.makeText(getActivity().getApplicationContext(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    user.setProfilePicUrl(downloadUri.toString());

                    updateData();
                    //onAuthSuccess(firebaseUser, downloadUri.toString());
                } else {
                    Toast.makeText( getActivity().getApplicationContext(), "failure"+task.getException(), Toast.LENGTH_SHORT).show();
                }
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
    void resetpassword(String reset){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(reset)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Password updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
