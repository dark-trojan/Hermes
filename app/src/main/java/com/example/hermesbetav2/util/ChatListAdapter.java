package com.example.hermesbetav2.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hermesbetav2.R;
import com.example.hermesbetav2.model.ChatModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter {

    private static int SENT_MESSAGE = 1;
    private static int RECEIVED_MESSAGE = 2;
    private Context context;
    private List<ChatModel> chatList;
    private FirebaseAuth auth;
    private FirebaseUser user;

    public ChatListAdapter(Context context, List<ChatModel> chatList) {
        this.context = context;
        this.chatList = chatList;
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if(viewType == SENT_MESSAGE){
            view = LayoutInflater.from(context).inflate(R.layout.my_message, parent, false);
            return new SentMessageHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.others_message, parent, false);
            return new ReceivedMessageHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatModel chatModel = chatList.get(position);

        int type = holder.getItemViewType();

        if(type == SENT_MESSAGE){
            ((SentMessageHolder) holder).bind(chatModel);
        } else if (type == RECEIVED_MESSAGE){
            ((ReceivedMessageHolder) holder).bind(chatModel);
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatModel chatModel = chatList.get(position);
        if (chatModel.getSender().equals(user.getDisplayName()))
            return SENT_MESSAGE;
        else
            return RECEIVED_MESSAGE;
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {

        private TextView sentMessage;
        private TextView sentTime;

        public SentMessageHolder(@NonNull View itemView) {
            super(itemView);
            sentMessage = itemView.findViewById(R.id.sentMessage);
            sentTime = itemView.findViewById(R.id.sentTime);
        }

        void bind(ChatModel chatModel){

            if(chatModel != null) {
                sentMessage.setText(chatModel.getMessageText());
                Long t=chatModel.getTimestamp().getSeconds();
                Date myDate = new Date(t*1000);
                sentTime.setText(myDate.toString());
            }
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {

        private TextView receivedMessage, receivedMessageSender, receivedTime;

        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            receivedMessage = itemView.findViewById(R.id.receivedMessage);
            receivedMessageSender = itemView.findViewById(R.id.receivedMessageSender);
            receivedTime = itemView.findViewById(R.id.receivedTime);
        }

        void bind(ChatModel chatModel){

            if(chatModel != null) {
                receivedMessageSender.setText(chatModel.getSender());
                receivedMessage.setText(chatModel.getMessageText());
                Long t=chatModel.getTimestamp().getSeconds();
                Date myDate = new Date(t*1000);
                receivedTime.setText(myDate.toString());

            }
        }
    }
}
