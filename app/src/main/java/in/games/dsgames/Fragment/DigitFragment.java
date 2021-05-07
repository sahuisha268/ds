package in.games.dsgames.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.games.dsgames.Activity.PlayBidActivity;
import in.games.dsgames.Adapter.PointsAdapter;
import in.games.dsgames.Config.Common;
import in.games.dsgames.Config.InputData;
import in.games.dsgames.Config.Module;
import in.games.dsgames.Model.TableModel;
import in.games.dsgames.R;
import in.games.dsgames.utils.LoadingBar;

import static in.games.dsgames.Activity.SplashActivity.min_bid_amount;
import static in.games.dsgames.Adapter.PointsAdapter.is_error;

public class DigitFragment extends Fragment implements View.OnClickListener {
    String game;
    RecyclerView rv_select;
    RadioButton rd_open ,rd_close;
    TextView tv_note,tv_date;
    Button btn_name;
    Button btn_sbmit ,btn_reset ;
    LoadingBar loadingBar ;
    Module module ;
    Common common;
    public static ArrayList<TableModel> patti_list,tempList;
    public static String g_type ="" ;
    public static TextView tv_total ;
    PointsAdapter pointsAdapter;
    List<String> digit_list ;
    String points="",total="" ,m_id="",matka_name="",game_id="",start_time="",end_time="";

    public DigitFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v = inflater.inflate(R.layout.fragment_digit, container, false);
       initViews(v);
       return v;
    }
    void initViews(View v)
    {
        module = new Module(getActivity());
        common = new Common(getActivity());
//        module.showToast("DigitFragment");
        loadingBar = new LoadingBar(getActivity());
        rv_select = v.findViewById(R.id.rv_digits);
        tv_date = v.findViewById(R.id.tv_date);
        tv_note = v.findViewById(R.id.tv_note);
        btn_name = v.findViewById(R.id.btn_name);
        rd_close = v.findViewById(R.id.rd_close);
        rd_open = v.findViewById(R.id.rd_open);
        btn_reset = v.findViewById(R.id.reset);
        btn_sbmit = v.findViewById(R.id.submit);
        tv_total = v.findViewById(R.id.tv_total);
        btn_sbmit.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
        game = getActivity().getIntent().getStringExtra("game");
        game_id = getActivity().getIntent().getStringExtra("game_id");
        m_id = getActivity().getIntent().getStringExtra("m_id");
        matka_name = getActivity().getIntent().getStringExtra("matka_name");
        start_time = getActivity(). getIntent().getStringExtra("start_time");
        end_time = getActivity().getIntent().getStringExtra("end_time");
        btn_name.setText(game);
        patti_list = new ArrayList<>();
        tempList = new ArrayList<>();
        rd_open.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    g_type="open";
                    rd_close.setChecked(false);
                    module.showToast(g_type);
                }
            }
        });  rd_close.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b)
            {
                g_type="close";
                module.showToast(g_type);
                rd_open.setChecked(false);
            }
        }
    });
        switch (game) {
            case "single digit":
                pointsAdapter = new PointsAdapter(Arrays.asList(InputData.single_digit),getActivity());
                pointsAdapter.notifyDataSetChanged();
                break;
            case "triple patti":
                pointsAdapter = new PointsAdapter(Arrays.asList(InputData.triple_patti),getActivity());
                pointsAdapter.notifyDataSetChanged();
                break;
        }
        rv_select.setLayoutManager(new GridLayoutManager(getActivity(),2));
        rv_select.setAdapter(pointsAdapter);
        common.setGameDate(tv_date,end_time);
        Log.e("frag_name","Digit Fragment");
    }

    @Override
    public void onClick(View view) {
//        module.showToast("viiii");
          if (view.getId() == R.id.submit) 
          {   int gid= Integer.parseInt (m_id);

              if(gid>20){
                  if(game.equals ("single digit")){
                      g_type="open";
                  }

              }
//              module.showToast("submit_click");
            tempList.clear();
            for(int k=0; k<patti_list.size();k++)
            {
                if(patti_list.get(k).getPoints().toString().equals("0") || patti_list.get(k).getPoints().toString().equals(""))
                { }
                else
                {

                    tempList.add(patti_list.get(k));

                }
            }
            for(TableModel model:tempList){
                Log.e("temp_data",""+model.getDigits()+" - "+model.getPoints());
            }
            if (tempList.size()<=0) {
                Toast.makeText(getActivity(), "Please enter some points", Toast.LENGTH_LONG).show();
            } else {
                if (is_error) {
                    Toast.makeText(getActivity(), "Minimum bid amount is "+min_bid_amount, Toast.LENGTH_LONG).show();
                } else {
                    String c = tv_date.getText().toString();
                    String w = PlayBidActivity.tv_wallet.getText().toString().trim();


//                    long bidTime=common.getTimeDifference(start_time);
//                    if(bidTime>0)
//                    {
                        common.insertData(tempList, m_id, c, game_id, w, matka_name,loadingBar,btn_sbmit, start_time, end_time);


//                    list.clear();
                }
            }

        }
          else if (view.getId() == R.id.reset)
          {

              resetBids();
          }
        
    }

    private void resetBids() {
        patti_list.clear();
        tempList.clear();
        tv_total.setText("0.0");
        pointsAdapter.notifyDataSetChanged();
    }
}