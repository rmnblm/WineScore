package ch.hsr.winescore.ui.activities;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.hsr.winescore.R;
import ch.hsr.winescore.ui.fragments.LatestFragment;
import ch.hsr.winescore.ui.fragments.ProfileFragment;
import ch.hsr.winescore.ui.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.navigation) BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(listener);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new LatestFragment());
        transaction.commit();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener listener = item -> {
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.latest:
                selectedFragment = new LatestFragment();
                break;
            case R.id.search:
                selectedFragment = new SearchFragment();
                break;
            case R.id.profile:
                selectedFragment = new ProfileFragment();
                break;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.commit();
        return true;
    };
}
