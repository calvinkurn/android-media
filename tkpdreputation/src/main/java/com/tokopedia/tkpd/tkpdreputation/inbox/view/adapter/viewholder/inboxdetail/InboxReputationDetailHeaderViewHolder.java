package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder.inboxdetail;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.reputation.ReputationView;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.ReputationAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.InboxReputationItemViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailHeaderViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.RevieweeBadgeSellerViewModel;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailHeaderViewHolder extends
        AbstractViewHolder<InboxReputationDetailHeaderViewModel> {

    private static final int NO_REPUTATION = 0;
    public static final int SMILEY_BAD = -1;
    public static final int SMILEY_NEUTRAL = 1;
    public static final int SMILEY_GOOD = 2;
    private final ReputationAdapter.ReputationListener reputationListener;

    ImageView userAvatar;
    TextView name;
    ReputationView reputationView;
    View deadlineLayout;
    TextView deadline;
    View lockedLayout;
    TextView promptMessage;
    View favoriteButton;
    TextView favoriteText;
    ImageView favoriteIcon;
    TextView changeButton;
    RecyclerView smiley;
    TextView opponentSmileyText;
    ImageView opponentSmiley;
    ReputationAdapter adapter;
    GridLayoutManager gridLayout;
    LinearLayoutManager linearLayoutManager;

    @LayoutRes
    public static final int LAYOUT = R.layout.inbox_reputation_detail_header;

    public InboxReputationDetailHeaderViewHolder(View itemView,
                                                 final ReputationAdapter.ReputationListener reputationListener) {
        super(itemView);
        this.reputationListener = reputationListener;
        userAvatar = (ImageView) itemView.findViewById(R.id.user_avatar);
        name = (TextView) itemView.findViewById(R.id.name);
        reputationView = (ReputationView) itemView.findViewById(R.id.reputation);
        deadline = (TextView) itemView.findViewById(R.id.deadline_text);
        deadlineLayout = itemView.findViewById(R.id.deadline);
        lockedLayout = itemView.findViewById(R.id.locked);
        promptMessage = (TextView) itemView.findViewById(R.id.prompt_text);
        favoriteButton = itemView.findViewById(R.id.favorite_button);
        favoriteText = (TextView) itemView.findViewById(R.id.favorite_text);
        favoriteIcon = (ImageView) itemView.findViewById(R.id.favorite_icon);
        changeButton = (TextView) itemView.findViewById(R.id.change_button);
        smiley = (RecyclerView) itemView.findViewById(R.id.smiley);
        opponentSmileyText = (TextView) itemView.findViewById(R.id.opponent_smiley_text);
        opponentSmiley = (ImageView) itemView.findViewById(R.id.opponent_smiley);
        adapter = ReputationAdapter.createInstance(reputationListener);
        gridLayout = new GridLayoutManager(itemView.getContext(), 3,
                LinearLayoutManager.VERTICAL, false);
        linearLayoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager
                .HORIZONTAL, false);
        smiley.setLayoutManager(gridLayout);
        smiley.setAdapter(adapter);
    }

    @Override
    public void bind(final InboxReputationDetailHeaderViewModel element) {
        ImageHandler.LoadImage(userAvatar, element.getAvatarImage());
        userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToInfoPage(element);
            }
        });
        name.setText(MethodChecker.fromHtml(element.getName()));
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToInfoPage(element);
            }
        });

        setReputation(element);

        if (!TextUtils.isEmpty(element.getDeadline()) && element.getReputationDataViewModel()
                .isShowLockingDeadline()) {
            deadline.setText(element.getDeadline());
            deadlineLayout.setVisibility(View.VISIBLE);
        } else {
            deadlineLayout.setVisibility(View.GONE);
        }

        if (element.getReputationDataViewModel().isInserted()) {
            lockedLayout.setVisibility(View.GONE);
            promptMessage.setText(MainApplication.getAppContext().getString(R.string.your_scoring));
            smiley.setLayoutManager(linearLayoutManager);
            setSmiley(element, adapter);
        } else if (element.getReputationDataViewModel().isLocked()) {
            lockedLayout.setVisibility(View.VISIBLE);
            promptMessage.setText(MainApplication.getAppContext().getString(R.string.your_scoring));
            smiley.setLayoutManager(linearLayoutManager);
            adapter.showLockedSmiley();
        } else {
            smiley.setLayoutManager(gridLayout);
            adapter.showAllSmiley();
            promptMessage.setText(getPromptText(element));
        }

        if (element.getReputationDataViewModel().isEditable()) {
            changeButton.setVisibility(View.VISIBLE);
            changeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.showChangeSmiley(element.getReputationDataViewModel().getReviewerScore());
                }
            });
        }
        else
            changeButton.setVisibility(View.GONE);

        if (element.getRevieweeBadgeSellerViewModel().getIsFavorited() != -1
                && element.getRole() ==
                InboxReputationItemViewModel.ROLE_SELLER) {
            favoriteButton.setVisibility(View.VISIBLE);
            setFavorite(element.getRevieweeBadgeSellerViewModel());
            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reputationListener.onFavoriteShopClicked(element.getShopId());
                }
            });
        } else {
            favoriteButton.setVisibility(View.GONE);
        }

        setSmileyOpponent(element);

    }

    private void goToInfoPage(InboxReputationDetailHeaderViewModel element) {
        if(element.getRole() ==  InboxReputationItemViewModel.ROLE_SELLER){
            reputationListener.onGoToShopDetail(element.getShopId());
        }else{
            reputationListener.onGoToPeopleProfile(element.getUserId());
        }
    }

    private void setFavorite(RevieweeBadgeSellerViewModel revieweeBadgeSellerViewModel) {
        if (revieweeBadgeSellerViewModel.getIsFavorited() == 1) {
            MethodChecker.setBackground(favoriteButton, MethodChecker.getDrawable(favoriteButton
                    .getContext(), R.drawable.white_button_rounded));
            ImageHandler.loadImageWithIdWithoutPlaceholder(favoriteIcon, R.drawable.shop_list_favorite_check);
            favoriteText.setTextColor(MethodChecker.getColor(favoriteText.getContext(), R.color
                    .grey_500));
            favoriteText.setText(R.string.already_favorite);
        } else {
            MethodChecker.setBackground(favoriteButton, MethodChecker.getDrawable(favoriteButton
                    .getContext(), R.drawable.green_button_rounded));
            ImageHandler.loadImageWithIdWithoutPlaceholder(favoriteIcon, R.drawable.ic_new_action_plus);
            favoriteText.setTextColor(MethodChecker.getColor(favoriteText.getContext(), R.color
                    .white));
            favoriteText.setText(R.string.favorite_button);
        }
    }

    private void setSmileyOpponent(InboxReputationDetailHeaderViewModel element) {
        switch (element.getReputationDataViewModel().getRevieweeScore()) {
            case NO_REPUTATION:
                ImageHandler.loadImageWithIdWithoutPlaceholder(opponentSmiley, R.drawable.ic_smiley_empty);
                opponentSmileyText.setText(MainApplication.getAppContext().getString(R.string
                        .seller_has_not_review));
                break;
            case SMILEY_BAD:
                ImageHandler.loadImageWithIdWithoutPlaceholder(opponentSmiley, R.drawable.ic_smiley_bad);
                opponentSmileyText.setText(MainApplication.getAppContext().getString(R.string
                        .score_from_seller));
                break;
            case SMILEY_NEUTRAL:
                ImageHandler.loadImageWithIdWithoutPlaceholder(opponentSmiley, R.drawable.ic_smiley_netral);
                opponentSmileyText.setText(MainApplication.getAppContext().getString(R.string
                        .score_from_seller));
                break;
            case SMILEY_GOOD:
                ImageHandler.loadImageWithIdWithoutPlaceholder(opponentSmiley, R.drawable.ic_smiley_good);
                opponentSmileyText.setText(MainApplication.getAppContext().getString(R.string
                        .score_from_seller));
                break;
        }
    }

    private void setSmiley(InboxReputationDetailHeaderViewModel element, ReputationAdapter adapter) {
        switch (element.getReputationDataViewModel().getReviewerScore()) {
            case SMILEY_BAD:
                adapter.showSmileyBad();
                break;
            case SMILEY_NEUTRAL:
                adapter.showSmileyNeutral();
                break;
            case SMILEY_GOOD:
                adapter.showSmileyGood();
                break;
        }
    }

    private String getPromptText(InboxReputationDetailHeaderViewModel element) {
        return MainApplication.getAppContext().getString(R.string
                .reputation_prompt) + " " + element.getName() + "?";
    }

    public void setReputation(InboxReputationDetailHeaderViewModel element) {
        if (element.getRole() == InboxReputationItemViewModel.ROLE_BUYER) {
            reputationView.setBuyer(
                    element.getRevieweeBadgeCustomerViewModel().getPositivePercentage(),
                    element.getRevieweeBadgeCustomerViewModel().getPositive(),
                    element.getRevieweeBadgeCustomerViewModel().getNeutral(),
                    element.getRevieweeBadgeCustomerViewModel().getNegative(),
                    element.getRevieweeBadgeCustomerViewModel().getNoReputation());
        } else {
            reputationView.setSeller(
                    element.getRevieweeBadgeSellerViewModel().getReputationBadge().getSet(),
                    element.getRevieweeBadgeSellerViewModel().getReputationBadge().getLevel(),
                    String.valueOf(element.getRevieweeBadgeSellerViewModel().getScore())
            );

        }
    }
}
