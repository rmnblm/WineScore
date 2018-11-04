package ch.hsr.winescore.ui.profile;

import com.google.firebase.auth.FirebaseUser;
import ch.hsr.winescore.ui.utils.View;

public interface ProfileView extends View {
    void onSignedIn(FirebaseUser user);
    void onSignedOut();
    void refreshFavoritesCount(int count);
    void refreshRatingsCount(int count);
    void refreshCommentsCount(int count);
}
