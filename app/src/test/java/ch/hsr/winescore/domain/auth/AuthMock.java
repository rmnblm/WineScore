package ch.hsr.winescore.domain.auth;

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
}
