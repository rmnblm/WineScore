package ch.hsr.winescore.ui.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private static final String TAG = ProfileFragment.class.getSimpleName();
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
        Log.d(TAG, "Navigate to Favorites");
    }

    @OnClick(R.id.layout_ratings)
    public void onClickRatings(View v) {
        Log.d(TAG, "Navigate to Ratings");
    }

    @OnClick(R.id.layout_comments)
    public void onClickComments(View v) {
        Log.d(TAG, "Navigate to Comments");
    }

    @OnClick(R.id.button_sign_out)
    public void onClickSignOut(View v) {
        mAuth.signOut();
        onSignedOut();
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
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

    private void onSignedIn(FirebaseUser user) {
        if (user.getPhotoUrl() != null) {
            Glide.with(this).load(user.getPhotoUrl()).apply(new RequestOptions().circleCrop()).into(mProfilePicture);
        }
        mLayoutSignIn.setVisibility(View.GONE);
        mLayoutMenu.setVisibility(View.VISIBLE);
        mDisplayName.setText(user.isAnonymous() ? mDisplayNameAnonymous : user.getDisplayName());
    }

    private void onSignedOut() {
        mDisplayName.setText(R.string.profile_welcome);
        mProfilePicture.setImageDrawable(mWineIcon);
        mLayoutMenu.setVisibility(View.GONE);
        mLayoutSignIn.setVisibility(View.VISIBLE);
    }
}
