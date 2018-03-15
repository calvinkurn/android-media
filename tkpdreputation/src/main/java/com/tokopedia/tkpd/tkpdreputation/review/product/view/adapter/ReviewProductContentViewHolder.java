package com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.util.TimeConverter;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.ImageUploadAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageAttachmentViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ReviewProductContentViewHolder extends AbstractViewHolder<ReviewProductModelContent> {
    public static final int LAYOUT = R.layout.item_product_review;
    private static final int MAX_CHAR = 50;
    private static final String MORE_DESCRIPTION = "<font color='#42b549'>Selengkapnya</font>";
    public static final int UNLIKE_STATUS = 3;
    public static final int LIKE_STATUS_ACTIVE = 1;
    public static final String WIB = "WIB";
    public static final String TARGET = "WIB";

    boolean isReplyOpened = false;
    private ListenerReviewHolder viewListener;

    private TextView reviewerName;
    private TextView reviewTime;
    private RecyclerView reviewAttachment;
    private ImageView reviewOverflow;
    private TextView review;
    private RatingBar reviewStar;
    private ImageUploadAdapter adapter;

    private View replyReviewLayout;
    private TextView seeReplyText;
    private ImageView replyArrow;

    private TextView sellerName;
    private TextView sellerReplyTime;
    private TextView sellerReply;
    private ImageView replyOverflow;
    private ImageView iconLike;
    private TextView counterLike;
    private View containerReplyView;
    private View containerLike;

    public ReviewProductContentViewHolder(View itemView, ListenerReviewHolder viewListener) {
        super(itemView);
        this.viewListener = viewListener;
        reviewerName = (TextView) itemView.findViewById(R.id.reviewer_name);
        reviewTime = (TextView) itemView.findViewById(R.id.review_time);
        reviewAttachment = (RecyclerView) itemView.findViewById(R.id.product_review_image);
        reviewOverflow = (ImageView) itemView.findViewById(R.id.review_overflow);
        review = (TextView) itemView.findViewById(R.id.review);
        reviewStar = (RatingBar) itemView.findViewById(R.id.product_rating);
        adapter = ImageUploadAdapter.createAdapter(itemView.getContext());
        adapter.setCanUpload(false);
        adapter.setListener(onImageClicked());
        reviewAttachment.setLayoutManager(new LinearLayoutManager(itemView.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        reviewAttachment.setAdapter(adapter);
        seeReplyText = (TextView) itemView.findViewById(R.id.see_reply_button);
        replyArrow = (ImageView) itemView.findViewById(R.id.reply_chevron);

        replyReviewLayout = itemView.findViewById(R.id.reply_review_layout);
        sellerName = (TextView) itemView.findViewById(R.id.seller_reply_name);
        sellerReplyTime = (TextView) itemView.findViewById(R.id.seller_reply_time);
        sellerReply = (TextView) itemView.findViewById(R.id.seller_reply);
        replyOverflow = (ImageView) itemView.findViewById(R.id.reply_overflow);
        iconLike = itemView.findViewById(R.id.icon_like);
        counterLike = itemView.findViewById(R.id.text_counter_like);
        containerReplyView = itemView.findViewById(R.id.container_reply_view);
        containerLike = itemView.findViewById(R.id.container_like);
    }

    @Override
    public void bind(final ReviewProductModelContent element) {
        reviewerName.setText(getReviewerNameText(element));
        reviewerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!element.isReviewIsAnonymous() || element.isSellerRepliedOwner()) {
                    viewListener.onGoToProfile(element.getReviewerId(), getAdapterPosition());
                }
            }
        });
        containerReplyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onSeeReplied(getAdapterPosition());
                toggleReply();
            }
        });
        reviewTime.setText(TimeConverter.generateTimeYearly(element.getReviewTime().replace(WIB, "")));

        reviewStar.setRating(element.getReviewStar());
        review.setText(getReview(element.getReviewMessage()));
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (review.getText().toString().endsWith(MainApplication.getAppContext().getString(R.string.more_to_complete))) {
                    review.setText(element.getReviewMessage());
                }

            }
        });

        if (element.isReviewCanReported()) {
            reviewOverflow.setVisibility(View.VISIBLE);
            reviewOverflow.setOnClickListener(onReviewOverflowClicked(element));
        }else{
            reviewOverflow.setVisibility(View.GONE);
        }

        initReplyViewState();

        if (element.isReviewHasReplied()) {
            setSellerReply(element);
        } else {
            replyReviewLayout.setVisibility(View.GONE);
            seeReplyText.setVisibility(View.GONE);
            replyArrow.setVisibility(View.GONE);
        }

        containerLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onLikeDislikePressed(element.getReviewId(), element.isLikeStatus() ? UNLIKE_STATUS : LIKE_STATUS_ACTIVE, element.getProductId(),
                        element.isLikeStatus(), getAdapterPosition());
                element.setLikeStatus(!element.isLikeStatus());
                element.setTotalLike(element.isLikeStatus() ? element.getTotalLike() + 1 : element.getTotalLike() - 1);
                setLikeStatus(element);
            }
        });
        setLikeStatus(element);

        adapter.addList(convertToAdapterViewModel(element.getReviewAttachment()));
        adapter.notifyDataSetChanged();
    }

    void setLikeStatus(ReviewProductModelContent element) {
        if (element.isLikeStatus()) {
            iconLike.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_like_pressed));
        } else {
            iconLike.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_like_normal));
        }
        if (element.isLogin()) {
            if (element.isLikeStatus() && element.getTotalLike() > 1) {
                counterLike.setText(itemView.getContext().getString(R.string.product_review_label_counter_like_1_formatted, element.getTotalLike() - 1));
            } else if (element.isLikeStatus() && element.getTotalLike() == 1) {
                counterLike.setText(R.string.product_review_label_counter_like_2_formatted);
            } else if (!element.isLikeStatus() && element.getTotalLike() < 1) {
                counterLike.setText(R.string.product_review_label_counter_like_3_formatted);
            } else {
                counterLike.setText(itemView.getContext().getString(R.string.product_review_label_counter_like_4_formatted, element.getTotalLike()));
            }
        } else {
            counterLike.setText(itemView.getContext().getString(R.string.product_review_label_counter_like_4_formatted, element.getTotalLike()));
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

    private void setSellerReply(final ReviewProductModelContent element) {
        seeReplyText.setVisibility(View.VISIBLE);
        replyArrow.setVisibility(View.VISIBLE);

        sellerName.setText(element.getSellerName());
        sellerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToShopInfo(element.getShopId());
            }
        });
        sellerReplyTime.setText(TimeConverter.generateTimeYearly(element.getResponseCreateTime().replace(WIB, "")));
        sellerReply.setText(MethodChecker.fromHtml(element.getResponseMessage()));
        if (element.isSellerRepliedOwner()) {
            replyOverflow.setVisibility(View.VISIBLE);
            replyOverflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final PopupMenu popup = new PopupMenu(itemView.getContext(), v);
                    popup.getMenu().add(1, R.id.menu_delete, 1,
                            MainApplication.getAppContext()
                                    .getString(R.string.menu_delete));

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.menu_delete) {
                                viewListener.onDeleteReviewResponse(element, getAdapterPosition());
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });

                    popup.show();

                }
            });
        } else {
            replyOverflow.setVisibility(View.GONE);
        }
    }

    private void toggleReply() {
        isReplyOpened = !isReplyOpened;
        initReplyViewState();
    }

    private void initReplyViewState() {
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

    private String getFormattedTime(String reviewTime) {
        return TimeConverter.generateTimeYearly(reviewTime.replace(TARGET, ""));
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

    private String getReviewerNameText(ReviewProductModelContent element) {
        if (element.isReviewIsAnonymous() && !element.isSellerRepliedOwner()) {
            return getAnonymousName(element.getReviewerName());
        } else {
            return element.getReviewerName();
        }
    }

    private String getAnonymousName(String name) {
        String first = name.substring(0, 1);
        String last = name.substring(name.length() - 1);
        return first + "***" + last;
    }

    private View.OnClickListener onReviewOverflowClicked(final ReviewProductModelContent element) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onMenuClicked(getAdapterPosition());
                PopupMenu popup = new PopupMenu(itemView.getContext(), v);
                popup.getMenu().add(1, R.id.menu_report, 2, v.getContext().getString(R.string.menu_report));
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu_report) {
                            viewListener.onGoToReportReview(
                                    element.getShopId(),
                                    element.getReviewId(),
                                    getAdapterPosition()
                            );
                            return true;
                        }
                        return false;
                    }

                });
                popup.show();

            }
        };
    }

    public interface ListenerReviewHolder {
        void onGoToProfile(String reviewerId, int adapterPosition);

        void goToPreviewImage(int position, ArrayList<ImageUpload> list);

        void onGoToShopInfo(String shopId);

        void onDeleteReviewResponse(ReviewProductModelContent element, int adapterPosition);

        void onSmoothScrollToReplyView(int adapterPosition);

        void onGoToReportReview(String shopId, String reviewId, int adapterPosition);

        void onLikeDislikePressed(String reviewId, int likeStatus, String productId, boolean status, int adapterPosition);

        void onMenuClicked(int adapterPosition);

        void onSeeReplied(int adapterPosition);
    }
}
