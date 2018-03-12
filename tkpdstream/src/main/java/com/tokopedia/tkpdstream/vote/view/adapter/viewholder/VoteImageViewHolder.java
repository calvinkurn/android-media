package com.tokopedia.tkpdstream.vote.view.adapter.viewholder;

import android.content.Context;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.image.SquareImageView;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.chatroom.view.listener.GroupChatContract;
import com.tokopedia.tkpdstream.vote.view.model.VoteViewModel;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class VoteImageViewHolder extends AbstractViewHolder<VoteViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.vote_option_image;
    private GroupChatContract.View viewListener;
    private ProgressBar progressBar;
    private TextView option;
    private TextView percent;
    private SquareImageView imageView;
    private View icon;
    private View percentLayout;

    public VoteImageViewHolder(View itemView, GroupChatContract.View viewListener) {
        super(itemView);
        this.viewListener = viewListener;
        progressBar = itemView.findViewById(R.id.progress_bar);
        option = itemView.findViewById(R.id.text_view);
        imageView = itemView.findViewById(R.id.imageView);
        percent = itemView.findViewById(R.id.percent);
        percentLayout = itemView.findViewById(R.id.percent_layout);
        icon = itemView.findViewById(R.id.icon);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            params.height = itemView.getContext().getResources().getDimensionPixelSize(R.dimen
                    .vote_icon_size);
            imageView.setLayoutParams(params);
        }
    }

    @Override
    public void bind(final VoteViewModel element) {
        Context context = itemView.getContext();
        if (element.getSelected() == VoteViewModel.DEFAULT) {
            percent.setVisibility(View.GONE);
            percentLayout.setVisibility(View.GONE);
            progressBar.setProgress(0);
            progressBar.setProgressDrawable(MethodChecker.getDrawable(context, R.drawable.vote_option_image_default));
            icon.setVisibility(View.GONE);
        } else {
            percent.setVisibility(View.VISIBLE);
            percentLayout.setVisibility(View.VISIBLE);
            progressBar.setProgress(element.getPercentageInteger());
            if (element.getSelected() == VoteViewModel.SELECTED) {
                icon.setVisibility(View.VISIBLE);
                progressBar.setProgressDrawable(MethodChecker.getDrawable(context, R.drawable.vote_option_image_selected));
            } else if (element.getSelected() == VoteViewModel.UNSELECTED) {
                icon.setVisibility(View.GONE);
                progressBar.setProgressDrawable(MethodChecker.getDrawable(context, R.drawable.vote_option_image_unselected));
            }
        }

        option.setText(element.getOption());
        percent.setText(element.getPercentage());
//        ImageHandler.loadImageWithTarget(imageView.getContext(), element.getUrl(), new SimpleTarget<Bitmap>() {
//
//            @Override
//            public void onResourceReady(Bitmap arg0, GlideAnimation<? super Bitmap> arg1) {
//                imageView.setImageBitmap(arg0);
//            }
//        });
        ImageHandler.LoadImage(imageView, element.getUrl());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onVoteOptionClicked(element);
            }
        });
    }
}
