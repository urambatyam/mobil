package com.example.beadando;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class HomeActivity extends AppCompatActivity {

    private static final String LOG_TAG = HomeActivity.class.getName();
    private static final int MODIFY_KEY = 7;

    private FirebaseUser user;
    private FirebaseFirestore firestore;
    //private CollectionReference userdata;
    //private DocumentReference surveysdata;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        user = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();


        if(user != null){
            Log.d(LOG_TAG,"Be van jellentkezve");
            username = "felhasználónév";
            firestore.collection("surveys")
                    .whereEqualTo("userid", uid)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        Button buttonFill = findViewById(R.id.buttonfill);
                        Button buttonModify = findViewById(R.id.buttonmodify);
                        Button buttonDelete = findViewById(R.id.buttondelete);
                        // Ellenőrizd, hogy van-e találat
                        if (!queryDocumentSnapshots.isEmpty()) {
                            buttonFill.setVisibility(View.GONE);
                            buttonModify.setVisibility(View.VISIBLE);
                            buttonDelete.setVisibility(View.VISIBLE);
                        } else {
                            buttonFill.setVisibility(View.VISIBLE);
                            buttonModify.setVisibility(View.GONE);
                            buttonDelete.setVisibility(View.GONE);
                            // Ha nincs találat, akkor itt kezeld le a hibát vagy tedd meg a szükséges intézkedéseket
                            Log.d(LOG_TAG, "Nincs megfelelő dokumentum a felhasználóhoz");
                        }
                    })
                    .addOnFailureListener(e -> Log.e(LOG_TAG, "Hiba történt a dokumentum lekérése során", e));
        }else {
            Log.d(LOG_TAG,"Nincs Bejellentkezve");
            finish();
        }


        //userdata = firestore.collection("users");


    }

    public void fill(View view) {
        Intent intent = new Intent(this, SurveyActivity.class);
        startActivity(intent);
    }

    public void modify(View view) {
        Intent intent = new Intent(this, SurveyActivity.class);
        intent.putExtra("MODIFY_KEY", MODIFY_KEY);
        startActivity(intent);
    }

    public void delete(View view) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestore.collection("surveys")
                .whereEqualTo("userid", uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        document.getReference().delete()
                                .addOnSuccessListener(aVoid -> Log.d(LOG_TAG, "A kérdőívet sikeresen töröltük!"))
                                .addOnFailureListener(e -> Log.w(LOG_TAG, "Nem sikerült törölni a kérdőívet", e));
                    }
                })
                .addOnFailureListener(e -> Log.w(LOG_TAG, "Nem sikerült lekérni a dokumentumokat", e));
        Button buttonFill = findViewById(R.id.buttonfill);
        Button buttonModify = findViewById(R.id.buttonmodify);
        Button buttonDelete = findViewById(R.id.buttondelete);
        buttonFill.setVisibility(View.GONE);
        buttonModify.setVisibility(View.VISIBLE);
        buttonDelete.setVisibility(View.VISIBLE);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        finish();
    }
}