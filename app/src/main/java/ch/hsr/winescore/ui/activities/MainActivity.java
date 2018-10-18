package ch.hsr.winescore.ui.activities;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.hsr.winescore.R;
import ch.hsr.winescore.ui.fragments.ExploreFragment;
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
    }

    private BottomNavigationView.OnNavigationItemSelectedListener listener = item -> {
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.wines:
                selectedFragment = ExploreFragment.newInstance();
                break;
            case R.id.my_favorites:
                selectedFragment = SearchFragment.newInstance();
                break;
            case R.id.profile:
                selectedFragment = ProfileFragment.newInstance();
                break;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.commit();
        return true;
    };
}
