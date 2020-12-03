package com.hexhad.introaprilone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private OnboardingAdapter onboardingAdapter;
    private LinearLayout layoutOnboardingIndicators;
    private FloatingActionButton nextbutton;
    private DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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



        layoutOnboardingIndicators = findViewById(R.id.layoutOnboardingIndicators);
        nextbutton = findViewById(R.id.buttonOnboardingAction);


        setupOnboardingItem();
        final ViewPager2 onboardingViewPG = findViewById(R.id.onboardingViewPager);
        onboardingViewPG.setAdapter(onboardingAdapter);

        setLayoutOnboardingIndicators();

        setCurrentOnboardIndicator(0);

        onboardingViewPG.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardIndicator(position);
            }
        });

        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onboardingViewPG.getCurrentItem() + 1 < onboardingAdapter.getItemCount()){
                    onboardingViewPG.setCurrentItem(onboardingViewPG.getCurrentItem()+1);
                }else{
                    startActivity(new Intent(getApplicationContext(),LogInActivity.class));
                    finish();
                }
            }
        });
    }
    private void setupOnboardingItem(){
        List<OnboardingItem> onboardingItems = new ArrayList<>();

        OnboardingItem hash = new OnboardingItem();
        hash.setTitle("Frozen Ice Cream Kottu");
        hash.setDesc("Welcome to the Largest Ice Cream Kotthu Collection");
        hash.setImage(R.drawable.intro_frozen);

        OnboardingItem hash2 = new OnboardingItem();
        hash2.setTitle("Online Orders");
        hash2.setDesc("Find a good time range and place your order");
        hash2.setImage(R.drawable.intro_calander);

        OnboardingItem hash3 = new OnboardingItem();
        hash3.setTitle("Gifts");
        hash3.setDesc("Manage your profile properly and win gifts, discounts and more cools things");
        hash3.setImage(R.drawable.intro_gft);

        onboardingItems.add(hash);
        onboardingItems.add(hash2);
        onboardingItems.add(hash3);

        onboardingAdapter = new OnboardingAdapter(onboardingItems);


    }

    private void setLayoutOnboardingIndicators(){
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);
        for (int i = 0 ; i < indicators.length ; i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.onboarding_indicator_inactiv
            ));
            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardingIndicators.addView(indicators[i]);
        }
    }
    private void setCurrentOnboardIndicator(int index){
        int childcount = layoutOnboardingIndicators.getChildCount();
        for (int i = 0 ; i < childcount; i++ ){
            ImageView imageView = (ImageView) layoutOnboardingIndicators.getChildAt(i);
            if (i == index){
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_active)
                );
            }else{
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_inactiv)
                );
            }
        }
        if (index == onboardingAdapter.getItemCount() - 1 ){
            nextbutton.setImageDrawable(getDrawable(R.drawable.ic_done));
        } else {
            nextbutton.setImageDrawable(getDrawable(R.drawable.ic_next));
        }
    }

    public boolean isCon(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo aNetwork = manager.getActiveNetworkInfo();

        return aNetwork != null && aNetwork.isConnected();
    }
}
