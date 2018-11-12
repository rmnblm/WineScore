package ch.hsr.winescore.domain.auth;

import android.content.Intent;

public class AuthMock implements IAuth {

    public boolean signOutCalled = false;
    public IUser currentUser;

    @Override
    public IUser getCurrentUser() {
        return currentUser;
    }

    @Override
    public void signOut() {
        signOutCalled = true;
    }

    @Override
    public Intent getSignInIntent() {
        return new Intent();
    }
}
