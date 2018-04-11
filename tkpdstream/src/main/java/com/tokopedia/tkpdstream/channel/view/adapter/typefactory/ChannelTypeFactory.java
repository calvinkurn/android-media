package com.tokopedia.tkpdstream.channel.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdstream.channel.view.adapter.viewholder.ChannelViewHolder;
import com.tokopedia.tkpdstream.channel.view.model.ChannelViewModel;


/**
 * @author by StevenFredian on 13/02/18.
 */

public class ChannelTypeFactory extends BaseAdapterTypeFactory implements AdapterTypeFactory{

    public int type(ChannelViewModel channelViewModel) {
        return ChannelViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if(type == ChannelViewHolder.LAYOUT){
            return new ChannelViewHolder(parent);
        }else {
            return super.createViewHolder(parent, type);
        }
    }
}

