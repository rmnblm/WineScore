package ch.hsr.winescore.ui.fragments;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.w3c.dom.Text;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.hsr.winescore.R;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.ui.datasources.CommentsFirebaseRepository;

public class CommentsBottomDialogFragment extends BottomSheetDialogFragment {

    private Wine mWine;
    private String mComment;
    private AlertDialog mAddCommentDialog;

    @OnClick(R.id.button_close)
    public void onClose(View v) {
        dismiss();
    }

    @OnClick(R.id.button_add_comment)
    public void onAddComment(View v) {
        mAddCommentDialog.show();
        mAddCommentDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
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

        setupAddCommentDialog();
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
            BottomSheetBehavior behavior = BottomSheetBehavior.from((View) view.getParent());
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });
        return view;
    }

    private void setupAddCommentDialog() {
        final EditText input = new EditText(getContext());
        input.setSingleLine(false);
        input.setLines(5);
        input.setHorizontalScrollBarEnabled(false);
        input.addTextChangedListener(mCommentTextWatcher);

        mAddCommentDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.comments_add_title)
                .setIcon(R.drawable.ic_mode_comment_black_24dp)
                .setView(input)
                .setPositiveButton(R.string.button_submit, (dialog, which) -> {
                    CommentsFirebaseRepository.add(mWine, mComment, result -> dismiss());
                })
                .setNegativeButton(R.string.button_cancel, (dialog, which) -> dialog.cancel())
                .create();
    }

    private TextWatcher mCommentTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {
            mComment = s.toString();
            mAddCommentDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(!TextUtils.isEmpty(s));
        }
    };
}
