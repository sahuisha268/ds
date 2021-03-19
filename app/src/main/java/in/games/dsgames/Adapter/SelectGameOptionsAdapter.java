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
import java.util.List;

import in.games.dsgames.Config.Module;
import in.games.dsgames.Model.SelectGameOptionsModel;
import in.games.dsgames.R;

import static in.games.dsgames.Fragment.PattiFragment.et_amount;
import static in.games.dsgames.Fragment.PattiFragment.lin_total;
import static in.games.dsgames.Fragment.PattiFragment.selected_list;
import static in.games.dsgames.Fragment.PattiFragment.tv_total;


public class SelectGameOptionsAdapter extends RecyclerView.Adapter<SelectGameOptionsAdapter.ViewHolder> {
    ArrayList<SelectGameOptionsModel>list;
    Activity activity;
    RecyclerView rv_seelcted;
    Module module ;

    public SelectGameOptionsAdapter(ArrayList<SelectGameOptionsModel> list, Activity activity, RecyclerView rv_seelcted) {
        this.list = list;
        this.activity = activity;
        this.rv_seelcted = rv_seelcted;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      View v = LayoutInflater.from(activity).inflate(R.layout.row_select_option,null);
      return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.tv_title.setText(list.get(position).getName());
        holder.rv_digits.setLayoutManager(new GridLayoutManager(activity, module.calculateNoOfColumns(activity,50)));
        DigitAdapter digitAdapter = new DigitAdapter(list.get(position).getDigit_list(),activity);
        holder.rv_digits.setAdapter(digitAdapter);
        digitAdapter.notifyDataSetChanged();
        holder.rel_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.rv_digits.getVisibility()==View.GONE)
                {
                    holder.rv_digits.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.rv_digits.setVisibility(View.GONE);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        RecyclerView rv_digits;
        RelativeLayout rel_select;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_name);
            rv_digits = itemView.findViewById(R.id.rv_digits);
            rel_select = itemView.findViewById(R.id.rel_option);
            module = new Module(activity);
        }
    }





class DigitAdapter extends RecyclerView.Adapter<DigitAdapter.D_viewHolder>
{
  List<String> d_lsit;
    Activity activity ;

    public DigitAdapter(List<String> d_lsit, Activity activity) {
        this.d_lsit = d_lsit;
        this.activity = activity;
    }

    @NonNull
    @Override
    public D_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View v = LayoutInflater.from(activity).inflate(R.layout.row_digit,null);
      return new D_viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final D_viewHolder holder, final int position) {
    holder.tv_digit.setText(d_lsit.get(position));
    holder.tv_digit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!selected_list.contains(d_lsit.get(position))) {
//                selected_list.add(d_lsit.get(position));
//                SelectedDigitAdapter selectedDigitAdapter = new SelectedDigitAdapter(selected_list, activity);
//                rv_seelcted.setLayoutManager(new GridLayoutManager(activity, module.calculateNoOfColumns(activity,90)));
//                rv_seelcted.setAdapter(selectedDigitAdapter);
//                selectedDigitAdapter.notifyDataSetChanged();

                if (!et_amount.getText().toString().trim().isEmpty()) {
                    String a = String.valueOf(et_amount.getText().toString().trim());
                    float p = Float.parseFloat(a);
                    float tot = p * selected_list.size();
                    if (tot > 0) {
                        lin_total.setVisibility(View.VISIBLE);
                        tv_total.setText("" + tot);
                    }
                }
//                d_lsit.remove(position);
//                notifyDataSetChanged();

            }
        }
    });
    }

    @Override
    public int getItemCount() {
        return d_lsit.size();
    }

    public class D_viewHolder extends RecyclerView.ViewHolder {
        TextView tv_digit;
        public D_viewHolder(@NonNull View itemView) {
            super(itemView);
            tv_digit = itemView.findViewById(R.id.tv_digit);
        }
    }
}}


