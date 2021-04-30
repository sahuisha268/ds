package in.games.dsgames.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.games.dsgames.Activity.PlayBidActivity;
import in.games.dsgames.Adapter.NewGameAdapter;
import in.games.dsgames.Adapter.Patti.NewPattiAdapter;
import in.games.dsgames.Adapter.SelectGameOptionsAdapter;
import in.games.dsgames.Adapter.SelectedDigitAdapter;
import in.games.dsgames.Config.Common;
import in.games.dsgames.Config.InputData;
import in.games.dsgames.Config.Module;
import in.games.dsgames.Model.NewDigitModel;
import in.games.dsgames.Model.NewDigitSectionModel;
import in.games.dsgames.Model.NewSelectOptionsModel;
import in.games.dsgames.Model.SelectedDigitModel;
import in.games.dsgames.Model.TableModel;
import in.games.dsgames.R;
import in.games.dsgames.utils.LoadingBar;

import static in.games.dsgames.Activity.SplashActivity.min_bid_amount;


public class PattiFragment extends Fragment implements View.OnClickListener {
    private final String TAG=PattiFragment.class.getSimpleName();
    String game;
    RecyclerView rv_select,rv_selected;
    RadioButton rd_open ,rd_close;
    TextView tv_note,tv_date ;
    Button btn_name;
    List<String> jodiMainList,bracketMainList;

    RecyclerView.LayoutManager layoutManager;
    TextInputLayout lay_amount;
   public static TextInputEditText et_amount;
    Button btn_sbmit ,btn_reset ;
    LoadingBar loadingBar ;
    Module module ;
    Common common;
   public static LinearLayout lin_total;
   public static TextView tv_total;
    ArrayList<NewSelectOptionsModel> s_p_list,d_p_list;
    public static ArrayList<SelectedDigitModel> selected_list;
  ArrayList<TableModel> bet_list;
    SelectGameOptionsAdapter gameOptionsAdapter;
    SelectedDigitAdapter dAdapter;
//    GameAdapter gameAdapter;
    NewGameAdapter adapter;
    NewPattiAdapter pAdapter;

    ArrayList<NewDigitModel> digitList;
    ArrayList<NewSelectOptionsModel> jodiList,r_list,pattiList;

    String type ="", points="",total="" ,m_id="",matka_name="",game_id="",start_time="",end_time="";
    public PattiFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v =inflater.inflate(R.layout.fragment_patti, container, false);
       initViews(v);
       return v;
    }
    void initViews(View v)
    {
        module = new Module(getActivity());
      common = new Common(getActivity());
     loadingBar = new LoadingBar(getActivity());
     rv_select = v.findViewById(R.id.rv_select);
     rv_selected = v.findViewById(R.id.rv_selected_digits);
     tv_date = v.findViewById(R.id.tv_date);
     jodiMainList=new ArrayList<>();
     bracketMainList=new ArrayList<>();
     digitList=new ArrayList<>();
     jodiList=new ArrayList<>();
     tv_note = v.findViewById(R.id.tv_note);
     btn_name = v.findViewById(R.id.btn_name);
     et_amount = v.findViewById(R.id.et_amount);
     lay_amount = v.findViewById(R.id.lay_amount);
     rd_close = v.findViewById(R.id.rd_close);
     rd_open = v.findViewById(R.id.rd_open);
     btn_reset = v.findViewById(R.id.reset);
     btn_sbmit = v.findViewById(R.id.submit);
     tv_total = v.findViewById(R.id.tv_total);
     lin_total = v.findViewById(R.id.lin_total);
     r_list= new ArrayList<>();
        pattiList= new ArrayList<>();
     s_p_list= new ArrayList<>();
     d_p_list= new ArrayList<>();
     selected_list = new ArrayList<>();
     bet_list = new ArrayList<>();
     dAdapter=new SelectedDigitAdapter(getActivity());
     game = getActivity().getIntent().getStringExtra("game");
        game_id = getActivity().getIntent().getStringExtra("game_id");
        m_id = getActivity().getIntent().getStringExtra("m_id");
        matka_name = getActivity().getIntent().getStringExtra("matka_name");
        start_time = getActivity(). getIntent().getStringExtra("start_time");
        end_time = getActivity().getIntent().getStringExtra("end_time");
        btn_name.setText(game);
        layoutManager=new LinearLayoutManager(getActivity());
        rv_select.setLayoutManager(layoutManager);
        switch (game)
        {
            case "jodi":
                rd_close.setVisibility(View.GONE);
                final ArrayList<NewDigitModel> newDigitList=new ArrayList<>();
                jodiMainList=  Arrays.asList(InputData.jodi_digits);
                digitList.clear();
                for(int i=0; i<jodiMainList.size();i++){
                    digitList.add(new NewDigitModel(String.valueOf(i),jodiMainList.get(i),false));
                    newDigitList.add(new NewDigitModel(String.valueOf(i),jodiMainList.get(i),false));
                }
                jodiList.add(new NewSelectOptionsModel("1","Select Jodi",digitList));

                adapter=new NewGameAdapter(jodiList, getActivity(), new NewGameAdapter.OnItemRecListener() {
                    @Override
                    public void onItemTouchListener(View view, NewSelectOptionsModel item, int position) {
                        final ArrayList<NewDigitModel> tempList=new ArrayList<>();
                        for(int i=0; i<jodiList.get(0).getDigit_list().size();i++){
                            tempList.add(jodiList.get(0).getDigit_list().get(i));
                        }


                        String dgt=tempList.get(position).getDigit();

                        int idx=getDigitModelIndex(tempList,dgt);
                        int iddx=getDigitModelIndex(newDigitList,dgt);
                        selected_list.add(new SelectedDigitModel(idx,dgt));

                        try {
                            tempList.get(idx).setDeleted(true);
                            newDigitList.get(iddx).setDeleted(true);
                            notifyAdapter(tempList,jodiList);
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        dAdapter=new SelectedDigitAdapter(selected_list, getActivity(), new SelectedDigitAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClickListener(View view, SelectedDigitModel item, int ps) {
                                if(view.getId() == R.id.iv_remove){
                                    try {
                                        int ix=getSelDigitIndex(selected_list,item.getDigit().toString());
                                        final ArrayList<NewDigitModel> tempList1=new ArrayList<>();
                                        for(int i=0; i<newDigitList.size();i++){
                                            tempList1.add(newDigitList.get(i));

                                        }

                                        int idx=findDigitPosition(jodiMainList,item.getDigit());
                                        tempList1.get(idx).setDeleted(false);
                                        notifyAdapter(tempList1,jodiList);
                                        selected_list.remove(ix);
                                        dAdapter.notifyDataSetChanged();
                                    }catch (Exception ex){
                                        ex.printStackTrace();
                                    }

                                }
                            }
                        });
                        rv_selected.setLayoutManager(new GridLayoutManager(getActivity(), module.calculateNoOfColumns(getActivity(),90)));
                        rv_selected.setAdapter(dAdapter);
                        dAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onItemClick(View view, NewSelectOptionsModel item, int position) {
                        View adapterView=layoutManager.findViewByPosition(position);
                        RecyclerView  rv_digits=adapterView.findViewById(R.id.rv_digits);

                        if(view.getId() == R.id.rel_option){

                            if (rv_digits.getVisibility()==View.GONE)
                            {
                                rv_digits.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                rv_digits.setVisibility(View.GONE);
                            }
                        }
                    }
                });


                break;

            case "red bracket":
                rd_close.setVisibility(View.GONE);
                final ArrayList<NewDigitModel> newBDigitList=new ArrayList<>();
                bracketMainList=  Arrays.asList(InputData.red_bracket);
                digitList.clear();
                for(int i=0; i<bracketMainList.size();i++){
                    digitList.add(new NewDigitModel(String.valueOf(i),bracketMainList.get(i),false));
                    newBDigitList.add(new NewDigitModel(String.valueOf(i),bracketMainList.get(i),false));
                }
                r_list.add(new NewSelectOptionsModel("1","Select Red Bracket Jodi",  digitList));
                adapter=new NewGameAdapter(r_list, getActivity(), new NewGameAdapter.OnItemRecListener() {
                    @Override
                    public void onItemTouchListener(View view, NewSelectOptionsModel item, int position) {
                        final ArrayList<NewDigitModel> tempList=new ArrayList<>();
                        for(int i=0; i<r_list.get(0).getDigit_list().size();i++){
                            tempList.add(r_list.get(0).getDigit_list().get(i));
                        }


                        String dgt=tempList.get(position).getDigit();

                        int idx=getDigitModelIndex(tempList,dgt);
                        int iddx=getDigitModelIndex(newBDigitList,dgt);
                        selected_list.add(new SelectedDigitModel(idx,dgt));

                        try {
                            tempList.get(idx).setDeleted(true);
                            newBDigitList.get(iddx).setDeleted(true);
                            notifyAdapter(tempList,r_list);
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        dAdapter=new SelectedDigitAdapter(selected_list, getActivity(), new SelectedDigitAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClickListener(View view, SelectedDigitModel item, int ps) {
                                if(view.getId() == R.id.iv_remove){
                                    try {
                                        int ix=getSelDigitIndex(selected_list,item.getDigit().toString());
                                        final ArrayList<NewDigitModel> tempList1=new ArrayList<>();
                                        for(int i=0; i<newBDigitList.size();i++){
                                            tempList1.add(newBDigitList.get(i));

                                        }

                                        int idx=findDigitPosition(bracketMainList,item.getDigit());
                                        tempList1.get(idx).setDeleted(false);
                                        notifyAdapter(tempList1,r_list);
                                        selected_list.remove(ix);
                                        dAdapter.notifyDataSetChanged();
                                    }catch (Exception ex){
                                        ex.printStackTrace();
                                    }

                                }
                            }
                        });
                        rv_selected.setLayoutManager(new GridLayoutManager(getActivity(), module.calculateNoOfColumns(getActivity(),90)));
                        rv_selected.setAdapter(dAdapter);
                        dAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onItemClick(View view, NewSelectOptionsModel item, int position) {
                        View adapterView=layoutManager.findViewByPosition(position);
                        RecyclerView  rv_digits=adapterView.findViewById(R.id.rv_digits);

                        if(view.getId() == R.id.rel_option){

                            if (rv_digits.getVisibility()==View.GONE)
                            {
                                rv_digits.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                rv_digits.setVisibility(View.GONE);
                            }
                        }
                    }
                });

        }

        rv_select.setAdapter(adapter);
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
                float tot = p * selected_list.size();
                if (tot>0)
                {
                    lin_total.setVisibility(View.VISIBLE);
                    tv_total.setText(""+tot);
                }
            }

        }
    });
        btn_sbmit.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
//        common.currentDateDay(tv_date);
        common.setGameDate(tv_date,end_time);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.submit)
        {
            points = et_amount.getText().toString();
            String c_date = tv_date.getText().toString();
            String w = PlayBidActivity.tv_wallet.getText().toString();
            if (type.equals("")||type.isEmpty())
            {
                module.showToast("Select Bet Type");
            }
            else if (selected_list.size()<=0)
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
                for (int i = 0 ; i<selected_list.size();i++)
                {
                    bet_list.add(new TableModel(selected_list.get(i).getDigit(),points,type));
                }
                if (bet_list.size()>0)
                {
                   common.insertData(bet_list,m_id,c_date,game_id,w,matka_name,loadingBar,btn_sbmit,start_time,end_time);
                }
            }
        }
        else if (view.getId()==R.id.reset)
        {
            bet_list.clear();
            selected_list.clear();

                dAdapter.notifyDataSetChanged();

            if(lin_total.getVisibility()==View.VISIBLE){
                lin_total.setVisibility(View.GONE);
            }
            tv_total.setText("0.0");
        }

    }

    public int getDigitIndex(List<String> list,String str){
        int inx=-1;
        for(int i=0; i<list.size();i++){
            if(list.get(i).equals(str)){
                inx=i;
                break;
            }
        }
        return inx;
    }
    public int getDigitModelIndex(ArrayList<NewDigitModel> list,String str){
        int inx=-1;
        for(int i=0; i<list.size();i++){
            if(list.get(i).getDigit().equals(str)){
                inx=i;
                break;
            }
        }
        return inx;
    }

    public int getDigitSectionModelIndex(ArrayList<NewDigitSectionModel> list,String str){
        int inx=-1;
        for(int i=0; i<list.size();i++){
            if(list.get(i).getDigit().equals(str)){
                inx=i;
                break;
            }
        }
        return inx;
    }

    public int getSelDigitIndex(List<SelectedDigitModel> list,String str){
        int inx=-1;
        for(int i=0; i<list.size();i++){
            if(list.get(i).getDigit().equals(str)){
                inx=i;
                break;
            }
        }
        return inx;
    }

    public int findDigitPosition(List<String> list,String dgt){
    int indx=-1;
    for(int i=0; i<list.size();i++){
        if(list.get(i).equals(dgt)){
            indx=i;
            break;
        }
    }
    return indx;
    }
    public void notifyAdapter(ArrayList<NewDigitModel> tempList,ArrayList<NewSelectOptionsModel> jList){
        ArrayList<NewDigitModel> tList=new ArrayList<>();
        tList.clear();
        for(int j=0; j<tempList.size();j++){
            if(!tempList.get(j).isDeleted()){
                tList.add(new NewDigitModel(tempList.get(j).getId(),tempList.get(j).getDigit(),false));
            }

        }
        jList.get(0).setDigit_list(tList);//.getDigit_list().remove(idx);
        adapter.notifyDataSetChanged();
    }

    public void notifyPattiAdapter(ArrayList<NewDigitSectionModel> tempList,ArrayList<NewSelectOptionsModel> jList,NewPattiAdapter pAda){
        ArrayList<NewDigitModel> tList=new ArrayList<>();
        tList.clear();
        for(int j=0; j<tempList.size();j++){
            if(!tempList.get(j).isDeleted()){
                tList.add(new NewDigitModel(tempList.get(j).getId(),tempList.get(j).getDigit(),false));
            }

        }
        jList.get(0).setDigit_list(tList);//.getDigit_list().remove(idx);
        pAda.notifyDataSetChanged();
    }
    public  ArrayList<NewDigitModel> getDigitList(String[] strArr){
        List<String> tempList=Arrays.asList(strArr);
        ArrayList<NewDigitModel> dList=new ArrayList<>();
        for(int i=0;i<tempList.size();i++){
            dList.add(new NewDigitModel(String.valueOf(i),tempList.get(i),false));
        }
        return dList;
    }
}