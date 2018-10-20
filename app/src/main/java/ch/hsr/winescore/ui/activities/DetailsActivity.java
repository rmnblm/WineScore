package ch.hsr.winescore.ui.activities;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.hsr.winescore.R;
import ch.hsr.winescore.model.Favorite;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.ui.datasources.FavoritesFirebaseRepository;
import ch.hsr.winescore.ui.presenters.DetailsPresenter;
import ch.hsr.winescore.ui.views.DetailsView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

public class DetailsActivity extends AppCompatActivity implements DetailsView {

    private static final String TAG = DetailsActivity.class.getSimpleName();

    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout tbl_appbar;
    @BindView(R.id.toolbar_bgimage) ImageView tbl_bgimage;
    @BindView(R.id.detail_toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton floatingActionButton;

    @BindView(R.id.appellation) TextView tv_appellation;
    @BindView(R.id.regions) TextView tv_regions;
    @BindView(R.id.country) TextView tv_country;
    @BindView(R.id.confidence_index) TextView tv_confidenceIndex;
    @BindView(R.id.score) TextView tv_score;
    @BindView(R.id.scoreCircle) ProgressBar pb_score;

    private DetailsPresenter presenter;
    private Wine wine;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        setupPresenter();
        setupActionBar();
        setupFloatingActionButton();
        setupIntentExtras();
        setupViewsWithExtras();
    }

    private void setupFloatingActionButton() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser == null) {
            floatingActionButton.hide();
        } else {
            floatingActionButton.show();
            floatingActionButton.setOnClickListener(view -> {
                FavoritesFirebaseRepository.add(wine).addOnCompleteListener(x -> {
                    Log.d(TAG, "New favorite: " + wine.getId());
                });
            });
        }
    }

    private void setupPresenter() {
        presenter = new DetailsPresenter();
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
        tbl_appbar.setTitle(String.format("%s %s", wine.getShortName(), wine.getVintage()));
        tv_appellation.setText(wine.getAppellation());
        tv_regions.setText(TextUtils.join(",", wine.getRegions()));
        tv_country.setText(wine.getCountry());
        tv_confidenceIndex.setText(wine.getConfidenceIndex());
        tv_score.setText(String.valueOf(wine.getScore()));
        pb_score.setProgress((int) Math.round(wine.getScore()));

        int colorResID = 0;
        switch(wine.getColor()) {
            case Red: colorResID = R.color.colorWineRed; break;
            case White: colorResID = R.color.colorWineWhite; break;
            case Pink: colorResID = R.color.colorWinePink; break;
        }

        floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(colorResID)));

        int countryResID = 0;
        switch(wine.getCountry().toLowerCase()) {
            case "italy": countryResID = R.drawable.img_it_montepulciano; break;
            case "usa": countryResID = R.drawable.img_us_sonoma; break;
            case "france": countryResID = R.drawable.img_fr_bordeaux; break;
            case "argentina": countryResID = R.drawable.img_arg_calchaqui; break;
        }

        Glide.with(this).load(countryResID).into(tbl_bgimage);
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }
}
