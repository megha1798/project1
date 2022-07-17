package com.servicepro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.PrivateKey;

public class AdminLogin extends AppCompatActivity {

    private Button logina;
    private EditText email,pass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        email =(EditText)findViewById(R.id.etEmailAdmin);
        pass=(EditText)findViewById(R.id.etPassAdmin);



        logina =(Button)findViewById(R.id.btnLoginAdmin);
        logina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email1 = email.getText().toString().trim();
                String password1 = pass.getText().toString().trim();
                if (email1.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Enter Email Address", Toast.LENGTH_SHORT).show();
                }
                else if (password1.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                }
                else if (email1.equals("admin") && password1.equals("admin"))
                {
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),AdminDashboard.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Enter Email Address", Toast.LENGTH_SHORT).show();
                }
            }






               /* Intent i =new Intent(AdminLogin.this,AdminDashboard.class);
                startActivity(i);*/
           // }
        });

    }
}