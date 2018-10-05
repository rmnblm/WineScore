package ch.hsr.winescore.ui.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.hsr.winescore.R;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.ui.presenters.WineDetailPresenter;
import ch.hsr.winescore.ui.views.WineDetailView;

public class WineDetailActivity extends AppCompatActivity implements WineDetailView {

    @BindView(R.id.detail_toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton floatingActionButton;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout tbl_appbar;

    @BindView(R.id.vintage) TextView tv_vintage;
    @BindView(R.id.color) TextView tv_color;
    @BindView(R.id.appellation) TextView tv_appellation;
    @BindView(R.id.regions) TextView tv_regions;
    @BindView(R.id.country) TextView tv_country;
    @BindView(R.id.confidence_index) TextView tv_confidenceIndex;
    @BindView(R.id.score) TextView tv_score;
    @BindView(R.id.scoreCircle) ProgressBar pb_score;

    private WineDetailPresenter presenter;
    private Wine wine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wine_detail);
        ButterKnife.bind(this);

        setupPresenter();
        setupActionBar();
        setupFloatingActionButton();
        setupIntentExtras();
        setupViewsWithExtras();
    }

    private void setupFloatingActionButton() {
        floatingActionButton.setOnClickListener(
                view -> Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        );
    }

    private void setupPresenter() {
        presenter = new WineDetailPresenter();
        presenter.attachView(this);
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupIntentExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            wine = (Wine) extras.get("wine");
            System.out.println(wine);
        }
    }

    private void setupViewsWithExtras() {
        tbl_appbar.setTitle(wine.getName());
        tv_vintage.setText(wine.getVintage());
        tv_color.setText(wine.getColor());
        tv_appellation.setText(wine.getAppellation());
        tv_regions.setText(TextUtils.join(",", wine.getRegions()));
        tv_country.setText(wine.getCountry());
        tv_confidenceIndex.setText(wine.getConfidenceIndex());
        tv_score.setText(String.valueOf(wine.getScore()));
        pb_score.setProgress((int) Math.round(wine.getScore()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }
}
