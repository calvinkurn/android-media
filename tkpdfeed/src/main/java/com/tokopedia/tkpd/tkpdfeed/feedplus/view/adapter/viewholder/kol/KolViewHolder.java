package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.kol;

import android.support.annotation.LayoutRes;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
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
    private ImageView commentIcon;
    private TextView commentText;
    View topSeparator;

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
        commentIcon = (ImageView) itemView.findViewById(R.id.comment_icon);
        commentText = (TextView) itemView.findViewById(R.id.comment_text);
        topSeparator = itemView.findViewById(R.id.separator);
    }

    @Override
    public void bind(KolViewModel element) {
        title.setText(MethodChecker.fromHtml(element.getTitle()));
        name.setText(MethodChecker.fromHtml(element.getName()));
        ImageHandler.LoadImage(avatar, element.getAvatar());
        label.setText(element.getLabel());

        if (element.isFollowed() && !element.isTemporarilyFollowed()) {
            followButton.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
            topSeparator.setVisibility(View.GONE);
        } else if (element.isFollowed() && element.isTemporarilyFollowed()) {
            followButton.setVisibility(View.VISIBLE);
            followText.setText(R.string.following);
            followText.setTextColor(MethodChecker.getColor(MainApplication.getAppContext(),
                    R.color.black_54));
            ImageHandler.loadImageWithIdWithoutPlaceholder(followIcon, R.drawable.ic_tick);
            title.setVisibility(View.VISIBLE);
            topSeparator.setVisibility(View.VISIBLE);
        } else {
            followButton.setVisibility(View.VISIBLE);
            ImageHandler.loadImageWithIdWithoutPlaceholder(followIcon, R.drawable.ic_plus_green);
            followText.setTextColor(MethodChecker.getColor(MainApplication.getAppContext(),
                    R.color.green_500));
            followText.setText(R.string.action_follow_english);
            title.setVisibility(View.VISIBLE);
            topSeparator.setVisibility(View.VISIBLE);
        }

        ImageHandler.LoadImage(reviewImage, element.getProductImage());
        tooltip.setText(element.getProductTooltip());

        kolText.setText(getKolText(element));

        if (element.isLiked()) {
            ImageHandler.loadImageWithIdWithoutPlaceholder(likeIcon, R.drawable.ic_icon_repsis_like_active);
            likeText.setText(String.valueOf(element.getTotalLike()));
        } else {
            ImageHandler.loadImageWithIdWithoutPlaceholder(likeIcon, R.drawable.ic_icon_repsis_like);
            likeText.setText(R.string.action_like);
        }

        if(element.getTotalComment() == 0){
            commentText.setText(R.string.comment);
        }else{
            commentText.setText(String.valueOf(element.getTotalComment()));
        }

        setListener(element);
    }

    private View.OnClickListener onTooltipClicked(final KolViewModel element) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToProductPageFromKol(element.getPage(),
                        getAdapterPosition(),
                        element.getProductId(), element.getProductImage());
            }
        };
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
                if (followText.getText().equals(MainApplication.getAppContext().getString(R.string
                        .action_follow_english))) {
                    viewListener.onFollowKolClicked(element.getPage(), getAdapterPosition(), element.getId());
                } else {
                    viewListener.onUnfollowKolClicked(element.getPage(), getAdapterPosition(), element.getId());
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

        likeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLikeClicked(element);
            }
        });

        likeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLikeClicked(element);
            }
        });

        commentIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToKolComment(element.getPage(), getAdapterPosition(), element);
            }
        });

        commentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToKolComment(element.getPage(), getAdapterPosition(), element);
            }
        });

        tooltipClickArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToProductPageFromKol(element.getPage(), getAdapterPosition(),
                        String.valueOf(element.getId()),
                        element.getProductImage());
            }
        });

    }

    private void onLikeClicked(KolViewModel element) {
        if (element.isLiked()) {
            viewListener.onUnlikeKol(element.getPage(), getAdapterPosition(), element.getId());
        } else {
            viewListener.onLikeKol(element.getPage(), getAdapterPosition(), element.getId());
        }

    }

    private Spanned getKolText(KolViewModel element) {
        if (!element.isReviewExpanded() && MethodChecker.fromHtml(element.getReview()).length() >
                MAX_CHAR) {
            String subDescription = MethodChecker.fromHtml(element.getReview()).toString().substring(0,
                    MAX_CHAR);
            return MethodChecker
                    .fromHtml(subDescription.replaceAll("(\r\n|\n)", "<br />") + "... "
                            + MainApplication.getAppContext().getString(R.string
                            .read_more_english));
        } else {
            return MethodChecker.fromHtml(element.getReview());
        }
    }


}
