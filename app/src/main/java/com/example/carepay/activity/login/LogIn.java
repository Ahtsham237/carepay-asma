package com.example.carepay.activity.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.example.carepay.R;
import com.example.carepay.activity.ngo.NGOMainScreen;
import com.example.carepay.activity.user.MainActivity;
import com.example.carepay.activity.admin.AdminActivity;
import com.example.carepay.activity.signup.SignUp;
import com.example.carepay.helper.SessionManager;
import com.example.carepay.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogIn extends AppCompatActivity {
    private static final String TAG = "AndroidClarified";
    private SignInButton googleSignInButton;
    private GoogleSignInClient googleSignInClient;
    TextView signin;
    EditText username,password;
    String first_name ;
    String last_name;
    String gettext;
    TextView forget_password;
    TextView notaccountyet;
    EditText editText;
    String passworddreset;
    String confirmpass;
    String image_url;
    Dialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        signin=findViewById(R.id.signinbutton);

        setTitle("Sign In");
        username=findViewById(R.id.namelogin);
        password=findViewById(R.id.signinpassword);
        notaccountyet=findViewById(R.id.notaccountyet);
        forget_password=findViewById(R.id.forgetpassword);
        progressDialog = new Dialog(LogIn.this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        TextView progressTv = progressDialog.findViewById(R.id.progress_tv);
        if(progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        progressDialog.setCancelable(false);
//        progressDialog.show();
        SessionManager sessionManager=new SessionManager(getApplicationContext());
        notaccountyet.setPaintFlags(notaccountyet.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        notaccountyet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inten=new Intent(LogIn.this, SignUp.class);
                startActivity(inten);
                finish();
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                if (!isValidEmail(username.getText().toString())||username.getText().toString().isEmpty()){
                    flag = false;
                    Snackbar.make(findViewById(android.R.id.content),"Please enter valid email address",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (password.getText().toString().isEmpty())
                    flag = false;
                if (flag){
                    progressDialog.show();
                    login();
                }else {
                    Snackbar.make(findViewById(android.R.id.content),"Please enter passsword",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void login(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(username.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference().child("users");

// Attach a listener to read the data at our posts reference
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            SessionManager sessionManager=new SessionManager(getApplicationContext());
                            sessionManager.setLogin(true);
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                User user=   snapshot.getValue(User.class);
                                progressDialog.dismiss();
                                //Toast.makeText(LogIn.this, ""+user.getEmail(), Toast.LENGTH_SHORT).show();
                                if (user.getUid().equals(FirebaseAuth.getInstance().getUid())){
                                    if (user.getRole().equals("admin")){
                                        sessionManager.setRole("admin");
                                        Intent intent=new Intent(LogIn.this, AdminActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    if (user.getRole().equals("user")){
                                        sessionManager.setRole("user");
                                        Intent intent=new Intent(LogIn.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    if (user.getRole().equals("ngo")){
                                        sessionManager.setRole("ngo");
                                        Intent intent=new Intent(LogIn.this, NGOMainScreen.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressDialog.dismiss();
                            Snackbar.make(findViewById(android.R.id.content),""+databaseError.getMessage(),Snackbar.LENGTH_SHORT).show();
                        }
                    });
                    /*Query query = FirebaseDatabase.getInstance().getReference().child("user");
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            SessionManager sessionManager=new SessionManager(getApplicationContext());
                            sessionManager.setLogin(true);
                            //FirebaseUser user =   FirebaseAuth.getInstance().getCurrentUser();
                            //task.getResult();
                            Intent intent=new Intent(LogIn.this, AdminActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/

                }else {
                    progressDialog.dismiss();
                    Snackbar.make(findViewById(android.R.id.content),"LogIn Failed.",Snackbar.LENGTH_SHORT).show();
                    //Toast.makeText(LogIn.this, task.getException()+"i dont know", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public boolean isValidEmail(CharSequence target) {

        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}
