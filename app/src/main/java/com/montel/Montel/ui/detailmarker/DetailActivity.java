package com.montel.Montel.ui.detailmarker;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.montel.Montel.MainActivity;
import com.montel.Montel.R;
import com.montel.Montel.model.Customer;
import com.montel.Montel.ui.home.HomeFragment;
import com.montel.Montel.ui.inputdata.InputData;
import com.montel.Montel.utils.ListAdapter;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public String tipe;
    public String noMeter;
    public String idpel;
    public String nama;
    public String daya;
    public String golongan;
    public String jenisPelanggaran;
    public String alamat;
    public String no_tlp;
    public String koordinat;
    public String petugas;
    public String noLaporan;
    public String imageURL1;
    public String imageURL2;
    public String imageURL3;
    public String pelanggaran;
    public String dateCreate;
    public String dateUpdate;
    public String key;
    public String lati, lngi;


    @BindView(R.id.tvNamaDetail) TextView tvNama;
    @BindView(R.id.tvAlamatDetail) TextView tvAlamat;
    @BindView(R.id.tvTlpDetail) TextView tvTlp;
    @BindView(R.id.tvDayaDetail)TextView tvDaya;
    @BindView(R.id.tvGolonganDetail)TextView tvGolongan;
    @BindView(R.id.tvJenisPelanggaranDetail) TextView tvJenisPelanggaran;
    @BindView(R.id.tvIDPELDetail) TextView tvIDPEL;
    @BindView(R.id.imageViewHeader) PhotoView imageView;
    @BindView(R.id.tvNoLaporan) TextView tvNoLaporan;
    @BindView(R.id.tvPelanggaran) TextView tvPelanggaran;
    @BindView(R.id.tvPetugas) TextView tvPetugas;
    @BindView(R.id.tvTypeGolongan) TextView tvTypeGolongan;
    @BindView(R.id.pb_holder_header) ProgressBar progressBarHeader;
    @BindView(R.id.fabNavigation)FloatingActionButton fabNav;
    @BindView(R.id.tvUnitPelayanan) TextView tvUnitPelayanan;


    private DatabaseReference reference;
    Handler handler;
    ImagePopup imagePopup;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String url = "URLKey";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        changeColor(R.color.colorPrimary);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

       getSupportActionBar().setTitle("");

        String sDATA = getIntent().getStringExtra("list_as_string_customer");


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            key  = extras.getString("key");
            //The key argument here must match that used in the other activity
        }

        if (key != null){
            doView(key);
        }


    }

    private void doView(String key) {
        reference = FirebaseDatabase.getInstance().getReference("customers");



            reference.orderByChild("key").equalTo(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                        Customer customers = snapshot.getValue(Customer.class);

                        tvNama.setText(customers.getName());
                        tvAlamat.setText(customers.getAddress());
                        tvDaya.setText(customers.getPowerType()+" Kwh");
                        tvGolongan.setText(customers.getPowerClass());
                        tvIDPEL.setText(customers.getNoIDPL());
                        tvJenisPelanggaran.setText(customers.getType_violation());
                        tvTlp.setText(customers.getPhone());
                        tvUnitPelayanan.setText(customers.getUnitPelayanan());

                        String kordinat = customers.getCoordinat();
                        String[] itemCoordinat = kordinat.split(",");
                        lati = itemCoordinat[0];
                        lngi = itemCoordinat[1];

                        Picasso.get().load(customers.getImageURL1()).into(imageView);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showwImage();
                            }
                        });


                        SharedPreferences sharedpreferences = getSharedPreferences(mypreference,Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(url, customers.getImageURL1());
                        editor.apply();


                        if (imageView== null){
                            progressBarHeader.setVisibility(View.VISIBLE);
                        }else {
                            progressBarHeader.setVisibility(View.GONE);
                        }

                        tvNoLaporan.setText(customers.getNoReport());
                        tvPelanggaran.setText(customers.getViolation());
                        tvPetugas.setText(customers.getEmploye());
                        tvTypeGolongan.setText(customers.getType());
                    }



                    Toast.makeText(DetailActivity.this, "Data Berhasil Dimuat", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(DetailActivity.this, "Data Gagal Dimuat", Toast.LENGTH_LONG).show();
                    Log.e("MyListActivity", databaseError.getDetails() + " " + databaseError.getMessage());
                }


            });



        fabNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("https")
                        .authority("www.google.com")
                        .appendPath("maps")
                        .appendPath("dir")
                        .appendPath("")
                        .appendQueryParameter("api", "1")
                        .appendQueryParameter("destination", lati + "," + lngi);
                String url = builder.build().toString();
                Log.d("Directions", url);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });







    }


    public void changeColor(int resourseColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), resourseColor));
        }

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(resourseColor)));

    }


    private void showwImage() {
        Dialog builder = new Dialog(DetailActivity.this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });
        SharedPreferences sharedpreferences = getSharedPreferences(mypreference,Context.MODE_PRIVATE);
        ImageView imageView = new ImageView(DetailActivity.this);
        Picasso.get().load(sharedpreferences.getString(url,"")).rotate(90).into(imageView);

        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        builder.show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(DetailActivity.this, InputData.class);
                intent.putExtra("edit",key);
                startActivity(intent);
                return true;
            case R.id.action_delete:
                doDeleted();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void doDeleted() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("customers").orderByChild("key").equalTo(key);
        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
//                    HomeFragment fragment = HomeFragment.newInstance(10);
//                    // R.id.container - the id of a view that will hold your fragment; usually a FrameLayout
//                    getSupportFragmentManager().beginTransaction()
//                            .add(R.id.nav_host_fragment, fragment, HomeFragment.TAG).commit();

                    Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
