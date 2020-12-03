package com.hexhad.introaprilone;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragmentOne#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragmentOne extends Fragment implements ItmPreview.ShowPurchase , PaymentInto.OnlinePayment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;

    ArrayList<rvList> ice_cream_list;

    RoundedImageView prof;


    Button snackopener;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private TextView username_top;
    private DatabaseReference reff,reff2;

    ViewPager viewPager;
    AdapterHomeSlider adapter;
    List<Model> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    private FirebaseRecyclerOptions<rvList> options;
    private FirebaseRecyclerAdapter<rvList,MyViewHolder> adapterR;



    public BlankFragmentOne() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragmentOne.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragmentOne newInstance(String param1, String param2) {
        BlankFragmentOne fragment = new BlankFragmentOne();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank_one, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        prof = view.findViewById(R.id.prof);
        username_top = view.findViewById(R.id.hex);
        retfromDB();
        setProfilePic();

        models = new ArrayList<>();


        models.add(new Model(R.drawable.ic_screen_1,"Guide","Swipe left to Check New Features"));
        models.add(new Model(R.drawable.ic_screen_1, "Select Time", "Select time using \"Pick your time\" button"));
        models.add(new Model(R.drawable.ic_screen_1, "Customize", "Customize all your layers"));
        models.add(new Model(R.drawable.ic_screen_1, "Payment", "Online transaction to order ice cream"));
        models.add(new Model(R.drawable.ic_screen_1, "Qr Code", "Visit store and show this to cashier"));
        models.add(new Model(R.drawable.ic_screen_1, "Loyal Customer", "First 10 transactions will Unlock this achievement"));
        models.add(new Model(R.drawable.ic_screen_1, "Just Order", "You need to become Loyal Customer"));




        recyclerView = view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(false);






        //=================================================================================




        adapter = new AdapterHomeSlider(models,getContext());

        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        //viewPager.setPadding(10,0,10,0);



        mRef = FirebaseDatabase.getInstance().getReference().child("Intro");
        loadDataRVSec();

    }

    private void loadDataRVSec() {

        options = new FirebaseRecyclerOptions.Builder<rvList>().setQuery(mRef,rvList.class).build();
        adapterR = new FirebaseRecyclerAdapter<rvList, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, final int position, @NonNull rvList model) {
                holder.itm_desc.setText(model.getDesc());
                holder.itm_name.setText(model.getName());
                holder.itm_price.setText(model.getPrice());
                Picasso.get().load(model.getImg()).into(holder.itm_img);
                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String selected = getRef(position).getKey();
                        ItmPreview itm = new ItmPreview();
                        //Toast.makeText(getContext(), selected, Toast.LENGTH_SHORT).show();
                        itm.setKey(selected);
                       // Bundle args = new Bundle();
                        //args.putString("key",getRef(position).getKey());
                        //args.putString("key","getRef(position).getKey()");
                        //itm.setArguments(args);
                        itm.setTargetFragment(BlankFragmentOne.this, 1);
                        itm.show(getFragmentManager(), "ItmPreview");
                        return;
                    }
                });
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View vv = LayoutInflater.from(parent.getContext()).inflate(R.layout.ver_list_item,parent,false);
                return new MyViewHolder(vv);
            }
        };

        adapterR.startListening();
        recyclerView.setAdapter(adapterR);
    }


    private void retfromDB() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String unique_id = user.getUid();

        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(unique_id);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String nameDB = dataSnapshot.child("name").getValue().toString();
                username_top.setText(nameDB);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void backToHome () {
        Intent home_intent = new Intent(getContext(),FisrstActivity.class);
        home_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(home_intent);
    }

    private void setProfilePic() {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String unique_id = user.getUid();
        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(unique_id);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String profile_pic = dataSnapshot.child("profile_pic").getValue().toString();

                if (profile_pic.equals("")){

                }else{
                    try {
                        Picasso.get().load(profile_pic).into(prof);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.img_pro_pic).into(prof);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void sendState(String prod, String price) {
        Toast.makeText(getContext(), prod + "\n" + price, Toast.LENGTH_SHORT).show();
        PaymentInto paymentInto = new PaymentInto();
        paymentInto.setTargetFragment(BlankFragmentOne.this, 1);
        paymentInto.show(getFragmentManager(), "PaymentInto");
    }

    @Override
    public void fakePayment(String name, String date, String pin, String type) {
        if (type.equals("sandbox")){
            Intent intent = new Intent(getContext(),BetaAct.class);
            startActivity(intent);
            getActivity().finish();
        }
    }
}
