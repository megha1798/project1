package com.servicepro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.servicepro.API.APICall;
import com.servicepro.API.APIResource;
import com.servicepro.POJO.AllUser;
import com.servicepro.POJO.User;

import java.util.ArrayList;
import java.util.List;

public class UserHomeActivity extends AppCompatActivity {

    private ArrayList<User> AllService;
    private ServiceListAdapter adapter;
    private RecyclerView rvList;

    private DrawerLayout drawerLayout;
    boolean doubleBackToExitPressedOnce = false;

    private ImageView imgHomeIcon;
    Boolean mSlideState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        imgHomeIcon = findViewById(R.id.imgHomeIcon);

        AllService = new ArrayList<User>();

        rvList = findViewById(R.id.rvService);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(UserHomeActivity.this, 2);
        rvList.setLayoutManager(mLayoutManager);

        adapter = new ServiceListAdapter(UserHomeActivity.this, AllService);
        rvList.setAdapter(adapter);

        new GetList().execute();

        drawerLayout.addDrawerListener(new ActionBarDrawerToggle(UserHomeActivity.this, drawerLayout, 0, 0) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mSlideState = false;
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mSlideState = true;
            }
        });

        imgHomeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                openCloseDrawerMenu();
            }
        });
    }

    @SuppressLint("WrongConstant")
    public void openCloseDrawerMenu()
    {
        if(mSlideState)
        {
            drawerLayout.closeDrawer(Gravity.START);
        }
        else
        {
            drawerLayout.openDrawer(Gravity.START);
        }
    }

    @Override
    public void onBackPressed()
    {
        if (doubleBackToExitPressedOnce)
        {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back button again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public class GetList extends AsyncTask<Void,Void, AllUser>
    {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(UserHomeActivity.this);
            dialog.setMessage("Loading..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected AllUser doInBackground(Void... voids)
        {
            AllUser result = new AllUser();
            result = new APICall().GetServiceList("");
            return result;
        }

        @Override
        protected void onPostExecute(AllUser AllUser) {


            if(AllUser !=null)
            {
                AllService.clear();
                AllService.addAll(AllUser.getData());
                adapter.notifyDataSetChanged();
            }
            else
            {
                Toast.makeText(UserHomeActivity.this, "No Service Found ", Toast.LENGTH_LONG).show();
            }


            super.onPostExecute(AllUser);
            dialog.dismiss();
        }
    }

    public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.MyViewHolder>
    {
        private Context context;
        private List<User> serviceList;

        public ServiceListAdapter(Context context, List<User> serviceList)
        {
            this.context = context;
            this.serviceList = serviceList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.layout_item_service,null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i)
        {
            final User user = serviceList.get(i);

            myViewHolder.tvUserName.setText(user.getService_name());
            myViewHolder.tvServiceprice.setText(user.getFixrate( )+ " INR Onwards");

            Glide.with(context)
                    .load(APIResource.BASE_URL+ user.getImage())
                    .into(myViewHolder.imgUser);

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(context, ServiceRequestAddActivity.class);
                    intent.putExtra("service_id", serviceList.get(i).getService_id());
                    intent.putExtra("service_name", serviceList.get(i).getService_name());
                    intent.putExtra("estimate_cost", serviceList.get(i).getFixrate());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return serviceList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder
        {
            TextView tvUserName ,tvServiceprice;
            ImageView imgUser;

            public MyViewHolder(@NonNull View itemView)
            {
                super(itemView);

                tvUserName = itemView.findViewById(R.id.tvService);
                tvServiceprice = itemView.findViewById(R.id.tvServiceprice);
                imgUser = itemView.findViewById(R.id.imgService);

            }
        }
    }
}