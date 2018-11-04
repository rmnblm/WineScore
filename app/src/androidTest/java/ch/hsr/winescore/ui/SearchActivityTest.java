package ch.hsr.winescore.ui;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import ch.hsr.winescore.R;
import ch.hsr.winescore.WineScoreConstants;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.ui.details.DetailsActivity;
import ch.hsr.winescore.ui.search.SearchActivity;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.*;

public class SearchActivityTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Rule
    public ActivityTestRule<SearchActivity> activityTestRule =
            new ActivityTestRule<>(SearchActivity.class, false, true);

    private MockWebServer server;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.start();
        WineScoreConstants.ROOT_URL = server.url("/").toString();

        Intents.init();
    }

    @Test
    public void whenShowErrorIsCalled_itShowsErrorMessage() {
        String message = "This is a test message";
        activityTestRule.getActivity().showError(message);
        onView(withText(message)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void whenServerResponseIsError_itShowsErrorMessage() {
        server.enqueue(new MockResponse().setResponseCode(403));
        onView(withId(R.id.searchEditText)).perform(typeText("test"), pressImeActionButton());
        onView(withText(R.string.dataload_error_message)).check(matches(isDisplayed()));
    }

    @Test
    public void whenNavigateToDetailScreenIsCalled_itLaunchesDetailActivity() {
        View dummyView = new View(activityTestRule.getActivity());
        Wine wine = new Wine("Terrazas De Los Andes, Reserva Malbec, Mendoza", "133251");
        activityTestRule.getActivity().navigateToDetailScreen(dummyView, wine);
        intended(hasComponent(DetailsActivity.class.getName()));
    }

    @After
    public void tearDown() throws IOException {
        Intents.release();
        server.shutdown();
    }
}
