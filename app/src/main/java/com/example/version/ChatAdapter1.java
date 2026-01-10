package com.example.version;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatAdapter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Message> messageList;
    private final String currentUserId;

    // View Types
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    public ChatAdapter1(List<com.example.version.Message> messageList, String currentUserId) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getSenderId().equals(currentUserId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_sent_message, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_received_message, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        if (holder instanceof SentMessageViewHolder) {
            ((SentMessageViewHolder) holder).setData(message);
        } else {
            ((ReceivedMessageViewHolder) holder).setData(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    // ViewHolder for Sent Messages
    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;

        SentMessageViewHolder(View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
        }

        void setData(Message message) {
            textMessage.setText(message.getMessageText());
        }
    }

    // ViewHolder for Received Messages
    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage, textName;

        ReceivedMessageViewHolder(View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
            textName = itemView.findViewById(R.id.textName);
        }

        void setData(Message message) {
            textMessage.setText(message.getMessageText());
            textName.setText(message.getSenderName());
        }
    }
}