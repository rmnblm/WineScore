package ch.hsr.winescore.domain.auth;

import android.net.Uri;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseUserWrapper implements IUser {

    private final FirebaseUser user;

    public FirebaseUserWrapper(FirebaseUser user) {
        this.user = user;
    }

    public Uri getPhotoUrl() {
        return user.getPhotoUrl();
    }

    @Override
    public String getDisplayName() {
        return user.getDisplayName();
    }

    @Override
    public boolean isAnonymous() {
        return user.isAnonymous();
    }


}
