package com.example.version;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Test1 extends AppCompatActivity {

    Button btnNext;
    EditText etname;
    EditText etage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnNext = findViewById(R.id.btnNext);
        etname = findViewById(R.id.edtxtName);
        etage = findViewById(R.id.edtxtAge);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = etname.getText().toString().trim();
                String userAge = etage.getText().toString();

                if(username.isEmpty() || userAge.isEmpty()){
                    Toast.makeText(Test1.this, "Please enter both name and age.", Toast.LENGTH_SHORT).show();
                    return;
                }


                int Age = Integer.parseInt(userAge);

                SharedPreferences sp = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                //write
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("Name", username);
                editor.putInt("Age", Age);
                editor.apply();

                Intent i = new Intent(Test1.this, Test2.class);
                startActivity(i);
            }
        });
    }
}