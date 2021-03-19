package in.games.dsgames.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.games.dsgames.Config.Common;
import in.games.dsgames.Model.Starline_Objects;
import in.games.dsgames.R;

public class StarlineAdapter extends RecyclerView.Adapter<StarlineAdapter.ViewHolder> {
ArrayList<Starline_Objects> star_list;
Activity activity ;
Common common ;

    public StarlineAdapter(ArrayList<Starline_Objects> star_list, Activity activity) {
        this.star_list = star_list;
        this.activity = activity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
   View v = LayoutInflater.from(activity).inflate(R.layout.row_starline_matka,null);
  return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        
        Date date=new Date();
        SimpleDateFormat format1=new SimpleDateFormat("hh:mm aa");
        String dr=format1.format(date);
        int sTime=common.getTimeFormatStatus(String.valueOf(star_list.get(position).getS_game_time()));
        holder.txtTime.setText(star_list.get(position).getS_game_time());
       holder.tv_time.setText(star_list.get(position).getS_game_time());
        //txtTime.setText(postion.getS_game_end_time().toString()+" -- "+dr);
        // Toast.makeText(context,"db_time:-  "++"\n curr_time:-  "+dr,Toast.LENGTH_LONG).show();

        String tm=common.getCloseStatus(star_list.get(position).getS_game_end_time().toString(),dr);
        String[] end_time=tm.split(":");
        int h= Integer.parseInt(end_time[0].toString());
        int m= Integer.parseInt(end_time[1].toString());

        if(h<=0 && m<0)
        {
            holder.txtName.setText("Bet Is Running ");
            holder.txtNumber.setText("***-**");

            holder.txtName.setTextColor(activity.getResources().getColor(R.color.running));
            //txtStatus.setText("o");

        }
        else
        {
            holder.txtName.setText("Bet is Closed ");
//            if(!star_list.get(position).getS_game_number().equalsIgnoreCase("***")){
////                common.shakeAnimations(rl_amin);
//
//            }
            holder.txtName.setTextColor(activity.getResources().getColor(R.color.closed));
            holder.txtNumber.setText(""+star_list.get(position).getS_game_number());
         
            //txtStatus.setText("c");
        }

    }

    @Override
    public int getItemCount() {
        return star_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNumber, txtTime,txtName,tv_time;
        public ViewHolder(@NonNull View view) {
            super(view);
          txtNumber=(TextView)view.findViewById(R.id.pg_Number);
          txtTime=(TextView)view.findViewById(R.id.pg_Time);
            txtName=(TextView)view.findViewById(R.id.pg_title);
           tv_time=(TextView)view.findViewById(R.id.tv_time);
           common = new Common(activity);
        }
    }
}
