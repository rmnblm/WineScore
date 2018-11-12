package ch.hsr.winescore.ui.profile;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.hsr.winescore.R;
import ch.hsr.winescore.domain.auth.IUser;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment implements ProfileView {

    private static final int RC_SIGN_IN = 100;

    private ProfilePresenter presenter;

    @BindView(R.id.layout) View layout;
    @BindView(R.id.layout_sign_in) View viewSignIn;
    @BindView(R.id.layout_menu) View viewMenu;
    @BindView(R.id.img_profile_picture) ImageView ivProfilePicture;
    @BindView(R.id.tv_displayname) TextView tvDisplayName;
    @BindView(R.id.favorites_count) TextView tvFavoritesCount;
    @BindView(R.id.ratings_count) TextView tvRatingsCount;
    @BindView(R.id.comments_count) TextView tvCommentsCount;

    @BindDrawable(R.drawable.ic_wine_red) Drawable drawWineIcon;
    @BindString(R.string.anonymous) String displayNameAnonymous;
    @BindString(R.string.error_no_internet_connection) String errorNoInternetConnection;
    @BindString(R.string.error_unknown) String errorUnknown;

    @OnClick(R.id.button_sign_in)
    public void onClickSignIn(View v) {
        startActivityForResult(presenter.getSignInIntent(), RC_SIGN_IN);
    }

    @OnClick(R.id.layout_favorites)
    public void onClickFavorites(View v) {
        navigateToList(R.string.favorites_title, "favorisedBy");
    }

    @OnClick(R.id.layout_ratings)
    public void onClickRatings(View v) {
        navigateToList(R.string.ratings_title, "ratedBy");
    }

    @OnClick(R.id.layout_comments)
    public void onClickComments(View v) {
        navigateToList(R.string.comments_title, "commentedBy");
    }

    @OnClick(R.id.button_sign_out)
    public void onClickSignOut(View v) {
        presenter.signOut();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, rootView);

        viewSignIn.setVisibility(View.GONE);
        viewMenu.setVisibility(View.GONE);
        setupPresenter();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.checkAuthentication();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                presenter.checkAuthentication();
            } else {
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if (response == null) {
                    return; // User left auth activity by back button
                }
                Snackbar snackbar = Snackbar.make(
                        layout,
                        response.getError() != null && response.getError().getErrorCode() == ErrorCodes.NO_NETWORK ? errorNoInternetConnection : errorUnknown,
                        Snackbar.LENGTH_INDEFINITE);
                snackbar.getView().setBackgroundResource(R.color.colorErrorMessage);
                snackbar.show();
            }
        }
    }

    private void navigateToList(int title, String queryField) {
        Intent intent = new Intent(getContext(), ListActivity.class);
        intent.putExtra(ListActivity.TITLE, title);
        intent.putExtra(WinesFragment.ARGUMENT_QUERY_FIELD, queryField);
        if (getActivity() != null) {
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onSignedIn(IUser user) {
        if (user.getPhotoUrl() != null) {
            Glide.with(this).load(user.getPhotoUrl()).apply(new RequestOptions().circleCrop()).into(ivProfilePicture);
        }
        presenter.loadCounts();
        viewSignIn.setVisibility(View.GONE);
        viewMenu.setVisibility(View.VISIBLE);
        tvDisplayName.setText(user.isAnonymous() ? displayNameAnonymous : user.getDisplayName());
    }

    @Override
    public void onSignedOut() {
        tvDisplayName.setText(R.string.profile_welcome);
        ivProfilePicture.setImageDrawable(drawWineIcon);
        viewMenu.setVisibility(View.GONE);
        viewSignIn.setVisibility(View.VISIBLE);
    }

    @Override
    public void refreshFavoritesCount(int count) {
        setCount(tvFavoritesCount, count);
    }

    @Override
    public void refreshRatingsCount(int count) {
        setCount(tvRatingsCount, count);
    }

    @Override
    public void refreshCommentsCount(int count) {
        setCount(tvCommentsCount, count);
    }

    private void setCount(TextView view, Integer count) {
        view.setText(count.toString());
        view.setVisibility(View.VISIBLE);
    }

    private void setupPresenter() {
        presenter = new ProfilePresenter();
        presenter.attachView(this);
    }
}
