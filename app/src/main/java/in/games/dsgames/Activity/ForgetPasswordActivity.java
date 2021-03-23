package in.games.dsgames.Activity;

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

import org.json.JSONObject;

import java.util.HashMap;

import in.games.dsgames.AppController;
import in.games.dsgames.Config.Common;
import in.games.dsgames.Config.Module;
import in.games.dsgames.R;
import in.games.dsgames.utils.CustomJsonRequest;
import in.games.dsgames.utils.LoadingBar;

import static in.games.dsgames.Config.BaseUrl.URL_UPDATE_PASS;

public class ForgetPasswordActivity extends AppCompatActivity {
Button btn_submit;
ImageView iv_back;
String pass;
String cpass;
String mobile;
String otp="";
LoadingBar loadingBar;
Module module;
TextView tv_whatsAppText;
Button btn_whatSumbit;
Common common;


TextInputEditText password,confpassword,editText_mobile;
TextInputLayout textInputLayoutpassword, textInputLayoutconfpassword, mobilenumber;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        common = new Common(ForgetPasswordActivity.this);
        btn_submit=findViewById(R.id.btn_forget_sumbit);

        iv_back = findViewById(R.id.iv_back);
        iv_back=findViewById(R.id.iv_back);
        password=findViewById(R.id.et_password_forget);
        confpassword=findViewById(R.id.et_conf_password_forget);
        textInputLayoutpassword=findViewById(R.id.tv_password_forget);
        textInputLayoutconfpassword=findViewById(R.id.tv_conf_password_forget);

        module = new Module(ForgetPasswordActivity.this);
        loadingBar = new LoadingBar(ForgetPasswordActivity.this);





        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pass = password.getText().toString().trim();
                cpass = confpassword.getText().toString().trim();
                mobile = getIntent().getStringExtra("mobile");
                otp = getIntent().getStringExtra("otp");

//              getforgetpassword(pass,cpass);


                 boolean isValid=true;
                 if (password.getText().toString().trim().length() < 6)
                {
                   textInputLayoutpassword.setError("Enter Password");
                   isValid=false;
                }
                else if (confpassword.getText().toString().trim().length() < 6)
                {
                    textInputLayoutconfpassword.setError("Enter Confirm Password");
                    isValid=false;
                }
                else if (!pass.equals(cpass))
                {
                    textInputLayoutconfpassword.setError("password didn't match");
                    isValid=false;
                 }
               else
                 {
                     getforgetpassword(pass,mobile,otp);
                     Toast.makeText(ForgetPasswordActivity.this, "password update successfull", Toast.LENGTH_SHORT).show();
                 }
                if(isValid){
                    Toast.makeText(ForgetPasswordActivity.this, R.string.signup_success, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void getforgetpassword(String pass, String mobile, String otp) {
        loadingBar.show();
        final HashMap<String,String> params=new HashMap<>();
        String tag_json_req="forget_password_request";

        params.put("password",pass);
        params.put("mobile",mobile);
        params.put("otp",otp);

        CustomJsonRequest customVolleyJsonRequest=new CustomJsonRequest(Request.Method.POST, URL_UPDATE_PASS,params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                    loadingBar.dismiss();
                    try {
                        String res = response.getString("status");

                        if (res == "failed") {
                            String str = response.getString("message");
                            Toast toast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
                            toast.show();

                        } else {
                            String error = response.getString("message");
                            Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT);
                            toast.show();
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                Log.e("response", "onResponse: "+ response.toString()+"\n"+params.toString());
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError message) {
                loadingBar.dismiss();
                message.printStackTrace();
                String msg=module.VolleyErrorMessage(message);
                if(!msg.equals(""))
                {
                    Toast toast=Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest);

    }
}