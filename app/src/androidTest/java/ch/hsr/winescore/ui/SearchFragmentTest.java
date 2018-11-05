package ch.hsr.winescore.ui;

import android.support.test.espresso.intent.Intents;
import android.support.test.runner.AndroidJUnit4;
import ch.hsr.winescore.R;
import ch.hsr.winescore.ui.latest.LatestFragment;
import ch.hsr.winescore.ui.search.SearchActivity;
import ch.hsr.winescore.ui.search.SearchFragment;
import com.android21buttons.fragmenttestrule.FragmentTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class SearchFragmentTest {

    @Rule
    public FragmentTestRule<?, SearchFragment> fragmentTestRule = FragmentTestRule.create(SearchFragment.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @Test
    public void whenClickingSearchButton_itOpensSearchActivity() {
        onView(withId(R.id.searchButton)).perform(click());
        intended(hasComponent(SearchActivity.class.getName()));
    }

    @After
    public void tearDown() {
        Intents.release();
    }
}
