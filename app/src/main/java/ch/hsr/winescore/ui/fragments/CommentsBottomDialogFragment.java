package ch.hsr.winescore.ui.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.hsr.winescore.R;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.ui.datasources.CommentsFirebaseRepository;

public class CommentsBottomDialogFragment extends BottomSheetDialogFragment {

    private Wine mWine;
    private String mComment;

    @BindView(R.id.addCommentLayout)
    View mLayoutAddComment;
    @BindView(R.id.input_comment)
    TextInputEditText mInputAddComment;
    @BindView(R.id.button_add_comment)
    ImageButton mButtonAddComment;

    @OnClick(R.id.button_close)
    public void onClose(View v) {
        dismiss();
    }

    @OnClick(R.id.button_add_comment)
    public void onAddComment(View v) {
        CommentsFirebaseRepository.add(mWine, mComment, result -> dismiss());
    }

    public static CommentsBottomDialogFragment newInstane(Wine wine) {
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
            mInputAddComment.setText("");
            BottomSheetBehavior behavior = BottomSheetBehavior.from((View) view.getParent());
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });
        mButtonAddComment.setEnabled(false);
        mInputAddComment.addTextChangedListener(mCommentTextWatcher);
        return view;
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
            mButtonAddComment.setEnabled(!TextUtils.isEmpty(s));
        }
    };
}
