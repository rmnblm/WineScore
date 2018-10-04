package ch.hsr.winescore.ui.adapters;

import android.arch.paging.PagedListAdapter;
import android.graphics.drawable.Drawable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.hsr.winescore.R;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.utils.BottomReachedListener;
import ch.hsr.winescore.utils.ItemClickListener;

public class WineOverviewAdapter extends PagedListAdapter<Wine, WineOverviewAdapter.ViewHolder> {

    private ItemClickListener itemClickListener;
    private final BottomReachedListener bottomReachedListener;

    public WineOverviewAdapter(ItemClickListener itemClickListener, BottomReachedListener bottomReachedListener) {
        super(Wine.DIFF_CALLBACK);
        this.itemClickListener = itemClickListener;
        this.bottomReachedListener = bottomReachedListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_wine_overview, parent, false);

        final ViewHolder wineItemViewHolder = new ViewHolder(itemView);
        itemView.setOnClickListener(v -> itemClickListener.onItemClick(v, wineItemViewHolder.getAdapterPosition()));

        return wineItemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Wine wine = getItem(position);
        if (wine != null) {
            holder.bindTo(wine);
        }

        if (position == getItemCount() - 1) {
            System.out.println("bottom reached");
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title) TextView title;
        @BindView(R.id.subtitle) TextView subtitle;
        @BindView(R.id.index) TextView index;
        @BindView(R.id.icon) ImageView icon;
        @BindDrawable(R.drawable.ic_wine_red) Drawable wine_red;
        @BindDrawable(R.drawable.ic_wine_white) Drawable wine_white;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindTo(Wine wine) {
            title.setText(wine.getName());
            subtitle.setText(wine.getCountry() + ", " + wine.getVintage());
            index.setText(wine.getConfidenceIndex());
            icon.setImageDrawable(wine.getColor().toLowerCase().equals("white") ? wine_white : wine_red);
        }
    }
}
