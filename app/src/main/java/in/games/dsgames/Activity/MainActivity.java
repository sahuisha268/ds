package in.games.dsgames.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import in.games.dsgames.Fragment.HistoryFragment;
import in.games.dsgames.Fragment.HomeFragment;
import in.games.dsgames.Fragment.ProfileFragment;
import in.games.dsgames.R;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        Fragment fm = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment,fm).commit();
    navView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fm = null ;
        if (item.getItemId()==R.id.navigation_home)
        {
           fm= new HomeFragment();

        }
        else if (item.getItemId()==R.id.navigation_histry)
        {
            fm = new HistoryFragment();
        }
        else if (item.getItemId() ==R.id.navigation_profile)
        {
            fm = new ProfileFragment();
        }
        if(fm!=null){

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, fm)
                    .addToBackStack(null)
                    .commit();


        }

        return true;
    }
}