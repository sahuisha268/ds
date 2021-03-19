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

import in.games.dsgames.Model.HistryOptionsModel;
import in.games.dsgames.R;

public class HistoryOptionAdapter extends RecyclerView.Adapter<HistoryOptionAdapter.ViewHolder> {
    ArrayList<HistryOptionsModel> h_list;
    Activity activity ;

    public HistoryOptionAdapter(ArrayList<HistryOptionsModel> h_list, Activity activity) {
        this.h_list = h_list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.row_histry_options,null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.tv_name.setText(h_list.get(position).getName());
    holder.iv_img.setImageResource(h_list.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return h_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_img;
        TextView tv_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_img = itemView.findViewById(R.id.iv_img);
            tv_name= itemView.findViewById(R.id.tv_name);
        }
    }
}
