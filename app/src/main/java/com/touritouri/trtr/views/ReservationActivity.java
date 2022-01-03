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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

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
import com.touritouri.trtr.R;
import com.touritouri.trtr.models.Site;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class ReservationActivity extends AppCompatActivity {

    private TextInputEditText firstName, name , email, phone, address,nbreDePersonne,dateViste;
    private Button reservationBtn;
    private ChipGroup chipsPrograms;
    private Chip mChip;

    private FirebaseFirestore firestore;
    private CollectionReference reference;
    private String collectionRefPath="reservations";

    private Site site;
    private ProgressDialog loadingDialog;
    private List<String> mDate_visites=new ArrayList<>();
    private StringBuffer currentDate =new StringBuffer();
    private CheckBox checkBox ;

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
        nbreDePersonne = findViewById(R.id.nbreDePersonne);
        dateViste = findViewById(R.id.resDate);
        checkBox = findViewById(R.id.confidentialite);

        loadingDialog = new ProgressDialog(ReservationActivity.this);
        loadingDialog.setMessage("Réservation en cours ...");
        loadingDialog.setCancelable(false);

        Date date = new Date();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        currentDate.append(day);
        currentDate.append("/");
        currentDate.append(month);
        currentDate.append("/");
        currentDate.append(year);

        Log.d("TAG", "onCreate: "+currentDate.toString());


        getAllDateVisite(day,month,year,site.getUid());

        checkBox.setOnClickListener(v->dialogConfidentialite());

        reservationBtn.setOnClickListener(v -> {
            Log.d("TAG", "onCreate: "+dateViste.getText().toString().trim().toLowerCase());
            boolean checkValue = checkBox.isChecked();
            if (checkValue){
                sendReservation(String.valueOf(year));
            }else{
                Toast.makeText(ReservationActivity.this, "veuillez accepter", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void sendReservation(String year){

        String firstNameValue = firstName.getText().toString().trim().toLowerCase();
        String nameValue = name.getText().toString().trim().toLowerCase();
        String phoneValue = phone.getText().toString().trim().toLowerCase();
        String quartierValue = address.getText().toString().trim().toLowerCase();
        String emailValue = email.getText().toString().trim().toLowerCase();
        String nbreDePersonneValue = nbreDePersonne.getText().toString().trim().toLowerCase();
        String dateValue = dateViste.getText().toString().trim().toLowerCase();

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

        if( TextUtils.isEmpty(nbreDePersonneValue)){
            nbreDePersonne.setError("ce champ est obligatoire");
            nbreDePersonne.requestFocus();
            return;
        }


        if (!TextUtils.isEmpty(dateValue)){

            mDate_visites.add(dateValue);
        }


        loadingDialog.show();

        Log.d("TAG", "sendReservation: "+mDate_visites.size());

        Map<String,Object> data = new HashMap<>();
        data.put("site_id",site.getUid());
        data.put("name",nameValue);
        data.put("firstname",firstNameValue);
        data.put("email",emailValue);
        data.put("phone",phoneValue);
        data.put("address",quartierValue);
        data.put("nbreDePersonne",nbreDePersonneValue);
        data.put("date_visites",mDate_visites);

        reference = firestore.collection(collectionRefPath).document(phoneValue).collection(year);
        reference.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                loadingDialog.dismiss();

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

    public void getAllDateVisite(int day , int month, int year,String site_id){
        //visites/12/2021/ ATFjp5DmpTMUUqiB6t1n
        //DocumentReference reference = firestore.document("/visites/11/2021/ ATFjp5DmpTMUUqiB6t1n");

        DocumentReference reference = firestore.document("/visites/"+month+"/"+year+"/ "+ site_id);

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

            if (compareToDay(currentDate.toString(),category) <= 0) {
                Log.i("app", "Date1 is before Date2 or Date1 is equal to Date2");

                mChip = (Chip) this.getLayoutInflater().inflate(R.layout.item_chip_category, null, false);
                mChip.setText(category);
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                mChip.setPadding(paddingDp, 0, paddingDp, 0);
                mChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        Log.d("TAG", "onCheckedChanged: " + compoundButton.isChecked());

                        if (!compoundButton.isChecked()) {
                            compoundButton.setTextColor(getResources().getColor(R.color.black));
                            if (mDate_visites.contains(compoundButton.getText().toString())) {
                                mDate_visites.remove(compoundButton.getText().toString());
                            }
                        } else {
                            compoundButton.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                            mDate_visites.add(compoundButton.getText().toString());
                        }

                        for (String s : mDate_visites) {
                            Log.d("TAG", "setCategoryChips: " + s);
                        }
                    }
                });
                chipsPrograms.addView(mChip);
            }


        }
    }

    public int compareToDay(String sDate1, String sDate2) {
        if (sDate1 == null || sDate2 == null) {
            return 0;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date1= null;
        Date date2= null;

        try {
            date1 = sdf.parse(sDate1);
            date2=sdf.parse(sDate2);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sdf.format(date1).compareTo(sdf.format(date2));
    }

    public void dialogConfidentialite(){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ReservationActivity.this);
        builder.setTitle("Politique de confidentialité");
        builder.setMessage(getResources().getString(R.string.confidentialite));
        builder.setPositiveButton("j'ai compris", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
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