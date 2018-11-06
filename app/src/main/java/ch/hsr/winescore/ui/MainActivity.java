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

@SuppressWarnings("squid:MaximumInheritanceDepth") // AppCompatActivity
public class MainActivity extends AppCompatActivity {

    private static final String SELECTED_FRAGMENT_ID = "selected_fragment_id";

    @BindView(R.id.navigation) BottomNavigationView bottomNavigationView;

    private int selectedFragmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(listener);
        selectedFragmentId = R.id.latest;
        refreshFragment();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(SELECTED_FRAGMENT_ID, selectedFragmentId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        bottomNavigationView.setSelectedItemId(savedInstanceState.getInt(SELECTED_FRAGMENT_ID));
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener listener = item -> {
        if (selectedFragmentId == item.getItemId()) return false;
        selectedFragmentId = item.getItemId();
        refreshFragment();
        return true;
    };

    private void refreshFragment() {
        Fragment selectedFragment;
        switch (selectedFragmentId) {
            case R.id.search:
                selectedFragment = new SearchFragment();
                break;
            case R.id.profile:
                selectedFragment = new ProfileFragment();
                break;
            default:
                selectedFragment = new LatestFragment();
                break;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.commit();
    }
}
