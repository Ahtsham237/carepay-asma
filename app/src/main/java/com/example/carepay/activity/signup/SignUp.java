package com.example.carepay.activity.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carepay.R;
import com.example.carepay.activity.login.LogIn;
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

public class SignUp extends AppCompatActivity {
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
    EditText  name,email,password;
    private String filename;
    Uri tempUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        setTitle("Sign Up");
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        upload=findViewById(R.id.uploadimage);
        setTitle("Sign Up");
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });
        SignUpbutton = findViewById(R.id.signupbutton);
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
                if ( tempUri ==null){
                    flag  =false;
                    Toast.makeText(SignUp.this, "please choose a image", Toast.LENGTH_SHORT).show();
                }
                if (flag){
                    progressDialog = new Dialog(SignUp.this);
                    progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    progressDialog.setContentView(R.layout.custom_dialog_progress);
                    TextView progressTv = progressDialog.findViewById(R.id.progress_tv);
                    progressTv.setTextColor(ContextCompat.getColor(SignUp.this, R.color.white));
                    progressTv.setText("Creating account");
                    progressTv.setTextSize(19F);
                    if(progressDialog.getWindow() != null)
                        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                   createUser();

                }else {
                    Toast.makeText(SignUp.this, "Please fill all fidels", Toast.LENGTH_SHORT).show();

                }
            }
        });
        alreadyaccountlogin = findViewById(R.id.alreadyaccountlogin);
        alreadyaccountlogin.setPaintFlags(alreadyaccountlogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        alreadyaccountlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUp.this, LogIn.class);
                startActivity(i);
                finish();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            filename = data.getData().getPath();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            tempUri = getImageUri(getApplicationContext(), bitmap);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,LogIn.class);
        startActivity(intent);
        finish();
    }

    private void init()
    {
        mAuth = FirebaseAuth.getInstance();
    }
    private void createUser() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {
                            uploadfile( task.getResult().getUser());

                        } else {
                            try {
                                Exception e = task.getException();
                                e.printStackTrace();
                                Log.d("exception:", e.getMessage());
                                Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(SignUp.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText( getApplicationContext(), "failure"+task.getException(), Toast.LENGTH_SHORT).show();
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
        user.setRole("user");
        mDatabase.child("users").child(firebaseUser.getUid()).setValue(user);
        progressDialog.dismiss();
        Intent i = new Intent(SignUp.this, LogIn.class);
        startActivity(i);
        finish();
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
