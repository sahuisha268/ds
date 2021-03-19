package in.games.dsgames.Activity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.games.dsgames.AppController;
import in.games.dsgames.Config.Module;
import in.games.dsgames.R;
import in.games.dsgames.utils.CustomVolleyJsonArrayRequest;
import in.games.dsgames.utils.Session_management;

import static in.games.dsgames.Config.BaseUrl.URL_INDEX;

public class SplashActivity extends AppCompatActivity {
Session_management session_management;
    public static String tagline,withdrw_text,withdrw_no,whatsapp_no,home_text,min_add_amount,min_withdraw_amount,msg_status,app_link,share_link,ver_code,dialog_image,call_no,min_bid_amount;
Module module ;
    private int version_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       requestWindowFeature(Window.FEATURE_NO_TITLE);
       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
       setContentView(R.layout.activity_splash);
       session_management = new Session_management(this);
       module = new Module(this);
        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            version_code = pInfo.versionCode;
            // Toast.makeText(splash_activity.this,""+version,Toast.LENGTH_LONG).show();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

             getApiData();
            }
        },2000);

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
                    tagline = dataObj.getString("tag_line");
                    withdrw_text = dataObj.getString("withdraw_text").toLowerCase();
                    withdrw_no = dataObj.getString("withdraw_no");
                    home_text = dataObj.getString("home_text").toString();
                    min_add_amount = dataObj.getString("min_amount");
                    msg_status = dataObj.getString("msg_status");
                    app_link = dataObj.getString("app_link");
                    share_link = dataObj.getString("share_link");
                    ver_code= dataObj.getString("version");
                    whatsapp_no= dataObj.getString("home_whatsapp");
                    call_no= dataObj.getString("home_call");
                    min_bid_amount= dataObj.getString("min_bid_points");
                    min_withdraw_amount= dataObj.getString("w_amount");
                    if(String.valueOf(version_code).equals(ver_code))
                    {
                        if (session_management.isLoggedIn())
                        {
                            startActivity(new Intent(SplashActivity.this,MainActivity.class));
                        }
                        else
                        {
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        }

                    }
                    else
                    {
                        AlertDialog.Builder builder=new AlertDialog.Builder(SplashActivity.this);
                        builder.setTitle("New Version Available");
                        builder.setMessage("Update your app to continue using");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String url = null;
                                try {
                                    url = app_link;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(url));
                                startActivity(intent);

                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                finishAffinity();
                            }
                        });
                        AlertDialog alertDialog=builder.create();
                        alertDialog.show();
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
}