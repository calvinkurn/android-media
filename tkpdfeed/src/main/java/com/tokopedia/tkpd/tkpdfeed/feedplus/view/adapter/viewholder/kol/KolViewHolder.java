package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.kol;

import android.support.annotation.LayoutRes;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolViewModel;

/**
 * @author by nisie on 10/27/17.
 */

public class KolViewHolder extends AbstractViewHolder<KolViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.kol_layout;

    private static final int MAX_CHAR = 250;
    private final FeedPlus.View.Kol viewListener;
    private TextView title;
    private TextView name;
    private ImageView avatar;
    private TextView label;
    private ImageView followIcon;
    private TextView followText;
    private View followButton;
    private ImageView reviewImage;
    private TextView tooltip;
    private View tooltipClickArea;
    private TextView kolText;
    private ImageView likeIcon;
    private TextView likeText;
    private TextView commentText;
    private View topSeparator;
    private View commentButton;
    private View likeButton;

    public KolViewHolder(View itemView, FeedPlus.View.Kol viewListener) {
        super(itemView);
        this.viewListener = viewListener;
        title = (TextView) itemView.findViewById(R.id.title);
        name = (TextView) itemView.findViewById(R.id.name);
        avatar = (ImageView) itemView.findViewById(R.id.avatar);
        label = (TextView) itemView.findViewById(R.id.label);
        followIcon = (ImageView) itemView.findViewById(R.id.follow_icon);
        followText = (TextView) itemView.findViewById(R.id.follow_text);
        followButton = itemView.findViewById(R.id.follow_button);
        reviewImage = (ImageView) itemView.findViewById(R.id.image);
        tooltip = (TextView) itemView.findViewById(R.id.tooltip);
        tooltipClickArea = itemView.findViewById(R.id.tooltip_area);
        kolText = (TextView) itemView.findViewById(R.id.kol_text);
        likeIcon = (ImageView) itemView.findViewById(R.id.like_icon);
        likeText = (TextView) itemView.findViewById(R.id.like_text);
        commentText = (TextView) itemView.findViewById(R.id.comment_text);
        topSeparator = itemView.findViewById(R.id.separator);
        commentButton = itemView.findViewById(R.id.comment_button);
        likeButton = itemView.findViewById(R.id.like_button);
    }

    @Override
    public void bind(KolViewModel element) {
        if (TextUtils.isEmpty(element.getTitle())) {
            title.setVisibility(View.GONE);
        } else {
            title.setVisibility(View.VISIBLE);
            title.setText(MethodChecker.fromHtml(element.getTitle()));
        }
        name.setText(MethodChecker.fromHtml(element.getName()));
        ImageHandler.LoadImage(avatar, element.getAvatar());

        if (element.isFollowed()) {
            label.setText(element.getTime());
        } else {
            label.setText(element.getLabel());
        }

        if (element.isFollowed() && !element.isTemporarilyFollowed()) {
            followButton.setVisibility(View.GONE);
            topSeparator.setVisibility(View.GONE);
        } else if (element.isFollowed() && element.isTemporarilyFollowed()) {
            followButton.setVisibility(View.VISIBLE);
            followText.setText(R.string.following);
            followText.setTextColor(MethodChecker.getColor(MainApplication.getAppContext(),
                    R.color.black_54));
            ImageHandler.loadImageWithIdWithoutPlaceholder(followIcon, R.drawable.ic_tick);
            topSeparator.setVisibility(View.VISIBLE);
        } else {
            followButton.setVisibility(View.VISIBLE);
            ImageHandler.loadImageWithIdWithoutPlaceholder(followIcon, R.drawable.ic_plus_green);
            followText.setTextColor(MethodChecker.getColor(MainApplication.getAppContext(),
                    R.color.green_500));
            followText.setText(R.string.action_follow_english);
            topSeparator.setVisibility(View.VISIBLE);
        }

        ImageHandler.LoadImage(reviewImage, element.getKolImage());
        tooltip.setText(element.getProductTooltip());

        kolText.setText(getKolText(element));

        if (element.isLiked()) {
            ImageHandler.loadImageWithIdWithoutPlaceholder(likeIcon, R.drawable.ic_thumb_green);
            likeText.setText(String.valueOf(element.getTotalLike()));
            likeText.setTextColor(MethodChecker.getColor(MainApplication.getAppContext(), R.color
                    .tkpd_main_green));
        } else if (element.getTotalLike() > 0) {
            ImageHandler.loadImageWithIdWithoutPlaceholder(likeIcon, R.drawable.ic_thumb);
            likeText.setText(String.valueOf(element.getTotalLike()));
            likeText.setTextColor(MethodChecker.getColor(MainApplication.getAppContext(), R.color
                    .black_54));
        } else {
            ImageHandler.loadImageWithIdWithoutPlaceholder(likeIcon, R.drawable.ic_thumb);
            likeText.setText(R.string.action_like);
            likeText.setTextColor(MethodChecker.getColor(MainApplication.getAppContext(), R.color
                    .black_54));
        }

        if (element.getTotalComment() == 0) {
            commentText.setText(R.string.comment);
        } else {
            commentText.setText(String.valueOf(element.getTotalComment()));
        }

        setListener(element);
    }

    private void setListener(final KolViewModel element) {
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToKolProfile(element.getPage(), getAdapterPosition(), element
                        .getKolProfileUrl());
            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToKolProfile(element.getPage(), getAdapterPosition(), element
                        .getKolProfileUrl());
            }
        });

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (element.isFollowed()) {
                    UnifyTracking.eventKolContentUnfollowClick(element.getTagsType());
                    viewListener.onUnfollowKolClicked(element.getPage(), getAdapterPosition(),
                            element.getUserId());
                } else {
                    UnifyTracking.eventKolContentFollowClick(element.getTagsType());
                    viewListener.onFollowKolClicked(element.getPage(), getAdapterPosition(),
                            element.getUserId());
                }
            }
        });

        kolText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kolText.getText().toString().endsWith(MainApplication.getAppContext().getString(R
                        .string.read_more_english))) {
                    kolText.setText(element.getReview());
                    element.setReviewExpanded(true);
                }
            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (element.isLiked()) {
                    viewListener.onUnlikeKolClicked(element.getPage(), getAdapterPosition(),
                            element.getId());
                } else {
                    viewListener.onLikeKolClicked(element.getPage(), getAdapterPosition(), element.getId());
                }
            }
        });

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToKolComment(element.getPage(), getAdapterPosition(), element);
            }
        });

        tooltipClickArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onOpenKolTooltip(element.getPage(), getAdapterPosition(),
                        element.getContentLink());
            }
        });

    }

    private Spanned getKolText(KolViewModel element) {
        if (!element.isReviewExpanded() && MethodChecker.fromHtml(element.getReview()).length() >
                MAX_CHAR) {
            String subDescription = MethodChecker.fromHtml(element.getReview()).toString().substring(0,
                    MAX_CHAR);
            return MethodChecker.fromHtml(
                    subDescription.replaceAll("(\r\n|\n)", "<br />") + "... "
                            + "<font color='#42b549'>"
                            + MainApplication.getAppContext().getString(R.string.read_more_english)
                            + "</font>");
        } else {
            return MethodChecker.fromHtml(element.getReview());
        }
    }

}
