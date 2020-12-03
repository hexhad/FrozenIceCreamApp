package com.hexhad.introaprilone;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class CustomRecoverEmail extends DialogFragment {

    private FirebaseAuth mAuth;

    TextInputLayout getemail;
    Button conf, close;

    public interface SendRecoverSt{
        void sendRec(String s);
    }

    SendRecoverSt sendRecoverSt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recover_custom_dialog, container, false);

        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mAuth = FirebaseAuth.getInstance();

        getemail = view.findViewById(R.id.email);
        conf = view.findViewById(R.id.recover);
        close = view.findViewById(R.id.cancel);

        conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEmail()){
                    final String email = getemail.getEditText().getText().toString().trim();
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getContext(), "Email Sent", Toast.LENGTH_SHORT).show();
                                getDialog().dismiss();
                            } else {
                                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            sendRecoverSt = (SendRecoverSt) getTargetFragment();
        } catch (ClassCastException e){
            Log.e("TAG","onAttach"+e.getMessage());
        }
    }

    private Boolean validateEmail() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String val = getemail.getEditText().getText().toString();
        if (val.isEmpty()) {
            getemail.setError("Field cannot be Empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            getemail.setError("Invalid Email");
            return false;
        } else {
            getemail.setError(null);
            getemail.setErrorEnabled(false);
            return true;
        }
    }
}
