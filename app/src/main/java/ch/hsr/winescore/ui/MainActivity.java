package ch.hsr.winescore.ui;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.hsr.winescore.R;
import ch.hsr.winescore.ui.latest.LatestFragment;
import ch.hsr.winescore.ui.profile.ProfileFragment;
import ch.hsr.winescore.ui.search.SearchFragment;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.navigation) BottomNavigationView bottomNavigationView;

    private int selectedFragmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(listener);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new LatestFragment());
        transaction.commit();

        selectedFragmentId = R.id.latest;
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener listener = item -> {
        if (selectedFragmentId == item.getItemId()) return false;

        selectedFragmentId = item.getItemId();

        Fragment selectedFragment = null;
        switch (selectedFragmentId) {
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
