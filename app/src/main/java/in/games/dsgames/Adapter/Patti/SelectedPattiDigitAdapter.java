package in.games.dsgames.Adapter.Patti;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.games.dsgames.Model.SelectedSectionDigitModel;
import in.games.dsgames.R;

public class SelectedPattiDigitAdapter extends RecyclerView.Adapter<SelectedPattiDigitAdapter.Viewholder> {
    List<SelectedSectionDigitModel> digit_lsit;
    Activity activity;

    public interface OnItemClickListener{
        void onItemClickListener(View view, SelectedSectionDigitModel digit, int position);
    }
    SelectedPattiDigitAdapter.OnItemClickListener listener;

    public SelectedPattiDigitAdapter(List<SelectedSectionDigitModel> digit_lsit, Activity activity, OnItemClickListener listener) {
        this.digit_lsit = digit_lsit;
        this.activity = activity;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View v = LayoutInflater.from(activity).inflate(R.layout.row_selected_digits,null);
      return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        holder.bind(position,digit_lsit.get(position),listener);
        holder.tv_digit.setText(digit_lsit.get(position).getDigit());

    }

    @Override
    public int getItemCount() {
        return digit_lsit.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView tv_digit;
        ImageView iv_remove;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tv_digit = itemView.findViewById(R.id.tv_digit);
            iv_remove = itemView.findViewById(R.id.iv_remove);
        }

        public void bind(final int pos, final SelectedSectionDigitModel item, final SelectedPattiDigitAdapter.OnItemClickListener listener){
            iv_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                  public void onClick(View view) {
                     listener.onItemClickListener(view,item,pos);
                }
            });
        }
    }
}
