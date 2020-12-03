package com.hexhad.introaprilone;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.List;

import www.sanju.motiontoast.MotionToast;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragmentTwo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragmentTwo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MaterialSearchBar searchBar;
    public int userLevel;

    private DatabaseReference reff,reff2;

    TextView gft_title, gft_desc, gft_add_desc;

    public BlankFragmentTwo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragmentTwo.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragmentTwo newInstance(String param1, String param2) {
        BlankFragmentTwo fragment = new BlankFragmentTwo();
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
        View view =  inflater.inflate(R.layout.fragment_blank_two, container, false);

        gft_title = view.findViewById(R.id.gft_title);
        gft_desc = view.findViewById(R.id.gft_desc);
        gft_add_desc = view.findViewById(R.id.gft_add_desc);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String unique_id = user.getUid();

        reff = FirebaseDatabase.getInstance().getReference().child("Discount");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int  target_count =  Integer.valueOf(dataSnapshot.child("Count").getValue().toString());
                String desc_1 = dataSnapshot.child("Desc").getValue().toString();
                String desc_2 = dataSnapshot.child("Desc_2").getValue().toString();
                String discount = dataSnapshot.child("Discount").getValue().toString();

                callCount();


                if (userLevel > target_count) {
                    gft_title.setText(desc_1);
                    gft_desc.setText(desc_2);
                    gft_add_desc.setText(discount);
                } else {
                    gft_title.setText("Oops!!");
                    gft_desc.setText("No Discounts are available for toady");
                    gft_add_desc.setText("😢");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void callCount() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String unique_id = user.getUid();

        reff2 = FirebaseDatabase.getInstance().getReference().child("Count");
        reff2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userLevel = (int) dataSnapshot.child(unique_id).getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
