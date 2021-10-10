package com.example.carepay.activity.stripe;


import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.carepay.R;
import com.example.carepay.helper.StaticClass;
import com.example.carepay.model.User;
import com.example.carepay.retrofit.ApiClient;
import com.example.carepay.retrofit.ApiInterface;
import com.example.carepay.retrofitModel.retroModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PaymentFragment extends Fragment {


    public PaymentFragment() {
        // Required empty public constructor
    }
    CardInputWidget cardInputWidget;
    Button button;
    TextView txt_name,text_receiver_account_number;
    CircleImageView ngo_image;
//    MySharedPref mySharedPref;
    String ngo_imag;
    String to_name;
    User data = new User();
    Dialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        cardInputWidget = view.findViewById(R.id.card_input_widget);
        final EditText edit_rent = view.findViewById(R.id.edit_rent);
        txt_name=view.findViewById(R.id.txt_name);
        text_receiver_account_number = view.findViewById(R.id.text_receiver_account_number);
        ngo_image=view.findViewById(R.id.img_ngo);
        Bundle bundle = getArguments();
        final String to_uid = bundle.getString("uid");
        FirebaseDatabase.getInstance().getReference().child("users").child(to_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot!=null){
                    data= dataSnapshot.getValue(User.class);
                    txt_name.setText(data.getName());
                    Glide.with(getActivity()).load(data.getProfilePicUrl()).into(ngo_image);
                    if (data.getAccount_number()!=null)
                        text_receiver_account_number.setText(data.getAccount_number());
                    else{

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
         to_name = bundle.getString("name");
         ngo_imag = bundle.getString("image");

        //edit_rent.setText("45454"+"$");

        //Toast.makeText(getActivity().getApplicationContext(), "", Toast.LENGTH_SHORT).show();
        button = view.findViewById(R.id.btn_card);////test:sk_test_IXnaZk4wP4O0vMLjWwP1u40q
        //sk:pk_test_NeK3qOq5LDE4yKdBt7REDoXu
        final Stripe stripe = new Stripe(getActivity().getApplicationContext(),"pk_test_NeK3qOq5LDE4yKdBt7REDoXu");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Card cardToSave = cardInputWidget.getCard();
                if (edit_rent.getText().toString().isEmpty()){
                    Toast.makeText(getActivity().getApplicationContext(), "Please fill all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (cardToSave == null) {

                    Toast.makeText(getActivity().getApplicationContext(), "Invalid Card", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog = new Dialog(getActivity());
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                progressDialog.setContentView(R.layout.custom_dialog_progress);
                TextView progressTv = progressDialog.findViewById(R.id.progress_tv);
                if(progressDialog.getWindow() != null) {
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                }
                progressDialog.setCancelable(false);
                progressDialog.show();
                stripe.createToken(
                        new Card(cardToSave.getNumber(), cardToSave.getExpMonth(), cardToSave.getExpYear(), cardToSave.getCVC()),
                        new TokenCallback() {
                            @Override
                            public void onError(Exception error) {
                                Toast.makeText(getActivity().getApplicationContext(), "Errror :"+error, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(final Token token) {
                                Gson gson = new GsonBuilder().create();
                                String s = gson.toJson(token);
                                Log.d("Tokken",token.getId());
                                //Toast.makeText(getActivity().getApplicationContext(), "Ceck logcat", Toast.LENGTH_SHORT).show();
                                ApiInterface apiService =
                                        ApiClient.getClient().create(ApiInterface.class);
                                Call<retroModel> call = apiService.stripeTokken(token.getId(), String.valueOf(Integer.valueOf(edit_rent.getText().toString())/154));
                                call.enqueue(new Callback<retroModel>() {
                                    @Override
                                    public void onResponse(Call<retroModel>call, Response<retroModel> response) {
                                        //if (response.body()!=null)
                                        {
                                            HashMap<String,String> myvalue=new HashMap<>();
                                            SimpleDateFormat sdfff = new SimpleDateFormat("HH:mm:ss EEE MMM dd yyyy");
                                            String currentDateandTime = sdfff.format(new Date());
                                            myvalue.put("uid",FirebaseAuth.getInstance().getUid());
                                            myvalue.put("amount",String.valueOf(Integer.valueOf(edit_rent.getText().toString())/154));
                                            myvalue.put("sender_name", StaticClass.name);
                                            myvalue.put("receiver_name",data.getName());
                                            myvalue.put("date", currentDateandTime);
                                            myvalue.put("sender_image_url", StaticClass.url);
                                            myvalue.put("receiver_image_url", data.getProfilePicUrl());
                                            myvalue.put("ruid", to_uid);
                                            myvalue.put("receiver_account", text_receiver_account_number.getText().toString());

                                            String key = FirebaseDatabase.getInstance().getReference().child("donate").child(to_uid).push().getKey();
                                            FirebaseDatabase.getInstance().getReference().child("donate").child(to_uid).child(key).setValue(myvalue).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful())
                                                        Toast.makeText(getActivity().getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }
                                        progressDialog.dismiss();
//                                            Toast.makeText(getActivity().getApplicationContext(), "Here: "+response.body().getError_msg(), Toast.LENGTH_SHORT).show();
//                                        Log.d("Response",response.body().getError_msg());
                                    }
                                    @Override
                                    public void onFailure(Call<retroModel>call, Throwable t) {
                                        Toast.makeText(getActivity().getApplicationContext(), "Error:"+t, Toast.LENGTH_SHORT).show();
                                        Log.e("Error", t.toString());
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        }
                );

            }
        });
        return view;
    }

}
