package com.example.ssquar2chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextInputLayout reset_input_email;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Toolbar reset_toolbar = findViewById(R.id.reset_toolbar);
        setSupportActionBar(reset_toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'> Reset Password </font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reset_input_email = findViewById(R.id.reset_input_email);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void resetPassword(View v){

        String resetEmail= reset_input_email.getEditText().getText().toString().trim();
        if (TextUtils.isEmpty(resetEmail)){
            reset_input_email.setError("Field cant be Empty");
            return;
        }else {
            firebaseAuth.sendPasswordResetEmail(resetEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ResetPasswordActivity.this, "Please Check You're Email", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ResetPasswordActivity.this, MainActivity.class));
                    }else {
                        String error = task.getException().getMessage();
                        Toast.makeText(ResetPasswordActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
