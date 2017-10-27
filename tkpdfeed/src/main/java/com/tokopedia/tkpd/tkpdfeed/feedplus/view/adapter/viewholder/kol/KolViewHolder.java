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
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.customview.TooltipImageView;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolViewModel;

/**
 * @author by nisie on 10/27/17.
 */

public class KolViewHolder extends AbstractViewHolder<KolViewModel> {

    private static final int MAX_CHAR = 250;
    private final FeedPlus.View.Kol viewListener;
    private TextView title;
    private TextView name;
    private ImageView avatar;
    private TextView label;
    private ImageView followIcon;
    private TextView followText;
    private View followButton;
    private TooltipImageView tooltipImageView;
    private TextView kolText;
    private ImageView likeIcon;
    private TextView likeText;
    private ImageView commentIcon;
    private TextView commentText;

    @LayoutRes
    public static final int LAYOUT = R.layout.kol_layout;

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
        tooltipImageView = (TooltipImageView) itemView.findViewById(R.id.tooltip_image);
        kolText = (TextView) itemView.findViewById(R.id.kol_text);
        likeIcon = (ImageView) itemView.findViewById(R.id.like_icon);
        likeText = (TextView) itemView.findViewById(R.id.like_text);
        commentIcon = (ImageView) itemView.findViewById(R.id.comment_icon);
        commentText = (TextView) itemView.findViewById(R.id.comment_text);
    }

    @Override
    public void bind(KolViewModel element) {
        title.setText(MethodChecker.fromHtml(element.getTitle()));
        name.setText(MethodChecker.fromHtml(element.getName()));
        ImageHandler.LoadImage(avatar, element.getAvatar());
        label.setText(element.getLabel());

        if (element.isFollowed() && !element.isTemporarilyFollowed()) {
            followButton.setVisibility(View.GONE);
        } else if (element.isFollowed() && element.isTemporarilyFollowed()) {
            followButton.setVisibility(View.VISIBLE);
            followText.setText(R.string.following);
            ImageHandler.loadImageWithIdWithoutPlaceholder(followIcon, R.drawable.ic_tick);
        } else {
            followButton.setVisibility(View.VISIBLE);
            ImageHandler.loadImageWithIdWithoutPlaceholder(followIcon, R.drawable.ic_plus_green);
            followText.setText(R.string.action_follow);
        }

        tooltipImageView.setImageTooltip(element.getProductImage(), element.getProductTooltip(),
                onTooltipClicked(element));
        kolText.setText(getKolText(element.getReview()));

        if (element.isLiked()) {
            ImageHandler.loadImageWithIdWithoutPlaceholder(likeIcon, R.drawable.ic_icon_repsis_like_active);
            likeText.setText(element.getTotalLike());
        } else {
            ImageHandler.loadImageWithIdWithoutPlaceholder(likeIcon, R.drawable.ic_icon_repsis_like);
            likeText.setText(R.string.action_like);
        }

        commentText.setText(element.getTotalComment());

        setListener(element);
    }

    private View.OnClickListener onTooltipClicked(final KolViewModel element) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToProductPageFromKol(element.getPage(),
                        getAdapterPosition(),
                        element.getProductId());
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
                viewListener.onGoToKolComment(element.getPage(), getAdapterPosition(), element
                        .getId());
            }
        });

        commentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToKolComment(element.getPage(), getAdapterPosition(), element
                        .getId());
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

    private Spanned getKolText(String review) {
        if (MethodChecker.fromHtml(review).length() > MAX_CHAR) {
            String subDescription = MethodChecker.fromHtml(review).toString().substring(0, MAX_CHAR);
            return MethodChecker
                    .fromHtml(subDescription.replaceAll("(\r\n|\n)", "<br />") + "... "
                            + MainApplication.getAppContext().getString(R.string
                            .read_more_english));
        } else {
            return MethodChecker.fromHtml(review);
        }
    }


}
