package ch.hsr.winescore.ui;

import android.util.SparseIntArray;
import ch.hsr.winescore.domain.models.Comment;
import ch.hsr.winescore.ui.details.DetailsView;

public class DetailsViewMock implements DetailsView {

    public boolean refreshFavoriteButtonValue = false;
    public boolean onFavoriteAddedCalled = false;
    public boolean onFavoriteRemovedCalled = false;
    public boolean refreshRatingsCalled = false;
    public boolean showMyRatingCalled = false;
    public int showMyRatingValue;
    public boolean hideMyRatingCalled = false;
    public boolean refreshLastCommentCalled = false;
    public boolean onBottomDialogClosedCalled = false;
    public Comment refreshLastCommentValue;

    @Override
    public void refreshFavoriteButton(boolean isFavorite) {
        refreshFavoriteButtonValue = isFavorite;
    }

    @Override
    public void onFavoriteAdded() {
        onFavoriteAddedCalled = true;
    }

    @Override
    public void onFavoriteRemoved() {
        onFavoriteRemovedCalled = true;
    }

    @Override
    public void refreshRatings(SparseIntArray ratings) {
        refreshRatingsCalled = true;
    }

    @Override
    public void showMyRating(int rating) {
        showMyRatingCalled = true;
        showMyRatingValue = rating;
    }

    @Override
    public void hideMyRating() {
        hideMyRatingCalled = true;
    }

    @Override
    public void refreshLastComment(Comment comment) {
        refreshLastCommentCalled = true;
        refreshLastCommentValue = comment;
    }

    @Override
    public void onBottomDialogClosed() {
        onBottomDialogClosedCalled = true;
    }
}
