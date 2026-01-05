package com.example.version;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText EmailEdtxt, PasswordEdtxt;

    Button login;
    TextView register;

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
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences userInfo = getSharedPreferences("UserInfo", MODE_PRIVATE);
        String username = userInfo.getString("Username", "No name");
        String email = userInfo.getString("Email", "No email");
        String password = userInfo.getString("Password", "No password");

        mAuth = FirebaseAuth.getInstance();
        EmailEdtxt = findViewById(R.id.EmailEdtxt);
        PasswordEdtxt = findViewById(R.id.PasswordEdtxt);


        login = findViewById(R.id.LoginBtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uname = String.valueOf(EmailEdtxt.getText());
                String upass = String.valueOf(PasswordEdtxt.getText());

                if (uname.isEmpty() || upass.isEmpty()) {
                    Toast.makeText(Login.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

//                if(uname.equals(username) && upass.equals(password)){
//                    Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
//                    Intent i1 = new Intent(Login.this, DrawerActivity.class);
//                    startActivity(i1);
//                }
//                else{
//                    Toast.makeText(Login.this,"Invalid username or password",Toast.LENGTH_SHORT).show();
//                    return;
//                }

                mAuth.signInWithEmailAndPassword(uname, upass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                    Intent i1 = new Intent(getApplicationContext(), DrawerActivity.class);
                                    startActivity(i1);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.

                                    String error = task.getException().getMessage();
                                    Toast.makeText(Login.this, "Error: " + error, Toast.LENGTH_LONG).show();
                                    ;
                                }
                            }
                        });



            }
        });

        register = findViewById(R.id.registerTxt);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(Login.this, Signup.class);
                startActivity(i2);
            }
        });
    }
}