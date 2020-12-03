package com.hexhad.introaprilone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QrCodeImg extends DialogFragment {

    private static final String TAG = " QrCodeImg";

    public interface ClearAll{
        public void clear();
    }

    public ClearAll clearAll;

    public String timeThatYouPickedUp;

    ImageView image;
    Button close_qr;

    public DatabaseReference reff1;
    public String order_st;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.custompopup_qr, container, false);

        image = view.findViewById(R.id.qr);
        //only back button
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //dismiss
        close_qr = view.findViewById(R.id.btn_close_qr);
        close_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        //dismiss when no
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();

        reff1 = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        reff1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String hash = dataSnapshot.child("order").getValue().toString();

                timeThatYouPickedUp  = uid + " " + hash;

                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                try {
                    BitMatrix bitMatrix = qrCodeWriter.encode(timeThatYouPickedUp, BarcodeFormat.QR_CODE,300,300);
                    Bitmap bitmap = Bitmap.createBitmap(300,300,Bitmap.Config.RGB_565);

                    for (int x = 0 ; x < 300 ;x++){
                        for (int y = 0 ; y < 300 ; y++){
                            bitmap.setPixel(x,y,bitMatrix.get(x,y)? Color.BLACK : Color.WHITE);
                        }
                    }

                    image.setImageBitmap(bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



//        if (order_st == ""){
//            getDialog().dismiss();
//        }

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        clearAll = (ClearAll) getTargetFragment();
    }
}
