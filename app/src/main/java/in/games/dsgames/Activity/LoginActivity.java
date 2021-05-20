package in.games.dsgames.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.games.dsgames.AppController;
import in.games.dsgames.Config.Module;
import in.games.dsgames.Config.Prevalent;
import in.games.dsgames.Model.UsersObjects;
import in.games.dsgames.R;
import in.games.dsgames.utils.CustomJsonRequest;
import in.games.dsgames.utils.LoadingBar;
import in.games.dsgames.utils.Session_management;

import static in.games.dsgames.Config.BaseUrl.URL_LOGIN;

public class LoginActivity extends AppCompatActivity {
Button btn;
Module module ;
LoadingBar loadingBar ;
    TextInputLayout username, password;
    TextInputEditText editText_username, editText_password;
Session_management session_management ;
TextView tv_signup ,tv_forget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        module = new Module(this);
        loadingBar = new LoadingBar(this);
        session_management = new Session_management(this);
        username=findViewById(R.id.tv_username);
        password=findViewById(R.id.tv_password);
        tv_signup = findViewById(R.id.tv_signup);
        editText_username=findViewById(R.id.et_username);
        editText_password=findViewById(R.id.et_password);
        btn = findViewById(R.id.btn_login);
        tv_forget= findViewById(R.id.tv_forget);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });
        tv_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,Send_Otp.class);
                i.putExtra("type","f");
                startActivity(i);

            }
        });
    }

    private void login() {

        if(editText_username.getText().toString().isEmpty()) {
           module.validateEditText(editText_username,username,"Enter Mobile Number");
        return;
        }
        else if (editText_username.getText().toString().length()!=10){
            module.showToast("Enter Valid Mobile Number");
//            module.validateEditText(editText_username,username,"Enter Valid Mobile Number");
        }
        else if(editText_password.getText().toString().isEmpty()) {
           module.validateEditText(editText_password,password,"Enter Password");
        }
       else if(editText_password.getText().toString().trim().length() < 4) {
            module.showToast("Enter Valid Password");
          //  module.validateEditText(editText_password,password,"Enter Valid Password");
        }
       else
        {
        loginRequest(editText_username.getText().toString(),editText_password.getText().toString());
        }

    }
    private void loginRequest(final String mName, final String mPass) {


       loadingBar.show();
        final String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobileno",mName);
        params.put("password",mPass);

         CustomJsonRequest loginRequest=new CustomJsonRequest(Request.Method.POST, URL_LOGIN, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                 loadingBar.dismiss();
                Log.e("login",response.toString());

                try {
                    boolean status = response.getBoolean("responce");

                        if (status)
                        {
                            JSONObject jsonObject = response.getJSONObject("data");
                            UsersObjects users = new UsersObjects();
                            users.setId(jsonObject.getString("id"));
                            users.setName(jsonObject.getString("name"));
                            users.setUsername(jsonObject.getString("username"));
                            users.setMobileno(jsonObject.getString("mobileno"));
                            users.setAddress(jsonObject.getString("address"));
                            users.setCity(jsonObject.getString("city"));
                            users.setPincode(jsonObject.getString("pincode"));
                            users.setPassword(jsonObject.getString("password"));
                            users.setAccountno(jsonObject.getString("accountno"));
                            users.setBank_name(jsonObject.getString("bank_name"));
                            users.setIfsc_code(jsonObject.getString("ifsc_code"));
                            users.setAccount_holder_name(jsonObject.getString("account_holder_name"));
                            users.setPaytm_no(jsonObject.getString("paytm_no"));
                            users.setTez_no(jsonObject.getString("tez_no"));
                            users.setPhonepay_no(jsonObject.getString("phonepay_no"));
                            Prevalent.currentOnlineuser = users;
                            String p = jsonObject.getString("password");
                            if (mPass.equals(p)) {
                                session_management.createLoginSession(jsonObject.getString("id"),jsonObject.getString("name"),jsonObject.getString("username"),
                                        jsonObject.getString("mobileno"),jsonObject.getString("email"),jsonObject.getString("dob"),
                                        jsonObject.getString("address"),jsonObject.getString("city"),
                                        jsonObject.getString("pincode"),jsonObject.getString("accountno"),jsonObject.getString("bank_name"),jsonObject.getString("ifsc_code"),jsonObject.getString("account_holder_name"),
                                        jsonObject.getString("paytm_no"),jsonObject.getString("tez_no"),jsonObject.getString("phonepay_no"),jsonObject.getString("wallet"));


                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                intent.putExtra("username", jsonObject.getString("username").toString());
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                                 loadingBar.dismiss();
                                finish();
                            }

                        }
                        else
                        {
                            module.showToast(response.getString("error"));
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                         loadingBar.dismiss();
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
        AppController.getInstance().addToRequestQueue(loginRequest,tag_json_obj);


    }
}