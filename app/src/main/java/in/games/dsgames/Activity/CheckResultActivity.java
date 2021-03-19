package in.games.dsgames.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import in.games.dsgames.R;

public class CheckResultActivity extends AppCompatActivity {
    ImageView iv_back;
    TextView tv_title ,tv_wallet;
    Spinner spn_bazar, spn_rep_bazar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_result);
        tv_title = findViewById(R.id.tv_Title);
        tv_wallet = findViewById(R.id.tv_wallet);
        iv_back = findViewById(R.id.iv_back);
        spn_bazar = findViewById(R.id.sp_bazar);
        spn_rep_bazar= findViewById(R.id.sp_respective_Bazar);
        tv_title.setText("Check Results");
        tv_wallet.setVisibility(View.GONE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CheckResultActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Bazar));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_bazar.setAdapter(adapter);


        ArrayAdapter<String> adp = new ArrayAdapter<String>(CheckResultActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.RespectiveBazar));
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_rep_bazar.setAdapter(adp);
    }
}