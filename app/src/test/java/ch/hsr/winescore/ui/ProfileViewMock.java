package ch.hsr.winescore.ui;

import ch.hsr.winescore.domain.auth.IUser;
import ch.hsr.winescore.ui.profile.ProfileView;

public class ProfileViewMock implements ProfileView {

    public boolean onSignedInCalled = false;
    public IUser onSignInUser;
    public boolean onSignedOutCalled = false;
    public boolean refreshFavoritesCountCalled = false;
    public boolean refreshRatingsCountCalled = false;
    public boolean refreshCommentsCountCalled = false;

    @Override
    public void onSignedIn(IUser user) {
        onSignedInCalled = true;
        onSignInUser = user;
    }

    @Override
    public void onSignedOut() {
        onSignedOutCalled = true;
    }

    @Override
    public void refreshFavoritesCount(int count) {
        refreshFavoritesCountCalled = true;
    }

    @Override
    public void refreshRatingsCount(int count) {
        refreshRatingsCountCalled = true;
    }

    @Override
    public void refreshCommentsCount(int count) {
        refreshCommentsCountCalled = true;
    }
}
