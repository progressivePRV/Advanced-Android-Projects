package com.helloworld.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewersListAdapter extends RecyclerView.Adapter<ViewersListAdapter.MyViewHolder> {
    private ArrayList<UserProfile> mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ViewersListAdapter(ArrayList<UserProfile> myDataset, Context ctx) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewersListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewers_list, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        UserProfile user = mDataset.get(position);
        holder.viewersListName.setText(user.firstName);
        Picasso.get()
                .load(user.profileImage)
                .into(holder.viewersListImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        TextView viewersListName;
        ImageView viewersListImage;
        ConstraintLayout scheduleConstraintLayout;
        public MyViewHolder(View view) {
            super(view);
            viewersListName = view.findViewById(R.id.viewersListName);
            viewersListImage = view.findViewById(R.id.viewersListImage);
        }

    }
}