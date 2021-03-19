package in.games.dsgames.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import in.games.dsgames.AppController;
import in.games.dsgames.Config.Module;
import in.games.dsgames.R;
import in.games.dsgames.utils.CustomVolleyJsonArrayRequest;
import in.games.dsgames.utils.LoadingBar;

import static in.games.dsgames.Config.BaseUrl.URL_PLAY;

public class HowToPlayActivity extends AppCompatActivity {
TextView textView_How_To_Play;
    TextView tv_title ,tv_wallet,txtLink;
    ImageView iv_back;
    LoadingBar loadingBar;
    Module module ;
    RelativeLayout rel_click;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_Title);
//        tv_wallet = findViewById(R.id.tv_wallet);
//        tv_wallet.setVisibility(View.GONE);
//        tv_title.setText("How To Play");
        loadingBar = new LoadingBar(HowToPlayActivity.this);
        module = new Module(HowToPlayActivity.this);
        textView_How_To_Play=findViewById(R.id.tv_How_to_Play);
        txtLink=(TextView) findViewById(R.id.link);
        rel_click=(RelativeLayout)findViewById(R.id.rel_click);

        rel_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String h= txtLink.getText().toString().trim();
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(h));
                startActivity(intent);
//               String h= iv_Link.toString().trim();
//
//                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(h));
//                 startActivity(intent);

//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/"));
//                startActivity(browserIntent);

            }
        });

        getHowToPlay();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }
    private void getHowToPlay() {
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
      CustomVolleyJsonArrayRequest customVolleyJsonArrayRequest=new CustomVolleyJsonArrayRequest(Request.Method.GET, URL_PLAY, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("Response",response.toString());
                loadingBar.dismiss();
                        if (response.length()!=0) {
                            try {
                                JSONObject obj = response.getJSONObject(0);
                                String resp = obj.getString("data");
                                String str = obj.getString("link");
                                textView_How_To_Play.setText(Html.fromHtml(resp));
                                txtLink.setText(Html.fromHtml(str));

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                Log.e("response", "onResponse: "+ response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                module.showToast(module.VolleyErrorMessage(error));
            }
        });
        AppController.getInstance().addToRequestQueue(customVolleyJsonArrayRequest);
    }
    }
