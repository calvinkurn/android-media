package com.tokopedia.tkpdstream.chatroom.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.AdminAnnouncementViewModel;

/**
 * @author by nisie on 2/7/18.
 */

public class AdminAnnouncementViewHolder extends AbstractViewHolder<AdminAnnouncementViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.admin_announcement_view_holder;

    public AdminAnnouncementViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(AdminAnnouncementViewModel element) {

    }
}
