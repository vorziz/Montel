package com.montel.Montel.ui.inputdata;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.AutoSizeableTextView;

import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.montel.Montel.MainActivity;
import com.montel.Montel.R;
import com.montel.Montel.model.Customer;
import com.montel.Montel.model.CustomerNonRegist;
import com.montel.Montel.model.CustomerPascabayar;
import com.montel.Montel.model.CustomerPrabayar;
import com.montel.Montel.ui.MapsActivity;
import com.montel.Montel.ui.detailmarker.DetailActivity;
import com.montel.Montel.ui.maps.MapActivity;
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

public class InputData extends AppCompatActivity {



    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    public EditText etIDPEL, etName, etAddress,etViolation,etLoc, etNoMeter,etRates,etPowerType,etPhone,etNoReport, etPetugas;
    Spinner classSpinner, divSpinner;

    DatabaseReference postRef;

    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;

    private String customerId;
    public Button btnSave;
    public String TAG;
    public ImageView ivGetLoc, ivHolder1,ivHolder2,ivHolder3;
    String lat, longi, uid;
    String newString,selectedClass, selectedDiv,newString2, newsEdit;
    String image1URL, image2URL,image3URL;
    String dateCreate = "18/02/2020";
    String dateUpdate = "18/02/2020";

    //Image handle
    private static final int PICK_IMG = 1;
    private ArrayList<Uri> ImageList = new ArrayList<Uri>();
    private int uploads = 0;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private AppCompatAutoCompleteTextView acTVPelanggaran, acTVGolongan, acTVDaya;
    private TextInputLayout textInputLayoutGolongan, textInputLayoutPelanggaran, textInputLayoutDaya, textInputLayoutNoMeter, textInputLayoutIDPEL;
    String[] arr = { "P1", "P2","P3","P4","PRR","TS"};
    String[] arrGolongan = { "Sosial", "Rumah Tangga","Bisnis","Industri","Pemerintah"};
    String[] arrDaya = { "450", "900","1300","2200","3500","4400","5500","7700","11000","6600","10600","13200","16500","23000","33000",
                        "41500","53000","66000","82500","105000","131000","171000","197000","329000","414000"};

    String typeU ;
    String golonganU ;
    String dayaU ;
    String namaU ;
    String alamatU ;
    String tlpU ;
    String koordinatU ;
    String pelanggaranU ;
    String jenis_pelanggaranU ;
    String no_berita_acaraU ;
    String petugasU ;
    String idpeU;
    String nometerU;
    String unitPelayanan;
    @BindView(R.id.pb_holder_1) ProgressBar progressBarHolder1;
    @BindView(R.id.pb_holder_2) ProgressBar progressBarHolder2;
    @BindView(R.id.pb_holder_3) ProgressBar progressBarHolder3;
    @BindView(R.id.et_unit_pelayanan) EditText unit_pelayanan;




    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("STRING_I_NEED");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("STRING_I_NEED");

        }


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString2= null;
            } else {
                newString2= extras.getString("STRING_I_NEED2");
            }
        } else {
            newString2= (String) savedInstanceState.getSerializable("STRING_I_NEED2");
        }

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newsEdit= null;
            } else {
                newsEdit= extras.getString("edit");
            }
        }



        setContentView(R.layout.activity_input_data);

        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        etIDPEL = (EditText) findViewById(R.id.et_idpel);
        etNoMeter = (EditText) findViewById(R.id.et_nometer);
        //etPowerType = (EditText) findViewById(R.id.et_powerType);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etNoReport = (EditText) findViewById(R.id.et_noreport);
        etName = (EditText) findViewById(R.id.et_name);
        etAddress = (EditText) findViewById(R.id.et_address);
        etViolation =  (EditText) findViewById(R.id.et_punishment);
        btnSave = (Button) findViewById(R.id.btnSave);
        etPetugas = (EditText) findViewById(R.id.et_petugas);
//        ivGetLoc = (ImageView) findViewById(R.id.iv_getloc);
        etLoc =  (EditText) findViewById(R.id.et_location);
        ivHolder1 = (ImageView) findViewById(R.id.ivHolder1);
        ivHolder2 = (ImageView) findViewById(R.id.ivHolder2);
        ivHolder3 = (ImageView) findViewById(R.id.ivHolder3);
//        classSpinner = (Spinner) findViewById(R.id.classSpinnerCategory);
//        divSpinner = (Spinner) findViewById(R.id.divSpinnerCategory);
        acTVPelanggaran = (AppCompatAutoCompleteTextView) findViewById(R.id.acTVPelanggaran);
        acTVGolongan = (AppCompatAutoCompleteTextView) findViewById(R.id.acTVGolongan);
        acTVDaya = (AppCompatAutoCompleteTextView) findViewById(R.id.acTVDaya);
        textInputLayoutDaya = (TextInputLayout) findViewById(R.id.tilDaya);
        textInputLayoutGolongan = (TextInputLayout) findViewById(R.id.tilGolongan);
        textInputLayoutPelanggaran = (TextInputLayout) findViewById(R.id.tilJenisPelanggaran);
        textInputLayoutIDPEL = (TextInputLayout) findViewById(R.id.tilIDPEL);
        textInputLayoutNoMeter = (TextInputLayout) findViewById(R.id.tilNoMeter);







        // update toolbar title
        getSupportActionBar().setTitle("Input Data "+newString);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
       // uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("customers");

        // store app title to 'app_title' node
        mFirebaseInstance.getReference("app_title").setValue("Realtime Database");

        //Fill

        //AutoCompleted
        ArrayAdapter<String> adapterPelanggaran = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, arr);
        ArrayAdapter<String> adapterGolongan = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, arrGolongan);
        ArrayAdapter<String> adapterDaya = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, arrDaya);

        acTVPelanggaran.setThreshold(2);
        acTVPelanggaran.setAdapter(adapterPelanggaran);
        acTVGolongan.setThreshold(2);
        acTVGolongan.setAdapter(adapterGolongan);
        acTVDaya.setThreshold(2);
        acTVDaya.setAdapter(adapterDaya);



       // spinerDO();


        ivHolder1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InputData.this, ImageSelectActivity.class);
                intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, false);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                //intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
                startActivityForResult(intent, 1213);
            }
        });

        ivHolder2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InputData.this, ImageSelectActivity.class);
                intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, false);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                //intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
                startActivityForResult(intent, 1214);
            }
        });

        ivHolder3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InputData.this, ImageSelectActivity.class);
                intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, false);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                //intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
                startActivityForResult(intent, 1215);
            }
        });


        //Check IF ELSE
        if (newString!= null && newString.matches("Prabayar")){
            textInputLayoutIDPEL.setVisibility(View.GONE);
            textInputLayoutNoMeter.setVisibility(View.VISIBLE);


            doPrabayar(newString);

        }else if (newString!= null && newString.matches("Pascabayar")){
            textInputLayoutIDPEL.setVisibility(View.VISIBLE);
            textInputLayoutNoMeter.setVisibility(View.GONE);


            doPascabayar(newString);
        }else if (newString!= null && newString.matches("Non IDPEL")){
            textInputLayoutIDPEL.setVisibility(View.VISIBLE);
            textInputLayoutNoMeter.setVisibility(View.GONE);


            doNonRegister(newString);
        }


        if (newString2!= null){
            etLoc.setText(newString2);
        }

        if (newsEdit!= null){
            doEdit(newsEdit);
        }

    }

    private void doEdit(String newsEdit) {
        btnSave.setText("Update");


        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("customers");


        mFirebaseDatabase.orderByChild("key").equalTo(newsEdit).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                   //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                   Customer customers = snapshot.getValue(Customer.class);


                   etName.setText(customers.getName());
                   etAddress.setText(customers.address);
                   acTVDaya.setText(customers.getPowerType());
                   acTVGolongan.setText(customers.powerClass);

                   etPhone.setText(customers.getPhone());
                   Picasso.get().load(customers.getImageURL1()).into(ivHolder1);
                   etNoReport.setText(customers.getNoReport());
                   etViolation.setText(customers.getViolation());
                   etPetugas.setText(customers.getEmploye());
                   etLoc.setText(customers.getCoordinat());
                   acTVPelanggaran.setText(customers.getType_violation());
                   typeU = customers.getType();


//                   idpeU = customers.getIdpel();
//                   String nometer = customers.getNoMeter();

//Check IF ELSE
                   if (typeU != null && typeU.matches("Prabayar")) {
                       textInputLayoutIDPEL.setVisibility(View.GONE);
                       textInputLayoutNoMeter.setVisibility(View.VISIBLE);
                       btnSave.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {

                               doUpdate();

                           }

                       });


                   } else if (typeU != null && typeU.matches("Pascabayar")) {
                       textInputLayoutIDPEL.setVisibility(View.VISIBLE);
                       textInputLayoutNoMeter.setVisibility(View.GONE);
                       btnSave.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {

                              doUpdate();

                           }

                       });


                   } else if (typeU != null && typeU.matches("Non IDPEL")) {
                       textInputLayoutIDPEL.setVisibility(View.VISIBLE);
                       textInputLayoutNoMeter.setVisibility(View.GONE);

                       btnSave.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {

                               doUpdate();

                           }

                       });


                   }



               }



                //Toast.makeText(InputData.this, "Data Berhasil Dimuat", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(InputData.this, "Data Gagal Dimuat", Toast.LENGTH_LONG).show();
                Log.e("MyListActivity", databaseError.getDetails() + " " + databaseError.getMessage());
            }


        });












    }

    private void doUpdate() {
        golonganU = acTVGolongan.getText().toString();
        dayaU = acTVDaya.getText().toString();
        namaU = etName.getText().toString();
        alamatU = etAddress.getText().toString();
        tlpU = etPhone.getText().toString();
        koordinatU = etLoc.getText().toString();
        pelanggaranU = etViolation.getText().toString();
        jenis_pelanggaranU = acTVPelanggaran.getText().toString();
        no_berita_acaraU = etNoReport.getText().toString();
        petugasU = etPetugas.getText().toString();


        if (!TextUtils.isEmpty(namaU))
            mFirebaseDatabase.child(newsEdit).child("name").setValue(namaU);
        if (!TextUtils.isEmpty(alamatU))
            mFirebaseDatabase.child(newsEdit).child("address").setValue(alamatU);
        if (!TextUtils.isEmpty(pelanggaranU))
            mFirebaseDatabase.child(newsEdit).child("violation").setValue(pelanggaranU);
        if (!TextUtils.isEmpty(no_berita_acaraU))
            mFirebaseDatabase.child(newsEdit).child("noReport").setValue(no_berita_acaraU);
        if (!TextUtils.isEmpty(jenis_pelanggaranU))
            mFirebaseDatabase.child(newsEdit).child("type_violation").setValue(jenis_pelanggaranU);
        if (!TextUtils.isEmpty(petugasU))
            mFirebaseDatabase.child(newsEdit).child("employe").setValue(petugasU);
        if (!TextUtils.isEmpty(tlpU))
            mFirebaseDatabase.child(newsEdit).child("phone").setValue(tlpU);
        if (!TextUtils.isEmpty(koordinatU))
            mFirebaseDatabase.child(newsEdit).child("coordinat").setValue(koordinatU);
        if (!TextUtils.isEmpty(golonganU))
            mFirebaseDatabase.child(newsEdit).child("powerClass").setValue(golonganU);
        if (!TextUtils.isEmpty(dayaU))
            mFirebaseDatabase.child(newsEdit).child("powerType").setValue(dayaU);


        Toast.makeText(InputData.this, "Updated", Toast.LENGTH_SHORT).show();
        onBackPressed();
        finish();
    }



    //Non Register
    private void doNonRegister(final String STR) {

        // Save / update the user
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nama = etName.getText().toString();
                final String golongan = acTVGolongan.getText().toString();
                final String daya = acTVDaya.getText().toString();
                final String alamat = etAddress.getText().toString();
                final String no_hp = etPhone.getText().toString() ;
                final String pelanggaran = etViolation.getText().toString();
                final String no_berita_acara = etNoReport.getText().toString() ;
                final String koordinat = etLoc.getText().toString();
                final String type = STR;
                final String petugas = etPetugas.getText().toString();
                final String jenis_pelanggaran = acTVPelanggaran.getText().toString();
                final  String noIDPEL = etIDPEL.getText().toString();
                final String unitPelayanan = unit_pelayanan.getText().toString();


                if (noIDPEL.length()==0){
                    etIDPEL.requestFocus();
                    etIDPEL.setError("Please input IDPEL/No Meter");
                }else if (golongan.length()==0) {
                    acTVGolongan.requestFocus();
                    acTVGolongan.setError("Please select Golongan");
                }else if (daya.length()==0){
                    acTVDaya.requestFocus();
                    acTVDaya.setError("Please select Daya");
                }else if (nama.length()==0){
                    etName.requestFocus();
                    etName.setError("Please input Nama");
                }else if (alamat.length()==0){
                    etAddress.requestFocus();
                    etAddress.setError("Please input Alamat");
                }else if (koordinat.length()==0){
                    etLoc.requestFocus();
                    etLoc.setText("Please input Kordinat");
                }else if (no_hp.length()==0){
                    etPhone.requestFocus();
                    etPhone.setError("Please input No Tlp");
                }else if (no_berita_acara.length()==0) {
                    etNoReport.requestFocus();
                    etNoReport.setError("Please input No Berita Acara");
                }else if (jenis_pelanggaran.length()==0){
                    acTVPelanggaran.requestFocus();
                    acTVPelanggaran.setError("Please select Jenis Pelanggaran");
                }else if (pelanggaran.length()==0){
                    etViolation.requestFocus();
                    etViolation.setError("Please input Pelanggaran");
                }else if (petugas.length()==0) {
                    etPetugas.requestFocus();
                    etPetugas.setError("Please input Petugas");
                }else if (petugas.length()==0) {
                    unit_pelayanan.requestFocus();
                    unit_pelayanan.setError("Please input Unit Pelayanan");
                }else {

                    // Check for already existed userId
                    if (TextUtils.isEmpty(customerId)) {

                        createCustomerNonRegister(noIDPEL,golongan, daya, nama,
                                alamat,koordinat,no_hp,no_berita_acara,jenis_pelanggaran,pelanggaran,petugas,unitPelayanan,type,dateCreate,dateUpdate);

                        Toast.makeText(InputData.this,"Avail : "+customerId,Toast.LENGTH_SHORT).show();
                    } else {
                        // updateUser(name, email);
                        Toast.makeText(InputData.this,"Not Avail : "+customerId,Toast.LENGTH_SHORT).show();

                    }

                }








            }
        });
    }


    //Pascabayar
    private void doPascabayar(final String STR) {

        // Save / update the user
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nama = etName.getText().toString();
                final String golongan = acTVGolongan.getText().toString();
                final String daya = acTVDaya.getText().toString();
                final String alamat = etAddress.getText().toString();
                final String no_hp = etPhone.getText().toString() ;
                final String pelanggaran = etViolation.getText().toString();
                final String no_berita_acara = etNoReport.getText().toString() ;
                final String idpel = etIDPEL.getText().toString();
                final String koordinat = etLoc.getText().toString();
                final String type = STR;
                final String petugas = etPetugas.getText().toString();
                final String jenis_pelanggaran = acTVPelanggaran.getText().toString();
                final String unitPelayanan = unit_pelayanan.getText().toString();



                if (idpel.length()==0) {
                    etIDPEL.requestFocus();
                    etIDPEL.setError("Please input IDPELr");
                }else if (golongan.length()==0) {
                    acTVGolongan.requestFocus();
                    acTVGolongan.setError("Please select Golongan");
                }else if (daya.length()==0){
                    acTVDaya.requestFocus();
                    acTVDaya.setError("Please select Daya");
                }else if (nama.length()==0){
                    etName.requestFocus();
                    etName.setError("Please input Nama");
                }else if (alamat.length()==0){
                    etAddress.requestFocus();
                    etAddress.setError("Please input Alamat");
                }else if (koordinat.length()==0){
                    etLoc.requestFocus();
                    etLoc.setText("Please input Kordinat");
                }else if (no_hp.length()==0){
                    etPhone.requestFocus();
                    etPhone.setError("Please input No Tlp");
                }else if (no_berita_acara.length()==0) {
                    etNoReport.requestFocus();
                    etNoReport.setError("Please input No Berita Acara");
                }else if (jenis_pelanggaran.length()==0){
                    acTVPelanggaran.requestFocus();
                    acTVPelanggaran.setError("Please select Jenis Pelanggaran");
                }else if (pelanggaran.length()==0){
                    etViolation.requestFocus();
                    etViolation.setError("Please input Pelanggaran");
                }else if (petugas.length()==0) {
                    etPetugas.requestFocus();
                    etPetugas.setError("Please input Petugas");

                }else if (petugas.length()==0) {
                    unit_pelayanan.requestFocus();
                    unit_pelayanan.setError("Please input Unit Pelayanan");


                }else {

                    // Check for already existed userId
                    if (TextUtils.isEmpty(customerId)) {

                        createCustomerPascabayar(idpel, golongan, daya, nama,
                                alamat,koordinat,no_hp,no_berita_acara,jenis_pelanggaran,pelanggaran,petugas,unitPelayanan,image1URL,image2URL,image3URL,type,dateCreate,dateUpdate);

                        Toast.makeText(InputData.this,"Avail : "+customerId,Toast.LENGTH_SHORT).show();
                    } else {
                        // updateUser(name, email);
                        Toast.makeText(InputData.this,"Not Avail : "+customerId,Toast.LENGTH_SHORT).show();

                    }

                }









            }
        });
    }

    //Prabayar
    private void doPrabayar(final String STR) {
        // Save / update the user
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String nama = etName.getText().toString();
                final String golongan = acTVGolongan.getText().toString();
                final String daya = acTVDaya.getText().toString();
                final String alamat = etAddress.getText().toString();
                final String no_hp = etPhone.getText().toString() ;
                final String pelanggaran = etViolation.getText().toString();
                final String no_berita_acara = etNoReport.getText().toString() ;
                final String no_meter = etNoMeter.getText().toString();
                final String koordinat = etLoc.getText().toString();
                final String type = STR;
                final String petugas = etPetugas.getText().toString();
                final String jenis_pelanggaran = acTVPelanggaran.getText().toString();
                final String unitPelayanan = unit_pelayanan.getText().toString();



                if (no_meter.length()==0) {
                    etNoMeter.requestFocus();
                    etNoMeter.setError("Please input No Meter");
                }else if (golongan.length()==0) {
                    acTVGolongan.requestFocus();
                    acTVGolongan.setError("Please select Golongan");
                }else if (daya.length()==0){
                    acTVDaya.requestFocus();
                    acTVDaya.setError("Please select Daya");
                }else if (nama.length()==0){
                    etName.requestFocus();
                    etName.setError("Please input Nama");
                }else if (alamat.length()==0){
                    etAddress.requestFocus();
                    etAddress.setError("Please input Alamat");
                }else if (koordinat.length()==0){
                    etLoc.requestFocus();
                    etLoc.setText("Please input Kordinat");
                }else if (no_hp.length()==0){
                    etPhone.requestFocus();
                    etPhone.setError("Please input No Tlp");
                }else if (no_berita_acara.length()==0) {
                    etNoReport.requestFocus();
                    etNoReport.setError("Please input No Berita Acara");
                }else if (jenis_pelanggaran.length()==0){
                    acTVPelanggaran.requestFocus();
                    acTVPelanggaran.setError("Please select Jenis Pelanggaran");
                }else if (pelanggaran.length()==0){
                    etViolation.requestFocus();
                    etViolation.setError("Please input Pelanggaran");
                }else if (petugas.length()==0) {
                    etPetugas.requestFocus();
                    etPetugas.setError("Please input Petugas");


                }else if (petugas.length()==0) {
                    unit_pelayanan.requestFocus();
                    unit_pelayanan.setError("Please input Unit Pelayanan");

                }else {

                    // Check for already existed userId
                    if (TextUtils.isEmpty(customerId)) {

                        createCustomerPrabayar(no_meter, golongan, daya, nama,
                                alamat,koordinat,no_hp,no_berita_acara,jenis_pelanggaran,pelanggaran,petugas,unitPelayanan,image1URL,image2URL,image3URL,type,dateCreate,dateUpdate);

                        Toast.makeText(InputData.this,"Avail : "+customerId,Toast.LENGTH_SHORT).show();
                    } else {
                        // updateUser(name, email);
                        Toast.makeText(InputData.this,"Not Avail : "+customerId,Toast.LENGTH_SHORT).show();

                    }

                }







            }
        });
    }

    private void spinerDO() {
        // Class Spinner implementing onItemSelectedListener
        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // do something upon option selection


                String selectedClass = parent.getItemAtPosition(position).toString();
                switch (selectedClass)
                {
                    case "S - SOSIAL":
                        // assigning div item list defined in XML to the div Spinner
                        divSpinner.setAdapter(new ArrayAdapter<String>(InputData.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                getResources().getStringArray(R.array.type_subcategory_rates_sosial)));
                        break;

                    case "R - RUMAH TANGGA":
                        divSpinner.setAdapter(new ArrayAdapter<String>(InputData.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                getResources().getStringArray(R.array.type_subcategory_rates_rumahtangga)));
                        break;

                    case "B - BISNIS":
                        divSpinner.setAdapter(new ArrayAdapter<String>(InputData.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                getResources().getStringArray(R.array.type_subcategory_rates_bisnis)));
                        break;

                    case "I - INDUSTRI":
                        divSpinner.setAdapter(new ArrayAdapter<String>(InputData.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                getResources().getStringArray(R.array.type_subcategory_rates_industri)));
                        break;

                    case "P - PEMERINTAHAN":
                        divSpinner.setAdapter(new ArrayAdapter<String>(InputData.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                getResources().getStringArray(R.array.type_subcategory_rates_pemerintahan)));
                        break;
                }

                //set divSpinner Visibility to Visible
                divSpinner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // can leave this empty
            }
        });

        // Div Spinner implementing onItemSelectedListener
        divSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // do something upon option selection

                selectedDiv = parent.getItemAtPosition(position).toString();
                /*
                    Now that we have both values, lets create a Toast to
                    show the values on screen
                */
                Toast.makeText(InputData.this, "\n Class: \t " + selectedClass +
                        "\n Div: \t" + selectedDiv, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // can leave this empty
            }

        });
    }


    /**
     * Creating new user node under 'users'
     */
    private void createCustomerPrabayar(
                                        String no_meter,
                                        String golongan,
                                        String daya,
                                        String nama,
                                        String alamat,
                                        String koordinat,
                                        String no_hp,
                                        String no_berita_acara,
                                        String jenis_pelanggaran,
                                        String pelanggaran,
                                        String petugas,
                                        String unitPelayanan,
                                        String image1,
                                        String image2,
                                        String image3,
                                        String type,
                                        String dateCreate,
                                        String dateUpdate) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(customerId)) {
            customerId = mFirebaseDatabase.push().getKey();
        }

        Bitmap selectedImage = BitmapFactory.decodeFile(image1URL);

        uploadFile(selectedImage,customerId);

        CustomerPrabayar customer = new CustomerPrabayar(customerId,no_meter, golongan, daya, nama,
                alamat,koordinat,no_hp,no_berita_acara,jenis_pelanggaran,pelanggaran,petugas,unitPelayanan,type,dateCreate,dateUpdate);

        mFirebaseDatabase.child(customerId).setValue(customer);

        addUserChangeListener();
    }


    /**
     * Creating new user node under 'users'
     */
    private void createCustomerPascabayar(
                                          String idpel,
                                          String golongan,
                                          String daya,
                                          String nama,
                                          String alamat,
                                          String koordinat,
                                          String no_hp,
                                          String no_berita_acara,
                                          String jenis_pelanggaran,
                                          String pelanggaran,
                                          String petugas,
                                          String unitPelayanan,
                                          String image1,
                                          String image2,
                                          String image3,
                                          String type,
                                          String dateCreate,
                                          String dateUpdate) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(customerId)) {
            customerId = mFirebaseDatabase.push().getKey();
        }

        Bitmap selectedImage = BitmapFactory.decodeFile(image1URL);

        uploadFile(selectedImage,customerId);

        CustomerPascabayar customer = new CustomerPascabayar(customerId,idpel, golongan, daya, nama,
                alamat,koordinat,no_hp,no_berita_acara,jenis_pelanggaran,pelanggaran,petugas,unitPelayanan,type,dateCreate,dateUpdate);

        mFirebaseDatabase.child(customerId).setValue(customer);

        addUserChangeListener();
    }




    /**
     * Creating new user node under 'users'
     */
    private void createCustomerNonRegister(
                                        String idpel,
                                        String golongan,
                                        String daya,
                                        String nama,
                                        String alamat,
                                        String koordinat,
                                        String no_hp,
                                        String no_berita_acara,
                                        String jenis_pelanggaran,
                                        String pelanggaran,
                                        String petugas,
                                        String unitPelayanan,
                                        String type,
                                        String dateCreate,
                                        String dateUpdate) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(customerId)) {
            customerId = mFirebaseDatabase.push().getKey();
        }

        Bitmap selectedImage = BitmapFactory.decodeFile(image1URL);

        uploadFile(selectedImage,customerId);

        CustomerNonRegist customer = new CustomerNonRegist(customerId,idpel,golongan, daya, nama,
                alamat,koordinat,no_hp,no_berita_acara,jenis_pelanggaran,pelanggaran,petugas,unitPelayanan,type,dateCreate,dateUpdate);

        mFirebaseDatabase.child(customerId).setValue(customer);

        addUserChangeListener();
    }

    /**
     * User data change listener
     */
    private void addUserChangeListener() {
        // User data change listener
        mFirebaseDatabase.child(customerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Customer customer = dataSnapshot.getValue(Customer.class);

                // Check for null
                if (customer == null) {
                   // Log.e(T, "User data is null!");
                    return;
                }

                //Log.e(TAG, "User data is changed!" + user.name + ", " + user.email);

                // Display newly updated name and email
                ///txtDetails.setText(user.name + ", " + user.email);


                // clear edit text
                etIDPEL.setText("");
                etName.setText("");
                etAddress.setText("");
                etLoc.setText("");
                etNoMeter.setText("");
                acTVDaya.setText("");
                acTVGolongan.setText("");
                acTVPelanggaran.setText("");
                etPetugas.setText("");
                etNoReport.setText("");
                etViolation.setText("");
                //etPowerType.setText("");
                etPhone.setText("");

//                Intent intent = new Intent(InputData.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                finish();

                //toggleButton();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }



    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {

                                    lat = location.getLatitude()+"";
                                    longi = location.getLongitude()+"";

                                   // etLoc.setText(lat+" , "+longi);

                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            lat = mLastLocation.getLatitude()+"";
            longi = mLastLocation.getLongitude()+"";

           // etLoc.setText(lat+" , "+longi);
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1213 && resultCode == Activity.RESULT_OK) {
            String filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
            Bitmap selectedImage = BitmapFactory.decodeFile(filePath);

            image1URL = filePath;


                ivHolder1.setImageBitmap(selectedImage);
        }else if (requestCode == 1214 && resultCode == Activity.RESULT_OK) {
            String filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
            Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
            image2URL = filePath;

            ivHolder2.setImageBitmap(selectedImage);
        }else if (requestCode == 1215 && resultCode == Activity.RESULT_OK) {
            String filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
            Bitmap selectedImage = BitmapFactory.decodeFile(filePath);

            image3URL = filePath;
            ivHolder3.setImageBitmap(selectedImage);
        }
    }




    private void uploadFile(Bitmap bitmap, final String customerId) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();

        final StorageReference ImagesRef = storageRef.child("images/"+customerId+".jpg");


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        final UploadTask uploadTask = ImagesRef.putBytes(data);



        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.i("whatTheFuck:",exception.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.i("problem", task.getException().toString());
                        }

                        return ImagesRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("customers").child(customerId);

                            Log.i("seeThisUri", downloadUri.toString());// This is the one you should store

                            ref.child("imageURL1").setValue(downloadUri.toString());


                        } else {
                            Log.i("wentWrong","downloadUri failure");
                        }
                    }
                });
            }
        });

    }




}
