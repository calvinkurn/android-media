package com.tokopedia.tkpdstream.vote.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.chatroom.view.listener.GroupChatContract;
import com.tokopedia.tkpdstream.vote.view.model.VoteViewModel;

import java.util.Locale;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class VoteBarViewHolder extends AbstractViewHolder<VoteViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.vote_option_bar;
    private final GroupChatContract.View viewListener;
    private ProgressBar progressBar;
    private TextView option;
    private TextView percent;
    private View icon;

    public VoteBarViewHolder(View itemView, GroupChatContract.View viewListener) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.progress_bar);
        option = itemView.findViewById(R.id.text_view);
        percent = itemView.findViewById(R.id.percent);
        icon = itemView.findViewById(R.id.icon_vote);
        this.viewListener = viewListener;
    }

    @Override
    public void bind(final VoteViewModel element) {

        Context context = itemView.getContext();
        if (element.getSelected() == VoteViewModel.DEFAULT) {
            percent.setVisibility(View.GONE);
            progressBar.setProgress(0);
            progressBar.setProgressDrawable(MethodChecker.getDrawable(context, R.drawable.vote_option_bar_default));
            icon.setVisibility(View.GONE);
        } else {
            percent.setVisibility(View.VISIBLE);
            progressBar.setProgress(element.getPercentageInteger());
            if (element.getSelected() == VoteViewModel.SELECTED) {
                icon.setVisibility(View.VISIBLE);
                progressBar.setProgressDrawable(MethodChecker.getDrawable(context, R.drawable.vote_option_bar_selected));
            } else if (element.getSelected() == VoteViewModel.UNSELECTED) {
                icon.setVisibility(View.GONE);
                progressBar.setProgressDrawable(MethodChecker.getDrawable(context, R.drawable.vote_option_bar_unselected));
            }
        }

        option.setText(element.getOption());
        percent.setText(String.format(Locale.getDefault(), "%d%%", element.getPercentageInteger()));

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onVoteOptionClicked(element);
            }
        });
    }
}
