package com.example.beadando;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegistrionActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegistrionActivity.class.getName();

    EditText userET;
    EditText emailET;
    EditText codeEt;
    EditText recodeET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int reg_key = getIntent().getIntExtra("REG_KEY",0);
        if(reg_key != 7){
            finish();
        }

        userET = findViewById(R.id.editTextUsername);
        emailET = findViewById(R.id.editTextEmail);
        codeEt = findViewById(R.id.editTextPassword);
        recodeET = findViewById(R.id.editTextRepassword);


    }

    public void regist(View view) {
        String user = userET.getText().toString();
        String email = emailET.getText().toString();
        String code = codeEt.getText().toString();
        String recode = recodeET.getText().toString();

        if(!code.equals(recode)){
            Log.e(LOG_TAG,"Nem egyelő jelszavak");
            return;
        }

        Log.i(LOG_TAG,"user: "+user+", email: "+email+" jelszo: "+code+" rejeslzo: "+recode);
        // TODO regisztráció
    }

    public void back(View view) {
        finish();
    }
}