package ch.hsr.winescore;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class WineScoreApplication extends Application {

    private static WineScoreApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder(firestore.getFirestoreSettings())
                .setTimestampsInSnapshotsEnabled(true).build();
        firestore.setFirestoreSettings(settings);
    }

    public static WineScoreApplication getApplicationInstance() {
        return instance;
    }

    public static SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(instance.getBaseContext());
    }

    public static String getResourcesString(int resId) {
        return instance.getString(resId);
    }

    private static void setInstance(WineScoreApplication applicationInstance) {
        instance = applicationInstance;
    }
}