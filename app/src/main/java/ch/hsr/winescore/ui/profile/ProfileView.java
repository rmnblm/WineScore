package ch.hsr.winescore.ui.profile;

import ch.hsr.winescore.domain.auth.IUser;
import ch.hsr.winescore.ui.utils.View;

public interface ProfileView extends View {
    void onSignedIn(IUser user);
    void onSignedOut();
    void refreshFavoritesCount(int count);
    void refreshRatingsCount(int count);
    void refreshCommentsCount(int count);
}
