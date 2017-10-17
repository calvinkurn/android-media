package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.LabelUtils;
import com.tokopedia.core.util.MethodChecker;
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
    private TextView deadline;
    private TextView invoice;
    private ImageView avatar;
    private TextView name;
    private ReputationView reputation;
    private TextView date;
    private TextView action;
    private ImageView unreadNotification;
    private Locale locale = new Locale("in", "ID");
    private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm", locale);
    private SimpleDateFormat newSdf = new SimpleDateFormat("dd MMM", locale);
    private LabelUtils labelUtils;

    public InboxReputationViewHolder(View itemView, InboxReputation.View viewListener) {
        super(itemView);
        mainView = itemView.findViewById(R.id.main_view);
        textDeadline = (TextView) itemView.findViewById(R.id.deadline_text);
        deadline = (TextView) itemView.findViewById(R.id.label_deadline);
        invoice = (TextView) itemView.findViewById(R.id.invoice);
        avatar = (ImageView) itemView.findViewById(R.id.avatar);
        name = (TextView) itemView.findViewById(R.id.name);
        reputation = (ReputationView) itemView.findViewById(R.id.reputation);
        date = (TextView) itemView.findViewById(R.id.date);
        action = (TextView) itemView.findViewById(R.id.action);
        unreadNotification = (ImageView) itemView.findViewById(R.id.unread_notif);
        labelUtils = LabelUtils.getInstance(itemView.getContext(), deadline);
        this.viewListener = viewListener;

    }

    @Override
    public void bind(final InboxReputationItemViewModel element) {

        name.setText(MethodChecker.fromHtml(element.getRevieweeName()));
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

    private void setIconDeadline(TextView deadline, String reputationDaysLeft) {
        deadline.setText(reputationDaysLeft + " " + MainApplication.getAppContext().getString(R.string.deadline_suffix));

        switch (reputationDaysLeft) {
            case "1":
                labelUtils.giveLabel(LabelUtils.COLOR_RED);
                break;
            case "2":
                labelUtils.giveLabel(LabelUtils.COLOR_YELLOW);
                break;
            default:
                labelUtils.giveLabel(LabelUtils.COLOR_BLUE);
                break;
        }
    }

}
