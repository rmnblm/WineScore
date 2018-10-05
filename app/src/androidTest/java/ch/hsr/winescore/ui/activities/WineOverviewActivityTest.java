package ch.hsr.winescore.ui.activities;

import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import ch.hsr.winescore.R;
import ch.hsr.winescore.WineScoreConstants;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class WineOverviewActivityTest {

    @Rule
    public ActivityTestRule<WineOverviewActivity> mActivityRule =
            new ActivityTestRule<>(WineOverviewActivity.class, true, false);

    private MockWebServer server;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.start();
        WineScoreConstants.ROOT_URL = server.url("/").toString();
    }

    @Test
    public void testRetryButtonShowsWhenError() {
        server.enqueue(new MockResponse().setResponseCode(403));
        mActivityRule.launchActivity(new Intent());
        onView(withText(R.string.dataload_error_message)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }
}
