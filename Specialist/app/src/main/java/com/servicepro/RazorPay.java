package com.servicepro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class RazorPay extends AppCompatActivity implements PaymentResultListener {

    private Button buttonConfirmOrder;
    private EditText editTextPayment;
    private String amt;
    private String myamt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razor_pay);
        findview();
        listeners();
        Checkout.preload(getApplicationContext());
        amt=getIntent().getStringExtra("amount");

        //Log.e("amountLog","amount = "+amt);
       // Toast.makeText(this, ""+amt, Toast.LENGTH_SHORT).show();

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();

        if (bundle!=null){
            amt=bundle.getString("amount");
            editTextPayment.setText(amt);

           // Toast.makeText(this, ""+amt, Toast.LENGTH_SHORT).show();
        }

    }
    public void findview()
    {
        buttonConfirmOrder =(Button)findViewById(R.id.btnPay);
        editTextPayment =(EditText)findViewById(R.id.EditPayment);

    }

    public void  listeners()
    {
        buttonConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextPayment.getText().toString().equals(""))
                {
                    Toast.makeText(RazorPay.this,"Please Make Payment",Toast.LENGTH_LONG).show();
                    return;
                }
                startPayment();
            }
        });
    }
    public void startPayment()
    {
        final Activity activity=this;
        final Checkout co =new Checkout();
        try {
            JSONObject option =new JSONObject();
            option.put("name","Darshil and Mit Thakkar");
            option.put("description","Payable Amount");
            option.put("currency","INR");
            String Payment = editTextPayment.getText().toString();
            double total = Double.parseDouble(Payment);
            total =total*100;
            option.put("amount",total);

            JSONObject preFill =new JSONObject();
            preFill.put("Email","spectraskinfosoft@gmail.com");
            preFill.put("contact","7880161803");

            option.put("preFill",preFill);
            co.open(activity,option);

        } catch (JSONException e) {
            Toast.makeText(RazorPay.this,"Errorin Payment"+ e.getMessage(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(RazorPay.this,"Success fully Payment",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(RazorPay.this,"Errorin Payment not success",Toast.LENGTH_LONG).show();
    }
}