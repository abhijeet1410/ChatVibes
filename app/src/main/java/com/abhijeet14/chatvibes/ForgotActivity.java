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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {
private EditText emailText;
private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        emailText = findViewById(R.id.forgot_email);
        mAuth = FirebaseAuth.getInstance();
    }
    public void doResetPassword(View view) {
        final ProgressDialog p = new ProgressDialog(this);
        String email = emailText.getText().toString();
        p.setMessage("Please Wait while we are Verifying");
        p.setTitle("Hold On ... ");
        p.setCancelable(false);
        p.setCanceledOnTouchOutside(false);
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email Field Cannot be left blank ..", Toast.LENGTH_SHORT).show();
        }else{
            p.show();
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ForgotActivity.this, "Email Sent Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        p.dismiss();
                        Toast.makeText(ForgotActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
