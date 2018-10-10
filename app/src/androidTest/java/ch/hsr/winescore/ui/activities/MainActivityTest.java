package ch.hsr.winescore.ui.activities;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.*;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> intentsTestRule = new ActivityTestRule<MainActivity>(MainActivity.class) {
        @Override
        public void beforeActivityLaunched() {
            // MainActivity launches an activity in the onCreate() method
            // Thus, Intents has to be initialized beforehand
            Intents.init();
            Instrumentation.ActivityResult result =
                    new Instrumentation.ActivityResult(Activity.RESULT_OK, new Intent());
            intending(allOf(
                    hasComponent(WineOverviewActivity.class.getName()),
                    hasCategories(hasItem(equalTo(Intent.CATEGORY_HOME))),
                    hasFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )).respondWith(result);
        }

        @Override
        public void afterActivityFinished() {
            Intents.release();
        }
    };

    @Test
    public void whenMainActivityLaunched_itLaunchesWineOverviewActivity() {
        intended(allOf(
                hasComponent(WineOverviewActivity.class.getName()),
                hasCategories(hasItem(equalTo(Intent.CATEGORY_HOME))),
                hasFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        ));
    }
}
