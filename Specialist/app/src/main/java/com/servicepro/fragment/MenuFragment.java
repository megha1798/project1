package com.servicepro.fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.servicepro.LoginActivity.Email;
import static com.servicepro.LoginActivity.Image;
import static com.servicepro.LoginActivity.MyPREFERENCES;
import static com.servicepro.LoginActivity.UserName;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.servicepro.API.APIResource;
import com.servicepro.AddComplain;
import com.servicepro.ChangePasswordActivity;
import com.servicepro.FeedbackAddActivity;
import com.servicepro.LoginActivity;
import com.servicepro.R;
import com.servicepro.RequestListActivity;


public class MenuFragment extends Fragment
{
    LinearLayout tv1;
    LinearLayout tv2;
    LinearLayout tv3;
    LinearLayout tv4;
    LinearLayout tv5;
    LinearLayout tv6;

    Context context;

    ImageView imgProfile;
    TextView tvName,tvEmail;

    View view2;
    private SharedPreferences sharedPreferences;
    private String image;
    private String user_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        tv1 = (LinearLayout) view.findViewById(R.id.lMenu1);
        tv2 = (LinearLayout) view.findViewById(R.id.lMenu2);
        tv3 = (LinearLayout) view.findViewById(R.id.lMenu3);
        tv4 = (LinearLayout) view.findViewById(R.id.lMenu4);
        tv5 = (LinearLayout) view.findViewById(R.id.lMenu5);
        tv6 = (LinearLayout) view.findViewById(R.id.lMenu6);

        tv1.setVisibility(View.GONE);

        view2 = (View) view.findViewById(R.id.view2);

        tvName = (TextView) view.findViewById(R.id.tvName);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        tvEmail.setVisibility(View.GONE);
        imgProfile = (ImageView) view.findViewById(R.id.imgUser);

        sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);

        user_name  = sharedPreferences.getString(UserName,"");
        image  = APIResource.BASE_URL+ sharedPreferences.getString(Image,"");
        String email  = sharedPreferences.getString(Email,"");

        tvName.setText("Welcome, \n"+user_name);

        Glide.with(getActivity())
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.user_default)
                .into(imgProfile);

        tvEmail.setText(email);

        listener();
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

            view2.setVisibility(View.VISIBLE);
            tv5.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(image)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.user_default)
                    .into(imgProfile);
            tvName.setText("Welcome, \n"+ user_name);

    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        this.context = context;
    }

    private void listener()
    {
        tv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i =new Intent(context, AddComplain.class);
                startActivity(i);

            }
        });


        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(context, RequestListActivity.class);
                startActivity(i);
            }
        });

        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(context, ChangePasswordActivity.class);
                startActivity(i);
            }
        });

        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(context, FeedbackAddActivity.class);
                startActivity(i);
            }
        });


        tv5.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Logout?");
                builder.setMessage("Are you sure want to Logout? ");
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        Intent i = new Intent(context, LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        ((Activity)context).finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }
}
