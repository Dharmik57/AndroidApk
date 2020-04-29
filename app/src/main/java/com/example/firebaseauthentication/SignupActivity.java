package com.example.firebaseauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firebaseauthentication.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    private EditText inputusername,inputEmail, inputPassword,usergetname,usergetemail;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private ArrayList<String> mKeys = new ArrayList<>();
    private ArrayList<User> mMemos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignIn =  findViewById(R.id.sign_in_button);
        btnSignUp =  findViewById(R.id.sign_up_button);
        inputEmail =  findViewById(R.id.email);
        inputPassword =  findViewById(R.id.password);
        progressBar =  findViewById(R.id.progressBar);
        btnResetPassword =  findViewById(R.id.btn_reset_password);
        inputusername=findViewById(R.id.username);
        usergetname=(EditText)findViewById(R.id.userprofilename);
        usergetemail=(EditText)findViewById(R.id.userprofileemail);
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String userName=inputusername.getText().toString().trim();
                final String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }


                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "onComplete: called");
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
//                                    Toast.makeText(SignupActivity.this, "Registration failed! " + "\n" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(), Toast.LENGTH_SHORT).show();
                                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        Toast.makeText(SignupActivity.this, "User with this email already exist.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    boolean success=AddDatabase(userName,email,password);
                                    if(success==true)
                                    {
//                                        getprofiledata();
                                        Toast.makeText(SignupActivity.this, "Successfully registered!", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                    }
                                    else
                                    {
                                        Toast.makeText(SignupActivity.this, "Registration failed! Error Occured", Toast.LENGTH_LONG).show();
                                    }

                                    finish();
                                }
                            }
                        });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

protected  boolean  AddDatabase(String userName,String email,String password)
{
    try {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        //FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
      //  DatabaseReference mDb = mDatabase.getReference();
        FirebaseUser userget = firebaseAuth.getCurrentUser();
        String userKey = userget.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        User obj = new User();
        HashMap<String, String> dataMap = obj.fireebaseMap();
        dataMap.put("UserId",userKey);
        dataMap.put("UserName", userName);
        dataMap.put("Email", email);
        dataMap.put("Password", password);
        mDatabase.child("RegisterUser").push().setValue(dataMap);
        return  true;
    }
    catch (Exception ex)
    {
        Toast.makeText(SignupActivity.this, "Error Occured." + ex.toString(),Toast.LENGTH_SHORT).show();
        return false;
      //  throw  ex;
    }
}


}