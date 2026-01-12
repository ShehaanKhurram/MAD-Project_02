package com.example.version; // Ensure this matches your project package!

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Chatbot extends AppCompatActivity {


    FirebaseAuth mAuth;
    FirebaseUser user;
    RecyclerView recyclerView;
    EditText messageEditText;
    ImageButton sendBtn;
    List<ChatMessage> messageList;
    ChatAdapter chatAdapter;

    GenerativeModelFutures model;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user != null) {
            // This is the "Key" to the user's private folder
            String currentUserID = user.getUid();
        } else {
            // Handle the case where no one is logged in (redirect to Login)
        }

        // 1. Initialize Gemini Model
        // REPLACE "YOUR_API_KEY" with the key you got from Google AI Studio
        GenerativeModel gm = new GenerativeModel("gemini-2.5-flash-lite", "AIzaSyByBUWzdBBa4s3LGbBnb3RmDOcF9iJHGwY");
        model = GenerativeModelFutures.from(gm);

        // 2. Setup UI
        recyclerView = findViewById(R.id.chatRecyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        sendBtn = findViewById(R.id.sendBtn);

        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sendBtn.setOnClickListener(v -> {
            String question = messageEditText.getText().toString().trim();
            if(!question.isEmpty()){
                addToChat(question, ChatMessage.SENT_BY_USER);
                messageEditText.setText("");

                // Add a "Typing..." placeholder
                addToChat("Version is thinking...", ChatMessage.SENT_BY_BOT);

                callGemini(question);
            }
        });

        MobileAds.initialize(this, initializationStatus -> {});
        loadInterstitialAd();
    }

    void addToChat(String message, String sentBy) {
        runOnUiThread(() -> {
            messageList.add(new ChatMessage(message, sentBy));
            chatAdapter.notifyItemInserted(messageList.size() - 1);
            recyclerView.smoothScrollToPosition(chatAdapter.getItemCount());
        });
    }

    void callGemini(String question) {
        String systemContext = "You are AI Version, an assistant for the 'Version' app. Keep answers short.";
        String finalPrompt = systemContext + "\nUser: " + question;

        Content content = new Content.Builder().addText(finalPrompt).build();
        Executor executor = Executors.newSingleThreadExecutor();
        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);

        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                // ALWAYS use runOnUiThread for anything that touches the UI
                runOnUiThread(() -> {
                    // 1. Remove "Thinking..." message
                    if (!messageList.isEmpty()) {
                        messageList.remove(messageList.size() - 1);
                        chatAdapter.notifyItemRemoved(messageList.size());
                    }

                    // 2. Add actual AI Response
                    String aiResponse = result.getText();
                    if (aiResponse != null) {
                        addToChat(aiResponse, ChatMessage.SENT_BY_BOT);
                    } else {
                        addToChat("I couldn't process that. Please try again.", ChatMessage.SENT_BY_BOT);
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
                runOnUiThread(() -> {
                    // Remove "Thinking..." message on failure too
                    if (!messageList.isEmpty()) {
                        messageList.remove(messageList.size() - 1);
                        chatAdapter.notifyItemRemoved(messageList.size());
                    }
                    addToChat("Error: " + t.getMessage(), ChatMessage.SENT_BY_BOT);
                });
            }
        }, executor);
    }

    private void loadInterstitialAd(){
        AdRequest adRequest = new AdRequest.Builder().build();

        //load ad
        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712",adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                Log.d("AdMob", "Ad Loaded Successfully");

                // SHOW THE AD IMMEDIATELY AS SOON AS IT ARRIVES
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(Chatbot.this);
                }
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
            }
        });

    }

}