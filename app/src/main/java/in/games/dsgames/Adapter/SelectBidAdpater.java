package in.games.dsgames.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.dsgames.Model.GameModel;
import in.games.dsgames.R;

public class SelectBidAdpater extends RecyclerView.Adapter<SelectBidAdpater.ViewHolder> {
    Activity activity;
    ArrayList<GameModel> game_list ;
    String matka_id, matka_name, start_time, end_time, s_num, num, e_num ,parent;

    public SelectBidAdpater(Activity activity, ArrayList<GameModel> game_list, String matka_id, String matka_name, String start_time, String end_time, String s_num, String num, String e_num) {
        this.activity = activity;
        this.game_list = game_list;
        this.matka_id = matka_id;
        this.matka_name = matka_name;
        this.start_time = start_time;
        this.end_time = end_time;
        this.s_num = s_num;
        this.num = num;
        this.e_num = e_num;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View v = LayoutInflater.from(activity).inflate(R.layout.row_select_game,null);
      return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rv_game;
        TextView tv_title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_matka_name);
           rv_game = itemView.findViewById(R.id.rv_game);
        }
    }


}

