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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomSouce extends DialogFragment {

    private static final String TAG = "CustomSouce";
    RadioGroup radio_group_s;
    RadioButton radioButton;
    RadioButton Chocolate,Strawberry,Caramel,CondensedMilk,KithulPani;
    
    public interface SendSouceCouple{
        void sendSouce(String s);
    }
    private DatabaseReference reff;

    SendSouceCouple sendSouceCouple;

    Button btn_sauce_frg, btn_close_sauce;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.custompopup_sauce, container, false);

        btn_sauce_frg = view.findViewById(R.id.btn_sauce_frg);
        btn_close_sauce = view.findViewById(R.id.btn_close_sauce);

        radio_group_s = view.findViewById(R.id.radio_group_s);

        Chocolate = view.findViewById(R.id.Chocolate);
        Strawberry = view.findViewById(R.id.Strawberry);
        Caramel = view.findViewById(R.id.Caramel);
        CondensedMilk = view.findViewById(R.id.CondensedMilk);
        KithulPani = view.findViewById(R.id.KithulPani);

        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        reff = FirebaseDatabase.getInstance().getReference().child("Stock");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String itm_5 = dataSnapshot.child("Chocolate").getValue().toString();
                    availableChecker(Chocolate, itm_5);
                    String itm_15 = dataSnapshot.child("Strawberry").getValue().toString();
                    availableChecker(Strawberry, itm_15);
                    String itm_21 = dataSnapshot.child("Caramel").getValue().toString();
                    availableChecker(Caramel, itm_21);
                    String itm_23 = dataSnapshot.child("CondensedMilk").getValue().toString();
                    availableChecker(CondensedMilk, itm_23);
                    String itm_25 = dataSnapshot.child("KithulPani").getValue().toString();
                    availableChecker(KithulPani, itm_25);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_close_sauce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        btn_sauce_frg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radio_group_s.getCheckedRadioButtonId() != -1) {
                    int radioId = radio_group_s.getCheckedRadioButtonId();
                    radioButton = getView().findViewById(radioId);
                    String state = radioButton.getText().toString();

                    sendSouceCouple.sendSouce(state);
                    getDialog().dismiss();
                } else {
                    Log.e(TAG,"ERROR");
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            sendSouceCouple = (SendSouceCouple) getTargetFragment();
        } catch (ClassCastException e){
            Log.e(TAG,"onAttach"+e.getMessage());
        }
    }

    private void availableChecker(RadioButton cb, String ava) {
        if (ava.equals("Available")) {
            cb.setEnabled(true);
        } else {
            cb.setEnabled(false);
        }
    }
}
