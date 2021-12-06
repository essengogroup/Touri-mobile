package com.touritouri.trtr.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.touritouri.trtr.R;
import com.touritouri.trtr.models.Site;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class ReservationActivity extends AppCompatActivity {

    private TextInputEditText firstName, name , email, phone, address;
    private Button reservationBtn;
    private ChipGroup chipsPrograms;

    private FirebaseFirestore firestore;
    private DocumentReference reference;
    private String collectionRefPath="reservations";

    private Site site;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        }

        site = getIntent().getParcelableExtra("site");

        firestore = FirebaseFirestore.getInstance();

        firstName = findViewById(R.id.resFirstName);
        name = findViewById(R.id.resName);
        email = findViewById(R.id.resEmail);
        phone = findViewById(R.id.resPhone);
        address = findViewById(R.id.resQuartier);
        chipsPrograms = findViewById(R.id.chipsPrograms);

        reservationBtn = findViewById(R.id.reservationBtn);

        dialog = new ProgressDialog(ReservationActivity.this);
        dialog.setMessage("Réservation en cours ...");
        dialog.setCancelable(false);

        reservationBtn.setOnClickListener(v -> {
            sendReservation();
        });

        Date date = new Date();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        Log.d("TAG", "onCreate: "+day+"/"+month+"/"+year);

        getDateVisite(day,month,year,site.getUid());

    }

    public void sendReservation(){
        String firstNameValue = firstName.getText().toString().trim().toLowerCase();
        String nameValue = name.getText().toString().trim().toLowerCase();
        String phoneValue = phone.getText().toString().trim().toLowerCase();
        String quartierValue = address.getText().toString().trim().toLowerCase();
        String emailValue = email.getText().toString().trim().toLowerCase();

        if( TextUtils.isEmpty(firstNameValue)){
            firstName.setError("ce champ est obligatoire");
            firstName.requestFocus();
            return;
        }

        if( TextUtils.isEmpty(nameValue)){
            name.setError("ce champ est obligatoire");
            name.requestFocus();
            return;
        }

        if( TextUtils.isEmpty(phoneValue)){
            phone.setError("ce champ est obligatoire");
            phone.requestFocus();
            return;
        }

        if( TextUtils.isEmpty(quartierValue)){
            address.setError("ce champ est obligatoire");
            address.requestFocus();
            return;
        }

        dialog.show();

        Map<String,Object> data = new HashMap<>();
        data.put("site_id",site.getUid());
        data.put("name",nameValue);
        data.put("firstname",firstNameValue);
        data.put("email",emailValue);
        data.put("phone",phoneValue);
        data.put("address",quartierValue);

        reference = firestore.collection(collectionRefPath).document(phoneValue);
        reference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                dialog.dismiss();

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ReservationActivity.this);

                builder.setTitle("Message");
                builder.setMessage("Votre demande de réservation du site "+site.getName()+" a bien été prise en compte,\nun mail vous sera envoyé à votre adresse mail ou par message");
                builder.setCancelable(false);
                builder.setPositiveButton("j'ai compris", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(ReservationActivity.this,AccueilActivity.class));
                    }
                });

                builder.show();
            }
        });
    }

    public void getDateVisite(int day , int month, int year,String site_id){
        //visites/12/2021/ ATFjp5DmpTMUUqiB6t1n

        DocumentReference reference = firestore
                .collection("visites")
                .document(String.valueOf(month))
                .collection(String.valueOf(year))
                .document(site_id);

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                        setCategoryChips((List<String>) document.get("date_visite"));
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    public void setCategoryChips(List<String> date_visite) {
        for (String category : date_visite) {
            Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.item_chip_category, null, false);
            mChip.setText(category);
            int paddingDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10,
                    getResources().getDisplayMetrics()
            );
            mChip.setPadding(paddingDp, 0, paddingDp, 0);
            mChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Log.d("TAG", "onCheckedChanged: "+compoundButton.isChecked() +"\n"+compoundButton.getText());
                }
            });
            chipsPrograms.addView(mChip);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                ReservationActivity.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}