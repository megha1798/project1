package com.servicepro.API;

import android.util.Log;

import com.google.gson.Gson;
import com.servicepro.POJO.AllRequest;
import com.servicepro.POJO.AllServiceType;
import com.servicepro.POJO.AllUser;
import com.servicepro.POJO.allcity;
import com.servicepro.POJO.allrequirement;
import com.servicepro.POJO.allstaff;
import com.servicepro.POJO.alltask;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class APICall
{
    public AllUser GetLogin(String email, String password)
    {
        AllUser result = new AllUser();
        HTTPCall httpCall = new HTTPCall();

        FormBody.Builder builder = new FormBody.Builder()
                .add("email",email)
                .add("password",password);

        RequestBody body = builder.build();
        String output = httpCall.POST(APIResource.LOGIN_URL,body);
        Log.d("Rittz",output);
        result = new Gson().fromJson(output,AllUser.class);
        return result;
    }

    public AllUser PostRegister(String name, String email, String mobile,  String pass)
    {
        AllUser result = new AllUser();
        HTTPCall httpCall = new HTTPCall();

        FormBody.Builder builder = new FormBody.Builder()
                .add("name",name)
                .add("email",email)
                .add("mobile",mobile)
                .add("password",pass);


        RequestBody body = builder.build();
        String output = httpCall.POST(APIResource.REGISTER_URL,body);
        Log.d("Rittz",output);
        result = new Gson().fromJson(output,AllUser.class);
        return result;
    }




    public AllUser SendOTPMail(String user_email , String msg)
    {
        AllUser result = new AllUser();
        HTTPCall httpCall = new HTTPCall();

        FormBody.Builder builder = new FormBody.Builder()
                .add("user_email",user_email)
                .add("email_msg",msg);

        RequestBody body = builder.build();
        String output = httpCall.POST(APIResource.OPT_EMAIL_URL,body);

        try {
            Log.d("Rittz",output);
        }
        catch (Exception e)
        {
            Log.e("Rittz",e.getMessage());
        }
        result = new Gson().fromJson(output,AllUser.class);
        return result;

    }

    public AllUser ForgotPassword(String email)
    {
        AllUser result = new AllUser();
        HTTPCall httpCall = new HTTPCall();

        FormBody.Builder builder = new FormBody.Builder()
                .add("email",email);

        RequestBody body = builder.build();
        String output = httpCall.POST(APIResource.FORGOT_URL,body);
        Log.d("Rittz",output);
        result = new Gson().fromJson(output,AllUser.class);
        return result;
    }

    public AllUser GetServiceList(String city_id)
    {
        AllUser result = new AllUser();
        HTTPCall httpCall = new HTTPCall();

        FormBody.Builder builder = new FormBody.Builder()
                .add("city_id",city_id);

        RequestBody body = builder.build();
        String output = httpCall.POST(APIResource.GET_SERVICE_LIST,body);
        Log.d("Rittz",output);
        result = new Gson().fromJson(output,AllUser.class);
        return result;
    }

    public AllUser PostRequest(String user_id , String service_id, String description ,String name ,String address ,
            String area , String estimate_cost ,String pickup_date , String pickup_time,String mobile)
    {
        AllUser result = new AllUser();
        HTTPCall httpCall = new HTTPCall();

        FormBody.Builder builder = new FormBody.Builder()
                .add("user_id",user_id)
                .add("service_id",service_id)
                .add("description",description)
                .add("name",name)
                .add("address",address)
                .add("area",area)
                .add("estimate_cost",estimate_cost)
                .add("service_date",pickup_date)
                .add("service_time",pickup_time)
                .add("mobile",mobile);

        RequestBody body = builder.build();
        String output = httpCall.POST(APIResource.ADD_SERVICE_REQUEST,body);
        Log.d("Rittz",output);
        result = new Gson().fromJson(output,AllUser.class);
        return result;
    }

    public AllRequest GetUserRequests(String user_id)
    {
        AllRequest result = new AllRequest();
        HTTPCall httpCall = new HTTPCall();

        FormBody.Builder builder = new FormBody.Builder()
                .add("user_id",user_id);

        RequestBody body = builder.build();
        String output = httpCall.POST(APIResource.GET_USER_REQUEST_URL,body);
        Log.d("Rittz",output);
        result = new Gson().fromJson(output,AllRequest.class);
        return result;
    }

    public AllRequest GetStaffTask(String user_id)
    {
        AllRequest result = new AllRequest();
        HTTPCall httpCall = new HTTPCall();

        FormBody.Builder builder = new FormBody.Builder()
                .add("user_id",user_id);

        RequestBody body = builder.build();
        String output = httpCall.POST(APIResource.GET_STAFF_TASK_URL,body);
        Log.d("Rittz",output);
        result = new Gson().fromJson(output,AllRequest.class);
        return result;
    }

    public AllUser PostReport(String user_id , String Staff_id, String Report_Description ,String Requirement_Id ,String TaskAssignId,String amounts)
    {
        AllUser result = new AllUser();
        HTTPCall httpCall = new HTTPCall();

        FormBody.Builder builder = new FormBody.Builder()
                .add("user_id",user_id)
                .add("Staff_id",Staff_id)
                .add("Report_Description",Report_Description)
                .add("Requirement_Id",Requirement_Id)
                .add("TaskAssignId",TaskAssignId)
                .add("TotalAmt",amounts);

        RequestBody body = builder.build();
        String output = httpCall.POST(APIResource.ADD_SERVICE_REPORT,body);
        Log.d("Rittz",output);
        result = new Gson().fromJson(output,AllUser.class);
        return result;
    }

    public AllRequest GetStaffReport(String user_id)
    {
        AllRequest result = new AllRequest();
        HTTPCall httpCall = new HTTPCall();

        FormBody.Builder builder = new FormBody.Builder()
                .add("user_id",user_id);

        RequestBody body = builder.build();
        String output = httpCall.POST(APIResource.GET_STAFF_REPORT_URL,body);
        Log.d("Rittz",output);
        result = new Gson().fromJson(output,AllRequest.class);
        return result;
    }

    public AllUser PostFeedback(String Feedback_Title , String Description, String EmailId )
    {
        AllUser result = new AllUser();
        HTTPCall httpCall = new HTTPCall();

        FormBody.Builder builder = new FormBody.Builder()
                .add("Feedback_Title",Feedback_Title)
                .add("Description",Description)
                .add("EmailId",EmailId);

        RequestBody body = builder.build();
        String output = httpCall.POST(APIResource.POST_FEED_URL,body);
        Log.d("Rittz",output);
        result = new Gson().fromJson(output,AllUser.class);
        return result;
    }

    public AllUser ChangePassword(String user_id, String newPass,String password)
    {
        AllUser result = new AllUser();
        HTTPCall httpCall = new HTTPCall();

        FormBody.Builder builder = new FormBody.Builder()
                .add("user_id",user_id)
                .add("newPass",newPass)
                .add("password",password);

        RequestBody body = builder.build();
        String output = httpCall.POST(APIResource.CHANGE_PASS_URL,body);
        Log.d("Rittz",output);
        result = new Gson().fromJson(output,AllUser.class);
        return result;
    }


    public allcity GetCity()
    {
        allcity result = new allcity();
        HTTPCall httpCall = new HTTPCall();
        String output  =httpCall.GET(APIResource.GET_CITY_URL);
        result = new Gson().fromJson(output,allcity.class);
        return result;
    }

    public allstaff GetStaff()
    {
        allstaff result = new allstaff();
        HTTPCall httpCall = new HTTPCall();
        String output  =httpCall.GET(APIResource.GET_STAFF_URL);

            result = new Gson().fromJson(output,allstaff.class);


        return result;
    }
    public allrequirement GetRequirement()
    {
        allrequirement result = new allrequirement();
        HTTPCall httpCall = new HTTPCall();
        String output  =httpCall.GET(APIResource.GET_REQUIREMENT_URL);

        result = new Gson().fromJson(output,allrequirement.class);


        return result;
    }


    public AllUser PostComplain(String Complain_Title , String Description, String EmailId )
    {
        AllUser result = new AllUser();
        HTTPCall httpCall = new HTTPCall();

        FormBody.Builder builder = new FormBody.Builder()
                .add("Complain_Title",Complain_Title)
                .add("Description",Description)
                .add("EmailId",EmailId);

        RequestBody body = builder.build();
        String output = httpCall.POST(APIResource.POST_COMPLAIN_URL,body);
        Log.d("Rittz",output);
        result = new Gson().fromJson(output,AllUser.class);
        return result;
    }

    public AllUser addcity(String CityName)
    {
        AllUser result = new AllUser();
        HTTPCall httpCall = new HTTPCall();

        FormBody.Builder builder = new FormBody.Builder()

                .add("City_Name",CityName);


        RequestBody body = builder.build();
        String output = httpCall.POST(APIResource.Addcity_URL,body);
        //Log.d("Rittz",output);
        result = new Gson().fromJson(output,AllUser.class);
        return result;


    }



    public alltask PostTaskDet(String srequirement , String sstaff, String sdate1)
    {
        alltask result = new alltask();
        HTTPCall httpCall = new HTTPCall();

        FormBody.Builder builder = new FormBody.Builder()

                .add("Requirement_Id",srequirement)
                .add("Staff_id",sstaff)
                .add("Sdate",sdate1);


        RequestBody body = builder.build();
        String output = httpCall.POST(APIResource.AddTASK_URL,body);
        //Log.d("Rittz",output);
        result = new Gson().fromJson(output,alltask.class);
        return result;


    }





    public AllUser adddesignation(String Designation)
    {
        AllUser result = new AllUser();
        HTTPCall httpCall = new HTTPCall();

        FormBody.Builder builder = new FormBody.Builder()

                .add("Designation",Designation);


        RequestBody body = builder.build();
        String output = httpCall.POST(APIResource.Adddesignation_URL,body);
        Log.d("Rittz",output);
        result = new Gson().fromJson(output,AllUser.class);
        return result;
    }

    public AllUser postServiceType(String ser_name,String ser_desc,String img_path,String f_rate)
    {
        AllUser result = new AllUser();
        HTTPCall httpCall = new HTTPCall();

        FormBody.Builder builder = new FormBody.Builder()

                .add("Service_Name",ser_name)
                .add("Service_Desc",ser_desc)
                .add("Image_Path",img_path)
                .add("Rate",f_rate);


        RequestBody body = builder.build();
        String output = httpCall.POST(APIResource.Addservicetype_URL,body);
        Log.d("Rittz",output);
        result = new Gson().fromJson(output,AllUser.class);
        return result;
    }

    public AllServiceType GetServiceType()
    {
        AllServiceType result = new AllServiceType();
        HTTPCall httpCall = new HTTPCall();
        String output  =httpCall.GET(APIResource.GET_SERVICETYPE_URL);
        result = new Gson().fromJson(output,AllServiceType.class);
        return result;
    }



    public allstaff PostRegister1(String name, String email, String mobile,  String pass,String scity,String sst)
    {
        allstaff result = new allstaff();
        HTTPCall httpCall = new HTTPCall();

        FormBody.Builder builder = new FormBody.Builder()
                .add("name",name)
                .add("email",email)
                .add("mobile",mobile)
                .add("password",pass)
                .add("scity",scity)
                .add("sst",sst);


        RequestBody body = builder.build();
        String output = httpCall.POST(APIResource.REGISTER1_URL,body);
        Log.d("Rittz",output);
        result = new Gson().fromJson(output,allstaff.class);
        return result;
    }


}
