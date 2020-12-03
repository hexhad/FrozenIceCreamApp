package com.hexhad.introaprilone;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.squareup.picasso.Picasso;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragmentFour#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragmentFour extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //-------------
    private LinearLayout button;
    private TextView email,uid,pres_mid,level,prof_name,prof_mobile,arch_three,arch_two,arch_one;

    RoundedImageView pro_pic;

    CircularProgressBar circularProgressBar;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference reff,reff1;


    public BlankFragmentFour() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragmentFour.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragmentFour newInstance(String param1, String param2) {
        BlankFragmentFour fragment = new BlankFragmentFour();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank_four, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        button = view.findViewById(R.id.btm_up);
        email = view.findViewById(R.id.email);
        uid = view.findViewById(R.id.uid);
        pres_mid = view.findViewById(R.id.pres_mid);
        level = view.findViewById(R.id.level);
        prof_mobile = view.findViewById(R.id.prof_mobile);
        prof_name = view.findViewById(R.id.prof_name);
        arch_one = view.findViewById(R.id.arch_one);
        arch_two = view.findViewById(R.id.arch_two);
        arch_three = view.findViewById(R.id.arch_three);
        pro_pic = view.findViewById(R.id.pro_pic);

        circularProgressBar = view.findViewById(R.id.circularProgressBar);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //----------------------------------------------------------------------------------------db_read
        final String unique_id = user.getUid();

        reff1 = FirebaseDatabase.getInstance().getReference().child("Count").child(unique_id);
        reff1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    int count = (int) dataSnapshot.getChildrenCount();

                    if (count == 1){
                        cirPresGen(10);
                        pres_mid.setText("10");
                    } else if (count == 2){
                        cirPresGen(20);
                        pres_mid.setText("20");
                    }
                    else if (count == 3){
                        cirPresGen(30);
                        pres_mid.setText("30");
                    }
                    else if (count == 4){
                        cirPresGen(40);
                        pres_mid.setText("40");
                    }
                    else if (count == 5){
                        cirPresGen(50);
                        pres_mid.setText("50");
                        arch_two.setText("Unlocked");
                    }
                    else if (count == 6){
                        cirPresGen(60);
                        pres_mid.setText("60");
                    }
                    else if (count == 7){
                        cirPresGen(70);
                        pres_mid.setText("70");
                    }
                    else if (count == 8){
                        cirPresGen(80);
                        pres_mid.setText("80");
                    }
                    else if (count == 9){
                        pres_mid.setText("90");
                        cirPresGen(90);
                    } else if (count > 9){
                        cirPresGen(100);
                        pres_mid.setText("100");
                        arch_three.setText("Unlocked");
                    } else {
                        pres_mid.setText("0");
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(unique_id);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                String nameDB = dataSnapshot.child("name").getValue().toString();
                String mobileDB = dataSnapshot.child("phone").getValue().toString();
                String levelDB =  dataSnapshot.child("level").getValue().toString();
                //String pres_midDB = dataSnapshot.child("level_pres").getValue().toString();
                String profile = dataSnapshot.child("profile").getValue().toString();
                String profile_pic = dataSnapshot.child("profile_pic").getValue().toString();


                prof_name.setText(nameDB);
                prof_mobile.setText(mobileDB);
                uid.setText(user.getUid());
                email.setText(user.getEmail());
                //level.setText(levelDB);

                try {
                    Picasso.get().load(profile_pic).into(pro_pic);
                }catch (Exception e){
                    Picasso.get().load(R.drawable.img_pro_pic).into(pro_pic);
                }



                if (profile.equals("not_finished")){
                    arch_one.setText("Please complete the Profile");
                } else if (profile.equals("finished")){

                    //pres_mid.setText("10");
                    arch_one.setText("Unlocked");

                } else {
                    arch_one.setText("Locked");
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //----------------------------------------------------------------------------------------db_read

        email.setText(user.getEmail());
        uid.setText(user.getUid());


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Objects.requireNonNull(getContext()), R.style.BottomSheet);
                Objects.requireNonNull(bottomSheetDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                View bottomSheet = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet, (LinearLayout) view.findViewById(R.id.bottomSheetCont));
                bottomSheet.findViewById(R.id.layoutOne).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent Login = new Intent(getContext(), ProfileActivity.class);
                        Login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(Login);
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheet.findViewById(R.id.layouttwo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent move_to_about = new Intent(getActivity(),AboutActivity.class);
                        startActivity(move_to_about);

                        //Toast.makeText(getContext(), "About", Toast.LENGTH_LONG).show();
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheet.findViewById(R.id.layoutthree).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent move_to_about = new Intent(getActivity(),FeedbackActivity.class);
                        startActivity(move_to_about);

                        //Toast.makeText(getContext(), "About", Toast.LENGTH_LONG).show();
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheet.findViewById(R.id.layoutfour).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent move_to_about = new Intent(getActivity(),MapsActivity.class);
                        startActivity(move_to_about);

                        //Toast.makeText(getContext(), "About", Toast.LENGTH_LONG).show();
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheet.findViewById(R.id.btn_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mAuth = FirebaseAuth.getInstance();
                        mAuth.signOut();
                        bottomSheetDialog.dismiss();
                        Intent loginIntent = new Intent(getContext(), LogInActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(loginIntent);
                        getActivity().finish();
                    }
                });

                bottomSheetDialog.setContentView(bottomSheet);
                bottomSheetDialog.show();
            }
        });


    }

    private void cirPresGen(float i) {
        if (i > 10)
            arch_two.setText("Unlocked");
        if (i > 50 && i < 90) {
            level.setText("SILVER");
        } else if (i > 80) {
            level.setText("GOLD");
        }
        circularProgressBar.setProgressDirection(CircularProgressBar.ProgressDirection.TO_LEFT);
        circularProgressBar.setStartAngle(0f);
        circularProgressBar.setProgressWithAnimation(i, (long) 1000);
    }


}
