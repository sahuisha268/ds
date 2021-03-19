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

public class WinFragment extends Fragment {
    LoadingBar loadingBar ;
    Module module ;
    Session_management session_management ;
    RecyclerView rv_hitry;
    ArrayList<BidHistoryObjects> list ,bid_list;
    Common common;
    TextView tv_no_data;
    BidHistoryAdapter bidHistoryAdapter;
    String type,url;

    public WinFragment(String type) {
        this.type = type;
    }

    public WinFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View v =inflater.inflate(R.layout.fragment_win, container, false);
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
        rv_hitry.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        bid_list = new ArrayList<>();

        rv_hitry = v.findViewById(R.id.rv_bid_histry);
        rv_hitry.setHasFixedSize(true);
        rv_hitry.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_hitry.setAdapter(new BidHistoryAdapter(getActivity(),initData()));
        //  textView=findViewById(R.id.tv_notice_board);
        String user_id = session_management.getUserDetails().get(KEY_ID);
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
//        list.add(new BidHistoryObjects());
        // list.add(new BidHistoryObjects("WITHDRAW","JHGF NJHGF HGF JHGHYTF DCVJK JNBV"));
        // list.add(new BidHistoryObjects("notice", "hgvf hgft gfg ihjn xfcvg vygu vygu"));
        return list;
    }

    private void getBidData(String user_id , String url) {
        final String json_request_tag="json_bid_history_tag";
        final HashMap<String,String> params = new HashMap<>();
        params.put("user_id",user_id);
        loadingBar.show();
        list.clear();

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("respose win bid",response.toString()+"/n"+URL_HISTORY+"/n"+params.toString());
                loadingBar.dismiss();
                try {
                    boolean stat = response.getBoolean("responce");
                    if(stat){
                        JSONArray dataArr=response.getJSONArray("data");

                        for (int i =0 ;i <dataArr.length();i++) {

                            BidHistoryObjects matkasObjects = new BidHistoryObjects();
                            JSONObject jsonObject = dataArr.getJSONObject(i);
                            String status = jsonObject.getString("status");
                            Log.e("win_stat",status);

                            if(status.equals("win")){
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

                                list.add(matkasObjects);
                            }

                            Log.e("win_bid_histry",""+list.size());
                        }

                        if (list.size()>0){
                            BidHistoryAdapter bidHistoryAdapter= new BidHistoryAdapter(getActivity(),list);
                            rv_hitry.setAdapter(bidHistoryAdapter);
                            tv_no_data.setVisibility(View.GONE);
                        }
                        else {
                            tv_no_data.setVisibility(View.VISIBLE);
                        }

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

}