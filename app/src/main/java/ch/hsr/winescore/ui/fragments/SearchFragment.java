package ch.hsr.winescore.ui.fragments;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.hsr.winescore.R;
import ch.hsr.winescore.ui.activities.SearchActivity;

public class SearchFragment extends Fragment {

    public static final String TAG = "SearchFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, rootView);

        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();

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
