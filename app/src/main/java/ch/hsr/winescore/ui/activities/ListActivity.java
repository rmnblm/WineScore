package ch.hsr.winescore.ui.activities;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import ch.hsr.winescore.R;
import ch.hsr.winescore.ui.fragments.ListFragment;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, ListFragment.newInstance(
                intent.getIntExtra(ListFragment.TITLE, R.string.favorites_title),
                intent.getStringExtra(ListFragment.QUERY_FIELD)));
        transaction.commit();
    }
}
