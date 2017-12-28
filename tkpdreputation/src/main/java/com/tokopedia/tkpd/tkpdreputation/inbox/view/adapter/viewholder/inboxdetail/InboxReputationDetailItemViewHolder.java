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
import com.tokopedia.core.util.TimeConverter;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.ImageUploadAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationDetail;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageAttachmentViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailItemViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ReviewResponseViewModel;

import java.util.ArrayList;
import java.util.List;

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

    View replyReviewLayout;
    View seeReplyLayout;
    TextView seeReplyText;
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
                                               final InboxReputationDetail.View viewListener) {
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

        sellerReplyLayout = itemView.findViewById(R.id.seller_reply_layout);
        seeReplyLayout = itemView.findViewById(R.id.see_reply_layout);
        seeReplyText = (TextView) seeReplyLayout.findViewById(R.id.see_reply_button);
        replyArrow = (ImageView) seeReplyLayout.findViewById(R.id.reply_chevron);

        replyReviewLayout = itemView.findViewById(R.id.reply_review_layout);
        sellerName = (TextView) itemView.findViewById(R.id.seller_reply_name);
        sellerReplyTime = (TextView) itemView.findViewById(R.id.seller_reply_time);
        sellerReply = (TextView) itemView.findViewById(R.id.seller_reply);
        replyOverflow = (ImageView) itemView.findViewById(R.id.reply_overflow);

        sellerAddReplyLayout = itemView.findViewById(R.id.seller_add_reply_layout);
        sellerAddReplyEditText = (EditText) itemView.findViewById(R.id.seller_reply_edit_text);
        sendReplyButton = (ImageView) itemView.findViewById(R.id.send_button);

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
        if (element.isProductDeleted()) {
            productName.setText(
                    MainApplication.getAppContext().getString(R.string.product_is_deleted));

            ImageHandler.loadImageWithIdWithoutPlaceholder(productAvatar, R.drawable.ic_product_deleted);
        } else if (element.isProductBanned()) {
            productName.setText(
                    MainApplication.getAppContext().getString(R.string.product_is_banned));

            ImageHandler.loadImageWithIdWithoutPlaceholder(productAvatar, R.drawable.ic_product_deleted);
        } else {
            productName.setText(MethodChecker.fromHtml(element.getProductName()));
            productName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewListener.onGoToProductDetail(element.getProductId(), element
                            .getProductAvatar(), element.getProductName());
                }
            });

            ImageHandler.LoadImage(productAvatar, element.getProductAvatar());
            productAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewListener.onGoToProductDetail(element.getProductId(),
                            element.getProductAvatar(), element.getProductName());
                }
            });
        }

        if (element.getTab() == InboxReputationActivity.TAB_BUYER_REVIEW
                || element.isReviewSkipped()) {
            giveReview.setVisibility(View.GONE);
        } else {
            giveReview.setVisibility(View.VISIBLE);
        }

        if (!element.isReviewHasReviewed()) {
            viewReview.setVisibility(View.GONE);
            seeReplyLayout.setVisibility(View.GONE);
            emptyReviewText.setVisibility(View.VISIBLE);
            emptyReviewText.setText(R.string.not_reviewed);
        } else if (element.isReviewHasReviewed() && element.isReviewSkipped()) {
            emptyReviewText.setVisibility(View.VISIBLE);
            viewReview.setVisibility(View.GONE);
            seeReplyLayout.setVisibility(View.GONE);
            emptyReviewText.setVisibility(View.VISIBLE);
            emptyReviewText.setText(R.string.review_is_skipped);
        } else {
            emptyReviewText.setVisibility(View.GONE);
            viewReview.setVisibility(View.VISIBLE);
            giveReview.setVisibility(View.GONE);

            reviewerName.setText(MethodChecker.fromHtml(getReviewerNameText(element)));
            reviewerName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewListener.onGoToProfile(element.getReviewerId());
                }
            });

            String time;

            if (element.isReviewIsEdited()) {
                time = getFormattedTime(element.getReviewTime()) +
                        MainApplication.getAppContext().getString(R.string.edited);
            } else {
                time = getFormattedTime(element.getReviewTime());
            }
            reviewTime.setText(time);

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

            if (canShowOverflow(element)) {
                reviewOverflow.setVisibility(View.VISIBLE);
                reviewOverflow.setOnClickListener(onReviewOverflowClicked(element));
            } else {
                reviewOverflow.setVisibility(View.GONE);
            }

            if (element.getReviewResponseViewModel() != null
                    && !TextUtils.isEmpty(element.getReviewResponseViewModel().getResponseMessage())) {
                setSellerReply(element);
            } else {
                seeReplyText.setVisibility(View.GONE);
                replyArrow.setVisibility(View.GONE);
                sellerReplyLayout.setVisibility(View.GONE);

                if (element.getTab() == InboxReputationActivity.TAB_BUYER_REVIEW) {
                    sellerAddReplyLayout.setVisibility(View.VISIBLE);
                } else {
                    sellerAddReplyLayout.setVisibility(View.GONE);
                }
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
                        element.getProductUrl(),
                        element.getRevieweeName(),
                        element.getproductStatus());
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
        sellerAddReplyLayout.setVisibility(View.GONE);
        sellerReplyLayout.setVisibility(View.VISIBLE);
        seeReplyLayout.setVisibility(View.VISIBLE);
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
        sellerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToShopInfo(element.getShopId());
            }
        });
        sellerReplyTime.setText(getFormattedTime(reviewResponseViewModel.getResponseCreateTime()));
        sellerReply.setText(MethodChecker.fromHtml(reviewResponseViewModel.getResponseMessage()));
        sellerAddReplyEditText.setText("");
        if (element.getTab() == InboxReputationActivity.TAB_BUYER_REVIEW) {
            seeReplyLayout.setVisibility(View.VISIBLE);
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
            replyReviewLayout.setVisibility(View.VISIBLE);
            viewListener.onSmoothScrollToReplyView(getAdapterPosition());
        } else {
            seeReplyText.setText(MainApplication.getAppContext().getText(R.string.see_reply));
            replyArrow.setRotation(0);
            replyReviewLayout.setVisibility(View.GONE);
        }

    }

    private String getFormattedReplyName(String responseBy) {
        return BY + " <b>" + responseBy + "</b>";
    }

    private String getFormattedTime(String reviewTime) {
        return TimeConverter.generateTimeYearly(reviewTime.replace("WIB", ""));
    }

    private Spanned getReview(String review) {
        if (MethodChecker.fromHtml(review).length() > MAX_CHAR) {
            String subDescription = MethodChecker.fromHtml(review).toString().substring(0, MAX_CHAR);
            return MethodChecker
                    .fromHtml(subDescription.replaceAll("(\r\n|\n)", "<br />") + "... "
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

    private String getReviewerNameText(InboxReputationDetailItemViewModel element) {
        if (element.isReviewIsAnonymous()
                && element.getTab() != InboxReputationActivity.TAB_BUYER_REVIEW) {
            return MainApplication.getAppContext().getString(R.string.by) + " " +
                    getAnonymousName(element.getReviewerName());
        } else {
            return MainApplication.getAppContext().getString(R.string.by) + " " +
                    element.getReviewerName();
        }
    }

    private String getAnonymousName(String name) {
        String first = name.substring(0, 1);
        String last = name.substring(name.length() - 1);
        return first + "***" + last;
    }


    private boolean canShowOverflow(InboxReputationDetailItemViewModel element) {
        return element.isReviewIsEditable()
                || element.getTab() == InboxReputationActivity.TAB_BUYER_REVIEW
                || !TextUtils.isEmpty(element.getProductName());
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

                if (!TextUtils.isEmpty(element.getProductName()))
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
}
