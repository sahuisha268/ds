package in.games.dsgames.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.games.dsgames.AppController;
import in.games.dsgames.Config.Module;
import in.games.dsgames.R;
import in.games.dsgames.utils.CustomJsonRequest;
import in.games.dsgames.utils.LoadingBar;

import static in.games.dsgames.Config.BaseUrl.URL_REGISTER;

public class RegistrationActivity extends AppCompatActivity {

Button btn;
LoadingBar loadingBar;
Spinner spinner_state, spinner_city;
Module module ;
TextInputLayout username, mobilenumber, password, confirm_password ,mpin;
TextInputEditText editText_username, editText_mobile, editText_password,editText_confirm_password ,et_mpin;
String state="" ,city="" ;
TextView tv_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        loadingBar = new LoadingBar(this);
        module = new Module(this);
        btn = findViewById(R.id.btn_register);

         spinner_state = findViewById(R.id.sp_state);
         spinner_city= findViewById(R.id.sp_city);

        username=findViewById(R.id.tv_username);
        tv_login=findViewById(R.id.tv_login);
        mpin=findViewById(R.id.tv_mpin);
        mobilenumber=findViewById(R.id.tv_mobile);
        password=findViewById(R.id.tv_password);
        confirm_password=findViewById(R.id.tv_confirm_password);
        editText_username=findViewById(R.id.et_username);
        editText_mobile=findViewById(R.id.et_mobile);
        et_mpin=findViewById(R.id.et_mpin);
        editText_password=findViewById(R.id.et_password);
        editText_confirm_password=findViewById(R.id.et_confirm_password);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.state));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_state.setAdapter(adapter);


        ArrayAdapter<String> adp = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.city));
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_city.setAdapter(adp);

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
            }
        });



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String name = editText_username.getText().toString().trim();
               String mobile = editText_mobile.getText().toString().trim();
               String pass = editText_password.getText().toString().trim();
               String cpass = editText_confirm_password.getText().toString().trim();
               String mpin = et_mpin.getText().toString().trim();

               if (name.isEmpty())
               {
                  module.validateRegEditText(editText_username,username,"Enter Username");
               }
               else if (mobile.isEmpty())
               {
                  module.validateRegEditText(editText_mobile,mobilenumber,"Enter Mobile Number");
               }
               else if(editText_mobile.getText().toString().trim().length() != 10) {
                   module.showToast("Enter Valid Mobile Number");
//                     module.validateRegEditText(editText_password,password,"Enter Valid Password");
               }
                   else if (pass.isEmpty())
                   {
                       module.validateRegEditText(editText_password,password,"Enter Password");
                   }
                   else if (cpass.isEmpty())
                   {
                       module.validateRegEditText(editText_confirm_password,confirm_password,"Enter Confirm Password");
                   }
                   else if(pass.length() < 4) {
                       module.validateRegEditText(editText_password,password,"Password length min 4 character");
                   } else if(cpass.length() < 4) {
                       module.validateRegEditText(editText_confirm_password,confirm_password,"Password length min 4 character");
                   }
                   else
                   {
                       if (!mobile.isEmpty()) {
                           char mob = mobile.charAt(0);
                           int m = Integer.parseInt(String.valueOf(mob));
                           Log.e("moblie_first_digit", String.valueOf(m));
                           if (m < 6) {

                               module.showToast("Enter Valid Mobile Number");

                           }else{
                               if (cpass.equals(pass))
                               {
                                   HashMap<String,String>params = new HashMap<>();
                                   params.put("key","1");
                                   params.put("username",name);
                                   params.put("name",name);
                                   params.put("mobile",mobile);
                                   params.put("password",pass);
                                   params.put("mpin",mpin);
//                           sendOtpForRegister(mobile,module.getRandomKey(4),params);
                                   makeRegisterRequest(name,mobile,pass,mpin,params);
                               }
                               else
                               {
                                   module.showToast("Passwords Don't Match");
                               }
                           }
                       }

                   }



            }
        });

    }

    void makeRegisterRequest(String name, final String mobile, String pass, String mpin, HashMap<String,String> params)
    {
        loadingBar.show();
        HashMap<String,String> param = new HashMap<>();
        param.put("key","1");
        param.put("username",name);
        param.put("name",name);
        param.put("mobile",mobile);
        param.put("password",pass);
        param.put("mpin",mpin);

        CustomJsonRequest jsonRequest = new CustomJsonRequest(Request.Method.POST, URL_REGISTER, param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingBar.dismiss();
                Log.e("register",response.toString());
                try {
                    boolean stat = response.getBoolean("responce");
                    if (stat)
                    {
                        module.showToast(response.getString("message"));
                        JSONObject dt = response.getJSONObject("data");
                        Intent intent=new Intent(RegistrationActivity.this,GetOtp.class);
                        intent.putExtra("mobile",mobile);
                        intent.putExtra("otp",dt.getString("otp"));
                        intent.putExtra("user_id",dt.getString("user_id"));
                        intent.putExtra("type",dt.getString("type"));
//                        intent.putExtra("map",map);
                        startActivity(intent);
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
                loadingBar.dismiss();
                module.showToast(module.VolleyErrorMessage(error));
                String msg=module.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    module.showToast(""+msg);
                }

            }
        });
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    @Override
    protected void onPause() {
        super.onPause();
        editText_username.setError(null);
        editText_mobile.setError(null);
        editText_password.setError(null);
        editText_confirm_password.setError(null);
        et_mpin.setError(null);

    }

    public void validation(){
        String name = editText_username.getText().toString().trim();
        String mobile = editText_mobile.getText().toString().trim();
        String pass = editText_password.getText().toString().trim();
        String cpass = editText_confirm_password.getText().toString().trim();
        String mpin = et_mpin.getText().toString().trim();
        if(name.isEmpty()){

        }
    }
   






}


