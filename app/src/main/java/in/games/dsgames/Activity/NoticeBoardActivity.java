package in.games.dsgames.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

import in.games.dsgames.Adapter.NoticeBoardAdapter;
import in.games.dsgames.AppController;
import in.games.dsgames.Config.Module;
import in.games.dsgames.Model.NoticeBoardModel;
import in.games.dsgames.R;
import in.games.dsgames.utils.CustomJsonRequest;
import in.games.dsgames.utils.LoadingBar;

import static in.games.dsgames.Config.BaseUrl.URL_NOTICE_BOARD;

public class NoticeBoardActivity extends AppCompatActivity {
TextView textView;
LoadingBar loadingBar;
Module module ;
ArrayList<NoticeBoardModel> list;
  RecyclerView recycler_rates;
  TextView tv_text,tv_title;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);
            loadingBar = new LoadingBar(NoticeBoardActivity.this);
            module = new Module(NoticeBoardActivity.this);
        list = new ArrayList<>();
        tv_title=findViewById(R.id.tv_title);
        tv_text =findViewById(R.id.tv_text);

        recycler_rates = findViewById(R.id.recycler_rates);
//        recycler_rates.setHasFixedSize(true);
        recycler_rates.setLayoutManager(new LinearLayoutManager(NoticeBoardActivity.this));


//        recycler_rates.setAdapter(new NoticeBoardAdapter(initData(),NoticeBoardActivity.this));
          //  textView=findViewById(R.id.tv_notice_board);
            getNoticeBoard();

        }
//        public ArrayList<NoticeBoardModel> initData()
//        {
//            list = new ArrayList<>();
//         //   list.add(new NoticeBoardModel("WITHDRAW","JHGF NJHGF HGF JHGHYTF DCVJK JNBV"));
//            //list.add(new NoticeBoardModel("notice", "hgvf hgft gfg ihjn xfcvg vygu vygu"));
//          return list;
//        }
    private void getNoticeBoard() {
        loadingBar.show();
        HashMap<String,String> params = new HashMap<>();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, URL_NOTICE_BOARD, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
             loadingBar.dismiss();
                Log.e("notice",response.toString());
                try {
//                    String res = response.getString("responce");
//                    JSONObject resobj=new JSONObject(res);
                    boolean stat = response.getBoolean("responce");
                    if(stat){
                        JSONArray dataArr=response.getJSONArray("data");
                      //  JSONObject obj=dataArr.getJSONObject(0);
                      //  String str=obj.getString("title");

                      //  tv_title.setText(Html.fromHtml(str));
                        for (int i =0 ;i <dataArr.length();i++) {
                            NoticeBoardModel model = new NoticeBoardModel("nbhvg","bhvg");
                            JSONObject object = dataArr.getJSONObject(i);
                            model.setTitle(object.getString("title"));
                            model.setText(object.getString("description"));

                            list.add(model);
                            //  list.add()
                        }

                        NoticeBoardAdapter noticeBoardAdapter = new NoticeBoardAdapter(list,NoticeBoardActivity.this);
                        recycler_rates.setAdapter(noticeBoardAdapter);

                        //tv_title.setText(title);
                    }
//                        String resp = obj.getString("noticeboard");
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                module.showToast(""+module.VolleyErrorMessage(error));
            }
        });
        AppController.getInstance().addToRequestQueue(customJsonRequest);
    }

    }

