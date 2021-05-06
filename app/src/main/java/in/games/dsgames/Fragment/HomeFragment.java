package in.games.dsgames.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.daimajia.slider.library.SliderLayout;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.games.dsgames.Activity.ContactUsActivity;
import in.games.dsgames.Activity.SplashActivity;
import in.games.dsgames.Activity.StarlineActivity;
import in.games.dsgames.Activity.WalletHistoryActivity;
import in.games.dsgames.Adapter.HomeCasinoAdapter;
import in.games.dsgames.Adapter.HomeMatkaAdapter;
import in.games.dsgames.AppController;
import in.games.dsgames.Config.Common;
import in.games.dsgames.Config.Module;
import in.games.dsgames.Interface.OnGetConfigData;
import in.games.dsgames.Model.ConfigDataModel;
import in.games.dsgames.Model.HomeCasinoModel;
import in.games.dsgames.Model.MatkasObjects;
import in.games.dsgames.R;
import in.games.dsgames.networkconnectivity.ConnectivityReceiver;
import in.games.dsgames.utils.CustomVolleyJsonArrayRequest;
import in.games.dsgames.utils.LoadingBar;
import in.games.dsgames.utils.Session_management;

import static in.games.dsgames.Activity.SplashActivity.call_no;
import static in.games.dsgames.Config.BaseUrl.URL_INDEX;
import static in.games.dsgames.Config.BaseUrl.URL_Matka;
import static in.games.dsgames.Config.Constants.KEY_ID;
import static in.games.dsgames.Config.Constants.KEY_NAME;


public class HomeFragment extends Fragment implements View.OnClickListener {
    RecyclerView rv_casino_games,rv_matka;
    HomeCasinoAdapter homeCasinoAdapter;
    ArrayList<HomeCasinoModel> casinoModels ,temp_casino_list;
GridLayoutManager casino_grid;
TextView tv_name ,tv_wallet ,tv_casino_one,tv_add_withdraw,tv_home_text,tv_whtsapp;
SliderLayout home_slider;
ImageView home_banner;
    String whts;
LinearLayout lin_chat,lin_feed,lin_whstaap,lin_call,lin_live_result,lin_starline;
CardView card_starline;
LoadingBar loadingBar ;
Module module;
Common common;
    ArrayList<MatkasObjects> matkaList;
    HomeMatkaAdapter newAdapter;
Session_management session_management ;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(root);

        return root;
    }

    void initViews(View v)
    {   tv_name = v.findViewById(R.id.tv_name);
      tv_wallet = v.findViewById(R.id.tv_wallet);
      tv_home_text = v.findViewById(R.id.tv_slider_txt);
      tv_add_withdraw = v.findViewById(R.id.tv_add_withrw);
     lin_call = v.findViewById(R.id.lin_call);
     lin_chat = v.findViewById(R.id.lin_chat);
   card_starline = v.findViewById(R.id.card_starline);
        lin_starline=v.findViewById (R.id.lin_starline);
        lin_starline.setOnClickListener (this);
     lin_feed= v.findViewById(R.id.lin_feedback);
     lin_live_result= v.findViewById(R.id.lin_live_result);
     lin_whstaap = v.findViewById(R.id.lin_whtsaap);
        tv_whtsapp=v.findViewById(R.id.tv_whtsapp);
     home_banner = v.findViewById(R.id.home_banner);
      home_slider = v.findViewById(R.id.home_slider);
        rv_casino_games=v.findViewById(R.id.rv_casino_game);
        rv_matka=v.findViewById(R.id.rv_matka);
       tv_casino_one=v.findViewById(R.id.casino_name);
       loadingBar = new LoadingBar(getActivity());
       module = new Module(getActivity());
       common = new Common(getActivity());
       session_management = new Session_management(getActivity());
        casinoModels = new ArrayList<>();
       matkaList = new ArrayList<>();

        temp_casino_list = new ArrayList<>();
        casinoModels.add(new HomeCasinoModel("1","Play Starline Bazar"));
        casino_grid = new GridLayoutManager(getActivity(),2);
        rv_casino_games.setLayoutManager(casino_grid);
        tv_casino_one.setText(casinoModels.get(0).getName());

        if (ConnectivityReceiver.isConnected())
        {
            getMatkaData();
            getApiData();
        }
        else
        {
            module.noInternetActivity();
        }

     common.cofigData(new OnGetConfigData() {
            @Override
            public void onGetConfigData(ConfigDataModel model) {


                if (model.getIs_starline().equals("1"))
                {
                   card_starline.setVisibility(View.VISIBLE);

                }
          else{
                   card_starline.setVisibility(View.GONE);

                }
            }
        });
        tv_home_text.setText(SplashActivity.home_text);
        tv_name.setText("Welcome,"+session_management.getUserDetails().get(KEY_NAME));
        common.setWallet_Amount(tv_wallet,loadingBar,session_management.getUserDetails().get(KEY_ID));
        lin_whstaap.setOnClickListener(this);
        lin_call.setOnClickListener(this);
        lin_feed.setOnClickListener(this);
        lin_chat.setOnClickListener(this);
        lin_live_result.setOnClickListener(this);
        card_starline.setOnClickListener(this);
        tv_add_withdraw.setOnClickListener(this);

        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Are you sure want to exit?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            //((MainActivity) getActivity()).finish();
                            getActivity().finishAffinity();


                        }
                    })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    final AlertDialog dialog=builder.create();
                    dialog.setOnShowListener( new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                        }
                    });
                    dialog.show();
                    return true;
                }
                return false;
            }
        });

    }

    public void getMatkaData()
    {
        loadingBar.show();

        final JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL_Matka, new
                Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("matka",String.valueOf(response));
                        matkaList.clear();
                        for(int i=0; i<response.length();i++)
                        {
                            try
                            {


                                JSONObject jsonObject=response.getJSONObject(i);
                                MatkasObjects matkasObjects=new MatkasObjects();
                                matkasObjects.setId(jsonObject.getString("id"));
                                matkasObjects.setName(jsonObject.getString("name"));
                                matkasObjects.setStart_time(jsonObject.getString("start_time"));
                                matkasObjects.setEnd_time(jsonObject.getString("end_time"));
                                matkasObjects.setStarting_num(jsonObject.getString("starting_num"));
                                matkasObjects.setNumber(jsonObject.getString("number"));
                                matkasObjects.setEnd_num(jsonObject.getString("end_num"));
                                matkasObjects.setBid_start_time(jsonObject.getString("bid_start_time"));
                                matkasObjects.setBid_end_time(jsonObject.getString("bid_end_time"));
                                matkasObjects.setCreated_at(jsonObject.getString("created_at"));
                                matkasObjects.setUpdated_at(jsonObject.getString("updated_at"));
                                matkasObjects.setSat_start_time(jsonObject.getString("sat_start_time"));
                                matkasObjects.setSat_end_time(jsonObject.getString("sat_end_time"));
                                matkasObjects.setStatus(jsonObject.getString("status"));
                                matkaList.add(matkasObjects);

                            }
                            catch (Exception ex)
                            {
                               loadingBar.dismiss();
                                Toast.makeText(getActivity(),"Error :"+ex.getMessage(),Toast.LENGTH_LONG).show();

                                return;
                            }
                        }

                       rv_matka.setLayoutManager(new LinearLayoutManager(getActivity()));
                        newAdapter=new HomeMatkaAdapter(getActivity(),matkaList);
                       rv_matka.setAdapter(newAdapter);
                        newAdapter.notifyDataSetChanged();

                      loadingBar.dismiss();


                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      loadingBar.dismiss();
                        String msg=module.VolleyErrorMessage(error);
                        if(!msg.isEmpty())
                        {
                            module.showToast(""+msg);
                        }
                    }
                });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);



    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.lin_call:
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: "+call_no));
                getActivity().startActivity(intent);
                break;
                case R.id.lin_chat:
                break;
                case R.id.lin_feedback:
                    startActivity(new Intent(getActivity(), ContactUsActivity.class));
                break;
                case R.id.lin_live_result:
                break;
                case R.id.lin_whtsaap:
                   // contactWhatsapp(whatsapp_no,""+getActivity().getResources().getString(R.string.app_name));
                    common.whatsapp(whts,"Hi!, Admin");
                break;
                case R.id.tv_add_withrw:
                    startActivity(new Intent(getActivity(), WalletHistoryActivity.class));
                break;
            case R.id.lin_starline:
                Intent intent1 = new Intent(getActivity(), StarlineActivity.class);
                intent1.putExtra("title",casinoModels.get(0).getName());
                startActivity(intent1);
        }

    }

//
    private void getApiData() {
        String json_tag="json_splash_request";
        HashMap<String, String> params=new HashMap<String, String>();
        CustomVolleyJsonArrayRequest customVolleyJsonArrayRequest=new CustomVolleyJsonArrayRequest(Request.Method.GET,URL_INDEX, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("getIndexData",""+response.toString());
                try{
                    String msg="";
                    JSONObject dataObj=response.getJSONObject(0);
                    whts= dataObj.getString("home_whatsapp");
                    tv_whtsapp.setText(whts);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                String msg=module.VolleyErrorMessage(error);
                if(!msg.isEmpty()){
                    module.showToast(""+msg);
                }
            }
        });

        AppController.getInstance().addToRequestQueue(customVolleyJsonArrayRequest,json_tag);


    }
}