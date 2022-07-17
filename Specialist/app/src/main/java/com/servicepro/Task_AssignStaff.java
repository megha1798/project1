package com.servicepro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AsyncNotedAppOp;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.servicepro.API.APICall;
import com.servicepro.POJO.AllUser;
import com.servicepro.POJO.User;
import com.servicepro.POJO.allcity;
import com.servicepro.POJO.allrequirement;
import com.servicepro.POJO.allstaff;
import com.servicepro.POJO.alltask;
import com.servicepro.POJO.city;
import com.servicepro.POJO.requirement;
import com.servicepro.POJO.staff;
import com.servicepro.POJO.stafftask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Task_AssignStaff extends AppCompatActivity {
    private EditText etServiceDate;
    private  String sdate1;
    private ArrayList<stafftask> resultList;

    private Button savedata;

    private ArrayList<staff> staffList;
    private Spinner spinStaff;
    private String sstaff = "0";
    private ArrayList<String> staffs;
    private ArrayAdapter<String> staffAdapter;


    private ArrayList<requirement> requirementList;
    private Spinner spinRequirement;
    private String srequirement = "0";
    private ArrayList<String> requirementss;
    private ArrayAdapter<String> requirementAdapter;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_assign_staff);


        etServiceDate =(EditText)findViewById(R.id.etserDatesp);
        savedata=(Button)findViewById(R.id.btnSubmittasksp);
        resultList = new ArrayList<stafftask>();


        spinStaff = (Spinner) findViewById(R.id.spinStaffidsp);
        staffList = new ArrayList<staff>();
        staffs = new ArrayList<String>();
        staffAdapter = new ArrayAdapter<String>(Task_AssignStaff.this, android.R.layout.simple_spinner_dropdown_item, staffs);
        spinStaff.setAdapter(staffAdapter);
        staffs.add("select Staff");
        new GetStaffDet().execute();

        spinRequirement = (Spinner) findViewById(R.id.spinreqsuidsp);
        requirementList = new ArrayList<requirement>();
        requirementss = new ArrayList<String>();
        requirementAdapter = new ArrayAdapter<String>(Task_AssignStaff.this, android.R.layout.simple_spinner_dropdown_item, requirementss);
        spinRequirement.setAdapter(requirementAdapter);
        requirementss.add("select Requirement");
        new GetRequirementDet().execute();

        etServiceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar myCalendar = Calendar.getInstance();
                DatePickerDialog datePicker = new DatePickerDialog(Task_AssignStaff.this, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                        etServiceDate.setText(sdf.format(myCalendar.getTime()));

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        String dateServer = simpleDateFormat.format(myCalendar.getTime());
                        etServiceDate.setTag(dateServer);
                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePicker.setTitle("Select date");
                datePicker.getDatePicker().setMinDate(new Date().getTime() + (1 * 24 * 60 * 60 * 1000));
                datePicker.show();
            }
        });


        spinStaff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0) {
                sstaff = staffList.get(position-1).getStaff_Id();
                    Toast.makeText(Task_AssignStaff.this, "id="+ sstaff.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    spinRequirement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(position>0)
            srequirement=requirementList.get(position-1).getRequirement_id();
            Toast.makeText(Task_AssignStaff.this, "Requirement id="+ srequirement, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });

        savedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sdate1=etServiceDate.getText().toString();
                new PostTask().execute();

            }
        });



    }



    public class GetRequirementDet extends AsyncTask<Void,Void, allrequirement>
    {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Task_AssignStaff.this);
            dialog.setMessage("Loading..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected allrequirement doInBackground(Void... voids) {
            allrequirement result = new allrequirement();

            result = new APICall().GetRequirement();


            return result;

        }

        @Override
        protected void onPostExecute(allrequirement allrequirement) {

            if (allrequirement != null) {
                requirementList.addAll(allrequirement.getData());
                for (requirement requirement : requirementList) {
                    requirementss.add(requirement.getRequirement_id());
                }
            }

            super.onPostExecute(allrequirement);
            requirementAdapter.notifyDataSetChanged();
            dialog.dismiss();
            

        }
    }

    public class GetStaffDet extends  AsyncTask<Void,Void,allstaff>
    {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Task_AssignStaff.this);
            dialog.setMessage("Loading..");
            dialog.show();
            dialog.setCancelable(false);

        }

        @Override
        protected allstaff doInBackground(Void... voids) {
            allstaff result = new allstaff();

                result = new APICall().GetStaff();


            return result;



        }

        @Override
        protected void onPostExecute(allstaff allstaff) {

            if (allstaff != null) {
                staffList.addAll(allstaff.getData());
                for (staff staff : staffList) {
                    staffs.add(staff.getStaff_Name());
                }
            }
            super.onPostExecute(allstaff);
            staffAdapter.notifyDataSetChanged();
            dialog.dismiss();
        }
    }




public class PostTask extends AsyncTask<Void,Void, alltask>{

    private ProgressDialog dialoug;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialoug = new ProgressDialog(Task_AssignStaff.this);
        dialoug.setMessage("Registering.. Please Wait..");
        dialoug.show();
        dialoug.setCancelable(false);
    }

    @Override
    protected alltask doInBackground(Void... voids) {

        alltask result = new alltask();
        result = new APICall().PostTaskDet(srequirement, sstaff, sdate1);
        return result;

    }

    @Override
    protected void onPostExecute(alltask alltask) {
        super.onPostExecute(alltask);


        if (alltask != null) {

            resultList.clear();
            resultList.addAll(alltask.getData());



            String result = resultList.get(0).getResult().toString().trim();
            String msg = resultList.get(0).getMsg().toString().trim();

            //           Toast.makeText(RegisterActivity.this,result+"  " + msg,Toast.LENGTH_SHORT).show();

            if (result.contains("No")) {
                if (msg.contains("Email")) {
                    Toast.makeText(Task_AssignStaff.this, "This Email is already Registered.", Toast.LENGTH_SHORT).show();
                } else if (msg.contains("Mobile")) {
                    Toast.makeText(Task_AssignStaff.this, "This Mobile is already Registered.", Toast.LENGTH_SHORT).show();
                } else if (msg.contains("NoData")) {
                    Toast.makeText(Task_AssignStaff.this, "Error in Server Connection.", Toast.LENGTH_SHORT).show();
                }

            } else if (result.contains("Yes"))
            {
                if (msg.contains("Sucess"))
                {

                    /*srequirement = resultList.get(0).getRequirement_Id().toString().trim();

                    sstaff = resultList.get(0).getStaff_id().toString().trim();
                    sdate1 = resultList.get(0).getDate().toString().trim();*/

                  /*  Intent login = new Intent(Task_AssignStaff.this, LoginActivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(login);*/
                    Toast.makeText(Task_AssignStaff.this, "Task Assign  Successfully", Toast.LENGTH_SHORT).show();
                    finish();

                }
            }
            dialoug.dismiss();
        }
    }


}
}




