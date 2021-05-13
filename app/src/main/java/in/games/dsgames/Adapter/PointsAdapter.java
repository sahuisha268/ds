package in.games.dsgames.Adapter;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.games.dsgames.Config.Common;
import in.games.dsgames.Model.TableModel;
import in.games.dsgames.R;

import static in.games.dsgames.Fragment.DigitFragment.g_type;
import static in.games.dsgames.Fragment.DigitFragment.patti_list;
import static in.games.dsgames.Fragment.DigitFragment.tv_total;


public class PointsAdapter extends RecyclerView.Adapter<PointsAdapter.ViewHolder> {

  public static ArrayList<TableModel> b_list ;
 List<String> digit_list ;
    Activity activity;
public static ArrayList<String> ponitsList;
Common common;

    int tot = 0;
    int index =0 ;
    String beforeTextChangeValue="";


public static Boolean is_empty = true , is_error = false ;

    public PointsAdapter(List<String> digit_list, Activity activity) {
        this.digit_list = digit_list;
        this.activity = activity;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_new_digits,null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        viewHolder.txt_digits.setText(digit_list.get(i));
        patti_list.clear();
        for(int j=0; j<digit_list.size();j++)
        {
            ponitsList.add("0");
            patti_list.add(new TableModel(digit_list.get(j).toString(),"0",g_type));
        }

        viewHolder.et_points.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               beforeTextChangeValue=s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (g_type.isEmpty()) {
                       common.showToast("Select Bet Type");
                } else {
                    boolean backSpace = false;
                    if (beforeTextChangeValue.length() > s.toString().length()) {
                        backSpace = true;
                    }

                    if (backSpace) {
                        String pnts = s.toString();
                        deleteFromList(pnts, i, beforeTextChangeValue);
                    } else {
                        String points = s.toString();
                        addToBetList(points, i);
                    }



                }

            }

        });


    }

    private void deleteFromList(String pnts, int pos, String beforeTextChangeValue) {

        if(!pnts.isEmpty())
        {
            if(tot!=0)
            {
                int tx= Integer.parseInt(pnts);
                int beforeValue= Integer.parseInt(beforeTextChangeValue);
                Log.e("beforeValue",""+beforeTextChangeValue+" - Next Value - " + tx);
                Log.e("leeeeeee",""+pnts.length());

                if(pnts.length()==1)
                {
                    tot = (tot)-beforeValue;
                }
                else if(pnts.length()==2)
                {
                    tot = (tot+tx)-beforeValue;
                }
                else if(pnts.length() == 3)
                {
                    tot = (tot+tx)-beforeValue;
                }
                else if(pnts.length()==4)
                {

                    tot = (tot+tx)-beforeValue;
                }
                else if(pnts.length()==5)
                {

                    tot = (tot+tx)-beforeValue;
                }

               tv_total.setText(String.valueOf(tot));

                ponitsList.set(pos,"0");
                if (g_type.isEmpty())
                { Toast.makeText(activity, "Select game type", Toast.LENGTH_LONG).show();
                }
                else {
                    if(pnts.length()>1)
                    {
                        common.updatePoints(patti_list,pos,pnts,g_type);

                    }else
                    {


                        common.updatePoints(patti_list,pos,"0",g_type);

                        ponitsList.clear ();
                    }

//
                }



            }

        }

    }

    private void addToBetList(String points, int pos) {
        int p =0;

        if(!points.isEmpty())
        {
            p = Integer.parseInt(points);

        }

        if (points.length() != 0) {

            if (points.isEmpty()) {
                is_empty = true;
            } else {
                is_empty = false;
                int pints = Integer.parseInt(points);
                if ( pints < 10) {
                    if(tot==0)
                    {
                        is_error = true;
                    }

                }
                else {

                    int ps = Integer.parseInt(points);

                    if(points.length()==2)
                    {
                        Log.e("digits2",""+points);
                        tot = tot + ps;
                    }
                    else if(points.length() == 3)
                    {
                        tot = (tot + ps)- Integer.parseInt(patti_list.get(pos).getPoints());
                        Log.e("digits3",""+points);
                    }
                    else if(points.length()==4)
                    {
                        tot = (tot + ps)- Integer.parseInt(patti_list.get(pos).getPoints());

                        Log.e("digits4",""+points);
                    }
                    else if(points.length()==5)
                    {
                        tot = (tot + ps)- Integer.parseInt(patti_list.get(pos).getPoints());

                        Log.e("digits4",""+points);
                    }

                    is_empty = false;
                    is_error = false;
                    ponitsList.set(pos, String.valueOf(ps));
                    tv_total.setText(String.valueOf(tot));




                    if (g_type.isEmpty())
                    {
//                                    Toast.makeText(activity, "Select game type", Toast.LENGTH_LONG).show();
                    }
                    else {

                        common.updatePoints(patti_list,pos,points,g_type);
//                        tot=0;

//                                    bet_list.add(new TableModel(digit_list.get(i), points, txt_type.getText().toString()));
                    }
                }


            }

        }

    }

    @Override
    public int getItemCount() {
        return digit_list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_digits ;
        EditText et_points;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ponitsList=new ArrayList<>();
            txt_digits = itemView.findViewById(R.id.txt_digit);
            et_points = itemView.findViewById(R.id.et_points);
            b_list = new ArrayList<>();
            common=new Common(activity);

        }
    }

    public static ArrayList<String> getPonitsList()
    {
        return ponitsList;
    }
    public static ArrayList<TableModel> getBetlist()
    {
        return b_list;
    }
}
