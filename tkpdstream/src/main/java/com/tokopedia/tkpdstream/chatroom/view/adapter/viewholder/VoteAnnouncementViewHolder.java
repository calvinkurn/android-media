package com.tokopedia.tkpdstream.chatroom.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tkpdstream.R;
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

    public VoteAnnouncementViewHolder(View itemView) {
        super(itemView);
        voteIcon = itemView.findViewById(R.id.vote_icon);
        voteTitle = itemView.findViewById(R.id.vote_title);
        voteQuestion = itemView.findViewById(R.id.vote_question);
    }

    @Override
    public void bind(VoteAnnouncementViewModel element) {
        super.bind(element);
        switch (element.getVoteType()) {
            case VoteAnnouncementViewModel.VOTE_ACTIVE:
                setVoteStarted(element);
                break;
            case VoteAnnouncementViewModel.VOTE_FINISHED:
                setVoteFinished(element);
                break;
            default:
                break;
        }
    }

    private void setVoteFinished(VoteAnnouncementViewModel element) {
        ImageHandler.loadImageWithIdWithoutPlaceholder(voteIcon, R.drawable.ic_vote_inactive);
        voteTitle.setText(R.string.title_poll_finished);
        voteTitle.setTextColor(MethodChecker.getColor(voteTitle.getContext(), R.color.black_54));
        voteQuestion.setText(element.getMessage()
        );
    }

    private void setVoteStarted(VoteAnnouncementViewModel element) {
        ImageHandler.loadImageWithIdWithoutPlaceholder(voteIcon, R.drawable.ic_vote);
        voteTitle.setText(R.string.title_poll_started);
        voteTitle.setTextColor(MethodChecker.getColor(voteTitle.getContext(), R.color.medium_green));
    }

}
