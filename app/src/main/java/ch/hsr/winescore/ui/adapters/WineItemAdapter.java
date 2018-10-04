package ch.hsr.winescore.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

public class WineItemAdapter extends RecyclerView.Adapter<WineItemAdapter.WineItemViewHolder> {

    private ItemClickListener itemClickListener;
    private Context context;
    private List<Wine> wines = new ArrayList<>();

    public WineItemAdapter(Context context, ItemClickListener itemClickListener) {
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public WineItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_wine_overview, parent, false);

        final WineItemViewHolder wineItemViewHolder = new WineItemViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(v, wineItemViewHolder.getAdapterPosition());
            }
        });

        return wineItemViewHolder;
    }

    @Override
    public void onBindViewHolder(final WineItemViewHolder holder, int position) {
        Wine wine = wines.get(position);
        holder.title.setText(wine.getName());
        holder.subtitle.setText(wine.getCountry() + ", " + wine.getVintage());
        holder.index.setText(wine.getConfidenceIndex());
        holder.icon.setImageDrawable(wine.getColor().toLowerCase().equals("white") ? holder.wine_white : holder.wine_red);
    }

    @Override
    public int getItemCount() {
        return wines.size();
    }

    public void addWine(Wine wine) {
        wines.add(wine);
        notifyItemChanged(getItemCount() - 1);
    }

    public void addWines(List<Wine> newWines) {
        wines.addAll(newWines);
        notifyDataSetChanged();
    }

    public void replaceAllWines(List<Wine> newWines) {
        wines.clear();
        wines.addAll(newWines);
        notifyDataSetChanged();
    }

    public List<Wine> getWines() {
        return wines;
    }

    public class WineItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title) TextView title;
        @BindView(R.id.subtitle) TextView subtitle;
        @BindView(R.id.index) TextView index;
        @BindView(R.id.icon) ImageView icon;
        @BindDrawable(R.drawable.ic_wine_red) Drawable wine_red;
        @BindDrawable(R.drawable.ic_wine_white) Drawable wine_white;

        public WineItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
