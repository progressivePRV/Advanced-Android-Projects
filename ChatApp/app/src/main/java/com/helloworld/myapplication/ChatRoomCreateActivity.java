package com.helloworld.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChatRoomCreateActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText chatRoomName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_create);
        db = FirebaseFirestore.getInstance();

        findViewById(R.id.newChatRoomCreateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatRoomName = findViewById(R.id.newChatRoomName);
                if(checkValidations(chatRoomName)){
                    db.collection("ChatRoomList").document(chatRoomName.getText().toString())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(!documentSnapshot.exists()){
                                        //Sending the new chatroomname to the chatRoom Activity so that we can add it to the Firebase there
                                        Intent intent = new Intent();
                                        intent.putExtra("chatRoomName",chatRoomName.getText().toString());
                                        setResult(200, intent);
                                        finish();
                                    }else{
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ChatRoomCreateActivity.this);
                                        builder1.setMessage("A ChatRoom with same name already exists, Please give a different ChatRoom name!");

                                        builder1.setPositiveButton(
                                                "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                        AlertDialog alert11 = builder1.create();
                                        alert11.show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }else{
                    Toast.makeText(ChatRoomCreateActivity.this, "Chat Room name cannot be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
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