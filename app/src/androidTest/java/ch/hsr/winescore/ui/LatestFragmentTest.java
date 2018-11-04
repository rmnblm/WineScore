package ch.hsr.winescore.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import ch.hsr.winescore.R;
import ch.hsr.winescore.WineScoreConstants;
import ch.hsr.winescore.ui.latest.LatestFragment;
import com.android21buttons.fragmenttestrule.FragmentTestRule;
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
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class LatestFragmentTest{

    @Rule
    public FragmentTestRule<?, LatestFragment> fragmentTestRule = FragmentTestRule.create(LatestFragment.class);

    @Test
    public void whenShowOrHideLoadingIsCalled_itShowsOrHidesLoadingIndicator() {
        LatestFragment fragment = fragmentTestRule.getFragment();
        SwipeRefreshLayout swipe = fragment.getView().findViewById(R.id.swipeContainer);

        fragment.showLoading();
        assertTrue(swipe.isRefreshing());

        fragment.hideLoading();
        assertFalse(swipe.isRefreshing());
    }
}
