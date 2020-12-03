package com.hexhad.introaprilone;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class TimePopUp extends DialogFragment {

    private String ExtractionDataUrl = "http://frozenice.000webhostapp.com/appCon/ExtractData.php";

    Button btn_set_time, btn_close;
    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView selected_view;
    RadioButton btn_1130, btn_1200, btn_1230, btn_0100, btn_0130, btn_0200, btn_0230, btn_0300, btn_0330, btn_0400, btn_0430, btn_0500, btn_0530, btn_0600;

    private DatabaseReference reff;

    private static final String TAG = "TimePopUp";

    public interface OnSetTimeSelected {
        void sendState(String input);
    }

    public OnSetTimeSelected onSetTimeSelected;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.custompopup_time, container, false);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btn_set_time = view.findViewById(R.id.btn_set_time);
        btn_close = view.findViewById(R.id.btn_close_list);
        selected_view = view.findViewById(R.id.selected_view);
        radioGroup = view.findViewById(R.id.radio_group);

        btn_1130 = view.findViewById(R.id.btn_1130);
        btn_1200 = view.findViewById(R.id.btn_1200);
        btn_1230 = view.findViewById(R.id.btn_1230);
        btn_0100 = view.findViewById(R.id.btn_100);
        btn_0130 = view.findViewById(R.id.btn_130);
        btn_0200 = view.findViewById(R.id.btn_200);
        btn_0230 = view.findViewById(R.id.btn_230);
        btn_0300 = view.findViewById(R.id.btn_300);
        btn_0330 = view.findViewById(R.id.btn_330);
        btn_0400 = view.findViewById(R.id.btn_400);
        btn_0430 = view.findViewById(R.id.btn_430);
        btn_0500 = view.findViewById(R.id.btn_500);
        btn_0530 = view.findViewById(R.id.btn_530);
        btn_0600 = view.findViewById(R.id.btn_600);

        ExtractionData();
        ExpiredButton();


        btn_set_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getCheckedRadioButtonId() != -1) {
                    int radioId = radioGroup.getCheckedRadioButtonId();
                    radioButton = getView().findViewById(radioId);
                    //selected_view.setText(radioButton.getText());

                    String state = radioButton.getText().toString();

                    if (state.equals("")) {
                        getDialog().dismiss();
                    } else {
                        onSetTimeSelected.sendState(radioButton.getText().toString());
                        getDialog().dismiss();
                    }
                } else {
                    selected_view.setText("Please Select A Time");
                }
            }
        });


        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    private void ExpiredButton() {
        SingleExpiredButton("11:15", btn_1130);
        SingleExpiredButton("11:45", btn_1200);
        SingleExpiredButton("12:15", btn_1230);
        SingleExpiredButton("12:45", btn_0100);
        SingleExpiredButton("13:15", btn_0130);
        SingleExpiredButton("13:45", btn_0200);
        SingleExpiredButton("14:15", btn_0230);
        SingleExpiredButton("14:45", btn_0300);
        SingleExpiredButton("15:15", btn_0330);
        SingleExpiredButton("15:45", btn_0400);
        SingleExpiredButton("16:15", btn_0430);
        SingleExpiredButton("16:45", btn_0500);
        SingleExpiredButton("24:15", btn_0530);
        SingleExpiredButton("24:45", btn_0600);
    }

    private void SingleExpiredButton(String time, RadioButton Radbtn) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("kk:mm");
        String getCurrentTime = date.format(c.getTime());
        //String getCurrentTime = "12:00";
        //Toast.makeText(getContext(), getCurrentTime, Toast.LENGTH_SHORT).show();
        //String myTime = "11:28 AM";
        //Toast.makeText(getContext(),getCurrentTime + " " + time , Toast.LENGTH_SHORT).show();

        if (getCurrentTime.compareTo(time) < 0) {
            //Toast.makeText(getContext(),"Younger", Toast.LENGTH_SHORT).show();
            Radbtn.setEnabled(true);
            //btn.setClickable(true);
        } else {
            //          Radbtn.setEnabled(false);
            Radbtn.setClickable(false);
            Radbtn.setText("Time Slot not Available");

//           //parse textColor from string hex code
            int textColor = Color.parseColor("#800000");
            Radbtn.setTextColor(ColorStateList.valueOf(textColor));
            Radbtn.setButtonTintList(ColorStateList.valueOf(textColor));
//           //Toast.makeText(getContext(),"Older", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onSetTimeSelected = (OnSetTimeSelected) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach" + e.getMessage());
        }
    }

    //MYSQLSERVER
    private void ExtractionData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ExtractionDataUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject data = array.getJSONObject(i);
                        String username = data.getString("username");
                        String count = data.getString("email");

                        syncDbAvailability(btn_1130, username, count, "11:30");
                        syncDbAvailability(btn_1200, username, count, "12:00");
                        syncDbAvailability(btn_1230, username, count, "12:30");
                        syncDbAvailability(btn_0100, username, count, "01:00");

                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void syncDbAvailability(RadioButton btn, String username, String count, String s) {
        if (username.equals(s)) {
            if (retAvai(count)) {
                btn.setEnabled(true);
            } else {
                btn.setClickable(false);
                btn.setText("Time Slot not Available");

//           //parse textColor from string hex code
                int textColor = Color.parseColor("#800000");
                btn.setTextColor(ColorStateList.valueOf(textColor));
                btn.setButtonTintList(ColorStateList.valueOf(textColor));
//           //Toast.makeText(getContext(),"Older", Toast.LENGTH_SHORT).show();
            }
        }
        //Toast.makeText(getContext(), username, Toast.LENGTH_SHORT).show();
    }

    private boolean retAvai(String email) {
        int count = Integer.parseInt(email);
        if (count >= 20) {
            return false;
        } else {
            return true;
        }
    }

}
