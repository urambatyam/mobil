package com.example.beadando;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class SurveyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = SurveyActivity.class.getName();
    private FirebaseUser user;

    private FirebaseFirestore firestore;
    private CollectionReference surveysdata;
    private NotificationHandler nh;


    RadioGroup question1RG;
    RadioGroup question2RG;
    Spinner majorsSP;
    RadioGroup question4RG;
    RadioGroup question5RG;
    CheckBox question6aCB;
    CheckBox question6bCB;
    CheckBox question6cCB;
    CheckBox question6dCB;
    RadioGroup question7RG;
    RadioGroup question8RG;
    CheckBox question9aCB;
    CheckBox question9bCB;
    CheckBox question9cCB;
    CheckBox question9dCB;
    RadioGroup question10RG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_survey);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(SurveyActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(SurveyActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS},101);
            }
        }
        firestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            int mode = getIntent().getIntExtra("MODIFY_KEY",0);
            Button buttonsave = findViewById(R.id.buttonsave);
            Button buttonsend = findViewById(R.id.buttonsend);
            if(mode == 7){
                Log.d(LOG_TAG,"Modosítani kell");
                buttonsave.setVisibility(View.VISIBLE);
                buttonsend.setVisibility(View.GONE);
                ini();
            }else {
                buttonsave.setVisibility(View.GONE);
                buttonsend.setVisibility(View.VISIBLE);
            }
            Log.d(LOG_TAG,"Be van jellentkezve");

        }else {
            Log.d(LOG_TAG,"Nincs Bejellentkezve");
            finish();
        }


        surveysdata = firestore.collection("surveys");
        question1RG = findViewById(R.id.first);
        question2RG = findViewById(R.id.second);
        majorsSP = findViewById(R.id.spinnermajor3);
        majorsSP.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.majors, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        majorsSP.setAdapter(adapter);
        question4RG = findViewById(R.id.fourth);
        question5RG = findViewById(R.id.fifth);
        question6aCB = findViewById(R.id.checkbox6a);
        question6bCB = findViewById(R.id.checkbox6b);
        question6cCB = findViewById(R.id.checkbox6c);
        question6dCB = findViewById(R.id.checkbox6d);
        question7RG = findViewById(R.id.seventh);
        question8RG = findViewById(R.id.eighth);
        question9aCB = findViewById(R.id.checkbox9a);
        question9bCB = findViewById(R.id.checkbox9b);
        question9cCB = findViewById(R.id.checkbox9c);
        question9dCB = findViewById(R.id.checkbox9d);
        question10RG = findViewById(R.id.tenth);
        nh = new NotificationHandler(this);
    }
    public void ini(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestore.collection("surveys")
                .whereEqualTo("userid", uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    kerdoiv kerdoivValaszok = new kerdoiv();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        kerdoivValaszok = documentSnapshot.toObject(kerdoiv.class);
                    }
                        //1
                        switch (kerdoivValaszok.getElso()){
                            case "Nő":
                                question1RG.check(R.id.radioButton1a);
                                break;
                            case "Férfi":
                                question1RG.check(R.id.radioButton1b);
                                break;
                        }
                        //2
                        switch (kerdoivValaszok.getMasodik()){
                            case "Első":
                                question2RG.check(R.id.radioButton2a);
                                break;
                            case "Második":
                                question2RG.check(R.id.radioButton2b);
                                break;
                            case "Harmadik":
                                question2RG.check(R.id.radioButton2c);
                                break;
                            case "Negyedik":
                                question2RG.check(R.id.radioButton2d);
                                break;
                            case "Ötödik":
                                question2RG.check(R.id.radioButton2e);
                                break;
                            case "Több...":
                                question2RG.check(R.id.radioButton2f);
                                break;
                        }
                        //3
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.majors, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        majorsSP.setAdapter(adapter);
                        int spinnerPosition = adapter.getPosition(kerdoivValaszok.getHarmadik());
                        majorsSP.setSelection(spinnerPosition);
                        //4
                        switch (kerdoivValaszok.getNegyedik()){
                            case "Mindig bejárok.":
                                question4RG.check(R.id.radioButton4a);
                                break;
                            case "Néha bejárok.":
                                question4RG.check(R.id.radioButton4b);
                                break;
                            case "Csak az első pár héten járokbe.":
                                question4RG.check(R.id.radioButton4c);
                                break;
                            case "Soha nem járokbe.":
                                question4RG.check(R.id.radioButton4d);
                                break;
                        }
                        //5
                        switch (kerdoivValaszok.getOtodik()){
                            case "Mindent teljesen megértek a legaprób részletekig.":
                                question5RG.check(R.id.radioButton5a);
                                break;
                            case "Nagy körvonalkban felfogom de részleteiben már nem.":
                                question5RG.check(R.id.radioButton5b);
                                break;
                            case "Szinte semmit.":
                                question5RG.check(R.id.radioButton5c);
                                break;
                            case "Nem tudom mert nem járokbe.":
                                question5RG.check(R.id.radioButton5d);
                                break;
                        }
                        //6
                        String[] tem = kerdoivValaszok.getHatodik().split(",");

                        for (String selectedcb6:tem) {
                            if("Az előadásokra való bejárás elég felkészülés nekem.".equals(selectedcb6)){
                                question6aCB.setChecked(true);
                            }
                            if("Elolvasom otthon az előadás diáit.".equals(selectedcb6)){
                                question6bCB.setChecked(true);
                            }
                            if("Elolvasom/megnézem az előadáshoz feltöltött segédanyagokat.".equals(selectedcb6)){
                                question6cCB.setChecked(true);
                            }
                            if("Forumokon olvasok utána (pl.: Discord)".equals(selectedcb6)){
                                question6dCB.setChecked(true);
                            }
                        }
                        //7
                        switch (kerdoivValaszok.getHetedik()){
                            case "Az előadás hossza. Nem tudok másfélórán keresztül összpontosítani.":
                                question7RG.check(R.id.radioButton7a);
                                break;
                            case "A segédanyagok (pl.: videók vagy jegyzet) hiánya.":
                                question7RG.check(R.id.radioButton7b);
                                break;
                            case "Feleslegesnem érzem az anyagot és ezért nem bírom megtanulni.":
                                question7RG.check(R.id.radioButton7c);
                                break;
                            case "Lusta vagyok.":
                                question7RG.check(R.id.radioButton7d);
                                break;
                            case "Túl hosszú a tananyag.":
                                question7RG.check(R.id.radioButton7e);
                                break;
                        }
                        //8
                        switch (kerdoivValaszok.getNyolcadik()){
                            case "Mindent teljesen megértek a legaprób részletek ig.":
                                question8RG.check(R.id.radioButton8a);
                                break;
                            case "Nagy körvonalkban felfogom de részleteiben már nem.":
                                question8RG.check(R.id.radioButton8b);
                                break;
                            case "Szinte semmit.":
                                question8RG.check(R.id.radioButton8c);
                                break;
                            case "Nem tudom mert nem járokbe.":
                                question8RG.check(R.id.radioButton8d);
                                break;
                        }
                        //9
                        String[] tem2 = kerdoivValaszok.getKilencedik().split(",");

                        for (String selectedcb9:tem2) {
                            if("A gyakorlatokra való bejárás elég felkészülés nekem.".equals(selectedcb9)){
                                question9aCB.setChecked(true);
                            }
                            if("Otthon gyakorlok.".equals(selectedcb9)){
                                question9bCB.setChecked(true);
                            }
                            if("Elolvasom/megnézem az gyakorlathoz feltöltött segédanyagokat.".equals(selectedcb9)){
                                question9cCB.setChecked(true);
                            }
                            if("Forumokon olvasok utána (pl.: Discord)".equals(selectedcb9)){
                                question9dCB.setChecked(true);
                            }
                        }
                        //10
                        switch (kerdoivValaszok.getTizedik()){
                            case "A gyakorlat hossza. Túlsok minden hagzik el túl rövid idő alatt nem tudom követni.":
                                question10RG.check(R.id.radioButton10a);
                                break;
                            case "A segédanyagok (pl.: videók vagy jegyzet) hiánya.":
                                question10RG.check(R.id.radioButton10b);
                                break;
                            case "Feleslegesnem érzem az anyagot és ezért nem bírom megtanulni.":
                                question10RG.check(R.id.radioButton10c);
                                break;
                            case "Lusta vagyok.":
                                question10RG.check(R.id.radioButton10d);
                                break;
                            case "Túl hosszú a tananyag.":
                                question10RG.check(R.id.radioButton10e);
                                break;
                        }
                })
                .addOnFailureListener(e -> Log.e(LOG_TAG, "Hiba történt a dokumentum lekérése során", e));
    }

    public void cancel(View view) {
        finish();
    }

    public kerdoiv pre(){
        RadioButton r = question1RG.findViewById(question1RG.getCheckedRadioButtonId());
        String egy = r.getText().toString();
        r = question2RG.findViewById(question2RG.getCheckedRadioButtonId());
        String ketto = r.getText().toString();
        String harom = majorsSP.getSelectedItem().toString();
        r = question4RG.findViewById(question4RG.getCheckedRadioButtonId());
        String negy = r.getText().toString();
        r = question5RG.findViewById(question5RG.getCheckedRadioButtonId());
        String ot = r.getText().toString();
        String hat = "";
        if (question6aCB.isChecked()) {
            hat += question6aCB.getText().toString() + ",";
        }
        if (question6bCB.isChecked()) {
            hat += question6bCB.getText().toString() + ",";
        }
        if (question6cCB.isChecked()) {
            hat += question6cCB.getText().toString() + ",";
        }
        if (question6dCB.isChecked()) {
            hat += question6dCB.getText().toString() + ",";
        }
        r = question7RG.findViewById(question7RG.getCheckedRadioButtonId());
        String het = r.getText().toString();
        r = question8RG.findViewById(question8RG.getCheckedRadioButtonId());
        String nyolc = r.getText().toString();
        String kilenc = "";
        if (question9aCB.isChecked()) {
            kilenc += question9aCB.getText().toString() + ",";
        }
        if (question9bCB.isChecked()) {
            kilenc += question9bCB.getText().toString() + ",";
        }
        if (question9cCB.isChecked()) {
            kilenc += question9cCB.getText().toString() + ",";
        }
        if (question9dCB.isChecked()) {
            kilenc += question9dCB.getText().toString() + ",";
        }
        r = question10RG.findViewById(question10RG.getCheckedRadioButtonId());
        String tiz = r.getText().toString();
        Log.d(LOG_TAG,"preparálás");
        Log.i(LOG_TAG, " 1. "+egy+"\n 2. "+ketto+"\n 3. "+harom+"\n 4. "+negy+"\n 5. "+ot+"\n 6. "+hat+"\n 7. "+het+"\n 8. "+nyolc+"\n 9. "+kilenc+"\n 10. "+tiz);
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return new kerdoiv(userid,egy,ketto,harom,negy,ot,hat,het,nyolc,kilenc,tiz);
    }

    public void setkerdoiv(View view) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestore.collection("surveys")
                .whereEqualTo("userid", uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        DocumentReference docRef = documentSnapshot.getReference();
                        docRef.set(pre())
                                .addOnSuccessListener(aVoid -> {
                                    Log.d(LOG_TAG, "A kérdőív frissült!");
                                    finish(); // A metódus befejezése a sikeres frissítés után
                                })
                                .addOnFailureListener(e -> Log.w(LOG_TAG, "Nem sikerült az update", e));
                    }
                })
                .addOnFailureListener(e -> Log.w(LOG_TAG, "Nem sikerült az update előtti lekérdezés", e));
    }


    public void send(View view) {
        surveysdata.add(pre()).addOnSuccessListener(documentReference -> Log.d(LOG_TAG, "Új kérdőív jött létre")).addOnFailureListener(e -> Log.w(LOG_TAG, "Nem jött létre új kérdőív", e));
        nh.send("Köszönjűk hogy kitöltöted a Kérdőívünket!");
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String major = parent.getItemAtPosition(position).toString();
            Log.i(LOG_TAG,major);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
