package in.games.dsgames.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.games.dsgames.Activity.PlayBidActivity;
import in.games.dsgames.AppController;
import in.games.dsgames.Config.Common;
import in.games.dsgames.Config.Module;
import in.games.dsgames.Model.TableModel;
import in.games.dsgames.R;
import in.games.dsgames.utils.CustomJsonRequest;
import in.games.dsgames.utils.LoadingBar;

import static in.games.dsgames.Activity.SplashActivity.min_bid_amount;
import static in.games.dsgames.Config.BaseUrl.URL_DpMotor;
import static in.games.dsgames.Config.BaseUrl.URL_SpMotor;

public class MotorFragment extends Fragment implements View.OnClickListener {

    String game;
    RadioButton rd_open ,rd_close;
    TextView tv_note,tv_date ;
    Button btn_name;
    TextInputLayout lay_amount,lay_digit;
  TextInputEditText et_amount,et_digit;
    Button btn_sbmit ,btn_reset ;
    LoadingBar loadingBar ;
    Module module ;
    Common common;
    ArrayList<TableModel> bet_list;
    String type ="", points="",total="",digit="" ,m_id="",matka_name="",game_id="",start_time="",end_time="",url="";
    public MotorFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_motor, container, false);
        initViews(v);
        return v;
    }
    void initViews(View v)
    {
        module = new Module(getActivity());
        common = new Common(getActivity());
        loadingBar = new LoadingBar(getActivity());

        tv_date = v.findViewById(R.id.tv_date);
        tv_note = v.findViewById(R.id.tv_note);
        btn_name = v.findViewById(R.id.btn_name);
        et_amount = v.findViewById(R.id.et_amount);
        et_digit = v.findViewById(R.id.et_digit);
        lay_amount = v.findViewById(R.id.lay_amount);
        lay_digit= v.findViewById(R.id.lay_digit);
        rd_close = v.findViewById(R.id.rd_close);
        rd_open = v.findViewById(R.id.rd_open);
        btn_reset = v.findViewById(R.id.reset);
        btn_sbmit = v.findViewById(R.id.submit);
        bet_list = new ArrayList<>();
        game = getActivity().getIntent().getStringExtra("game");
        game_id = getActivity().getIntent().getStringExtra("game_id");
        m_id = getActivity().getIntent().getStringExtra("m_id");
        matka_name = getActivity().getIntent().getStringExtra("matka_name");
        start_time = getActivity(). getIntent().getStringExtra("start_time");
        end_time = getActivity().getIntent().getStringExtra("end_time");
        btn_name.setText(game);
//         getActivity().getIntent().getStringExtra("start_num");
//     getActivity().getIntent().getStringExtra("num");
//       getActivity().getIntent().getStringExtra("end_num");


        rd_open.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    type="open";
                    rd_close.setChecked(false);
                }
            }
        });  rd_close.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b)
            {
                type="close";
                rd_open.setChecked(false);
            }
        }
    });

        switch (game)
        {
            case "sp motor":
                url = URL_SpMotor;
                break;
            case "dp motor":
                url = URL_DpMotor;
                break;
        }

        et_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String a = String.valueOf(charSequence);
//                float p = Float.parseFloat(a);
//                float tot = p * selected_list.size();
//                if (tot>0)
//                {
//                    lin_total.setVisibility(View.VISIBLE);
//                    tv_total.setText(""+tot);
//                }

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        btn_sbmit.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
//        common.currentDateDay(tv_date);
        common.setGameDate(tv_date,end_time);


    }


    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.submit)
        {
            points = et_amount.getText().toString().trim();
            digit= et_digit.getText().toString().trim();
            if (type.isEmpty())
            {
                module.showToast("Select Bet Type");
            }
           else if (digit.isEmpty())
            {
                module.validateEditText(et_digit,lay_digit,"Enter Digits");
            }
            else if (digit.length()!=4)
            {
                module.validateEditText(et_digit,lay_digit,"Enter 4 digits ");
            }
            else if (points.isEmpty())
            {
                module.validateEditText(et_amount,lay_amount,"Add some amount");
            }
            else if (Integer.parseInt(points)<Integer.parseInt(min_bid_amount))
            {
                module.showToast("Minimum bid amount is "+min_bid_amount);
            }
            else
            {
                getDataSet(digit,points,type);
            }
        }
        else if (view.getId()==R.id.reset)
        {
            et_digit.setText(" ");
            et_amount.setText(" ");

        }
    }

    private void getDataSet(final String inputData, final String d, final String th) {
      loadingBar.show();
      String json_tag="json_motor";
        Map<String, String> params = new HashMap<>();
        params.put("arr", inputData);
        CustomJsonRequest customJsonRequest=new CustomJsonRequest(Request.Method.POST,url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("motor",""+game+"\n"+url+"\n"+response.toString());
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        JSONArray as = response.getJSONArray("data");
                        bet_list.clear();
                        for (int i = 0; i < as.length(); i++) {
                            String p = as.getString(i);
                            bet_list.add(new TableModel(p, d, th));
                            Log.e("betlist_size",bet_list.size()+"");
                        }
//                            common.setBidsDialog(Integer.parseInt(PlayBidActivity.tv_wallet.getText().toString()),bet_list,m_id,tv_date.getText().toString().trim(),game_id,PlayBidActivity.tv_wallet.getText().toString().trim(),matka_name,loadingBar,btn_sbmit,start_time,end_time);
                            common.insertData(bet_list,m_id,tv_date.getText().toString().trim(),game_id, PlayBidActivity.tv_wallet.getText().toString(),matka_name,loadingBar,btn_sbmit,start_time,end_time);
                            resetControls();

                        loadingBar.dismiss();

                    } else {
                        loadingBar.dismiss();
                        Toast.makeText(getActivity(), "Something wrong", Toast.LENGTH_LONG).show();

                    }


//                            JSONObject object=new JSONObject(response);
//                            String status=object.getString("status");
//                            List asd=Arrays.asList(object.getString("answer"));

                } catch (Exception ex) {
                    loadingBar.dismiss();
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loadingBar.dismiss();
              module.showToast(module.VolleyErrorMessage(error));
            }
        });

        AppController.getInstance().addToRequestQueue(customJsonRequest,json_tag);

    }

    void resetControls()
    {
        switch (game)
        {
            case "sp motor":
                url = URL_SpMotor;
                break;
            case "dp motor":
                url = URL_DpMotor;
                break;
        }
        bet_list.clear();
        type ="";
        points="";
        digit="";

    }

}