package com.helloworld.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ViewersListActivity extends AppCompatActivity {

    private RecyclerView mainRecyclerView;
    private RecyclerView.Adapter mainAdapter;
    private RecyclerView.LayoutManager mainLayoutManager;
    ArrayList<UserProfile> userArrayList = new ArrayList<>();
    FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewers_list);

        mainRecyclerView = (RecyclerView) findViewById(R.id.viewersListRecyclerView);
        mainLayoutManager = new LinearLayoutManager(ViewersListActivity.this);
        mainRecyclerView.setLayoutManager(mainLayoutManager);
        // specify an adapter (see also next example)
        mainAdapter = new ViewersListAdapter(userArrayList, ViewersListActivity.this);
        mainRecyclerView.setAdapter(mainAdapter);

        String chatRoomName = getIntent().getExtras().getString("chatRoomName");
        Log.d("demo",chatRoomName);

        db = FirebaseFirestore.getInstance();

        //Adding snapshot listener to the firestore
        db.collection("ChatRoomList").document(chatRoomName)
                .collection("CurrentViewers").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                            Log.d("TAG", "New Msg: " + dc.getDocument().getString("firstName"));
                            UserProfile user = new UserProfile(dc.getDocument().getString("firstName"),
                                    dc.getDocument().getString("lastName"),
                                    dc.getDocument().getString("gender"),
                                    dc.getDocument().getString("email"),
                                    dc.getDocument().getString("city"),
                                    dc.getDocument().getString("profileImage"),
                                    dc.getDocument().getString("uid"));
                            userArrayList.add(user);
                            break;
                        case MODIFIED:
                            break;
                        case REMOVED:
                            //Functionality to be completed yet
                            break;
                    }
                }
                mainAdapter.notifyDataSetChanged();
            }
        });
    }
}