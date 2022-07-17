package com.servicepro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.servicepro.POJO.User;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.servicepro.LoginActivity.Login;
import static com.servicepro.LoginActivity.MyPREFERENCES;

public class VerifyOtpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editOPassword, editPassword, editCPassword;
    private LinearLayout btnRegister;
    private String user_id, sopass, spass, scpass;
    private SharedPreferences sharedPreferences;
    private ArrayList<User> resultList;

    private String otp = "";
    private Bundle extra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        extra = getIntent().getExtras();
        otp = extra.getString("otp");

        editPassword = (EditText) findViewById(R.id.etPassword);

        btnRegister = (LinearLayout) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        resultList = new ArrayList<User>();


    }

    // validating mobile no
    private boolean isValidMobile(String mobile) {
        String Mobile_PATTERN = "[0-9]{4}";

        Pattern pattern = Pattern.compile(Mobile_PATTERN);
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }

    @Override
    public void onClick(View v) {

        if (v == btnRegister) {

            spass = editPassword.getText().toString().trim();


            if (spass.equals("")) {
                editPassword.setError("Enter OTP");
                return;
            } else if (!isValidMobile(spass)) {
                editPassword.setError("Enter 4 digit OTP");
                return;
            } else {
                if (!spass.equals(otp)) {
                    Toast.makeText(VerifyOtpActivity.this, "Enter Valid OTP", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Login, "Yes");
                    editor.commit();

                    Toast.makeText(VerifyOtpActivity.this, "OTP Verified", Toast.LENGTH_SHORT).show();
                    Intent login = new Intent(VerifyOtpActivity.this, UserHomeActivity.class);
                    startActivity(login);
                    finish();
                }
            }
        }
    }
}
