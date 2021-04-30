package in.games.dsgames.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.dsgames.Activity.BidHistoryActivity;
import in.games.dsgames.Activity.MainActivity;
import in.games.dsgames.Activity.RequestPointActivity;
import in.games.dsgames.Activity.TestingActivity;
import in.games.dsgames.Activity.WithDrawActivity;
import in.games.dsgames.Adapter.HistoryOptionAdapter;
import in.games.dsgames.Config.Common;
import in.games.dsgames.Interface.OnGetConfigData;
import in.games.dsgames.Model.ConfigDataModel;
import in.games.dsgames.Model.HistryOptionsModel;
import in.games.dsgames.R;
import in.games.dsgames.utils.RecyclerTouchListener;


public class HistoryFragment extends Fragment {
RecyclerView rv_bid ,rv_wallet;
ArrayList<HistryOptionsModel>bid_list,wallet_list;
HistoryOptionAdapter bid_adapter,wallet_adapter;
ImageView iv_back;
Common common ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_history, container, false);
        initViews(root);
        return root;
    }
    void initViews(View v)
    {
        rv_bid = v.findViewById(R.id.rv_bid_histry);
    rv_wallet = v.findViewById(R.id.rv_wallet_histry);
    iv_back = v.findViewById(R.id.iv_back);
    bid_list =new ArrayList<>();
    wallet_list = new ArrayList<>();
    common = new Common(getActivity());

    bid_list.add(new HistryOptionsModel("2","Matka Bazar History","matka",R.drawable.matkahistrory));
        common.cofigData(new OnGetConfigData() {
            @Override
            public void onGetConfigData(ConfigDataModel model) {

                if (model.getIs_starline().equals("1"))
                {
                    bid_list.add(new HistryOptionsModel("4","Starline History","starline",R.drawable.starlineimage));

                }
                else{



                }
            }
        });

    wallet_list.add(new HistryOptionsModel("1","Deposit Request","deposit",R.drawable.depositrequest));
    wallet_list.add(new HistryOptionsModel("2","Withdraw Request","withdraw",R.drawable.withdraw));
   // wallet_list.add(new HistryOptionsModel("3","Account Statement","account",R.drawable.accountstatement));
    rv_bid.setLayoutManager(new GridLayoutManager(getActivity(),2));
    rv_wallet.setLayoutManager(new GridLayoutManager(getActivity(),2));
    bid_adapter = new HistoryOptionAdapter(bid_list,getActivity());
   wallet_adapter = new HistoryOptionAdapter(wallet_list,getActivity());
   rv_wallet.setAdapter(wallet_adapter);
   rv_bid.setAdapter(bid_adapter);
   rv_bid.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_bid, new RecyclerTouchListener.OnItemClickListener() {
       @Override
       public void onItemClick(View view, int position) {

           Intent intent = new Intent(getActivity(), BidHistoryActivity.class);
           intent.putExtra("name",bid_list.get(position).getName());
           intent.putExtra("type",bid_list.get(position).getType());

           startActivity(intent);

       }

       @Override
       public void onLongItemClick(View view, int position) {

       }
   }));
   rv_wallet.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_bid, new RecyclerTouchListener.OnItemClickListener() {
       @Override
       public void onItemClick(View view, int position) {
         switch (position)
         {
             case 0:
                 startActivity(new Intent(getActivity(), RequestPointActivity.class));
                 break;
             case 1:
                 startActivity(new Intent(getActivity(), WithDrawActivity.class));
                 break;
             case 2:
               //  startActivity(new Intent(getActivity(), PaymentReportsActivity.class));
                 startActivity(new Intent(getActivity(), TestingActivity.class));
                 break;
         }
       }

       @Override
       public void onLongItemClick(View view, int position) {

       }
   }));
   iv_back.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           startActivity(new Intent(getActivity(), MainActivity.class));
       }
   });

    }


}