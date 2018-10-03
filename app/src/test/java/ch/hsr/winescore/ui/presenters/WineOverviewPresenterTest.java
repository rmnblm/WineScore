package ch.hsr.winescore.ui.presenters;

import ch.hsr.winescore.ui.views.WineOverviewViewMock;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class WineOverviewPresenterTest {

    private WineOverviewViewMock view;
    private WineOverviewPresenter presenter;

    @Before
    public void setupPresenter() {
        view = new WineOverviewViewMock();
        presenter = new WineOverviewPresenter();
        presenter.attachView(view);

    }

    @Test
    public void whenSubscribingToPresenter_itShowsTheWineList() {
        presenter.subscribe();
        assertTrue(view.showWineListCalled);
    }
}
