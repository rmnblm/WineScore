package ch.hsr.winescore.ui.search;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.hsr.winescore.R;

public class SearchFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.searchButton)
    public void openSearchActivity(View animationSource) {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                getActivity(), animationSource, "search"
        );
        startActivity(intent, options.toBundle());
    }
}
