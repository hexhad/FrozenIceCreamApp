package com.hexhad.introaprilone;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LogInActivity extends AppCompatActivity implements CustomRecoverEmail.SendRecoverSt {

    ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private TextInputLayout email_link, pwd;
    private TextView reg;
    private Button login_btn,forget;
    private DatabaseReference reff;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        if (Build.VERSION.SDK_INT >= 21) {
            //getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.hashOne));
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.hashTwo));
        }

        email_link = findViewById(R.id.email);
        pwd = findViewById(R.id.pwd);

        mAuth = FirebaseAuth.getInstance();

        reg = findViewById(R.id.want_to_reg);
        login_btn = findViewById(R.id.login_btn);
        forget = findViewById(R.id.forget);

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomRecoverEmail customRecoverEmail = new CustomRecoverEmail();
                customRecoverEmail.show(getFragmentManager(), "CustomDItem");
            }
        });


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEmail() | !validatePassword()){
                    return;
                } else {
                    mProgress = new ProgressDialog(LogInActivity.this);
                    mProgress.setTitle("Login");
                    mProgress.setMessage("Processing");
                    mProgress.show();
                    String email = Objects.requireNonNull(email_link.getEditText()).getText().toString();
                    String password = Objects.requireNonNull(pwd.getEditText()).getText().toString();
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()) {
                                    mProgress.dismiss();

                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    String unique_id = user.getUid();

                                    reff = FirebaseDatabase.getInstance().getReference().child("Users").child(unique_id);
                                    reff.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String profileDB = dataSnapshot.child("profile").getValue().toString();

                                            if (profileDB.equals("not_finished")) {

                                                //get from db
                                                String nameDB = dataSnapshot.child("name").getValue().toString();
                                                String mobileDB = dataSnapshot.child("phone").getValue().toString();

                                                //not_finished
                                                Intent nintent = new Intent(LogInActivity.this, ProfileActivity.class);
                                                nintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                nintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                                nintent.putExtra("name",nameDB);
                                                nintent.putExtra("phone",mobileDB);
                                                startActivity(nintent);
                                            } else {
                                                //finished
                                                Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                } else {
                                    mAuth.signOut();
                                    mProgress.dismiss();
                                    msgPopup("Oops..", "Please verify your Email");
                                }
                            }
                        }
                    });
                }
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, RegActivity.class);

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View,String>(email_link,"tra_email");
                pairs[1] = new Pair<View,String>(pwd,"tra_pwd");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LogInActivity.this,pairs);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent,options.toBundle());
            }
        });
    }

    private Boolean validateEmail() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String val = email_link.getEditText().getText().toString();
        if (val.isEmpty()) {
            email_link.setError("Field cannot be Empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            email_link.setError("Invalid Email");
            return false;
        } else {
            email_link.setError(null);
            email_link.setErrorEnabled(false);
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
        String val = pwd.getEditText().getText().toString();

        if (val.isEmpty() ) {
            pwd.setError("Check Password Fields");
            pwd.setCounterEnabled(true);
            return false;
        } else if (!val.matches(passwordVal)) {
            pwd.setError("Cannot accept");
            pwd.setCounterEnabled(true);
            return false;
        } else {
            pwd.setError(null);
            pwd.setErrorEnabled(false);
            pwd.setHelperText("Alright");
            pwd.setCounterEnabled(false);
            return true;
        }
    }

    public void msgPopup(String title, String msg) {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(LogInActivity.this);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(msg);
        dialogBuilder.show();
    }

    @Override
    public void sendRec(String s) {

    }
}
