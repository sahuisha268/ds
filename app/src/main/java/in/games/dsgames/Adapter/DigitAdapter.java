package in.games.dsgames.Adapter;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.dsgames.Model.TableModel;
import in.games.dsgames.R;


/**
 * Developed by Binplus Technologies pvt. ltd.  on 06,January,2021
 */
public class DigitAdapter extends RecyclerView.Adapter<DigitAdapter.DigitHolder>{
    Activity activity;
    ArrayList<TableModel> tList;

    public interface OnItemTextChangeListener{
        void onbeforeTextChanged(TableModel item, int position, String str);
        void onTextChanged(TableModel item, int position, String str);
        void afterTextChanged(TableModel item, int position, String str);
    }
    public DigitAdapter.OnItemTextChangeListener listener;
    public DigitAdapter(Activity activity, ArrayList<TableModel> tList) {
        this.activity = activity;
        this.tList = tList;
    }

    public DigitAdapter(Activity activity, ArrayList<TableModel> tList, OnItemTextChangeListener listener) {
        this.activity = activity;
        this.tList = tList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DigitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.row_jodi,null);
        return new DigitHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DigitHolder holder, int position) {

        TableModel m=tList.get(position);
        holder.bind(m,position,listener);
        holder.tv_number.setText(m.getDigits().toString());

    }

    @Override
    public int getItemCount() {
        return tList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class DigitHolder extends RecyclerView.ViewHolder {
        TextView tv_number;
        EditText et_amount;
        public DigitHolder(@NonNull View v) {
            super(v);
            tv_number=v.findViewById(R.id.tv_number);
            et_amount=v.findViewById(R.id.et_amount);
        }
        public void bind(final TableModel item, final int position, final OnItemTextChangeListener itemTextChangeListener){
           et_amount.addTextChangedListener(new TextWatcher() {
               @Override
               public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                   itemTextChangeListener.onbeforeTextChanged(item,position,charSequence.toString());
               }

               @Override
               public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                   itemTextChangeListener.onTextChanged(item,position,charSequence.toString());

               }

               @Override
               public void afterTextChanged(Editable editable) {
                   itemTextChangeListener.afterTextChanged(item,position,editable.toString().toString());
               }
           });
        }
    }
}
