package com.hexhad.introaprilone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class RegActivity extends AppCompatActivity {

    Button reg;
    Button login;
    EditText name_test;
    private TextInputLayout name_field, email_field, pwd_field, phone;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase,mDatabase2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        //testing


        //Status Bar
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.hashTwo));
        }

        //Edit T Linker
        name_field = findViewById(R.id.enter_name);
        email_field = findViewById(R.id.enter_email);
        pwd_field = findViewById(R.id.enter_pwd_one);
        phone = findViewById(R.id.mob_num);
        name_test = findViewById(R.id.name_test);


        //Instance
        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);

        //database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Count");

        //Btn Linker
        reg = findViewById(R.id.btn_register);
        login = findViewById(R.id.btn_login);

        //Login Up Linker
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(getApplicationContext(), LogInActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
                finish();
            }
        });

        //Sign In Linker
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();

            }
        });

    }



    private Boolean validateName() {
        String val = name_field.getEditText().getText().toString();
        String noWhiteSpaces = "\\A\\w{4,20}\\z";
        if (val.isEmpty()) {
            name_field.setError("Field cannot be Empty");
            return false;
        } else if (!val.matches(noWhiteSpaces)) {
            name_field.setError("white spaces are not allowed");
            return false;
        } else {
            name_field.setError(null);
            name_field.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String val = email_field.getEditText().getText().toString();
        if (val.isEmpty()) {
            email_field.setError("Field cannot be Empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            email_field.setError("Invalid Email");
            return false;
        } else {
            email_field.setError(null);
            email_field.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{6,}" +               //at least 6 characters
                "$";
        String val = pwd_field.getEditText().getText().toString();

        if (val.isEmpty() ) {
            pwd_field.setError("Check Password Fields");
            pwd_field.setCounterEnabled(true);
            return false;
        } else if (!val.matches(passwordVal)) {
            pwd_field.setError("Password is too weak");
            pwd_field.setCounterEnabled(true);
            return false;
        } else {
            pwd_field.setError(null);
            pwd_field.setErrorEnabled(false);
            pwd_field.setHelperText("Good");
            pwd_field.setCounterEnabled(false);
            return true;
        }
    }


    private void startRegister() {
        final String name = name_field.getEditText().getText().toString().trim();
        final String email = email_field.getEditText().getText().toString().trim();
        final String password_one = pwd_field.getEditText().getText().toString().trim();
        final String mobile = phone.getEditText().getText().toString().trim();

        if (!validateEmail() | !validateName() | !validatePassword()) {
            return;
        } else {
            //if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password_one) && !TextUtils.isEmpty(password_two) && (password_one.equals(password_two))) {

            mProgress = new ProgressDialog(RegActivity.this);
            mProgress.setTitle("Sign Up");
            mProgress.setMessage("Please check your email to verify the address");
            mProgress.show();


            mAuth.createUserWithEmailAndPassword(email, password_one).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    String user_id = mAuth.getCurrentUser().getUid();
                                    DatabaseReference current_user_db = mDatabase.child(user_id);

                                    current_user_db.child("profile").setValue("not_finished");
                                    current_user_db.child("name").setValue(name);
                                    current_user_db.child("Email").setValue(email);
                                    current_user_db.child("phone").setValue(mobile);
                                    current_user_db.child("Password").setValue(password_one);
                                    current_user_db.child("level").setValue("BASIC");
                                    current_user_db.child("PayCount").setValue(0);
                                    current_user_db.child("level2").setValue("locked");
                                    current_user_db.child("level3").setValue("locked");
                                    current_user_db.child("level_pres").setValue(10);
                                    current_user_db.child("cred_name").setValue("null");
                                    current_user_db.child("cred_date").setValue("null");
                                    current_user_db.child("cred_pin").setValue("null");
                                    current_user_db.child("profile_pic").setValue("");
                                    current_user_db.child("order").setValue("");
                                    current_user_db.child("resetLoyal").setValue(3);

                                    

                                    mProgress.dismiss();
                                    mAuth.signOut();

                                    Intent Login = new Intent(getApplicationContext(), LogInActivity.class);
                                    Login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(Login);
                                } else {
                                    mProgress.dismiss();
                                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(RegActivity.this);
                                    dialogBuilder.setTitle("Oops..");
                                    dialogBuilder.setMessage("Error");
                                    dialogBuilder.show();
                                }
                            }
                        });

                    } else {
                        mProgress.dismiss();
                        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(RegActivity.this);
                        dialogBuilder.setTitle("Oops..");
                        dialogBuilder.setMessage("Incorrect Email or Email already in use");
                        dialogBuilder.show();
                    }
                }
            });

        }

    }

}
