package ch.hsr.winescore.ui.profile;

import android.content.Intent;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import ch.hsr.winescore.R;
import ch.hsr.winescore.data.repositories.CommentsFirebaseRepository;
import ch.hsr.winescore.data.repositories.FavoritesFirebaseRepository;
import ch.hsr.winescore.data.repositories.RatingsFirebaseRepository;
import ch.hsr.winescore.ui.utils.Presenter;

public class ProfilePresenter implements Presenter<ProfileView> {

    private ProfileView view;
    private FirebaseAuth auth;

    @Override
    public void attachView(ProfileView view) {
        this.view = view;
        this.auth = FirebaseAuth.getInstance();
    }

    public void checkAuthentication() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            view.onSignedIn(currentUser);
        } else {
            view.onSignedOut();
        }
    }

    public Intent getSignInIntent() {
        return AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.GoogleBuilder().build(),
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.AnonymousBuilder().build()
                ))
                .setIsSmartLockEnabled(false)
                .setTheme(R.style.AppTheme)
                .build();
    }

    public void signOut() {
        auth.signOut();
        view.onSignedOut();
    }

    public void loadCounts() {
        FavoritesFirebaseRepository.getCount(count -> view.refreshFavoritesCount(count));
        RatingsFirebaseRepository.getCount(count -> view.refreshRatingsCount(count));
        CommentsFirebaseRepository.getCount(count -> view.refreshCommentsCount(count));
    }
}
