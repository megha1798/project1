package com.servicepro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminDashboard extends AppCompatActivity {

    Button addCity,addDesig,addServiceType,taskAssign,detail,lout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        addCity=(Button)findViewById(R.id.btnCity);
        addDesig=(Button)findViewById(R.id.btnDesignation);
        addServiceType=(Button)findViewById(R.id.btnServiceType);
        taskAssign=(Button)findViewById(R.id.btnTaskAssign);
        detail=(Button)findViewById(R.id.btnViewDetail);

        addCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i =new Intent(AdminDashboard.this,Add_City.class);
                startActivity(i);


            }
        });

        lout=(Button)findViewById(R.id.btnadminLogout);
        lout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(AdminDashboard.this,LoginActivity.class);
                startActivity(i);
            }
        });

        addDesig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i =new Intent(AdminDashboard.this,Add_Designation.class);
                startActivity(i);

            }
        });

        addServiceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(AdminDashboard.this,Add_ServiceType.class);
                startActivity(i);
            }
        });

        taskAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(AdminDashboard.this,Task_AssignStaff.class);
                startActivity(i);
            }
        });

        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }
}