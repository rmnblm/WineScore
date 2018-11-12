package ch.hsr.winescore.ui;

import android.support.test.runner.AndroidJUnit4;
import android.support.v4.widget.SwipeRefreshLayout;
import ch.hsr.winescore.R;
import ch.hsr.winescore.ui.latest.LatestFragment;
import com.android21buttons.fragmenttestrule.FragmentTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
