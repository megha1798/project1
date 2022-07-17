package com.servicepro;

import static com.servicepro.LoginActivity.MyPREFERENCES;
import static com.servicepro.LoginActivity.UserID;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.servicepro.API.APICall;
import com.servicepro.POJO.AllUser;
import com.servicepro.POJO.User;
import com.servicepro.POJO.allcity;
import com.servicepro.POJO.city;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ServiceRequestAddActivity extends AppCompatActivity {

    LinearLayout btnRegister;
    private EditText etService,etDescription,etName, etMobile, etAddress , etArea , etserviceTime , etserviceDate;
    private String user_id, estimate_cost,description,name,mobile,address , area , service_time , service_date ,service_id , service_name ;
    private Bundle extra;
    private ArrayList<User> resultList;
    private TextView tvTitle;



    private ArrayList<city> cityList;
    private Spinner spinCity;
    private String scity = "0";
    private ArrayList<String> cities;
    private ArrayAdapter<String> cityAdapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_request_add);

        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        user_id  = sharedPreferences.getString(UserID,"");

        btnRegister = (LinearLayout) findViewById(R.id.btnRegister);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("Add Request");
        etService = (EditText) findViewById(R.id.etBrand);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etName = (EditText) findViewById(R.id.etName);
        etMobile = (EditText) findViewById(R.id.etMobile);
        etAddress = (EditText) findViewById(R.id.etPickupAddress);
        etArea = (EditText) findViewById(R.id.etPickupArea);
        etserviceDate = (EditText) findViewById(R.id.etPickupDate);
        etserviceTime = (EditText) findViewById(R.id.etPickupTime);



        spinCity = (Spinner) findViewById(R.id.spinCity);
        cityList = new ArrayList<city>();
        cities = new ArrayList<String>();
        cityAdapter = new ArrayAdapter<String>(ServiceRequestAddActivity.this, android.R.layout.simple_spinner_dropdown_item, cities);
        spinCity.setAdapter(cityAdapter);
        cities.add("select city");




        extra = getIntent().getExtras();

        service_id= extra.getString("service_id","0");
        service_name= extra.getString("service_name","0");
        estimate_cost= extra.getString("estimate_cost","0");

        etService.setText(service_name);

        resultList = new ArrayList<User>();


        listener();


        new GetCity().execute();


    }

    private void listener() {

        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                service_name = etService.getText().toString().trim();
                description = etDescription.getText().toString().trim();
                name = etName.getText().toString().trim();
                mobile = etMobile.getText().toString().trim();
                address = etAddress.getText().toString().trim();
                area = etArea.getText().toString().trim();
                service_date = etserviceDate.getText().toString().trim();
                service_time = etserviceTime.getText().toString().trim();

                if (description.isEmpty())
                {
                    Toast.makeText(ServiceRequestAddActivity.this,"Please enter the issue ",Toast.LENGTH_LONG).show();
                    return;
                }
                else if (name.isEmpty())
                {
                    Toast.makeText(ServiceRequestAddActivity.this,"Enter Name",Toast.LENGTH_LONG).show();
                    return;
                }
                else if (mobile.isEmpty())
                {
                    Toast.makeText(ServiceRequestAddActivity.this,"Enter Mobile",Toast.LENGTH_LONG).show();
                    return;
                }
                else if (mobile.length() != 10)
                {
                    Toast.makeText(ServiceRequestAddActivity.this,"Invalid mobile number",Toast.LENGTH_LONG).show();
                    return;
                }
                else if (address.isEmpty())
                {
                    Toast.makeText(ServiceRequestAddActivity.this, "Please enter Address",Toast.LENGTH_LONG).show();
                    return;
                }
                else if (area.isEmpty())
                {
                    Toast.makeText(ServiceRequestAddActivity.this,"Please enter Area",Toast.LENGTH_LONG).show();
                    return;
                }
                else if (service_date.isEmpty())
                {
                    Toast.makeText(ServiceRequestAddActivity.this,"Please enter Service Date",Toast.LENGTH_LONG).show();
                    return;
                }
                else if (service_time.isEmpty())
                {
                    Toast.makeText(ServiceRequestAddActivity.this,"Please enter Service Time",Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    new PostReport().execute();
                }
            }
        });

        etserviceDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Calendar myCalendar = Calendar.getInstance();
                DatePickerDialog datePicker = new DatePickerDialog(ServiceRequestAddActivity.this, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                        etserviceDate.setText(sdf.format(myCalendar.getTime()));

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        String dateServer = simpleDateFormat.format(myCalendar.getTime());
                        etserviceDate.setTag(dateServer);
                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePicker.setTitle("Select date");
                datePicker.getDatePicker().setMinDate(new Date().getTime() + (1 * 24 * 60 * 60 * 1000));
                datePicker.show();
            }
        });

    }

    public class PostReport extends AsyncTask<Void,Void, AllUser>
    {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(ServiceRequestAddActivity.this);
            dialog.setMessage("Please Wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected AllUser doInBackground(Void... voids) {

            AllUser result = new AllUser();
            result = new APICall().PostRequest(user_id,service_id,description,name,address,area,estimate_cost,service_date,service_time,mobile);
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
                        Toast.makeText(ServiceRequestAddActivity.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                    }
                    else if(msg.contains("NoData"))
                    {
                        Toast.makeText(ServiceRequestAddActivity.this,"Server Connection Error",Toast.LENGTH_SHORT).show();
                    }
                }
                else if(result.contains("Yes"))
                {
                    Toast.makeText(ServiceRequestAddActivity.this,"Request Added", Toast.LENGTH_LONG).show();
                    finish();

                }
            }
            dialog.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish();
        }
        return true;
    }



    public class GetCity extends AsyncTask<Void, Void, allcity> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ServiceRequestAddActivity.this);
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

}