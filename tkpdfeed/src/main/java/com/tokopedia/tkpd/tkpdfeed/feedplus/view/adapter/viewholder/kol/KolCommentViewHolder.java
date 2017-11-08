package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.kol;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.KolComment;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolCommentViewModel;

/**
 * @author by nisie on 10/31/17.
 */

public class KolCommentViewHolder extends AbstractViewHolder<KolCommentViewModel> {


    public static final int LAYOUT = R.layout.kol_comment_item;
    private static final String SPACE = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
    private final KolComment.View viewListener;

    TextView comment;
    TextView time;
    ImageView avatar;
    ImageView badge;

    public KolCommentViewHolder(View itemView, KolComment.View viewListener) {
        super(itemView);
        this.viewListener = viewListener;
        avatar = (ImageView) itemView.findViewById(R.id.avatar);
        time = (TextView) itemView.findViewById(R.id.time);
        comment = (TextView) itemView.findViewById(R.id.comment);
        badge = (ImageView) itemView.findViewById(R.id.badge);

    }

    @Override
    public void bind(final KolCommentViewModel element) {
        ImageHandler.LoadImage(avatar, element.getAvatarUrl());
        time.setText(element.getTime());

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToProfile(element.getUrl());
            }
        });

        if (element.isOfficial()) {
            badge.setVisibility(View.VISIBLE);
            comment.setText(MethodChecker.fromHtml(SPACE + getCommentText(element)));
        } else {
            badge.setVisibility(View.GONE);
            comment.setText(MethodChecker.fromHtml(getCommentText(element)));
        }

    }

    private String getCommentText(KolCommentViewModel element) {
        return "<b>" + element.getName() + "</b>" + " " + element.getReview();
    }
}
