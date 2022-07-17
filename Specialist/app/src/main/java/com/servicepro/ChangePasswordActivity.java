package com.servicepro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
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

import static com.servicepro.LoginActivity.MyPREFERENCES;
import static com.servicepro.LoginActivity.UserID;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editNewPassword, editPassword;
    private LinearLayout btnUpdate;
    private String sNewpassword, spassword, user_id;
    private ArrayList<User> resultList;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        sharedPreferences = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);

        user_id  = sharedPreferences.getString(UserID,"");

        editNewPassword = (EditText) findViewById(R.id.editNewPassword);
        editPassword = (EditText) findViewById(R.id.editPassword);

        btnUpdate = (LinearLayout) findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);

        resultList = new ArrayList<User>();
    }

    @Override
    public void onClick(View v) {

        if (v == btnUpdate) {
            sNewpassword = editNewPassword.getText().toString().trim();
            spassword = editPassword.getText().toString().trim();

            if (spassword.equals("")) {
                editPassword.setError("Enter current Password");
                return;
            }
            else if (sNewpassword.equals("")) {
                editPassword.setError("Enter new  Password");
                return;
            }
            else if (sNewpassword.equals(spassword)) {
                editNewPassword.setError("New Password cant be same as current");
                return;
            }else
            {
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

            dialog = new ProgressDialog(ChangePasswordActivity.this);
            dialog.setMessage("Please Wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected AllUser doInBackground(Void... voids) {

            AllUser result = new AllUser();
            result = new APICall().ChangePassword(user_id,sNewpassword,spassword);
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

                //  Toast.makeText(ChangePasswordActivity.this, resultList.get(0).getUser_id(), Toast.LENGTH_SHORT).show();

                if(result.contains("No"))
                {
                    if(msg.contains("Wrong"))
                    {
                        Toast.makeText(ChangePasswordActivity.this,"Current Password is wrong ",Toast.LENGTH_SHORT).show();
                    }
                    else if(msg.contains("NoData"))
                    {
                        Toast.makeText(ChangePasswordActivity.this,"Server Connection Error",Toast.LENGTH_SHORT).show();
                    }
                }
                else if(result.contains("Yes"))
                {
                    Toast.makeText(ChangePasswordActivity.this, "Password Updated", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            dialog.dismiss();
        }
    }
}