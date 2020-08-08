package com.example.ssquar2chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout textInputUsername,textInputPassword,textInputMail;
    private FirebaseAuth firebaseAuth;
    private TextView text;
    private DatabaseReference reference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'> Register </font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();

        textInputUsername=findViewById(R.id.text_input_username);
        textInputPassword=findViewById(R.id.text_password);
        textInputMail=findViewById(R.id.text_email);

        progressDialog = new ProgressDialog(this);

        text = findViewById(R.id.text_view);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterActivity.this,"Login",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
            }
        });
    }

    public void buttonInput(View view){

        String usernameInput = textInputUsername.getEditText().getText().toString().trim();
        String email = textInputMail.getEditText().getText().toString().trim();
        String password= textInputPassword.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(usernameInput) || usernameInput.length()>20 || TextUtils.isEmpty(password) || password.length()<6){
            if (TextUtils.isEmpty(usernameInput)){
                textInputUsername.setError("Field cant be Empty");
                return;
            }else if (usernameInput.length()>20){
                textInputUsername.setError("UserName Too Long");
                return;
            }
            if (TextUtils.isEmpty(email)){
                textInputMail.setError("Field cant be empty");
                return;
            }

            if (TextUtils.isEmpty(password)){
                textInputPassword.setError("Field cant be empty");
                return;
            }else if (password.length()<6){
                textInputPassword.setError("Enter minimum 6 character");
                return;

            }
        }else {
            register(usernameInput,email,password);
            progressDialog.setMessage("Processing....");
            progressDialog.show();
        }
    }

    public void register(final String username, String emailInput, String passwordInput){
        firebaseAuth.createUserWithEmailAndPassword(emailInput, passwordInput)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");
                            hashMap.put("status", "offline");


                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent myintent = new Intent(RegisterActivity.this,StartActivity.class);
                                        startActivity(myintent);
                                        progressDialog.dismiss();
                                        Toast.makeText(RegisterActivity.this,"Register Successfully",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this,"Registration Failed",Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

}
