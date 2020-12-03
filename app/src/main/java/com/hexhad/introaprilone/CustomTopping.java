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

public class CustomTopping extends DialogFragment {

    private static final String TAG = "CustomTopping";

    public interface SendToppingCouple{
        void sendTopping(String s);
    }

    SendToppingCouple sendToppingCouple;

    private DatabaseReference reff;


    private ArrayList<String> topping;
    CheckBox ChocolateChips,Marshmallow,ChocoPops,Strawberry,WaferSticks,FruityLoops,PeanutStick,Cherry;
    Button btn_topping_add, btn_close_topping;
    TextView show_err;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.custompopup_topping, container, false);

        topping = new ArrayList<>();

        ChocolateChips = view.findViewById(R.id.ChocolateChips);
        Marshmallow = view.findViewById(R.id.Marshmallow);
        ChocoPops = view.findViewById(R.id.ChocoPops);
        Strawberry = view.findViewById(R.id.Strawberry);
        WaferSticks = view.findViewById(R.id.WaferSticks);
        FruityLoops = view.findViewById(R.id.FruityLoops);
        PeanutStick = view.findViewById(R.id.PeanutStick);
        Cherry = view.findViewById(R.id.Cherry);

        show_err = view.findViewById(R.id.show_err);

        btn_topping_add = view.findViewById(R.id.btn_topping_add);
        btn_close_topping = view.findViewById(R.id.btn_close_topping);

        btn_close_topping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        reff = FirebaseDatabase.getInstance().getReference().child("Stock");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String itm_14 = dataSnapshot.child("ChocolateChips").getValue().toString();
                    availableChecker(ChocolateChips, itm_14);
                    String itm_26 = dataSnapshot.child("WaferSticks").getValue().toString();
                    availableChecker(WaferSticks, itm_26);
                    String itm_15 = dataSnapshot.child("Strawberry").getValue().toString();
                    availableChecker(Strawberry, itm_15);
                    String itm_16 = dataSnapshot.child("Marshmallow").getValue().toString();
                    availableChecker(Marshmallow, itm_16);
                    String itm_20 = dataSnapshot.child("FruityLoops").getValue().toString();
                    availableChecker(FruityLoops, itm_20);
                    String itm_18 = dataSnapshot.child("ChocoPops").getValue().toString();
                    availableChecker(ChocoPops, itm_18);
                    String itm_22 = dataSnapshot.child("PeanutStick").getValue().toString();
                    availableChecker(PeanutStick, itm_22);
                    String itm_24 = dataSnapshot.child("Cherry").getValue().toString();
                    availableChecker(Cherry, itm_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ChocolateChips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fruitChecker(ChocolateChips,"ChocolateChips");
            }
        });
        Marshmallow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fruitChecker(Marshmallow,"Marshmallow");
            }
        });
        ChocoPops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fruitChecker(ChocoPops,"ChocoPops");
            }
        });
        Strawberry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fruitChecker(Strawberry,"Strawberry");
            }
        });
        WaferSticks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fruitChecker(WaferSticks,"WaferSticks");
            }
        });
        FruityLoops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fruitChecker(FruityLoops,"FruityLoops");
            }
        });
        PeanutStick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fruitChecker(PeanutStick,"PeanutStick");
            }
        });
        Cherry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fruitChecker(Cherry,"Cherry");
            }
        });

        btn_topping_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( topping.size() < 3 ){
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String s : topping)
                        stringBuilder.append(s).append(" ");

                    String stff = stringBuilder.toString();
                    sendToppingCouple.sendTopping(stff);
                    getDialog().dismiss();
                } else {
                    show_err.setText("Maximum 2");
                }
            }
        });

        return view;
    }

    private void availableChecker(CheckBox cb, String ava) {
        if (ava.equals("Available")) {
            cb.setEnabled(true);
        } else {
            cb.setEnabled(false);
        }
    }

    private void fruitChecker(CheckBox cb,String Fruit) {
        if (cb.isChecked()){
            topping.add(Fruit);
        } else {
            topping.remove(Fruit);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            sendToppingCouple = (SendToppingCouple) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG,"onAttach"+e.getMessage());
        }
    }
}
