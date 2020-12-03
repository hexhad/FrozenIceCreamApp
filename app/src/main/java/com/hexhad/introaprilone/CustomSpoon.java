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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomSpoon extends DialogFragment {

    private static final String TAG = "CustomSpoon";

    Button btn_base_frag, btn_close_base;
    RadioGroup radio_group_b;
    RadioButton radioButton;

    RadioButton SweetCream, Chocolate, Vanilla, Milo;

    public interface SendBaseCouple{
        void sendBase(String s);
    }

    SendBaseCouple sendBaseCouple;

    private DatabaseReference reff;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.custompopup_spoon, container, false);

        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btn_base_frag = view.findViewById(R.id.btn_base_frag);
        btn_close_base = view.findViewById(R.id.btn_close_base);

        radio_group_b = view.findViewById(R.id.radio_group_b);


        SweetCream = view.findViewById(R.id.SweetCream);
        Chocolate = view.findViewById(R.id.Chocolate);
        Vanilla = view.findViewById(R.id.Vanilla);
        Milo = view.findViewById(R.id.Milo);

        btn_close_base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });



        reff = FirebaseDatabase.getInstance().getReference().child("Stock");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String itm_1 = dataSnapshot.child("SweetCream").getValue().toString();
                    availableChecker(SweetCream, itm_1);
                    String itm_5 = dataSnapshot.child("Chocolate").getValue().toString();
                    availableChecker(Chocolate, itm_5);
                    String itm_7 = dataSnapshot.child("Vanilla").getValue().toString();
                    availableChecker(Vanilla, itm_7);
                    String itm_9 = dataSnapshot.child("Milo").getValue().toString();
                    availableChecker(Milo, itm_9);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_base_frag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radio_group_b.getCheckedRadioButtonId() != -1) {
                    int radioId = radio_group_b.getCheckedRadioButtonId();
                    radioButton = getView().findViewById(radioId);
                    String state = radioButton.getText().toString();
                    sendBaseCouple.sendBase(state);
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
            sendBaseCouple = (SendBaseCouple) getTargetFragment();
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
