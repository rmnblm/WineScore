package ch.hsr.winescore;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import ch.hsr.winescore.dummy.DummyContent;

/**
 * A fragment representing a single Wine detail screen.
 * This fragment is either contained in a {@link WineListActivity}
 * in two-pane mode (on tablets) or a {@link WineDetailActivity}
 * on handsets.
 */
public class WineDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WineDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.title);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.wine_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.vintage)).setText(String.valueOf(mItem.vintage));
            ((TextView) rootView.findViewById(R.id.color)).setText(mItem.color);
            ((TextView) rootView.findViewById(R.id.appellation)).setText(mItem.appellation);
            ((TextView) rootView.findViewById(R.id.regions)).setText(mItem.regions);
            ((TextView) rootView.findViewById(R.id.country)).setText(mItem.country);
            ((TextView) rootView.findViewById(R.id.confidence_index)).setText(mItem.index);
            ((TextView) rootView.findViewById(R.id.score)).setText(String.valueOf(mItem.score));
            ((ProgressBar) rootView.findViewById(R.id.scoreCircle)).setProgress((int) Math.round(mItem.score));
        }

        return rootView;
    }
}
