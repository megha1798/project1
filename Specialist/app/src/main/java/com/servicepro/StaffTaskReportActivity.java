package com.servicepro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.servicepro.API.APICall;
import com.servicepro.POJO.AllUser;
import com.servicepro.POJO.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.servicepro.LoginActivity.MyPREFERENCES;
import static com.servicepro.LoginActivity.UserID;

public class StaffTaskReportActivity extends AppCompatActivity {


    LinearLayout btnRegister;
    private EditText etDescription;
    private String user_id, Staff_id,Report_Description,Requirement_Id,TaskAssignId,amounts;
    private Bundle extra;
    private ArrayList<User> resultList;
    private TextView tvTitle;
    private Button pay;
    private EditText amount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_task_report);

        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        Staff_id  = sharedPreferences.getString(UserID,"");

        extra = getIntent().getExtras();
        user_id = extra.getString("user_id", "");
        Requirement_Id = extra.getString("Requirement_Id", "");
        TaskAssignId = extra.getString("TaskAssignId", "");


        btnRegister = (LinearLayout) findViewById(R.id.btnRegister);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("Add Report");
        etDescription = (EditText) findViewById(R.id.etDescription);
        pay=(Button)findViewById(R.id.btnPay);
        amount =(EditText)findViewById(R.id.etFianlAmt);






        resultList = new ArrayList<User>();


        listener();
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(StaffTaskReportActivity.this,RazorPay.class);
                i.putExtra("amount",amount.getText().toString());
                startActivity(i);
            }
        });

    }

    private void listener() {

        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Report_Description = etDescription.getText().toString().trim();
                amounts =amount.getText().toString();
                Toast.makeText(StaffTaskReportActivity.this, ""+ amounts, Toast.LENGTH_SHORT).show();

                if (Report_Description.isEmpty())
                {
                    Toast.makeText(StaffTaskReportActivity.this,"Please enter report",Toast.LENGTH_LONG).show();
                    return;
                }
                else if (amounts.isEmpty())
                {
                    Toast.makeText(StaffTaskReportActivity.this,"Please enter amount",Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    new PostReport().execute();
                }
            }
        });

    }

    public class PostReport extends AsyncTask<Void,Void, AllUser>
    {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(StaffTaskReportActivity.this);
            dialog.setMessage("Please Wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected AllUser doInBackground(Void... voids) {

            AllUser result = new AllUser();
            result = new APICall().PostReport(user_id,Staff_id,Report_Description,Requirement_Id,TaskAssignId,amounts);
            return result;

        }

        @Override
        protected void onPostExecute(AllUser allUser) {
            super.onPostExecute(allUser);

            if(allUser != null)
            {
                resultList.clear();
                resultList.addAll(allUser.getData());

                String result = resultList.get(0).getResult().toString().trim();
                String msg = resultList.get(0).getMsg().toString().trim();

                if(result.contains("No"))
                {
                    if(msg.contains("Error"))
                    {
                        Toast.makeText(StaffTaskReportActivity.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                    }
                    else if(msg.contains("NoData"))
                    {
                        Toast.makeText(StaffTaskReportActivity.this,"Server Connection Error",Toast.LENGTH_SHORT).show();
                    }
                }
                else if(result.contains("Yes"))
                {
                    Toast.makeText(StaffTaskReportActivity.this,"Report Added", Toast.LENGTH_LONG).show();
                    Intent login = new Intent(StaffTaskReportActivity.this, StaffHomeActivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(login);
                    finish();
                }
            }
            dialog.dismiss();
        }
    }

}