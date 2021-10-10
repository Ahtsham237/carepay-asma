package com.example.carepay.activity.admin.fragment.addngo;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
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

import com.example.carepay.R;
import com.example.carepay.activity.admin.fragment.HomeScreenFragment;
import com.example.carepay.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {




    public AddFragment() {
        // Required empty public constructor
    }
    ImageView SignUpbutton;
    TextView alreadyaccountlogin;
    Button upload;
    ProgressDialog nDialog;
    String picturePath="";
    Dialog progressDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    File compressedImgFile;
    Bitmap bitmap;
    EditText name,email,password,account_number;
    private String filename;
    Uri tempUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_ngo, container, false);
        mAuth = FirebaseAuth.getInstance();
        getActivity().setTitle("Add NGO");
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        account_number = view.findViewById(R.id.account_number);
        upload = view.findViewById(R.id.uploadimage);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });
        SignUpbutton = view.findViewById(R.id.signupbutton);
        SignUpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean flag = true;
                if (name.getText().toString().isEmpty()) {
                    flag  =false;
                }
                if (email.getText().toString().isEmpty())
                {
                    flag  =false;
                }
                if (password.getText().toString().isEmpty())
                {
                    flag  =false;
                }
                if (account_number.getText().toString().isEmpty())
                {
                    flag  =false;
                }
                if ( tempUri == null){
                    flag  =false;
                    Toast.makeText(getActivity().getApplicationContext(), "please choose a image", Toast.LENGTH_SHORT).show();
                }
                if (flag){
                    progressDialog = new Dialog(getActivity());
                    progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    progressDialog.setContentView(R.layout.custom_dialog_progress);
                    TextView progressTv = progressDialog.findViewById(R.id.progress_tv);
                    progressTv.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.white));
                    progressTv.setText("Creating account");
                    progressTv.setTextSize(19F);
                    if(progressDialog.getWindow() != null)
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    createUser();

                }else {
                    Toast.makeText(getActivity().getApplicationContext(), "Please fill all fidels", Toast.LENGTH_SHORT).show();

                }
            }
        });
        alreadyaccountlogin = view.findViewById(R.id.alreadyaccountlogin);
         alreadyaccountlogin.setVisibility(View.GONE);
        return view;
    }
    private void createUser() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {
                            uploadfile( task.getResult().getUser());
                        } else {
                            try {
                                progressDialog.dismiss();
                                Exception e = task.getException();
                                e.printStackTrace();
                                Log.d("exception:", e.getMessage());
                                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d("exception:", e.getMessage());
                            }
                        }
                    }
                });
    }
    public void uploadfile( final FirebaseUser firebaseUser){
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
                    onAuthSuccess(firebaseUser, downloadUri.toString());
                } else {
                    Toast.makeText( getActivity().getApplicationContext(), "failure"+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void onAuthSuccess(FirebaseUser firebaseUser,String url) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        User user = new User();
        user.setName(name.getText().toString());
        user.setEmail(firebaseUser.getEmail());
        user.setUid(firebaseUser.getUid());
        user.setProfilePicUrl(url);
        user.setRole("ngo");
        user.setAccount_number(account_number.getText().toString());
        mDatabase.child("users").child(firebaseUser.getUid()).setValue(user);
        progressDialog.dismiss();
        name.setText("");
        email.setText("");
        password.setText("");
        account_number.setText("");
        /*Intent i = new Intent(SignUp.this, LogIn.class);
        startActivity(i);
        finish();*/
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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
        }
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

}
