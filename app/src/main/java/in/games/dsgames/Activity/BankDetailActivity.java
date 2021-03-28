package in.games.dsgames.Activity;

import android.app.Activity;
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
import java.util.Map;

import in.games.dsgames.AppController;
import in.games.dsgames.Config.Module;
import in.games.dsgames.R;
import in.games.dsgames.utils.CustomJsonRequest;
import in.games.dsgames.utils.LoadingBar;
import in.games.dsgames.utils.Session_management;

import static in.games.dsgames.Config.BaseUrl.URL_REGISTER;
import static in.games.dsgames.Config.Constants.KEY_ACCOUNNO;
import static in.games.dsgames.Config.Constants.KEY_HOLDER;
import static in.games.dsgames.Config.Constants.KEY_ID;
import static in.games.dsgames.Config.Constants.KEY_IFSC;

public class BankDetailActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView iv_back;
    TextView tv_title;
    Button btn_add_update;
    LoadingBar loadingBar ;
    TextInputEditText et_name ,et_acc_no,et_ifsc;
    TextInputLayout lay_name,lay_acc,lay_ifsc;
    Module module;
    Activity ctx = BankDetailActivity.this;
    Session_management session_management ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_detail);
        initViews();
    }
    void initViews()
    { iv_back = findViewById(R.id.iv_back);
    tv_title= findViewById(R.id.tv_Title);
    btn_add_update = findViewById(R.id.btn_add_update);
    et_ifsc= findViewById(R.id.et_ifsc_code);
    et_acc_no = findViewById(R.id.et_account_no);
    et_name = findViewById(R.id.et_holder_name);
    lay_ifsc= findViewById(R.id.tv_ifsc_code);
    lay_acc = findViewById(R.id.tv_account_no);
    lay_name = findViewById(R.id.tv_holder_name);
    loadingBar = new LoadingBar(ctx);
    module = new Module(ctx);
    session_management = new Session_management(ctx);
    iv_back.setOnClickListener(this);
    btn_add_update.setOnClickListener(this);
  if( module.checkNullString(session_management.getUserDetails().get(KEY_HOLDER))==false)
  {
        et_name.setText(session_management.getUserDetails().get(KEY_HOLDER));
    }
        if( module.checkNullString(session_management.getUserDetails().get(KEY_ACCOUNNO))==false) {
            et_acc_no.setText(session_management.getUserDetails().get(KEY_ACCOUNNO));
        }
        if( module.checkNullString(session_management.getUserDetails().get(KEY_IFSC))==false)
        {
    et_ifsc.setText(session_management.getUserDetails().get(KEY_IFSC));}


    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.iv_back)
        {
            finish();
        }
        else if (view.getId()==R.id.btn_add_update)
        {
            String h_name = et_name.getText().toString().trim();
            String acc_no = et_acc_no.getText().toString().trim();
            String ifsc = et_ifsc.getText().toString().trim();
            if (h_name.isEmpty()&& acc_no.isEmpty()&& ifsc.isEmpty())
            {
                module.showToast("Enter Details");
            }
            else
            {
                if (h_name.isEmpty())
                {
                    module.validateEditText(et_name,lay_name,"Enter Account Holder Name");
                }
                else if (acc_no.isEmpty())
                {
                    module.validateEditText(et_acc_no,lay_acc,"Enter Account Number");
                }
                else if (ifsc.isEmpty())
                {
                    module.validateEditText(et_ifsc,lay_ifsc,"Enter IFSC Code");
                } else
                {
                    if(acc_no.length()<7){
                        module.showToast("Invalid Account Number");
                    }else{
                        storeBankDetails(acc_no,ifsc,h_name,session_management.getUserDetails().get(KEY_ID));
                    }

                }
            }
        }

    }
    private void storeBankDetails(final String accno,final String ifsc,final String hod_name,final String userid) {

        loadingBar.show();
        Map<String,String> params=new HashMap<>();
        params.put("key","3");
        params.put("user_id",userid);
        params.put("accountno",accno);
        params.put("bankname","bankname");
        params.put("ifsc",ifsc);
        params.put("accountholder",hod_name);

        CustomJsonRequest customJsonRequest=new CustomJsonRequest(Request.Method.POST, URL_REGISTER, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    Log.e("bank",response.toString());
                    boolean success=response.getBoolean("responce");
                    if(success)
                    {
                        loadingBar.dismiss();
                        session_management.updateAccSection(accno,"",ifsc,hod_name);
                        String msg=response.getString("message");
                        Toast.makeText(ctx, ""+msg, Toast.LENGTH_SHORT).show();
                        finish();

                    }
                    else
                    {
                        loadingBar.dismiss();
                        String msg=response.getString("message");
                        Toast.makeText(ctx, ""+msg, Toast.LENGTH_SHORT).show();

                        return;
                    }


                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    module.showToast("Something Went Wrong");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               loadingBar.dismiss();
                String msg=module.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    module.showToast(msg);
                }
            }
        });

        AppController.getInstance().addToRequestQueue(customJsonRequest,"add_bank_json");

    }

}