package ch.hsr.winescore.ui.wine;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.hsr.winescore.R;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.ui.utils.ItemClickListener;

public class WineRecyclerViewAdapter extends PagedListAdapter<Wine, WineViewHolder> {

    private final ItemClickListener itemClickListener;

    public WineRecyclerViewAdapter(ItemClickListener itemClickListener) {
        super(Wine.DIFF_CALLBACK);
        this.itemClickListener = itemClickListener;
    }

    @Override
    @NonNull
    public WineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wine_listentry, parent, false);

        final WineViewHolder wineItemViewHolder = new WineViewHolder(itemView);
        itemView.setOnClickListener(v -> itemClickListener.onItemClick(v, wineItemViewHolder.getAdapterPosition()));

        return wineItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final WineViewHolder holder, int position) {
        Wine wine = getItem(position);
        if (wine != null) {
            holder.bindTo(wine);
        }
    }

}
