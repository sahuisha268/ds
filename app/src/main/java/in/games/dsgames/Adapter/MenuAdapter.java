package in.games.dsgames.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.dsgames.Config.Module;
import in.games.dsgames.Model.MenuModel;
import in.games.dsgames.R;



public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    ArrayList<MenuModel> list;
    Activity activity;
    Module module;

    public MenuAdapter(ArrayList<MenuModel> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.row_menu,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.iv_icon.setImageDrawable(activity.getResources().getDrawable(list.get(position).getIcon()));
        holder.tv_name.setText(list.get(position).getName());
//        module.setRelativeTint(holder.rl_back,list.get(position).getColorcode());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_icon;
        RelativeLayout rl_back;
        TextView tv_name;
        public ViewHolder(@NonNull View v) {
            super(v);
            tv_name=v.findViewById(R.id.tv_name);
            rl_back=v.findViewById(R.id.rl_back);
            iv_icon=v.findViewById(R.id.iv_icon);
            module=new Module(activity);
        }
    }
}
