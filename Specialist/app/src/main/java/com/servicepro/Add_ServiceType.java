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

public class Add_ServiceType extends AppCompatActivity {

    private EditText sername,serdesc,imgpath,frate;
    private String ser_name,ser_desc,img_path,f_rate;
    private ArrayList<User> resultList;
    private TextView tvTitleC;
    private Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service_type);

        sername=(EditText)findViewById(R.id.etservicenamesp);
        serdesc=(EditText)findViewById(R.id.etserviceDescsp);
        imgpath =(EditText)findViewById(R.id.etserviceImagesp);
        frate=(EditText)findViewById(R.id.etfixratesp);
        ok=(Button)findViewById(R.id.btnAddServicesp);

        resultList = new ArrayList<User>();
        listener();




    }

    private void listener() {

        ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ser_name = sername.getText().toString().trim();
                ser_desc = serdesc.getText().toString().trim();
                img_path = imgpath.getText().toString().trim();
                f_rate = frate.getText().toString().trim();

                if (ser_name.isEmpty())
                {
                    Toast.makeText(Add_ServiceType.this,"Please enter Service Name",Toast.LENGTH_LONG).show();
                    return;
                }
                else if (ser_desc.isEmpty())
                {
                    Toast.makeText(Add_ServiceType.this,"Please enter description",Toast.LENGTH_LONG).show();
                    return;
                }

                else if (img_path.isEmpty())
                {
                    Toast.makeText(Add_ServiceType.this,"Please enter Image Path",Toast.LENGTH_LONG).show();
                    return;
                }

                else if (f_rate.isEmpty())
                {
                    Toast.makeText(Add_ServiceType.this,"Please enter Fixed Rate",Toast.LENGTH_LONG).show();
                    return;
                }


                else
                {
                    new AddService1().execute();

                }
            }
        });

    }

    public class AddService1 extends AsyncTask<Void,Void, AllUser>
    {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Add_ServiceType.this);
            dialog.setMessage("Please Wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected AllUser doInBackground(Void... voids) {

            AllUser result = new AllUser();
            result = new APICall().postServiceType(ser_name,ser_desc,img_path,f_rate);
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

                Toast.makeText(Add_ServiceType.this, ""+result, Toast.LENGTH_SHORT).show();

                if(result.contains("No"))
                {
                    if(msg.contains("Error"))
                    {
                        Toast.makeText(Add_ServiceType.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                    }
                    else if(msg.contains("NoData"))
                    {
                        Toast.makeText(Add_ServiceType.this,"Server Connection Error",Toast.LENGTH_SHORT).show();
                    }
                }
                else if(result.contains("Yes"))
                {
                    Toast.makeText(Add_ServiceType.this,"Service Type Added", Toast.LENGTH_LONG).show();
                    //finish();
                }
            }
            dialog.dismiss();
        }
    }



}