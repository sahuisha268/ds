package in.games.dsgames.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import in.games.dsgames.AppController;
import in.games.dsgames.Config.Common;
import in.games.dsgames.Config.Module;
import in.games.dsgames.Model.TimeSlots;
import in.games.dsgames.R;
import in.games.dsgames.utils.CustomJsonRequest;
import in.games.dsgames.utils.CustomVolleyJsonArrayRequest;
import in.games.dsgames.utils.LoadingBar;
import in.games.dsgames.utils.Session_management;

import static in.games.dsgames.Config.BaseUrl.URL_INDEX;
import static in.games.dsgames.Config.BaseUrl.URL_TIME_SLOT;
import static in.games.dsgames.Config.BaseUrl.URL_WITHDRAW;
import static in.games.dsgames.Config.Constants.KEY_ACCOUNNO;
import static in.games.dsgames.Config.Constants.KEY_HOLDER;
import static in.games.dsgames.Config.Constants.KEY_ID;
import static in.games.dsgames.Config.Constants.KEY_IFSC;


public class WithDrawActivity extends AppCompatActivity {
    Common common;
    Session_management sessionMangement;
    private TextView txtback,txtWalletAmount,txtMobile,txt_withdrw_instrctions,tv_number,tv_instrction,tv_min_withdrw;
    private LoadingBar progressDialog;
    private EditText etPoint;
    private Button btnSave;
  //  ArrayList<TimeSlots> timeList;
    int amount_limt=0;
    int req_limit=1;
    String text="",no="";
    int wSaturday=0,wSunday=0;
    RelativeLayout rl_whts;
    String request_status="pending";
    String type="";
    String bank_type="";
    Module module;
    ArrayList<TimeSlots> timeList;
    TextInputEditText etPoints;
    TextInputLayout tvPoints;
    RadioButton r_bank;
    LinearLayout account_detail;
    ImageView iv_back;
    TextView tv_title;
    TextInputEditText et_account_no,et_holder_name,et_ifsc_code;
    Button btn_cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_draw);
        sessionMangement = new Session_management(WithDrawActivity.this);
        common=new Common(WithDrawActivity.this);
        btn_cancel=findViewById(R.id.btn_cancel);
      //  txtback=(TextView)findViewById(R.id.txtBack);
        tv_number=(TextView)findViewById(R.id.tv_number);
        txtWalletAmount=(TextView)findViewById(R.id.tv_wallet);
        etPoint=(EditText)findViewById(R.id.etRequstPoints);
        btnSave=(Button)findViewById(R.id.btn_withdraw);
        tv_instrction = findViewById(R.id.tv_instruction);
        etPoints=findViewById(R.id.et_amount);
        tvPoints =findViewById(R.id.tv_amount);
        r_bank=findViewById(R.id.rb_bank_account);
        account_detail=findViewById(R.id.ll_account_detail);
        tv_min_withdrw = findViewById(R.id.tv_min_withdraw);
        et_account_no=findViewById(R.id.et_account_no);
        et_holder_name=findViewById(R.id.et_holder_name);
        et_ifsc_code=findViewById(R.id.et_ifsc_code);
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_Title);
        tv_title.setText("Withdraw Funds");
        module = new Module(WithDrawActivity.this);
        progressDialog=new LoadingBar(WithDrawActivity.this);
        tv_instrction.setText(Html.fromHtml(SplashActivity.withdrw_text));
        final String user_id = sessionMangement.getUserDetails().get(KEY_ID);
        timeList=new ArrayList<>();
        getApiData();
        getTimeSlot();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        r_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account_detail.setVisibility(View.VISIBLE);
                et_account_no.setText(sessionMangement.getUserDetails().get(KEY_ACCOUNNO));
                et_holder_name.setText(sessionMangement.getUserDetails().get(KEY_HOLDER));
                et_ifsc_code.setText(sessionMangement.getUserDetails().get(KEY_IFSC));

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String points=etPoints.getText().toString().trim();

                if(etPoints.getText().toString().isEmpty()) {
                    module.validateEditText(etPoints, tvPoints, "Enter Some Points");
                }
                else
                {

                    String user_id = sessionMangement.getUserDetails().get(KEY_ID);
                    String pnts = String.valueOf(points);
                    String st = "pending";
                    int w_amt = Integer.parseInt(txtWalletAmount.getText().toString().trim());
                    int t_amt = Integer.parseInt(pnts);



                    if (w_amt > 0) {

                        if(t_amt<amount_limt)
                        {
                            common.errorMessageDialog("Minimum Withdraw amount "+amount_limt+".");
                        }
                        else
                        {

                            if (t_amt > w_amt) {

                                common.errorMessageDialog("Your requested amount exceeded");
                                return;
                            } else {

                                int flg=0;
                                if(getCurrentDay().equalsIgnoreCase("Sunday"))
                                {
                                    if(wSunday==1){
                                        flg=1;
                                    }
                                    else{
                                        flg=2;
                                    }
                                }
                                else if(getCurrentDay().equalsIgnoreCase("Saturday"))
                                {
                                    if(wSaturday==1){
                                        flg=3;
                                    }
                                    else{
                                        flg=4;
                                    }
                                }
                                else
                                {
                                    flg=5;
                                }
                                if(flg==1 || flg==3 || flg==5){
                                    if(getStartTimeOutStatus(timeList) || getEndTimOutStatus(timeList))
                                    {
                                       // saveInfoWithDate(user_id,String.valueOf(t_amt),st);
                                        getwithdrawAmount(user_id,st,type,bank_type, String.valueOf(t_amt));
                                    }
                                    else
                                    {
                                        common.showToast("Timeout");
                                    }

                                }
                                else if(flg==2 || flg==4)
                                {
                                    common.showToast("Withdraw Request is not allowed on "+getCurrentDay());
                                }
                                // saveInfoIntoDatabase(user_id, String.valueOf(t_amt), st);

                            }
//
                        }

                    } else {
                        common.errorMessageDialog("You don't have enough points in wallet ");
                    }

                }


//                        saveInfoIntoDatabase(user_id,pnts,st);
            }


        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        common.setWallet_Amount(txtWalletAmount,progressDialog, sessionMangement.getUserDetails().get(KEY_ID));

    }

    public boolean getStartTimeOutStatus(ArrayList<TimeSlots> list){
        int j=0;
        boolean flag=false;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH");
        SimpleDateFormat spdfMin=new SimpleDateFormat("mm");
        Date c_date=new Date();
        int chours=Integer.parseInt(simpleDateFormat.format(c_date));
        int cMins=Integer.parseInt(spdfMin.format(c_date));
        for(int i=0; i<list.size();i++){
            int shours=Integer.parseInt(list.get(i).getStart_time().split(":")[0].toString());
            int sMins=Integer.parseInt(list.get(i).getStart_time().split(":")[1].toString());
            if(chours>shours)
            {j=1;
                flag=true;
                break;
            }
            else if(chours == shours)
            {
                if(cMins<=sMins)
                {
                    j=2;
                    flag=true;
                    break;
                }
                else{
                    j=3;
                    flag=false;
                    break;
                }
            }
        }

        return flag;

    }
    public boolean getEndTimOutStatus(ArrayList<TimeSlots> list){
        int j=0;
        boolean flag=false;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH");
        SimpleDateFormat spdfMin=new SimpleDateFormat("mm");
        Date c_date=new Date();
        int chours=Integer.parseInt(simpleDateFormat.format(c_date));
        int cMins=Integer.parseInt(spdfMin.format(c_date));
        for(int i=0; i<list.size();i++){
            int ehours=Integer.parseInt(list.get(i).getEnd_time().split(":")[0].toString());
            int eMins=Integer.parseInt(list.get(i).getEnd_time().split(":")[1].toString());
            if(chours<ehours)
            {j=1;
                flag=true;
                break;
            }
            else if(chours == ehours)
            {
                if(cMins<=eMins)
                {
                    j=4;
                    flag=true;
                    break;
                }
                else{
                    j=5;
                    flag=false;
                    break;
                }
            }
        }
//         common.showToast("end_timeout-  "+j);

        return flag;

    }
    public String getCurrentDay()
    {
        Date date=new Date();
        SimpleDateFormat smdf=new SimpleDateFormat("EEEE");
        String day=smdf.format(date);
        return day;
    }
    public void getTimeSlot()
    {
        progressDialog.show();
        HashMap<String,String> params=new HashMap<>();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, URL_TIME_SLOT, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("timeSlot",response.toString());
                try {
                    boolean resp = response.getBoolean("response");
                    if(resp){
                        JSONArray arr=response.getJSONArray("timeslots");
                        for(int i=0;i<arr.length();i++){

                            JSONObject obj=arr.getJSONObject(0);
                            TimeSlots model=new TimeSlots();
                            model.setId(obj.getString("id"));
                            model.setStart_time(obj.getString("start_time"));
                            model.setEnd_time(obj.getString("end_time"));

                            timeList.add(model);
                            Log.e("lmswndebh", String.valueOf(timeList.size()));
                        }

                    }

                }catch (Exception ex)
                {
                  ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(customJsonRequest);

    }

    public void getwithdrawAmount(String user_id,String st, String type, String bank_type,String points)
    {
        progressDialog.show();
        String json_request_tag="json_withdraw_request";
        HashMap<String,String> params=new HashMap<String, String>();
        params.put("user_id",user_id);
        params.put("request_status",st);
        params.put("type","withdraw");
        params.put("bank_type","bank");
        params.put("points",points);
        Log.e("asdasd",""+params.toString());
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, URL_WITHDRAW, params, new Response.Listener<JSONObject>() {
    @Override
    public void onResponse(JSONObject response) {
        Log.d("withdrw_req", response.toString());

        try {
            boolean resp = response.getBoolean("responce");
            if (resp) {
              //  Toast.makeText(WithDrawActivity.this,"Request Added",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Toast.makeText(WithDrawActivity.this,"Request Added", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(WithDrawActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

                return;

            }
        }catch (Exception ex)
        {
           ex.printStackTrace();
        }

            }
}, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {

    }
});
        AppController.getInstance().addToRequestQueue(customJsonRequest,json_request_tag);
    }
    private void getApiData() {

        progressDialog.show();
        String json_tag="json_splash_request";
        HashMap<String, String> params=new HashMap<String, String>();
        CustomVolleyJsonArrayRequest customVolleyJsonArrayRequest=new CustomVolleyJsonArrayRequest(Request.Method.GET,URL_INDEX, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("getIndexData",""+response.toString());
                try
                {

                    String msg="";
                    JSONObject dataObj=response.getJSONObject(0);
                     amount_limt= Integer.parseInt(dataObj.getString("min_amount"));
                    //tv_min_withdrw.setText(amount_limt+"INR" );
                    tv_min_withdrw.setText("(min "+amount_limt+")" );
                    wSunday= Integer.parseInt(dataObj.getString("w_sunday"));
                    wSaturday= Integer.parseInt(dataObj.getString("w_sunday"));
                 //   withdraw_limit= dataObj.getString("withdraw_limit");
              //     amount_limt = Integer.parseInt(dataObj.getString("min_amount"));
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                String msg=module.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    module.showToast(""+msg);
                }
            }
        });

        AppController.getInstance().addToRequestQueue(customVolleyJsonArrayRequest,json_tag);


    }

}
