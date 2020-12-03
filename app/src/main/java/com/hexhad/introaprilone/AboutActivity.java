package com.hexhad.introaprilone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AboutActivity extends AppCompatActivity {

    Button home,linked_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        home = findViewById(R.id.btn_home);


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move_to_home = new Intent(AboutActivity.this,HomeActivity.class);
                move_to_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                move_to_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(move_to_home);
                finish();
            }
        });



        linked_in = findViewById(R.id.btn_linked_in);
        linked_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            clicked_btn("https://www.linkedin.com/in/hashandharmapriya/");

            }
        });
    }

    private void clicked_btn(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
