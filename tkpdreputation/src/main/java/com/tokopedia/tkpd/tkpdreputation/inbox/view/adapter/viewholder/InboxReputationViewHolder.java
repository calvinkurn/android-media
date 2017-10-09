package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.reputation.ReputationView;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputation;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.InboxReputationItemViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationViewHolder extends AbstractViewHolder<InboxReputationItemViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.inbox_reputation_item;
    private final InboxReputation.View viewListener;

    private View mainView;
    private TextView textDeadline;
    private ImageView deadline;
    private TextView invoice;
    private ImageView avatar;
    private TextView name;
    private ReputationView reputation;
    private TextView date;
    private TextView action;
    private ImageView unreadNotification;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm", Locale.getDefault());
    private SimpleDateFormat newSdf = new SimpleDateFormat("dd MMM", Locale.getDefault());


    public InboxReputationViewHolder(View itemView, InboxReputation.View viewListener) {
        super(itemView);
        mainView = itemView.findViewById(R.id.main_view);
        textDeadline = (TextView) itemView.findViewById(R.id.deadline_text);
        deadline = (ImageView) itemView.findViewById(R.id.icon_deadline);
        invoice = (TextView) itemView.findViewById(R.id.invoice);
        avatar = (ImageView) itemView.findViewById(R.id.avatar);
        name = (TextView) itemView.findViewById(R.id.name);
        reputation = (ReputationView) itemView.findViewById(R.id.reputation);
        date = (TextView) itemView.findViewById(R.id.date);
        action = (TextView) itemView.findViewById(R.id.action);
        unreadNotification = (ImageView) itemView.findViewById(R.id.unread_notif);
        this.viewListener = viewListener;

    }

    @Override
    public void bind(final InboxReputationItemViewModel element) {

        name.setText(element.getRevieweeName());
        date.setText(getDate(element.getCreateTime()));
        invoice.setText(element.getInvoice());
        ImageHandler.LoadImage(avatar, element.getRevieweePicture());
        setDeadline(element);
        setReputation(element);
        setAction(element);
        setUnreadNotification(element);

        mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToDetail(
                        element.getReputationId(),
                        element.getInvoice(),
                        element.getCreateTime(),
                        element.getRevieweeName(),
                        element.getRevieweePicture(),
                        element.getReputationDataViewModel(),
                        getTextDeadline(element),
                        getAdapterPosition(),
                        element.getRole());
            }
        });
    }

    private void setUnreadNotification(InboxReputationItemViewModel element) {
        if (element.getReputationDataViewModel().isShowBookmark()) {
            unreadNotification.setVisibility(View.VISIBLE);
        } else {
            unreadNotification.setVisibility(View.GONE);
        }
    }

    private String getDate(String createTime) {
        try {
            return newSdf.format(sdf.parse(createTime));
        } catch (ParseException e) {
            e.printStackTrace();
            return createTime;
        }
    }


    private void setAction(InboxReputationItemViewModel inboxReputationItemViewModel) {
        action.setText(inboxReputationItemViewModel.getReputationDataViewModel()
                .getActionMessage());
    }

    private void setReputation(InboxReputationItemViewModel element) {
        if (element.getRole() == InboxReputationItemViewModel.ROLE_BUYER) {
            reputation.setBuyer(
                    element.getRevieweeBadgeCustomerViewModel().getPositivePercentage(),
                    element.getRevieweeBadgeCustomerViewModel().getPositive(),
                    element.getRevieweeBadgeCustomerViewModel().getNeutral(),
                    element.getRevieweeBadgeCustomerViewModel().getNegative(),
                    element.getRevieweeBadgeCustomerViewModel().getNoReputation(),
                    false);
        } else {
            reputation.setSeller(
                    element.getRevieweeBadgeSellerViewModel().getReputationBadge().getSet(),
                    element.getRevieweeBadgeSellerViewModel().getReputationBadge().getLevel(),
                    String.valueOf(element.getRevieweeBadgeSellerViewModel().getScore()),
                    false);
        }
    }

    private void setDeadline(InboxReputationItemViewModel element) {
        if (element.getReputationDataViewModel().isShowLockingDeadline()) {
            deadline.setVisibility(View.VISIBLE);
            textDeadline.setVisibility(View.VISIBLE);
            setIconDeadline(deadline, element.getReputationDaysLeft());
        } else {
            deadline.setVisibility(View.INVISIBLE);
            textDeadline.setVisibility(View.INVISIBLE);

        }
    }

    private String getTextDeadline(InboxReputationItemViewModel element) {
        return MainApplication.getAppContext().getString(R.string.deadline_prefix)
                + " " + element.getReputationDaysLeft() + " " +
                MainApplication.getAppContext().getString(R.string.deadline_suffix);
    }

    private void setIconDeadline(ImageView deadline, String reputationDaysLeft) {
        switch (reputationDaysLeft) {
            case "1":
                ImageHandler.loadImageWithId(deadline, R.drawable.one_day_left);
                break;
            case "2":
                ImageHandler.loadImageWithId(deadline, R.drawable.two_day_left);
                break;
            case "3":
                ImageHandler.loadImageWithId(deadline, R.drawable.three_day_left);
                break;
        }
    }

}
