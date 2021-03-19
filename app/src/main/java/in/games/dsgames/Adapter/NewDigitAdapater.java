package in.games.dsgames.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.games.dsgames.R;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 11,December,2020
 */
public class NewDigitAdapater extends RecyclerView.Adapter<NewDigitAdapater.DigitViewHolder>{

    List<String> dList;
    Activity activity ;

    public interface OnDigitItemClickListener{
        void onDigitItemClick(View view,String digit,int pos);
    }

    public NewDigitAdapater.OnDigitItemClickListener listener;

    public NewDigitAdapater(List<String> dList, Activity activity, OnDigitItemClickListener listener) {
        this.dList = dList;
        this.activity = activity;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DigitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.row_digit,null);
        return new DigitViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DigitViewHolder holder, int position) {
        holder.bind(position,dList.get(position),listener);
        holder.tv_digit.setText(dList.get(position));
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

     public void bind(final int pos, final String digit, final NewDigitAdapater.OnDigitItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDigitItemClick(v,digit,pos);
                }
            });
     }
    }
}
