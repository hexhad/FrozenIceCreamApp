package com.hexhad.introaprilone;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class PaymentInto extends DialogFragment {
    String prod,price;

    private static final String TAG = "PaymentInto";

    public String getProd() {
        return prod;
    }

    public String getPrice() {
        return price;
    }

    public interface OnlinePayment{
        void fakePayment(String name,String date,String pin,String type);
    }
    OnlinePayment onlinePayment2;

    Button paywithpaypal2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payment_intro ,container, false);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        paywithpaypal2 = view.findViewById(R.id.paywithpaypal2);
        paywithpaypal2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onlinePayment2.fakePayment("","","","sandbox");
                getDialog().dismiss();
            }
        });
        
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onlinePayment2 = (OnlinePayment) getTargetFragment();
        } catch (ClassCastException e) {
            Log.wtf(TAG,"ERROR");
        }
    }
}
