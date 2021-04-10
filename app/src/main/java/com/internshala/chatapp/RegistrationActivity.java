package com.internshala.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {
EditText etEmail, etPassword;
Button btnRegister1;

//ProgressBar
    ProgressDialog processDialog;

    //Declaring instance of FirebaseAuth
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //Actionbar and its title
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Create Account");
        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //init
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        btnRegister1=findViewById(R.id.btnRegister1);

        mAuth=FirebaseAuth.getInstance();

        processDialog=new ProgressDialog(this);
        processDialog.setMessage("Registering User...");
        //handle register button
        btnRegister1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=etEmail.getText().toString().trim();
                String password=etPassword.getText().toString().trim();
                //validate
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    etEmail.setError("Invalid Email");
                    etEmail.setFocusable(true);
                }else if(password.length()<6)
                {
                    etPassword.setError("Password must contain at least 6 characters");
                    etPassword.setFocusable(true);
                }else{
                    registerUser(email, password);
                }
            }
        });
    }

    private void registerUser(String email, String password) {
        //First, declare an instance of FirebaseAuth
        processDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                         processDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegistrationActivity.this, "Registered...\n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(RegistrationActivity.this,ProfileActivity.this));
                        } else {
                            // If sign in fails, display a message to the user.
                           processDialog.dismiss();
                            Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //error, dismiss progress dialog and get and show the error message
                processDialog.dismiss();
                Toast.makeText(RegistrationActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}