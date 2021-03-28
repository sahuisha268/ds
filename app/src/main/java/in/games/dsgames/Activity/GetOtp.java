package in.games.dsgames.Activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import in.games.dsgames.AppController;
import in.games.dsgames.Config.Module;
import in.games.dsgames.R;
import in.games.dsgames.utils.CustomJsonRequest;
import in.games.dsgames.utils.LoadingBar;

import static in.games.dsgames.Activity.SplashActivity.msg_status;
import static in.games.dsgames.Config.BaseUrl.URL_FORGOT_OTP;
import static in.games.dsgames.Config.BaseUrl.URL_REGISTER;
import static in.games.dsgames.Config.BaseUrl.URL_REGISTER_OTP;


public class GetOtp extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    Button btn_verify_otp;
    TextInputEditText user_phone;
    String type="",gen_otp="", mobile, code1, code2, code3, code4,otp="",user_id="";
    Module module;
    EditText inputCode1,inputCode2,inputCode3,inputCode4;
    private static final long COUNTDOWN_IN_MILLIS =30000;
    LoadingBar loadingBar;
    TextView timer ,tv_resned;
    HashMap<String, String> params;
    private ColorStateList textColorDefaultCd;
    private CountDownTimer countDownTimer;
    private long timeLeftMillis;
ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_otp);
        iv_back = findViewById(R.id.iv_back);
        btn_verify_otp=findViewById(R.id.btn_confirm_opt);
        timer=findViewById(R.id.tv_timer);
        tv_resned=findViewById(R.id.tv_resend);
        textColorDefaultCd=timer.getTextColors();
        timeLeftMillis= COUNTDOWN_IN_MILLIS;
        startCountDown();
        loadingBar = new LoadingBar(GetOtp.this);
        module = new Module(GetOtp.this);
        user_phone=findViewById(R.id.et_phone);
        inputCode1=findViewById(R.id.et_inputCode1);
        inputCode2=findViewById(R.id.et_inputCode2);
        inputCode3=findViewById(R.id.et_inputCode3);
        inputCode4=findViewById(R.id.et_inputCode4);
        inputCode1.addTextChangedListener(this);
        inputCode2.addTextChangedListener(this);
        inputCode3.addTextChangedListener(this);
        inputCode4.addTextChangedListener(this);
        params = (HashMap<String, String>)getIntent().getSerializableExtra("map");
        if (getIntent().getExtras().containsKey("user_id"))
        {
            user_id = getIntent().getStringExtra("user_id");
        }
        type = getIntent().getStringExtra("type");
        gen_otp = getIntent().getStringExtra("otp");
        mobile = getIntent().getStringExtra("mobile");
        btn_verify_otp.setOnClickListener(this);
        tv_resned.setOnClickListener(this);
       iv_back.setOnClickListener(this);
        Log.e("otp",gen_otp +"\n mobile"+mobile);
        if(msg_status.equals("0"))
        {

            countDownTimer=new CountDownTimer(5000,1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {

                    inputCode1.setText(""+gen_otp.charAt(0));
                    inputCode2.setText(""+gen_otp.charAt(1));
                    inputCode3.setText(""+gen_otp.charAt(2));
                    inputCode4.setText(""+gen_otp.charAt(3));

//                    et_otp.setText(params.get("otp"));
                }
            };
            countDownTimer.start();
        }

    }

    private void startCountDown() {
        countDownTimer=new CountDownTimer(timeLeftMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
              timeLeftMillis=millisUntilFinished;
              updateCountDownText();
            }

            @Override
            public void onFinish() {
            timeLeftMillis = 0;
//            updateCountDownText();

                    timer.setText("TimeOut");
//

            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes =(int) (timeLeftMillis / 1000) /60;
        int seconds =(int) (timeLeftMillis / 1000) % 60;
        String timeFormatted =String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        timer.setText(timeFormatted);
        if (timeLeftMillis < 10000) {
            timer.setTextColor(Color.RED);
        }
            else
            {

            timer.setTextColor(textColorDefaultCd);
          }
    }





    void makeRegisterRequest(HashMap<String,String>params)
    {
        loadingBar.show();
//        HashMap<String,String>params = new HashMap<>();
//        params.put("key","1");
//        params.put("username",name);
//        params.put("name",name);
//        params.put("mobile",mobile);
//        params.put("password",pass);
//        params.put("mpin",mpin);

        CustomJsonRequest jsonRequest = new CustomJsonRequest(Request.Method.POST, URL_REGISTER, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingBar.dismiss();
                Log.e("register",response.toString());
                try {
                    boolean stat = response.getBoolean("responce");
                    if (stat)
                    {
                        module.showToast(response.getString("message"));
                      startActivity(new Intent(GetOtp.this,LoginActivity.class));
                    }
                    else
                    {
                        module.showToast(response.getString("error"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                module.showToast(module.VolleyErrorMessage(error));

            }
        });
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.iv_back:
                startActivity(new Intent(GetOtp.this,LoginActivity.class));

            case R.id.tv_resend:
                if (type.equals("r"))
                {
                    resendOtp(mobile,module.getRandomKey(4),URL_REGISTER_OTP);
                }
                else
                {
                    resendOtp(mobile,module.getRandomKey(4),URL_FORGOT_OTP);
                }

            break;
            case R.id.btn_confirm_opt:

                code1=inputCode1.getText().toString().trim();
                code2=inputCode2.getText().toString().trim();
                code3=inputCode3.getText().toString().trim();
                code4=inputCode4.getText().toString().trim();
                otp = code1+code2+code3+code4;

                if (otp.length()!=4)
                {
                    module.showToast("Enter Valid Otp");
                }
                else
                {
                    if (otp.equals(gen_otp)) {
                        if (type.equals("r")) {
//                            makeRegisterRequest(params);
                            verifyOtp(user_id,mobile,otp);
                        }
                        else
                        {
                            Intent intent = new Intent(GetOtp.this,ForgetPasswordActivity.class);
                            intent.putExtra("mobile",mobile);
                            intent.putExtra("otp",otp);
                            startActivity(intent);
                        }
                    }
                    else
                    {
                        module.showToast("Wrong Otp Entered");
                    }
                }


        
                break;
        }
        
    }

    private void resendOtp(final String mobile, final String otp ,String url) 
    {
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("mobile",mobile);
        params.put("otp",otp);

        CustomJsonRequest customJsonRequest=new CustomJsonRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("gen",""+response.toString());
                loadingBar.dismiss();
                try
                {
                    String res=response.getString("status");
                    if(res.equalsIgnoreCase("success"))
                    {
                        module.showToast(response.getString("message"));
                        gen_otp = otp;
                        startCountDown();
                    }
                    else
                    {
                        module.showToast(response.getString("message"));
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    module.showToast("Something went wrong");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String msg=module.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    module.showToast(""+msg);
                }
            }
        });
        AppController.getInstance().addToRequestQueue(customJsonRequest);

    }
    private void verifyOtp(String user_id, final String mobile, final String otp)
    {
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",user_id);
//        params.put("mobile",mobile);
        params.put("otp",otp);

        CustomJsonRequest customJsonRequest=new CustomJsonRequest(Request.Method.POST, URL_REGISTER_OTP, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("gen_otp",""+response.toString());
                loadingBar.dismiss();
                try
                {
                    String res=response.getString("status");
                    if(res.equalsIgnoreCase("success"))
                    {
//                        module.showToast(response.getString("message"));
                        module.showToast(
                                "Otp sent to your mobile number "
                        );
                        Intent intent=new Intent(GetOtp.this,LoginActivity.class);
//                        intent.putExtra("mobile",mobile);
//                        intent.putExtra("otp",otp);
//                        intent.putExtra("type","r");
//                        intent.putExtra("map",map);
                        startActivity(intent);
                    }
                    else
                    {
                        module.showToast(response.getString("message"));
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    module.showToast("Something went wrong");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                Log.e("error",error.toString());
                String msg=module.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    module.showToast(""+msg);
                }
            }
        });
        AppController.getInstance().addToRequestQueue(customJsonRequest);

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


    }

    @Override
    public void afterTextChanged(Editable editable) {
        String c = editable.toString();
        if (c.length()==1)
        {
            if (inputCode1.hasFocus())
            {
                inputCode2.requestFocus();
            }
            else if (inputCode2.hasFocus())
            {
                inputCode3.requestFocus();
            }
            else if (inputCode3.hasFocus())
            {
                inputCode4.requestFocus();
            }
        }
        else if (c.length()==0)
        {
            if (inputCode4.hasFocus())
            {
                inputCode3.requestFocus(inputCode3.getText().length());
            }
            else if (inputCode3.hasFocus())
            {
                inputCode2.requestFocus(inputCode2.getText().length());
            }
            else if (inputCode2.hasFocus())
            {
                inputCode1.requestFocus(inputCode1.getText().length());
            }
        }

    }
}