package com.hexhad.introaprilone;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragmentThree#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragmentThree extends Fragment implements TimePopUp.OnSetTimeSelected, CustomMixIn.SendMixInCouple, CustomTopping.SendToppingCouple, CustomSouce.SendSouceCouple, CustomSpoon.SendBaseCouple, CustomDialog.OnlinePayment, QrCodeImg.ClearAll {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private static final String TAG = "BlankFragmentThree";

    private DatabaseReference reff1,reff3,reff2;
    private String order_st;

    Dialog dialog;

    ExtendedFloatingActionButton btn_qr,btn_spoon_frag,btn_sauce_frag, btn_topping_frag, btn_mixin_frag;
    public Button pay_popup, btn_time   ;
    ImageView btn_cancel_time;
    TextView relativeLayout,guide,ingr;
    public String mix = "", topping = "", sauce = "", base = "", time;

    private DatabaseReference reff;
    private DatabaseReference mDatabase, mDatabase2;

    private CharSequence[] mArray;

    public BlankFragmentThree() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragmentThree.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragmentThree newInstance(String param1, String param2) {
        BlankFragmentThree fragment = new BlankFragmentThree();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank_three, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dialog = new Dialog(getContext());

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Orders");
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Users");

        pay_popup = view.findViewById(R.id.popup_up_btn);
        btn_qr = view.findViewById(R.id.btn_qr);
        btn_time = view.findViewById(R.id.btn_time);
        btn_spoon_frag = view.findViewById(R.id.btn_spoon_frag);
        btn_cancel_time = view.findViewById(R.id.btn_cancel_time);
        btn_mixin_frag = view.findViewById(R.id.btn_mixin_frag);
        btn_topping_frag = view.findViewById(R.id.btn_topping_frag);
        btn_sauce_frag = view.findViewById(R.id.btn_sauce_frag);
        relativeLayout = view.findViewById(R.id.already_ordered);
        guide = view.findViewById(R.id.guide);
        ingr = view.findViewById(R.id.ingr);

        FabManager();

        showItemsOnINGR();

        //---------------------------------------------------------------------
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        reff3 = FirebaseDatabase.getInstance().getReference().child("Orders").child(currentDate);
        reff3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    final String uid = user.getUid();

                    reff2 = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                    reff2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            order_st = dataSnapshot.child("order").getValue().toString();

                            if (order_st.equals("")) {
                                relativeLayout.setVisibility(View.INVISIBLE);
                                btn_time.setVisibility(View.VISIBLE);
                                btn_cancel_time.setVisibility(View.INVISIBLE);
                                btn_sauce_frag.setVisibility(View.INVISIBLE);
                                btn_topping_frag.setVisibility(View.INVISIBLE);
                                btn_mixin_frag.setVisibility(View.INVISIBLE);
                                btn_spoon_frag.setVisibility(View.INVISIBLE);
                                pay_popup.setVisibility(View.INVISIBLE);
                                btn_qr.setVisibility(View.INVISIBLE);
                                guide.setVisibility(View.INVISIBLE);

                            } else {
                                relativeLayout.setVisibility(View.INVISIBLE);
                                btn_time.setVisibility(View.INVISIBLE);
                                btn_cancel_time.setVisibility(View.INVISIBLE);
                                btn_sauce_frag.setVisibility(View.INVISIBLE);
                                btn_topping_frag.setVisibility(View.INVISIBLE);
                                btn_mixin_frag.setVisibility(View.INVISIBLE);
                                btn_spoon_frag.setVisibility(View.INVISIBLE);
                                pay_popup.setVisibility(View.INVISIBLE);
                                btn_qr.setVisibility(View.VISIBLE);
                                guide.setVisibility(View.VISIBLE);

                                NotifyOrder(order_st);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    btn_time.setVisibility(View.INVISIBLE);
                    relativeLayout.setVisibility(View.VISIBLE);
                    relativeLayout.setText("We are Closed Today");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //---------------------------------------------------------------------



        btn_qr.shrink();

        btn_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_qr.isExtended()){
                    btn_qr.shrink();
                    QrCodeImg qrCodeImg = new QrCodeImg();
                    qrCodeImg.show(getFragmentManager(), "");
                } else {
                    btn_qr.extend();
                }

            }
        });


        pay_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMix() && isTopping() && isSauce() && isBase()) {
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    final String unique_id = user.getUid();

                    reff = FirebaseDatabase.getInstance().getReference().child("Count").child(unique_id);
                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                int count = (int) dataSnapshot.getChildrenCount();

                                if (count > 9) {
                                    mArray = new CharSequence[]{"Online Payment", "Cash Payment"};
                                    MaterialAlertDialogBuilder d = new MaterialAlertDialogBuilder(getContext());
                                    d.setTitle("Payment Method");
                                    d.setBackground(getResources().getDrawable(R.drawable.all_corner_24_white, null));

                                    d.setSingleChoiceItems(mArray, 0, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            if (which == 0) {
                                                CustomDialog customDialog = new CustomDialog();
                                                customDialog.setTargetFragment(BlankFragmentThree.this, 5);
                                                customDialog.show(getFragmentManager(), "");
                                                dialog.dismiss();
                                            } else {

                                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                final String unique_id = user.getUid();

                                                Calendar calendar = Calendar.getInstance();
                                                String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                                                DatabaseReference current_user_db = mDatabase.child(currentDate);

                                                DatabaseReference sub = current_user_db.child(time);
                                                sub.child("UID").setValue(unique_id);
                                                sub.child("Add_01").setValue(mix);
                                                sub.child("Add_02").setValue(topping);
                                                sub.child("Add_03").setValue(sauce);
                                                sub.child("Add_04").setValue(base);
                                                sub.child("Status").setValue("Booked");
                                                sub.child("PaymentType").setValue("Rs. 300.00");

                                                T24Conv();


                                                btn_cancel_time.setVisibility(View.INVISIBLE);
                                                btn_sauce_frag.setVisibility(View.INVISIBLE);
                                                btn_topping_frag.setVisibility(View.INVISIBLE);
                                                btn_mixin_frag.setVisibility(View.INVISIBLE);
                                                btn_spoon_frag.setVisibility(View.INVISIBLE);
                                                pay_popup.setVisibility(View.INVISIBLE);
                                                btn_qr.setVisibility(View.VISIBLE);
                                                guide.setVisibility(View.VISIBLE);

                                                dialog.dismiss();

                                            }

                                        }
                                    });
                                    d.show();

                                } else {
                                    CustomDialog customDialog = new CustomDialog();
                                    customDialog.setTargetFragment(BlankFragmentThree.this, 5);
                                    customDialog.show(getFragmentManager(), "");
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                } else {
                    emptyChecker();
                }
            }
        });

        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePopUp timePopUp = new TimePopUp();
                timePopUp.setTargetFragment(BlankFragmentThree.this, 1);
                timePopUp.show(getFragmentManager(), "TimePopUpDialog");


            }
        });

        btn_cancel_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_cancel_time.setVisibility(View.INVISIBLE);
                btn_sauce_frag.setVisibility(View.INVISIBLE);
                btn_topping_frag.setVisibility(View.INVISIBLE);
                btn_mixin_frag.setVisibility(View.INVISIBLE);
                btn_spoon_frag.setVisibility(View.INVISIBLE);
                pay_popup.setVisibility(View.INVISIBLE);
                pay_popup.setVisibility(View.INVISIBLE);
                btn_time.setVisibility(View.VISIBLE);
                guide.setVisibility(View.VISIBLE);

            }
        });

    }

    private void showItemsOnINGR() {
        String s = mix+" "+topping+" "+sauce+" "+base;
        ingr.setText(s);
    }

    private void FabManager() {

        btn_spoon_frag.shrink();
        btn_sauce_frag.shrink();
        btn_topping_frag.shrink();
        btn_mixin_frag.shrink();

        btn_spoon_frag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_spoon_frag.isExtended()){
                    btn_spoon_frag.shrink();
                    CustomSpoon customSpoon = new CustomSpoon();
                    customSpoon.setTargetFragment(BlankFragmentThree.this, 5);
                    customSpoon.show(getFragmentManager(), "");
                } else {
                    btn_spoon_frag.extend();
                    btn_sauce_frag.shrink();
                    btn_topping_frag.shrink();
                    btn_mixin_frag.shrink();
                }

            }
        });
        btn_sauce_frag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_sauce_frag.isExtended()){
                    btn_sauce_frag.shrink();
                    CustomSouce customSouce = new CustomSouce();
                    customSouce.setTargetFragment(BlankFragmentThree.this, 4);
                    customSouce.show(getFragmentManager(), "");
                } else {
                    btn_sauce_frag.extend();
                    btn_spoon_frag.shrink();
                    btn_topping_frag.shrink();
                    btn_mixin_frag.shrink();
                }

            }
        });
        btn_topping_frag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_topping_frag.isExtended()){
                    btn_topping_frag.shrink();
                    CustomTopping customTopping = new CustomTopping();
                    customTopping.setTargetFragment(BlankFragmentThree.this, 3);
                    customTopping.show(getFragmentManager(), "");
                } else {
                    btn_topping_frag.extend();
                    btn_spoon_frag.shrink();
                    btn_sauce_frag.shrink();
                    btn_mixin_frag.shrink();
                }

            }
        });
        btn_mixin_frag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_mixin_frag.isExtended()){
                    btn_mixin_frag.shrink();
                    CustomMixIn customMixIn = new CustomMixIn();
                    customMixIn.setTargetFragment(BlankFragmentThree.this, 2);
                    customMixIn.show(getFragmentManager(), "");
                } else {
                    btn_mixin_frag.extend();
                    btn_spoon_frag.shrink();
                    btn_sauce_frag.shrink();
                    btn_topping_frag.shrink();
                }

            }
        });
    }

    private void T24Conv() {
        String timeUpdate = "";
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String unique_id = user.getUid();
        DatabaseReference credit_c = mDatabase2.child(unique_id);
        if (time.equals("11:30")) {
            timeUpdate = "11:30";
        } else if (time.equals("12:00")) {
            timeUpdate = "12:00";
        } else if (time.equals("12:30")) {
            timeUpdate = "12:30";
        } else if (time.equals("01:00")) {
            timeUpdate = "13:00";
        } else if (time.equals("01:30")) {
            timeUpdate = "13:30";
        } else if (time.equals("02:00")) {
            timeUpdate = "14:00";
        } else if (time.equals("02:30")) {
            timeUpdate = "14:30";
        } else if (time.equals("03:00")) {
            timeUpdate = "15:00";
        } else if (time.equals("03:30")) {
            timeUpdate = "15:30";
        } else if (time.equals("04:00")) {
            timeUpdate = "16:00";
        } else if (time.equals("04:30")) {
            timeUpdate = "16:30";
        } else if (time.equals("05:00")) {
            timeUpdate = "17:00";
        } else if (time.equals("05:30")) {
            timeUpdate = "17:30";
        } else if (time.equals("06:00")) {
            timeUpdate = "18:00";
        }

        if (timeUpdate == "") {

        } else {
            credit_c.child("order").setValue(timeUpdate);
        }

    }

    private void NotifyOrder(String time) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("kk:mm");
        String getCurrentDate = sdf.format(c.getTime());
        //Toast.makeText(getContext(),getCurrentDate + " " + time , Toast.LENGTH_SHORT).show();

        if (getCurrentDate.compareTo(time) < 0) {
            //Toast.makeText(getContext(), "HurryUp!!! Visit the Store", Toast.LENGTH_SHORT).show();
            notifyPopUp(time);

        } else {

            if (getCurrentDate.compareTo(add15mins(time)) < 0) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String uid = user.getUid();

//                reff1 = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
//                reff1.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        if (dataSnapshot.exists()) {
//                            int count = (int) dataSnapshot.child("resetLoyal").getValue();
//                            --count;
                DatabaseReference credit_c = mDatabase2.child(uid);
                credit_c.child("order").setValue("");
                //credit_c.child("resetLoyal").setValue(count);
//
//                            if (count == 0) {
//                                DatabaseReference current_user_db = mDatabase.child("Count").child(uid);
//                                current_user_db.removeValue();
//                            }
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });

            } else {
                //Toast.makeText(getContext(),"Older", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private String add15mins(String time) {
        String timeUpdate = "";
        if (time.equals("11:30")) {
            timeUpdate = "11:45";
        } else if (time.equals("12:00")) {
            timeUpdate = "12:15";
        } else if (time.equals("12:30")) {
            timeUpdate = "12:45";
        } else if (time.equals("13:00")) {
            timeUpdate = "13:00";
        } else if (time.equals("13:30")) {
            timeUpdate = "13:45";
        } else if (time.equals("14:00")) {
            timeUpdate = "14:15";
        } else if (time.equals("14:30")) {
            timeUpdate = "14:45";
        } else if (time.equals("15:00")) {
            timeUpdate = "15:15";
        } else if (time.equals("15:30")) {
            timeUpdate = "15:45";
        } else if (time.equals("16:00")) {
            timeUpdate = "16:15";
        } else if (time.equals("16:30")) {
            timeUpdate = "16:45";
        } else if (time.equals("17:00")) {
            timeUpdate = "17:15";
        } else if (time.equals("17:30")) {
            timeUpdate = "17:45";
        } else if (time.equals("18:00")) {
            timeUpdate = "18:15";
        }
        return timeUpdate;
    }

    private void notifyPopUp(String time) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        String getCurrentT = sdf.format(c.getTime());
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(getContext())
                .setSmallIcon(R.drawable.ic_card_giftcard)
                .setContentTitle("Frozen Ice Cream")
                .setContentText("Your Order will be ready in " + time + " Visit us!!" )
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }


    @Override
    public void sendState(String input) {

        if (input.equals("") || input.equals("holiday")) {
            //relativeLayout.setText("Not Ordered Yet");
        } else {
            time = input;
            btn_time.setVisibility(View.INVISIBLE);
            btn_cancel_time.setVisibility(View.VISIBLE);
            btn_sauce_frag.setVisibility(View.VISIBLE);
            btn_topping_frag.setVisibility(View.VISIBLE);
            btn_mixin_frag.setVisibility(View.VISIBLE);
            btn_spoon_frag.setVisibility(View.VISIBLE);
            pay_popup.setVisibility(View.VISIBLE);

            //relativeLayout.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void sendMixIn(String s) {
        if (s.equals("")) {
            relativeLayout.setText("");
        } else {
            relativeLayout.setText(s);
            mix = s;
        }
    }

    @Override
    public void sendTopping(String s) {
        if (s.equals("")) {
            relativeLayout.setText("");
        } else {
            relativeLayout.setText(s);
            topping = s;
        }
    }

    @Override
    public void sendSouce(String s) {
        if (s.equals("")) {
            relativeLayout.setText("");
        } else {
            relativeLayout.setText(s);
            sauce = s;
        }
    }

    @Override
    public void sendBase(String s) {
        if (s.equals("")) {
            relativeLayout.setText("");
        } else {
            relativeLayout.setText(s);
            base = s;
        }
    }


    private void emptyChecker() {
        if (!isBase()) {
            errPopUP("Base", "Select at least one item");
        } else if (!isMix()) {
            errPopUP("Mix", "Select at least one item");
        } else if (!isSauce()) {
            errPopUP("Sauce", "Select at least one item");
        } else if (!isTopping()) {
            errPopUP("Topping", "Select at least one item");
        }
    }

    private void errPopUP(String cate, String cate_disc) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());

        builder.setIcon(R.drawable.ic_close_black_48dp);
        builder.setTitle(cate);
        builder.setBackground(getResources().getDrawable(R.drawable.all_corner_24_white, null));
        builder.setMessage(cate_disc);
        builder.show();
    }

    private boolean isBase() {
        if (base == "") {
            return false;
        } else {
            return true;
        }
    }

    private boolean isSauce() {
        if (sauce == "") {
            return false;
        } else {
            return true;
        }
    }

    private boolean isTopping() {
        if (topping == "") {
            return false;
        } else {
            return true;
        }
    }

    private boolean isMix() {
        if (mix == "") {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void clear() {
        mix = "";
        topping = "";
        sauce = "";
        base = "";
        time = "";
    }

    @Override
    public void fakePayment(String name, String date, String pin, String type) {

        if (type.equals("sandbox")){
            Intent intent = new Intent(getContext(),BetaAct.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final String unique_id = user.getUid();

            if (isMix() && isTopping() && isSauce() && isBase()) {

                Calendar calendar = Calendar.getInstance();
                String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                DatabaseReference current_user_db = mDatabase.child(currentDate);

                DatabaseReference sub = current_user_db.child(time);
                sub.child("UID").setValue(unique_id);
                sub.child("Add_01").setValue(mix);
                sub.child("Add_02").setValue(topping);
                sub.child("Add_03").setValue(sauce);
                sub.child("Add_04").setValue(base);
                sub.child("Status").setValue("Booked");
                sub.child("PaymentType").setValue("Online");

                DatabaseReference credit_c = mDatabase2.child(unique_id);
                credit_c.child("cred_date").setValue(date);
                credit_c.child("cred_name").setValue(name);
                credit_c.child("cred_pin").setValue(pin);
                credit_c.child("order").setValue(time);


                btn_cancel_time.setVisibility(View.INVISIBLE);
                btn_sauce_frag.setVisibility(View.INVISIBLE);
                btn_topping_frag.setVisibility(View.INVISIBLE);
                btn_mixin_frag.setVisibility(View.INVISIBLE);
                btn_spoon_frag.setVisibility(View.INVISIBLE);
                pay_popup.setVisibility(View.INVISIBLE);
                btn_qr.setVisibility(View.VISIBLE);
            } else {
                emptyChecker();
            }
        }
    }
}
