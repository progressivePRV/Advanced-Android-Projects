package com.helloworld.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ChatRoomActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String chatRoomName;
    private RecyclerView mainRecyclerView;
    private RecyclerView.Adapter mainAdapter;
    private RecyclerView.LayoutManager mainLayoutManager;
    ArrayList<ChatMessageDetails> chatMessageDetailsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        setTitle("Welcome to Chat Room");

        //temporary code for logout. Please remove while actual implementation
//        findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAuth = FirebaseAuth.getInstance();
//                mAuth.signOut();
//                Intent loginIntent = new Intent(ChatRoomActivity.this,MainActivity.class);
//                startActivity(loginIntent);
//            }
//        });
        db = FirebaseFirestore.getInstance();
        chatRoomName = getIntent().getExtras().getString("chatRoomName");

        //Adding snapshot listener to the firestore
        db.collection("ChatRoomMessages").document(chatRoomName).collection("Messages").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "listen:error", e);
                    return;
                }

                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Log.d("TAG", "New Msg: " + dc.getDocument().toObject(ChatMessageDetails.class));
                            ChatMessageDetails added = dc.getDocument().toObject(ChatMessageDetails.class);
                            chatMessageDetailsArrayList.add(dc.getDocument().toObject(ChatMessageDetails.class));
                            break;
                        case MODIFIED:
                            //For now I dont think there is any modification
                            break;
                        case REMOVED:
                            Log.d("TAG", "Removed Msg: " + dc.getDocument().toObject(ChatMessageDetails.class));
                            //Functionality to be completed yet
                            break;
                    }
                }

                mainAdapter.notifyDataSetChanged();
            }
        });

        mainRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewChatRoomMessages);
        mainLayoutManager = new LinearLayoutManager(ChatRoomActivity.this);
        mainRecyclerView.setLayoutManager(mainLayoutManager);
        // specify an adapter (see also next example)
        mainAdapter = new ChatMessageAdapter(chatMessageDetailsArrayList, ChatRoomActivity.this);
        mainRecyclerView.setAdapter(mainAdapter);


        findViewById(R.id.SendMessageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText enterMessageText = findViewById(R.id.enterMessageText);
                if(checkValidations(enterMessageText)){
                    //Once the profile is created in the firestore based on the UID then we can work on this part.
                    // For now keeping it like this.
                    final ChatMessageDetails chatMessageDetails = new ChatMessageDetails();
                    chatMessageDetails.isLiked = false;
                    chatMessageDetails.firstname = "";
                    chatMessageDetails.Message = enterMessageText.getText().toString();
                    chatMessageDetails.Uid = "";
                    db.collection("ChatRoomMessages").document(chatRoomName).collection("Messages").document()
                            .set(chatMessageDetails)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(ChatRoomActivity.this, "Message sent!", Toast.LENGTH_SHORT).show();
//                                    chatMessageDetailsArrayList.add(chatMessageDetails);
                                    mainAdapter.notifyDataSetChanged();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChatRoomActivity.this, "Some error occured. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(ChatRoomActivity.this, "Message cannot be empty!", Toast.LENGTH_SHORT).show();
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