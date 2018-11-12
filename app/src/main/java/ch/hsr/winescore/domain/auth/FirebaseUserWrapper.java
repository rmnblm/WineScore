package ch.hsr.winescore.domain.auth;

import android.net.Uri;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseUserWrapper implements IUser {

    private final FirebaseUser user;

    public FirebaseUserWrapper(FirebaseUser user) {
        this.user = user;
    }

    @Override
    public boolean isNull() {
        return user == null;
    }

    public Uri getPhotoUrl() {
        return user != null ? user.getPhotoUrl() : null;
    }

    @Override
    public String getDisplayName() {
        return user != null ? user.getDisplayName() : null;
    }

    @Override
    public boolean isAnonymous() {
        return user == null || user.isAnonymous();
    }


}
