package ch.hsr.winescore;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class BottomNavigationFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;


    public BottomNavigationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bottom_navigation, container, false);

        bottomNavigationView = rootView.findViewById(R.id.bottomNavigationView);
        if (getActivity() instanceof  WineListActivity) {
            bottomNavigationView.setSelectedItemId(R.id.wines);
        } else { // TODO: add other activities
            bottomNavigationView.setSelectedItemId(R.id.my_favorites);
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(listener);

        return rootView;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            if (bottomNavigationView.getSelectedItemId() == menuItem.getItemId()) {
                return false;
            }
            switch (menuItem.getItemId()) {
                case R.id.wines:
                    getActivity().startActivity(new Intent(getContext(), WineListActivity.class));
                    break;
                case R.id.my_favorites:
                    getActivity().startActivity(new Intent(getContext(), WineListActivity.class)); // TODO: replace by correct activity
                    break;
                case R.id.my_ratings:
                    getActivity().startActivity(new Intent(getContext(), WineListActivity.class)); // TODO: replace by correct activity
                    break;
            }
            return false;
        }
    };

}
