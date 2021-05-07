package in.games.dsgames.Fragment.PattiFragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;

import in.games.dsgames.Activity.PlayBidActivity;
import in.games.dsgames.Adapter.PattiAdapter.PattiAdapter;
import in.games.dsgames.Config.Common;
import in.games.dsgames.Config.Module;
import in.games.dsgames.Model.PattiModel.PattiModel;
import in.games.dsgames.Model.TableModel;
import in.games.dsgames.R;
import in.games.dsgames.utils.LoadingBar;

import static in.games.dsgames.Activity.SplashActivity.min_bid_amount;
import static in.games.dsgames.Config.InputData.single_pana_patti;

public class SinglePattiFragment extends Fragment implements View.OnClickListener{
    Button btnAdd,btnSubmit,btnReset,btn_name;
    PattiAdapter pattiAdapter;
    RadioButton rd_open ,rd_close;
    RecyclerView rv_select;
    ArrayList<PattiModel> list;
    ArrayList<TableModel> bet_list;
    Module module;
    AutoCompleteTextView tv_digit;
    TextInputLayout lay_amount;
    TextInputEditText et_amount;
    LoadingBar loadingBar;
    Common common;
    TextView tv_date;
    LinearLayout lin_total;
    TextView tv_total;
    String type ="", points="",total="" ,m_id="",matka_name="",game_id="",start_time="",end_time="",game="";

    public SinglePattiFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_single_patti, container, false);
       initViews(v);
        rv_select.setHasFixedSize(true);
        rv_select.setLayoutManager(new GridLayoutManager(getActivity(), module.calculateNoOfColumns(getActivity(),90)));
        pattiAdapter=  new PattiAdapter(initData(), getActivity(), new PattiAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, PattiModel digit, int position) {
                list.remove(position);
                pattiAdapter.notifyDataSetChanged();
                getTotal();

            }
        });

        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,single_pana_patti);
        final AutoCompleteTextView etDgt= (AutoCompleteTextView)v.findViewById(R.id.tv_number);
        etDgt.setAdapter(adapter);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dgt=tv_digit.getText().toString();
                if(dgt.isEmpty()){
                    module.showToast("Select any one point");
                }else {
                    if (existDigit(list, dgt)) {
                        module.showToast("Digit all ready exist");
                    } else {


                        if (!Arrays.asList(single_pana_patti).contains(dgt)) {
                            String message = "Invalid Patti";
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            //errorMessageDialog(SingleDigitActivity.this,message);
                            tv_digit.requestFocus();
                            return;

                        }else{

                            PattiModel model = new PattiModel(dgt);
                            model.setDigit(dgt);
                            list.add(model);
                            if(!et_amount.getText().toString().isEmpty()){
                                getTotal();
                            }
                        }
                    }
                }



                rv_select.setAdapter(pattiAdapter);
                tv_digit.setText("");
                tv_digit.requestFocus();
            }
        });
        return v;
    }

    private void initViews(View v) {
        bet_list = new ArrayList<>();
        btnAdd=v.findViewById(R.id.add);
        rv_select=v.findViewById(R.id.rv_select);
        btnSubmit=v.findViewById(R.id.submit);
        btnReset=v.findViewById(R.id.reset);
        btnSubmit.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        tv_digit=v.findViewById(R.id.tv_number);
        et_amount = v.findViewById(R.id.et_amount);
        lay_amount = v.findViewById(R.id.lay_amount);
        module = new Module(getActivity());
        common=new Common(getActivity());
        loadingBar=new LoadingBar(getActivity());
        btn_name=v.findViewById(R.id.btn_name);
        rd_open=v.findViewById(R.id.rd_open);
        rd_close=v.findViewById(R.id.rd_close);
        tv_date=v.findViewById(R.id.tv_date);
        lin_total=v.findViewById(R.id.lin_total);
        tv_total=v.findViewById(R.id.tv_total);
        game = getActivity().getIntent().getStringExtra("game");
        game_id = getActivity().getIntent().getStringExtra("game_id");
        m_id = getActivity().getIntent().getStringExtra("m_id");
        matka_name = getActivity().getIntent().getStringExtra("matka_name");
        start_time = getActivity(). getIntent().getStringExtra("start_time");
        end_time = getActivity().getIntent().getStringExtra("end_time");
        btn_name.setText(game);
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

        et_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {

                String a = editable.toString();
                if(a.isEmpty()){
                    module.showToast("Enter some points");
                    if(lin_total.getVisibility()==View.VISIBLE){
                        lin_total.setVisibility(View.GONE);
                        tv_total.setText("0.0");
                    }


                }
                else{
                    float p = Float.parseFloat(a);
                    float tot = p * list.size();
                    if (tot>0)
                    {
                        lin_total.setVisibility(View.VISIBLE);
                        tv_total.setText(""+tot);
                    }
                }

            }
        });

        common.setGameDate(tv_date,end_time);
    }

    private ArrayList<PattiModel> initData() {
        list = new ArrayList<>();
        //  list.add(new SelectedDigitModel("WITHDRAW","JHGF NJHGF HGF JHGHYTF DCVJK JNBV"));
        //list.add(new NoticeBoardModel("notice", "hgvf hgft gfg ihjn xfcvg vygu vygu"));
        return list;
    }
    boolean existDigit(ArrayList<PattiModel> list1,String digit)
    {
        boolean flag = false;
        if (list1.size()>0)
        {


            for (int i=0; i<list1.size();i++)
            {
                if (list1.get(i).getDigit().equals(digit))
                {
                    flag=true;
                }
            }
        }
        return flag;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.submit:
                int gid= Integer.parseInt (m_id);

                if(gid>20){
                    type="open";
                }
                points = et_amount.getText().toString();
                String c_date = tv_date.getText().toString();
                String w = PlayBidActivity.tv_wallet.getText().toString();
                if (type.equals("")||type.isEmpty())
                {
                    module.showToast("Select Bet Type");
                    //
                }
                else if (list.size()<=0)
                {
                    module.showToast("Select atleast one digit");
                }
                else if (points.isEmpty())
                {
//                module.showToast("Add points");
                    module.validateEditText(et_amount,lay_amount,"Add some amount");
                }
                else if (Integer.parseInt(points)<Integer.parseInt(min_bid_amount))
                {
                    module.showToast("Minimum bid amount is "+min_bid_amount);
                }
                else
                {
                    for (int i = 0 ; i<list.size();i++)
                    {
                        bet_list.add(new TableModel(list.get(i).getDigit(),points,type));
                    }
                    if (bet_list.size()>0)
                    {
                        common.insertData(bet_list,m_id,c_date,game_id,w,matka_name,loadingBar,btnSubmit,start_time,end_time);
                    }
                }
                break;
            case R.id.reset:
                resetBids();
                break;
        }
    }

    void resetBids(){
        bet_list.clear();
        list.clear();
        pattiAdapter.notifyDataSetChanged();
        if(lin_total.getVisibility()==View.VISIBLE){
            lin_total.setVisibility(View.GONE);
        }
        tv_total.setText("0.0");
    }

    public void getTotal(){
        String point=et_amount.getText().toString();
        if(!point.isEmpty()){
            if(list.size()>0){

                float p = Float.parseFloat(et_amount.getText().toString());
                float tot = p * list.size();
                if (tot>0)
                {
                    lin_total.setVisibility(View.VISIBLE);
                    tv_total.setText(""+tot);
                }
            }else{
                lin_total.setVisibility(View.GONE);
                tv_total.setText("0.0");
            }

        }
            }
}