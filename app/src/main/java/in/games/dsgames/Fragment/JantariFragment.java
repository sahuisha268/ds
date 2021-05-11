package in.games.dsgames.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.games.dsgames.Adapter.DigitAdapter;
import in.games.dsgames.Adapter.GamesAdapter;
import in.games.dsgames.Config.Common;
import in.games.dsgames.Config.InputData;
import in.games.dsgames.Interface.OnGetConfigData;
import in.games.dsgames.Interface.OnSuccess;
import in.games.dsgames.Model.ConfigDataModel;
import in.games.dsgames.Model.GamesModel;
import in.games.dsgames.Model.MatkaModel;
import in.games.dsgames.Model.TableModel;
import in.games.dsgames.R;
import in.games.dsgames.utils.LoadingBar;
import in.games.dsgames.utils.Session_management;

import static in.games.dsgames.Config.Constants.KEY_WALLET;

public class JantariFragment extends Fragment implements View.OnClickListener{
    private final String TAG = JantariFragment.class.getSimpleName();
    Common common;
    Session_management session_management;
    LoadingBar loadingBar;
    TextView tv_wallet,tv_total;
    LinearLayout btn_submit;
    RecyclerView rv_jodi,rv_andar,rv_bahar;
//    ToastMsg toastMsg;
    List<String> jodiList,panaList;
    ArrayList<GamesModel> gList;
    ArrayList<TableModel> jList,aList,bList;
    GamesAdapter gameAdapter;
    int min_bid=0;
    DigitAdapter digitAdapterJodi,digitAdapterAndar,digitAdapterBahar;
    ArrayList<TableModel> allList;
    int tot=0;
    String matka_id="";
    MatkaModel model=null;
    String beforeTextChangeValue="",beforeTextChangeValueAndar="",beforeTextChangeValueBahar="";


    public JantariFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_jantari, container, false);
        initView(view);
        return view;

    }

    private void initView(View v) {
        matka_id=getActivity().getIntent().getStringExtra("matka_id");
        common=new Common(getActivity());
//        toastMsg=new ToastMsg(getActivity());
        loadingBar=new LoadingBar(getActivity());
        model=common.getModelFromIntent(getActivity().getIntent().getStringExtra("matka"));
        session_management=new Session_management(getActivity());
        tv_wallet=v.findViewById(R.id.tv_wallet);
        tv_total=v.findViewById(R.id.tv_total);
        btn_submit=v.findViewById(R.id.btn_submit);
        rv_jodi=v.findViewById(R.id.rv_jodi);
        rv_andar=v.findViewById(R.id.rv_andar);
        rv_bahar=v.findViewById(R.id.rv_bahar);
        btn_submit.setOnClickListener(this);
        rv_jodi.setNestedScrollingEnabled(false);
        rv_andar.setNestedScrollingEnabled(false);
        rv_bahar.setNestedScrollingEnabled(false);
        jodiList=new ArrayList<>();
        gList=new ArrayList<>();
        jList=new ArrayList<>();
        aList=new ArrayList<>();
        bList=new ArrayList<>();
        panaList=new ArrayList<>();
        allList=new ArrayList<>();
        panaList= Arrays.asList(InputData.triple_patti);
        jodiList= Arrays.asList(InputData.jodi_digits);
//        common.checkLoginStatus();

        rv_jodi.setLayoutManager(new GridLayoutManager(getActivity(),10));
        rv_andar.setLayoutManager(new GridLayoutManager(getActivity(),10));
        rv_bahar.setLayoutManager(new GridLayoutManager(getActivity(),10));
        btn_submit.setOnClickListener(this);
        jList=getTableList(jodiList,"Jodi");
        aList=getTableList(panaList,"Andar");
        bList=getTableList(panaList,"Bahar");
        allList.addAll(getTableList(jodiList,"Jodi"));
        allList.addAll(getTableList(panaList,"Andar"));
        allList.addAll(getTableList(panaList,"Bahar"));
       gList.add(new GamesModel("Jantari",getTableList(jodiList,"Jodi")));
       gList.add(new GamesModel("Andar Haruf",getTableList(panaList,"Andar")));
       gList.add(new GamesModel("Bahar Haruf",getTableList(panaList,"Bahar")));

       digitAdapterJodi=new DigitAdapter(getActivity(), getTableList(jodiList, "Jodi"), new DigitAdapter.OnItemTextChangeListener() {
           @Override
           public void onbeforeTextChanged(TableModel item, int position, String str) {
               Log.e(TAG, "onbeforeTextChanged: jodi "+str+" - "+position );
               beforeTextChangeValue=str.toString();
           }

           @Override
           public void onTextChanged(TableModel item, int position, String str) {
               Log.e(TAG, "onTextChanged: jodi"+str+" - "+position );
           }

           @Override
           public void afterTextChanged(TableModel item, int position, String str) {
               Log.e(TAG, "afterTextChanged: jodi"+str+" - "+position );
               boolean backSpace = false;
               if (beforeTextChangeValue.length() > str.toString().length()) {
                   backSpace = true;
               }

               if (backSpace) {

                   String pnts = str.toString();
                   deleteFromList(item.getDigits(),pnts, position, beforeTextChangeValue,"Jodi");
               } else {
                   String points = str.toString();

                   addToBetList(item.getDigits(),points, position,"Jodi");
               }

               getTotal();

           }
       });

        digitAdapterAndar=new DigitAdapter(getActivity(), getTableList(panaList,"Andar"), new DigitAdapter.OnItemTextChangeListener() {
            @Override
            public void onbeforeTextChanged(TableModel item, int position, String str) {
                Log.e(TAG, "onbeforeTextChanged: andar"+str+" - "+position );
                beforeTextChangeValueAndar=str.toString();
            }

            @Override
            public void onTextChanged(TableModel item, int position, String str) {
                Log.e(TAG, "onTextChanged: andar"+str+" - "+position );
            }

            @Override
            public void afterTextChanged(TableModel item, int position, String str) {
                Log.e(TAG, "afterTextChanged: andar "+str+" - "+position );
                boolean backSpace = false;
                if (beforeTextChangeValueAndar.length() > str.toString().length()) {
                    backSpace = true;
                }

                if (backSpace) {

                    String pnts = str.toString();
                    deleteFromList(item.getDigits(),pnts, position, beforeTextChangeValueAndar,"Andar");
                } else {
                    String points = str.toString();
                    addToBetList(item.getDigits(),points, position,"Andar");
                }

                getTotal();
            }
        });

        digitAdapterBahar=new DigitAdapter(getActivity(), getTableList(panaList,"Bahar"), new DigitAdapter.OnItemTextChangeListener() {
            @Override
            public void onbeforeTextChanged(TableModel item, int position, String str) {
                Log.e(TAG, "onbeforeTextChanged: bahar"+str+" - "+position );
                beforeTextChangeValueBahar=str.toString();
            }

            @Override
            public void onTextChanged(TableModel item, int position, String str) {
                Log.e(TAG, "onTextChanged: bahar"+str+" - "+position );
            }

            @Override
            public void afterTextChanged(TableModel item, int position, String str) {
                Log.e(TAG, "afterTextChanged: bahar"+str+" - "+position );
                boolean backSpace = false;
                if (beforeTextChangeValueBahar.length() > str.toString().length()) {
                    backSpace = true;
                }

                if (backSpace) {

                    String pnts = str.toString();
                    deleteFromList(item.getDigits(),pnts, position, beforeTextChangeValueBahar,"Bahar");
                } else {
                    String points = str.toString();
                    addToBetList(item.getDigits(),points, position,"Bahar");
                }

                getTotal();
            }
        });

        rv_jodi.setAdapter(digitAdapterJodi);
        rv_andar.setAdapter(digitAdapterAndar);
        rv_bahar.setAdapter(digitAdapterBahar);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn_submit){
            ArrayList<TableModel> bidList=new ArrayList<>();
            for(TableModel m:allList){
                if(!m.getPoints().equals("0")){
//                    Log.e(TAG, "onClick: "+m.getNumber()+" : "+m.getAmount()+" : "+m.getType());
                    bidList.add(m);
                }

            }
            String date="";
            if(common.checkNullString(model.getGame_code()).equalsIgnoreCase(getResources().getString(R.string.desawer_game))){
                if(common.getTimeDiffernce(common.checkNullString(model.getBid_end_time()))<=0){
                    date=common.getPreviousDate();
                }else{
                    date=common.getCDate();
                }
            }else{
                date=common.getCDate();
            }
            for (int i = 0 ; i <bidList.size();i++)
            {
               if( (bidList.get(i).getDigits()).equals("100"))
                {
                   bidList.get(i).setDigits("00");
                }
            }

            common.setOpenSheetData(bidList, matka_id, date, session_management.getUserDetails().get(KEY_WALLET), new OnSuccess() {
                @Override
                public void onSuccess(String msg) {
                    getActivity().finish();
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        min_bid = Integer.parseInt(min_bid_amount);
        tv_wallet.setText(session_management.getUserDetails().get(KEY_WALLET));
        common.cofigData(new OnGetConfigData() {
            @Override
            public void onGetConfigData(ConfigDataModel model) {
                min_bid=Integer.parseInt(model.getMin_amount().toString());
            }
        });

    }

    public ArrayList<TableModel> getTableList(List<String> list, String type){
        ArrayList<TableModel> tList=new ArrayList<>();
        for(int i=0; i<list.size();i++){
            tList.add(new TableModel(list.get(i).toString(),"0",type));
        }
        return tList;
    }

        private void deleteFromList(String num,String pnts, int pos, String beforeTextChangeValue,String type) {
                    int tx= Integer.parseInt(common.checkNullNumber(pnts));
                    int beforeValue= Integer.parseInt(beforeTextChangeValue);
                    Log.e("beforeValue",""+beforeTextChangeValue+" - Next Value - " + tx);
                    Log.e("leeeeeee",""+pnts.length());
                    common.updatePoints(allList,getPosition(num,type),common.checkNullNumber(pnts),type);
                    getTotal();

        }

        private void addToBetList(String num,String points, int pos,String type) {
            int p =0;
            if(!points.isEmpty())
            {
                p = Integer.parseInt(points);

            }

                int pints = Integer.parseInt(common.checkNullNumber(points));
                if ( pints < min_bid) {
                    if(tot==0)
                    {

                    }

                }
                else {

                    tv_total.setText(String.valueOf(tot));
                    common.updatePoints(allList,getPosition(num,type),points,type);
                }
            getTotal();

    //        }
        }

    public void getTotal(){
        int sum=0;
        for(int i=0; i<allList.size();i++){
            if(!allList.get(i).getPoints().equals("0")){
                Log.e(TAG, "getTotal: "+allList.get(i).getPoints()+" : "+allList.get(i).getDigits());
            }

            sum=sum+Integer.parseInt(allList.get(i).getPoints());
        }
        tv_total.setText(""+sum);
    }

    public int getPosition(String num,String type){
        int index=-1;
        Log.e(TAG, "getPosition: "+num+" - "+type);
        for(int i=0; i<allList.size();i++){
            if(allList.get(i).getDigits().equals(num) && allList.get(i).getType().equalsIgnoreCase(type)){
                index=i;
                break;
            }
        }
        return index;

    }
}