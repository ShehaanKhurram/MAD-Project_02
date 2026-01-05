package com.example.version;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserInfo extends AppCompatActivity {

    TextView name;
    TextView add;
    TextView age;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        name = findViewById(R.id.tvName);
        add = findViewById(R.id.tvAddress);
        age = findViewById(R.id.tvAge);

        SharedPreferences sp = getSharedPreferences("MyData", MODE_PRIVATE);
        String username = sp.getString("username", "Username not found");
        String address = sp.getString("address", "Address not found");
        int Age =  sp.getInt("Age", 0);

        name.setText("Name: " + username);
        add.setText("Address: " + address);
        age.setText("Age: " + Age);
        Toast.makeText(this, "User Information", Toast.LENGTH_SHORT).show();

    }
}