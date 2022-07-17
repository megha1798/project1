package com.servicepro;

import static com.servicepro.LoginActivity.Login;
import static com.servicepro.LoginActivity.MyPREFERENCES;
import static com.servicepro.LoginActivity.UserID;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.servicepro.API.APICall;
import com.servicepro.POJO.AllRequest;
import com.servicepro.POJO.AllUser;
import com.servicepro.POJO.Request;

import java.util.ArrayList;

public class RequestListActivity extends AppCompatActivity implements View.OnClickListener {

    private String user_id = "";
    private ArrayList<Request> resultList;

    private ListView listCodes;
    private ArrayList<Request> AllRequests;
    private ListAdapter adapter;

    private SharedPreferences sharedPreferences;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("Order List");

        resultList = new ArrayList<Request>();

        listCodes = (ListView) findViewById(R.id.llRequests);

        AllRequests = new ArrayList<Request>();

        adapter = new ListAdapter();
        listCodes.setAdapter(adapter);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        user_id = sharedPreferences.getString(UserID, "");

        if (!user_id.equals(""))
        {

            AllRequests.clear();
            adapter.notifyDataSetChanged();
            new GetList().execute();

        }
    }

    @Override
    public void onClick(View v)
    {

    }

    public class GetList extends AsyncTask<Void,Void, AllRequest>
    {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(RequestListActivity.this);
            dialog.setMessage("Loading..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected AllRequest doInBackground(Void... voids)
        {
            AllRequest result = new AllRequest();
            result = new APICall().GetUserRequests(user_id);
            return result;
        }

        @Override
        protected void onPostExecute(AllRequest allRequest) {


            if(allRequest !=null)
            {
                AllRequests.clear();
                AllRequests.addAll(allRequest.getData());
                adapter.notifyDataSetChanged();
            }
            else {
                Toast.makeText(RequestListActivity.this, "No History ", Toast.LENGTH_LONG).show();

            }

            super.onPostExecute(allRequest);
            dialog.dismiss();
        }
    }


    public class ListAdapter extends BaseAdapter
    {

        private ViewHolder Holder;

        @Override
        public int getCount() {
            return AllRequests.size();
        }

        @Override
        public Object getItem(int position) {
            return AllRequests.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView ==null)
            {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView= inflater.inflate(R.layout.layout_item_requests,parent,false);

                Holder = new ViewHolder();

                Holder.tvServiceName = (TextView) convertView.findViewById(R.id.tvServiceName);
                Holder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
                Holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
                Holder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
                Holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
                Holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
                Holder.tvEstimate = (TextView) convertView.findViewById(R.id.tvEstimate);

                convertView.setTag(Holder);
            }
            else
            {
                Holder = (ViewHolder) convertView.getTag();
            }

            Holder.tvServiceName.setText(AllRequests.get(position).getService_name());
            Holder.tvDescription.setText(AllRequests.get(position).getDescription());
            Holder.tvName.setText(AllRequests.get(position).getName());
            Holder.tvAddress.setText(AllRequests.get(position).getAddress()+ AllRequests.get(position).getArea());
            Holder.tvDate.setText(AllRequests.get(position).getService_date());
            Holder.tvTime.setText(AllRequests.get(position).getService_time());
            Holder.tvEstimate.setText(AllRequests.get(position).getEstimate_cost() + " INR ");



            return convertView;
        }
    }

    private static class ViewHolder
    {
        TextView tvTime, tvDate, tvAddress ,tvName ,tvDescription ,tvServiceName ,tvEstimate;
    }

}
