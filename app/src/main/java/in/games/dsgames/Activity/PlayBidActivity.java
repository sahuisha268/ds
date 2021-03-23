package in.games.dsgames.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import in.games.dsgames.Config.Common;
import in.games.dsgames.Fragment.ChoiceFragment;
import in.games.dsgames.Fragment.DigitFragment;
import in.games.dsgames.Fragment.JodiDigitFragment;
import in.games.dsgames.Fragment.MotorFragment;
import in.games.dsgames.Fragment.PattiFragment;
import in.games.dsgames.Fragment.PattiFragments.DoublePattiFragment;
import in.games.dsgames.Fragment.PattiFragments.SinglePattiFragment;
import in.games.dsgames.R;
import in.games.dsgames.utils.LoadingBar;
import in.games.dsgames.utils.Session_management;

import static in.games.dsgames.Config.Constants.KEY_ID;

public class PlayBidActivity extends AppCompatActivity implements View.OnClickListener {
  ImageView iv_back,iv_reload;
  public static  TextView tv_title ,tv_wallet;
  Session_management session_management ;
  LoadingBar loadingBar ;
Common common;
    Fragment fm = null;
    String m_id="";
Activity ctx =PlayBidActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_bid);
        initViews();
    }
    void initViews()
    {
        session_management = new Session_management(ctx);
        common = new Common(ctx);
        loadingBar = new LoadingBar(ctx);
        iv_back = findViewById(R.id.iv_back);
        iv_reload= findViewById(R.id.iv_reload);
        tv_title= findViewById(R.id.tv_Title);
        tv_wallet= findViewById(R.id.tv_wallet);
        m_id = getIntent().getStringExtra("m_id");

        if (Integer.parseInt(getIntent().getStringExtra("m_id"))>20)
        {
            tv_title.setText("Starline");
        }else {
            tv_title.setText(""+getIntent().getStringExtra("matka_name"));
        }


        Bundle args = new Bundle();
        args.putString("game", getIntent().getStringExtra("game"));
        args.putString("game_id", getIntent().getStringExtra("game_id"));
        args.putString("m_id", getIntent().getStringExtra("m_id"));
        args.putString("matka_name", getIntent().getStringExtra("matka_name"));
        args.putString("start_time",  getIntent().getStringExtra("start_time"));
        args.putString("end_time",  getIntent().getStringExtra("end_time"));
        args.putString("start_num",  getIntent().getStringExtra("start_num"));
        args.putString ("num",  getIntent().getStringExtra("num"));
        args.putString("end_num",  getIntent().getStringExtra("end_num"));


        switch (getIntent().getStringExtra("game"))
        {
            case"single digit":
            case "triple patti":
                fm = new DigitFragment();

                break;
            case "choice":
                fm = new ChoiceFragment();
                break;
            case "jodi":
                fm=new PattiFragment();
                break;
            case "single patti":
                fm = new SinglePattiFragment();
                break;
            case "double patti":
                fm = new DoublePattiFragment();
                break;
            case "red bracket":
                fm = new PattiFragment();
                break;
            case "jodi digit":
                fm = new JodiDigitFragment();
                break;

            case "sp motor":
            case"dp motor":
                fm = new MotorFragment();
                break;


        }
        if(fm!=null){
            fm.setArguments(args);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.bid_frame, fm)
                    .commit();


        }
iv_back.setOnClickListener(this);
        Log.e("user_id",session_management.getUserDetails().get(KEY_ID));
        common.setWallet_Amount(tv_wallet,loadingBar,session_management.getUserDetails().get(KEY_ID));


    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.iv_back)
        {
            finish();
        }
    }
}