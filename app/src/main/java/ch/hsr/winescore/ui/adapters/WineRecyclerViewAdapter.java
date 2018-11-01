package ch.hsr.winescore.ui.adapters;

import android.arch.paging.PagedListAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.hsr.winescore.R;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.utils.BottomReachedListener;
import ch.hsr.winescore.utils.ItemClickListener;

public class WineRecyclerViewAdapter extends PagedListAdapter<Wine, WineViewHolder> {

    private ItemClickListener itemClickListener;

    public WineRecyclerViewAdapter(ItemClickListener itemClickListener) {
        super(Wine.DIFF_CALLBACK);
        this.itemClickListener = itemClickListener;
    }

    @Override
    public WineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_explore_listentry, parent, false);

        final WineViewHolder wineItemViewHolder = new WineViewHolder(itemView);
        itemView.setOnClickListener(v -> itemClickListener.onItemClick(v, wineItemViewHolder.getAdapterPosition()));

        return wineItemViewHolder;
    }

    @Override
    public void onBindViewHolder(final WineViewHolder holder, int position) {
        Wine wine = getItem(position);
        if (wine != null) {
            holder.bindTo(wine);
        }
    }

}
