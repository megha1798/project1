package com.servicepro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.servicepro.API.APICall;
import com.servicepro.POJO.AllUser;
import com.servicepro.POJO.User;

import java.util.ArrayList;

public class Add_City extends AppCompatActivity {

    private EditText cityname;
    private String city_name;
    private ArrayList<User> resultList;
    private TextView tvTitleC;
    private Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        cityname = (EditText)findViewById(R.id.etCitysp);
        ok=(Button)findViewById(R.id.btnInsertsp);
        resultList = new ArrayList<User>();
        listener();
    }

    private void listener() {

        ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                city_name = cityname.getText().toString().trim();


                if (city_name.isEmpty())
                {
                    Toast.makeText(Add_City.this,"Please enter CityName",Toast.LENGTH_LONG).show();
                    return;
                }

                else
                {
                    new PostCity().execute();
                }
            }
        });


    }

    public class PostCity extends AsyncTask<Void,Void, AllUser>
    {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Add_City.this);
            dialog.setMessage("Please Wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected AllUser doInBackground(Void... voids) {

            AllUser result = new AllUser();
            result = new APICall().addcity(city_name);
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

                Toast.makeText(Add_City.this, ""+result, Toast.LENGTH_SHORT).show();

                if(result.contains("No"))
                {
                    if(msg.contains("Error"))
                    {
                        Toast.makeText(Add_City.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                    }
                    else if(msg.contains("NoData"))
                    {
                        Toast.makeText(Add_City.this,"Server Connection Error",Toast.LENGTH_SHORT).show();
                    }
                }
                else if(result.contains("Yes"))
                {
                    Toast.makeText(Add_City.this,"City Added", Toast.LENGTH_LONG).show();
                   cityname.setText("");
                    // finish();
                }
            }
            dialog.dismiss();
        }
    }





}