package ch.hsr.winescore.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.hsr.winescore.R;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.utils.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class WineItemAdapter extends RecyclerView.Adapter<WineItemAdapter.WineItemViewHolder> {

    ItemClickListener itemClickListener;

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
        holder.subtitle.setText(wine.getCountry());
        holder.index.setText(wine.getConfidenceIndex());
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

        public WineItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
