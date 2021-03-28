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
import static in.games.dsgames.Config.Constants.KEY_WALLET;


public class WithDrawActivity extends AppCompatActivity {
    private final String TAG=WithDrawActivity.class.getSimpleName();
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
    String wSaturday="",wSunday="";
    RelativeLayout rl_whts;
    String request_status="pending";
    int type=-1;
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
                type=getValueType("Bank Account");

            }
        });
        //getwithdrawAmount(user_id,st,type,bank_type, String.valueOf(t_amt));
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getValidWtihdrawDay(wSunday,wSaturday)){
                    if(getValidTime()){
                        validate();
                    }else{
                        common.errorMessageDialog("Withdraw Timeout");
                    }
                }else{
                    common.errorMessageDialog("Withdraw Request Closed for today");
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
    private void validate() {
        if(etPoints.getText().toString().isEmpty()){
            common.errorMessageDialog("Enter Points");
        }else{
            Log.e(TAG, "validate: "+sessionMangement.getUserDetails().get(KEY_WALLET));
            int points=Integer.parseInt(common.checkNullNumber(etPoints.getText().toString().trim()));
            if(points<amount_limt){
                common.errorMessageDialog("Minimum Withdraw Amount is "+amount_limt);
            }else{
                if(points>Integer.parseInt(sessionMangement.getUserDetails().get(KEY_WALLET))){
                    common.errorMessageDialog("Your requested amount exceeded");
                }else{
                    if(type==-1){
                        common.errorMessageDialog("Select Any One Withdraw Type");
                    }else{
                        validateWithdrawType(type);
                    }
                }
            }
        }
    }

    private void validateWithdrawType(int type) {

        String wType="";
        String details="";
        if(type==0){
//            String paytm=et_paytm.getText().toString().trim();
            String paytm="";
            if(paytm.isEmpty()){
                common.errorMessageDialog("Enter Paytm Number");
            }else if(paytm.length()<10){
                common.errorMessageDialog("Enter Mobile Number");
            }else{
                wType="Paytm";
                details=paytm;
            }
        }else if(type==1){
//            String google=et_google.getText().toString().trim();
            String google="";
            if(google.isEmpty()){
                common.errorMessageDialog("Enter Google Pay Number");
            }else if(google.length()<10){
                common.errorMessageDialog("Enter Mobile Number");
            }else{
                wType="Google Pay";
                details=google;
            }
        }else if(type==2) {
//            String phone = et_phone.getText().toString().trim();
            String phone = "";
            if (phone.isEmpty()) {
                common.errorMessageDialog("Enter Phone Pe Number");
            } else if (phone.length() < 10) {
                common.errorMessageDialog("Enter Mobile Number");
            } else {
                wType = "PhonePe";
                details = phone;
            }
        }else if(type==3) {
            String holder = et_holder_name.getText().toString().trim();
            String acc_no = et_account_no.getText().toString().trim();
            String ifsc_code = et_ifsc_code.getText().toString().trim();
            if (holder.isEmpty()) {
                common.errorMessageDialog("Enter Account Holder Name");
            } else if (acc_no.isEmpty())
            {
                common.errorMessageDialog("Enter Account Number");
            }
            else if (!acc_no.matches("^\\d{9,18}$"))
            {
                common.errorMessageDialog("Invalid Account Number");
            }
            else if (ifsc_code.isEmpty())
            {
                common.errorMessageDialog("Enter Ifsc code");
            }
           else {
                wType ="Bank";
                details = "Account Holder Name - "+holder +"\n" +"Account Number - "+acc_no +"\n" +"Ifsc Code - "+ ifsc_code;
            }
        }
        Log.e(TAG, "validateWithdrawType: "+wType+" \n "+details);
        if((!common.checkNull(wType)) && (!common.checkNull(details))){
            String points=etPoints.getText().toString().trim();
            String user_id = sessionMangement.getUserDetails().get(KEY_ID);
            String pnts = String.valueOf(points);
            String st = "pending";
            int w_amt = Integer.parseInt(txtWalletAmount.getText().toString().trim());
            int t_amt = Integer.parseInt(pnts);

            getwithdrawAmount(user_id,st,String.valueOf(t_amt));
//            addWithdrawRequest(et_point.getText().toString(),wType,details);
        }


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

                            JSONObject obj=arr.getJSONObject(i);
                            TimeSlots model=new TimeSlots();
                            model.setId(obj.getString("id"));
                            model.setStart_time(obj.getString("start_time"));
                            model.setEnd_time(obj.getString("end_time"));

                            timeList.add(model);

                        }
                        Log.e("lmswndebh", String.valueOf(timeList.size()));

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

    public void getwithdrawAmount(String user_id,String st,String points)
    {
        progressDialog.show();
        String json_request_tag="json_withdraw_request";
        HashMap<String,String> params=new HashMap<String, String>();
        params.put("user_id",user_id);
        params.put("request_status",st);
        params.put("type","Withdrawal");
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
                     amount_limt= Integer.parseInt(dataObj.getString("w_amount"));
                    tv_min_withdrw.setText("(min "+amount_limt+")" );
                    wSunday= dataObj.getString("w_sunday");
                    wSaturday= dataObj.getString("w_sunday");
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

    public boolean getValidWtihdrawDay(String wsun,String wsat){
        boolean flag=false;
        String day=new SimpleDateFormat("EEEE").format(new Date()).toString();
        if(day.equalsIgnoreCase("Sunday")){
            if(wsun.equals("1")){
                flag=true;
            }else{
                flag=false;
            }
        }else if(day.equalsIgnoreCase("Saturday")){
            if(wsat.equals("1")){
                flag=true;
            }else{
                flag=false;
            }
        }else{
            flag=true;
        }
        return flag;
    }

    public boolean getValidTime(){
        boolean flag=false;
        for(int i=0;i<timeList.size();i++){
            long startDiff=common.getTimeDiffernce(timeList.get(i).getStart_time());
            long endDiff=common.getTimeDiffernce(timeList.get(i).getEnd_time());
            Log.e(TAG, "getValidTime: "+startDiff+"::"+endDiff);
            if(startDiff>0 && endDiff<0){
                flag=true;
                break;
            }
        }
        return flag;
    }
    public int getValueType(String getValue){
        int pos=-1;
        if(getValue.equalsIgnoreCase("Paytm")){
            pos=0;
        }else  if(getValue.equalsIgnoreCase("Google Pay")){
            pos=1;
        }else  if(getValue.equalsIgnoreCase("Phone Pe")){
            pos=2;
        }else  if(getValue.equalsIgnoreCase("Bank Account")){
            pos=3;
        }
        return pos;

    }


}
