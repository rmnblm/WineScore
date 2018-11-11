package ch.hsr.winescore.domain.auth;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthWrapper implements IAuth {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    public IUser getCurrentUser() {
        return new FirebaseUserWrapper(auth.getCurrentUser());
    }

    @Override
    public void signOut() {
        auth.signOut();
    }
}
