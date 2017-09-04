package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder.inboxdetail;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationDetail;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailItemViewModel;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailItemViewHolder extends
        AbstractViewHolder<InboxReputationDetailItemViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.inbox_reputation_detail_item;
    private final InboxReputationDetail.View viewListener;

    TextView productName;
    ImageView productAvatar;
    View viewReview;
    TextView reviewerName;
    TextView reviewTime;
    RecyclerView reviewAttachment;
    ImageView reviewOverflow;
    TextView review;
    RatingBar reviewStar;
    View giveReview;
    Context context;

    public InboxReputationDetailItemViewHolder(View itemView,
                                               InboxReputationDetail.View viewListener) {
        super(itemView);
        context = itemView.getContext();
        this.viewListener = viewListener;
        productName = (TextView) itemView.findViewById(R.id.product_name);
        productAvatar = (ImageView) itemView.findViewById(R.id.product_image);
        viewReview = itemView.findViewById(R.id.review_layout);
        reviewerName = (TextView) itemView.findViewById(R.id.reviewer_name);
        reviewTime = (TextView) itemView.findViewById(R.id.review_time);
        reviewAttachment = (RecyclerView) itemView.findViewById(R.id.product_review_image);
        reviewOverflow = (ImageView) itemView.findViewById(R.id.review_overflow);
        review = (TextView) itemView.findViewById(R.id.review);
        reviewStar = (RatingBar) itemView.findViewById(R.id.product_rating);
        giveReview = itemView.findViewById(R.id.add_review_layout);
    }

    @Override
    public void bind(final InboxReputationDetailItemViewModel element) {
        productName.setText(element.getProductName());
        ImageHandler.LoadImage(productAvatar, element.getProductAvatar());

        if (!element.isReviewHasReviewed()) {
            viewReview.setVisibility(View.GONE);
            giveReview.setVisibility(View.VISIBLE);
        } else if (element.isReviewSkipped()) {

        } else {
            viewReview.setVisibility(View.VISIBLE);
            giveReview.setVisibility(View.GONE);
            reviewerName.setText(getReviewerNameText(element.getReviewerName()));
            reviewTime.setText(element.getReviewTime());
            reviewStar.setRating(element.getReviewStar());
            review.setText(element.getReview());
            setOverflowButton(element);
        }

        giveReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToGiveReview(element.getReviewId(),
                        element.getProductId(),
                        element.getShopId());
            }
        });

    }

    private String getReviewerNameText(String reviewerName) {
        return MainApplication.getAppContext().getString(R.string.by) + " " + reviewerName;
    }

    public void setOverflowButton(InboxReputationDetailItemViewModel element) {
        if (element.isReviewIsEditable()) {
            reviewOverflow.setVisibility(View.VISIBLE);
            reviewOverflow.setOnClickListener(onOverflowClicked(element));
        } else {
            reviewOverflow.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener onOverflowClicked(final InboxReputationDetailItemViewModel element) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);
                if (element.isReviewIsEditable())
                    popup.getMenu().add(1, R.id.menu_edit, 1, MainApplication.getAppContext()
                            .getString(R.string.menu_edit));

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu_edit) {
                            viewListener.onEditReview();
                            return true;
                        } else {
                            return false;
                        }
                    }

                });

                popup.show();


            }
        };
    }

}
