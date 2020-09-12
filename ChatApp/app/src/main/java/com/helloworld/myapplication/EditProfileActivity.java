package com.helloworld.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    EditText editTextFirstName;
    EditText editTextLastName;
    EditText editTextGender;
    EditText editTextCity;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference mDatabase;
    ImageView profileImage;
    UserProfile user;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setTitle("Edit User Profile");

        if(getIntent()!=null && getIntent().getExtras()!=null && getIntent().getExtras().get("user")!=null){
            showProgressBarDialog("Loading...");
            user = (UserProfile) getIntent().getExtras().get("user");
            editTextFirstName = findViewById(R.id.editFirstName);
            editTextLastName = findViewById(R.id.editlastName);
            editTextGender = findViewById(R.id.editGender);
            editTextCity = findViewById(R.id.editCity);
            profileImage = findViewById(R.id.editProfileImage);

            storage = FirebaseStorage.getInstance();
            storageReference=storage.getReference();

            final StorageReference profileImageRef = storageReference.child(user.profileImage);

            profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri.toString()).into(profileImage);
                    editTextFirstName.setText(user.firstName);
                    editTextLastName.setText(user.lastName);
                    editTextGender.setText(user.gender);
                    editTextCity.setText(user.city);
                    hideProgressBarDialog();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    hideProgressBarDialog();
                    Log.d("demo","An error occured");
                    Toast.makeText(EditProfileActivity.this, "Failed to Load Profile", Toast.LENGTH_SHORT).show();
                }
            });

            findViewById(R.id.editButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkValidations(editTextFirstName) && checkValidations(editTextLastName) && checkValidations(editTextGender) && checkValidations(editTextCity)){
                        showProgressBarDialog("Updating...");
                        HashMap<String,Object> editUser = new HashMap<>();
                        editUser.put("firstName",editTextFirstName.getText().toString());
                        editUser.put("lastName",editTextLastName.getText().toString());
                        editUser.put("gender",editTextGender.getText().toString());
                        editUser.put("email",user.email);
                        editUser.put("city",editTextCity.getText().toString());
                        editUser.put("profileImage",user.profileImage);

                        mDatabase = FirebaseDatabase.getInstance().getReference("users");

                        mDatabase.child(user.uid).updateChildren(editUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(EditProfileActivity.this, "Profile edited successfully", Toast.LENGTH_SHORT).show();
                                    hideProgressBarDialog();
                                    finish();
                                }
                                else{
                                    Log.d("demo","Profile could not be updated");
                                    Toast.makeText(EditProfileActivity.this, "Failed to edit profile", Toast.LENGTH_SHORT).show();
                                    hideProgressBarDialog();
                                }
                            }
                        });
                    }
                }
            });

            findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        else{
            Log.d("demo","No user present");
            Toast.makeText(EditProfileActivity.this, "No user present", Toast.LENGTH_SHORT).show();
        }
    }

    //for showing the progress dialog
    public void showProgressBarDialog(String message)
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    //for hiding the progress dialog
    public void hideProgressBarDialog()
    {
        progressDialog.dismiss();
    }

    //For checking the empty strings
    public boolean checkValidations(EditText editText){
        if(editText.getText().toString().trim().equals("")){
            editText.setError("Cannot be empty");
            return false;
        }else{
            return true;
        }
    }
}