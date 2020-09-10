package com.helloworld.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pub.devrel.easypermissions.EasyPermissions;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Chat Room SignUp");

        //For profile pic selection either from Camera or from Gallery
        imageView = findViewById(R.id.imageButton);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setTitle("Choose your profile picture");

                builder.setItems(options, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (options[item].equals("Take Photo")) {
                            Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, 0);

                        } else if (options[item].equals("Choose from Gallery")) {
                            //trying to get the permission for the profile picture. but it is not happening.
                            String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            if (EasyPermissions.hasPermissions(SignUpActivity.this, galleryPermissions)) {
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto , 1);
                            } else {
                                EasyPermissions.requestPermissions(SignUpActivity.this, "Access for storage",
                                        101, galleryPermissions);
                            }


                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.buttonSignupFirst).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting all the values for signup
                EditText fname = findViewById(R.id.editTextFname);
                EditText lname = findViewById(R.id.editTextLname);
                EditText email = findViewById(R.id.editTextEmail);
                EditText pass = findViewById(R.id.editTextChoosePassword);
                EditText rePass = findViewById(R.id.editTextRepeatPassword);
                EditText gender = findViewById(R.id.editTextGender);
                if(checkValidations(fname) && checkValidations(lname) &&
                        checkValidations(email) && checkEmailValidations(email)
                        && checkValidations(pass) && checkValidations(rePass) && checkValidations(gender)) {
                    String password = pass.getText().toString().trim();
                    String repeatPassword = rePass.getText().toString().trim();
                    String gen = gender.getText().toString();
                    if (password.equals(repeatPassword)) {
                        showProgressBarDialog();
                        //saving the authentication information and moving the activity from signup to chatroom.
                        mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), password)
                                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d("demo", "createUserWithEmail:success");
                                            Toast.makeText(SignUpActivity.this, "Signed Up Sucessfully!", Toast.LENGTH_LONG).show();
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            hideProgressBarDialog();
                                            Intent intent = new Intent(SignUpActivity.this, ChatRoomActivity.class);
                                            //sending userid to the next activity and based on user id we can fetch the data from the firebase.
                                            intent.putExtra("user", user.getUid());
                                            startActivityForResult(intent, 1000);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w("demo", "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(SignUpActivity.this, "Create user failed!" + task.getException(),
                                                    Toast.LENGTH_SHORT).show();
                                            hideProgressBarDialog();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(SignUpActivity.this, "Passwords and Repeat Passwords are not matching", Toast.LENGTH_SHORT).show();
                    }
                    hideProgressBarDialog();
                }
            }
        });

        //Cancel Button
        //Actions: If cancel is pressed the signup screen finishes and goes to the login screen
        findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //OnActivityResult
    //Actions: From Signup it would directly go to chat room and if the user clicks on signout from the chatroom,
    //the login screen should be visible to the user instead of signup. So OnActivityResult is finish is called here.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                Log.d("demo",picturePath);
                                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }

        if(requestCode == 1000 && resultCode == 2000){
            finish();
        }
    }

    //for showing the progress dialog
    public void showProgressBarDialog()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    //for hiding the progress dialog
    public void hideProgressBarDialog()
    {
        progressDialog.dismiss();
    }

    //Regex for checking email validations
    public boolean checkEmailValidations(EditText editText)
    {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(!editText.getText().toString().trim().matches(emailPattern))
        {
            editText.setError("Invalid Email");
            return false;
        }
        else
        {
            return true;
        }
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