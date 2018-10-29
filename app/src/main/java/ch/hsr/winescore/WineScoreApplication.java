package ch.hsr.winescore;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class WineScoreApplication extends Application {

    private static WineScoreApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder(firestore.getFirestoreSettings())
                .setTimestampsInSnapshotsEnabled(true).build();
        firestore.setFirestoreSettings(settings);
    }

    public static WineScoreApplication getApplicationInstance() {
        return instance;
    }

    public static boolean hasNetwork() {
        return instance.isNetworkAvailable();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo != null) && activeNetworkInfo.isConnected();
    }
}