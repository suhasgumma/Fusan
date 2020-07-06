package com.example.fusan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    EditText username, password, usernameR, fName, lName, rePassword;
    Button signUp;
    TextView signin;
    ProgressBar progressBar;
    boolean email, pwd;
    FirebaseDatabase database;
    DatabaseReference firebaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mauthlistner;

    public static boolean validEmail(String emailId){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (emailId == null)
            return false;
        return pat.matcher(emailId).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.uname);
        usernameR = findViewById(R.id.username);
        fName = findViewById(R.id.fname);
        password = findViewById(R.id.pswd);
        signUp = findViewById(R.id.signUp);
        signin = findViewById(R.id.signinup);
        progressBar = findViewById(R.id.signupeprogress);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        firebaseReference = database.getReference("users");

        signUp.getBackground().setAlpha(98);

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()!=0){
                    email = true;
                    if(pwd){
                        signUp.setEnabled(true);
                        signUp.getBackground().setAlpha(255);
                    }
                } else {
                    email = false;
                    signUp.setEnabled(false);
                    signUp.getBackground().setAlpha(98);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()!=0){
                    pwd = true;
                    if(email){
                        signUp.setEnabled(true);
                        signUp.getBackground().setAlpha(255);
                    }
                } else {
                    pwd = false;
                    signUp.setEnabled(false);
                    signUp.getBackground().setAlpha(98);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                signUp.setVisibility(View.INVISIBLE);
                final String emailId = username.getText().toString();
                String passwordS = password.getText().toString();
                final String usernameS = usernameR.getText().toString();
                final String firstName = fName.getText().toString();

                if(emailId.isEmpty()){
                    progressBar.setVisibility(View.INVISIBLE);
                    signUp.setVisibility(View.VISIBLE);
                    username.setError("please enter email id");
                    username.requestFocus();
                }
                else if(!validEmail(emailId)){
                    progressBar.setVisibility(View.INVISIBLE);
                    signUp.setVisibility(View.VISIBLE);
                    username.setError("Invalid Email Id");
                    username.requestFocus();
                }
                else if(passwordS.isEmpty()){
                    progressBar.setVisibility(View.INVISIBLE);
                    signUp.setVisibility(View.VISIBLE);
                    password.setError("please enter Password");
                    password.requestFocus();
                }
                else{
                    mAuth.createUserWithEmailAndPassword(emailId, passwordS).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                progressBar.setVisibility(View.INVISIBLE);
                                signUp.setVisibility(View.VISIBLE);

                                Toast.makeText(SignUp.this, "Registration unsuccessful.",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                progressBar.setVisibility(View.VISIBLE);
                                signUp.setVisibility(View.INVISIBLE);
                                firebaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Username").setValue(usernameS);
                                firebaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Name").setValue(firstName);
                                firebaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Email Id").setValue(emailId);
                                Toast.makeText(SignUp.this, "Registration successful.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUp.this, SignIn.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}