package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder.inboxdetail;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.customview.ReputationView;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.ReputationAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationDetail;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailHeaderViewModel;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailHeaderViewHolder extends
        AbstractViewHolder<InboxReputationDetailHeaderViewModel> {

    private static final int NO_REPUTATION = 0;

    ImageView userAvatar;
    TextView name;
    ReputationView reputationView;
    View deadlineLayout;
    TextView deadline;
    TextView promptMessage;
    Button favoriteButton;
    TextView changeButton;
    RecyclerView smiley;
    TextView opponentSmileyText;
    ImageView opponentSmiley;
    ReputationAdapter adapter;

    @LayoutRes
    public static final int LAYOUT = R.layout.inbox_reputation_detail_header;

    public InboxReputationDetailHeaderViewHolder(View itemView,
                                                 InboxReputationDetail.View viewListener,
                                                 ReputationAdapter.ReputationListener reputationListener) {
        super(itemView);
        userAvatar = (ImageView) itemView.findViewById(R.id.user_avatar);
        name = (TextView) itemView.findViewById(R.id.name);
        reputationView = (ReputationView) itemView.findViewById(R.id.reputation);
        deadline = (TextView) itemView.findViewById(R.id.deadline_text);
        deadlineLayout = itemView.findViewById(R.id.deadline);
        promptMessage = (TextView) itemView.findViewById(R.id.prompt_text);
        favoriteButton = (Button) itemView.findViewById(R.id.favorite_button);
        changeButton = (TextView) itemView.findViewById(R.id.change_button);
        smiley = (RecyclerView) itemView.findViewById(R.id.smiley);
        opponentSmileyText = (TextView) itemView.findViewById(R.id.opponent_smiley_text);
        opponentSmiley = (ImageView) itemView.findViewById(R.id.opponent_smiley);
        adapter = ReputationAdapter.createInstance(reputationListener);
        smiley.setLayoutManager(new GridLayoutManager(itemView.getContext(), 3,
                LinearLayoutManager.VERTICAL, false));
        smiley.setAdapter(adapter);
    }

    @Override
    public void bind(InboxReputationDetailHeaderViewModel element) {
        ImageHandler.LoadImage(userAvatar, element.getAvatarImage());
        name.setText(element.getName());

        if (!TextUtils.isEmpty(element.getDeadline()) && element.getReputationDataViewModel()
                .isShowLockingDeadline()) {
            deadline.setText(element.getDeadline());
            deadlineLayout.setVisibility(View.VISIBLE);
        } else {
            deadlineLayout.setVisibility(View.GONE);
        }

        if (element.getReputationDataViewModel().isInserted()) {
            promptMessage.setText(MainApplication.getAppContext().getString(R.string.your_scoring));
            setSmiley(element, adapter);
        } else {
            adapter.showAllSmiley();
            promptMessage.setText(getPromptText(element));
        }

        if (element.getReputationDataViewModel().isEditable())
            changeButton.setVisibility(View.VISIBLE);
        else
            changeButton.setVisibility(View.GONE);

        if(element.getReputationDataViewModel().getReviewerScore() == NO_REPUTATION){
            opponentSmileyText.setText(MainApplication.getAppContext().getString(R.string
                    .seller_has_not_review));
            ImageHandler.loadImageWithId(opponentSmiley, R.drawable.ic_smiley_empty);
        }else{
            opponentSmileyText.setText(MainApplication.getAppContext().getString(R.string
                    .score_from_seller));
            ImageHandler.loadImageWithId(opponentSmiley, R.drawable.ic_smiley_good);
        }
    }

    private void setSmiley(InboxReputationDetailHeaderViewModel element, ReputationAdapter adapter) {

    }

    private String getPromptText(InboxReputationDetailHeaderViewModel element) {
        return MainApplication.getAppContext().getString(R.string
                .reputation_prompt) + " " + element.getName() + "?";
    }
}
