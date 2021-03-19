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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.games.dsgames.Adapter.Withdraw_request_Adapter;
import in.games.dsgames.Config.BaseUrl;
import in.games.dsgames.Config.Common;
import in.games.dsgames.Config.Module;
import in.games.dsgames.Model.Withdraw_requwset_obect;
import in.games.dsgames.R;
import in.games.dsgames.utils.LoadingBar;
import in.games.dsgames.utils.Session_management;

import static in.games.dsgames.Config.Constants.KEY_ID;

public class WalletHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_wallet,tv_his ;
    Button btn_withdrw ,btn_deposit;
    RecyclerView rv_wallet_histry;
    LoadingBar loadingBar;
    Module module ;
    Common common ;
    ImageView iv_back;
    Session_management session_management;
Activity ctx = WalletHistoryActivity.this;
    ArrayList<Withdraw_requwset_obect> list;
    Withdraw_request_Adapter request_historyAdapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_history);

        initViews();

    }
   void initViews()
    {  iv_back = findViewById(R.id.iv_back);
       tv_wallet= findViewById(R.id.tv_wallet);
       btn_deposit = findViewById(R.id.btn_deposit);
       btn_withdrw = findViewById(R.id.btn_withdraw);
       tv_his=findViewById(R.id.tv_his);
       rv_wallet_histry = findViewById(R.id.rv_wallet_histry);
       session_management = new Session_management(ctx);
       loadingBar = new LoadingBar(ctx);
       module = new Module(ctx);
       common = new Common(ctx);
       btn_withdrw.setOnClickListener(this);
       btn_deposit.setOnClickListener(this);
       iv_back.setOnClickListener(this);
       progressDialog = new ProgressDialog(WalletHistoryActivity.this);
       progressDialog.setMessage("Please Wait...");
       common.setWallet_Amount(tv_wallet,loadingBar,session_management.getUserDetails().get(KEY_ID));

        rv_wallet_histry.setHasFixedSize(true);
        rv_wallet_histry.setLayoutManager(new LinearLayoutManager(WalletHistoryActivity.this));


        rv_wallet_histry.setAdapter(new Withdraw_request_Adapter(WalletHistoryActivity.this,initData()));
        String User_id= session_management.getUserDetails().get(KEY_ID);

        getWithdrawData(User_id);
        getRequestData(User_id);

    }
    public ArrayList<Withdraw_requwset_obect> initData()
    {
        list = new ArrayList<>();
         // list.add(new Withdraw_requwset_obect());
        //list.add(new NoticeBoardModel("notice", "hgvf hgft gfg ihjn xfcvg vygu vygu"));
        return list;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_withdraw:
                startActivity(new Intent(ctx,WithDrawActivity.class));
                break;
                case R.id.btn_deposit:
                    startActivity(new Intent(ctx,RequestPointActivity.class) );
                break;
            case R.id.iv_back:
                finish();
                break;
        }

    }
    private void getRequestData(final String user_id) {

       progressDialog.show();

        list.clear();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, BaseUrl.URL_POINT_ADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("points_histry",response.toString());
                progressDialog.dismiss();
                try {
                    JSONObject obj=new JSONObject(response);
                    JSONArray jsonArray=obj.getJSONArray("data");

                    for(int i=0; i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Withdraw_requwset_obect matkasObjects = new Withdraw_requwset_obect();
                        matkasObjects.setId(jsonObject.getString("request_id"));
                        matkasObjects.setWithdraw_points(jsonObject.getString("request_points"));
                        matkasObjects.setTime(jsonObject.getString("time"));
                        matkasObjects.setWithdraw_status(jsonObject.getString("request_status"));
                        matkasObjects.setUser_id(jsonObject.getString("user_id"));
                        matkasObjects.setType(jsonObject.getString("type"));
                        list.add(matkasObjects);

                    }

                    if (list.size()>0){
                        request_historyAdapter=new Withdraw_request_Adapter(WalletHistoryActivity.this,list);
                        rv_wallet_histry.setAdapter(request_historyAdapter);

                        request_historyAdapter.notifyDataSetChanged();
                    }
                    else{
                        //progressDialog.dismiss();
                    }
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(WalletHistoryActivity.this,"Error"+error.toString(),Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();

                params.put("user_id",user_id);

                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void getWithdrawData(final String user_id) {

            progressDialog.show();

        list.clear();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, BaseUrl.URL_POINT_WITHDRAW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("points_withdraw_histry",response.toString());
                progressDialog.dismiss();
                try {
                    JSONObject obj=new JSONObject(response);
                    JSONArray jsonArray=obj.getJSONArray("data");

                    for(int i=0; i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Withdraw_requwset_obect matkasObjects = new Withdraw_requwset_obect();
                        matkasObjects.setId(jsonObject.getString("request_id"));
                        matkasObjects.setWithdraw_points(jsonObject.getString("request_points"));
                        matkasObjects.setTime(jsonObject.getString("time"));
                        matkasObjects.setWithdraw_status(jsonObject.getString("request_status"));
                        matkasObjects.setUser_id(jsonObject.getString("user_id"));
                        matkasObjects.setType(jsonObject.getString("type"));
                        list.add(matkasObjects);
                    }
                    if (list.size()>0){
                        request_historyAdapter=new Withdraw_request_Adapter(WalletHistoryActivity.this,list);
                        rv_wallet_histry.setAdapter(request_historyAdapter);
                        request_historyAdapter.notifyDataSetChanged();
                    }else {

                    }

                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(WalletHistoryActivity.this,"Error"+error.toString(),Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();

                params.put("user_id",user_id);

                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}