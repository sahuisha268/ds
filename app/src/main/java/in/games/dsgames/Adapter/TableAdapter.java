package in.games.dsgames.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.dsgames.Model.TableModel;
import in.games.dsgames.R;


/**
 * Developed by Binplus Technologies pvt. ltd.  on 05,January,2021
 */
public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {
     Activity activity;
     ArrayList<TableModel> list;
     public interface OnItemClickListener{
         void onItemClickListener(TableModel item, int position, View view);

     }
     public TableAdapter.OnItemClickListener listener;

    public TableAdapter(Activity activity, ArrayList<TableModel> list, OnItemClickListener listener) {
        this.activity = activity;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TableAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.row_number,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableAdapter.ViewHolder holder, int position) {
        TableModel model=list.get(position);
        holder.bind(model,position,listener);
        holder.tv_type.setText(model.getType());
//        holder.tv_number.setText(model.getNumber());
        holder.tv_number.setText(model.getDigits());
//        holder.tv_amount.setText(model.getAmount());
        holder.tv_amount.setText(model.getPoints());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_type,tv_number,tv_amount;
        ImageView iv_remove;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_type=itemView.findViewById(R.id.tv_type);
            tv_number=itemView.findViewById(R.id.tv_number);
            tv_amount=itemView.findViewById(R.id.tv_amount);
            iv_remove=itemView.findViewById(R.id.iv_remove);
        }
        public void bind(final TableModel item, final int position, final OnItemClickListener listener1){
            iv_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    listener1.onItemClickListener(item,position,view);
                }
            });
        }
    }
}
