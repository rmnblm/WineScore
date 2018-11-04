package ch.hsr.winescore.ui.details;

import android.util.SparseIntArray;

import ch.hsr.winescore.domain.models.Comment;
import ch.hsr.winescore.ui.utils.View;

public interface DetailsView extends View {
    void refreshFavoriteButton(boolean isFavorite);
    void onFavoriteAdded();
    void onFavoriteRemoved();
    void refreshRatings(SparseIntArray ratings);
    void showMyRating(int rating);
    void hideMyRating();
    void refreshLastComment(Comment comment);
    void onBottomDialogClosed();
}
