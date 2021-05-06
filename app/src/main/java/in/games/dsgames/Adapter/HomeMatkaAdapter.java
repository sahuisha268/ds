package in.games.dsgames.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.games.dsgames.Activity.SeelctGameActivity;
import in.games.dsgames.Config.Common;
import in.games.dsgames.Model.MatkasObjects;
import in.games.dsgames.R;

public class HomeMatkaAdapter extends RecyclerView.Adapter<HomeMatkaAdapter.ViewHolder> {
    Context context;
    private ArrayList<MatkasObjects> list;
    private String m_id;
    public static int flag=0;
    Common common;

    public HomeMatkaAdapter(Context context, ArrayList<MatkasObjects> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_home_matka,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        MatkasObjects postion=list.get(position);

        String dt=new SimpleDateFormat("EEEE").format(new Date());

        holder.txtMatkaName.setText(postion.getName());
        String s_time=null;
        String e_time=null;
        String s=null;
        String e=null;
        int day_flag=0;
        if(dt.equals("Sunday"))
        {
            if(postion.getStart_time().toString().equals("00:00:00") &&postion.getEnd_time().toString().equals("00:00:00"))
            {
                day_flag=1;
                s=postion.getBid_start_time().toString();
                e=postion.getBid_end_time().toString();
            }
            else
            {
                day_flag=2;
                s=postion.getStart_time().toString();
                e=postion.getEnd_time().toString();

            }

        }
        else if(dt.equals("Saturday"))
        {

            if(postion.getSat_start_time().toString().equals("00:00:00") && postion.getSat_end_time().toString().equals("00:00:00"))
            {
                day_flag=3;
                s=postion.getBid_start_time().toString();
                e=postion.getBid_end_time().toString();
            }
            else
            {
                day_flag=4;
                s=postion.getSat_start_time().toString();
                e=postion.getSat_end_time().toString();

            }

        }
        else
        {
            day_flag=5;
            s=postion.getBid_start_time().toString();
            e=postion.getBid_end_time().toString();
        }


        try {
            Date date=new SimpleDateFormat("HH:mm:ss").parse(s);
            Date date1=new SimpleDateFormat("HH:mm:ss").parse(e);
            s_time=new SimpleDateFormat("h:mm a").format(date);
            e_time=new SimpleDateFormat("h:mm a").format(date1);

        } catch (ParseException ex) {
            ex.printStackTrace();
        }



        holder.txtmatkaBid_openTime.setText(String.valueOf(s_time));
        holder.txtmatkaBid_closeTime.setText(String.valueOf(e_time));

        if (!common.checkNull(postion.getNumber())) {
            holder.txtMatka_resNo.setText(" - "+ postion.getNumber());
        }
        else
        {
            holder.txtMatka_resNo.setVisibility(View.GONE);
        }
        if (!common.checkNull(postion.getStarting_num()))
        {
            holder.txtMatka_startingNo.setText(postion.getStarting_num());
        }
        else
        {
            holder.txtMatka_startingNo.setVisibility(View.GONE);
        }
        if (!common.checkNull(postion.getEnd_num())) {

            holder.txtMatka_endNo.setText(" - "+postion.getEnd_num());
        }
        else
        {
            holder.txtMatka_endNo.setVisibility(View.GONE);
        }

        holder.txtMatka_id.setText(postion.getId());
        String status=postion.getStatus();

        Date date=new Date();
        SimpleDateFormat sim=new SimpleDateFormat("HH:mm:ss");
        String ct=sim.format(date);

        String time1 = s.toString();
        String time2 = e.toString();

        Date cdate=new Date();



        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String time3=format.format(cdate);
        Date date1 = null;
        Date date2=null;
        Date date3=null;
        try {
            date1 = format.parse(time1);
            date2 = format.parse(time2);
            date3=format.parse(time3);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        long difference = date3.getTime() - date1.getTime();
        long as=(difference/1000)/60;

        long diff_close=date3.getTime()-date2.getTime();
        long c=(diff_close/1000)/60;

        long current_time=date3.getTime();
        long cur=(current_time/1000)/60;
        // viewHolder.txtStatus.setText("cu "+cur+"\n o  "+as+"\n c "+c);
        if (status.equals( "active" )) {
            if (as < 0) {
                flag = 2;
                if(day_flag==1 || day_flag == 3)
                {
                    holder.txtStatus.setTextColor(context.getResources().getColor(R.color.closed)  );
                    holder.txtStatus.setText( "BID IS CLOSED" );

                }
                else
                {
                    holder.txtStatus.setTextColor( context.getResources().getColor(R.color.running)  );
                    holder.txtStatus.setText( "RUNNING FOR TODAY" );

                }

            } else if (c > 0) {
                flag = 3;
                holder.txtStatus.setTextColor(context.getResources().getColor(R.color.closed)  );
                holder.txtStatus.setText( "BID IS CLOSED" );

            } else {
                flag = 1;
                if(day_flag==1 || day_flag == 3)
                {
                    holder.txtStatus.setTextColor(context.getResources().getColor(R.color.closed)  );
                    holder.txtStatus.setText("BID IS CLOSED" );
                }
                else
                {
                    holder.txtStatus.setVisibility( View.INVISIBLE );
                }

            }
        }
        else
        {
            holder.txtStatus.setText( "BID IS CLOSED" );
            holder.txtStatus.setTextColor(context.getResources().getColor(R.color.closed)  );

        }

        final String finalE = e;
        final String finalS = s;
        holder.rel_matka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.txtStatus.getVisibility()==View.VISIBLE && holder.txtStatus.getText().toString().equalsIgnoreCase("BID IS CLOSED") ){
                    common.showToast("BID IS CLOSED");
                }else {
                    MatkasObjects m = list.get(position);
                    String m_id = m.getId().toString().trim();
                    String matka_name = m.getName().toString().trim();
                    Intent intent = new Intent(context, SeelctGameActivity.class);
                    intent.putExtra("matka_name", matka_name);
                    intent.putExtra("m_id", m_id);
                    intent.putExtra("end_time", finalE);
                    intent.putExtra("start_time", finalS);
                    intent.putExtra("start_num", m.getStarting_num());
                    intent.putExtra("num", m.getNumber());
                    intent.putExtra("end_num", m.getEnd_num());
                    context.startActivity(intent);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDess2,txtmatkaBid_openTime,txtmatkaBid_closeTime,txtMatkaName,txtMatka_startingNo,
                txtMatka_resNo,txtMatka_endNo,txtStatus,txtMatka_id,txt_play;
        RelativeLayout rl,rel_matka ,rel_number,rl_anim;
        ImageView imageGame;
        public ViewHolder(@NonNull View view) {
            super(view);
            txtDess2=(TextView)view.findViewById(R.id.matka_dess2);
            txtmatkaBid_openTime=(TextView)view.findViewById(R.id.matka_open_bid_Time);
            txtmatkaBid_closeTime=(TextView)view.findViewById(R.id.matka_close_bid_Time);
            txtMatkaName=(TextView)view.findViewById(R.id.matkaName);
            txtMatka_startingNo=(TextView)view.findViewById(R.id.matka_starting_Number);
            txtMatka_resNo=(TextView)view.findViewById(R.id.matka_res_Number);
            txtMatka_endNo=(TextView)view.findViewById(R.id.matka_end_Number);
            txtStatus=(TextView)view.findViewById(R.id.matkaBettingStatus);
            txtMatka_id=(TextView) view.findViewById(R.id.matka_id);
            rel_matka = view.findViewById(R.id.rel_matka);
            rel_number = view.findViewById(R.id.rel_number);
            common = new Common(context);
        }
    }
}
