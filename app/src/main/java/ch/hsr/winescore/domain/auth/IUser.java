package ch.hsr.winescore.domain.auth;

import android.net.Uri;

public interface IUser {
    boolean isNull();
    Uri getPhotoUrl();
    String getDisplayName();
    boolean isAnonymous();
}
