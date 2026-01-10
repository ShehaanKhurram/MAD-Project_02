package com.example.version;

import android.os.Bundle;
import com.example.version.Message;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupChat extends AppCompatActivity {

    private DatabaseReference groupMsgRef;
    private FirebaseAuth mAuth;
    private String currentUserId, currentUserName;

    private EditText messageInput;
    private ImageButton sendBtn;
    private RecyclerView chatRecyclerView;
    private List<com.example.version.Message> messageList;
    private ChatAdapter1 chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_group_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //1.
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        currentUserName = mAuth.getCurrentUser().getDisplayName();

        groupMsgRef = FirebaseDatabase.getInstance().getReference().child("Groups").child("GlobalChat").child("messages");

        // 2. Bind XML Views
        messageInput = findViewById(R.id.groupMessageInput);
        sendBtn = findViewById(R.id.groupSendBtn);
        chatRecyclerView = findViewById(R.id.groupChatRecyclerView);

        // 3. Setup RecyclerView
        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter1(messageList, currentUserId);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);


        sendBtn.setOnClickListener(v -> {
            String text = messageInput.getText().toString().trim();
            if (!text.isEmpty()) {
                sendMessageToFirebase(text);
            }
        });

        listenForMessages();
    }

    private void sendMessageToFirebase(String text) {
        // Generate a unique key for this message
        String messageKey = groupMsgRef.push().getKey();

        // Get current time
        long timestamp = System.currentTimeMillis();

        // Create message object (using the Message class from Step 1)
        Message message = new Message(currentUserId, currentUserName, text, timestamp);

        if (messageKey != null) {
            groupMsgRef.child(messageKey).setValue(message)
                    .addOnSuccessListener(aVoid -> {
                        // Clear input after sending
                        messageInput.setText("");
                    })
                    .addOnFailureListener(e -> {
                        // Handle error
                        Toast.makeText(GroupChat.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void listenForMessages() {
        groupMsgRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear(); // Clear to prevent duplicates on refresh
                for (DataSnapshot data : snapshot.getChildren()) {
                    Message message = data.getValue(Message.class);
                    if (message != null) {
                        messageList.add(message);
                    }
                }
                // Notify adapter and scroll to the bottom
                chatAdapter.notifyDataSetChanged();
                if (!messageList.isEmpty()) {
                    chatRecyclerView.smoothScrollToPosition(messageList.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GroupChat.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}