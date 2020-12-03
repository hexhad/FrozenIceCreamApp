package com.hexhad.introaprilone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FeedbackActivity extends AppCompatActivity {

    EditText feed_back;
    Button fbk_submit,btn_home;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        feed_back = findViewById(R.id.feed_back);
        fbk_submit = findViewById(R.id.fbk_submit);
        btn_home = findViewById(R.id.btn_home);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Feedback");

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move_to_home = new Intent(FeedbackActivity.this,HomeActivity.class);
                move_to_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                move_to_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(move_to_home);
                finish();
            }
        });

        fbk_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feed = feed_back.getText().toString().trim();

                //calender date
                Calendar calendar = Calendar.getInstance();
                String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

                //cal time
                SimpleDateFormat format = new SimpleDateFormat("HH:MM:SS");
                String time = format.format(calendar.getTime());

                //get_uid
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String unique_id = user.getUid();

                //write on fire
                DatabaseReference current_user_db = mDatabase.child(unique_id);
                current_user_db.child(currentDate+":"+time).setValue(feed);

                feed_back.setText(null);
            }
        });
    }
}
