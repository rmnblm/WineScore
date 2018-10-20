package ch.hsr.winescore.ui.activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import ch.hsr.winescore.R;
import ch.hsr.winescore.model.ListType;
import ch.hsr.winescore.ui.fragments.ExploreFragment;
import ch.hsr.winescore.ui.fragments.FavoritesFragment;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ListType type = (ListType) getIntent().getSerializableExtra(ListType.class.getSimpleName());
        switch (type) {
            case FAVORITES:
                transaction.replace(R.id.frame_layout, FavoritesFragment.newInstance());
                break;
            case RATINGS:
                // transaction.replace(R.id.frame_layout, RatingsFragment.newInstance());
                // break;
            case COMMENTS:
                // transaction.replace(R.id.frame_layout, CommentsFragment.newInstance());
                // break;
            default:
                finish();
        }
        transaction.commit();
    }
}
