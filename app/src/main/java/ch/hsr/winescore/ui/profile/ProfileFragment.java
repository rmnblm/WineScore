package ch.hsr.winescore.ui.profile;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ch.hsr.winescore.ui.wine.WinesFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.hsr.winescore.R;
import ch.hsr.winescore.data.repositories.CommentsFirebaseRepository;
import ch.hsr.winescore.data.repositories.FavoritesFirebaseRepository;
import ch.hsr.winescore.data.repositories.RatingsFirebaseRepository;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private static final int RC_SIGN_IN = 100;
    private FirebaseAuth mAuth;

    @BindView(R.id.layout)
    View mLayout;
    @BindView(R.id.layout_sign_in)
    View mLayoutSignIn;
    @BindView(R.id.layout_menu)
    View mLayoutMenu;
    @BindView(R.id.img_profile_picture)
    ImageView mProfilePicture;
    @BindView(R.id.tv_displayname)
    TextView mDisplayName;
    @BindView(R.id.favorites_count)
    TextView mFavoritesCount;
    @BindView(R.id.ratings_count)
    TextView mRatingsCount;
    @BindView(R.id.comments_count)
    TextView mCommentsCount;

    @BindDrawable(R.drawable.ic_wine_red)
    Drawable mWineIcon;
    @BindString(R.string.anonymous)
    String mDisplayNameAnonymous;
    @BindString(R.string.error_no_internet_connection)
    String mErrorNoInternetConnection;
    @BindString(R.string.error_unknown)
    String mErrorUnknown;

    @OnClick(R.id.button_sign_in)
    public void onClickSignIn(View v) {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build(),
                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                new AuthUI.IdpConfig.AnonymousBuilder().build()
                        ))
                        .setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN);
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
        mAuth.signOut();
        onSignedOut();
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, rootView);
        mLayoutSignIn.setVisibility(View.GONE);
        mLayoutMenu.setVisibility(View.GONE);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            onSignedIn(currentUser);
        } else {
            onSignedOut();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                onSignedIn(mAuth.getCurrentUser());
            } else {
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if (response == null) {
                    return; // User left auth activity by back button
                }
                Snackbar snackbar = Snackbar.make(
                        mLayout,
                        response.getError().getErrorCode() == ErrorCodes.NO_NETWORK ? mErrorNoInternetConnection : mErrorUnknown,
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
        getActivity().startActivity(intent);
    }

    private void onSignedIn(FirebaseUser user) {
        if (user.getPhotoUrl() != null) {
            Glide.with(this).load(user.getPhotoUrl()).apply(new RequestOptions().circleCrop()).into(mProfilePicture);
        }
        mLayoutSignIn.setVisibility(View.GONE);
        mLayoutMenu.setVisibility(View.VISIBLE);
        mDisplayName.setText(user.isAnonymous() ? mDisplayNameAnonymous : user.getDisplayName());
        loadCounts();
    }

    private void onSignedOut() {
        mDisplayName.setText(R.string.profile_welcome);
        mProfilePicture.setImageDrawable(mWineIcon);
        mLayoutMenu.setVisibility(View.GONE);
        mLayoutSignIn.setVisibility(View.VISIBLE);
    }

    private void loadCounts() {
        FavoritesFirebaseRepository.getCount(count -> setCount(mFavoritesCount, count));
        RatingsFirebaseRepository.getCount(count -> setCount(mRatingsCount, count));
        CommentsFirebaseRepository.getCount(count -> setCount(mCommentsCount, count));
    }

    private void setCount(TextView view, Integer count) {
        view.setText(count.toString());
        view.setVisibility(View.VISIBLE);
    }
}
