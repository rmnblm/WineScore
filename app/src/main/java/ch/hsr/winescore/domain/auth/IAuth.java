package ch.hsr.winescore.domain.auth;

public interface IAuth {
    IUser getCurrentUser();
    void signOut();
}
