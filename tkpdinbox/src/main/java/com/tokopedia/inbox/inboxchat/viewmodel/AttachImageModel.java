package com.tokopedia.inbox.inboxchat.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactory;

/**
 * Created by stevenfredian on 11/28/17.
 */

public class AttachImageModel implements Visitable<ChatRoomTypeFactory> {

    String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int type(ChatRoomTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }

}
