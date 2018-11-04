package ch.hsr.winescore.ui.details;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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

    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout tblAppbar;
    @BindView(R.id.toolbar_bgimage) ImageView ivBackgroundImage;
    @BindView(R.id.detail_toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fabFavorite;
    @BindView(R.id.wine_detail_container) View viewDetailContainer;

    @BindView(R.id.appellation) TextView tvAppellation;
    @BindView(R.id.regions) TextView tvRegions;
    @BindView(R.id.country) TextView tvCountry;
    @BindView(R.id.confidence_index) TextView tvConfidenceIndex;
    @BindView(R.id.score) TextView tvScore;
    @BindView(R.id.scoreCircle) ProgressBar pbScore;

    @BindView(R.id.ratings_1) TextView tvRatings1;
    @BindView(R.id.ratings_2) TextView tvRatings2;
    @BindView(R.id.ratings_3) TextView tvRatings3;
    @BindView(R.id.ratings_4) TextView tvRatings4;
    @BindView(R.id.ratings_5) TextView tvRatings5;

    @BindView(R.id.ratings_1_stars) RatingBar rbRatings1;
    @BindView(R.id.ratings_2_stars) RatingBar rbRatings2;
    @BindView(R.id.ratings_3_stars) RatingBar rbRatings3;
    @BindView(R.id.ratings_4_stars) RatingBar rbRatings4;
    @BindView(R.id.ratings_5_stars) RatingBar rbRatings5;

    @BindView(R.id.myRatingsLayout) View viewMyRatings;
    @BindView(R.id.ratingBar_my_ratings) RatingBar rbMyRating;
    @BindView(R.id.button_remove_rating) Button btnRemoveRating;

    @BindView(R.id.commentsLastComment) View viewLastComment;
    @BindView(R.id.tv_last_comment) TextView tvLastComment;

    @OnClick(R.id.commentsLayout)
    public void openCommentsDialog(View v) {
        mDialogFragment.show(getSupportFragmentManager(), mDialogFragment.getTag(), this);
    }

    @OnClick(R.id.fab)
    public void onClickFavorite(View v) {
        fabFavorite.setEnabled(false);
        if (mIsFavorite) {
            presenter.removeAsFavorite(wine);
        } else {
            presenter.addAsFavorite(wine);
        }
    }

    @OnClick(R.id.button_remove_rating)
    public void onClickRemoveRating(View v) {
        rbMyRating.setRating(0);
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
        tblAppbar.setTitle(String.format("%s %s", wine.getShortName(), wine.getVintage()));
        tvAppellation.setText(wine.getAppellation());
        tvRegions.setText(TextUtils.join(",", wine.getRegions()));
        tvCountry.setText(wine.getCountry());
        tvConfidenceIndex.setText(wine.getConfidenceIndex());
        tvScore.setText(String.valueOf(wine.getScore()));
        pbScore.setProgress((int) Math.round(wine.getScore()));

        int colorResID = 0;
        switch(wine.getColor()) {
            case RED: colorResID = R.color.colorWineRed; break;
            case WHITE: colorResID = R.color.colorWineWhite; break;
            case PINK: colorResID = R.color.colorWinePink; break;
        }
        int color = ContextCompat.getColor(this, colorResID);
        fabFavorite.setBackgroundTintList(ColorStateList.valueOf(color));
        pbScore.setProgressTintList(ColorStateList.valueOf(color));
        rbRatings1.setProgressTintList(ColorStateList.valueOf(color));
        rbRatings1.setSecondaryProgressTintList(ColorStateList.valueOf(color));
        rbRatings2.setProgressTintList(ColorStateList.valueOf(color));
        rbRatings2.setSecondaryProgressTintList(ColorStateList.valueOf(color));
        rbRatings3.setProgressTintList(ColorStateList.valueOf(color));
        rbRatings3.setSecondaryProgressTintList(ColorStateList.valueOf(color));
        rbRatings4.setProgressTintList(ColorStateList.valueOf(color));
        rbRatings4.setSecondaryProgressTintList(ColorStateList.valueOf(color));
        rbRatings5.setProgressTintList(ColorStateList.valueOf(color));
        rbRatings5.setSecondaryProgressTintList(ColorStateList.valueOf(color));
        rbMyRating.setProgressTintList(ColorStateList.valueOf(color));
        rbMyRating.setSecondaryProgressTintList(ColorStateList.valueOf(color));

        int countryResID;
        switch(wine.getCountry().toLowerCase()) {
            case "italy": countryResID = R.drawable.img_it_montepulciano; break;
            case "usa": countryResID = R.drawable.img_us_sonoma; break;
            case "france": countryResID = R.drawable.img_fr_bordeaux; break;
            case "argentina": countryResID = R.drawable.img_arg_calchaqui; break;
            case "new zealand": countryResID = R.drawable.img_nz_centralotago; break;
            default: countryResID = R.drawable.img_grapes; break;
        }

        Glide.with(this).load(countryResID).into(ivBackgroundImage);
    }

    private void setupFloatingActionButton() {
        fabFavorite.hide();
        if (mUser != null) {
            presenter.isFavorite(wine);
        }
    }

    private void setupRatings() {
        presenter.loadRatings(wine);
        if (mUser == null) {
            viewMyRatings.setVisibility(View.GONE);
        } else {
            presenter.loadMyRating(wine);
            rbMyRating.setOnRatingBarChangeListener((rb, rating, fromUser) -> onRatingChanged(rating, fromUser));
        }
    }

    private void onRatingChanged(float rating, boolean fromUser) {
        if (fromUser) {
            presenter.setMyRating(wine, (int) rating);
            btnRemoveRating.setVisibility(View.VISIBLE);
        }
    }

    private void setupComments() {
        viewLastComment.setVisibility(View.GONE);
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
        fabFavorite.setImageResource(mIsFavorite ? R.drawable.ic_unfavorite_black_24dp : R.drawable.ic_favorite_black_24dp);
        fabFavorite.setEnabled(true);
        fabFavorite.show();
    }

    @Override
    public void onFavoriteAdded() {
        Snackbar.make(viewDetailContainer, R.string.favorite_added, Snackbar.LENGTH_LONG).show();
        refreshFavoriteButton(true);
    }

    @Override
    public void onFavoriteRemoved() {
        Snackbar.make(viewDetailContainer, R.string.favorite_removed, Snackbar.LENGTH_LONG).show();
        refreshFavoriteButton(false);
    }

    @Override
    public void refreshRatings(SparseIntArray ratings) {
        tvRatings1.setText(String.valueOf(ratings.get(1, 0)));
        tvRatings2.setText(String.valueOf(ratings.get(2, 0)));
        tvRatings3.setText(String.valueOf(ratings.get(3, 0)));
        tvRatings4.setText(String.valueOf(ratings.get(4, 0)));
        tvRatings5.setText(String.valueOf(ratings.get(5, 0)));
    }

    @Override
    public void showMyRating(int rating) {
        rbMyRating.setRating(rating);
        btnRemoveRating.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideMyRating() {
        rbMyRating.setRating(0);
        btnRemoveRating.setVisibility(View.GONE);
    }

    @Override
    public void refreshLastComment(Comment comment) {
        tvLastComment.setText(getString(R.string.comments_citation, comment.getContent()));
        viewLastComment.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBottomDialogClosed() {
        loadLastComment();
    }
}
