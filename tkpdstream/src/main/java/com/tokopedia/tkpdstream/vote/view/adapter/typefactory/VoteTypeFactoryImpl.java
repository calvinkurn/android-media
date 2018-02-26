package com.tokopedia.tkpdstream.vote.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdstream.chatroom.view.fragment.GroupChatFragment;
import com.tokopedia.tkpdstream.vote.view.adapter.viewholder.VoteBarViewHolder;
import com.tokopedia.tkpdstream.vote.view.adapter.viewholder.VoteImageViewHolder;
import com.tokopedia.tkpdstream.vote.view.model.VoteViewModel;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class VoteTypeFactoryImpl extends BaseAdapterTypeFactory implements VoteTypeFactory{

    public VoteTypeFactoryImpl(GroupChatFragment context) {
    }

    public int type(VoteViewModel voteViewModel) {
        if(voteViewModel.getType() == VoteViewModel.IMAGE_TYPE) {
            return VoteImageViewHolder.LAYOUT;
        }else{
            return VoteBarViewHolder.LAYOUT;
        }
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if(type == VoteBarViewHolder.LAYOUT){
            return new VoteBarViewHolder(parent);
        }else if(type == VoteImageViewHolder.LAYOUT){
            return new VoteImageViewHolder(parent);
        }else {
            return super.createViewHolder(parent, type);
        }
    }
}
