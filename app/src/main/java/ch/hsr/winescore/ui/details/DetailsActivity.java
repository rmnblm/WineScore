package ch.hsr.winescore.ui.details;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.hsr.winescore.R;
import ch.hsr.winescore.domain.models.Comment;
import ch.hsr.winescore.domain.models.Wine;

public class DetailsActivity extends AppCompatActivity implements DetailsView {

    public static final String ARGUMENT_WINE = "wine";

    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout tbl_appbar;
    @BindView(R.id.toolbar_bgimage) ImageView tbl_bgimage;
    @BindView(R.id.detail_toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton floatingActionButton;
    @BindView(R.id.wine_detail_container) View detailContainer;

    @BindView(R.id.appellation) TextView tv_appellation;
    @BindView(R.id.regions) TextView tv_regions;
    @BindView(R.id.country) TextView tv_country;
    @BindView(R.id.confidence_index) TextView tv_confidenceIndex;
    @BindView(R.id.score) TextView tv_score;
    @BindView(R.id.scoreCircle) ProgressBar pb_score;

    @BindView(R.id.ratings_1) TextView tv_ratings_1;
    @BindView(R.id.ratings_2) TextView tv_ratings_2;
    @BindView(R.id.ratings_3) TextView tv_ratings_3;
    @BindView(R.id.ratings_4) TextView tv_ratings_4;
    @BindView(R.id.ratings_5) TextView tv_ratings_5;

    @BindView(R.id.ratings_1_stars) RatingBar rb_ratings_1;
    @BindView(R.id.ratings_2_stars) RatingBar rb_ratings_2;
    @BindView(R.id.ratings_3_stars) RatingBar rb_ratings_3;
    @BindView(R.id.ratings_4_stars) RatingBar rb_ratings_4;
    @BindView(R.id.ratings_5_stars) RatingBar rb_ratings_5;

    @BindView(R.id.myRatingsLayout) View view_my_ratings;
    @BindView(R.id.ratingBar_my_ratings) RatingBar rb_my_rating;
    @BindView(R.id.button_remove_rating) Button btn_remove_rating;

    @BindView(R.id.commentsLastComment) View view_last_comment;
    @BindView(R.id.tv_last_comment) TextView tv_last_comment;

    @OnClick(R.id.commentsLayout)
    public void openCommentsDialog(View v) {
        mDialogFragment.show(getSupportFragmentManager(), mDialogFragment.getTag(), this);
    }

    @OnClick(R.id.fab)
    public void onClickFavorite(View v) {
        floatingActionButton.setEnabled(false);
        if (mIsFavorite) {
            presenter.removeAsFavorite(wine);
        } else {
            presenter.addAsFavorite(wine);
        }
    }

    @OnClick(R.id.button_remove_rating)
    public void onClickRemoveRating(View v) {
        rb_my_rating.setRating(0);
        presenter.removeMyRating(wine);
    }

    private DetailsPresenter presenter;
    private Wine wine;
    private FirebaseUser mUser;
    private boolean mIsFavorite = false;
    private CommentsBottomDialogFragment mDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        setupPresenter();
        setupActionBar();
        setupIntentExtras();
        setupViewsWithExtras();
        setupFloatingActionButton();
        setupRatings();
        setupComments();
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
            wine = (Wine) extras.get(ARGUMENT_WINE);
        }
        mUser = FirebaseAuth.getInstance().getCurrentUser();
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
            case RED: colorResID = R.color.colorWineRed; break;
            case WHITE: colorResID = R.color.colorWineWhite; break;
            case PINK: colorResID = R.color.colorWinePink; break;
        }

        floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(colorResID)));
        pb_score.setProgressTintList(ColorStateList.valueOf(getResources().getColor(colorResID)));
        rb_ratings_1.setProgressTintList(ColorStateList.valueOf(getResources().getColor(colorResID)));
        rb_ratings_1.setSecondaryProgressTintList(ColorStateList.valueOf(getResources().getColor(colorResID)));
        rb_ratings_2.setProgressTintList(ColorStateList.valueOf(getResources().getColor(colorResID)));
        rb_ratings_2.setSecondaryProgressTintList(ColorStateList.valueOf(getResources().getColor(colorResID)));
        rb_ratings_3.setProgressTintList(ColorStateList.valueOf(getResources().getColor(colorResID)));
        rb_ratings_3.setSecondaryProgressTintList(ColorStateList.valueOf(getResources().getColor(colorResID)));
        rb_ratings_4.setProgressTintList(ColorStateList.valueOf(getResources().getColor(colorResID)));
        rb_ratings_4.setSecondaryProgressTintList(ColorStateList.valueOf(getResources().getColor(colorResID)));
        rb_ratings_5.setProgressTintList(ColorStateList.valueOf(getResources().getColor(colorResID)));
        rb_ratings_5.setSecondaryProgressTintList(ColorStateList.valueOf(getResources().getColor(colorResID)));
        rb_my_rating.setProgressTintList(ColorStateList.valueOf(getResources().getColor(colorResID)));
        rb_my_rating.setSecondaryProgressTintList(ColorStateList.valueOf(getResources().getColor(colorResID)));

        int countryResID;
        switch(wine.getCountry().toLowerCase()) {
            case "italy": countryResID = R.drawable.img_it_montepulciano; break;
            case "usa": countryResID = R.drawable.img_us_sonoma; break;
            case "france": countryResID = R.drawable.img_fr_bordeaux; break;
            case "argentina": countryResID = R.drawable.img_arg_calchaqui; break;
            case "new zealand": countryResID = R.drawable.img_nz_centralotago; break;
            default: countryResID = R.drawable.img_grapes; break;
        }

        Glide.with(this).load(countryResID).into(tbl_bgimage);
    }

    private void setupFloatingActionButton() {
        floatingActionButton.hide();
        if (mUser != null) {
            presenter.isFavorite(wine);
        }
    }

    private void setupRatings() {
        presenter.loadRatings(wine);
        if (mUser == null) {
            view_my_ratings.setVisibility(View.GONE);
        } else {
            presenter.loadMyRating(wine);
            rb_my_rating.setOnRatingBarChangeListener(this::onRatingChanged);
        }
    }

    private void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        if (fromUser) {
            presenter.setMyRating(wine, (int) rating);
            btn_remove_rating.setVisibility(View.VISIBLE);
        }
    }

    private void setupComments() {
        view_last_comment.setVisibility(View.GONE);
        loadLastComment();
        mDialogFragment = CommentsBottomDialogFragment.newInstane(wine);
    }

    private void loadLastComment() {
        presenter.loadLastComment(wine);
    }


    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    public void refreshFavoriteButton(boolean isFavorite) {
        mIsFavorite = isFavorite;
        floatingActionButton.setImageResource(mIsFavorite ? R.drawable.ic_unfavorite_black_24dp : R.drawable.ic_favorite_black_24dp);
        floatingActionButton.setEnabled(true);
        floatingActionButton.show();
    }

    @Override
    public void onFavoriteAdded() {
        Snackbar.make(detailContainer, R.string.favorite_added, Snackbar.LENGTH_LONG).show();
        refreshFavoriteButton(true);
    }

    @Override
    public void onFavoriteRemoved() {
        Snackbar.make(detailContainer, R.string.favorite_removed, Snackbar.LENGTH_LONG).show();
        refreshFavoriteButton(false);
    }

    @Override
    public void refreshRatings(SparseIntArray ratings) {
        tv_ratings_1.setText(String.valueOf(ratings.get(1, 0)));
        tv_ratings_2.setText(String.valueOf(ratings.get(2, 0)));
        tv_ratings_3.setText(String.valueOf(ratings.get(3, 0)));
        tv_ratings_4.setText(String.valueOf(ratings.get(4, 0)));
        tv_ratings_5.setText(String.valueOf(ratings.get(5, 0)));
    }

    @Override
    public void showMyRating(int rating) {
        rb_my_rating.setRating(rating);
        btn_remove_rating.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideMyRating() {
        rb_my_rating.setRating(0);
        btn_remove_rating.setVisibility(View.GONE);
    }

    @Override
    public void refreshLastComment(Comment comment) {
        tv_last_comment.setText(getString(R.string.comments_citation, comment.getContent()));
        view_last_comment.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBottomDialogClosed() {
        loadLastComment();
    }
}
