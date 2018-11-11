package ch.hsr.winescore.domain.auth;

import android.net.Uri;

public class UserMock implements IUser {

    private String displayName;
    private Uri photoUrl;
    private boolean isAnonymous;

    public UserMock(String displayName, Uri photoUrl, boolean isAnonymous) {
        this.displayName = displayName;
        this.photoUrl = photoUrl;
        this.isAnonymous = isAnonymous;
    }

    @Override
    public Uri getPhotoUrl() {
        return photoUrl;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public boolean isAnonymous() {
        return isAnonymous;
    }
}
