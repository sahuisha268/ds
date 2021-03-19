package in.games.dsgames.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.games.dsgames.Adapter.GameRateAdapter;
import in.games.dsgames.AppController;
import in.games.dsgames.Config.Common;
import in.games.dsgames.Config.Module;
import in.games.dsgames.Model.GameRateModel;
import in.games.dsgames.R;
import in.games.dsgames.utils.CustomJsonRequest;
import in.games.dsgames.utils.LoadingBar;

import static in.games.dsgames.Config.BaseUrl.URL_NOTICE;

public class GameRatesActivity extends AppCompatActivity {
LoadingBar loadingBar;
Module module ;
ArrayList<GameRateModel> list ,slist;
Activity ctx = GameRatesActivity.this;
GameRateAdapter gameRateAdapter ,sGameRateAdapter;
RecyclerView rv_rates,rv_star_rates;
ImageView iv_back;
RecyclerView.LayoutManager layoutManager,layoutManager1;
// TextView txtsp,txtdp,txttp,txtsd,txtjd,txtrb,txths,txtfs,txts_sp,txts_dp,txts_tp,txts_sd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_rates);
       initViews();

  


    }
    void initViews()
    {
        rv_rates = findViewById(R.id.rv_m_rates);
        rv_star_rates = findViewById(R.id.rv_star_rate);
        iv_back = findViewById(R.id.iv_back);
        rv_star_rates.setLayoutManager(new LinearLayoutManager(ctx));
        rv_rates.setLayoutManager(new LinearLayoutManager(ctx));
        module = new Module(ctx);
        loadingBar = new LoadingBar(ctx);

        getNotice();
        slist= new ArrayList<>();
        list = new ArrayList<>();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    private void getNotice() {

        loadingBar.show();
      list=new ArrayList<>();
      slist=new ArrayList<>();
        String tag_json_obj = "json_notice_req";
        Map<String, String> params = new HashMap<String, String>();

        CustomJsonRequest customJsonRequest=new CustomJsonRequest(Request.Method.POST,URL_NOTICE, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String status=response.getString("status");
                    if(status.equals("success"))
                    {
                        JSONArray array=response.getJSONArray("data");

                        for (int i=0; i<array.length();i++)
                        {
                            GameRateModel gameRateModel=new GameRateModel();
                            JSONObject object=array.getJSONObject(i);
                             gameRateModel.setId(object.getString("id"));
                             gameRateModel.setName(object.getString("name"));
                             gameRateModel.setRate_range(object.getString("rate_range"));
                             gameRateModel.setRate(object.getString("rate"));
                             String type=object.getString("type").toString();
                             gameRateModel.setType(type);
                             if(type.equals("0"))
                             {
                                 list.add(gameRateModel);

                             }
                             else
                             {
                                 slist.add(gameRateModel);
                             }


                        }
                       gameRateAdapter = new GameRateAdapter(list,ctx);
                      sGameRateAdapter = new GameRateAdapter(slist,ctx);
                      rv_rates.setAdapter(gameRateAdapter);
                      rv_star_rates.setAdapter(sGameRateAdapter);
                    }
                    else
                    {
                        Toast.makeText(ctx,"Something Went Wrong", Toast.LENGTH_LONG).show();
                    }

                    loadingBar.dismiss();
                }
                catch (Exception ex)
                {
                    loadingBar.dismiss();
                    Toast.makeText(ctx,"Something Went Wrong", Toast.LENGTH_LONG).show();
//                    Toast.makeText(ctx,""+ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
               module.showToast(new Common(ctx).VolleyErrorMessage(error));
            }
        });
        AppController.getInstance().addToRequestQueue(customJsonRequest,tag_json_obj);
    }

}
