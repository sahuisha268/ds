package in.games.dsgames.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.games.dsgames.AppController;
import in.games.dsgames.Config.Common;
import in.games.dsgames.Config.Module;
import in.games.dsgames.R;
import in.games.dsgames.utils.CustomJsonRequest;
import in.games.dsgames.utils.CustomVolleyJsonArrayRequest;
import in.games.dsgames.utils.Session_management;

import static in.games.dsgames.Config.BaseUrl.URL_FEEDBACK;
import static in.games.dsgames.Config.BaseUrl.URL_INDEX;
import static in.games.dsgames.Config.Constants.KEY_ID;
import static in.games.dsgames.Config.Constants.KEY_MOBILE;
import static in.games.dsgames.Config.Constants.KEY_NAME;


public class ContactUsActivity extends AppCompatActivity {

    EditText user_name;
    EditText user_phone;
    EditText messages;
    TextView tv_whatsapp,tv_phone;
    LinearLayout ll_whatsapp,ll_phone;
    String phn,whts;
    String name;
    String mobile;
    String message;
    String id;
    Module module;
    ImageView iv_back;
    Common common;
Session_management session_management;
    Button btn;
TextView tv_Title,tv_wallet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        session_management=new Session_management(ContactUsActivity.this);
        tv_Title=findViewById(R.id.tv_Title);
        tv_Title.setText("FeedBack");
        btn=findViewById(R.id.submit);
        iv_back=findViewById(R.id.iv_back);
        user_name=findViewById(R.id.et_name);
        user_phone=findViewById(R.id.et_phone);
        messages=findViewById(R.id.et_message);
        tv_wallet=findViewById(R.id.tv_wallet);
        tv_whatsapp=findViewById(R.id.whatsapp);
        tv_phone=findViewById(R.id.phone);
        ll_whatsapp=findViewById(R.id.ll_whatsapp);
        ll_phone=findViewById(R.id.ll_phone);
        common= new Common(ContactUsActivity.this);
        id=session_management.getUserDetails().get(KEY_ID);
        user_name.setText(session_management.getUserDetails().get(KEY_NAME));
        user_phone.setText(session_management.getUserDetails().get(KEY_MOBILE));
        tv_wallet.setVisibility(View.GONE);
        getApiData();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name=session_management.getUserDetails().get(KEY_NAME);
                mobile=session_management.getUserDetails().get(KEY_MOBILE);
                message=messages.getText().toString();
                checkDataEntered();

            }
        });
                ll_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL); // Action for what intent called for
                intent.setData(Uri.parse("tel: " + phn)); // Data with intent respective action on intent
                startActivity(intent);
            }
        });
        ll_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                common.whatsapp(whts,"Hi!, Admin");
            }
        });
    }
    private void getaboutus(String user_id,String name, String mobile, String message) {
        HashMap<String, String> params=new HashMap<>();
        String tag_json_req="Contact_us_request";

        params.put("user_id",user_id );
        params.put("user_name", name);
        params.put("user_phone", mobile);
        params.put("user_email","");
        params.put("message", message);


        CustomJsonRequest customVolleyJsonRequest=new CustomJsonRequest(Request.Method.POST, URL_FEEDBACK,params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean res=response.getBoolean("responce");

                    if(res){
                        String str=response.getString("message");
                        Toast toast=Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT);
                        toast.show();

                    }
                    else{
                        String error = response.getString("message");
                        Toast toast=Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT);
                        toast.show();

                    }

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

                Log.e("response", "onResponse: "+ response.toString());
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError message) {
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
                    whts= dataObj.getString("home_whatsapp");
                    phn= dataObj.getString("home_call");
                    tv_whatsapp.setText(whts);
                    tv_phone.setText(phn);

                    if (whts.equals(null)||whts.isEmpty()||whts.equalsIgnoreCase("0000000000"))
                    {
                        ll_whatsapp.setVisibility(View.GONE);
                    }
                    else {
                        ll_whatsapp.setVisibility(View.VISIBLE);
                    }

                    if (phn.equals(null)||phn.equals(" ")||phn.equalsIgnoreCase("0000000000"))
                    {
                      ll_phone.setVisibility(View.GONE);

                    }
                    else {
                        ll_phone.setVisibility(View.VISIBLE);
                    }


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



    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }


    public  void checkDataEntered()
    {
        if (isEmpty(user_name)) {
            user_name.setError("Enter valid username!");
        }

        else if (isEmpty(user_phone)) {
            user_phone.setError("Enter valid number!");
        }

       else if (isEmpty(messages)) {
            messages.setError("Enter valid message!");
        }
       else
        {
            getaboutus(id,name,mobile,message);

        }

    }
}