package com.abhijeet14.chatvibes;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
private EditText nameText,emailText,passText;
private FirebaseAuth mAuth;
private DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        nameText = findViewById(R.id.signup_name);
        emailText = findViewById(R.id.signup_email);
        passText = findViewById(R.id.signup_password);
        mAuth = FirebaseAuth.getInstance();
       // mAuth.getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
    }

    public void doRegister(View view) {
        final ProgressDialog p = new ProgressDialog(this);
        p.setMessage("Please Wait Registration is on progress...");
        p.setTitle("Hold On ... ");
        p.setCancelable(false);
        p.setCanceledOnTouchOutside(false);
        final String email = emailText.getText().toString();
        final String name = nameText.getText().toString();
        String pass = passText.getText().toString();
        if(TextUtils.isEmpty(email)|| TextUtils.isEmpty(name) || TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Fields cannot be left Blank", Toast.LENGTH_SHORT).show();
        }else{
            p.show();
            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    p.dismiss();
                    if(task.isSuccessful()){
                        String uid = task.getResult().getUser().getUid();
                        /*userRef.child(uid).child("name").setValue(name);
                        userRef.child(uid).child("email").setValue(email);*/
                        Map<String,Object> m = new HashMap<>();
                        m.put("name",name);
                        m.put("email",email);
                        m.put("dp","nothing");
                        userRef.child(uid).updateChildren(m).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(SignupActivity.this, "Successfully registered ..", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
