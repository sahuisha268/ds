package in.games.dsgames.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;

import in.games.dsgames.AppController;
import in.games.dsgames.Config.Common;
import in.games.dsgames.Config.Module;
import in.games.dsgames.R;
import in.games.dsgames.utils.CustomJsonRequest;
import in.games.dsgames.utils.LoadingBar;

import static in.games.dsgames.Config.BaseUrl.URL_FORGOT_OTP;


public class Send_Otp extends AppCompatActivity {
    ImageView iv_back;
    Button btn;
    LoadingBar loadingBar ;
    Module module ;
    Common common;
    TextInputLayout textInputLayout_Phone;
    TextInputEditText textInputEditText_phone;
String mobilePattern ="(0/91)?[7-9][0-9]{9}";
String type ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send__otp);
common = new Common(this);
loadingBar = new LoadingBar(this);
module = new Module(this);
        textInputEditText_phone=findViewById(R.id.et_phone);
        iv_back = findViewById(R.id.iv_back);
        textInputLayout_Phone=findViewById(R.id.tv_phone);
        btn=findViewById(R.id.btn_send_opt);
type = getIntent().getStringExtra("type");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void send() {

        boolean isValid = true;
        String mobile = textInputEditText_phone.getText().toString().trim();
        if (mobile.isEmpty())
        {
            textInputLayout_Phone.setError("Enter your Mobile");
            isValid = false;
        }
        else if (!mobile.matches(mobilePattern))
        {
            textInputLayout_Phone.setError("Enter valid mobile number");
        }
        else
            {
            textInputLayout_Phone.setErrorEnabled(false);
            sendOtpforPass(mobile,module.getRandomKey(4));
           }
    }
    private void sendOtpforPass(final String mobile, final String otp) {
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("mobile",mobile);
        params.put("otp",otp);

        CustomJsonRequest customJsonRequest=new CustomJsonRequest(Request.Method.POST, URL_FORGOT_OTP, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("gen",""+response.toString());
                loadingBar.dismiss();
                try
                {
                    String res=response.getString("status");
                    if(res.equalsIgnoreCase("success"))
                    {
                        common.showToast(response.getString("message"));
                        Intent intent=new Intent(Send_Otp.this,GetOtp.class);
                        intent.putExtra("mobile",mobile);
                        intent.putExtra("otp",otp);
                        intent.putExtra("type",type);
                        startActivity(intent);
                    }
                    else
                    {
                        common.showToast(response.getString("message"));
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    common.showToast("Something went wrong");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String msg=common.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    common.showToast(""+msg);
                }
            }
        });
        AppController.getInstance().addToRequestQueue(customJsonRequest);

    }
}