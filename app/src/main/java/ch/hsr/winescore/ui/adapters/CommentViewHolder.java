package ch.hsr.winescore.ui.adapters;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.hsr.winescore.R;
import ch.hsr.winescore.model.Comment;
import ch.hsr.winescore.model.Wine;

public class CommentViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.constraintLayout) View layout;
    @BindView(R.id.date) TextView textViewDate;
    @BindView(R.id.user) TextView textViewUser;
    @BindView(R.id.content) TextView textViewContent;

    public CommentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindTo(Comment comment) {
        textViewDate.setText(DateUtils.getRelativeTimeSpanString(
                comment.getTimestamp().toDate().getTime(),
                System.currentTimeMillis(),
                DateUtils.DAY_IN_MILLIS));
        textViewUser.setText(comment.getUserName());
        textViewContent.setText(comment.getComment());
    }
}
