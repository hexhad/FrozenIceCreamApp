package com.hexhad.introaprilone;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ItmPreview extends DialogFragment {

    private static final String TAG = "ItmPreview";

    public interface ShowPurchase{
        void sendState(String prod, String price);
    }
    ShowPurchase showPurchase;

    private ImageView imageView;
    TextView title,desc,price;
    String key;

    DatabaseReference ref;

    Button btn_buy,btn_cancel;

    public String pub_prod,pub_price;

    public void setKey(String key) {
        this.key = key;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.itm_prev, container, false);

        imageView = view.findViewById(R.id.itm_img);
        title = view.findViewById(R.id.itm_name);
        desc = view.findViewById(R.id.itm_desc);
        price = view.findViewById(R.id.itm_price);
        btn_buy = view.findViewById(R.id.btn_buy);
        btn_cancel = view.findViewById(R.id.btn_cancel);

        String please = key;
        desc.setText(please);

        ref = FirebaseDatabase.getInstance().getReference().child("Intro");
        ref.child(please).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String getTitle = dataSnapshot.child("name").getValue().toString();
                    String getDesc = dataSnapshot.child("desc").getValue().toString();
                    String getPrice = dataSnapshot.child("price").getValue().toString();
                    String getImg = dataSnapshot.child("img").getValue().toString();

                    title.setText(getTitle);
                    desc.setText(getDesc);
                    price.setText(getPrice);
                    Picasso.get().load(getImg).into(imageView);

                    pub_price = getPrice;
                    pub_prod = getTitle;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPurchase.sendState(pub_prod,pub_price);
                getDialog().dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            showPurchase = (ShowPurchase) getTargetFragment();
        } catch (ClassCastException e){
            Log.e(TAG,"onAttach"+e.getMessage());
        }
    }
}
