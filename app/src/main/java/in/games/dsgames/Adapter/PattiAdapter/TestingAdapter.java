package in.games.dsgames.Adapter.PattiAdapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.dsgames.Config.Module;
import in.games.dsgames.Model.PattiModel.TestingModel;
import in.games.dsgames.R;

public class TestingAdapter extends RecyclerView.Adapter<TestingAdapter.ViewHolder> {
    ArrayList<TestingModel> list;
    Activity activity;
    Module module;
    public interface OnItemClickListener{
        void onItemClickListener(View view,TestingModel digit,int position);
    }
    TestingAdapter.OnItemClickListener listener;

    public TestingAdapter(ArrayList<TestingModel> list, Activity activity, TestingAdapter.OnItemClickListener listener) {
        this.list = list;
        this.activity = activity;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TestingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.row_selected_digits,parent,false);
        TestingAdapter.ViewHolder viewHolder = new TestingAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TestingAdapter.ViewHolder holder, int position) {
        holder.bind(position,list.get(position),listener);

        holder.tv_text.setText(list.get(position).getDigit());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_text;
         ImageView txtDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_text=itemView.findViewById(R.id.tv_digit);
            txtDelete=itemView.findViewById(R.id.iv_remove);
            module=new Module(activity);
        }
        public void bind(final int pos, final TestingModel item, final TestingAdapter.OnItemClickListener listener){
            txtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClickListener(view,item,pos);
                   // list.remove(item);
                }
            });
        }
    }
}
