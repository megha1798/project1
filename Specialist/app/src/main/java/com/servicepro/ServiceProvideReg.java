package com.servicepro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AsyncNotedAppOp;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.servicepro.API.APICall;
import com.servicepro.POJO.AllServiceType;
import com.servicepro.POJO.AllUser;
import com.servicepro.POJO.ServiceType;
import com.servicepro.POJO.User;
import com.servicepro.POJO.allcity;
import com.servicepro.POJO.allstaff;
import com.servicepro.POJO.city;
import com.servicepro.POJO.staff;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceProvideReg extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<staff> resultList;
    private EditText editName, editEmail, editMobile, editPassword, editCPassword;
    private String sname, semail, smobile, suserid, spass, scpass;
    private Button ok;


    private ArrayList<city> cityList;
    private Spinner spinCity;
    private String scity = "0";
    private ArrayList<String> cities;
    private ArrayAdapter<String> cityAdapter;

    private ArrayList<ServiceType> stList;
    private Spinner spinst;
    private String sst = "0";
    private ArrayList<String> sts;
    private ArrayAdapter<String> stAdapter;
    private SharedPreferences sharedPreferences;

    private String utype = "";
    private Bundle extra;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provide_reg);

        extra = getIntent().getExtras();

        editName = (EditText) findViewById(R.id.etNamesp);
        editEmail = (EditText) findViewById(R.id.etEmailsp);
        editMobile = (EditText) findViewById(R.id.etMobilesp);
        editPassword = (EditText) findViewById(R.id.etPasssp);
        editCPassword = (EditText) findViewById(R.id.etConfpasssp);
        ok = (Button) findViewById(R.id.btnSubmitsp);
        ok.setOnClickListener(this);
        resultList = new ArrayList<staff>();



        spinCity = (Spinner) findViewById(R.id.spinCitysp);
        cityList = new ArrayList<city>();
        cities = new ArrayList<String>();
        cityAdapter = new ArrayAdapter<String>(ServiceProvideReg.this, android.R.layout.simple_spinner_dropdown_item, cities);
        spinCity.setAdapter(cityAdapter);
        cities.add("select city");
        new GetCity1().execute();



        spinst = (Spinner) findViewById(R.id.spinStypesp);
        stList = new ArrayList<ServiceType>();
        sts = new ArrayList<String>();
        stAdapter = new ArrayAdapter<String>(ServiceProvideReg.this, android.R.layout.simple_spinner_dropdown_item, sts);
        spinst.setAdapter(stAdapter);
        sts.add("select Servie Type");
        new GetServiceType().execute();

        spinCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position > 0 ) {
                    scity = cityList.get(position - 1).getCity_Id();
                    Toast.makeText(ServiceProvideReg.this, "" + scity.toString(), Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(ServiceProvideReg.this, "Select City", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinst.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    sst = stList.get(position - 1).getService_id();

                    Toast.makeText(ServiceProvideReg.this, "" + sst.toString(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ServiceProvideReg.this, "Select Se", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });











    }



    @Override
    public void onClick(View v) {


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sname = editName.getText().toString().trim();
                semail = editEmail.getText().toString().trim();
                smobile = editMobile.getText().toString().trim();
                spass = editPassword.getText().toString().trim();
                scpass = editCPassword.getText().toString().trim();



                if (sname.equals("")) {
                    editName.setError("Enter Name");
                    return;
                } else if (semail.equals("")) {
                    editEmail.setError("Enter Email");
                    return;
                } else if (!isValidEmail(semail)) {
                    editEmail.setError("Enter Valid Email");
                    return;
                } else if (smobile.equals("")) {
                    editMobile.setError("Enter Mobile");
                    return;
                } else if (!isValidMobile(smobile)) {
                    editMobile.setError("Enter Valid Mobile");
                    return;
                } else if (spass.equals("")) {
                    editPassword.setError("Enter Password");
                    return;
                } else if (scpass.equals("")) {
                    editCPassword.setError("Enter Password");
                    return;
                } else {

                    if(spinCity.getSelectedItemPosition() == 0)
                    {
                        Toast.makeText(ServiceProvideReg.this,"Select City",Toast.LENGTH_LONG).show();
                        return;
                    }
                    else
                    {
                        scity = cityList.get(spinCity.getSelectedItemPosition()-1).getCity_Id();

                    }

                    if(spinst.getSelectedItemPosition() == 0)
                    {
                        Toast.makeText(ServiceProvideReg.this,"Select Service",Toast.LENGTH_LONG).show();
                        return;
                    }
                    else
                    {
                        sst = stList.get(spinst.getSelectedItemPosition()-1).getService_id();

                    }
                    if (!spass.equals(scpass)) {
                        Toast.makeText(ServiceProvideReg.this, "Enter Same Password", Toast.LENGTH_SHORT).show();
                        return;
                    } else {



                        new PostRegister1().execute();
                    }
                }

            }
        });





    }

    public class  PostRegister1 extends  AsyncTask<Void,Void, allstaff>
    {
        private ProgressDialog dialoug;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialoug = new ProgressDialog(ServiceProvideReg.this);
            dialoug.setMessage("Registering.. Please Wait..");
            dialoug.show();
            dialoug.setCancelable(false);
        }

        @Override
        protected allstaff doInBackground(Void... voids) {

            allstaff result = new allstaff();
            result = new APICall().PostRegister1(sname, semail, smobile,  spass,scity,sst);
            return result;

        }

        @Override
        protected void onPostExecute(allstaff allstaff) {
            super.onPostExecute(allstaff);
            if (allstaff != null) {

                resultList.clear();
                resultList.addAll(allstaff.getData());

                String result = resultList.get(0).getResult().toString().trim();
                String msg = resultList.get(0).getMsg().toString().trim();

                //           Toast.makeText(RegisterActivity.this,result+"  " + msg,Toast.LENGTH_SHORT).show();

                if (result.contains("No")) {
                    if (msg.contains("Email")) {
                        Toast.makeText(ServiceProvideReg.this, "This Email is already Registered.", Toast.LENGTH_SHORT).show();
                    } else if (msg.contains("Mobile")) {
                        Toast.makeText(ServiceProvideReg.this, "This Mobile is already Registered.", Toast.LENGTH_SHORT).show();
                    } else if (msg.contains("NoData")) {
                        Toast.makeText(ServiceProvideReg.this, "Error in Server Connection.", Toast.LENGTH_SHORT).show();
                    }

                } else if (result.contains("Yes"))
                {
                    if (msg.contains("Sucess"))
                    {
                        String uid = resultList.get(0).getStaff_Id().toString().trim();

                        String uname = resultList.get(0).getStaff_Name().toString().trim();
                        String mobile = resultList.get(0).getContactNo().toString().trim();

                        Intent login = new Intent(ServiceProvideReg.this, LoginActivity.class);
                        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(login);
                        Toast.makeText(ServiceProvideReg.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                }
                dialoug.dismiss();
            }










        }
    }

    public class GetCity1 extends AsyncTask<Void, Void, allcity> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ServiceProvideReg.this);
            dialog.setMessage("Loading..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected allcity doInBackground(Void... voids) {
            allcity result = new allcity();
            result = new APICall().GetCity();
            return result;
        }

        @Override
        protected void onPostExecute(allcity allcity) {

            if (allcity != null) {
                cityList.addAll(allcity.getData());
                for (city city : cityList) {
                    cities.add(city.getCity_Name());
                }
            }
            super.onPostExecute(allcity);
            cityAdapter.notifyDataSetChanged();
            dialog.dismiss();
        }
    }


    public class GetServiceType extends AsyncTask<Void,Void, AllServiceType>{
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ServiceProvideReg.this);
            dialog.setMessage("Loading..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected AllServiceType doInBackground(Void... voids) {
            AllServiceType result = new AllServiceType();
            result = new APICall().GetServiceType();
            return result;

        }

        @Override
        protected void onPostExecute(AllServiceType allServiceType) {
            stList.addAll(allServiceType.getData());
            for (ServiceType serviceType :stList) {
                sts.add(serviceType.getService_name());
            }
            super.onPostExecute(allServiceType);
            stAdapter.notifyDataSetChanged();
            dialog.dismiss();
            }


        }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z]+(\\.[A-Za-z]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating mobile no
    private boolean isValidMobile(String mobile) {
        String Mobile_PATTERN = "[0-9]{10}";

        Pattern pattern = Pattern.compile(Mobile_PATTERN);
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }

    // validating only Charater
    private boolean isValidOnlyChar(String Char) {
        String CHAR_PATTERN = "[a-zA-Z]+";

        Pattern pattern = Pattern.compile(CHAR_PATTERN);
        Matcher matcher = pattern.matcher(Char);
        return matcher.matches();
    }

    // validating only Number
    private boolean isValidOnlyNumber(String Char) {
        String CHAR_PATTERN = "[0-9]+";

        Pattern pattern = Pattern.compile(CHAR_PATTERN);
        Matcher matcher = pattern.matcher(Char);
        return matcher.matches();
    }

}



