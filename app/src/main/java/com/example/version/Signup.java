package com.example.version;
import android.content.pm.PackageManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Signup extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText edName, edEmail, edPassword;

    Button signup;
    TextView alrAcc;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent i1 = new Intent(getApplicationContext(), DrawerActivity.class);
            startActivity(i1);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        edName = findViewById(R.id.NameEdtxt);
        edEmail = findViewById(R.id.EmailEdtxt);
        edPassword = findViewById(R.id.PasswordEdtxt);

        signup = findViewById(R.id.SignupBtn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // REQUEST PERMISSION FOR ANDROID 13+
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(Signup.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Signup.this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
                        return; // Stop here so they can click Allow first
                    }
                }

                String username = String.valueOf(edName.getText());
                String email = String.valueOf(edEmail.getText());
                String password = String.valueOf(edPassword.getText());

                if(username.isEmpty() || email.isEmpty() || password.isEmpty()){
                    Toast.makeText(Signup.this,"Please fill all the fields",Toast.LENGTH_SHORT).show();
                    return;
                }

//                SharedPreferences userInfo = getSharedPreferences("UserInfo", MODE_PRIVATE);
//                SharedPreferences.Editor editor = userInfo.edit();
//                editor.putString("Username", username);
//                editor.putString("Email", email);
//                editor.putString("Password", password);
//                editor.apply();
//
//                Intent i1 = new Intent(Signup.this, DrawerActivity.class);
//                startActivity(i1);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    if (user != null) {
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(username)
                                                .build();

                                        // Attach a listener to the profile update task
                                        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> updateTask) {
                                                // ONLY trigger these once the name is officially saved
                                                sendLocalNotification(username);

                                                Toast.makeText(Signup.this, "Account Created.", Toast.LENGTH_SHORT).show();

                                                Intent i1 = new Intent(Signup.this, DrawerActivity.class);
                                                startActivity(i1);
                                                finish();
                                            }
                                        });
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    String error = task.getException().getMessage();
                                    Toast.makeText(Signup.this, "Error: " + error, Toast.LENGTH_LONG).show();;

                                }
                            }
                        });

            }
        });

        alrAcc = findViewById(R.id.alreadyAccountTxt);
        alrAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(Signup.this, Login.class);
                startActivity(i2);
            }
        });
    }

    // Helper method to show the welcome notification immediately
    private void sendLocalNotification(String name) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String channelId = "welcome_channel";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Welcome", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.versionlogo)
                .setContentTitle("Welcome to Version, " + name + "!")
                .setContentText("Your account has been created successfully. Start chatting!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);

        notificationManager.notify(1, builder.build());
    }
}