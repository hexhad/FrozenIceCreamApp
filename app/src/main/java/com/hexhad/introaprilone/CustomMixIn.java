package com.hexhad.introaprilone;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomMixIn extends DialogFragment {

    //logt
    private static final String TAG = "CustomMixIn";
    public interface SendMixInCouple{
        void sendMixIn(String s);
    }

    public SendMixInCouple sendMixInCouple;

    private DatabaseReference reff;
    
    CheckBox Pineapple,Passion,Mango,Strawberry,Raisins,Oreo,Blueberry,Kitkat,Chocos,Banana,Cheese,Blackberry;
    private ArrayList<String> mixIns;
    private Button btn_mixin_add,btn_close_mixin;
    private TextView show_err;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.custompopup_mixin, container, false);

        mixIns = new ArrayList<>();

        Pineapple = view.findViewById(R.id.Pineapple);
        Passion = view.findViewById(R.id.Passion);
        Mango = view.findViewById(R.id.Mango);
        Strawberry = view.findViewById(R.id.Strawberry);
        Raisins = view.findViewById(R.id.Raisins);
        Oreo = view.findViewById(R.id.Oreo);
        Blueberry = view.findViewById(R.id.Blueberry);
        Kitkat = view.findViewById(R.id.Kitkat);
        Chocos = view.findViewById(R.id.Chocos);
        Banana = view.findViewById(R.id.Banana);
        Cheese = view.findViewById(R.id.Cheese);
        Blackberry = view.findViewById(R.id.Blackberry);
        show_err = view.findViewById(R.id.show_err);


        btn_mixin_add = view.findViewById(R.id.btn_mixin_add);
        btn_close_mixin = view.findViewById(R.id.btn_close_mixin);

        reff = FirebaseDatabase.getInstance().getReference().child("Stock");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String itm_3 = dataSnapshot.child("Pineapple").getValue().toString();
                    availableChecker(Pineapple, itm_3);
                    String itm_4 = dataSnapshot.child("Kitkat").getValue().toString();
                    availableChecker(Kitkat, itm_4);
                    String itm_2 = dataSnapshot.child("Blueberry").getValue().toString();
                    availableChecker(Blueberry, itm_2);
                    String itm_11 = dataSnapshot.child("Passion").getValue().toString();
                    availableChecker(Passion, itm_11);
                    String itm_12 = dataSnapshot.child("Blackberry").getValue().toString();
                    availableChecker(Blackberry, itm_12);
                    String itm_10 = dataSnapshot.child("Cheese").getValue().toString();
                    availableChecker(Cheese, itm_10);
                    String itm_8 = dataSnapshot.child("Banana").getValue().toString();
                    availableChecker(Banana, itm_8);
                    String itm_13 = dataSnapshot.child("Mango").getValue().toString();
                    availableChecker(Mango, itm_13);
                    String itm_15 = dataSnapshot.child("Strawberry").getValue().toString();
                    availableChecker(Strawberry, itm_15);
                    String itm_17 = dataSnapshot.child("Raisins").getValue().toString();
                    availableChecker(Raisins, itm_17);
                    String itm_19 = dataSnapshot.child("Oreo").getValue().toString();
                    availableChecker(Oreo, itm_19);
                    String itm_6 = dataSnapshot.child("Chocos").getValue().toString();
                    availableChecker(Chocos, itm_6);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btn_close_mixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        Pineapple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fruitChecker(Pineapple,"Pineapple");
            }
        });
        Passion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fruitChecker(Passion,"Passion");
            }
        });
        Mango.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fruitChecker(Mango,"Mango");
            }
        });
        Strawberry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fruitChecker(Strawberry,"Strawberry");
            }
        });
        Raisins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fruitChecker(Raisins,"Raisins");
            }
        });
        Oreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fruitChecker(Oreo,"Oreo");
            }
        });
        Blueberry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fruitChecker(Blueberry,"Blueberry");
            }
        });
        Kitkat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fruitChecker(Kitkat,"Kitkat");
            }
        });
        Chocos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fruitChecker(Chocos,"Chocos");
            }
        });
        Banana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fruitChecker(Banana,"Banana");
            }
        });
        Cheese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fruitChecker(Cheese,"Cheese");
            }
        });
        Blackberry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fruitChecker(Blackberry,"Blackberry");
            }
        });



        btn_mixin_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( mixIns.size() < 3 ){
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String s : mixIns)
                        stringBuilder.append(s).append(" ");
                    show_err.setText(stringBuilder.toString());
                    String stff = stringBuilder.toString();
                    sendMixInCouple.sendMixIn(stff);
                    getDialog().dismiss();
                } else {
                    show_err.setText("Maximum 2");
                }
            }
        });

        return view;
    }

    private void fruitChecker(CheckBox cb,String Fruit) {
        if (cb.isChecked()){
            mixIns.add(Fruit);
        } else {
            mixIns.remove(Fruit);
        }
    }

    private void availableChecker(CheckBox cb, String ava) {
        if (ava.equals("Available")) {
            cb.setEnabled(true);
        } else {
            cb.setEnabled(false);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            sendMixInCouple = (SendMixInCouple) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG,"onAttach"+e.getMessage());
        }

    }
}
