package com.servicepro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import static com.servicepro.LoginActivity.MyPREFERENCES;

public class StaffHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout btnTaskList;
    private LinearLayout btnReportList;
    private LinearLayout btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);

        btnTaskList = (LinearLayout) findViewById(R.id.btnTaskList);
        btnTaskList.setOnClickListener(this);

        btnReportList = (LinearLayout) findViewById(R.id.btnReportList);
        btnReportList.setOnClickListener(this);

        btnLogout = (LinearLayout) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v==btnTaskList)
        {
            Intent Register = new Intent(StaffHomeActivity.this,StaffTaskListActivity.class);
            startActivity(Register);
        }
        if(v==btnReportList)
        {
            Intent Register = new Intent(StaffHomeActivity.this,StaffReportListActivity.class);
            startActivity(Register);
        }
        if(v==btnLogout)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(StaffHomeActivity.this);
            builder.setTitle("Logout?");
            builder.setMessage("Are you sure want to Logout? ");
            builder.setPositiveButton("Logout", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    Intent i = new Intent(StaffHomeActivity.this, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}