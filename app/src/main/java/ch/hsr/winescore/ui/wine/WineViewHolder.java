package ch.hsr.winescore.ui.wine;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.hsr.winescore.R;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.ui.utils.BaseViewHolder;

public class WineViewHolder extends BaseViewHolder<Wine> {

    @BindView(R.id.constraintLayout) View layout;
    @BindView(R.id.titleName) TextView tvTitleName;
    @BindView(R.id.titleOrigin) TextView tvTitleOrigin;
    @BindView(R.id.titleWinery) TextView tvTitleWinery;
    @BindView(R.id.score) TextView tvScore;
    @BindView(R.id.icon) ImageView ivIcon;
    @BindDrawable(R.drawable.ic_wine_red) Drawable drawWineRed;
    @BindDrawable(R.drawable.ic_wine_white) Drawable drawWineWhite;
    @BindDrawable(R.drawable.ic_wine_pink) Drawable drawWinePink;

    WineViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public static WineViewHolder newInstance(View view) {
        return new WineViewHolder(view);
    }

    @Override
    public void bindTo(Wine wine) {
        tvTitleWinery.setText(wine.getWinery());

        tvTitleName.setText(String.format("%s %s", wine.getShortName(), wine.getVintage()));
        tvTitleOrigin.setText(String.format("%s, %s", wine.getAppellation(), wine.getCountry()));
        tvScore.setText(String.format(Locale.getDefault(), "%.2f", wine.getScore()));

        switch(wine.getColor()) {
            case RED: ivIcon.setImageDrawable(drawWineRed); break;
            case WHITE: ivIcon.setImageDrawable(drawWineWhite); break;
            case PINK: ivIcon.setImageDrawable(drawWinePink); break;
        }
    }
}
