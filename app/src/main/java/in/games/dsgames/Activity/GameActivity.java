package in.games.dsgames.Activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.tabs.TabLayout;

import in.games.dsgames.Config.Common;
import in.games.dsgames.Fragment.CrossingFragment;
import in.games.dsgames.Fragment.JantariFragment;
import in.games.dsgames.Fragment.OpenSheetFragment;
import in.games.dsgames.R;
import in.games.dsgames.utils.Custom_ViewPager;
import in.games.dsgames.utils.LoadingBar;


public class GameActivity extends AppCompatActivity {
    private final String TAG= GameActivity.class.getSimpleName();
    Common common;
    LoadingBar loadingBar;
    TabLayout tabLayout;
    Custom_ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initViews();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("matka_name"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.pager);
        tabLayout.addTab(tabLayout.newTab().setText("OPEN SHEET"));
        tabLayout.addTab(tabLayout.newTab().setText("JANTARI"));
        tabLayout.addTab(tabLayout.newTab().setText("CROSSING"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager.setPagingEnabled(false);
        ViewPagerAdapter pagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
       viewPager.setAdapter(pagerAdapter);
        wrapTabIndicatorToTitle(tabLayout,10,10);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        int mNumOfTabs;
        public ViewPagerAdapter(@NonNull FragmentManager fm, int mNumOfTabs) {
            super(fm);
            this.mNumOfTabs=mNumOfTabs;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    OpenSheetFragment tab1=new OpenSheetFragment();
                    return tab1;
                case 1:
                    JantariFragment tab2=new JantariFragment();
                    return tab2;
                case 2:
                    CrossingFragment tab3=new CrossingFragment();
                    return tab3;
            }
            return null;
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

    public void wrapTabIndicatorToTitle(TabLayout tabLayout, int externalMargin, int internalMargin) {
        View tabStrip = tabLayout.getChildAt(0);
        if (tabStrip instanceof ViewGroup) {
            ViewGroup tabStripGroup = (ViewGroup) tabStrip;
            int childCount = ((ViewGroup) tabStrip).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View tabView = tabStripGroup.getChildAt(i);
                tabView.setMinimumWidth(0);
                tabView.setPadding(0, tabView.getPaddingTop(), 0, tabView.getPaddingBottom());
                if (tabView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tabView.getLayoutParams();
                    if (i == 0) {
                        setMargin(layoutParams, externalMargin, internalMargin);
                    } else if (i == childCount - 1) {
                        setMargin(layoutParams, internalMargin, externalMargin);
                    } else {
                        setMargin(layoutParams, internalMargin, internalMargin);
                    }
                }
            }

            tabLayout.requestLayout();
        }
    }

    private void setMargin(ViewGroup.MarginLayoutParams layoutParams, int start, int end) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.setMarginStart(start);
            layoutParams.setMarginEnd(end);
        } else {
            layoutParams.leftMargin = start;
            layoutParams.rightMargin = end;
        }
    }
}