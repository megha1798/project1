package com.servicepro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.servicepro.API.APICall;
import com.servicepro.POJO.AllUser;
import com.servicepro.POJO.User;

import java.util.ArrayList;
import java.util.Random;

import static com.servicepro.LoginActivity.Email;
import static com.servicepro.LoginActivity.Mobile;
import static com.servicepro.LoginActivity.MyPREFERENCES;

public class VerificationActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout btnRegister;
    private SharedPreferences sharedPreferences;

    private String utype = "User";
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    String number_email = "";
    String message = "";
    String otp = "";

    private ArrayList<User> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        btnRegister = (LinearLayout) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        resultList = new ArrayList<User>();
        Random random = new Random();

        otp = String.format("%04d", random.nextInt(10000));

        message = "Hello, User \nYour OTP is " + otp + " \nThank you.";
    }

    @Override
    public void onClick(View v) {

        if (v == btnRegister) {

            int selectedId = radioGroup.getCheckedRadioButtonId();
            radioButton = (RadioButton) findViewById(selectedId);

            String action = radioButton.getText().toString().trim();


            if (action.contains("Email")) {
                number_email = sharedPreferences.getString(Email, "admin@gmail.com");
                new PostSendMail().execute();
            } else if (action.contains("Mobile")) {
                number_email = sharedPreferences.getString(Mobile, "+919874563210");

                if (ContextCompat.checkSelfPermission(VerificationActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(VerificationActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);
                } else {

                    try {
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(number_email, null, message, null, null);

                    } catch (Exception e) {

                    }
                    Toast.makeText(getApplicationContext(), "OTP Message Sent successfully!", Toast.LENGTH_LONG).show();

                    Intent login = new Intent(VerificationActivity.this, VerifyOtpActivity.class);
                    login.putExtra("otp", otp);
                    startActivity(login);
                    finish();
                }
            }

        }
    }

    public class PostSendMail extends AsyncTask<Void, Void, AllUser> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("Rittz", number_email);
            dialog = new ProgressDialog(VerificationActivity.this);
            dialog.setMessage("Please Wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected AllUser doInBackground(Void... voids) {

            AllUser result = new AllUser();
            result = new APICall().SendOTPMail(number_email, message);
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

                if (result.contains("No")) {
                    if (msg.contains("Error")) {
                        Toast.makeText(VerificationActivity.this, "Invalid Email/ Mobile ", Toast.LENGTH_SHORT).show();
                    } else if (msg.contains("NoData")) {
                        Toast.makeText(VerificationActivity.this, "Server Connection Error", Toast.LENGTH_SHORT).show();
                    }
                } else if (result.contains("Yes")) {
                    Toast.makeText(VerificationActivity.this, "OTP Mail Sent successfully!", Toast.LENGTH_LONG).show();

                    Intent login = new Intent(VerificationActivity.this, VerifyOtpActivity.class);
                    login.putExtra("otp", otp);
                    startActivity(login);
                    finish();
                }
            }
            dialog.dismiss();
        }
    }
}
