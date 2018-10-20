package ch.hsr.winescore.ui.adapters;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.hsr.winescore.R;
import ch.hsr.winescore.model.Wine;

public class WineViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.constraintLayout)
    View layout;
    @BindView(R.id.titleName)
    TextView title_name;
    @BindView(R.id.titleOrigin) TextView title_origin;
    @BindView(R.id.titleWinery) TextView title_winery;
    @BindView(R.id.score) TextView score;
    @BindView(R.id.icon)
    ImageView icon;
    @BindDrawable(R.drawable.ic_wine_red)
    Drawable wine_red;
    @BindDrawable(R.drawable.ic_wine_white) Drawable wine_white;
    @BindDrawable(R.drawable.ic_wine_pink) Drawable wine_pink;

    public WineViewHolder(View itemView) {
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
