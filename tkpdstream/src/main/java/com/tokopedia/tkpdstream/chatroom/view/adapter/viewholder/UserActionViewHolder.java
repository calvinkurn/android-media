package com.tokopedia.tkpdstream.chatroom.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.UserActionViewModel;

/**
 * @author by nisie on 2/22/18.
 */

public class UserActionViewHolder extends AbstractViewHolder<UserActionViewModel> {

    TextView actionMessage;
    ImageView avatar;
    Context context;

    @LayoutRes
    public static final int LAYOUT = R.layout.user_action_view_holder;

    public UserActionViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        actionMessage = itemView.findViewById(R.id.action_message);
        avatar = itemView.findViewById(R.id.avatar);
    }

    @Override
    public void bind(UserActionViewModel element) {

        ImageHandler.loadImageCircle2(avatar.getContext(), avatar, element.getAvatarUrl());
        String name = MethodChecker.fromHtml(element.getUserName()).toString();
        String action = getActionString(context, element.getActionType());

        String actionString = name + " " + action;
        actionMessage.setText(actionString);

    }

    private String getActionString(Context context, int actionType) {
        switch (actionType) {
            case UserActionViewModel.ACTION_ENTER:
                return context.getString(R.string.groupchat_action_enter);
            default:
                return "";
        }
    }
}
