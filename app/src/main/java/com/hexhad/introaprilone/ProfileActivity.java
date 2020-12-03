package com.hexhad.introaprilone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class ProfileActivity extends AppCompatActivity {


    private EditText name,phone_test;
    Button doneAll,btn_cancel;
    LinearLayout update_p;
    private DatabaseReference reff;
    RoundedImageView pro_pic;

    //permission
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;

    String cameraPermissions[];
    String storagePermissions[];

    //URI
    Uri image_uri;

    ProgressDialog pd;

    //Sto
    StorageReference storageReference;
    //path
    String storagePath = "user_profile_imgs/";

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        pro_pic = findViewById(R.id.pro_pic);
        setProfilePic();
        ret_data_form_db();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        cameraPermissions= new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        name = findViewById(R.id.name_test);
        phone_test = findViewById(R.id.phone_test);
        doneAll = findViewById(R.id.btn_done);
        pro_pic = findViewById(R.id.pro_pic);
        update_p = findViewById(R.id.update_img);
        btn_cancel =findViewById(R.id.btn_cancel);
        pd = new ProgressDialog(ProfileActivity.this);

        storageReference = getInstance().getReference();

        update_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cam_or_gallery();
            }
        });


        doneAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameUpdated = name.getText().toString().trim();
                String phoneUpdated = phone_test.getText().toString().trim();
                pd.show();
                pd.setTitle("Updating");
                pd.setMessage("Updating Name and Mobile Number");
                if (!nameUpdated.isEmpty() && !phoneUpdated.isEmpty()){

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                    String user_id = user.getUid();
                    DatabaseReference current_user_db = mDatabase.child(user_id);

                    current_user_db.child("name").setValue(nameUpdated);
                    current_user_db.child("phone").setValue(phoneUpdated).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pd.dismiss();
                            Toast.makeText(ProfileActivity.this,"Updated",Toast.LENGTH_SHORT).show();
                            moveBackToHome();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(ProfileActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                            moveBackToHome();
                        }
                    });
                }


            }
        });

        pressedBtnCancel();

    }

    private void moveBackToHome() {
        Intent move_to_home = new Intent(ProfileActivity.this,HomeActivity.class);
        move_to_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        move_to_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(move_to_home);
        finish();
    }

    private void pressedBtnCancel() {
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveBackToHome();
            }
        });
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
                        Picasso.get().load(profile_pic).into(pro_pic);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.img_pro_pic).into(pro_pic);
                    }
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ret_data_form_db() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        final String unique_id = user.getUid();

        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(unique_id);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                String nameDB = dataSnapshot.child("name").getValue().toString();
                String mobileDB = dataSnapshot.child("phone").getValue().toString();


                name.setText(nameDB);
                phone_test.setText(mobileDB);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void cam_or_gallery() {

        String options[] = {"Camera","Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    if (!checkCameraPermission()){
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                } else if (which == 1) {
                    if (!checkStoragePermission()){
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }

                }
            }
        });
        builder.create().show();
    }
    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void requestStoragePermission(){
        requestPermissions(storagePermissions, STORAGE_REQUEST_CODE);
    }
    private boolean checkCameraPermission(){
        boolean result1 = ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result = ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }
    private void requestCameraPermission(){
        requestPermissions( cameraPermissions, CAMERA_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                image_uri = data.getData();

                uploadProfilePic(image_uri);
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE){
                uploadProfilePic(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void uploadProfilePic(final Uri uri) {

        pd.show();
        pd.setTitle("Updating");
        pd.setMessage("Updating Profile Picture");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        String filePathName = storagePath+""+user.getUid();
        StorageReference storageReference2nd = storageReference.child(filePathName);
        storageReference2nd.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                    Uri downloadUri = uriTask.getResult();
                    if (uriTask.isSuccessful()){

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                        String user_id = user.getUid();
                        DatabaseReference current_user_db = mDatabase.child(user_id);

                        current_user_db.child("profile").setValue("finished");

                        current_user_db.child("profile_pic").setValue(downloadUri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                pd.dismiss();
                                Toast.makeText(ProfileActivity.this,"Image Updated",Toast.LENGTH_SHORT).show();




                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(ProfileActivity.this,"Error Updating Image",Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        pd.dismiss();
                        Toast.makeText(ProfileActivity.this,"Some Error Occured",Toast.LENGTH_SHORT).show();
                    }
                }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode){
//            case CAMERA_REQUEST_CODE: {
//                if (grantResults.length > 0) {
//                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//                    if (cameraAccepted && writeStorageAccepted){
//                        pickFromCamera();
//                    } else {
//                        Toast.makeText(getApplicationContext(),"Please Enable Camera & Storage Permissions",Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//            break;
//            case STORAGE_REQUEST_CODE:{
//                if (grantResults.length > 0) {
//                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//                    if (writeStorageAccepted){
//                        pickFromGallery();
//                    } else {
//                        Toast.makeText(getApplicationContext(),"Please Enable Storage Permission",Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//            break;
//        }

        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
                        pickFromCamera();
                    }
                    else {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                                Toast.makeText(this, "You need to allow storage permissions.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                }
                break;
            case STORAGE_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
                        pickFromGallery();
                    }
                    else {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                Toast.makeText(this, "You need to allow storage permissions.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                }
                break;
        }



    }
    private void pickFromGallery() {
        Intent galleyIntent = new Intent(Intent.ACTION_PICK);
        galleyIntent.setType("image/*");
        startActivityForResult(galleyIntent,IMAGE_PICK_GALLERY_CODE);
    }
    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");

        image_uri = ProfileActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);

    }

}