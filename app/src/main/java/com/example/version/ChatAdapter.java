package com.example.version;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    List<com.example.version.ChatMessage> messageList;

    public ChatAdapter(List<com.example.version.ChatMessage> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) { // User
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_user, parent, false);
        } else { // AI
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_ai, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        com.example.version.ChatMessage message = messageList.get(position);
        if (message.getSentBy().equals(com.example.version.ChatMessage.SENT_BY_USER)) {
            holder.userText.setText(message.getMessage());
        } else {
            holder.aiText.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() { return messageList.size(); }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getSentBy().equals(com.example.version.ChatMessage.SENT_BY_USER)) return 0;
        return 1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userText, aiText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userText = itemView.findViewById(R.id.userMessageText);
            aiText = itemView.findViewById(R.id.aiMessageText);
        }
    }
}