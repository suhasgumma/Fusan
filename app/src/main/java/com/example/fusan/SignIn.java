package com.example.fusan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class SignIn extends AppCompatActivity {

    public static boolean validEmail(String emailId){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (emailId == null)
            return false;
        return pat.matcher(emailId).matches();
    }

    EditText username, password;
    TextView signInUp;
    Button login;
    ProgressBar progressBar;
    RelativeLayout relativeLayout;
    boolean email, pwd;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mauthlistner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        username = findViewById(R.id.unamelogin);
        password = findViewById(R.id.pswdlogin);
        login = findViewById(R.id.signInLogin);
        signInUp = findViewById(R.id.signinupLogin);
        relativeLayout= findViewById(R.id.rlayout);
        progressBar = findViewById(R.id.signupeprogress);

        mAuth = FirebaseAuth.getInstance();

        login.getBackground().setAlpha(98);


        mauthlistner=new FirebaseAuth.AuthStateListener() {
            FirebaseUser mfirebaseuser = mAuth.getCurrentUser();

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (mfirebaseuser != null) {
                    Toast.makeText(SignIn.this,"You are logged in",Toast.LENGTH_SHORT).show();
                    Intent i =new Intent(SignIn.this,Dashboard.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(SignIn.this,"Please login",Toast.LENGTH_SHORT).show();
                }
            }
        };

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()!=0){
                    email = true;
                    if(pwd){
                        login.setEnabled(true);
                        login.getBackground().setAlpha(255);
                    }
                } else {
                    email = false;
                    login.setEnabled(false);
                    login.getBackground().setAlpha(98);
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
                        login.setEnabled(true);
                        login.getBackground().setAlpha(255);
                    }
                } else {
                    pwd = false;
                    login.setEnabled(false);
                    login.getBackground().setAlpha(98);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login.setVisibility(View.INVISIBLE);
                relativeLayout.setBackgroundColor(Color.parseColor("#2f95f6"));
                progressBar.setVisibility(View.VISIBLE);

                String emailId = username.getText().toString();
                String passwordS = password.getText().toString();

                if(emailId.isEmpty()){
                    username.setError("please enter email id");
                    username.requestFocus();
                }
                else if(!validEmail(emailId)){
                    login.setVisibility(View.VISIBLE);
                    relativeLayout.setBackgroundColor(0x00000000);
                    progressBar.setVisibility(View.INVISIBLE);
                    username.setError("Invalid Email Id");
                    username.requestFocus();
                }
                else if(passwordS.isEmpty()){
                    password.setError("please enter Password");
                    password.requestFocus();
                }
                else{
                    mAuth.signInWithEmailAndPassword(emailId, passwordS).addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                login.setVisibility(View.VISIBLE);
                                relativeLayout.setBackgroundColor(0x00000000);
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(SignIn.this, "Login Failed. Check Your credentials and try again.",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent in = new Intent(SignIn.this,Dashboard.class);
                                startActivity(in);
                                finish();
                            }
                        }
                    });
                }

            }
        });


        signInUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });
    }

}