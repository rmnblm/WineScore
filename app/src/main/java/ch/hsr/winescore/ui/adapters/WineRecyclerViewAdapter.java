package ch.hsr.winescore.ui.adapters;

import android.arch.paging.PagedListAdapter;
import android.graphics.drawable.Drawable;
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

import java.util.Locale;

public class WineRecyclerViewAdapter extends PagedListAdapter<Wine, WineRecyclerViewAdapter.ViewHolder> {

    private ItemClickListener itemClickListener;
    private final BottomReachedListener bottomReachedListener;

    public WineRecyclerViewAdapter(ItemClickListener itemClickListener, BottomReachedListener bottomReachedListener) {
        super(Wine.DIFF_CALLBACK);
        this.itemClickListener = itemClickListener;
        this.bottomReachedListener = bottomReachedListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_explore_listentry, parent, false);

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
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.titleName) TextView title_name;
        @BindView(R.id.titleOrigin) TextView title_origin;
        @BindView(R.id.titleWinery) TextView title_winery;
        @BindView(R.id.score) TextView score;
        @BindView(R.id.icon) ImageView icon;
        @BindDrawable(R.drawable.ic_wine_red) Drawable wine_red;
        @BindDrawable(R.drawable.ic_wine_white) Drawable wine_white;
        @BindDrawable(R.drawable.ic_wine_pink) Drawable wine_pink;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindTo(Wine wine) {
            title_winery.setText(wine.getWinery());

            title_name.setText(String.format("%s %s", wine.getShortName(), wine.getVintage()));
            title_origin.setText(String.format("%s, %s", wine.getAppellation(), wine.getCountry()));
            score.setText(String.format(Locale.getDefault(), "%.2f", wine.getScore()));

            switch(wine.getColor()) {
                case Red: icon.setImageDrawable(wine_red); break;
                case White: icon.setImageDrawable(wine_white); break;
                case Pink: icon.setImageDrawable(wine_pink); break;
            }
        }
    }
}
