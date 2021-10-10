package com.example.carepay.activity.ngo.fragment.post;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carepay.R;
import com.example.carepay.model.User;
import com.example.carepay.model.ngoData;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostImageVideoFragment extends Fragment implements View.OnClickListener {

    Bitmap bitmap;
    ListView listview;
    EditText edit_details;
    TextView text_post,text_upload;
    private String filename;
    Uri tempUri;
    ngoData data;
    PostAdapter postAdapter;
    ArrayList<ngoData> arrayList;
    User user;
    public PostImageVideoFragment() {

    }
    HashMap<String, String> group = new HashMap<>();
    ImageView image_to_upload,video_to_upload;
    Dialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_image_video, container, false);
        data = new ngoData();
        listview = view.findViewById(R.id.listview);
        image_to_upload = view.findViewById(R.id.image_to_upload);
        video_to_upload = view.findViewById(R.id.video_to_upload);
        edit_details = view.findViewById(R.id.edit_details);
        text_upload = view.findViewById(R.id.text_upload);
        text_post = view.findViewById(R.id.text_post);
        video_to_upload.setOnClickListener(this);
        image_to_upload.setOnClickListener(this);
        text_post.setOnClickListener(this);
        getActivity().setTitle("Home");
        arrayList = new ArrayList<>();
        postAdapter = new PostAdapter(getActivity(),arrayList);
        listview.setAdapter(postAdapter);
        progressDialog = new Dialog(getActivity());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        TextView progressTv = progressDialog.findViewById(R.id.progress_tv);
        if(progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        progressDialog.setCancelable(false);
        progressDialog.show();
        group.put("type","text");
        loadAllData();
        justifyListViewHeightBasedOnChildren(listview);
        return view;
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.image_to_upload:
                data.setType("image");
                group.put("type","image");
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
                break;
            case R.id.video_to_upload:
                data.setType("video");
                group.put("type","video");
                photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("video/*");
                startActivityForResult(photoPickerIntent, 2);
                break;
            case R.id.text_post:
                if (true){
                    text_upload.setText("Uploading");
                    text_upload.setCompoundDrawablesWithIntrinsicBounds( (R.drawable.ic_hourglass_empty_black_24dp),0,0,0);
                    text_upload.setVisibility(View.VISIBLE);
//                    ic_hourglass_empty_black_24dp
                    FirebaseDatabase.getInstance().getReference().child("users")
                            .child(FirebaseAuth.getInstance().getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                user = dataSnapshot.getValue(User.class);
                                //Toast.makeText(getActivity().getApplicationContext(), ""+user.getName(), Toast.LENGTH_SHORT).show();
                                data.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                group.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                //data.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                if (!edit_details.getText().toString().isEmpty()){
                                    data.setDetail(edit_details.getText().toString());
                                    group.put("detail",edit_details.getText().toString());

                                }
                                SimpleDateFormat sdfff = new SimpleDateFormat("HH:mm:ss EEE MMM dd yyyy");
                                String currentDateandTime = sdfff.format(new Date());
                                String date = Calendar.getInstance().getTime().toString();
                                data.setDate(date);
                                group.put("date",currentDateandTime);
                                group.put("userimage",user.getProfilePicUrl());
                                group.put("username",user.getName());
                                if (group.get("type")==null)
                                    group.put("type","text");

                                if (group.get("type").equals("image")){
                                    uploadfile();
                                }
                                else if (group.get("type").equals("video")){
                                    uploadVideo();
                                }else {
                                    if (!edit_details.getText().toString().isEmpty()){

                                        data.setDetail(edit_details.getText().toString());
                                        group.put("detail",edit_details.getText().toString());
                                        onAuthSuccess(edit_details.getText().toString());
                                        return;
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            text_upload.setText("Failed to upload");
                            text_upload.setCompoundDrawablesWithIntrinsicBounds( (R.drawable.ic_thumb_down_black_24dp),0,0,0);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    text_upload.setVisibility(View.GONE);
                                }
                            },3000);
                            Toast.makeText(getActivity().getApplicationContext(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                break;
            default:
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode==1) {
            filename = data.getData().getPath();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            tempUri = getImageUri(getActivity().getApplicationContext(), bitmap);
        }else if (resultCode == RESULT_OK && requestCode==2){
            tempUri = data.getData();
            filename = data.getData().getPath();
            getRealPathFromURI(getActivity().getApplicationContext(),tempUri);
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
        String name ="images/"+datename+".jpg";
        group.put("filename",name);
        final StorageReference ref = storageRef.child(name);
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
                    onAuthSuccess( downloadUri.toString());
                } else {
                    Toast.makeText( getActivity().getApplicationContext(), "failure"+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }


        });
    }
    private void onAuthSuccess( String toString) {
        edit_details.setText("");
        data.setLink(toString);
        group.put("link",toString);
        group.put("timestamp", String.valueOf(Calendar.getInstance().getTimeInMillis()));
        String key = FirebaseDatabase.getInstance().getReference().child("posts").child(FirebaseAuth.getInstance().getUid()).push().getKey();
        group.put("key",key);
        FirebaseDatabase.getInstance().getReference().child("posts").child(FirebaseAuth.getInstance().getUid()).child(key).setValue(group).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    text_upload.setCompoundDrawablesWithIntrinsicBounds( (R.drawable.ic_thumb_up_black_24dp),0,0,0);
                    text_upload.setText("Uploaded successfully");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            text_upload.setVisibility(View.GONE);
                            group = new HashMap<>();
                        }
                    },3000);
                    //Toast.makeText(getActivity().getApplicationContext(), "YUp", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        Toast.makeText(getActivity().getApplicationContext(), "Come to papa", Toast.LENGTH_SHORT).show();
    }
    public void uploadVideo(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String datename = String.valueOf(Calendar.getInstance().getTimeInMillis());
        String abc = "abcdefghijklmnopqrstvuwxyz123456789";
        for (int i=0;i<5;i++){
            final int random = new Random().nextInt(35);
            datename+=abc.charAt(random);
        }
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("video/mp4")
                .build();
        String name = "images/"+datename+".mp4";
        group.put("filename",name);
        final StorageReference ref = storageRef.child("images/"+datename+".mp4");
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
                    onAuthSuccess( downloadUri.toString());
                } else {
                    Toast.makeText( getActivity().getApplicationContext(), "failure"+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }


        });
    }
    private void loadAllData() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
        FirebaseDatabase.getInstance().getReference().child("posts").child(FirebaseAuth.getInstance().getUid())

                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()){
                    ngoData ngodata = dataSnapshot.getValue(ngoData.class);
                    //Toast.makeText(getActivity().getApplicationContext(), ""+ngodata.getUid(), Toast.LENGTH_SHORT).show();
                    if (ngodata!=null)
                        arrayList.add(ngodata);
                }
                //Collections.reverse(arrayList);
                postAdapter = new PostAdapter(getActivity(),arrayList);
                listview.setAdapter(postAdapter);
                justifyListViewHeightBasedOnChildren(listview);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    {
                        ngoData ngodata = dataSnapshot.getValue(ngoData.class);
                        if (ngodata!=null)
                            if (arrayList.contains(ngodata))
                                arrayList.remove(ngodata);
                    }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });
    }
    public static void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
/*private void loadAllData() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
        FirebaseDatabase.getInstance().getReference().child("posts").child(FirebaseAuth.getInstance().getUid())

                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()){
                    ngoData ngodata = dataSnapshot.getValue(ngoData.class);
                    //Toast.makeText(getActivity().getApplicationContext(), ""+ngodata.getUid(), Toast.LENGTH_SHORT).show();
                    if (ngodata!=null)
                        arrayList.add(ngodata);
                }
                //Collections.reverse(arrayList);
                postAdapter = new PostAdapter(getActivity(),arrayList);
                listview.setAdapter(postAdapter);
                justifyListViewHeightBasedOnChildren(listview);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    {
                        ngoData ngodata = dataSnapshot.getValue(ngoData.class);
                        if (ngodata!=null)
                            if (arrayList.contains(ngodata))
                                arrayList.remove(ngodata);
                    }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });
    }*/