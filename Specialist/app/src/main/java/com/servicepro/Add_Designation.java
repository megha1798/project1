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

public class Add_Designation extends AppCompatActivity {

    private EditText designation;
    private String desig;
    private ArrayList<User> resultList;
    private TextView tvTitleC;
    private Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_designation);

        designation = (EditText)findViewById(R.id.etDesigsp);
        ok=(Button)findViewById(R.id.btnAddrecsp);
        resultList = new ArrayList<User>();
        listener();




    }

    private void listener() {

        ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                desig = designation.getText().toString().trim();


                if (desig.isEmpty())
                {
                    Toast.makeText(Add_Designation.this,"Please enter CityName",Toast.LENGTH_LONG).show();
                    return;
                }

                else
                {
                    new Add_Designations().execute();
                }
            }
        });


    }

    public class Add_Designations extends AsyncTask<Void,Void, AllUser>
    {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Add_Designation.this);
            dialog.setMessage("Please Wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected AllUser doInBackground(Void... voids) {

            AllUser result = new AllUser();
            result = new APICall().adddesignation(desig);
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

                Toast.makeText(Add_Designation.this, ""+result, Toast.LENGTH_SHORT).show();

                if(result.contains("No"))
                {
                    if(msg.contains("Error"))
                    {
                        Toast.makeText(Add_Designation.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                    }
                    else if(msg.contains("NoData"))
                    {
                        Toast.makeText(Add_Designation.this,"Server Connection Error",Toast.LENGTH_SHORT).show();
                    }
                }
                else if(result.contains("Yes"))
                {
                    Toast.makeText(Add_Designation.this,"Designation Added", Toast.LENGTH_LONG).show();
                    designation.setText("");
                    // finish();
                }
            }
            dialog.dismiss();
        }
    }




}