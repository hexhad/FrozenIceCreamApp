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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CustomDialog extends DialogFragment {
    private static final String TAG = " CustomDialog";

    public interface OnlinePayment{
        void fakePayment(String name,String date,String pin,String type);
    }
    ImageView paywithpaypal;
    OnlinePayment onlinePayment;
    CheckBox checkBox;
    Button exit_btn, btn_payment_done;
    EditText pay_pin, pay_date, pay_name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.custompopup, container, false);

        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        paywithpaypal = view.findViewById(R.id.paywithpaypal);
        paywithpaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onlinePayment.fakePayment("","","","sandbox");
                getDialog().dismiss();
            }
        });

        exit_btn = view.findViewById(R.id.btn_close);
        checkBox = view.findViewById(R.id.remember_me);
        pay_pin = view.findViewById(R.id.pay_pin);
        pay_date = view.findViewById(R.id.pay_date);
        pay_name = view.findViewById(R.id.pay_name);

        btn_payment_done = view.findViewById(R.id.btn_payment_done);

        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        btn_payment_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getName = pay_name.getText().toString();
                String getDate = pay_date.getText().toString();
                String getPin = pay_pin.getText().toString();
                if (checkBox.isChecked()){
                    onlinePayment.fakePayment(getName,getDate,getPin,"");
                    getDialog().dismiss();
                } else {
                    onlinePayment.fakePayment("","","","");
                    getDialog().dismiss();
                }


            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onlinePayment = (OnlinePayment) getTargetFragment();
        } catch (ClassCastException e) {
            Log.wtf(TAG,"ERROR");
        }
    }
}
