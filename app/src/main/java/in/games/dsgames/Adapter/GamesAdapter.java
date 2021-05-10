package in.games.dsgames.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.dsgames.Model.GamesModel;
import in.games.dsgames.Model.TableModel;
import in.games.dsgames.R;


/**
 * Developed by Binplus Technologies pvt. ltd.  on 06,January,2021
 */
public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.GameHolder> {
    Activity activity;
    ArrayList<GamesModel> gList;
    GridLayoutManager layoutManager;

    public GamesAdapter(Activity activity, ArrayList<GamesModel> gList) {
        this.activity = activity;
        this.gList = gList;
    }

    @NonNull
    @Override
    public GameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.row_jantari,null);
        return new GameHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameHolder holder, int position) {
        GamesModel gameModel=gList.get(position);
        holder.tv_title.setText(gameModel.getTitle().toString());

        DigitAdapter digitAdapter=new DigitAdapter(activity,gameModel.getList());
        holder.rv_digit.setAdapter(digitAdapter);
        digitAdapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return gList.size();
    }

    public class GameHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        RecyclerView rv_digit;


        public GameHolder(@NonNull View v) {
            super(v);
            tv_title=v.findViewById(R.id.tv_title);
            rv_digit=v.findViewById(R.id.rv_digit);
             layoutManager=new GridLayoutManager(activity,10);
            rv_digit.setLayoutManager(layoutManager);
            rv_digit.setNestedScrollingEnabled(false);

        }




    }

    public class DigitAdapter extends RecyclerView.Adapter<DigitAdapter.DigitHolder>{
        Activity activity;
        ArrayList<TableModel> tList;



        public DigitAdapter(Activity activity, ArrayList<TableModel> tList) {
            this.activity = activity;
            this.tList = tList;
        }

        @NonNull
        @Override
        public DigitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(activity).inflate(R.layout.row_jodi,null);
            return new DigitHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DigitHolder holder, int position) {
            TableModel m=tList.get(position);
            holder.tv_number.setText(m.getDigits().toString());

        }

        @Override
        public int getItemCount() {
            return tList.size();
        }

        public class DigitHolder extends RecyclerView.ViewHolder {
            TextView tv_number;
            EditText et_amount;
            public DigitHolder(@NonNull View v) {
                super(v);
                tv_number=v.findViewById(R.id.tv_number);
                et_amount=v.findViewById(R.id.et_amount);
            }
        }
    }
}
