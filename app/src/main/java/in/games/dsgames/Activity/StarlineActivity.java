package in.games.dsgames.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.games.dsgames.Adapter.StarlineAdapter;
import in.games.dsgames.Config.Common;
import in.games.dsgames.Config.Module;
import in.games.dsgames.Model.Starline_Objects;
import in.games.dsgames.R;
import in.games.dsgames.utils.LoadingBar;
import in.games.dsgames.utils.RecyclerTouchListener;
import in.games.dsgames.utils.Session_management;

import static in.games.dsgames.Config.BaseUrl.URL_STARLINE;
import static in.games.dsgames.Config.Constants.KEY_ID;

public class StarlineActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView iv_back,iv_reload;
    TextView tv_title ,tv_wallet;
    Session_management session_management ;
    LoadingBar loadingBar ;
    Common common;
    Activity ctx =StarlineActivity.this;
    RecyclerView rv_starline;
    StarlineAdapter starlineAdapter;
    ArrayList<Starline_Objects>star_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starline);
        initViews();
    }

    void initViews()
    {
        session_management = new Session_management(ctx);
        common = new Common(ctx);
        loadingBar = new LoadingBar(ctx);
        iv_back = findViewById(R.id.iv_back);
        iv_reload= findViewById(R.id.iv_reload);
        tv_title= findViewById(R.id.tv_Title);
        tv_wallet= findViewById(R.id.tv_wallet);
        rv_starline= findViewById(R.id.rv_starline);
        tv_title.setText(""+getIntent().getStringExtra("title"));
        star_list = new ArrayList<>();
        iv_back.setOnClickListener(this);
        rv_starline.setLayoutManager(new LinearLayoutManager(ctx));
        common.setWallet_Amount(tv_wallet,loadingBar,session_management.getUserDetails().get(KEY_ID));
        getMatkaData();

        rv_starline.addOnItemTouchListener(new RecyclerTouchListener(ctx, rv_starline, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Starline_Objects starline_objects=star_list.get(position);
                int sTime=common.getTimeFormatStatus(starline_objects.getS_game_time());
                Date date=new Date();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH");
                String ddt=simpleDateFormat.format(date);
                int c_tm=Integer.parseInt(ddt);
                String st=common.get24Hours(starline_objects.getS_game_end_time().toString());
                long tmLong=common.getTimeDifference(st);
//                Toast.makeText(PlayGameActivity.this," dt:  "+tmLong,Toast.LENGTH_LONG).show();
                if(tmLong<=0)
                {common.errorMessageDialog("Betting is closed for today");
                    return;
                }
                else
                {

                    String e_t = common.get24Hours(starline_objects.getS_game_end_time());
                    String s_t = common.get24Hours(starline_objects.getS_game_time());
                    Log.e("time",s_t+"\n"+e_t);

                    String s_id=starline_objects.getId();
                    String matka_name="STARLINE";

                    Intent intent=new Intent(ctx,SeelctGameActivity.class);

                    intent.putExtra("matka_name",matka_name);
                    intent.putExtra("m_id",s_id);
                    intent.putExtra("end_time",e_t);
                    intent.putExtra("start_time",s_t);
                    intent.putExtra("start_num",starline_objects.getS_game_number());
                    intent.putExtra("num","");
                    intent.putExtra("end_num","");
                    startActivity(intent);
                    //Toast.makeText(PlayGameActivity.this,""+s_id,Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }
    public void getMatkaData()
    {
        loadingBar.show();

        final JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL_STARLINE, new
                Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("starline",response.toString());

                        for(int i=0; i<response.length();i++)
                        {
                            try
                            {

                                JSONObject jsonObject=response.getJSONObject(i);

                                Starline_Objects matkasObjects=new Starline_Objects();
                                matkasObjects.setId(jsonObject.getString("id"));
                                matkasObjects.setS_game_time(jsonObject.getString("s_game_time"));
                                matkasObjects.setS_game_number(jsonObject.getString("s_game_number"));
                                matkasObjects.setS_game_end_time(jsonObject.getString("s_game_end_time"));

                                star_list.add(matkasObjects);
                                if (star_list.size()>0)
                                {
                                    starlineAdapter = new StarlineAdapter(star_list,ctx);
                                    rv_starline.setAdapter(starlineAdapter);
                                }
                            }
                            catch (Exception ex)
                            {
                                loadingBar.dismiss();
                                Toast.makeText(ctx,"Error :"+ex.getMessage(),Toast.LENGTH_LONG).show();

                                return;
                            }
                        }

                        loadingBar.dismiss();


                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    new Module(ctx).showToast(common.VolleyErrorMessage(error));

                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);



    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.iv_back)
        {
            finish();
//
        }
    }
}
