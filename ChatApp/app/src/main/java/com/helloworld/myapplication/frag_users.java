package com.helloworld.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frag_users#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frag_users extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "okay";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public frag_users() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment frag_users.
     */
    // TODO: Rename and change types and number of parameters
    public static frag_users newInstance(String param1, String param2) {
        frag_users fragment = new frag_users();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users");
    ArrayList<User> users = new ArrayList<>();
    RecyclerView rv;
    RecyclerView.Adapter rvAdapter;
    RecyclerView.LayoutManager rvLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Log.d(TAG, "onCreate: frag user called");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d(TAG, "onDataChange: called from value listner");
                //String value = dataSnapshot.getValue(String.class);
                //Log.d(TAG, "Value is: " + value);
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    User u  = postSnapshot.getValue(User.class);
                    users.add(u);
                    Log.d(TAG, postSnapshot.toString());
                }
                rvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d(TAG, "Failed to read value.", error.toException());
            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        Log.d(TAG, "onCreateView: frag_users called");

        rv = view.findViewById(R.id.rv_container_for_users);
        rv.setHasFixedSize(true);
        rvLayoutManager =  new LinearLayoutManager(getContext());
        rv.setLayoutManager(rvLayoutManager);
        rvAdapter =  new rvAdapterForUsersList(getActivity(),users);
        rv.setAdapter(rvAdapter);
        return view;
    }
}