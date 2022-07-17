package com.servicepro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.servicepro.API.APICall;
import com.servicepro.POJO.AllUser;
import com.servicepro.POJO.User;

import java.util.ArrayList;

import static com.servicepro.LoginActivity.Email;
import static com.servicepro.LoginActivity.MyPREFERENCES;

public class AddComplain extends AppCompatActivity {

    LinearLayout btnRegisterC;
    private EditText etDescriptionC , etSubjectC;
    private String user_idC, subjectC, descriptionC;
    private ArrayList<User> resultList;
    private TextView tvTitleC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_complain);

        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        user_idC  = sharedPreferences.getString(Email,"");


        btnRegisterC = (LinearLayout) findViewById(R.id.btnRegisterC);

       // tvTitle = (TextView) findViewById(R.id.tvTitle);
        //tvTitle.setText("Add Complain");

        etSubjectC= (EditText) findViewById(R.id.etTitleC);
        etDescriptionC = (EditText) findViewById(R.id.etDescriptionC);

        resultList = new ArrayList<User>();


        listener();




    }

    private void listener() {

        btnRegisterC.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                subjectC = etSubjectC.getText().toString().trim();
                descriptionC = etDescriptionC.getText().toString().trim();

                if (subjectC.isEmpty())
                {
                    Toast.makeText(AddComplain.this,"Please enter subject",Toast.LENGTH_LONG).show();
                    return;
                }
                else if (descriptionC.isEmpty())
                {
                    Toast.makeText(AddComplain.this,"Please enter description",Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    new PostReports().execute();
                }
            }
        });

    }

    public class PostReports extends AsyncTask<Void,Void, AllUser>
    {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(AddComplain.this);
            dialog.setMessage("Please Wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected AllUser doInBackground(Void... voids) {

            AllUser result = new AllUser();
            result = new APICall().PostComplain(subjectC,descriptionC,user_idC);
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

                Toast.makeText(AddComplain.this, ""+result, Toast.LENGTH_SHORT).show();

                if(result.contains("No"))
                {
                    if(msg.contains("Error"))
                    {
                        Toast.makeText(AddComplain.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                    }
                    else if(msg.contains("NoData"))
                    {
                        Toast.makeText(AddComplain.this,"Server Connection Error",Toast.LENGTH_SHORT).show();
                    }
                }
                else if(result.contains("Yes"))
                {
                    Toast.makeText(AddComplain.this,"Complain Added", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            dialog.dismiss();
        }
    }

}