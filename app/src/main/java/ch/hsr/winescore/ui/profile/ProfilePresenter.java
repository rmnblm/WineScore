package ch.hsr.winescore.ui.profile;

import android.content.Intent;
import ch.hsr.winescore.data.repositories.*;
import ch.hsr.winescore.domain.auth.FirebaseAuthWrapper;
import ch.hsr.winescore.domain.auth.IAuth;
import ch.hsr.winescore.domain.auth.IUser;
import ch.hsr.winescore.ui.utils.Presenter;

public class ProfilePresenter implements Presenter<ProfileView> {

    private ProfileView view;
    private IAuth auth;
    private ICommentsRepository commentsRepo;
    private IFavoritesRepository favoritesRepo;
    private IRatingsRepository ratingsRepo;

    public ProfilePresenter() {
        this(new FirebaseAuthWrapper(),
                new CommentsFirebaseRepository(),
                new FavoritesFirebaseRepository(),
                new RatingsFirebaseRepository());
    }

    public ProfilePresenter(
            IAuth auth,
            ICommentsRepository commentsRepo,
            IFavoritesRepository favoritesRepo,
            IRatingsRepository ratingsRepo
    ) {
        this.auth = auth;
        this.commentsRepo = commentsRepo;
        this.favoritesRepo = favoritesRepo;
        this.ratingsRepo = ratingsRepo;
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
        return auth.getSignInIntent();
    }

    public void signOut() {
        auth.signOut();
        view.onSignedOut();
    }

    public void loadCounts() {
        favoritesRepo.getCount(count -> view.refreshFavoritesCount(count));
        ratingsRepo.getCount(count -> view.refreshRatingsCount(count));
        commentsRepo.getCount(count -> view.refreshCommentsCount(count));
    }
}
