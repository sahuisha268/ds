package in.games.dsgames.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.dsgames.Config.Module;
import in.games.dsgames.Model.NewDigitModel;
import in.games.dsgames.Model.NewSelectOptionsModel;
import in.games.dsgames.R;
import in.games.dsgames.utils.RecyclerTouchListener;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 11,December,2020
 */
public class NewGameAdapter extends RecyclerView.Adapter<NewGameAdapter.GameViewHolder> {
    ArrayList<NewSelectOptionsModel> gameList;
    Activity activity;
    Module module;

//    public interface OnItemClickListener {
//        void onItemClick(View view,SelectGameOptionsModel item, int position);
//    }

    public interface OnItemRecListener{
        void onItemTouchListener(View view, NewSelectOptionsModel item, int position);
        void onItemClick(View view, NewSelectOptionsModel item, int position);
    }

//    public GameAdapter.OnItemClickListener listener;
    public NewGameAdapter.OnItemRecListener touchlistener;

//
//    public GameAdapter(ArrayList<SelectGameOptionsModel> gameList, Activity activity, OnItemClickListener listener) {
//        this.gameList = gameList;
//        this.activity = activity;
//        this.listener = listener;
//    }


    public NewGameAdapter(ArrayList<NewSelectOptionsModel> gameList, Activity activity, OnItemRecListener touchlistener) {
        this.gameList = gameList;
        this.activity = activity;
        this.touchlistener = touchlistener;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.row_select_option, null);
        return new GameViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
//        holder.bind(position,gameList.get(position),listener);
        holder.bindRec(position,gameList.get(position),touchlistener);
        holder.tv_title.setText(gameList.get(position).getName());
        holder.rv_digits.setLayoutManager(new GridLayoutManager(activity, module.calculateNoOfColumns(activity,50)));
       NewDigitAdapater digitAdapter=new NewDigitAdapater(gameList.get(position).getDigit_list(),activity);
        holder.rv_digits.setAdapter(digitAdapter);
        digitAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public class GameViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        RecyclerView rv_digits;
        RelativeLayout rel_select;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_name);
            rv_digits = itemView.findViewById(R.id.rv_digits);
            rel_select = itemView.findViewById(R.id.rel_option);
            module = new Module(activity);
        }
//        public void bind(final int pos, final SelectGameOptionsModel item, final GameAdapter.OnItemClickListener listener){
//            rel_select.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onItemClick(v,item,pos);
//                }
//            });
//        }

        public void bindRec(final int pos, final NewSelectOptionsModel item, NewGameAdapter.OnItemRecListener recListener ){
            rv_digits.addOnItemTouchListener(new RecyclerTouchListener(activity, rv_digits, new RecyclerTouchListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    touchlistener.onItemTouchListener(view,item,position);
                }

                @Override
                public void onLongItemClick(View view, int position) {

                }
            }));

            rel_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NewSelectOptionsModel s=new NewSelectOptionsModel();
                touchlistener.onItemClick(view,s,pos);
                }
            });
        }
    }
    public class NewDigitAdapater extends RecyclerView.Adapter<NewDigitAdapater.DigitViewHolder>{

        ArrayList<NewDigitModel> dList;
        Activity activity ;





        public NewDigitAdapater(ArrayList<NewDigitModel> dList, Activity activity) {
            this.dList = dList;
            this.activity = activity;

        }

        @NonNull
        @Override
        public DigitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(activity).inflate(R.layout.row_digit,null);
            return new DigitViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull DigitViewHolder holder, int position) {


            holder.tv_digit.setText(dList.get(position).getDigit());
//            if(dList.get(position).isDeleted()){
//                holder.itemView.setVisibility(View.GONE);
//                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
//            }else{
//                if(holder.itemView.getVisibility()==View.GONE){
//                    holder.itemView.setVisibility(View.VISIBLE);
//                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                }
//            }
        }

        @Override
        public int getItemCount() {
            return dList.size();
        }

        public class DigitViewHolder extends RecyclerView.ViewHolder {
            TextView tv_digit;
            public DigitViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_digit = itemView.findViewById(R.id.tv_digit);
            }


        }
    }

}
