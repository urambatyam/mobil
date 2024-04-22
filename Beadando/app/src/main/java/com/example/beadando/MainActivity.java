package com.example.beadando;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final int REG_KEY = 7;


    EditText userET;

    EditText codeET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        userET = findViewById(R.id.editTextUsername);
        codeET = findViewById(R.id.editTextPassword);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void login(View view) {
        String struser = userET.getText().toString();
        String strcode = codeET.getText().toString();

        Log.i(LOG_TAG, "felhasználó: "+struser+" jelszó: "+strcode);
    }

    public void regist(View view) {
        Intent intent = new Intent(this, RegistrionActivity.class);
        intent.putExtra("REG_KEY",REG_KEY);
        startActivity(intent);
        // TODO
    }
}