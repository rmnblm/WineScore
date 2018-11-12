package ch.hsr.winescore.ui;

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
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SplashActivityTest {

    @Rule
    public ActivityTestRule<SplashActivity> intentsTestRule = new ActivityTestRule<SplashActivity>(SplashActivity.class) {
        @Override
        public void beforeActivityLaunched() {
            // SplashActivity launches an activity in the onCreate() method
            // Thus, Intents has to be initialized beforehand
            Intents.init();
            Instrumentation.ActivityResult result =
                    new Instrumentation.ActivityResult(Activity.RESULT_OK, new Intent());
            intending(hasComponent(MainActivity.class.getName())).respondWith(result);
        }

        @Override
        public void afterActivityFinished() {
            Intents.release();
        }
    };

    @Test
    public void whenSplashActivityLaunched_itLaunchesMainActivity() {
        intended(hasComponent(MainActivity.class.getName()));
    }
}
