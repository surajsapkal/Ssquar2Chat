package com.example.ssquar2chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private TextInputLayout textInputEmail,textInputPassword;
    private TextView txtv;
    private TextView txt_reset;
    private    String strng="Register New Account";
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onStart() {
        super.onStart();

        //one time login.
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            startActivity(new Intent(MainActivity.this,StartActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textInputEmail = findViewById(R.id.text_input_email);
        textInputPassword = findViewById(R.id.text_input_password);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        txtv =findViewById(R.id.textView);
        txtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),strng,Toast.LENGTH_LONG).show();

                Intent it =new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(it);
            }
        });

        txt_reset = findViewById(R.id.txt_reset);
        txt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Reset Password", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    public void confirmInput(View v){

        String emailInput= textInputEmail.getEditText().getText().toString().trim();
        String passwordInput= textInputPassword.getEditText().getText().toString().trim();


        if (TextUtils.isEmpty(emailInput) || TextUtils.isEmpty(passwordInput) || passwordInput.length()<6){
            if (TextUtils.isEmpty(emailInput)){
                textInputEmail.setError("Field cant be Empty");
                return;
            }

            if (TextUtils.isEmpty(passwordInput)){
                textInputPassword.setError("Field cant be Empty");
                return;
            }else if (passwordInput.length()<6){
                textInputPassword.setError("Enter minimum 6 character");
                return;
            }
        }else {
            login(emailInput, passwordInput);
            progressDialog.setMessage("Processing....");
            progressDialog.show();
        }

    }

    public void login(String emailInput, String passwordInput){

        firebaseAuth.signInWithEmailAndPassword(emailInput, passwordInput)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(MainActivity.this,StartActivity.class);
                            startActivity(intent);
                            progressDialog.dismiss();


                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this,"User not valid",Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }

}
