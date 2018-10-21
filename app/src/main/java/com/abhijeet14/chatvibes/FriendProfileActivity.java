package com.abhijeet14.chatvibes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class FriendProfileActivity extends AppCompatActivity {
    private TextView nameText,emailText;
    private EditText aboutText;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private CircleImageView dp;
    private StorageReference dpRef;
    private String uid,myUid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nameText = findViewById(R.id.profile_name);
        emailText = findViewById(R.id.profile_email);
        aboutText = findViewById(R.id.profile_about);
        dp=findViewById(R.id.dp);
        dpRef= FirebaseStorage.getInstance().getReference().child("dp");
        mAuth = FirebaseAuth.getInstance();
        uid = getIntent().getExtras().getString("frienduid");
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        userRef.keepSynced(true);
        userRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("name").getValue(String.class);
                String userEmail = dataSnapshot.child("email").getValue(String.class);
                String url=dataSnapshot.child("dp").getValue(String.class);
                Picasso.with(FriendProfileActivity.this).load(url).into(dp);
                nameText.setText("Name : "+userName);
                emailText.setText("Email :"+userEmail);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void upload(View view) {
        CropImage.activity()
                .setAspectRatio(1,1)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                CropImage.ActivityResult result=CropImage.getActivityResult(data);
                Uri resultUri=result.getUri();
                File f=new File(resultUri.getPath());
                Bitmap b =null;
                try {
                    b=new Compressor(this)
                            .setMaxHeight(100)
                            .setMaxWidth(100)
                            .setQuality(10)
                            .compressToBitmap(f);
                }catch (IOException io){
                    Toast.makeText(this, io.getMessage(), Toast.LENGTH_SHORT).show();
                }
                ByteArrayOutputStream bos=new ByteArrayOutputStream();
                assert b != null;
                b.compress(Bitmap.CompressFormat.JPEG,100,bos);
                byte[] bytes=bos.toByteArray();
                dpRef.child(uid+".jpg").putBytes(bytes).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            String downloadUrl=task.getResult().getDownloadUrl().toString();
                            userRef.child(uid).child("dp").setValue(downloadUrl).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(FriendProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(FriendProfileActivity.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Toast.makeText(FriendProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(FriendProfileActivity.this,AllChatActivity.class));
    }
}
