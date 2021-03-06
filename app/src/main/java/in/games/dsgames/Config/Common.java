package in.games.dsgames.Config;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.games.dsgames.Activity.MainActivity;
import in.games.dsgames.Adapter.ListItemAdapter;
import in.games.dsgames.AppController;
import in.games.dsgames.Interface.OnGetConfigData;
import in.games.dsgames.Interface.OnInsertion;
import in.games.dsgames.Interface.OnSuccess;
import in.games.dsgames.Model.ConfigDataModel;
import in.games.dsgames.Model.MatkaModel;
import in.games.dsgames.Model.MatkasObjects;
import in.games.dsgames.Model.StarlineModel;
import in.games.dsgames.Model.Starline_Objects;
import in.games.dsgames.Model.TableModel;
import in.games.dsgames.Model.WalletObjects;
import in.games.dsgames.OnGetMatka;
import in.games.dsgames.OnGetStarline;
import in.games.dsgames.R;
import in.games.dsgames.utils.CustomJsonRequest;
import in.games.dsgames.utils.CustomVolleyJsonArrayRequest;
import in.games.dsgames.utils.LoadingBar;
import in.games.dsgames.utils.Session_management;

import static in.games.dsgames.Config.BaseUrl.URL_GET_SINGLE_STARLINE;
import static in.games.dsgames.Config.BaseUrl.URL_INDEX;
import static in.games.dsgames.Config.BaseUrl.URl_GET_SINGLE_MATKA;
import static in.games.dsgames.Config.Constants.KEY_ID;

public class Common {
    Context context;
    Session_management session_management;
    LoadingBar loadingBar;
    public static String tagline,withdrw_text,withdrw_no,whatsapp_no,home_text,min_add_amount,min_withdraw_amount,msg_status,app_link,share_link,ver_code,dialog_image,call_no,min_bid_amount,forgot_whatsapp,forgot_text;
    public Common(Context context) {
        this.context = context;
        session_management=new Session_management(context);
        loadingBar = new LoadingBar(context);
    }

    public void showToast(String s)
    {
        Toast.makeText(context,""+s, Toast.LENGTH_SHORT).show();

    }


    public void setMobileNumber(final TextView txt)
    {
        String json_tag_request="json_mobile_request";
        HashMap<String, String> params=new HashMap<String, String>();
        CustomJsonRequest customVolleyJsonArrayRequest=new CustomJsonRequest(Request.Method.GET, BaseUrl.URL_MOBILE, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try
                {
                    JSONObject object=response;
                    String mobile=object.getString("mobile");
                    int m_cnt= Integer.parseInt(object.getString("count"));
                    txt.setText(mobile);
                    Prevalent.Matka_count=m_cnt;
                }
                catch (Exception ex)
                {
                    Toast.makeText(context,""+ex.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg=VolleyErrorMessage(error);
                errorMessageDialog(msg);

            }
        });

        AppController.getInstance().addToRequestQueue(customVolleyJsonArrayRequest,json_tag_request);
    }

    public String VolleyErrorMessage(VolleyError error) {
        String str_error = "";
        if (error instanceof TimeoutError) {
            str_error = "Connection Timeout";
        } else if (error instanceof AuthFailureError) {
            str_error = "Session Timeout";
            //TODO
        } else if (error instanceof ServerError) {
            str_error = "Server not responding please try again later";
            //TODO
        } else if (error instanceof NetworkError) {
            str_error = "No Internet Connection";
            //TODO
        } else if (error instanceof ParseError) {
            //TODO
            str_error = "An Unknown error occur";
        } else if (error instanceof NoConnectionError) {
            str_error = "No Internet Connection";
        } else {
            str_error = "Something Went Wrong";
        }

        return str_error;
    }

    public void errorMessageDialog(String message)
    {

                    final Dialog dialog=new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_error_message_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    TextView txtMessage=(TextView)dialog.findViewById(R.id.txtmessage);
                    Button btnOk=(Button)dialog.findViewById(R.id.btnOK);
                    dialog.setCanceledOnTouchOutside(false);
                    if(dialog.isShowing())
                    {
                        dialog.dismiss();
                    }
                    dialog.show();

                    txtMessage.setText(message);

                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });


    }


    public void setWallet_Amount(final TextView txt, final LoadingBar progressDialog, final String mid) {
        progressDialog.show();
        String json_tag_request = "json_wallet_tag";
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id", mid);
        CustomVolleyJsonArrayRequest customJsonRequest = new CustomVolleyJsonArrayRequest(Request.Method.POST, BaseUrl.URL_GET_WALLET_AMOUNT, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("wallet",response.toString()+"\n"+params);

                JSONObject object = null;
                try {
                    if (response.length()>0) {
                        object = response.getJSONObject(0);

                        WalletObjects walletObjects = new WalletObjects();
                        walletObjects.setUser_id(object.getString("user_id"));
                        walletObjects.setWallet_points(object.getString("wallet_points"));
                        walletObjects.setWallet_id(object.getString("wallet_id"));
                        String w = "";
                        if (checkNull(object.getString("wallet_points"))) {
                            w = "0";
                        } else {
                            w = object.getString("wallet_points");
                        }
                        session_management.updateWallet(w);

                        txt.setText(walletObjects.getWallet_points());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                String msg=VolleyErrorMessage(error);
                errorMessageDialog(msg);

                // Toast.makeText(context, "Error :" + error.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }
        });

        AppController.getInstance().addToRequestQueue(customJsonRequest, json_tag_request);
    }
public void setGameDate(TextView tv_date ,String time)
{
    String date=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        Log.e("c_date",date+"\n diff"+getTimeDifference(time));
        if (getTimeDifference(time)<0)
        { tv_date.setText(getNextDate(date));
        Log.e("nextdate",getNextDate(date));
        }
        else
        {
            tv_date.setText(date);
        }


}
    public long getTimeDifference(String time) {
        long diff_e_s=0;
        Date date = new Date();
        SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm:ss");
        String cur_time = parseFormat.format(date);
        try {
            final Date s_time = parseFormat.parse(cur_time.trim());
            Date e_time = parseFormat.parse(time.trim());
            diff_e_s = e_time.getTime() - s_time.getTime();
            Log.e("dddddd","curr - "+s_time.toString()+" -end - "+e_time.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return diff_e_s;
    }


    public String getSumOfPoints(List<TableModel> list) {
        int sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum = sum + Integer.parseInt(list.get(i).getPoints());
        }

        return String.valueOf(sum);
    }

    public void postRequest(String url, final HashMap<String, String> params, Response.Listener<String> listener, Response.ErrorListener errorListener){

        Log.e("url", ""+url );
        StringRequest stringRequest=new StringRequest(Request.Method.POST,url,listener,errorListener){
            @Override
            protected Map<String, String> getParams(){
                Log.e("params", ""+params );
                return params;
            }
        };

        RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                Constants.REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(mRetryPolicy);
        AppController.getInstance().addToRequestQueue(stringRequest,"req");

    }
    public void showTimeoutDialog(String message)
    {

        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.timeout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        TextView txtMessage=(TextView)dialog.findViewById(R.id.txtmessage);
        Button btnOk=(Button)dialog.findViewById(R.id.btnOK);
        ImageView iv_close =(ImageView) dialog.findViewById(R.id.iv_close);

        dialog.setCanceledOnTouchOutside(false);
        if(dialog.isShowing())
        {
            dialog.dismiss();
        }
        dialog.show();

//        txtMessage.setText("Withdrawal request timing is : \n Monday to Saturday 10:00 am to 03:00pm.  ");
        txtMessage.setText(message);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }
    public void showVolleyError(VolleyError error)
    {
        String msg=VolleyErrorMessage(error);
        if(!msg.isEmpty())
        {
            showToast(""+msg);
        }
    }
    public void insertData(List<TableModel> list, String m_id, String c, String game_id, String w, String dashName, LoadingBar progressDialog, Button btnSave, final String start_time, final String end_time) {
//        int er = list.size();
//        if (er <= 0) {
//            String message = "Please Add Some Bids";
//            errorMessageDialog(message);
//            return;
//        } else {
            try {
                int amt = 0;
                ArrayList list_digits = new ArrayList();
                ArrayList list_type = new ArrayList();
                ArrayList list_points = new ArrayList();
                int rows = list.size();

                for (int i = 0; i < rows; i++) {


                    TableModel tableModel = list.get(i);

                    String asd = tableModel.getDigits().toString();
                    String asd1 = tableModel.getPoints().toString();
                    String asd2 = tableModel.getType().toString();
                    int b = 9;

                    if (asd2.equalsIgnoreCase("Close")) {
                        b = 1;
                    } else if (asd2.equalsIgnoreCase("Open")) {
                        b = 0;
                    }


                    amt = amt + Integer.parseInt(asd1);

                    char quotes = '"';
                    list_digits.add(quotes + asd + quotes);
                    list_points.add(asd1);
                    list_type.add(b);

          }


//                String id = Prevalent.currentOnlineuser.getId().toString().trim();
                String s_id = new Session_management(context).getUserDetails().get(KEY_ID).toString().trim();
                String matka_id = m_id.toString().trim();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("points", list_points);
                jsonObject.put("digits", list_digits);
                jsonObject.put("bettype", list_type);
                jsonObject.put("user_id", s_id);
                jsonObject.put("matka_id", matka_id);
                jsonObject.put("game_date", c);
                jsonObject.put("game_id", game_id);

                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObject);

                int wallet_amount = Integer.parseInt(w);
                if (wallet_amount < amt) {

                    String message = "Insufficient Amount";
                    errorMessageDialog(message);
                    return;

                } else {
                    btnSave.setEnabled(false);

                    updateWalletAmount(jsonArray, progressDialog, dashName, m_id,start_time,end_time);

                }
            } catch (Exception ex) {
               Log.e("" , ex.getMessage());
            }

//        }

    }

    public String get24To12Format(String timestr)
    {
        String tm="";
        SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");

        try {
            Date _24Hourst = _24HourSDF.parse(timestr);
            tm = _12HourSDF.format(_24Hourst);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return tm;
    }


    //Function for get Next Day
    public String getNextDay(String currentDate)
    {
        String nextDate="";

        try
        {
            Calendar calendar= Calendar.getInstance();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEEE");
            Date c=simpleDateFormat.parse(currentDate);
            calendar.setTime(c);
            calendar.add(Calendar.DAY_OF_WEEK,1);
            nextDate=simpleDateFormat.format(calendar.getTime());

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            //Toast.makeText(OddEvenActivity.this,""+ex.getMessage(),Toast.LENGTH_LONG).show();
        }

        return nextDate.toString();
    }
    public void setBidsDialog(int wallet_amount , final List<TableModel> list, final String m_id, final String c, final String  game_id, final String w, final String dashName, final LoadingBar progressDialog, final Button btnSave,final String start_time,final String end_time)
    {
        TableRow tr1;
        ListItemAdapter listItemAdapter = new ListItemAdapter(list,context);
        TextView txtD1,txtP1,txtT1;

        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.starline_save_layout);
        if(dialog.isShowing())
        {
            dialog.dismiss();
        }
//        dialog.show();
        final TableLayout tableLayout=(TableLayout)dialog.findViewById(R.id.tblLayout1);
        ListView listView = dialog.findViewById(R.id.list_item);
        Button btn_dialog_add=(Button)dialog.findViewById(R.id.btnOk);
        TextView btnDialogCancel=(Button)dialog.findViewById(R.id.btnCancel);
        TextView txtCountBids=(TextView)dialog.findViewById(R.id.txtCountBids);
        TextView txtAmount=(TextView)dialog.findViewById(R.id.txtAmount);
        TextView txtBeforeAmount=(TextView)dialog.findViewById(R.id.txtBeforeAmount);
        TextView txtAfterAmount=(TextView)dialog.findViewById(R.id.txtAfterAmount);
        TextView matka_name = dialog.findViewById( R.id.matka_name );
        matka_name.setText( dashName );

        listView.setAdapter(listItemAdapter);

        int amt=0;
        for(int j=0;j<list.size();j++)
        {
            amt=amt+Integer.parseInt(list.get(j).getPoints());
        }

        int bid_count=list.size();
        txtCountBids.setText(String.valueOf(String.valueOf(bid_count)));
        txtAmount.setText(String.valueOf(amt));
        int w_a=wallet_amount;
        int after_amt=w_a-amt;
        txtBeforeAmount.setText(String.valueOf(w_a));
        txtAfterAmount.setText(String.valueOf(after_amt));
        btn_dialog_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        insertData(list,m_id,c,game_id,w,dashName,progressDialog,btnSave,start_time,end_time);

            }
        });

        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                list.clear();
            }
        });
        dialog.show();
    }

    // Function for get Next Date
    public String getNextDate(String currentDate)
    {
        String nextDate="";
        try
        {
            Calendar calendar= Calendar.getInstance();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
            Date c=simpleDateFormat.parse(currentDate);
            calendar.setTime(c);
            calendar.add(Calendar.DAY_OF_WEEK,1);
            nextDate=simpleDateFormat.format(calendar.getTime());

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            //Toast.makeText(OddEvenActivity.this,""+ex.getMessage(),Toast.LENGTH_LONG).show();
        }

        return nextDate.toString();
    }

    public MatkaModel getModelFromIntent(String str){
        Gson gson=new Gson();
        MatkaModel m=(MatkaModel) gson.fromJson(str.toString(),MatkaModel.class);
        return m;
    }
    public String getStringFromModel(MatkasObjects m){
        Gson gson=new Gson();
        return gson.toJson(m);
    }
    public String getPreviousDate()
    {
        String nextDate="";
        try
        {

            Calendar calendar= Calendar.getInstance();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
            String currentDate=simpleDateFormat.format(new Date());
            Date c=simpleDateFormat.parse(currentDate);
            calendar.setTime(c);
            calendar.add(Calendar.DAY_OF_WEEK,-1);
            nextDate=simpleDateFormat.format(calendar.getTime());

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            //Toast.makeText(OddEvenActivity.this,""+ex.getMessage(),Toast.LENGTH_LONG).show();
        }

        return nextDate.toString();
    }

    public String getCDate(){
        String str="";
        //06/01/2021
        try{
            Date date=new Date();
            SimpleDateFormat smf=new SimpleDateFormat("dd/MM/yyyy");
            str=smf.format(date);
        }catch (Exception ex){
            ex.printStackTrace();

        }
        return str;
    }

    public void setOpenSheetData(List<TableModel> list, String m_id, String c, String w, final OnSuccess onSuccess) {
        loadingBar.show();

        int er = list.size();
        if (er <= 0) {
            loadingBar.dismiss();
            String message = "No Lottery selected";
            errorMessageDialog(message);
            return;
        } else {
            String game_id="";
            try {
                int amt = 0;
                ArrayList list_digits = new ArrayList();
                ArrayList list_type = new ArrayList();
                ArrayList list_points = new ArrayList();
                ArrayList list_game_id = new ArrayList();
                int rows = list.size();

                for (int i = 0; i < rows; i++) {


                    TableModel tableModel = list.get(i);

                    String asd = tableModel.getDigits().toString();
                    String asd1 = tableModel.getPoints().toString();
                    String asd2 = tableModel.getType().toString();
                    int b = 9;

                    if (asd2.equals("Bahar")) {
                        b = 1;
                        game_id="1";
                    } else if (asd2.equals("Andar")) {
                        b = 0;
                        game_id="1";
                    }else if( asd2.equals("Jodi")){
                        b = 1;
                        game_id="2";
                    }else if(asd2.equals("Spin")){
                        b=0;
                        game_id="3";
                    }


                    amt = amt + Integer.parseInt(asd1);

                    char quotes = '"';
                    list_digits.add(quotes + asd + quotes);
                    list_game_id.add(quotes + game_id + quotes);
                    list_points.add(asd1);
                    list_type.add(b);

                }


                String id = session_management.getUserDetails().get(KEY_ID).toString().trim();
                String matka_id = m_id.toString().trim();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("points", list_points);
                jsonObject.put("digits", list_digits);
                jsonObject.put("bettype", list_type);
                jsonObject.put("user_id", id);
                jsonObject.put("matka_id", matka_id);
                jsonObject.put("game_date", c);
                jsonObject.put("game_id", game_id);

                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObject);

                int wallet_amount = Integer.parseInt(w);
//                showToast(""+amt+"\n"+wallet_amount);
                if (wallet_amount < amt) {
                    loadingBar.dismiss();
                    String message = "Insufficient Amount";
                    errorMessageDialog(message);
                    return;

                } else {

                    insertionData(jsonArray, matka_id, new OnInsertion() {
                        @Override
                        public void onInsertion(String msg) {
                            showToast("Bid Added");
//                            showSuccessToast("Bid Added.");
                            onSuccess.onSuccess(msg);
                        }
                    });


                }
            } catch (Exception ex) {
                loadingBar.dismiss();
                Toast.makeText(context, "Err" + ex.getMessage(), Toast.LENGTH_LONG).show();
            }

        }

    }
    private void insertionData(JSONArray jsonArray, String matka_id, final OnInsertion onInsertion) {
        final String data=String.valueOf(jsonArray);
        String json_request_tag="json_insert_request";
        HashMap<String,String> params=new HashMap<String, String>();
        params.put("data",data);
        Log.e("asdasd",""+data.toString());
        postRequest(BaseUrl.URL_INSERT_DATA, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String resp) {
                loadingBar.dismiss();
                Log.e("insertionData",resp.toString());
                try {
                    JSONObject object=new JSONObject(resp);
                    if(object.getString("status").equalsIgnoreCase("success")){
                        onInsertion.onInsertion("success");
                    }else{
                        errorMessageDialog("Someting Went Wrong");
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showVolleyError(error);
            }
        });
    }

    public void currentDateDay(TextView btn)
    {
        String date=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        Date date1=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEEE");
        String day =simpleDateFormat.format(date1);
        btn.setText(date);

    }

  public  String  get24Hours(String time)
    {
        String t[]=time.split(" ");
        String time_type=t[1].toString();
        String t1[]=t[0].split(":");

        int tm=Integer.parseInt(t1[0].toString());

        if(time_type.equalsIgnoreCase("PM") || time_type.equalsIgnoreCase("p.m"))
        {
            if(tm==12)
            {

            }
            else
            {
                tm=12+tm;
            }
        }

//       String s ="";
//       String h = time.substring(0,1);
//       if (time.contains("PM")|| time.contains("p.m"))
//       {
//       int hours = Integer.parseInt(h);
//       if (hours<12)
//       {
//          hours =hours+12;
//       }
        String s = String.valueOf(tm)+":"+t1[1]+":00";

        return s;
    }
    public void  setCounterTimer(long diff,final TextView txt_timer)
    {
        CountDownTimer countDownTimer = new CountDownTimer(diff, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String text = String.format(Locale.getDefault(), " %02d : %02d : %02d ",

                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 60, TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                txt_timer.setText(text);

            }

            @Override
            public void onFinish() {

                txt_timer.setText("Bid Closed");

            }
        }.start();

    }

    public void setEndCounterTimer(long diff,final TextView txt_timer)
    {
        CountDownTimer countDownTimer = new CountDownTimer(diff, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String text = String.format(Locale.getDefault(), " %02d : %02d : %02d ",

                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 60, TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);

                txt_timer.setText(text);
            }
            @Override
            public void onFinish() {
                txt_timer.setText("Bid Closed");
            }
        }.start();

    }

    public void setBetDateDay(final String m_id, final Button btnGameType, final LoadingBar progressDialog)
    {
        String json_request_tag="matka_with_id";
        HashMap<String, String> params=new HashMap<String, String>();
        params.put("id",m_id);
        progressDialog.show();

        CustomJsonRequest customJsonRequest=new CustomJsonRequest(Request.Method.POST, BaseUrl.URL_MATKA_WITH_ID, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try
                {
                    String status=response.getString("status");
                    if(status.equals("success"))
                    {
                        JSONObject jsonObject=response.getJSONObject("data");
                        MatkasObjects matkasObjects = new MatkasObjects();
                        matkasObjects.setId(jsonObject.getString("id"));
                        matkasObjects.setName(jsonObject.getString("name"));
                        matkasObjects.setStart_time(jsonObject.getString("start_time"));
                        matkasObjects.setEnd_time(jsonObject.getString("end_time"));
                        matkasObjects.setStarting_num(jsonObject.getString("starting_num"));
                        matkasObjects.setNumber(jsonObject.getString("number"));
                        matkasObjects.setEnd_num(jsonObject.getString("end_num"));
                        matkasObjects.setBid_start_time(jsonObject.getString("bid_start_time"));
                        matkasObjects.setBid_end_time(jsonObject.getString("bid_end_time"));
                        matkasObjects.setCreated_at(jsonObject.getString("created_at"));
                        matkasObjects.setUpdated_at(jsonObject.getString("updated_at"));
                        matkasObjects.setSat_start_time(jsonObject.getString("sat_start_time"));
                        matkasObjects.setSat_end_time(jsonObject.getString("sat_end_time"));
                        matkasObjects.setStatus(jsonObject.getString("status"));

                        String dt=new SimpleDateFormat("EEEE").format(new Date());
                        String bid_start = "";
                        String bid_end="";
//                        String bid_start = matkasObjects.getBid_start_time();
//                        String bid_end=matkasObjects.getBid_end_time().toString();

                        if(dt.equals("Sunday"))
                        {
                            bid_start=matkasObjects.getStart_time();
                            bid_end=matkasObjects.getEnd_time();
                        }
                        else if(dt.equals("Saturday"))
                        {
                            bid_start=matkasObjects.getSat_start_time();
                            bid_end=matkasObjects.getSat_end_time();

                        }
                        else
                        {
                            bid_start=matkasObjects.getBid_start_time();
                            bid_end=matkasObjects.getBid_end_time();

                        }


                        String time1 = bid_start.toString();
                        String time2 = bid_end.toString();

                        Date cdate=new Date();



                        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                        String time3=format.format(cdate);
                        Date date1 = null;
                        Date date2=null;
                        Date date3=null;
                        try {
                            date1 = format.parse(time1);
                            date2 = format.parse(time2);
                            date3=format.parse(time3);
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                        long difference = date3.getTime() - date1.getTime();
                        long as=(difference/1000)/60;

                        long diff_close=date3.getTime()-date2.getTime();
                        long c=(diff_close/1000)/60;
                        Log.e("dataaaa",""+c);
                        Date c_dat=new Date();
                        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy EEEE");
                        String s_dt=dateFormat.format(c_dat);
                        String n_dt= getNextDate(s_dt);
                        String a_dt= getNextDate(n_dt);

                        if(c>0)
                        {progressDialog.dismiss();
                            btnGameType.setText(s_dt+" Bet Close");


                            // Toast.makeText(OddEvenActivity.this,""+s_dt+"  Close",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            btnGameType.setText(s_dt+" Bet Open");

                        }


//                        }


                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(context,"Something wrong", Toast.LENGTH_LONG).show();


                    }

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    Toast.makeText(context,"something went wrong ", Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                String msg=VolleyErrorMessage(error);
                errorMessageDialog(msg);
            }
        });
        AppController.getInstance().addToRequestQueue(customJsonRequest,json_request_tag);
    }

    public void getStarlineGameData(final String m_id, final Button btnType, final LoadingBar progressDialog )
    {
        progressDialog.show();
        final Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        final String ctt = dateFormat.format(date);

        String json_tag="json_starline";
        HashMap<String, String> params=new HashMap<>();
        params.put("id",m_id);

        CustomJsonRequest customJsonRequest=new CustomJsonRequest(Request.Method.POST, BaseUrl.URL_StarLine_id, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try
                {
                    JSONObject jsonObject=response;
                    Starline_Objects matkasObjects=new Starline_Objects();
                    matkasObjects.setId(jsonObject.getString("id"));
                    matkasObjects.setS_game_time(jsonObject.getString("s_game_time"));
                    matkasObjects.setS_game_number(jsonObject.getString("s_game_number"));

                    progressDialog.dismiss();
                    btnType.setText(ctt+"-"+matkasObjects.getS_game_time());

                }
                catch(Exception ex)
                {
                    progressDialog.dismiss();
                    Toast.makeText(context,"Something "+ex.getMessage(), Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                String msg=VolleyErrorMessage(error);
                errorMessageDialog(msg);
            }
        });
        AppController.getInstance().addToRequestQueue(customJsonRequest,json_tag);



    }


    public int getTimeFormatStatus(String time)
    {
        //02:00 PM;
        String t[]=time.split(" ");
        String time_type=t[1].toString();
        String t1[]=t[0].split(":");
        int tm=Integer.parseInt(t1[0].toString());

        if(time_type.equals("AM"))
        {

        }
        else
        {
            if(tm==12)
            {

            }
            else
            {
                tm=12+tm;
            }
        }
        return tm;

    }


    public String getCloseStatus(String gm_time,String current_time)
    {
        int h=0;
        int m=0;
        try {
            int days=0,hours=0,min=0;

            Date date1=new Date();
            Date date2=new Date();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
            boolean st_time=getStatusTime(current_time);
            if(st_time)
            {
                date1 = simpleDateFormat.parse(formatTime(gm_time));
                date2 = simpleDateFormat.parse(current_time);

            }
            else
            {
                date1 = simpleDateFormat.parse(gm_time);
                date2 = simpleDateFormat.parse(current_time);

            }

            long difference = date2.getTime() - date1.getTime();
            days = (int) (difference / (1000*60*60*24));
            hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
            min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);

//        hours = (hours < 0 ? -hours : hours);
//        min = (min < 0 ? -min : min);
            h=hours;
            m=min;
            Log.i("======= Hours"," :: "+hours);
        }
        catch (Exception ex)
        {
            Toast.makeText(context,"err :--  "+ex.getMessage()+"\n "+gm_time+"\n "+current_time,Toast.LENGTH_LONG).show();
        }
        String d=""+h+":"+m;
        return String.valueOf(d);
    }

    public boolean getStatusTime(String current_tim)
    {
        boolean st=false;
        String t[]=current_tim.split(" ");
        String time_type=t[1].toString();
        if(time_type.equals("a.m.") || time_type.equals("p.m."))
        {
            st=true;
        }
        else if(time_type.equals("AM") || time_type.equals("PM"))
        {
            st=false;
        }
        return st;
    }

    public String formatTime(String time)
    {
        String tm="";
        String t[]=time.split(" ");
        String time_type=t[1].toString();

        if(time_type.equals("PM"))
        {
            tm="p.m.";
        }
        else if(time_type.equals("AM"))
        {
            tm="a.m.";
        }
        else
        {
            tm=time_type;
        }

        String c_tm=t[0].toString()+" "+tm;
        return c_tm;
    }


    public void updateWalletAmount(final JSONArray jsonArray, final LoadingBar progressDialog, final String matka_name, final String m_id, final String start_time, final String end_time )
    {
        final String data= String.valueOf(jsonArray);
        String json_request_tag="json_insert_request";
        HashMap<String, String> params=new HashMap<String, String>();
        params.put("data",data);

        Log.e("params_data",""+params.toString());
     //  Toast.makeText(context,""+data,Toast.LENGTH_LONG).show();
        if(progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
        progressDialog.show();


        CustomJsonRequest customJsonRequest=new CustomJsonRequest(Request.Method.POST, BaseUrl.URL_INSERT_DATA, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try
                {
                    Log.d("insert_data",response.toString());
                    JSONObject jsonObject=response;
                    // Toast.makeText(context,""+response.toString(),Toast.LENGTH_LONG).show();
                    final String status=jsonObject.getString("status");
                    progressDialog.dismiss();
                    if(status.equals("success"))
                    {


                        //updateWalletAmount(id,amt,context);
//                        Intent intent=new Intent(context, SeelctGameActivity.class);
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("m_id",m_id);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
//                        CustomIntent.customType(context, "up-to-bottom");
                        Toast.makeText(context,"Bid Added Successfully.", Toast.LENGTH_LONG).show();
                    }
                    else if(status.equals("failed"))
                    {
                        String sd=status.toString();
                        errorMessageDialog(sd.toString());
                        // Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
                    }
                    else if(status.equals("timeout"))
                    {

//                        final Dialog myDialog=new Dialog(context);
//                        myDialog.setContentView(R.layout.dialog_error_message_dialog);
//                        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//                        TextView txtmessage=(TextView)myDialog.findViewById(R.id.txtmessage);
//                        Button btnOK=(Button) myDialog.findViewById(R.id.btnOK);

//                        txtmessage.setText("Biding closed for this date");
//                        btnOK.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//
//                                myDialog.dismiss();
//
//                                String sd=status.toString();
//                                //errorMessageDialog(context,sd.toString());
//                                Intent intent=new Intent(context, HomeActivity.class);
//                                context.startActivity(intent);
//                            }
//                        });

//                        myDialog.show();
//                        String sd=status.toString();
//                        errorMessageDialog(context,sd.toString());
//                        Intent intent=new Intent(context,HomeActivity.class);
//                        context.startActivity(intent);


                    }




                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(context,"Err"+ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("sxdcfvghj",error.toString());
                progressDialog.dismiss();
                String msg=VolleyErrorMessage(error);
                errorMessageDialog(msg);


            }
        });
        customJsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        AppController.getInstance().addToRequestQueue(customJsonRequest,json_request_tag);




    }
    public void whatsapp(String phone, String message) {
        PackageManager packageManager = context.getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);

        try {

            context.startActivity(
                    new Intent(Intent.ACTION_VIEW,
                            Uri.parse(
                                    String.format("https://api.whatsapp.com/send?phone=%s&text=%s", phone, message)
                            )
                    )
            );




////            String url = "whatsapp://send?phone=91"+ phone +"&text=" + URLEncoder.encode(message, "UTF-8");
//            String url = "whatsapp://send?phone="+ phone +"&text=" + URLEncoder.encode(message, "UTF-8");
//            Log.e("sdfzxsdfg",phone);
////            Log.e("ASWDER","whatsapp://send?phone=");
//
//
//            i.setData(Uri.parse(url));
//            if (i.resolveActivity(packageManager) != null) {
//                context.startActivity(i);
//            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getRandomKey(int i)
    {
        final String characters="0123456789";
        StringBuilder stringBuilder=new StringBuilder();
        while (i>0)
        {
            Random ran=new Random();
            stringBuilder.append(characters.charAt(ran.nextInt(characters.length())));
            i--;
        }
        return stringBuilder.toString();
    }

//    public void setBackTint(TextView mView)
//    {
//        final int sdk = android.os.Build.VERSION.SDK_INT;
//        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//            mView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.gray_circle));
//        } else {
//            mView.setBackground(context.getResources().getDrawable(R.drawable.gray_circle));
//        }
//        mView.setEnabled(false);
//    }
//    public void setNormalTint(TextView mView)
//    {
//        final int sdk = android.os.Build.VERSION.SDK_INT;
//        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//            mView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.circle));
//        } else {
//            mView.setBackground(context.getResources().getDrawable(R.drawable.circle));
//        }
//        mView.setEnabled(true);
//    }


    public String changeTimeFormat(String time)
    {
        //14:15:16
        //02:15:00 PM,p.m.
        int hour=0;
        String timeFormat="";
        String[] arrTime24=time.split(":");
        int h24= Integer.parseInt(arrTime24[0].toString());
        if(h24==12)
        {
            hour=h24;
            timeFormat="PM";
        }
        else if(h24<12)
        {
            hour=h24;
            timeFormat="AM";
        }
        else
        {
            hour=h24-12;
            timeFormat="PM";
        }
        String strTime=hourForamt(hour)+":"+arrTime24[1].toString()+" "+timeFormat;
        return strTime;
    }
    public String hourForamt(int hour)
    {
        String h= String.valueOf(hour);
        if(h.length()<2)
        {
            h="0"+h;
        }
        return h;
    }

    public String getNumberFromMessage(String message) {
        // This will match any 6 digit number in the message
        String strNumber="";
        Pattern pattern = Pattern.compile("(|^)\\d{10}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
           strNumber=matcher.group(0);
        }
        return strNumber;
    }

    public SpannableString underlineString(String str)
    {
        String mystring=new String(str);
        SpannableString content = new SpannableString(mystring);
        content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
        return content;
    }
//    public void shakeAnimations(RelativeLayout rl){
//        Animation shake= AnimationUtils.loadAnimation(context,R.anim.swinging);
//        rl.startAnimation(shake);
//
//    }

    public void updatePoints(ArrayList<TableModel> list, int pos, String points, String betType)
    {

        TableModel tableModel=list.get(pos);
        tableModel.setPoints(points);
        tableModel.setType(betType);


    }

    public String checkNullString(String str){
        if(str == null || str.isEmpty() || str.equalsIgnoreCase("null")){
            return "";
        }else{
            return str;
        }
    }

    public boolean checkNull(String str){
        if(str == null || str.isEmpty() || str.equalsIgnoreCase("null")){
            return true;
        }else{
            return false;
        }
    }

    public String checkNullNumber(String str){
        if(str == null || str.isEmpty() || str.equalsIgnoreCase("null")){
            return "***";
        }else{
            return str;
        }
    }

    public void getSingleMatka(String mid, final OnGetMatka onGetMatka){
        HashMap<String,String> params=new HashMap<>();
        params.put("m_id",mid);
        postRequest(URl_GET_SINGLE_MATKA, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String resp) {
              try{
                  JSONObject response=new JSONObject(resp);
                  ArrayList<MatkasObjects> mList=new ArrayList<>();
                  if(response.getBoolean("responce")){

                      JSONArray jsonArray = response.getJSONArray("data");
                      for (int i = 0 ; i <jsonArray.length();i++) {
                          JSONObject jsonObject = jsonArray.getJSONObject(i);
                          MatkasObjects matkasObjects = new MatkasObjects();
                          matkasObjects.setId(jsonObject.getString("id"));
                          matkasObjects.setName(jsonObject.getString("name"));
                          matkasObjects.setStart_time(jsonObject.getString("start_time"));
                          matkasObjects.setEnd_time(jsonObject.getString("end_time"));
                          matkasObjects.setStarting_num(jsonObject.getString("starting_num"));
                          matkasObjects.setNumber(jsonObject.getString("number"));
                          matkasObjects.setEnd_num(jsonObject.getString("end_num"));
                          matkasObjects.setBid_start_time(jsonObject.getString("bid_start_time"));
                          matkasObjects.setBid_end_time(jsonObject.getString("bid_end_time"));
                          matkasObjects.setCreated_at(jsonObject.getString("created_at"));
                          matkasObjects.setUpdated_at(jsonObject.getString("updated_at"));
                          matkasObjects.setSat_start_time(jsonObject.getString("sat_start_time"));
                          matkasObjects.setSat_end_time(jsonObject.getString("sat_end_time"));
                          matkasObjects.setStatus(jsonObject.getString("status"));
                          mList.add(matkasObjects);
                      }
                      onGetMatka.onGetMatka(mList.get(0));

                  }else{
                      showToast(""+response.getString("error"));
                  }
              }catch (Exception ex){
                  ex.printStackTrace();
              }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showVolleyError(error);
            }
        });
    }
    public void getSingleStarline(String mid, final OnGetStarline onGetStarline){
        HashMap<String,String> params=new HashMap<>();
        params.put("m_id",mid);
        postRequest(URL_GET_SINGLE_STARLINE, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String resp) {
                try{
                    Log.e("starline",resp.toString());
                    JSONObject response=new JSONObject(resp);
                    ArrayList<MatkasObjects> mList=new ArrayList<>();
                    if(response.getBoolean("responce")){
                        JSONArray array = response.getJSONArray("data");
                        JSONObject jsonObject = array.getJSONObject(0);
                        StarlineModel model = new StarlineModel();
                        model.setId(jsonObject.getString("id"));
                        model.setS_game_time(jsonObject.getString("id"));
                        model.setS_game_end_time(jsonObject.getString("s_game_end_time"));
                        model.setS_game_number(jsonObject.getString("s_game_number"));
                        model.setUpdated_at(jsonObject.getString("updated_at"));
                        model.setCreated_at(jsonObject.getString("created_at"));
                        Log.e("zsxdfg",model.toString());

                        onGetStarline.OnGetStarline(model);
//                        onGetMatka.onGetMatka(model);


                    }else{
                        showToast(""+response.getString("error"));
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showVolleyError(error);
            }
        });
    }

    public boolean getValidTime(String sTime,String eTime){

        if(sTime.equalsIgnoreCase("00:00:00") && eTime.equalsIgnoreCase("00:00:00")){
            return false;
        }else if(sTime.equalsIgnoreCase("00:00:00.000000") && eTime.equalsIgnoreCase("00:00:00.000000")){
            return false;
        }else{
            return true;
        }
    }

    public void forgetPasswordWhatsApp()
    {
        HashMap<String,String> params = new HashMap<>();
        CustomVolleyJsonArrayRequest customVolleyJsonArrayRequest = new CustomVolleyJsonArrayRequest(Request.Method.GET, URL_INDEX, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("forget",response.toString());
                try
                {
                    JSONObject dataObj=response.getJSONObject(0);
                    forgot_whatsapp = dataObj.getString("forgot_whatsapp");
                    forgot_text = dataObj.getString("forgot_text");
                    msg_status = dataObj.getString("msg_status");
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              showVolleyError(error);
            }
        });
           AppController.getInstance().addToRequestQueue(customVolleyJsonArrayRequest,"json_tag");
    }

    public void cofigData(final OnGetConfigData onGetConfigData)

        {

        String json_tag="json_splash_request";
        HashMap<String, String> params=new HashMap<String, String>();
        CustomVolleyJsonArrayRequest customVolleyJsonArrayRequest=new CustomVolleyJsonArrayRequest(Request.Method.GET,URL_INDEX, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("cofigData",""+response.toString());
                try
                {
                    JSONObject dataObj=response.getJSONObject(0);

                    ConfigDataModel model = new ConfigDataModel();

                    model.setTag_line(dataObj.getString("tag_line"));
                    model.setForgot_whatsapp(dataObj.getString("forgot_whatsapp"));
                    model.setForgot_text(dataObj.getString("forgot_text"));
                    model.setMsg_status(dataObj.getString("msg_status"));
                    model.setWithdraw_text(dataObj.getString("withdraw_text").toLowerCase());
                    model.setWithdraw_no(dataObj.getString("withdraw_no"));
                    model.setHome_text(dataObj.getString("home_text").toString());
                    model.setMin_amount(dataObj.getString("min_amount"));
                    model.setApp_link(dataObj.getString("app_link"));
                    model.setShare_link(dataObj.getString("share_link"));
                    model.setVersion(dataObj.getString("version"));
                    model.setHome_whatsapp(dataObj.getString("home_whatsapp"));
                    model.setHome_call(dataObj.getString("home_call"));
                    model.setMin_bid_points(dataObj.getString("min_bid_points"));
                    model.setW_amount(dataObj.getString("w_amount"));
                    model.setForgot_msg(dataObj.getString("forgot_msg"));
                    model.setIs_starline(dataObj.getString("is_starline"));

                    onGetConfigData.onGetConfigData(model);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showVolleyError(error);
            }
        });

        AppController.getInstance().addToRequestQueue(customVolleyJsonArrayRequest,json_tag);


    }
    public boolean validNumber(String str){
        boolean flag=false;
        if(str.length()>3){
            if((str.length()%2)==0){
                flag=true;
            }else{
                flag=false;
            }
        }else{
            flag=true;
        }
        return flag;
    }
    public String getNumberType(String str){
        String type="";
        if(str.length()==1){
            type="Andar";
        }else if(str.length()==3){
            type="Bahar";
        }else{
            type="Jodi";
        }
        return type;
    }
    public String getBaharNumber(String number){
        int len=number.length();
        return String.valueOf(number.charAt(len-1));
    }
    public ArrayList<String> getJodiNumbers(String number){
        //123456
        ArrayList<String> tList=new ArrayList<>();
        int len=number.length();
        for(int i=0;i<len;i=i+2){
            int k=0;
            if(k<len){
                k=i+2;
            }
            tList.add(number.substring(i,k).toString());
        }
        return tList;
    }

    public long getTimeDiffernce(String time)
    {
        Date cdate=new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String time3=format.format(cdate);
        Date date1 = null;
        Date date3=null;
        try {
            date1 = format.parse(time);

            date3=format.parse(time3);
//                Log.e("pos : "+position, "onBindViewHolder: "+date2+"  \n "+date2.getTime() );
//                Log.e("poscurr : "+position, "onBindViewHolder: "+date3+"  \n "+date3.getTime() );
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        long difference = date3.getTime() - date1.getTime();
        long as=(difference/1000)/60;


        return as;
    }

}


