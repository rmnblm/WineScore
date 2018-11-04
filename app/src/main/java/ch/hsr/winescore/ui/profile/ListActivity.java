package ch.hsr.winescore.ui.profile;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import ch.hsr.winescore.R;
import ch.hsr.winescore.ui.wine.WinesFragment;

@SuppressWarnings("squid:MaximumInheritanceDepth") // AppCompatActivity
public class ListActivity extends AppCompatActivity {

    public static final String TITLE = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        setTitle(intent.getIntExtra(TITLE, R.string.favorites_title));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, WinesFragment.newInstance(intent.getStringExtra(WinesFragment.ARGUMENT_QUERY_FIELD)));
        transaction.commit();
    }
}
