package in.games.dsgames.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import in.games.dsgames.Adapter.SelectBidAdpater;
import in.games.dsgames.Config.Common;
import in.games.dsgames.Config.Module;
import in.games.dsgames.Interface.OnAllGamesListener;
import in.games.dsgames.Model.GameModel;
import in.games.dsgames.Model.GetGamesModel;
import in.games.dsgames.Model.MatkasObjects;
import in.games.dsgames.Model.StarlineModel;
import in.games.dsgames.OnGetMatka;
import in.games.dsgames.OnGetStarline;
import in.games.dsgames.R;
import in.games.dsgames.utils.LoadingBar;
import in.games.dsgames.utils.Session_management;

import static in.games.dsgames.Config.BaseUrl.URL_GET_GAMES;
import static in.games.dsgames.Config.Constants.KEY_ID;

public class SeelctGameActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView iv_back ,iv_reload;
    TextView tv_title,tv_wallet,tv_matka_name ,tv_number ,tv_days ,tv_s_time,tv_e_time;
    LinearLayout lin_single , lin_choice,lin_sp,lin_dp,lin_s_patti,lin_time , lin_d_patti,lin_t_patti,lin_jodi,lin_red,lin_jodi_digit;
    RecyclerView rv_bid;
    ArrayList<GameModel>gamelist;
    LoadingBar loadingBar ;
    Module module ;
    Common common ;
    Activity ctx = SeelctGameActivity.this;
    SelectBidAdpater selectBidAdpater ;
    String game_id="";
    Session_management session_management ;
    CardView card_digit,card_motor,card_jodi,card_patti;
    String matkaId="",mName,start_time,end_time,start_num,end_num,num,is_ds="";
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seelct_game);
        initViews();

    }
    void initViews()
    {session_management = new Session_management(ctx);
        common = new Common(ctx);
        module=new Module(ctx);
        matkaId=getIntent().getStringExtra("m_id");
        is_ds = getIntent().getStringExtra("is_ds");
        loadingBar = new LoadingBar(ctx);
        iv_back = findViewById(R.id.iv_back);
        iv_reload = findViewById(R.id.iv_reload);
        tv_title = findViewById(R.id.tv_Title);
        tv_wallet = findViewById(R.id.tv_wallet);
        tv_matka_name = findViewById(R.id.tv_matka_name);
        tv_number = findViewById(R.id.tv_number);
        tv_days = findViewById(R.id.tv_days);
        tv_s_time = findViewById(R.id.matka_open_bid_Time);
        tv_e_time= findViewById(R.id.matka_close_bid_Time);
        lin_single= findViewById(R.id.lin_single_digit);
        lin_choice= findViewById(R.id.lin_choice);
        lin_s_patti= findViewById(R.id.lin_single_patti);
        lin_d_patti= findViewById(R.id.lin_double_patti);
        lin_t_patti= findViewById(R.id.lin_triple_patti);
        lin_jodi= findViewById(R.id.lin_jodi);
        lin_red= findViewById(R.id.lin_red_bracket);
        lin_jodi_digit= findViewById(R.id.lin_jodi_digit);
        lin_sp= findViewById(R.id.lin_sp);
        lin_dp= findViewById(R.id.lin_dp);
        lin_time= findViewById(R.id.lin_time);
        card_digit= findViewById(R.id.card_digit);
        card_jodi= findViewById(R.id.card_jodi);
        card_motor= findViewById(R.id.card_motor);
        card_patti= findViewById(R.id.card_patti);
        lin_jodi_digit.setOnClickListener(this);
        lin_single.setOnClickListener(this);
        lin_choice.setOnClickListener(this);
        lin_s_patti.setOnClickListener(this);
        lin_d_patti.setOnClickListener(this);
        lin_t_patti.setOnClickListener(this);
        lin_jodi.setOnClickListener(this);
        lin_red.setOnClickListener(this);
        lin_sp.setOnClickListener(this);
        lin_dp.setOnClickListener(this);
        game_id = getIntent().getStringExtra("m_id");

        //getGames(game_id);
//        flag = getGames(game_id);
//        Log.e("sdcfvgbh", String.valueOf(flag));
//        if (!flag)
//        {
//            if (game_id.equals("2"))
//            {
//                card_digit.setVisibility(View.VISIBLE);
//            }
//            else {
//                card_digit.setVisibility(View.GONE);
//            }
//
//        }

        module.getAllGames(new OnAllGamesListener () {
            @Override
            public void onMatkaGames(ArrayList<GetGamesModel> matkaGameList) {
                ArrayList<GameModel> tempList = new ArrayList<> (  );
                Log.e ("matka visible", "onMatkaGames: "+matkaGameList.size () );

                for (int j = 0 ; j <matkaGameList.size ();j++) {

                    tempList.add (new GameModel (matkaGameList.get (j).getGame_id (),matkaGameList.get (j).getName (),R.drawable.logo,"",""));

                }



                for (int i = 0 ; i <tempList.size ();i++)
                {
                    switch (tempList.get (i).getName ().toLowerCase ())
                    {
                        case "single digit":
                            card_digit.setVisibility(View.VISIBLE);
                            break;

                        case "jodi digit":
                            card_jodi.setVisibility(View.VISIBLE);
                            lin_jodi.setVisibility(View.VISIBLE);

                            break;

                        case "single panna":
                            card_patti.setVisibility(View.VISIBLE);
                            lin_s_patti.setVisibility(View.VISIBLE);

                            break;

                        case "double panna":
                            card_patti.setVisibility(View.VISIBLE);
                            lin_d_patti.setVisibility(View.VISIBLE);

                            break;

                        case "triple panna":
                            card_patti.setVisibility(View.VISIBLE);
                            lin_t_patti.setVisibility(View.VISIBLE);

                            break;

                        case "red bracket":
                            card_jodi.setVisibility(View.VISIBLE);
                            lin_red.setVisibility(View.VISIBLE);

                            break;

                        case "sp motor":
                            card_motor.setVisibility(View.VISIBLE);
                            lin_sp.setVisibility(View.VISIBLE);

                            break;

                        case "dp motor":
                            card_motor.setVisibility(View.VISIBLE);
                            lin_dp.setVisibility(View.VISIBLE);
                            break;


                  }
                }
                if(Integer.parseInt (matkaId)<=20){
                    //  allGame (tempList);
                    getGames (game_id);
                    getMatka ();
                }

            }

            @Override
            public void onStarlineGames (ArrayList <GetGamesModel > starlineGAmeList) {
                ArrayList<GameModel> tempList = new ArrayList<> (  );


                for (int j = 0 ; j <starlineGAmeList.size ();j++) {

                    tempList.add (new GameModel (starlineGAmeList.get (j).getGame_id (),starlineGAmeList.get (j).getName (),R.drawable.logo,"",""));

                }



                for (int i = 0 ; i <tempList.size ();i++)
                {
                    switch (tempList.get (i).getName ().toLowerCase ())
                    {
                        case "single digit":
                            card_digit.setVisibility(View.VISIBLE);
                            break;

                        case "jodi digit":
                            lin_jodi.setVisibility(View.VISIBLE);
                            card_jodi.setVisibility(View.VISIBLE);
                            break;

                        case "single pana":
                            lin_s_patti.setVisibility(View.VISIBLE);
                            card_patti.setVisibility(View.VISIBLE);
                            break;

                        case "double pana":
                            lin_d_patti.setVisibility(View.VISIBLE);
                            card_patti.setVisibility(View.VISIBLE);
                            break;

                        case "triple pana":
                            lin_t_patti.setVisibility(View.VISIBLE);
                            card_patti.setVisibility(View.VISIBLE);
                            break;

                        case "red_bracket":
                            lin_red.setVisibility(View.VISIBLE);
                            card_jodi.setVisibility(View.VISIBLE);
                            break;

                        case "sp_motor":
                            lin_sp.setVisibility(View.VISIBLE);
                            card_motor.setVisibility(View.VISIBLE);
                            break;

                        case "dp_motor":
                            lin_dp.setVisibility(View.VISIBLE);
                            card_motor.setVisibility(View.VISIBLE);
                            break;

                    }
                }

                if(Integer.parseInt (matkaId)>20){
                    //allGame (tempList);
                    getGames (game_id);
                    getStartline ();
                }

            }

        });
//        if (Integer.parseInt(getIntent().getStringExtra("m_id"))<20)
//        {
//
//            getMatka();
//
//        }
//        else {
//            getStartline();
//        }


//        rv_bid = findViewById(R.id.rv_bids);
//        gamelist = new ArrayList<>();
//        loadingBar = new LoadingBar(ctx);
//        common = new Common(ctx);
//        rv_bid.setLayoutManager(new LinearLayoutManager(ctx));
//
//        gamelist.add(new GameModel("1","Single Digit",R.drawable.patti,"0",""));
//        gamelist.add(new GameModel("2","Choice Pana",R.drawable.patti,"0",""));
//        gamelist.add(new GameModel("3","Jodi",R.drawable.patti,"0","Jodi"));
//       gamelist.add(new GameModel("4","Red Bracket",R.drawable.patti,"1","Jodi"));
//       gamelist.add(new GameModel("6","Group Jodi",R.drawable.patti,"1","Jodi"));
//        gamelist.add(new GameModel("7","Single Patti",R.drawable.patti,"2","Patti"));
//        gamelist.add(new GameModel("8","Double Patti",R.drawable.patti,"2","Patti"));
//        gamelist.add(new GameModel("9","Triple Patti",R.drawable.patti,"0","Patti"));
//       gamelist.add(new GameModel("10","SP Motor",R.drawable.patti,"1","Motor"));
//       gamelist.add(new GameModel("11","DP Motor",R.drawable.patti,"1","Motor"));
////        gamelist.add(new GameModel("12","Half Sangam",R.drawable.half_sangam_digit,"1"));
////        gamelist.add(new GameModel("13","Full Sangam",R.drawable.full_sangam_digits,"1"));
////       gamelist.add(new GameModel("14","Cycle Pana",R.drawable.cyclepana,"1"));
//        selectBidAdpater = new SelectBidAdpater(ctx,gamelist, getIntent().getStringExtra("m_id"),
//                getIntent().getStringExtra("matka_name"),
//                getIntent().getStringExtra("start_time"),
//                getIntent().getStringExtra("end_time"), getIntent().getStringExtra("start_num"),
//                getIntent().getStringExtra("num"),
//                getIntent().getStringExtra("end_num"));
//        rv_bid.setAdapter(selectBidAdpater);

        if (Integer.parseInt(getIntent().getStringExtra("m_id"))>20)
        {
            lin_time.setVisibility(View.GONE);
            card_motor.setVisibility(View.GONE);
            card_jodi.setVisibility(View.GONE);
        }

        common.setWallet_Amount(tv_wallet,loadingBar,session_management.getUserDetails().get(KEY_ID));
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {


        String game ="" ,game_id="";
        switch (view.getId())
        {
            case R.id.lin_single_digit:
                game ="single digit";
                game_id ="2";
                break;
            case R.id.lin_choice:
                game ="choice";
                game_id ="16";
                break;
            case R.id.lin_jodi:
                game ="jodi";
                game_id ="3";
                break;
            case R.id.lin_jodi_digit:
                game ="jodi digit";
                game_id ="18";
                break;
            case R.id.lin_single_patti:
                game ="single patti";
                game_id ="7";
                break;
            case R.id.lin_double_patti:
                game ="double patti";
                game_id ="8";
                break;
            case R.id.lin_triple_patti:
                game ="triple patti";
                game_id ="9";
                break;
            case R.id.lin_sp:
                game ="sp motor";
                game_id ="10";
                break;
            case R.id.lin_dp:
                game ="dp motor";
                game_id ="11";
                break;
            case R.id.lin_red_bracket:
                game ="red bracket";
                game_id ="4";
                break;
            case R.id.iv_back:
                goToMain();
                break;

        }
        if (!(game.isEmpty()||game.equals(""))) {
            Intent intent = new Intent(ctx, PlayBidActivity.class);
            intent.putExtra("game", game);
            intent.putExtra("game_id", game_id);
            intent.putExtra("m_id", getIntent().getStringExtra("m_id"));
            if (Integer.parseInt(getIntent().getStringExtra("m_id"))>20)
            {
                intent.putExtra("matka_name", "starline");
            }
            intent.putExtra("matka_name", mName);
            intent.putExtra("start_time",start_time);
            intent.putExtra("end_time", end_time);
            intent.putExtra("start_num", start_num);
            intent.putExtra("num", num);
            intent.putExtra("end_num", end_num);

            startActivity(intent);
        }



    }

    @Override
    public void onBackPressed() {
        goToMain();
    }
    public void goToMain(){
        Intent intent=new Intent(ctx, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public void getMatka()
    {
        common.getSingleMatka(matkaId, new OnGetMatka() {
            @Override
            public void onGetMatka(MatkasObjects model) {
                String dt=new SimpleDateFormat("EEEE").format(new Date());
                if(dt.equalsIgnoreCase("Sunday")){
                    if(common.getValidTime(model.getStart_time().toString(), model.getStart_time().toString())){
                        start_time=model.getStart_time();
                        end_time=model.getEnd_time();
                    }else{
                        start_time=model.getBid_start_time();
                        end_time=model.getBid_end_time();
                    }

                }else if(dt.equalsIgnoreCase("Saturday")){
                    if(common.getValidTime(model.getSat_start_time().toString(), model.getSat_end_time().toString())){
                        start_time=model.getSat_start_time();
                        end_time=model.getSat_end_time();
                    }else{
                        start_time=model.getBid_start_time();
                        end_time=model.getBid_end_time();
                    }
                }else{
                    start_time=model.getBid_start_time();
                    end_time=model.getBid_end_time();
                }

                mName=model.getName().toString();
                start_num=model.getStarting_num();
                end_num=model.getEnd_num();
                num=model.getNumber();

                tv_matka_name.setText(mName);
                tv_s_time.setText("Opens at :"+common.get24To12Format(start_time));
                tv_e_time.setText("Closes at :"+common.get24To12Format(end_time));
                tv_number.setText(common.checkNullNumber(start_num)+" - "+common.checkNullNumber(num)+" - "+common.checkNullNumber(end_num));
            }
        });
    }

    public void getStartline()
    {
        common.getSingleStarline(matkaId, new OnGetStarline() {
            @Override
            public void OnGetStarline(StarlineModel model)
            {
                String dt=new SimpleDateFormat("EEEE").format(new Date());
                if(dt.equalsIgnoreCase("Sunday")){
                    if(common.getValidTime(model.getS_game_time().toString(), model.getS_game_time().toString())){
//                            common.showToast("if");
                        start_time=model.getS_game_time();
                        end_time=model.getS_game_end_time();
                    }else{
//                            common.showToast("else");
//                            start_time=model.getBid_start_time();
//                            end_time=model.getBid_end_time();
                    }

                }else if(dt.equalsIgnoreCase("Saturday")){
                    if(common.getValidTime(model.getS_game_time().toString(), model.getS_game_end_time().toString())){
                        start_time=model.getS_game_time();
                        end_time=model.getS_game_end_time();
                    }else{
//                            start_time=model.getBid_start_time();
//                            end_time=model.getBid_end_time();
                    }
                }
                else{
                    start_time=model.getS_game_time();
                    end_time=model.getS_game_end_time();
                }

//                    mName=model.getName();
//                    start_num=model.getS_game_number();
//                    end_num=model.getendnumber();
                num=model.getS_game_number();

                tv_matka_name.setText("Starline");
                tv_s_time.setText("Opens at :"+common.get24To12Format(start_time));
                tv_e_time.setText("Closes at :"+common.get24To12Format(end_time));
                tv_number.setText(""+" - "+common.checkNullNumber(num)+" - "+"");
            }

        });
    }

    public void getGames(final String game_id)
    {    loadingBar.show();
        HashMap<String,String> params = new HashMap<>();
        common.postRequest(URL_GET_GAMES, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadingBar.dismiss();
                Log.e("getGames",response.toString());
                ArrayList<GetGamesModel> gList= new ArrayList<>();
                try {
                    JSONArray arr=new JSONArray(response);
                    for(int i=0; i<arr.length();i++){
                        JSONObject obj=arr.getJSONObject(i);
                        GetGamesModel model=new GetGamesModel();
                        model.setGame_id(common.checkNullString(obj.getString("game_id")));
                        model.setGame_name(common.checkNullString(obj.getString("game_name")));
                        model.setName(common.checkNullString(obj.getString("name")));
                        model.setPoints(common.checkNullString(obj.getString("points")));
                        model.setStarline_points(common.checkNullString(obj.getString("starline_points")));
                        model.setIs_starline(common.checkNullString(obj.getString("is_starline")));
                        model.setIs_close(common.checkNullString(obj.getString("is_close")));
                        model.setIs_deleted(common.checkNullString(obj.getString("is_deleted")));
                        model.setIs_disabled(common.checkNullString(obj.getString("is_disabled")));
                        model.setIs_ds_disabled(common.checkNullString(obj.getString("is_ds_disabled")));
                        model.setIs_starline_disable(common.checkNullString(obj.getString("is_starline_disable")));
                        gList.add(model);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            Log.e("game_id",gList.get(0).getGame_id());
            Log.e("is_ds",is_ds);

//                if (is_ds.equals("1"))
//                {
////                    Intent intent = new Intent(SeelctGameActivity.this,GameActivity.class);
////                    intent.putExtra("matka_name",gList.get(0).getGame_name().toString());
////                    startActivity(intent);
//                    for (int i=0;i<gList.size();i++)
//                    {
//
//                        if (Integer.parseInt(game_id)<20)
//                        {
//                            if (gList.get(i).getName().equalsIgnoreCase("SINGLE DIGIT"))
//                            {
//                                if (gList.get(i).getIs_ds_disabled().equals("0"))
//                                {
//                                    card_digit.setVisibility(View.VISIBLE);
//                                }
//
//                            }else  if (gList.get(i).getName().equalsIgnoreCase("RED BRACKET"))
//                            {
//                                if (gList.get(i).getIs_ds_disabled().equals("0"))
//                                {
//                                    lin_red.setVisibility(View.VISIBLE);
//                                    card_jodi.setVisibility(View.VISIBLE);
//                                }
//
//                            }else  if (gList.get(i).getName().equalsIgnoreCase("JODI DIGIT"))
//                            {
//                                if (gList.get(i).getIs_ds_disabled().equals("0"))
//                                {
//                                    lin_jodi.setVisibility(View.VISIBLE);
//                                    card_jodi.setVisibility(View.VISIBLE);
//                                }
//
//                            }
//                            else  if (gList.get(i).getName().equalsIgnoreCase("SINGLE PANNA"))
//                            {
//                                if (gList.get(i).getIs_ds_disabled().equals("0"))
//                                {
//                                    lin_s_patti.setVisibility(View.VISIBLE);
//                                    card_patti.setVisibility(View.VISIBLE);
//                                }
//
//                            }else  if (gList.get(i).getName().equals("DOUBLE PANNA"))
//                            {
//                                if (gList.get(i).getIs_ds_disabled().equals("0"))
//                                {
//                                    lin_d_patti.setVisibility(View.VISIBLE);
//                                    card_patti.setVisibility(View.VISIBLE);
//                                }
//
//                            }
//                            else  if (gList.get(i).getName().equals("TRIPLE PANNA"))
//                            {
//                                if (gList.get(i).getIs_ds_disabled().equals("0"))
//                                {
//                                    lin_t_patti.setVisibility(View.VISIBLE);
//                                    card_patti.setVisibility(View.VISIBLE);
//                                }
//
//                            }else  if (gList.get(i).getName().equalsIgnoreCase("SP MOTOR"))
//                            {
//                                if (gList.get(i).getIs_ds_disabled().equals("0"))
//                                {
//                                    lin_sp.setVisibility(View.VISIBLE);
//                                    card_motor.setVisibility(View.VISIBLE);
//                                }
//
//                            }else  if (gList.get(i).getName().equalsIgnoreCase("DP MOTOR"))
//                            {
//                                if (gList.get(i).getIs_ds_disabled().equals("0"))
//                                {
//                                    lin_dp.setVisibility(View.VISIBLE);
//                                    card_motor.setVisibility(View.VISIBLE);
//                                }
//
//                            }
//                        }else if (Integer.parseInt(game_id)>20)
//                        {
//                            if (gList.get(i).getName().equalsIgnoreCase("SINGLE DIGIT"))
//                            {
//                                if (gList.get(i).getIs_starline_disable().equals("0"))
//                                {
//                                    card_digit.setVisibility(View.VISIBLE);
//                                }
//
//                            }else  if (gList.get(i).getName().equalsIgnoreCase("RED BRACKET"))
//                            {
//                                if (gList.get(i).getIs_starline_disable().equals("0"))
//                                {
//                                    lin_red.setVisibility(View.VISIBLE);
//                                    card_jodi.setVisibility(View.VISIBLE);
//                                }
//
//                            }else  if (gList.get(i).getName().equalsIgnoreCase("JODI DIGIT"))
//                            {
//                                if (gList.get(i).getIs_starline_disable().equals("0"))
//                                {
//                                    lin_jodi.setVisibility(View.VISIBLE);
//                                    card_jodi.setVisibility(View.VISIBLE);
//                                }
//
//                            }
//                            else  if (gList.get(i).getName().equalsIgnoreCase("SINGLE PANNA"))
//                            {
//                                if (gList.get(i).getIs_starline_disable().equals("0"))
//                                {
//                                    lin_s_patti.setVisibility(View.VISIBLE);
//                                    card_patti.setVisibility(View.VISIBLE);
//                                }
//
//                            }else  if (gList.get(i).getName().equals("DOUBLE PANNA"))
//                            {
//                                if (gList.get(i).getIs_starline_disable().equals("0"))
//                                {
//                                    lin_d_patti.setVisibility(View.VISIBLE);
//                                    card_patti.setVisibility(View.VISIBLE);
//                                }
//
//                            }
//                            else  if (gList.get(i).getName().equals("TRIPLE PANNA"))
//                            {
//                                if (gList.get(i).getIs_starline_disable().equals("0"))
//                                {
//                                    lin_t_patti.setVisibility(View.VISIBLE);
//                                    card_patti.setVisibility(View.VISIBLE);
//                                }
//
//                            }else  if (gList.get(i).getName().equalsIgnoreCase("SP MOTOR"))
//                            {
//                                if (gList.get(i).getIs_starline_disable().equals("0"))
//                                {
//                                    lin_sp.setVisibility(View.VISIBLE);
//                                    card_motor.setVisibility(View.VISIBLE);
//                                }
//
//                            }else  if (gList.get(i).getName().equalsIgnoreCase("DP MOTOR"))
//                            {
//                                if (gList.get(i).getIs_starline_disable().equals("0"))
//                                {
//                                    lin_dp.setVisibility(View.VISIBLE);
//                                    card_motor.setVisibility(View.VISIBLE);
//                                }
//
//                            }
//                        }
//                    }
//
//                }else {
                if (is_ds.equals("0")){
                    for (int i=0;i<gList.size();i++)
                    {

                        if (Integer.parseInt(game_id)<20)
                        {
                            if (gList.get(i).getName().equalsIgnoreCase("SINGLE DIGIT"))
                            {
                                if (gList.get(i).getIs_disabled().equals("0"))
                                {
//                        flag = false;
//                        gameTypes();
                                    card_digit.setVisibility(View.VISIBLE);
                                }

                            }else  if (gList.get(i).getName().equalsIgnoreCase("RED BRACKET"))
                            {
                                if (gList.get(i).getIs_disabled().equals("0"))
                                {
                                    lin_red.setVisibility(View.VISIBLE);
                                    card_jodi.setVisibility(View.VISIBLE);
                                }

                            }else  if (gList.get(i).getName().equalsIgnoreCase("JODI DIGIT"))
                            {
                                if (gList.get(i).getIs_disabled().equals("0"))
                                {
                                    lin_jodi.setVisibility(View.VISIBLE);
                                    card_jodi.setVisibility(View.VISIBLE);
                                }

                            }
                            else  if (gList.get(i).getName().equalsIgnoreCase("SINGLE PANNA"))
                            {
                                if (gList.get(i).getIs_disabled().equals("0"))
                                {
                                    lin_s_patti.setVisibility(View.VISIBLE);
                                    card_patti.setVisibility(View.VISIBLE);
                                }

                            }else  if (gList.get(i).getName().equals("DOUBLE PANNA"))
                            {
                                if (gList.get(i).getIs_disabled().equals("0"))
                                {
                                    lin_d_patti.setVisibility(View.VISIBLE);
                                    card_patti.setVisibility(View.VISIBLE);
                                }

                            }
                            else  if (gList.get(i).getName().equals("TRIPLE PANNA"))
                            {
                                if (gList.get(i).getIs_disabled().equals("0"))
                                {
                                    lin_t_patti.setVisibility(View.VISIBLE);
                                    card_patti.setVisibility(View.VISIBLE);
                                }

                            }else  if (gList.get(i).getName().equalsIgnoreCase("SP MOTOR"))
                            {
                                if (gList.get(i).getIs_disabled().equals("0"))
                                {
                                    lin_sp.setVisibility(View.VISIBLE);
                                    card_motor.setVisibility(View.VISIBLE);
                                }

                            }else  if (gList.get(i).getName().equalsIgnoreCase("DP MOTOR"))
                            {
                                if (gList.get(i).getIs_disabled().equals("0"))
                                {
                                    lin_dp.setVisibility(View.VISIBLE);
                                    card_motor.setVisibility(View.VISIBLE);
                                }

                            }
                        }else if (Integer.parseInt(game_id)>20)
                        {
                            if (gList.get(i).getName().equalsIgnoreCase("SINGLE DIGIT"))
                            {
                                if (gList.get(i).getIs_starline_disable().equals("0"))
                                {
                                    card_digit.setVisibility(View.VISIBLE);
                                }

                            }else  if (gList.get(i).getName().equalsIgnoreCase("RED BRACKET"))
                            {
                                if (gList.get(i).getIs_starline_disable().equals("0"))
                                {
                                    lin_red.setVisibility(View.VISIBLE);
                                    card_jodi.setVisibility(View.VISIBLE);
                                }

                            }else  if (gList.get(i).getName().equalsIgnoreCase("JODI DIGIT"))
                            {
                                if (gList.get(i).getIs_starline_disable().equals("0"))
                                {
                                    lin_jodi.setVisibility(View.VISIBLE);
                                    card_jodi.setVisibility(View.VISIBLE);
                                }

                            }
                            else  if (gList.get(i).getName().equalsIgnoreCase("SINGLE PANNA"))
                            {
                                if (gList.get(i).getIs_starline_disable().equals("0"))
                                {
                                    lin_s_patti.setVisibility(View.VISIBLE);
                                    card_patti.setVisibility(View.VISIBLE);
                                }

                            }else  if (gList.get(i).getName().equals("DOUBLE PANNA"))
                            {
                                if (gList.get(i).getIs_starline_disable().equals("0"))
                                {
                                    lin_d_patti.setVisibility(View.VISIBLE);
                                    card_patti.setVisibility(View.VISIBLE);
                                }

                            }
                            else  if (gList.get(i).getName().equals("TRIPLE PANNA"))
                            {
                                if (gList.get(i).getIs_starline_disable().equals("0"))
                                {
                                    lin_t_patti.setVisibility(View.VISIBLE);
                                    card_patti.setVisibility(View.VISIBLE);
                                }

                            }else  if (gList.get(i).getName().equalsIgnoreCase("SP MOTOR"))
                            {
                                if (gList.get(i).getIs_starline_disable().equals("0"))
                                {
                                    lin_sp.setVisibility(View.VISIBLE);
                                    card_motor.setVisibility(View.VISIBLE);
                                }

                            }else  if (gList.get(i).getName().equalsIgnoreCase("DP MOTOR"))
                            {
                                if (gList.get(i).getIs_starline_disable().equals("0"))
                                {
                                    lin_dp.setVisibility(View.VISIBLE);
                                    card_motor.setVisibility(View.VISIBLE);
                                }

                            }
                        }
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                common.showVolleyError(error);

            }
        });
//    Log.e("dereftrgyth", String.valueOf(flag));
    }
 /*   public boolean gameTypes()
    {
        boolean flag = true;
        if

       return
    }*/
}