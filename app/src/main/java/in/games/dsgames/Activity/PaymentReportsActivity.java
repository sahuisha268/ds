package in.games.dsgames.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import in.games.dsgames.R;

public class PaymentReportsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView tv_title ,tv_wallet;
    ImageView iv_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_reports);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.myViewPager);
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_Title);
        tv_wallet = findViewById(R.id.tv_wallet);
        tv_wallet.setVisibility(View.GONE);
        tv_title.setText("Payment Reports");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//       setSupportActionBar(toolbar);
//        setViewPager(viewPager);
        // setSupportActionBar(toolbar);
        //setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);


    }
//
//    private void setViewPager(ViewPager viewPagerpayment) {
//        PaymentReportsViewPagerAdapter paymentReportsViewPagerAdapter= new PaymentReportsViewPagerAdapter(getSupportFragmentManager());
//       paymentReportsViewPagerAdapter.addFragment(new AccountStatementFragment(),"ACCOUNT STATEMENT");
//        paymentReportsViewPagerAdapter.addFragment(new RequestsFragment(),"REQUESTS");
//        viewPagerpayment.setAdapter(paymentReportsViewPagerAdapter);
//    }

}