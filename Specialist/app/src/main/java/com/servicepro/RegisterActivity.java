package com.servicepro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editName, editEmail, editMobile, editPassword, editCPassword;
    private LinearLayout btnRegister;
    private TextView textLogin;
    private String sname, semail, smobile, suserid, spass, scpass;
    private SharedPreferences sharedPreferences;
    private ArrayList<User> resultList;

    private String utype = "";
    private Bundle extra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        extra = getIntent().getExtras();

        editName = (EditText) findViewById(R.id.etName);
        editEmail = (EditText) findViewById(R.id.etEmail);
        editMobile = (EditText) findViewById(R.id.etMobile);
        editPassword = (EditText) findViewById(R.id.etPassword);
        editCPassword = (EditText) findViewById(R.id.etCPassword);

        btnRegister = (LinearLayout) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        textLogin = (TextView) findViewById(R.id.tvLogin);
        textLogin.setOnClickListener(this);

        resultList = new ArrayList<User>();
    }



    @Override
    public void onClick(View v) {

        if (v == btnRegister) {
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
                if (!spass.equals(scpass)) {
                    Toast.makeText(RegisterActivity.this, "Enter Same Password", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    new PostRegister().execute();
                }
            }
        }

        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    public class PostRegister extends AsyncTask<Void, Void, AllUser> {
        private ProgressDialog dialoug;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialoug = new ProgressDialog(RegisterActivity.this);
            dialoug.setMessage("Registering.. Please Wait..");
            dialoug.show();
            dialoug.setCancelable(false);
        }

        @Override
        protected AllUser doInBackground(Void... voids) {
            AllUser result = new AllUser();
            result = new APICall().PostRegister(sname, semail, smobile,  spass);
            return result;
        }


        @Override
        protected void onPostExecute(AllUser allUser) {
            super.onPostExecute(allUser);

            if (allUser != null) {

                resultList.clear();
                resultList.addAll(allUser.getData());

                String result = resultList.get(0).getResult().toString().trim();
                String msg = resultList.get(0).getMsg().toString().trim();

                //           Toast.makeText(RegisterActivity.this,result+"  " + msg,Toast.LENGTH_SHORT).show();

                if (result.contains("No")) {
                    if (msg.contains("Email")) {
                        Toast.makeText(RegisterActivity.this, "This Email is already Registered.", Toast.LENGTH_SHORT).show();
                    } else if (msg.contains("Mobile")) {
                        Toast.makeText(RegisterActivity.this, "This Mobile is already Registered.", Toast.LENGTH_SHORT).show();
                    } else if (msg.contains("NoData")) {
                        Toast.makeText(RegisterActivity.this, "Error in Server Connection.", Toast.LENGTH_SHORT).show();
                    }

                } else if (result.contains("Yes"))
                {
                    if (msg.contains("Sucess"))
                    {
                        String uid = resultList.get(0).getUser_id().toString().trim();

                        String uname = resultList.get(0).getUser_name().toString().trim();
                        String mobile = resultList.get(0).getContactNo().toString().trim();



                        Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
                        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(login);
                        Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                }
                dialoug.dismiss();
            }
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