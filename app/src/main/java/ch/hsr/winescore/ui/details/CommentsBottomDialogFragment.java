package ch.hsr.winescore.ui.details;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import ch.hsr.winescore.data.repositories.ICommentsRepository;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.hsr.winescore.R;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.data.repositories.CommentsFirebaseRepository;

public class CommentsBottomDialogFragment extends BottomSheetDialogFragment {

    private Wine mWine;
    private String mComment;
    private DetailsView mDetailsView;
    private final ICommentsRepository commentsRepository = new CommentsFirebaseRepository();

    @BindView(R.id.addCommentLayout) View viewAddComment;
    @BindView(R.id.input_comment) TextInputEditText inputAddComment;
    @BindView(R.id.button_add_comment) ImageButton btnAddComment;

    @OnClick(R.id.button_close)
    public void onClose(View v) {
        dismiss();
    }

    @OnClick(R.id.button_add_comment)
    public void onAddComment(View v) {
        commentsRepository.add(mWine, mComment, result -> dismiss());
    }

    public static CommentsBottomDialogFragment newInstance(Wine wine) {
        CommentsBottomDialogFragment fragment = new CommentsBottomDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(CommentsFragment.ARGUMENT_WINE, wine);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mWine = (Wine) arguments.getSerializable(CommentsFragment.ARGUMENT_WINE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments_dialog, container, false);
        ButterKnife.bind(this, view);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, CommentsFragment.newInstance(mWine))
                .commit();
        getDialog().setOnShowListener(dialog -> {
            inputAddComment.setText("");
            BottomSheetBehavior behavior = BottomSheetBehavior.from((View) view.getParent());
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });

        viewAddComment.setVisibility(FirebaseAuth.getInstance().getCurrentUser() != null ? View.VISIBLE : View.GONE);
        btnAddComment.setEnabled(false);
        inputAddComment.addTextChangedListener(mCommentTextWatcher);
        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mDetailsView != null) {
            mDetailsView.onBottomDialogClosed();
        }
        super.onDismiss(dialog);
    }

    public void show(FragmentManager manager, String tag, DetailsView view) {
        mDetailsView = view;
        super.show(manager, tag);
    }

    private final TextWatcher mCommentTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // not used
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // not used
        }

        @Override
        public void afterTextChanged(Editable s) {
            mComment = s.toString();
            btnAddComment.setEnabled(!TextUtils.isEmpty(s));
        }
    };
}
