package com.hexhad.introaprilone;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.text.DateFormat;

public class HomeActivity extends AppCompatActivity {

    ChipNavigationBar bottom_nav;
    FragmentManager fragmentManager;

    private DatabaseReference reff,reff5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (!isCon()){
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);

            builder.setIcon(R.drawable.ic_close_black_48dp);
            builder.setTitle("Internet Connection");
            builder.setCancelable(false);
            builder.setBackground(getResources().getDrawable(R.drawable.all_corner_24_white, null));
            builder.setMessage("Please Check your Internet Connection");
            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            builder.show();

        }

        reff = FirebaseDatabase.getInstance().getReference().child("Version").child("Ver");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String hashError = dataSnapshot.getValue().toString();

                if (hashError.equals("33")){} else {
                    Toast.makeText(getApplicationContext(),"You are not a Valid User",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reff5 = FirebaseDatabase.getInstance().getReference().child("Broadcast");
        reff5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String msg = dataSnapshot.child("msg").getValue().toString();
                    String st = dataSnapshot.child("state").getValue().toString();

                    if (st.equals("true")) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bottom_nav = findViewById(R.id.bottom_nav);

        if (savedInstanceState == null) {
            bottom_nav.setItemSelected(R.id.bottom_home, true);
            fragmentManager = getSupportFragmentManager();
            BlankFragmentOne blankFragmentOne = new BlankFragmentOne();
            fragmentManager.beginTransaction().replace(R.id.fragment_cont, blankFragmentOne).commit();
        }

        bottom_nav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                Fragment fragment = null;
                switch (id) {
                    case R.id.bottom_home:
                        fragment = new BlankFragmentOne();
                        break;
                    case R.id.bottom_search:
                        fragment = new BlankFragmentTwo();
                        break;
                    case R.id.bottom_cart:
                        fragment = new BlankFragmentThree();
                        break;
                    case R.id.bottom_profile:
                        fragment = new BlankFragmentFour();
                        break;
                }
                if (fragment != null) {
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_cont, fragment).commit();
                } else {
                    Log.e("HASH_ERROR", "FRAGMENT ERROR");
                }
            }
        });

    }


    public boolean isCon(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo aNetwork = manager.getActiveNetworkInfo();

        return aNetwork != null && aNetwork.isConnected();
    }

}
