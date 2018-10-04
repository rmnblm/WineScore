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
import ch.hsr.winescore.utils.ItemClickListener;

public class WineOverviewAdapter extends PagedListAdapter<Wine, WineOverviewAdapter.ViewHolder> {

    private ItemClickListener itemClickListener;

    public WineOverviewAdapter(ItemClickListener itemClickListener) {
        super(DIFF_CALLBACK);
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_wine_overview, parent, false);

        final ViewHolder wineItemViewHolder = new ViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(v, wineItemViewHolder.getAdapterPosition());
            }
        });

        return wineItemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Wine wine = getItem(position);
        if (wine != null) {
            holder.bindTo(wine);
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

    private static final DiffUtil.ItemCallback<Wine> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Wine>() {
                @Override
                public boolean areItemsTheSame(Wine oldItem, Wine newItem) {
                    return oldItem.getId() == newItem.getId();
                }
                @Override
                public boolean areContentsTheSame(Wine oldItem, Wine newItem) {
                    return (oldItem.getName() == newItem.getName()
                            && oldItem.getCountry() == newItem.getCountry());
                }
            };
}
