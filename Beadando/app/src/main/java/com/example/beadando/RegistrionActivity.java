package com.example.beadando;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Filterable;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistrionActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegistrionActivity.class.getName();
    private static final String S_KEY = RegistrionActivity.class.getPackage().toString();
    private SharedPreferences preference;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private CollectionReference userdata;


    EditText userET;
    EditText emailET;
    EditText codeEt;
    EditText recodeET;

    EditText phoneET;

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



        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userdata = firestore.collection("users");

        userET = findViewById(R.id.editTextUsername);
        emailET = findViewById(R.id.editTextEmail);
        codeEt = findViewById(R.id.editTextPassword);
        recodeET = findViewById(R.id.editTextRepassword);
        phoneET = findViewById(R.id.editTextPhone);
        preference = getSharedPreferences(S_KEY,MODE_PRIVATE);
        String email = preference.getString("email","");
        String jelszo = preference.getString("jelszo","");
        emailET.setText(email);
        codeEt.setText(jelszo);
        recodeET.setText(jelszo);


    }


    public void regist(View view) {
        String user = userET.getText().toString();
        String email = emailET.getText().toString();
        String code = codeEt.getText().toString();
        String recode = recodeET.getText().toString();
        String phone = phoneET.getText().toString();

        if(!code.equals(recode)){
            Log.e(LOG_TAG,"Nem egyelő jelszavak");
            return;
        }

        Log.i(LOG_TAG,"user: "+user+", email: "+email+" jelszo: "+code+" rejeslzo: "+recode);

        auth.createUserWithEmailAndPassword(email,code).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(LOG_TAG,"Új felhasználó létrejött!");
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    userdata.add(new User(id,user,phone))
                            .addOnSuccessListener(documentReference -> Log.d(LOG_TAG, "Új felhasználó jött létre"))
                            .addOnFailureListener(e -> Log.w(LOG_TAG, "Nem jött létre új felhasználó", e));
                    startHome();
                }else {
                    Log.d(LOG_TAG,"Mért nem sikerül semmi?");
                    Toast.makeText(RegistrionActivity.this, "Nem sikerült létrehozni az új felhasználót: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    public void startHome(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void back(View view) {
        finish();
    }
}