package in.games.dsgames.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.razorpay.Checkout;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.games.dsgames.AppController;
import in.games.dsgames.Config.BaseUrl;
import in.games.dsgames.Config.Common;
import in.games.dsgames.Config.Module;
import in.games.dsgames.R;
import in.games.dsgames.utils.CustomJsonRequest;
import in.games.dsgames.utils.CustomVolleyJsonArrayRequest;
import in.games.dsgames.utils.LoadingBar;
import in.games.dsgames.utils.Session_management;

import static in.games.dsgames.Config.BaseUrl.URL_INDEX;
import static in.games.dsgames.Config.BaseUrl.URL_INSERT_REQUEST;
import static in.games.dsgames.Config.Constants.KEY_ACCOUNNO;
import static in.games.dsgames.Config.Constants.KEY_BANK_NAME;
import static in.games.dsgames.Config.Constants.KEY_EMAIL;
import static in.games.dsgames.Config.Constants.KEY_HOLDER;
import static in.games.dsgames.Config.Constants.KEY_ID;
import static in.games.dsgames.Config.Constants.KEY_IFSC;
import static in.games.dsgames.Config.Constants.KEY_MOBILE;
import static in.games.dsgames.Config.Constants.KEY_NAME;
import static in.games.dsgames.Config.Constants.KEY_PAYTM;
import static in.games.dsgames.Config.Constants.KEY_PHONEPAY;
import static in.games.dsgames.Config.Constants.KEY_TEZ;

public class RequestPointActivity extends AppCompatActivity implements PaymentStatusListener {
    ProgressDialog progressDialog;
    private TextView bt_back,txtMatka;
    Button btnRequest;
    LoadingBar loadingBar;
    private TextView txtWallet_amount;
    int min_amount,amt_limit=0 ;
    Session_management session_management;
    String upi_status="";
    String tot_points="0";
    String upi="",upi_name="",upi_desc="",upi_type="",transactionId="";
    boolean upi_flag=false;
    private EasyUpiPayment mEasyUpiPayment;
    Common common;
    ImageView iv_back;
    TextView tv_title;
    TextInputEditText etPoints;
    TextInputLayout tvPoints;
    Module module;
    TextView min_am;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_point);
        etPoints=findViewById(R.id.etRequstPoints);
        min_am= findViewById(R.id.min_amount);
        btnRequest=(Button)findViewById(R.id.add_Request);
        tvPoints=findViewById(R.id.tvRequstPoints);
        tv_title = findViewById(R.id.tv_Title);
        tv_title.setText("Funds");
        txtWallet_amount=(TextView)findViewById(R.id.tv_wallet);
        loadingBar= new LoadingBar(RequestPointActivity.this);
        progressDialog=new ProgressDialog(RequestPointActivity.this);
        //progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Please wait..");
        module= new Module(RequestPointActivity.this);
        session_management = new Session_management(RequestPointActivity.this);
        common = new Common(RequestPointActivity.this);
        Checkout.preload(getApplicationContext());

        getApiData();

//        getDetails();
     //   min_amount = amt_limit;
        Log.e("dllfmkvngjb", String.valueOf(min_amount));
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              //  int points= Integer.parseInt(etPoints.getText().toString().trim());
                String points=etPoints.getText().toString().trim();

                if(etPoints.getText().toString().isEmpty()) {
                    module.validateEditText(etPoints, tvPoints, "Enter Some Points");
                }

//                    if(TextUtils.isEmpty(etPoints.getText().toString()))
//                {
//                    tvPoints.setError("Enter Some Points");
//                    //return;
//
//                }
                else
                {
                    int pp = Integer.parseInt(points);
                    if(pp<amt_limit)
                    {
                        common.errorMessageDialog("Minimum Range for points is "+ amt_limit);

                    }
                    else
                    {

//                        String user_id= Prevalent.currentOnlineuser.getId();
                        String user_id= session_management.getUserDetails().get(KEY_ID);

                        String p= String.valueOf(points);
                        String st="pending";
                        String method = etPoints.getText().toString().trim();
                        String wh = "0";
//                       saveInfoIntoDatabase(user_id,p,st);
                        transactionId = "TXN" + System.currentTimeMillis();
                        String payeeVpa = upi;
                        String payeeName = upi_name;
                        String transactionRefId = transactionId;
                        String description = upi_desc;
                        String amount = p.toString()+".00";
                        String details = "";
                        details = getMetodData(method);
//                        String user_id= common.getUserID();
                        if(upi_status.equals("0")){
//                            addRequest(user_id,p,"pending","");
                            tot_points= String.valueOf(points);

                             pp = Integer.parseInt(points);
                            startPayment(pp);


                            saveInfoIntoDatabase(user_id, p, st, "Withdrawal", method, wh, details,"");


                        }else{
//                            payViaUpi(transactionId,payeeVpa,payeeName,transactionRefId,description,amount);
                            saveInfoIntoDatabase(user_id, p, st, "Withdrawal", method, wh, details,"");
                            addRequest(user_id,p,"pending","");
                        }

                    }
                }

            }
        });


    }
    private void getApiData() {

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
                    amt_limit= Integer.parseInt(dataObj.getString("min_amount"));
                  //  min_am.setText("(min "+amt_limit+")" );
                    min_am.setText(amt_limit+" INR" );
                 //   wSunday= Integer.parseInt(dataObj.getString("w_sunday"));
            //        wSaturday= Integer.parseInt(dataObj.getString("w_sunday"));
                    //   withdraw_limit= dataObj.getString("withdraw_limit");
                    //     amount_limt = Integer.parseInt(dataObj.getString("min_amount"));


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

    private void saveInfoIntoDatabase(final String user_id, final String points, final String st, final String type, String method, String whts, String details, String trans_id) {
        progressDialog.show();
        HashMap<String, String> params=new HashMap<>();
        params.put("user_id",user_id);
        params.put("points",points);
        params.put("request_status",st);
        params.put("type",type);
        params.put("method",method);
        params.put("whatsapp",whts);
        params.put("details",details);
        params.put("trans_id",trans_id);
        Log.e("hgh",params.toString());

        common.postRequest(BaseUrl.URL_REQUEST, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {
                progressDialog.dismiss();
                Log.e("msnsndbd",res.toString());
                try {
                    JSONObject response=new JSONObject(res);
                    boolean resp=response.getBoolean("responce");
                    if(resp)
                    {
//                          common.showTimeoutDialog(""+response.getString("message"));
                        common.showTimeoutDialog(""+response.getString("message"));
                        if (type.equalsIgnoreCase("add"))
                        {
//                            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//                            add_dialog.dismiss();
                        }
                        else
                        {
//
//                            withdraw_dialog.dismiss();
                        }

                    }
                    else
                    {
                        common.showToast(""+response.getString("error"));
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                common.showVolleyError(error);

            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(upi_flag){
            mEasyUpiPayment.detachListener();
        }
    }

    public void addRequest(String user_id, String points, String status, String txn_id)
    {
        progressDialog.show();
        HashMap<String, String> params=new HashMap<>();
        params.put("user_id",user_id);
        params.put("points",points);
        params.put("request_status",status);
        params.put("type","Add");
        params.put("txn_id",txn_id);
        params.put("wallet",txtWallet_amount.getText().toString().trim());
        Log.e("money",params.toString());
        CustomJsonRequest customJsonRequest=new CustomJsonRequest(Request.Method.POST, URL_INSERT_REQUEST, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
              Log.e("money add",response.toString());
                    String status=response.getString("status");
                    if(status.equals("success"))
                    {
                        progressDialog.dismiss();
                        Toast.makeText(RequestPointActivity.this,"successfull", Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(RequestPointActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                        return;
                    }
                    else
                    {
                        progressDialog.dismiss();

                        Toast.makeText(RequestPointActivity.this,"Something Went Wrong", Toast.LENGTH_LONG).show();
                        return;
                    }


                }
                catch (Exception ex)
                {
                    progressDialog.dismiss();

                    Toast.makeText(RequestPointActivity.this,"Error :"+ex.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                String msg=common.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    common.showToast("error");
                }
            }
        });
        AppController.getInstance().addToRequestQueue(customJsonRequest);
    }

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {
        Log.e("transactionDetails",""+transactionDetails);
        if(transactionDetails.getStatus().equalsIgnoreCase("success"))
        {
            String user_id= session_management.getUserDetails().get(KEY_ID);
            addRequest(user_id,transactionDetails.getAmount().toString(),"approved",transactionDetails.getTransactionId().toString());

        }
        else
        {
            common.showToast("Payment Failed.");
        }

    }

    public void onPaymentSuccess(String s) {
        Log.e("onPaymentSuccess: ",s.toString() );

        saveInfoIntoDatabase(session_management.getUserDetails().get(KEY_ID), tot_points, "approved", "Add","","","",s);
    }

    @Override
    public void onTransactionSuccess() {

    }

    @Override
    public void onTransactionSubmitted() {

    }

    @Override
    public void onTransactionFailed() {
        common.showToast("Failed");
    }

    @Override
    public void onTransactionCancelled() {
        common.showToast("Cancelled");
    }

    @Override
    public void onAppNotFound() {
        common.showToast("App Not Found");
    }


    @Override
    protected void onStart() {
        super.onStart();
        // setSessionTimeOut(RequestActivity.this);
        common.setWallet_Amount(txtWallet_amount,loadingBar, session_management.getUserDetails().get(KEY_ID));

    }
    public void startPayment(int amt) {
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
//        checkout.setKeyID("rzp_live_s7AZi7HtHIO5Ff");

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.ic_account_balance_wallet_black_24dp);

        /**
         * Reference to current activity
         */
        Activity activity=this;
        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();
            options.put("name", session_management.getUserDetails().get(KEY_NAME));
            options.put("description", "Add funds");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", "order_DBJOWzybf0sJbdsds");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", (amt*100));//pass amount in currency subunits
//            options.put("amount", (500));//pass amount in currency subunits
            options.put("prefill.email", session_management.getUserDetails().get(KEY_EMAIL));
            options.put("prefill.contact",session_management.getUserDetails().get(KEY_MOBILE));
            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e("Error in Checkout", String.valueOf(e));
        }
    }
    public String getMetodData(String methd){
        String str="";
        switch (methd){
            case "Google Pay":
                String google_pay=session_management.getUserDetails().get(KEY_TEZ);
                str=google_pay;
                break;
            case "PhonePe":
                String phonepe=session_management.getUserDetails().get(KEY_PHONEPAY);
                str=phonepe;
                break;
            case "Paytm":
                String ptm=session_management.getUserDetails().get(KEY_PAYTM);
                str=ptm;
                break;
            case "Bank Account":
                String bnk=session_management.getUserDetails().get(KEY_ACCOUNNO);
                String bname=session_management.getUserDetails().get(KEY_BANK_NAME);
                String bif=session_management.getUserDetails().get(KEY_IFSC);
                String bholder=session_management.getUserDetails().get(KEY_HOLDER);
                str=bnk+"<br/>"+bname+"<br/>"+bif;
                break;

        }
        return str;
    }
}