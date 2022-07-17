package com.servicepro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editEmail, editPassword;
    private LinearLayout btnLogin;
    private TextView textRegister,textForgot;
    private String semail, spassword;
    private ArrayList<User> resultList;
    private SharedPreferences sharedPreferences;
    private Button adminLogin;
    private Button serproReg;

    public static final String MyPREFERENCES = "MyPrefspa" ;
    public static final String Email = "emailKey";
    public static final String Login = "loginKey";
    public static final String UserID = "UserIDKey";
    public static final String UserType = "UserKey";
    public static final String UserName = "UserNameKey";
    public static final String Mobile = "mobileKey";
    public static final String Image = "imageKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       


        sharedPreferences = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
        adminLogin =(Button)findViewById(R.id.btnadminLogin);

        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(LoginActivity.this,AdminLogin.class);
                startActivity(i);
            }
        });

        serproReg=(Button)findViewById(R.id.btnServiceProReg);
        serproReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(LoginActivity.this,ServiceProvideReg.class);
                startActivity(i);
            }
        });
        /*btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(LoginActivity.this,AdminLogin.class);
                startActivity(i);
            }
        });*/

        String Login1  = sharedPreferences.getString(Login,"");
        String Utype  = sharedPreferences.getString(UserType,"");

        if(Login1.contains("Yes"))
        {
            if(Utype.contains("User")) {
                Intent login = new Intent(LoginActivity.this,UserHomeActivity.class);
                startActivity(login);
                finish();
            }
            else if(Utype.contains("Staff")) {
                Intent login = new Intent(LoginActivity.this,StaffHomeActivity.class);
                startActivity(login);
                finish();
            }

        }

        editEmail = (EditText) findViewById(R.id.etUserName);
        editPassword = (EditText) findViewById(R.id.etPassword);

        btnLogin = (LinearLayout) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        textRegister = (TextView) findViewById(R.id.tvRegister);
        textRegister.setOnClickListener(this);


        textForgot = (TextView) findViewById(R.id.tvForgotPassword);
        textForgot.setOnClickListener(this);

        resultList = new ArrayList<User>();
    }

    @Override
    public void onClick(View v) {
        if(v==textRegister)
        {
            Intent Register = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(Register);
            //finish();
        }

        if(v==textForgot)
        {
            Intent Register = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
            startActivity(Register);
        }

        if (v == btnLogin) {
            semail = editEmail.getText().toString().trim();
            spassword = editPassword.getText().toString().trim();

            if (semail.equals("")) {
                editEmail.setError("Enter Email");
                return;
            }
            else if (!isValidEmail(semail)) {
                editEmail.setError("Enter Valid Email");
                return;
            }else if (spassword.equals("")) {
                editPassword.setError("Enter Password");
                return;
            } else {
                new PostLogin().execute();
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
    public class PostLogin extends AsyncTask<Void,Void, AllUser>
    {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Please Wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected AllUser doInBackground(Void... voids) {

            AllUser result = new AllUser();
            result = new APICall().GetLogin(semail,spassword);
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

             //   Toast.makeText(LoginActivity.this, resultList.get(0).getUser_id(), Toast.LENGTH_SHORT).show();

                if(result.contains("No"))
                {
                    if(msg.contains("Error"))
                    {
                        Toast.makeText(LoginActivity.this,"Invalid Email/Password",Toast.LENGTH_SHORT).show();
                    }
                    else if(msg.contains("NoData"))
                    {
                        Toast.makeText(LoginActivity.this,"Server Connection Error",Toast.LENGTH_SHORT).show();
                    }
                }
                else if(result.contains("Yes"))
                {
                    String utype = resultList.get(0).getUser_type().toString().trim();
                    String uid = resultList.get(0).getUser_id().toString().trim();

                    String uname = resultList.get(0).getUser_name().toString().trim();
                    String mobile = resultList.get(0).getContactNo().toString().trim();
                   String image = resultList.get(0).getImage().toString().trim();

                    if(utype.contains("User"))
                    {
                        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Email, semail);
                        editor.putString(Login, "No");
                        editor.putString(UserID, uid);
                        editor.putString(UserType, utype);
                        editor.putString(UserName, uname);
                        editor.putString(Mobile, mobile);
                       editor.putString(Image, image);
                        editor.commit();

                       // Intent login = new Intent(LoginActivity.this, VerificationActivity.class);
                       Intent login = new Intent(LoginActivity.this, UserHomeActivity.class);
                        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(login);
                        finish();
                    }
                    else if(utype.contains("Staff"))
                    {
                        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Email, semail);
                        editor.putString(Login, "Yes");
                        editor.putString(UserID, uid);
                        editor.putString(UserType, utype);
                        editor.putString(UserName, uname);
                        editor.putString(Mobile, mobile);
                        editor.putString(Image, image);
                        editor.commit();

                        Intent login = new Intent(LoginActivity.this, StaffHomeActivity.class);
                        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(login);
                        finish();
                    }

                }
            }
            dialog.dismiss();
        }
    }
}