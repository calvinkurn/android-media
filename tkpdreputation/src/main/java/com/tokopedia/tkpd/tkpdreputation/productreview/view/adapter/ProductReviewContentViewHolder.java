package com.tokopedia.tkpd.tkpdreputation.productreview.view.adapter;

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
import com.tokopedia.abstraction.common.utils.MethodChecker;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.util.TimeConverter;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.ImageUploadAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageAttachmentViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;
import com.tokopedia.tkpd.tkpdreputation.productreview.view.presenter.ProductReviewContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ProductReviewContentViewHolder extends AbstractViewHolder<ProductReviewModelContent> {
    public static final int LAYOUT = R.layout.item_product_review;
    private static final int MAX_CHAR = 50;
    private static final String MORE_DESCRIPTION = "<font color='#42b549'>Selengkapnya</font>";
    private static final String BY = "Oleh";

    boolean isReplyOpened = false;
    private ProductReviewContract.View viewListener;

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

    public ProductReviewContentViewHolder(View itemView, ProductReviewContract.View viewListener) {
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
    }

    @Override
    public void bind(final ProductReviewModelContent element) {

        reviewerName.setText(com.tokopedia.core.util.MethodChecker.fromHtml(getReviewerNameText(element)));
        reviewerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToProfile(element.getReviewerId());
            }
        });

        String time = getFormattedTime(element.getReviewTime());
        reviewTime.setText(time);

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

        reviewOverflow.setOnClickListener(onReviewOverflowClicked(element));


        if (element.isReviewHasReplied()) {
            setSellerReply(element);
        } else {
            replyReviewLayout.setVisibility(View.GONE);
            seeReplyText.setVisibility(View.GONE);
            replyArrow.setVisibility(View.GONE);
        }

        if(element.isLikeStatus()){
            iconLike.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_like_pressed));
        }else{
            iconLike.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_like_normal));
        }
        counterLike.setText(String.valueOf(element.getTotalLike()));

        adapter.addList(convertToAdapterViewModel(element.getReviewAttachment()));
        adapter.notifyDataSetChanged();
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

    private void setSellerReply(final ProductReviewModelContent element) {
        replyReviewLayout.setVisibility(View.VISIBLE);
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

        sellerName.setText(MethodChecker.fromHtml(getFormattedReplyName(element.getSellerName())));
        sellerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToShopInfo(element.getShopId());
            }
        });
        sellerReplyTime.setText(getFormattedTime(element.getResponseCreateTime()));
        sellerReply.setText(MethodChecker.fromHtml(element.getResponseMessage()));
        replyOverflow.setVisibility(View.VISIBLE);
        replyOverflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (element.isSellerRepliedOwner()) {
                    final PopupMenu popup = new PopupMenu(itemView.getContext(), v);
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

            }
        });
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

    private String getReviewerNameText(ProductReviewModelContent element) {
        if (element.isReviewIsAnonymous()) {
            return getString(R.string.by) + " " +
                    getAnonymousName(element.getReviewerName());
        } else {
            return getString(R.string.by) + " " + element.getReviewerName();
        }
    }

    private String getAnonymousName(String name) {
        String first = name.substring(0, 1);
        String last = name.substring(name.length() - 1);
        return first + "***" + last;
    }

    private View.OnClickListener onReviewOverflowClicked(final ProductReviewModelContent element) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (element.isReviewCanReported()) {
                    PopupMenu popup = new PopupMenu(itemView.getContext(), v);
                    popup.getMenu().add(1, R.id.menu_report, 2, MainApplication.getAppContext()
                            .getString(R.string.menu_report));
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.menu_report) {
                                viewListener.onGoToReportReview(
                                        element.getShopId(),
                                        element.getReviewId()
                                );
                                return true;
                            } else {
                                return false;
                            }
                        }

                    });
                    popup.show();
                }

            }
        };
    }
}
