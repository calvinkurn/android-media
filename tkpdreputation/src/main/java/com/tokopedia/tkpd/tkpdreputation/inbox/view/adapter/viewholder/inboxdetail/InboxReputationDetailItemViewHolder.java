package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder.inboxdetail;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.ImageUploadAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationDetail;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageAttachmentViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailItemViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.LikeDislikeViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ReviewResponseViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailItemViewHolder extends
        AbstractViewHolder<InboxReputationDetailItemViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.inbox_reputation_detail_item;
    private static final int MAX_CHAR = 50;
    private static final String MORE_DESCRIPTION = "<font color='#42b549'>Selengkapnya</font>";
    private static final String BY = "Oleh";

    private final InboxReputationDetail.View viewListener;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm", Locale.getDefault());
    private SimpleDateFormat newSdf = new SimpleDateFormat("dd MMM ", Locale.getDefault());
    boolean isReplyOpened = false;

    TextView productName;
    ImageView productAvatar;
    TextView emptyReviewText;
    View viewReview;
    TextView reviewerName;
    TextView reviewTime;
    RecyclerView reviewAttachment;
    ImageView reviewOverflow;
    TextView review;
    RatingBar reviewStar;
    View giveReview;
    Context context;
    ImageUploadAdapter adapter;

    View helpfulLayout;
    TextView helpfulText;
    TextView seeReplyText;
    ImageView helpfulIcon;
    ImageView replyArrow;

    View sellerReplyLayout;
    TextView sellerName;
    TextView sellerReplyTime;
    TextView sellerReply;
    ImageView replyOverflow;

    View sellerAddReplyLayout;
    EditText sellerAddReplyEditText;
    ImageView sendReplyButton;

    public InboxReputationDetailItemViewHolder(View itemView,
                                               InboxReputationDetail.View viewListener) {
        super(itemView);
        context = itemView.getContext();
        this.viewListener = viewListener;
        productName = (TextView) itemView.findViewById(R.id.product_name);
        productAvatar = (ImageView) itemView.findViewById(R.id.product_image);
        emptyReviewText = (TextView) itemView.findViewById(R.id.empty_review_text);
        viewReview = itemView.findViewById(R.id.review_layout);
        reviewerName = (TextView) itemView.findViewById(R.id.reviewer_name);
        reviewTime = (TextView) itemView.findViewById(R.id.review_time);
        reviewAttachment = (RecyclerView) itemView.findViewById(R.id.product_review_image);
        reviewOverflow = (ImageView) itemView.findViewById(R.id.review_overflow);
        review = (TextView) itemView.findViewById(R.id.review);
        reviewStar = (RatingBar) itemView.findViewById(R.id.product_rating);
        giveReview = itemView.findViewById(R.id.add_review_layout);
        adapter = ImageUploadAdapter.createAdapter(itemView.getContext());
        adapter.setCanUpload(false);
        adapter.setListener(onImageClicked());
        reviewAttachment.setLayoutManager(new LinearLayoutManager(itemView.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        reviewAttachment.setAdapter(adapter);

        helpfulLayout = itemView.findViewById(R.id.review_detail_helpful_layout);
        helpfulText = (TextView) helpfulLayout.findViewById(R.id.helpful_text);
        seeReplyText = (TextView) helpfulLayout.findViewById(R.id.see_reply_button);
        replyArrow = (ImageView) helpfulLayout.findViewById(R.id.reply_chevron);

        sellerReplyLayout = itemView.findViewById(R.id.seller_reply_layout);
        sellerName = (TextView) itemView.findViewById(R.id.seller_reply_name);
        sellerReplyTime = (TextView) itemView.findViewById(R.id.seller_reply_time);
        sellerReply = (TextView) itemView.findViewById(R.id.seller_reply);
        replyOverflow = (ImageView) itemView.findViewById(R.id.reply_overflow);

        sellerAddReplyLayout = itemView.findViewById(R.id.seller_add_reply_layout);
        sellerAddReplyEditText = (EditText) itemView.findViewById(R.id.seller_reply_edit_text);
        sendReplyButton = (ImageView) itemView.findViewById(R.id.send_button);
        helpfulIcon = (ImageView) itemView.findViewById(R.id.helpful_icon);

        sellerAddReplyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(sellerAddReplyEditText.getText().toString())) {
                    ImageHandler.loadImageWithIdWithoutPlaceholder(sendReplyButton, R.drawable.ic_send_grey_transparent);
                    sendReplyButton.setEnabled(false);
                } else {
                    ImageHandler.loadImageWithIdWithoutPlaceholder(sendReplyButton, R.drawable.ic_send_green);
                    sendReplyButton.setEnabled(true);
                }

            }
        });
    }

    private ImageUploadAdapter.ProductImageListener onImageClicked() {
        return new ImageUploadAdapter.ProductImageListener() {
            @Override
            public View.OnClickListener onUploadClicked(int position) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                };
            }

            @Override
            public View.OnClickListener onImageClicked(final int position, ImageUpload imageUpload) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewListener.goToPreviewImage(position, adapter.getList());
                    }
                };
            }
        };
    }

    @Override
    public void bind(final InboxReputationDetailItemViewModel element) {
        productName.setText(MethodChecker.fromHtml(element.getProductName()));
        productName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToProductDetail(element.getProductId());
            }
        });

        ImageHandler.LoadImage(productAvatar, element.getProductAvatar());
        productAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToProductDetail(element.getProductId());
            }
        });

        if (element.getTab() == InboxReputationActivity.TAB_BUYER_REVIEW
                || element.isReviewSkipped()) {
            giveReview.setVisibility(View.GONE);
        } else {
            giveReview.setVisibility(View.VISIBLE);
        }

        if (!element.isReviewHasReviewed()) {
            viewReview.setVisibility(View.GONE);
            helpfulLayout.setVisibility(View.GONE);
            emptyReviewText.setVisibility(View.VISIBLE);
        } else {
            emptyReviewText.setVisibility(View.GONE);
            viewReview.setVisibility(View.VISIBLE);
            giveReview.setVisibility(View.GONE);
            reviewerName.setText(MethodChecker.fromHtml(getReviewerNameText(element
                    .getReviewerName())));
            reviewTime.setText(getFormattedTime(element.getReviewTime()));
            reviewStar.setRating(element.getReviewStar());
            review.setText(getReview(element.getReview()));
            review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (review.getText().toString().endsWith(MainApplication.getAppContext().getString(R.string.more_to_complete))) {
                        review.setText(element.getReview());
                    }

                }
            });
            reviewOverflow.setOnClickListener(onReviewOverflowClicked(element));

            setHelpful(element);

            if (element.getReviewResponseViewModel() != null
                    && !TextUtils.isEmpty(element.getReviewResponseViewModel().getResponseMessage())) {
                setSellerReply(element);
            } else if (element.getTab() == InboxReputationActivity.TAB_BUYER_REVIEW) {
                seeReplyText.setVisibility(View.GONE);
                replyArrow.setVisibility(View.GONE);
                sellerReplyLayout.setVisibility(View.GONE);
                sellerAddReplyLayout.setVisibility(View.VISIBLE);
            } else {
                seeReplyText.setVisibility(View.GONE);
                replyArrow.setVisibility(View.GONE);
                sellerReplyLayout.setVisibility(View.GONE);
                sellerAddReplyLayout.setVisibility(View.GONE);
            }


        }

        giveReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToGiveReview(element.getReviewId(),
                        element.getProductId(),
                        element.getShopId(),
                        element.isReviewIsSkippable(),
                        element.getProductAvatar(),
                        element.getProductName(),
                        element.getProductUrl());
            }
        });

        adapter.addList(convertToAdapterViewModel(element.getReviewAttachment()));
        adapter.notifyDataSetChanged();
        sendReplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onSendReplyReview(element, sellerAddReplyEditText.getText().toString());
            }
        });


    }

    private void setSellerReply(final InboxReputationDetailItemViewModel element) {
        seeReplyText.setVisibility(View.VISIBLE);
        replyArrow.setVisibility(View.VISIBLE);

        seeReplyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleReply();
            }
        });
        replyArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleReply();
            }
        });

        ReviewResponseViewModel reviewResponseViewModel = element.getReviewResponseViewModel();
        sellerName.setText(MethodChecker.fromHtml(getFormattedReplyName(reviewResponseViewModel
                .getResponseBy())));
        sellerReplyTime.setText(getFormattedTime(reviewResponseViewModel.getResponseCreateTime()));
        sellerReply.setText(MethodChecker.fromHtml(reviewResponseViewModel.getResponseMessage()));
        if (element.getTab() == InboxReputationActivity.TAB_BUYER_REVIEW) {
            helpfulLayout.setVisibility(View.VISIBLE);
            replyOverflow.setVisibility(View.VISIBLE);
            replyOverflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popup = new PopupMenu(context, v);
                    popup.getMenu().add(1, R.id.menu_delete, 1,
                            MainApplication.getAppContext()
                                    .getString(R.string.menu_delete));

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.menu_delete) {
                                viewListener.onDeleteReviewResponse(element);
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });

                    popup.show();

                }
            });
        } else
            replyOverflow.setVisibility(View.GONE);
    }

    private void toggleReply() {
        isReplyOpened = !isReplyOpened;
        if (isReplyOpened) {
            seeReplyText.setText(MainApplication.getAppContext().getText(R.string.close_reply));
            replyArrow.setRotation(180);
            sellerReplyLayout.setVisibility(View.VISIBLE);
        } else {
            seeReplyText.setText(MainApplication.getAppContext().getText(R.string.see_reply));
            replyArrow.setRotation(0);
            sellerReplyLayout.setVisibility(View.GONE);
        }


    }

    private String getFormattedReplyName(String responseBy) {
        return BY + " <b>" + responseBy + "</b>";
    }

    private String getFormattedTime(String reviewTime) {
        try {
            return newSdf.format(sdf.parse(reviewTime.replace("WIB", "")));
        } catch (ParseException e) {
            e.printStackTrace();
            return reviewTime;
        }
    }

    private Spanned getReview(String review) {
        if (MethodChecker.fromHtml(review).length() > MAX_CHAR) {
            String subDescription = MethodChecker.fromHtml(review).toString().substring(0, MAX_CHAR);
            return MethodChecker.fromHtml(subDescription.replaceAll("(\r\n|\n)", "<br />") + "... "
                    + MORE_DESCRIPTION);
        } else {
            return MethodChecker.fromHtml(review);
        }
    }

    private ArrayList<ImageUpload> convertToAdapterViewModel(List<ImageAttachmentViewModel> reviewAttachment) {
        ArrayList<ImageUpload> list = new ArrayList<>();
        for (ImageAttachmentViewModel vm : reviewAttachment) {
            list.add(new ImageUpload(
                    vm.getUriThumbnail(),
                    vm.getUriLarge(),
                    vm.getDescription(),
                    String.valueOf(vm.getAttachmentId())));
        }
        return list;
    }

    private String getReviewerNameText(String reviewerName) {
        return MainApplication.getAppContext().getString(R.string.by) + " " + reviewerName;
    }

    private View.OnClickListener onReviewOverflowClicked(final InboxReputationDetailItemViewModel element) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);
                if (element.isReviewIsEditable())
                    popup.getMenu().add(1, R.id.menu_edit, 1, MainApplication.getAppContext()
                            .getString(R.string.menu_edit));

                if (element.getTab() == InboxReputationActivity.TAB_BUYER_REVIEW)
                    popup.getMenu().add(1, R.id.menu_report, 2, MainApplication.getAppContext()
                            .getString(R.string.menu_report));

                if (element.getTab() != InboxReputationActivity.TAB_BUYER_REVIEW)
                    popup.getMenu().add(1, R.id.menu_share, 3, MainApplication.getAppContext()
                            .getString(R.string.menu_share));

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu_edit) {
                            viewListener.onEditReview(element);
                            return true;
                        } else if (item.getItemId() == R.id.menu_report) {
                            viewListener.onGoToReportReview(
                                    element.getShopId(),
                                    element.getReviewId()
                            );
                            return true;
                        } else if (item.getItemId() == R.id.menu_share) {
                            viewListener.onShareReview(
                                    element.getProductName(),
                                    element.getProductAvatar(),
                                    element.getProductUrl(),
                                    element.getReview()
                            );
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

    public void setHelpful(final InboxReputationDetailItemViewModel element) {

        final LikeDislikeViewModel likeDislike = element.getLikeDislikeViewModel();
        String helpfulString = "";

        if (likeDislike != null
                && likeDislike.getTotalLike() == 1
                && likeDislike.getLikeStatus() == InboxReputationDetailItemViewModel.IS_LIKED
                && element.getTab() == InboxReputationActivity.TAB_BUYER_REVIEW) {
            setHelpfulVisible();
            ImageHandler.loadImageWithIdWithoutPlaceholder(helpfulIcon, R.drawable
                    .ic_thumbsup_active);
            helpfulString += MainApplication.getAppContext().getString(R.string.you_got_helped);
        } else if (likeDislike != null
                && likeDislike.getTotalLike() > 1
                && likeDislike.getLikeStatus() == InboxReputationDetailItemViewModel.IS_LIKED
                && element.getTab() == InboxReputationActivity.TAB_BUYER_REVIEW) {
            setHelpfulVisible();
            ImageHandler.loadImageWithIdWithoutPlaceholder(helpfulIcon, R.drawable
                    .ic_thumbsup_active);
            helpfulString += MainApplication.getAppContext().getString(R.string.You_and)
                    + " " + likeDislike.getTotalLike() + " " + MainApplication.getAppContext()
                    .getString(R.string.people_helped);
        } else if (likeDislike != null
                && likeDislike.getTotalLike() != 0
                && likeDislike.getLikeStatus() != InboxReputationDetailItemViewModel.IS_LIKED) {
            setHelpfulVisible();
            ImageHandler.loadImageWithIdWithoutPlaceholder(helpfulIcon, R.drawable
                    .ic_thumbsup);
            helpfulString += likeDislike.getTotalDislike() + " " + MainApplication.getAppContext()
                    .getString(R.string.people_helped);
        } else if (element.getTab() == InboxReputationActivity.TAB_BUYER_REVIEW) {
            setHelpfulVisible();
            ImageHandler.loadImageWithIdWithoutPlaceholder(helpfulIcon, R.drawable
                    .ic_thumbsup);
            helpfulString += MainApplication.getAppContext().getString(R.string
                    .helpful_question);
        } else {
            helpfulIcon.setVisibility(View.GONE);
            helpfulText.setVisibility(View.GONE);
            helpfulLayout.setVisibility(View.GONE);
        }

        helpfulText.setText(helpfulString);
        helpfulIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (element.getTab() == InboxReputationActivity.TAB_BUYER_REVIEW) {
                    viewListener.onLikeReview(getAdapterPosition(),
                            element.getReviewId(),
                            likeDislike.getLikeStatus(),
                            element.getProductId(),
                            String.valueOf(element.getShopId()));
                }
            }
        });

    }

    private void setHelpfulVisible() {
        helpfulIcon.setVisibility(View.VISIBLE);
        helpfulText.setVisibility(View.VISIBLE);
        helpfulLayout.setVisibility(View.VISIBLE);
    }
}
