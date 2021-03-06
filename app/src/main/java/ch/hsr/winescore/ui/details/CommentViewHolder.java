package ch.hsr.winescore.ui.details;

import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.hsr.winescore.R;
import ch.hsr.winescore.domain.models.Comment;
import ch.hsr.winescore.ui.utils.BaseViewHolder;

public class CommentViewHolder extends BaseViewHolder<Comment> {

    @BindView(R.id.constraintLayout) View layout;
    @BindView(R.id.date) TextView tvDate;
    @BindView(R.id.user) TextView tvUser;
    @BindView(R.id.content) TextView tvContent;

    public CommentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public static CommentViewHolder newInstance(View view) {
        return new CommentViewHolder(view);
    }

    @Override
    public void bindTo(Comment comment) {
        tvDate.setText(DateUtils.getRelativeTimeSpanString(
                comment.getTimestamp().toDate().getTime(),
                System.currentTimeMillis(),
                DateUtils.DAY_IN_MILLIS));
        tvUser.setText(comment.getUserName());
        tvContent.setText(comment.getContent());
    }
}
