package ch.hsr.winescore.domain.auth;

import android.content.Intent;

public interface IAuth {
    IUser getCurrentUser();
    void signOut();
    Intent getSignInIntent();
}
