package in.games.dsgames.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.dsgames.Adapter.TableAdapter;
import in.games.dsgames.Config.Common;
import in.games.dsgames.Interface.OnGetConfigData;
import in.games.dsgames.Interface.OnSuccess;
import in.games.dsgames.Model.ConfigDataModel;
import in.games.dsgames.Model.MatkaModel;
import in.games.dsgames.Model.TableModel;
import in.games.dsgames.R;
import in.games.dsgames.utils.LoadingBar;
import in.games.dsgames.utils.Session_management;

import static in.games.dsgames.Config.Constants.KEY_ID;
import static in.games.dsgames.Config.Constants.KEY_WALLET;


public class CrossingFragment extends Fragment implements View.OnClickListener{

    private final String TAG = CrossingFragment.class.getSimpleName();
    Common common;
    Session_management session_management;
    LoadingBar loadingBar;
    TextView tv_wallet,tv_total;
    EditText et_number1,et_number2,et_amount;
    RelativeLayout btn_add,btn_submit;
    RecyclerView rv_number;
//    ToastMsg toastMsg;
    ArrayList<TableModel> tList;
    String matka_id="";
    TableAdapter tableAdapter;
    int min_bid=0;
    CheckBox chk_cut;
    MatkaModel model=null;
    public CrossingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_crossing, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View v) {
        matka_id=getActivity().getIntent().getStringExtra("matka_id");
        tList=new ArrayList<>();
        common=new Common(getActivity());
//        toastMsg=new ToastMsg(getActivity());
        loadingBar=new LoadingBar(getActivity());
        model=common.getModelFromIntent(getActivity().getIntent().getStringExtra("matka"));
        session_management=new Session_management(getActivity());
        tv_wallet=v.findViewById(R.id.tv_wallet);
        tv_total=v.findViewById(R.id.tv_total);
        et_number1=v.findViewById(R.id.et_number1);
        et_number2=v.findViewById(R.id.et_number2);
        et_amount=v.findViewById(R.id.et_amount);
        btn_add=v.findViewById(R.id.btn_add);
        btn_submit=v.findViewById(R.id.btn_submit);
        rv_number=v.findViewById(R.id.rv_number);
        chk_cut=v.findViewById(R.id.chk_cut);
        rv_number.setLayoutManager(new LinearLayoutManager(getActivity()));
        btn_submit.setOnClickListener(this);
        btn_add.setOnClickListener(this);
//        common.checkLoginStatus();
        et_number1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s=editable.toString();
                et_number2.setText(s);

            }
        });

        chk_cut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               setList(b);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
//        min_bid = Integer.parseInt(min_bid_amount);
        common.cofigData(new OnGetConfigData() {
            @Override
            public void onGetConfigData(ConfigDataModel model) {
                min_bid=Integer.parseInt(model.getMin_bid_points ().toString());
            }
        });
        common.setWallet_Amount(tv_wallet,loadingBar,session_management.getUserDetails().get(KEY_ID));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add:
                setList(chk_cut.isChecked());
                break;
            case R.id.btn_submit:
                String date="";
                if(common.checkNullString(model.getGame_code()).equalsIgnoreCase(getResources().getString(R.string.desawer_game))){
                    if(common.getTimeDiffernce(common.checkNullString(model.getBid_end_time()))<=0){
                        date=common.getPreviousDate();
                    }else{
                        date=common.getCDate();
                    }
                }else{
                    date=common.getCDate();
                }//
                common.setOpenSheetData(tList, matka_id, date,session_management.getUserDetails().get(KEY_WALLET), new OnSuccess() {
                    @Override
                    public void onSuccess(String msg) {
                        getActivity().finish();
                    }
                });
                break;
        }
    }

    public void  clear()
    {
        et_amount.getText().clear();
        et_number1.getText().clear();
        et_number2.getText().clear();
    }
    public String totalAmount(){
        int sum=0;
        if(tList.size()>0) {
            for (int i = 0; i < tList.size(); i++) {
                sum = sum + Integer.parseInt(tList.get(i).getPoints().toString());

            }
        }
        return String.valueOf(sum);
    }

    public ArrayList<String> getCrossingNumbers(String str1,String str2){
        //str1=123 //str2=123
        ArrayList<String> nList=new ArrayList<>();
        int s1Len=str1.length();
        int s2Len=str2.length();
        for(int i=0; i<s1Len;i++){
            int ki=0;
            if(ki<s1Len){
                ki=i+1;
            }
            String fi=str1.substring(i,ki);
            for(int j=0; j<s2Len;j++){
                int kj=0;
                if(kj<s2Len){
                    kj=j+1;
                }
                String fj=str2.substring(j,kj);
                nList.add(fi+""+fj);
            }
        }
        return nList;
    }

    public ArrayList<String> getJodiCutNumbers(String str1,String str2){
        //str1=123 //str2=123
        ArrayList<String> nList=new ArrayList<>();
        int s1Len=str1.length();
        int s2Len=str2.length();
        for(int i=0; i<s1Len;i++){
            int ki=0;
            if(ki<s1Len){
                ki=i+1;
            }
            String fi=str1.substring(i,ki);
            for(int j=0; j<s2Len;j++){
                int kj=0;
                if(kj<s2Len){
                    kj=j+1;
                }
                String fj=str2.substring(j,kj);
                if(!fi.equals(fj)){
                    nList.add(fi+""+fj);
                }

            }
        }

        return nList;
    }

    private void setList(boolean flag){
        tList.clear();
        String number1=et_number1.getText().toString();
        String number2=et_number2.getText().toString();
        String amt=et_amount.getText().toString().trim();
        if(number1.isEmpty()){
            common.errorMessageDialog("Enter Number");
        }else if(amt.isEmpty()){
            common.errorMessageDialog("Enter Amount");
        } else if (Integer.parseInt(amt) < 1) {
            common.errorMessageDialog("Enter Valid Amount");
        } else if(min_bid>Integer.parseInt(amt)){
            common.errorMessageDialog("Minimum bid amount is "+min_bid);
        }else{
            ArrayList<String> list=new ArrayList<>();
            list.clear();
            if(flag){
                list.addAll(getJodiCutNumbers(number1,number2));
            }else{
                list.addAll(getCrossingNumbers(number1,number2));
            }

             for(int i=0; i<list.size();i++){
                 tList.add(new TableModel(list.get(i),amt,"Jodi"));
             }

             tableAdapter=new TableAdapter(getActivity(), tList, new TableAdapter.OnItemClickListener() {
                 @Override
                 public void onItemClickListener(TableModel item, int position, View view) {
                     tList.remove(position);
                     tv_total.setText(totalAmount());
                     tableAdapter.notifyDataSetChanged();
                 }
             });

             rv_number.setAdapter(tableAdapter);
             tableAdapter.notifyDataSetChanged();
             tv_total.setText(totalAmount());
//             clear();
        }


    }


}