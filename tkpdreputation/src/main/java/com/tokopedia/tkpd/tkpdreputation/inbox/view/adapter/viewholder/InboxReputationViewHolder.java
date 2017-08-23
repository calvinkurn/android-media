package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.customview.ReputationView;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputation;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.InboxReputationItemViewModel;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationViewHolder extends AbstractViewHolder<InboxReputationItemViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.inbox_reputation_item;
    private final InboxReputation.View viewListener;

    private TextView textDeadline;
    private View deadline;
    private TextView invoice;
    private ImageView notification;
    private ImageView avatar;
    private TextView name;
    private ReputationView reputation;
    private TextView date;
    private TextView action;

    public InboxReputationViewHolder(View itemView, InboxReputation.View viewListener) {
        super(itemView);
        textDeadline = (TextView) itemView.findViewById(R.id.deadline_text);
        deadline = itemView.findViewById(R.id.deadline);
        invoice = (TextView) itemView.findViewById(R.id.invoice);
        notification = (ImageView) itemView.findViewById(R.id.notification);
        avatar = (ImageView) itemView.findViewById(R.id.avatar);
        name = (TextView) itemView.findViewById(R.id.name);
        reputation = (ReputationView) itemView.findViewById(R.id.reputation);
        date = (TextView) itemView.findViewById(R.id.date);
        action = (TextView) itemView.findViewById(R.id.action);

        this.viewListener = viewListener;

    }

    @Override
    public void bind(final InboxReputationItemViewModel element) {

        name.setText(element.getRevieweeName());
        date.setText(element.getCreateTime());
        invoice.setText(element.getInvoice());
        ImageHandler.LoadImage(avatar, element.getRevieweePicture());
        setDeadline(element);
//        setReputation(holder, position);
        setNotification(element.showBookmark());
        setAction(element);

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToDetail(
                        element.getReputationId(),
                        getAdapterPosition());
            }
        });
    }


    private void setAction(InboxReputationItemViewModel inboxReputationItemViewModel) {
        action.setText(inboxReputationItemViewModel.getActionMessage());
    }

    private void setNotification(boolean showBookmark) {
        if (showBookmark)
            notification.setVisibility(View.VISIBLE);
        else
            notification.setVisibility(View.GONE);
    }

    private void setReputation() {

    }

    private void setDeadline(InboxReputationItemViewModel element) {
        if (element.showLockingDeadline()) {
            deadline.setVisibility(View.VISIBLE);
            textDeadline.setText(getTextDeadline(element));
        } else {
            deadline.setVisibility(View.GONE);
        }
    }

    private String getTextDeadline(InboxReputationItemViewModel element) {
        return MainApplication.getAppContext().getString(R.string.deadline_prefix)
                + element.getReputationDaysLeft() + " " +
                MainApplication.getAppContext().getString(R.string.deadline_suffix);
    }
}
