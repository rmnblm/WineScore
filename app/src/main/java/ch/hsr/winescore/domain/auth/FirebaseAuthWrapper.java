package ch.hsr.winescore.domain.auth;

import android.content.Intent;
import ch.hsr.winescore.R;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

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

    @Override
    public Intent getSignInIntent() {
        return AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.GoogleBuilder().build(),
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.AnonymousBuilder().build()
                ))
                .setIsSmartLockEnabled(false)
                .setTheme(R.style.AppTheme)
                .build();
    }

}
