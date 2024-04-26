package com.example.beadando;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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


public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String S_KEY = MainActivity.class.getPackage().toString();
    private FirebaseAuth auth;


    EditText emailET;

    EditText codeET;
    private SharedPreferences preference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        emailET = findViewById(R.id.editTextEmail);
        codeET = findViewById(R.id.editTextPassword);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        auth = FirebaseAuth.getInstance();
        preference = getSharedPreferences(S_KEY,MODE_PRIVATE);
    }

    public void startHome(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        String stremail = emailET.getText().toString();
        String strcode = codeET.getText().toString();
        Log.i(LOG_TAG, "email: "+stremail+" jelszó: "+strcode);
        auth.signInWithEmailAndPassword(stremail,strcode).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(LOG_TAG,"Sikerült bejelentkezni!");
                    startHome();
                }else {
                    Log.d(LOG_TAG,"Mért nem sikerül semmi?");
                    Toast.makeText(MainActivity.this, "Nem sikerült bejelnetkezni: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preference.edit();
        editor.putString("email", emailET.getText().toString());
        editor.putString("jelszo", codeET.getText().toString());
        editor.apply();
    }

    public void regist(View view) {
        Intent intent = new Intent(this, RegistrionActivity.class);
        startActivity(intent);
    }
}