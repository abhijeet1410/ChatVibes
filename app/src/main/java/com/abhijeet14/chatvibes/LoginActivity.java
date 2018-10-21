package com.abhijeet14.chatvibes;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
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

public class LoginActivity extends AppCompatActivity {
private EditText emailText,passwordText;
private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailText = findViewById(R.id.login_email);
        passwordText = findViewById(R.id.login_password);
        mAuth = FirebaseAuth.getInstance();
    }
    public void doLogin(View view) {
        final ProgressDialog p = new ProgressDialog(this);
        p.setMessage("Please Wait while we are logging you in");
        p.setTitle("Hold On ... ");
        p.setCancelable(false);
        p.setCanceledOnTouchOutside(false);
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this, "Fields Cannot be left blank", Toast.LENGTH_SHORT).show();
        }else{
            p.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    p.dismiss();
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Successful..", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this,AllChatActivity.class));
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
       /* if(connectivityManager.getActiveNetworkInfo().getState() == NetworkInfo.State.DISCONNECTED || connectivityManager.getActiveNetworkInfo().getState()!=null) {
                Toast.makeText(this, "Check your Internet Connectivity", Toast.LENGTH_SHORT).show();
               AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Network Connection Failed !")
                        .setCancelable(false)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                builder.show();
        }else{
            if(mAuth.getCurrentUser()!=null) {
            startActivity(new Intent(LoginActivity.this, AllChatActivity.class));
            finish();
            }
        }*/
        if(mAuth.getCurrentUser()!=null) {
            startActivity(new Intent(LoginActivity.this, AllChatActivity.class));
            finish();
        }
    }

    public void doForgotPassword(View view) {
        startActivity(new Intent(LoginActivity.this,ForgotActivity.class));
    }

    public void doSignup(View view) {
        startActivity(new Intent(LoginActivity.this,SignupActivity.class));
    }
}