package com.tokopedia.inbox.inboxchat.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.helper.ChatGlideImageRequestListener;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.imageannouncement.ImageAnnouncementViewModel;

/**
 * @author by nisie on 5/15/18.
 */
public class ImageAnnouncementViewHolder extends BaseChatViewHolder<ImageAnnouncementViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_image_announcement;

    private ImageView attachment;

    public ImageAnnouncementViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView, viewListener);
        attachment = itemView.findViewById(R.id.image);

    }

    @Override
    public void bind(final ImageAnnouncementViewModel viewModel) {
        super.bind(viewModel);

        view.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        view.getLayoutParams().height = view.getLayoutParams().width;

        ImageHandler.loadImageChat(attachment, viewModel.getImageUrl(), new
                ChatGlideImageRequestListener());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(viewModel.getRedirectUrl())) {
                    viewListener.onGoToWebView(viewModel.getRedirectUrl(),
                            viewModel.getAttachmentId());
                }
            }
        });
    }
}
