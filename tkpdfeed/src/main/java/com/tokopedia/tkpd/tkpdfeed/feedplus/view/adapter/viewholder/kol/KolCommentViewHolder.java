package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.kol;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.KolComment;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolCommentViewModel;

import javax.inject.Inject;

/**
 * @author by nisie on 10/31/17.
 */

public class KolCommentViewHolder extends AbstractViewHolder<KolCommentViewModel> {


    @Inject
    SessionHandler sessionHandler;

    public static final int LAYOUT = R.layout.kol_comment_item;
    private static final String SPACE = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
    private final KolComment.View viewListener;

    View mainView;
    TextView comment;
    TextView time;
    ImageView avatar;
    ImageView badge;

    public KolCommentViewHolder(View itemView, final KolComment.View viewListener) {
        super(itemView);
        this.viewListener = viewListener;
        avatar = (ImageView) itemView.findViewById(R.id.avatar);
        time = (TextView) itemView.findViewById(R.id.time);
        comment = (TextView) itemView.findViewById(R.id.comment);
        badge = (ImageView) itemView.findViewById(R.id.badge);
        mainView = itemView.findViewById(R.id.main_view);
    }

    @Override
    public void bind(final KolCommentViewModel element) {
        ImageHandler.LoadImage(avatar, element.getAvatarUrl());
        time.setText(element.getTime());

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(element.getUrl())) {
                    viewListener.onGoToProfile(element.getUrl());
                }
            }
        });

        if (element.isOfficial()) {
            badge.setVisibility(View.VISIBLE);
            comment.setText(MethodChecker.fromHtml(SPACE + getCommentText(element)));
        } else {
            badge.setVisibility(View.GONE);
            comment.setText(MethodChecker.fromHtml(getCommentText(element)));
        }

        mainView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (element.canDeleteComment()) {
                    showDeleteDialog(v, viewListener, element.getId(), getAdapterPosition());
                    return true;
                } else {
                    return false;
                }
            }
        });

    }

    private String getCommentText(KolCommentViewModel element) {
        return "<b>" + element.getName() + "</b>" + " " + element.getReview();
    }

    private void showDeleteDialog(View v, final KolComment.View viewListener, final int id, final int adapterPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

        builder.setMessage(R.string.prompt_delete_comment_kol);
        builder.setPositiveButton(R.string.title_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewListener.onDeleteCommentKol(id, adapterPosition);
            }
        });
        builder.setNegativeButton(R.string.title_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
