package com.helloworld.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MyViewHolder> {
    private ArrayList<ChatMessageDetails> mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChatMessageAdapter(ArrayList<ChatMessageDetails> myDataset, Context ctx) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChatMessageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_room_message_list, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ChatMessageDetails chatMessageDetails = mDataset.get(position);
        holder.chatRoomMessage.setText(chatMessageDetails.Message);
        holder.ChatRoomUserName.setText(chatMessageDetails.firstname);
//        holder.chatRoomMessageDate.setText(chatMessageDetails.date);
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
        ImageButton ButtonMessageFavorites, ButtonMessageDelete;
        ImageView ProfileImageChatRoom;
        TextView ChatRoomUserName;
        TextView chatRoomMessage;
        TextView chatRoomMessageDate;
        public MyViewHolder(View view) {
            super(view);
            ProfileImageChatRoom = view.findViewById(R.id.ProfileImageChatRoom);
            ButtonMessageFavorites = view.findViewById(R.id.ButtonMessageFavorites);
            ButtonMessageDelete = view.findViewById(R.id.ButtonMessageDelete);
            ChatRoomUserName = view.findViewById(R.id.ChatRoomUserName);
            chatRoomMessage = view.findViewById(R.id.chatRoomMessage);
            chatRoomMessageDate = view.findViewById(R.id.chatRoomMessageDate);
        }
    }
}

