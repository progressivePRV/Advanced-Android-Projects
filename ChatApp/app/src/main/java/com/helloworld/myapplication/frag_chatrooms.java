package com.helloworld.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frag_chatrooms#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frag_chatrooms extends Fragment implements ChatRoomAdapter.InteractWithRecyclerView{

    private FirebaseFirestore db;
    private RecyclerView mainRecyclerView;
    private RecyclerView.Adapter mainAdapter;
    private RecyclerView.LayoutManager mainLayoutManager;
    ArrayList<String> globalChatRoomList = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public frag_chatrooms() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment frag_chatrooms.
     */
    // TODO: Rename and change types and number of parameters
    public static frag_chatrooms newInstance(String param1, String param2) {
        frag_chatrooms fragment = new frag_chatrooms();
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
        return inflater.inflate(R.layout.fragment_chatrooms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton chatRoomAdd = getView().findViewById(R.id.ButtonChatRoomAdd);
        chatRoomAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatRoomCreateActivity.class);
                startActivityForResult(intent, 100);
            }
        });

        db = FirebaseFirestore.getInstance();
        mainRecyclerView = (RecyclerView) getView().findViewById(R.id.chatRoomRecyclerView);
        mainLayoutManager = new LinearLayoutManager(getActivity());
        mainRecyclerView.setLayoutManager(mainLayoutManager);
        // specify an adapter
        mainAdapter = new ChatRoomAdapter(globalChatRoomList, frag_chatrooms.this);
        mainRecyclerView.setAdapter(mainAdapter);

        db.collection("ChatRoomList").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        globalChatRoomList.add(document.getId());
                    }
                    mainAdapter.notifyDataSetChanged();
                } else {
                    Log.d("demo", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        db = FirebaseFirestore.getInstance();
        //super.onActivityResult(requestCode, resultCode, data);
        //Here I will be getting the new chatRoom Name with a result code of 200.
        //Adding it in the firebase and snapshot is added such that recycler view will automatically update it.
        if(requestCode == 100 && resultCode == 200 && data!=null){
            final String chatRoomName = data.getExtras().getString("chatRoomName");
            Log.d("demo", chatRoomName);
            Map<String, Object> docData = new HashMap<>();
            db.collection("ChatRoomList").document(chatRoomName).set(docData)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d("demo", "Yes it is a success");
                            Toast.makeText(getActivity(), "ChatRoom successfully added", Toast.LENGTH_SHORT).show();
                            globalChatRoomList.add(chatRoomName);
                            mainAdapter.notifyDataSetChanged();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Some error occured in creating chatroom. Please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void getDetails(String chatRoom, int position) {
        Log.d("demo", "It is getting the chatRoom details to go to the next Activity");
        Toast.makeText(getActivity(), "Successfully selected the chatRoom", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
        intent.putExtra("chatRoomName",chatRoom);
        startActivity(intent);
    }
}