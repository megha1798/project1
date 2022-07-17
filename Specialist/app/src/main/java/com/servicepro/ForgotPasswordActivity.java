package com.servicepro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.servicepro.API.APICall;
import com.servicepro.POJO.AllUser;
import com.servicepro.POJO.User;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editEmail;
    private Button btnLogin;
    private String semail;
    private ArrayList<User> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password);

        editEmail = (EditText) findViewById(R.id.etEmail);

        btnLogin = (Button) findViewById(R.id.btnForgot);
        btnLogin.setOnClickListener(this);

        resultList = new ArrayList<User>();
    }

    @Override
    public void onClick(View v) {

        if (v == btnLogin) {
            semail = editEmail.getText().toString().trim();

            if (semail.equals("")) {
                editEmail.setError("Enter Email");
                return;
            }
            else if (!isValidEmail(semail)) {
                editEmail.setError("Enter Valid Email");
                return;
            }
            else {
                new PostForgot().execute();
            }
        }


    }
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z]+(\\.[A-Za-z]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public class PostForgot extends AsyncTask<Void,Void, AllUser>
    {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(ForgotPasswordActivity
                    .this);
            dialog.setMessage("Please Wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected AllUser doInBackground(Void... voids) {

            AllUser result = new AllUser();
            result = new APICall().ForgotPassword(semail);
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
                        Toast.makeText(ForgotPasswordActivity.this,"This Email in not registered with this Application",Toast.LENGTH_SHORT).show();
                    }
                    else if(msg.contains("NoData"))
                    {
                        Toast.makeText(ForgotPasswordActivity.this,"Server Connection Error",Toast.LENGTH_SHORT).show();
                    }
                }
                else if(result.contains("Yes"))
                {
                    Toast.makeText(ForgotPasswordActivity.this,"Password Sent to your Email", Toast.LENGTH_LONG).show();
                    finish();

                }
            }
            dialog.dismiss();
        }
    }
}