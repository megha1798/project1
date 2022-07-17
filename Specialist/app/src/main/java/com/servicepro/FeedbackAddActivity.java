package com.servicepro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import static com.servicepro.LoginActivity.UserID;

public class FeedbackAddActivity extends AppCompatActivity {

    LinearLayout btnRegister;
    private EditText etDescription , etSubject;
    private String user_id, subject, description;
    private ArrayList<User> resultList;
    private TextView tvTitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_add);

        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        user_id  = sharedPreferences.getString(Email,"");


        btnRegister = (LinearLayout) findViewById(R.id.btnRegister);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("Add Feedback");

        etSubject= (EditText) findViewById(R.id.etTitle);
        etDescription = (EditText) findViewById(R.id.etDescription);



        resultList = new ArrayList<User>();


        listener();

    }

    private void listener() {

        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                subject = etSubject.getText().toString().trim();
                description = etDescription.getText().toString().trim();

                if (subject.isEmpty())
                {
                    Toast.makeText(FeedbackAddActivity.this,"Please enter subject",Toast.LENGTH_LONG).show();
                    return;
                }
                else if (description.isEmpty())
                {
                    Toast.makeText(FeedbackAddActivity.this,"Please enter description",Toast.LENGTH_LONG).show();
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

            dialog = new ProgressDialog(FeedbackAddActivity.this);
            dialog.setMessage("Please Wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected AllUser doInBackground(Void... voids) {

            AllUser result = new AllUser();
            result = new APICall().PostFeedback(subject,description,user_id);
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
                        Toast.makeText(FeedbackAddActivity.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                    }
                    else if(msg.contains("NoData"))
                    {
                        Toast.makeText(FeedbackAddActivity.this,"Server Connection Error",Toast.LENGTH_SHORT).show();
                    }
                }
                else if(result.contains("Yes"))
                {
                    Toast.makeText(FeedbackAddActivity.this,"Feedback Added", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            dialog.dismiss();
        }
    }

}