package in.games.dsgames.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

import in.games.dsgames.AppController;
import in.games.dsgames.Config.BaseUrl;
import in.games.dsgames.Config.Common;
import in.games.dsgames.Config.Module;
import in.games.dsgames.R;
import in.games.dsgames.utils.CustomJsonRequest;
import in.games.dsgames.utils.LoadingBar;
import in.games.dsgames.utils.Session_management;

import static in.games.dsgames.Config.Constants.KEY_ID;


public class ChangePasswordActivity extends AppCompatActivity {
    private final String TAG=ChangePasswordActivity.class.getSimpleName();
    Activity ctx=ChangePasswordActivity.this;
    EditText et_cnew_pass,et_new_pass,et_old_pass;
    ImageView iv_back;
    Button btn_change;
    Session_management session_management;
    LoadingBar loadingBar;
    Common common;
    Module module;
    CheckBox chk_show;
    TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initViews();
    }

    private void initViews() {

//        iv_back = findViewById(R.id.iv_back);
//        btn_sumbit = findViewById(R.id.btn_sumbit);
//        tv_oldPassword = findViewById(R.id.tv_oldPassword);
//        tv_newPassword = findViewById(R.id.tv_newPassword);
//        tv_confirmPassword = findViewById(R.id.tv_confirmPassword);
//        et_oldPassword = findViewById(R.id.et_oldPassword);
//        et_newPassword = findViewById(R.id.et_newPassword);
//        et_confirmPassword = findViewById(R.id.et_confirmPassword);
//        common = new Common(ChangePasswordActivity.this);


        et_old_pass=findViewById(R.id.et_oldPassword);
        et_new_pass=findViewById(R.id.et_newPassword);
        et_cnew_pass=findViewById(R.id.et_confirmPassword);
//        chk_show=findViewById(R.id.chk_show);
        iv_back=findViewById(R.id.iv_back);
        btn_change=findViewById(R.id.btn_sumbit);
        tv_title = findViewById(R.id.tv_title);
        session_management=new Session_management(ctx);
        loadingBar=new LoadingBar(ctx);
        common=new Common(ctx);
        tv_title.setText("Change Password");


        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String user_id=common.getUserID();
                String user_id = session_management.getUserDetails().get(KEY_ID);
                String old_pass=et_old_pass.getText().toString();
                String new_pass=et_new_pass.getText().toString();
                String con_pass=et_cnew_pass.getText().toString();
                if(old_pass.isEmpty()){
                    common.showToast("Enter Current Password");
                    et_old_pass.requestFocus();
                    return;
                }else if(old_pass.length()<4){
                    common.showToast("Password must be at least 4 characters");
                    et_old_pass.requestFocus();
                    return;
                }else if(new_pass.isEmpty()){
                    common.showToast("Enter New Password");
                    et_new_pass.requestFocus();
                    return;
                }else if(new_pass.length()<4){
                    common.showToast("Password must be at least 4 characters");
                    et_new_pass.requestFocus();
                    return;
                }else if(con_pass.isEmpty()){
                    common.showToast("Enter Confirm Password");
                    et_cnew_pass.requestFocus();
                    return;
                }else if(con_pass.length()<4){
                    common.showToast("Password must be at least 4 characters");
                    et_cnew_pass.requestFocus();
                    return;
                }else {

                    if(!new_pass.equalsIgnoreCase(con_pass)){
                        common.showToast("Password must be matched");
                    }else{
                        changePassword(user_id,old_pass,new_pass);
                    }
                }

            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        chk_show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(b){
//                    et_old_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                    et_new_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                    et_cnew_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                }else{
//                    et_old_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                    et_new_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                    et_cnew_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                }
//            }
//        });
    }

    private void changePassword(String user_id, String old_pass, String new_pass) {
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",user_id);
        params.put("old_pass",old_pass);
        params.put("new_pass",new_pass);


        Log.e(TAG, "changePassword: "+params.toString() );
        CustomJsonRequest request=new CustomJsonRequest(Request.Method.POST, BaseUrl.CHANGE_PASSWORD, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("changePasswordResp",response.toString());
                loadingBar.dismiss();
                try {

                    JSONObject object = new JSONObject(response.toString());

                    if (object.getBoolean("response"))
                    {
                        common.showToast(""+response.getString("message"));
                        finish();
                    }
                    else{
                        common.showToast(""+response.getString("error"));
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                common.showVolleyError(error);

            }
        });
        AppController.getInstance().addToRequestQueue(request);

    }
}








