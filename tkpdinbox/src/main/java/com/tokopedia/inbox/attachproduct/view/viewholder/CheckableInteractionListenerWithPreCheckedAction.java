package com.tokopedia.inbox.attachproduct.view.viewholder;

import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder;

/**
 * Created by Hendri on 26/02/18.
 */

public interface CheckableInteractionListenerWithPreCheckedAction extends BaseCheckableViewHolder.CheckableInteractionListener {
    boolean shouldAllowCheckChange(int position, boolean checked);
}
