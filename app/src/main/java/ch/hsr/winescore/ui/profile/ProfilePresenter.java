package ch.hsr.winescore.ui.profile;

import android.content.Intent;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;

import ch.hsr.winescore.R;
import ch.hsr.winescore.data.repositories.CommentsFirebaseRepository;
import ch.hsr.winescore.data.repositories.FavoritesFirebaseRepository;
import ch.hsr.winescore.data.repositories.RatingsFirebaseRepository;
import ch.hsr.winescore.domain.auth.FirebaseAuthWrapper;
import ch.hsr.winescore.domain.auth.IAuth;
import ch.hsr.winescore.domain.auth.IUser;
import ch.hsr.winescore.ui.utils.Presenter;

public class ProfilePresenter implements Presenter<ProfileView> {

    private ProfileView view;
    private IAuth auth;

    public ProfilePresenter() {
        this(new FirebaseAuthWrapper());
    }

    public ProfilePresenter(IAuth auth) {
        this.auth = auth;
    }

    @Override
    public void attachView(ProfileView view) {
        this.view = view;
    }

    public void checkAuthentication() {
        IUser currentUser = auth.getCurrentUser();
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
