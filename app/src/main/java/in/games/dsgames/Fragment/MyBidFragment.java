package in.games.dsgames.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.games.dsgames.Adapter.BidHistoryAdapter;
import in.games.dsgames.AppController;
import in.games.dsgames.Config.Common;
import in.games.dsgames.Config.Module;
import in.games.dsgames.Model.BidHistoryObjects;
import in.games.dsgames.R;
import in.games.dsgames.utils.CustomJsonRequest;
import in.games.dsgames.utils.LoadingBar;
import in.games.dsgames.utils.Session_management;

import static in.games.dsgames.Config.BaseUrl.URL_HISTORY;
import static in.games.dsgames.Config.BaseUrl.URl_STARLINE_HISTORY;
import static in.games.dsgames.Config.Constants.KEY_ID;


public class MyBidFragment extends Fragment {
LoadingBar loadingBar ;
Module module ;
Session_management session_management ;
RecyclerView rv_hitry;
ArrayList<BidHistoryObjects>list ,bid_list;
Common common;
TextView tv_no_data;
BidHistoryAdapter bidHistoryAdapter;
String user_id;
String type,url;

    public MyBidFragment(String type) {
        this.type = type;
    }

    public MyBidFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_bid, container, false);
        initViews(v);
        return v;
    }
    void  initViews(View v)
    {
        module = new Module(getActivity());
    session_management = new Session_management(getActivity());
    loadingBar = new LoadingBar(getActivity());
    common = new Common(getActivity());
    rv_hitry = v.findViewById(R.id.rv_bid_histry);
   tv_no_data = v.findViewById(R.id.tv_no);
   user_id = session_management.getUserDetails().get(KEY_ID);
    rv_hitry.setLayoutManager(new LinearLayoutManager(getActivity()));
    list = new ArrayList<>();
    bid_list = new ArrayList<>();

        rv_hitry = v.findViewById(R.id.rv_bid_histry);
        rv_hitry.setHasFixedSize(true);
        rv_hitry.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_hitry.setAdapter(new BidHistoryAdapter(getActivity(),initData()));
        //  textView=findViewById(R.id.tv_notice_board);
        if (type.equals("matka"))
        {
            url=URL_HISTORY;
        }
        else {
            url=URl_STARLINE_HISTORY;
        }
        getBidData(user_id,url);



    }
    public ArrayList<BidHistoryObjects> initData()
    {
        list = new ArrayList<>();
     //   list.add(new BidHistoryObjects("nb","nb","nbv","10","8931","16-12-2020","1:30 PM","close","matka name","1","close","edfws","cdea","dsa"));
        // list.add(new BidHistoryObjects("WITHDRAW","JHGF NJHGF HGF JHGHYTF DCVJK JNBV"));
        // list.add(new BidHistoryObjects("notice", "hgvf hgft gfg ihjn xfcvg vygu vygu"));
        return list;
    }

    private void getBidData(String user_id, final String url) {
        final String json_request_tag="json_bid_history_tag";
        final HashMap <String,String> params = new HashMap<>();
        params.put("user_id",user_id);
        loadingBar.show();
        list.clear();

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("respose bid",response.toString()+"/n"+url+"/n"+params.toString());
                loadingBar.dismiss();
                try {
                    boolean stat = response.getBoolean("responce");
                    if(stat){
                        JSONArray dataArr=response.getJSONArray("data");
                        //  JSONObject obj=dataArr.getJSONObject(0);
                        //  String str=obj.getString("title");

                        //  tv_title.setText(Html.fromHtml(str));
                        for (int i =0 ;i <dataArr.length();i++) {
                            BidHistoryObjects matkasObjects = new BidHistoryObjects();
                            JSONObject jsonObject = dataArr.getJSONObject(i);
                            matkasObjects.setId(jsonObject.getString("id"));
                            matkasObjects.setUser_id(jsonObject.getString("user_id"));
                            matkasObjects.setMatka_id(jsonObject.getString("matka_id"));
                            matkasObjects.setBet_type(jsonObject.getString("bet_type"));
                            matkasObjects.setPoints(jsonObject.getString("points"));
                            matkasObjects.setDigits(jsonObject.getString("digits"));
                            matkasObjects.setDate(jsonObject.getString("date"));
                            matkasObjects.setTime(jsonObject.getString("time"));
                            matkasObjects.setName(jsonObject.getString("name"));
                            matkasObjects.setGame_id(jsonObject.getString("game_id"));
                            matkasObjects.setStatus(jsonObject.getString("status"));
                           // matkasObjects.setPlay_for(jsonObject.getString("play_for"));
                           // matkasObjects.setPlay_on(jsonObject.getString("play_on"));
                       //     matkasObjects.setDay(jsonObject.getString("day"));
                            list.add(matkasObjects);
Log.e("nffc", String.valueOf(list.size()));

                           // list.add(model);
                            //  list.add()
                        }

                        if (list.size()>0){
                            BidHistoryAdapter bidHistoryAdapter= new BidHistoryAdapter(getActivity(),list);
                            rv_hitry.setAdapter(bidHistoryAdapter);
                            tv_no_data.setVisibility(View.GONE);
                        }
                        else {
                            tv_no_data.setVisibility(View.VISIBLE);
                        }

                        //tv_title.setText(title);
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                common.errorMessageDialog("Something Went Wrong");
                loadingBar.dismiss();
            }
        });
        AppController.getInstance().addToRequestQueue(customJsonRequest,json_request_tag);
    }


//    private void getBidData(final String user_id, final String matka_id) {
//
//        final String json_request_tag="json_bid_history_tag";
//        HashMap<String,String> params=new HashMap<String, String>();
//        params.put("us_id",user_id);
//        params.put("matka_id",matka_id);
//
//        loadingBar.show();
//
//        list.clear();
//
//        CustomVolleyJsonArrayRequest customVolleyJsonArrayRequest=new CustomVolleyJsonArrayRequest(Request.Method.POST,URL_BID_HISTORY, params, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//
//                try
//                {
//                    JSONArray jsonArray=response;
//                    String h=jsonArray.getString(0);
//                    if(h.equals("null") || h.equals(null))
//                    {
//                        loadingBar.dismiss();
//                        common.errorMessageDialog("No history for this Matka");
//                    }
//                    else
//                    {
//
//                        for(int i=0; i<=jsonArray.length()-1;i++) {
//
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                            BidHistoryObjects matkasObjects = new BidHistoryObjects();
//                            matkasObjects.setId(jsonObject.getString("id"));
//                            matkasObjects.setUser_id(jsonObject.getString("user_id"));
//                            matkasObjects.setMatka_id(jsonObject.getString("matka_id"));
//                            matkasObjects.setBet_type(jsonObject.getString("bet_type"));
//                            matkasObjects.setPoints(jsonObject.getString("points"));
//                            matkasObjects.setDigits(jsonObject.getString("digits"));
//                            matkasObjects.setDate(jsonObject.getString("date"));
//                            matkasObjects.setTime(jsonObject.getString("time"));
//                            matkasObjects.setName(jsonObject.getString("name"));
//                            matkasObjects.setGame_id(jsonObject.getString("game_id"));
//                            matkasObjects.setStatus(jsonObject.getString("status"));
//                           // matkasObjects.setPlay_for(jsonObject.getString("play_for"));
//                          //  matkasObjects.setPlay_on(jsonObject.getString("play_on"));
//                          //  matkasObjects.setDay(jsonObject.getString("day"));
//                            list.add(matkasObjects);
//
//
//                        }
////                        bidHistoryAdapter.notifyDataSetChanged();
//                        loadingBar.dismiss();
//
//
//                    }
//                    //loadingBar.dismiss();
//
//                }
//                catch (Exception ex)
//                {
//                    loadingBar.dismiss();
//                    Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_LONG).show();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                common.errorMessageDialog("Something Went Wrong");
//
//                //  Toast.makeText(BidActivity.this,"Error"+error.toString(),Toast.LENGTH_LONG).show();
////                        Log.e("Volley",error.toString());
//                loadingBar.dismiss();
//            }
//        });
//
//        AppController.getInstance().addToRequestQueue(customVolleyJsonArrayRequest,json_request_tag);
//
//
//
//
//    }
}