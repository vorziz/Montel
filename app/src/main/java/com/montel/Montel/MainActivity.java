package com.montel.Montel;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.montel.Montel.ui.inputdata.InputData;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    PopupWindow popupWindow;
    LinearLayout linearLayout1;
    Button showPopupBtn, closePopupBtn;
    ConstraintLayout constraintLayout1;
    Dialog mDialog;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

       /// linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);

        constraintLayout1 = (ConstraintLayout) findViewById(R.id.cl_layout);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                //setContentView(R.layout.activity_input_data);


                createData();
            }
        });



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_list, R.id.nav_setting)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void createData(){

        mDialog = new Dialog(MainActivity.this);
        mDialog.setContentView(R.layout.popup_register);
        Button create = (Button)mDialog.findViewById(R.id.btnCreate);
        Button cancle = (Button)mDialog.findViewById(R.id.btnCancle);


            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RadioGroup yourRadioGroup=(RadioGroup)mDialog.findViewById(R.id.radioGroup);
                    int selectedID = yourRadioGroup.getCheckedRadioButtonId();
                    radioButton = (RadioButton)mDialog.findViewById(selectedID);

                    if (radioButton != null){

                        Toast.makeText(getApplicationContext(),radioButton.getText().toString(),Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), InputData.class);
                        String strName = radioButton.getText().toString();
                        i.putExtra("STRING_I_NEED", strName);
                        startActivity(i);

                        mDialog.hide();
                    }else {
                        Toast.makeText(getApplicationContext(),"Please select options above",Toast.LENGTH_SHORT).show();
                    }



                }

            });

            cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.hide();
                }
            });





        mDialog.show();
        mDialog.setCanceledOnTouchOutside(false);
    }
    @Override
    protected void onResume() {
        super.onResume();

        int googlePlayServicesCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        Log.i(MainActivity.class.getSimpleName(), "googlePlayServicesCode = " + googlePlayServicesCode);

        if (googlePlayServicesCode == 1 || googlePlayServicesCode == 2 || googlePlayServicesCode == 3) {
            GooglePlayServicesUtil.getErrorDialog(googlePlayServicesCode, this, 0).show();
        }
    }


}
