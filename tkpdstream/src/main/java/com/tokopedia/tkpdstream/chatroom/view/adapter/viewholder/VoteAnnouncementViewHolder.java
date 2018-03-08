package com.tokopedia.tkpdstream.chatroom.view.adapter.viewholder;

import android.content.Context;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.chatroom.view.listener.GroupChatContract;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.VoteAnnouncementViewModel;

/**
 * @author by nisie on 2/27/18.
 */

public class VoteAnnouncementViewHolder extends BaseChatViewHolder<VoteAnnouncementViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.vote_announcement_view_holder;

    ImageView voteIcon;
    TextView voteTitle;
    TextView voteQuestion;
    View voteLayout;
    Context context;
    private final GroupChatContract.View.VoteAnnouncementViewHolderListener listener;

    public VoteAnnouncementViewHolder(View itemView, GroupChatContract.View.VoteAnnouncementViewHolderListener imageListener) {
        super(itemView);
        voteIcon = itemView.findViewById(R.id.vote_icon);
        voteTitle = itemView.findViewById(R.id.vote_title);
        voteQuestion = itemView.findViewById(R.id.vote_question);
        voteLayout = itemView.findViewById(R.id.vote_layout);
        listener = imageListener;
    }

    @Override
    public void bind(final VoteAnnouncementViewModel element) {
        super.bind(element);
        switch (element.getVoteType()) {
            case VoteAnnouncementViewModel.POLLING_START:
                setVoteStarted(element);
                break;
            case VoteAnnouncementViewModel.POLLING_FINISHED:
                setVoteFinished(element);
                break;
            default:
                break;
        }

        voteQuestion.setText(MethodChecker.fromHtml(element.getMessage()));
        voteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onVoteComponentClicked("vote", element.getMessage());
            }
        });
    }

    private void setVoteFinished(VoteAnnouncementViewModel element) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ImageHandler.loadImageWithIdWithoutPlaceholder(voteIcon, R.drawable.ic_vote_inactive);
        } else {
            voteIcon.setImageResource(R.drawable.ic_vote_inactive);
        }

        voteTitle.setText(R.string.title_poll_finished);
        voteTitle.setTextColor(MethodChecker.getColor(voteTitle.getContext(), R.color.black_54));
    }

    private void setVoteStarted(VoteAnnouncementViewModel element) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ImageHandler.loadImageWithIdWithoutPlaceholder(voteIcon, R.drawable.ic_vote);
        } else {
            voteIcon.setImageResource(R.drawable.ic_vote);
        }
        voteTitle.setText(R.string.title_poll_started);
        voteTitle.setTextColor(MethodChecker.getColor(voteTitle.getContext(), R.color.medium_green));
    }

}
