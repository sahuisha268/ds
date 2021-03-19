package in.games.dsgames.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.dsgames.Model.GameModel;
import in.games.dsgames.R;

public class SelectGameAdapter extends RecyclerView.Adapter<SelectGameAdapter.ViewHolder> {
    Activity activity;
    ArrayList<GameModel> game_list;
    String matka_id, matka_name, start_time, end_time, s_num, num, e_num;

    public SelectGameAdapter(Activity activity, ArrayList<GameModel> game_list, String matka_id, String matka_name, String start_time, String end_time, String s_num, String num, String e_num) {
        this.activity = activity;
        this.game_list = game_list;
        this.matka_id = matka_id;
        this.matka_name = matka_name;
        this.start_time = start_time;
        this.end_time = end_time;
        this.s_num = s_num;
        this.num = num;
        this.e_num = e_num;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(
                R.layout.row_games, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final GameModel model = game_list.get(position);
        holder.game_name.setText(model.getName());

//       holder.lin_game.setLayoutAnimation(controller);
//       holder.lin_game.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//
//
//               tv_game.setText(model.getName());
//           Fragment fm = null ;
//               if( model.getType().equals("0"))
//               {
//                   fm = new PanaFragment();
//
//                   final Bundle arg = new Bundle();
//                   arg.putString("game_id",model.getId());
//                   arg.putString("game_name",model.getName());
//                   arg.putString("m_id",matka_id);
//                   arg.putString("matka_name",matka_name);
//                   arg.putString("start_time",start_time);
//                   arg.putString("end_time",end_time);
//                   fm.setArguments(arg);
//                   AppCompatActivity activity=(AppCompatActivity) v.getContext();
//                   activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_frame,fm)
//                           .addToBackStack(null)
//                           .commit();
//
//               }
//               else if( model.getType().equals("1"))
//               {
//
//                   final Bundle arg = new Bundle();
//                   arg.putString("game_id",model.getId());
//                   arg.putString("game_name",model.getName());
//                   arg.putString("m_id",matka_id);
//                   arg.putString("matka_name",matka_name);
//                   arg.putString("start_time",start_time);
//                   arg.putString("end_time",end_time);
//
//
//                   switch (model.getName())
//                   {
//                       case  "Half Sangam" :
//                           fm = new HalfSangamFragment();
//                           break;
//                       case  "DP Motor" :
//                       case  "SP Motor" :
//                           fm = new SPMotor();
//                           break;
//                       case  "Cycle Pana" :
//                           fm = new CyclePana();
//                           break;
//                       case "Full Sangam" :
//                           fm = new FullSangamFragmnet();
//                           break;
//                       case "Panel Group" :
//                           fm = new GroupPanel();
//                           break;
//                       case "Group Jodi" :
//                           fm = new GroupJodi();
//                           break;
//                       case "Red Bracket": fm = new RedBracketFragment();
//                           break;
//                       case "Odd Even" : fm = new OddEvenFragment();
//                           break;
////                        case "Single Pana" :
////                         case "Double Pana":
////                            fm = new FragmentDigits();
////                            break;
//
//                       default:  fm = new SelectGameFragment();
//                           break;
//
//
//                   }
//                   fm.setArguments(arg);
//                   AppCompatActivity activity=(AppCompatActivity) v.getContext();
//                   activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_frame,fm)
//                           .addToBackStack(null)
//                           .commit();
//
//               }
//               else
//               {
//                   Intent intent=new Intent(activity, PanaActivity.class);
//                   intent.putExtra("game_id",model.getId());
//                   intent.putExtra("game_name",model.getName());
//                   intent.putExtra("m_id",matka_id);
//                   intent.putExtra("matka_name",matka_name);
//                   intent.putExtra("start_time",start_time);
//                   intent.putExtra("end_time",end_time);
//                   intent.putExtra("start_num",s_num);
//                   intent.putExtra("num",num);
//                   intent.putExtra("end_num",e_num);
//                   activity.startActivity(intent);
//               }
//
//
//           }
//       });


    }

    @Override
    public int getItemCount() {
        return game_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView game_img;
        TextView game_name;
        LinearLayout lin_game;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            game_img = itemView.findViewById(R.id.game_img);
            game_name = itemView.findViewById(R.id.game_name);
            lin_game = itemView.findViewById(R.id.lin_game);


        }
    }
}
