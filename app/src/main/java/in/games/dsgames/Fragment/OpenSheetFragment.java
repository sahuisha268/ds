package in.games.dsgames.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class OpenSheetFragment extends Fragment implements View.OnClickListener{
    private final String TAG = OpenSheetFragment.class.getSimpleName();
    Common common;
    Session_management session_management;
    LoadingBar loadingBar;
    TextView tv_wallet,tv_total;
    EditText et_number,et_amount;
    RelativeLayout btn_add,btn_submit;
    RecyclerView rv_number;
//    ToastMsg toastMsg;
    ArrayList<TableModel> tList;
    TableAdapter tableAdapter;
    int min_bid=0;
    String matka_id="",matka_code="";
    MatkaModel model=null;

    public OpenSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_open_sheet, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View v) {
        tList=new ArrayList<>();
        common=new Common(getActivity());
//        toastMsg=new ToastMsg(getActivity());
        loadingBar=new LoadingBar(getActivity());
        matka_id=getActivity().getIntent().getStringExtra("matka_id");

        model=common.getModelFromIntent(getActivity().getIntent().getStringExtra("matka"));
        session_management=new Session_management(getActivity());
        tv_wallet=v.findViewById(R.id.tv_wallet);
        tv_total=v.findViewById(R.id.tv_total);
        et_number=v.findViewById(R.id.et_number);
        et_amount=v.findViewById(R.id.et_amount);
        btn_add=v.findViewById(R.id.btn_add);
        btn_submit=v.findViewById(R.id.btn_submit);
        rv_number=v.findViewById(R.id.rv_number);
        rv_number.setLayoutManager(new LinearLayoutManager(getActivity()));
        btn_submit.setOnClickListener(this);
        btn_add.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        common.cofigData(new OnGetConfigData() {
            @Override
            public void onGetConfigData(ConfigDataModel model) {
                min_bid=Integer.parseInt(model.getMin_bid_points ().toString());
            }
        });

        common.setWallet_Amount(tv_wallet,loadingBar,session_management.getUserDetails().get(KEY_ID));
//        tv_wallet.setText("1000000");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add:
                String number=et_number.getText().toString();
                String amt=et_amount.getText().toString().trim();
                if(number.isEmpty()){
                    common.errorMessageDialog("Enter Number");
                }else if(amt.isEmpty()){
                    common.errorMessageDialog("Enter Amount");
                } else if (Integer.parseInt(amt) < 1) {
                    common.errorMessageDialog("Enter Valid Amount");
                } else if(min_bid>Integer.parseInt(amt)){
                    common.errorMessageDialog("Minimum bid amount is "+min_bid);
                }
                else {
                    if (common.validNumber(number)) {
                        switch (common.getNumberType(number)) {
                            case "Andar":
                                tList.add(new TableModel(number,amt,"Andar"));
                                break;
                            case "Bahar":
                                if(number.equals("100")){

                                    tList.add(new TableModel("00",amt,"Jodi"));
                                }else{
                                    tList.add(new TableModel(common.getBaharNumber(number),amt,"Bahar"));
                                }

                                break;
                            case "Jodi":

                                 ArrayList<String> nList=common.getJodiNumbers(number);
                                 for(String num:nList){
                                     tList.add(new TableModel(num,amt,"Jodi"));
                                 }
                                break;
                        }
                        tableAdapter=new TableAdapter(getActivity(), tList, new TableAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClickListener(TableModel item, int position, View view) {
                              if(view.getId()==R.id.iv_remove){
                                  tList.remove(position);
                                  tv_total.setText(totalAmount());
                                  tableAdapter.notifyDataSetChanged();
                              }

                            }
                        });


                        rv_number.setAdapter(tableAdapter);
                        tableAdapter.notifyDataSetChanged();

                        tv_total.setText(totalAmount());
                        clear();

                    } else {
                        common.errorMessageDialog("Please enter even value");
                    }
                }

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
                }//session_management.getUserDetails().get(KEY_WALLET)

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
        et_number.getText().clear();
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


}